
package com.android.dx.io.opcode.verify;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;
import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory1nr;
import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * The move-result instructions copy data out of a "pseudo-register" with the
 * results from the last method invocation. In practice we might want to hold
 * the result in an actual CPU register, so the Dalvik spec requires that these
 * only appear immediately after an invoke or filled-new-array. These calls
 * invalidate the "result" register. (This is now redundant with the reset done
 * below, but it can make the debug info easier to read in some cases.)
 */
public class VerifyMoveResult implements OpcodeVerifier {

    protected RegisterCategory categoryToCheck = kTypeCategory1nr;

    /**
     * Verifies register move operation: the source register must be type 1nr.
     */
    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {

        // copyResultRegister1(workLine, insnRegCount, decInsn.vA,
        // kTypeCategory1nr, &failure);

        int vDst = instruction.getA();

        if (vDst >= instruction.getCode().getRegistersSize() - 1)
            throw new IllegalStateException(
                    String.format(
                            "destination register %d is does not exist in instruction %s(%d)",
                            vDst, instruction.getOpcodeEnum().toString(),
                            instruction.getOpcode()));

        RegisterType type = registerTypes.getResultRegisterTypeLow(instruction);
        if (type == null)
            throw new IllegalStateException(
                    "the source register has not been set.");

        if (type.typeEnum().category() != categoryToCheck) {
            // create a cause exception so we generate the same information as
            // the C code.
            VerifyException cause = new VerifyException(VERIFY_ERROR_GENERIC,
                    String.format("VFY: copy1 type=%s(s) cat=%s",
                            type.toString(), type.typeEnum().category(),
                            categoryToCheck.toString()));
            if (RegisterType.WARN_UNINIT) {
                if (DO_LOG)
                    Log.w(LOG_TAG, cause);
            } else {
                throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
                        "VFY: copy1 v%u<-R type=%s(%s) cat=%s", vDst,
                        type.toString(), type.typeEnum().category(),
                        categoryToCheck.toString()), cause);
            }

        }

        if (VALIDATE_EXTRA) {
            type = registerTypes.get(vDst);
            if (type != null && type.typeEnum().category() != categoryToCheck) {
                VerifyException cause = new VerifyException(
                        VERIFY_ERROR_GENERIC, String.format(
                                "VFY: copy1 type=%s cat=%s", type.toString(),
                                categoryToCheck.toString()));

                throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
                        "VFY: copy1 v%d<-Rlo type=%s cat=%s", vDst,
                        type.toString(), categoryToCheck.toString()), cause);
            }

        }

    }

}
