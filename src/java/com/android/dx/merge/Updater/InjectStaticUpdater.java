
package com.android.dx.merge.Updater;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;

import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;

import com.android.dx.io.MethodId;
import com.android.dx.merge.DexInjectMerger.ClassMethodPair;
import com.android.dx.merge.MethodDefinition;

/**
 * Use for inject non-static function. Strips the return statement from injected
 * and updates the registers with method id and class id values.
 * 
 * @author rschilling
 */
public class InjectStaticUpdater extends InjectUpdater {

    public InjectStaticUpdater(List<ClassMethodPair> methodList) {
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

        if (DO_LOG) {
            if (LOG_UPDATES)
                Log.i(BaseUpdater.LOG_TAG, String.format(
                        "setting parameters to report class id %d, method id %d.",
                        methodId.getDeclaringClassIndex(),
                        methodId.getMethodIdIndex()));
        }

        methodList().add(new ClassMethodPair(declaringClassIndex, methodIndex, 0));

        InstructionList instructionsToInject = injected
                .instructionsDecodedAsList();

        // instruction 0 contains the class def literal
        instructionsToInject.valueAt(0).setLiteral(
                (long) (declaringClassIndex & 0xff));

        // instruction 1 contains the method def literal.
        instructionsToInject.valueAt(1).setLiteral((long) (methodIndex & 0xff));

        injected.setInstructions(injected.getRegistersSize(),
                instructionsToInject);

    }

    @Override
    public MethodDefinition getMethodToInject() {
        return BaseUpdater.injectMethodStatic;
    }

    @Override
    public boolean observeRegisterLimits() {
        return true;
    }

}
