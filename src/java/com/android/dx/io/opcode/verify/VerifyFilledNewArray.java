
package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.r3.app.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.Opcodes;

public class VerifyFilledNewArray extends VerifyResolveClass {

    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {
        try {
            super.verify(instruction, registerTypes);
        } catch (VerifyException ex) {
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("VFY: unable to resolve new-array %d",
                            instruction.getC()), ex);
        }

        ClassDef resClass = instruction.getBuffer().classDefList()
                .get(instruction.getIndex());
        if (!resClass.dvmIsArrayClass())
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    "VFY: filled-new-array on non-array class");

        /* check the arguments to the instruction */
        verifyFilledNewArrayRegs(instruction, registerTypes);

    }

    /**
     * Verify that the arguments in a filled-new-array instruction are valid.
     * <p>
     * "resClass" is the class refered to by pDecInsn->vB.
     */
    private void verifyFilledNewArrayRegs(DecodedInstruction pDecInsn,
            RegisterTypeSparseArray registerTypes) throws VerifyException {
        {
            boolean isRange = pDecInsn.getOpcode() == Opcodes.FILLED_NEW_ARRAY_RANGE
                    || pDecInsn.getOpcode() == Opcodes.FILLED_NEW_ARRAY_JUMBO;
            int argCount = pDecInsn.getA();
            ClassDef resClass = pDecInsn.getBuffer().classDefList()
                    .get(pDecInsn.getIndex());

            RegisterType expectedType = null;
            PrimitiveType elemType = PrimitiveType.primitiveTypeFromSignature(
                    resClass.getSignature(), false);

            if (!resClass.dvmIsPrimitiveClass()) {
                expectedType = RegisterType.newInstance(kRegTypeRef);
            } else {
                expectedType = PrimitiveType
                        .primitiveTypeToRegisterType(elemType);
            }

            if (DO_LOG)
                Log.i(LOG_TAG,
                        String.format("filled-new-array: %s -> %d",
                                resClass.getSignature(), expectedType.toString()));

            /*
             * see InstructionCodes.FORMAT_35MI and the C code for
             * DecodedInstruction structure (InstrUtils.h). here is the mapping:
             * arg[0] = a, arg[1] = b .. arg[4] = e in the Java implementation.
             */

            /*
             * Verify each register. If "argCount" is bad, verifyRegisterType()
             * will run off the end of the list and fail. It's legal, if silly,
             * for argCount to be zero.
             */
            for (int ui = 0; ui < argCount; ui++) {
                int getReg;

                if (isRange)
                    getReg = pDecInsn.getC() + ui;
                else
                    getReg = pDecInsn.getRegisterContentsByIndex(ui);

                try {
                    RegisterType.verifyRegisterType(registerTypes, getReg,
                            expectedType, pDecInsn);
                } catch (VerifyException ex) {
                    throw new VerifyException(
                            VerifyErrorCause.VERIFY_ERROR_GENERIC,
                            String.format(
                                    "VFY: filled-new-array arg %u(%u) not valid",
                                    ui, getReg));
                }
            }
        }
    }
}
