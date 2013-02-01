
package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.r3.app.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeMoveResult extends RegisterTypeSetter {

    @Override
    public void setRegisterTypes(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) {

        int vDst = instruction.getA();

        // copyRegister1(workLine, decInsn.vA, decInsn.vB, kTypeCategory1nr,
        // &failure);

        RegisterType type = registerTypes.getResultRegisterTypeLow(instruction);
        if (type == null) {
            if (CodeVerify.WARN_NULL_REGISTERS) {
                if (DO_LOG)
                    Log.w(CodeVerify.LOG_TAG,
                            String.format(
                                    "Cannot move types from result register to %d. The result register has not been set for instruction %s(%d).",
                                    vDst, instruction.getOpcodeEnum().toString(),
                                    instruction.getOpcode()));
                type = RegisterType.newInstance(kRegTypeUnknown);
            } else {
                throw new IllegalStateException(
                        String.format(
                                "Cannot move types from result register to %d. The result register has not been set for instruction %s(%d).",
                                vDst, instruction.getOpcodeEnum().toString(),
                                instruction.getOpcode()));
            }
        }

        registerTypes.put(vDst, type, instruction, null);
        registerTypes.setResultRegisterTypeLow(null, instruction);

    }

}
