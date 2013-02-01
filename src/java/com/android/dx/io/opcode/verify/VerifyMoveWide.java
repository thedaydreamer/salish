
package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory2;
import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import static schilling.richard.r3.app.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyMoveWide implements OpcodeVerifier {

    /**
     * Verifies register move operation: the source register must be type 1nr.
     */
    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {

        if (!WARN_VERIFY) {
            // TODO remove WARN_VERIFY bypass.

            int vSrcLow = instruction.getB();
            int vSrcHigh = vSrcLow + 1;

            int vDstLow = instruction.getA();

            // copyRegister2(workLine, decInsn.vA, decInsn.vB, kTypeCategory2,
            // &failure);

            RegisterType typel = registerTypes.get(vSrcLow, instruction);
            RegisterType typeh = registerTypes.get(vSrcHigh, instruction);

            if (typel.typeEnum().category() != kTypeCategory2) {
                // create a cause exception so we generate the same information
                // as
                // the C code.
                VerifyException cause = new VerifyException(
                        VERIFY_ERROR_GENERIC, String.format(
                                "VFY: copy1 type=%s cat=%s", typel.toString(),
                                kTypeCategory2.toString()));

                if (DO_LOG) // TODO combine the logic - revisit and remove
                            // logging.
                    if (WARN_VERIFY)
                        Log.w(LOG_TAG, String.format("VFY: copy1 type=%s cat=%s",
                                typel.toString(), kTypeCategory2.toString()));
                    else
                        throw new VerifyException(VERIFY_ERROR_GENERIC,
                                String.format("VFY: copy1 v%u<-v%u type=%d cat=%d",
                                        vDstLow, vSrcLow, typel,
                                        kTypeCategory2.toString()), cause);

            }

            try {
                CodeVerify.checkWidePair(typel.typeEnum(), typeh.typeEnum());
            } catch (VerifyException ex) {

                throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
                        "VFY: copy2 v%u<-v%u type=%d/%d", vDstLow, vSrcLow,
                        typel.toString(), typeh.toString()), ex);
            }

            if (VALIDATE_EXTRA) {
                if (typeh.typeEnum().category() != kTypeCategory2) {
                    // create a cause exception so we generate the same
                    // information
                    // as
                    // the C code.
                    VerifyException cause = new VerifyException(
                            VERIFY_ERROR_GENERIC, String.format(
                                    "VFY: copy1 type=%d cat=%d", typel,
                                    kTypeCategory2.toString()));

                    throw new VerifyException(VERIFY_ERROR_GENERIC,
                            String.format("VFY: copy1 v%u<-v%u type=%d cat=%d",
                                    vDstLow, vSrcLow, typel,
                                    kTypeCategory2.toString()), cause);

                }

                int vDstHigh = vDstLow + 1;

                RegisterType destTypeLow = registerTypes.get(vDstLow,
                        instruction);
                RegisterType destTypeHigh = registerTypes.get(vDstHigh,
                        instruction);

                if (destTypeLow != null
                        && destTypeLow.typeEnum().category() != kTypeCategory2)
                    throw new VerifyException(VERIFY_ERROR_GENERIC,
                            "destination register type is not compatible.");

                if (destTypeHigh != null
                        && destTypeHigh.typeEnum().category() != kTypeCategory2)
                    throw new VerifyException(VERIFY_ERROR_GENERIC,
                            "destination register type is not compatible.");

            }
        }

    }

}
