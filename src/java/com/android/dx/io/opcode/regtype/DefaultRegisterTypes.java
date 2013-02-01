
package com.android.dx.io.opcode.regtype;

import static schilling.richard.r3.app.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

public class DefaultRegisterTypes extends RegisterTypeSetter {

    @Override
    public void setRegisterTypes(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) {

        if (DO_LOG)
            Log.w(LOG_TAG,
                    String.format(
                            "Opcode verification for instruction %s (%d) not implemented yet.",
                            instruction.getOpcodeEnum().toString(),
                            instruction.getOpcode()));
    }

}
