
package com.android.dx.io.opcode.verify;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

public class DefaultVerifier implements OpcodeVerifier {

    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {

        if (DO_LOG)
            Log.w(LOG_TAG,
                    String.format(
                            "Opcode verification for instruction %s (%d) not implemented yet.",
                            instruction.getOpcodeEnum().toString(),
                            instruction.getOpcode()));

    }

}
