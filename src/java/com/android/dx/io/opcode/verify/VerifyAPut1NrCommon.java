
package com.android.dx.io.opcode.verify;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import schilling.richard.dalvik.vm.CodeVerifyConstants;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

public abstract class VerifyAPut1NrCommon implements OpcodeVerifier {

    protected RegisterTypeEnum typeToCheck = null;

    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {

        if (typeToCheck == null)
            throw new IllegalStateException("subclass must set tmpType");

        RegisterTypeEnum tmpType = typeToCheck;
        RegisterType indexType = registerTypes.get(instruction.getC(),
                instruction);
        checkArrayIndexType(indexType);

        RegisterType srcType = registerTypes.get(instruction.getA(),
                instruction);
        if (srcType.typeEnum() == kRegTypeFloat && tmpType == kRegTypeInteger)
            tmpType = kRegTypeFloat;

        if (!CodeVerifyConstants.canConvertTo1nr(srcType.typeEnum(), tmpType)) {
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: invalid reg type %d on aput instr (need %d)",
                            srcType.toString(), tmpType.toString()));
        }

        String resSignature = null;
        if (!RegisterType.WARN_UNINIT)
            registerTypes.get(instruction.getB(), instruction)
                    .getRefersTo(true);

        /* resClass can be null if the reg type is Zero */
        if (resSignature == null) {
            if (DO_LOG)
                Log.i(LOG_TAG, String
                        .format("class %s is not set in register. Continuing."));
            return;
        }

        ClassDef resClass = instruction.getBuffer().getDef(resSignature);
        if (resClass == null)
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("Unable to resolve class %s", resSignature));

        boolean isPrimitiveType = PrimitiveType.dvmIsPrimitiveClass(
                resClass.getElementClass(), false);

        if (!resClass.dvmIsArrayClass() || resClass.getArrayDimensions() != 1
                || !isPrimitiveType)
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("VFY: invalid aput-1nr on %s", resSignature));

        /* verify that instruction matches array */
        RegisterType dstType = PrimitiveType
                .primitiveTypeToRegisterType(PrimitiveType
                        .primitiveTypeFromSignature(resSignature, false));

        /* correct if float */
        if (dstType.typeEnum() == kRegTypeFloat && tmpType == kRegTypeInteger)
            tmpType = kRegTypeFloat;

        RegisterType.verifyRegisterType(registerTypes, instruction.getA(),
                dstType, instruction);

        if (dstType.typeEnum() == kRegTypeUnknown
                || tmpType != dstType.typeEnum()) // !checkFieldArrayStore1nr(tmpType,
            // dstType))
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: invalid aput-1nr on %s (inst=%d dst=%d)",
                            resSignature, tmpType.toString(),
                            dstType.toString()));

    }

    /*
     * there is no reason to have this function. private boolean
     * checkFieldArrayStore1nr(RegisterType instrType, RegisterType targetType){
     * return instrType == targetType; }
     */

    private void checkArrayIndexType(RegisterType regType)
            throws VerifyException {

        if (regType == null)
            throw new IllegalArgumentException("regType cannot be null.");

        if (regType.typeEnum().category() != RegisterCategory.kTypeCategory1nr)
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("Invalid reg type for array index (%d)",
                            regType.toString()));

    }
}
