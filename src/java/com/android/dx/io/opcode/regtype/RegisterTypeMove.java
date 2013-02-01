
package com.android.dx.io.opcode.regtype;

import static schilling.richard.r3.app.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Sets a register in slot A to the type specified in register slot B.
 * 
 * @author rschilling
 */
public class RegisterTypeMove extends RegisterTypeSetter {

    @Override
    public void setRegisterTypes(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) {

        int vSrc = instruction.getB();
        int vDst = instruction.getA();

        // copyRegister1(workLine, decInsn.vA, decInsn.vB, kTypeCategory1nr,
        // &failure);

        RegisterType type = registerTypes.get(vSrc, instruction);
        if (type == null) {
            if (RegisterType.WARN_UNINIT) {
                if (DO_LOG)
                    Log.w(LOG_TAG, "the source register has not been set.");

            } else
                throw new IllegalStateException(
                        "the source register has not been set.");

        }

        registerTypes.setValueAt(vDst, type);

    }
}
