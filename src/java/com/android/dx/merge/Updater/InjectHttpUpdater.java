
package com.android.dx.merge.Updater;

import static schilling.richard.r3.app.DebugConstants.DO_LOG;

import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;

import com.android.dx.io.MethodId;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.merge.DexInjectMerger.ClassMethodPair;
import com.android.dx.merge.MethodDefinition;

public class InjectHttpUpdater extends BaseUpdater {

    private MethodId statusLineMethodId = null;

    public InjectHttpUpdater(MethodId mId, List<ClassMethodPair> methodList) {
        super(methodList);

        this.order = Integer.MAX_VALUE;

        statusLineMethodId = mId;
    }

    /**
     * The return statement is removed, and the first instruction is set with
     * the value returned from the call to StatusLine.getStatusCode().
     */
    @Override
    public void preInjectUpdate(MethodDefinition target, int address,
            MethodDefinition injected) {

        MethodId methodId = target.methodId();
        int declaringClassIndex = methodId.getDeclaringClassIndex();
        int methodIndex = methodId.getMethodIdIndex();

        if (DO_LOG) {
            if (LOG_UPDATES)
                Log.i(BaseUpdater.LOG_TAG,
                        String.format(
                                "setting parameters to report class id %d, method id %d, and the http response code",
                                methodId.getDeclaringClassIndex(),
                                methodId.getMethodIdIndex()));
        }

        injected.removeReturnInstruction();

        InstructionList instructionsToInject = injected
                .instructionsDecodedAsList();

        // instructionsToInject is a copy of underlying instruction short
        // array.

        // position 0: invoke-virtual
        // position 1: move result
        // position 2: move register (to modify)

        // remove the first two instructions
        /*
         * DecodedInstruction NOP = DecodedInstructionFactory.NOP(address);
         * NOP.setMethodDef(injected); instructionsToInject.setValueAt(0, NOP);
         * NOP = DecodedInstructionFactory.NOP(address);
         * NOP.setMethodDef(injected); instructionsToInject.setValueAt(1, NOP);
         */

        // instruction 3 contains the class def literal
        instructionsToInject.valueAt(3).setLiteral(
                (long) (declaringClassIndex & 0xff));

        // instruction 4 contains the method def literal.
        instructionsToInject.valueAt(4).setLiteral((long) (methodIndex & 0xff));

        instructionsToInject.removeAt(0);
        instructionsToInject.removeAt(1);

        instructionsToInject.recalibrateAddresses();

        injected.setInstructions(injected.getRegistersSize(),
                instructionsToInject);

    }

    @Override
    public List<Integer> findInjectPosition(MethodDefinition target) {
        List<Integer> result = new LinkedList<Integer>();
        InstructionList targetInstructions = target.instructionsDecodedAsList();

        for (int i = 0; i < targetInstructions.size(); i++) {
            DecodedInstruction instruction = targetInstructions.valueAt(i);

            if (instruction.isInvoke()) {
                int bId = instruction.getIndex();
                int statusLineId = statusLineMethodId.getMethodIdIndex();
                if (bId == statusLineId)
                    // inject at two instructions later.
                    result.add(i + 2);
            }

        }

        return result;

    }

    @Override
    public MethodDefinition getMethodToInject() {
        return BaseUpdater.injectHttpMethod;
    }

    /**
     * Returns true because this inject update will always happen after
     * InjectedUpdated or InjectStaticUpdater.
     */
    @Override
    public boolean reuseLocalRegisters() {

        return true;
    }

    @Override
    public boolean observeRegisterLimits() {
        return false;
    }

    @Override
    public void postInjectUpdate(MethodDefinition target, int position) {

        InstructionList targetInstructions = target.instructionsDecodedAsList();

        // position is one instruction after the move result

        // get the register value where the statusCode parameter is stored.
        DecodedInstruction moveInstruction = targetInstructions
                .valueAt(position);

        int vDst = moveInstruction.getA();

        // get the register where the result to getStatusCode is stored.
        int moveResultPosition = position - 1;
        DecodedInstruction moveResultInstruction = targetInstructions
                .valueAt(moveResultPosition);
        if (!moveResultInstruction.isMoveResult())
            throw new IllegalStateException(
                    String.format(
                            "Expecting a move result instruciton at position %d but foud %s",
                            moveResultPosition,
                            moveResultInstruction.toLongString()));
        int vSrc = moveResultInstruction.getA();

        moveInstruction.setB(vSrc);

        // create an instruction to move the status code into the parameter
        // register.
        /*
         * DecodedInstruction moveInstruction = DecodedInstructionFactory.MOVE(
         * targetInstructions.keyAt(position), vDst, vSrc);
         * moveInstruction.setMethodDef(target);
         */

        // replace the filler operation.
        // targetInstructions.setValueAt(position, moveInstruction);

        if (DO_LOG) {
            if (LOG_UPDATES)
                Log.i(BaseUpdater.LOG_TAG, String.format(
                        "copying getStatusCode() result v%d<-v%d", vDst, vSrc));
        }

        target.setInstructions(target.getRegistersSize(), targetInstructions);
    }

}
