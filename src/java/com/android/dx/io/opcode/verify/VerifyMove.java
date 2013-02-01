
package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory1nr;
import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyMove implements OpcodeVerifier {

    protected RegisterCategory categoryToCheck = kTypeCategory1nr;

    /**
     * Verifies register move operation: the source register must be type 1nr.
     */
    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {
        int vSrc = instruction.getB();
        int vDst = instruction.getA();

        // copyRegister1(workLine, decInsn.vA, decInsn.vB, kTypeCategory1nr,
        // &failure);

        RegisterType type = registerTypes.get(vSrc, instruction);
        if (type == null)
            throw new IllegalStateException(
                    "the source register has not been set.");
        if (CodeVerify.WARN_UNKNOWN_REGISTERS
                && type.typeEnum() == RegisterTypeEnum.kRegTypeUnknown) {
            if (DO_LOG)
                Log.w(LOG_TAG, String.format("VFY: copy1 type=%s cat=%s",
                        type.toString(), categoryToCheck.toString()));
        } else if (type.typeEnum().category() != categoryToCheck) {
            // create a cause exception so we generate the same information as
            // the C code.
            VerifyException cause = new VerifyException(VERIFY_ERROR_GENERIC,
                    String.format("VFY: copy1 type=%s cat=%s", type.toString(),
                            categoryToCheck.toString()));

            throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
                    "VFY: copy1 v%d<-v%d type=%s cat=%s", vDst, vSrc,
                    type.toString(), categoryToCheck.toString()), cause);

        }

        if (VALIDATE_EXTRA) {
            type = registerTypes.get(vDst, instruction);
            if (type != null && type.typeEnum().category() != categoryToCheck) {
                VerifyException cause = new VerifyException(
                        VERIFY_ERROR_GENERIC, String.format(
                                "VFY: copy1 type=%s cat=%s", type.toString(),
                                categoryToCheck.toString()));

                throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
                        "VFY: copy1 v%d<-v%d type=%d cat=%d", vDst, vSrc,
                        type.toString(), categoryToCheck.toString()), cause);
            }

        }

    }

}
