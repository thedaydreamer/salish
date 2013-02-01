
package com.android.dx.merge.Updater;

import static schilling.richard.r3.app.DebugConstants.DO_LOG;

import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;

import com.android.dx.io.MethodId;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.DecodedInstructionFactory;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.merge.DexInjectMerger.ClassMethodPair;
import com.android.dx.merge.MethodDefinition;

/**
 * Use for inject non-static function. Strips the return statement from injected
 * and updates the registers with method id and class id values.
 * 
 * @author rschilling
 */
public class InjectUpdater extends BaseUpdater {

    public InjectUpdater(List<ClassMethodPair> methodList) {
        super(methodList);

    }

    /**
     * The return instruction is removed from injected and the literal values of
     * the first two instructions are updated with literal constants which are
     * the class id and the method id.
     */
    @Override
    public void preInjectUpdate(MethodDefinition target, int address,
            MethodDefinition injected) {

        MethodId methodId = target.methodId();
        int declaringClassIndex = methodId.getDeclaringClassIndex();
        int methodIndex = methodId.getMethodIdIndex();
        long httpStatus = target.callsHttpExecute() ? 1 : 0;

        if (DO_LOG) {
            if (LOG_UPDATES)
                Log.i(BaseUpdater.LOG_TAG, String.format(
                        "setting parameters to report class id %d, method id %d.",
                        methodId.getDeclaringClassIndex(),
                        methodId.getMethodIdIndex()));
        }

        methodList().add(new ClassMethodPair(declaringClassIndex, methodIndex, 0));

        /* replace the return instruction with NOP. */
        injected.removeReturnInstruction();

        InstructionList instructionsToInject = injected
                .instructionsDecodedAsList();

        // instruction 1 contains the class def literal
        instructionsToInject.valueAt(1).setLiteral(
                (long) (declaringClassIndex & 0xff));

        // instruction 2 contains the method def literal.
        instructionsToInject.valueAt(2).setLiteral((long) (methodIndex & 0xff));

        // instruction 3 contains flag information to be read by the server.
        instructionsToInject.valueAt(3).setLiteral((long) (httpStatus & 0xff));

        instructionsToInject.convert();

        // save to use MAX_VALUE as new address - this is discarded when encoded
        if (!isByteAligned(instructionsToInject)) {
            DecodedInstruction nop = DecodedInstructionFactory
                    .NOP(Integer.MAX_VALUE);
            nop.setMethodDef(injected);
            instructionsToInject.put(Integer.MAX_VALUE, nop);
        }

        instructionsToInject.recalibrateAddresses();
        injected.setInstructions(injected.getRegistersSize(),
                instructionsToInject);

    }

    /**
     * Determines if the instruction list will be byte aligned when it is
     * encoded.
     * 
     * @param instructionsToInject the instructions to check.
     * @return true if the instruction list is aligned on a four byte boundary.
     */
    private boolean isByteAligned(InstructionList instructionsToInject) {
        short[] encoded = instructionsToInject.encode();
        int len = encoded.length;
        return (len % 2 == 0);
    }

    /**
     * The injected position is the first position after any call to super.
     */
    @Override
    public List<Integer> findInjectPosition(MethodDefinition target) {

        List<Integer> result = new LinkedList<Integer>();

        // find the first instruction that is not a call to super.
        int pos = -1;

        InstructionList instructionList = target.instructionsDecodedAsList();
        int sz = instructionList.size();

        // find the first position of an INVOKE_SUPER_* instruction.
        for (int i = 0; i < instructionList.size(); i++) {

            if (pos != -1)
                continue; // burn after finding.

            DecodedInstruction instruction = instructionList.valueAt(i);

            int opCode = instruction.getOpcode();
            switch (opCode) {
                case Opcodes.INVOKE_SUPER:
                case Opcodes.INVOKE_SUPER_JUMBO:
                case Opcodes.INVOKE_SUPER_RANGE:
                    pos = i;
                    break;

            }

        }

        pos++;

        // pos >= 0

        if (pos < sz)
            result.add(new Integer(pos));
        else
            result.add(new Integer(Integer.MAX_VALUE)); // causes append

        return result;

    }

    @Override
    public MethodDefinition getMethodToInject() {
        return BaseUpdater.injectMethod;
    }

    /**
     * Local registers are not reused because it causes problems in the target
     * method's registers. TODO: allow more than 15 registers in a method - see
     * DexInjectMerger.REGISTER_LIMIT
     */
    @Override
    public boolean reuseLocalRegisters() {
        return false;
    }

    @Override
    public boolean observeRegisterLimits() {
        return true;
    }

    @Override
    public void postInjectUpdate(MethodDefinition target, int address) {

    }

}
