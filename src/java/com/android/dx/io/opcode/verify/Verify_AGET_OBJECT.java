
package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

public class Verify_AGET_OBJECT extends VerifyAGet1NrCommon {
    public Verify_AGET_OBJECT() {
        this.typeToCheck = kRegTypeInteger;
    }

    @Override
    public void verify(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {

        if (typeToCheck == null)
            throw new IllegalStateException("subclass must set tmpType");

        RegisterType indexType = registerTypes.get(instruction.getC(),
                instruction);
        checkArrayIndexType(indexType.typeEnum());

        String resSignature = null;
        if (!RegisterType.WARN_UNINIT)
            resSignature = registerTypes.get(instruction.getB(), instruction)
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

        if (!resClass.dvmIsArrayClass())
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    "VFY: aget-object on non-array class");

        /*
         * Find the element class.
         */
        String elementClass = resClass.getElementClass();
        if (elementClass.charAt(0) != 'L')
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: aget-object on non-ref array class (%s)",
                            resSignature));

    }

}
