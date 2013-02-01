package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Does not use typeToGet, but still extends VerifyAGet1NrCommon to use the
 * checkArrayIndex function.
 * 
 * @author rschilling
 * 
 */
public class Verify_APUT_WIDE extends VerifyAGet1NrCommon {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		RegisterType tmpType = registerTypes.get(instruction.getC(),
				instruction);
		checkArrayIndexType(tmpType.typeEnum());

		String resSignature = null;
		if (!RegisterType.WARN_UNINIT)
			registerTypes.get(instruction.getB(), instruction)
					.getRefersTo(true);

		/* resClass can be null if the reg type is Zero */
		if (resSignature == null)
			return;

		ClassDef resClass = instruction.getBuffer().getDef(resSignature);
		if (resClass == null)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("Unable to resolve class %s", resSignature));

		boolean isPrimitiveType = PrimitiveType.dvmIsPrimitiveClass(
				resClass.getElementClass(), false);

		if (!resClass.dvmIsArrayClass() || resClass.getArrayDimensions() != 1
				|| !isPrimitiveType)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: invalid aput-wide on %s", resSignature));

		/* make sure array type matches instruction */
		PrimitiveType pType = PrimitiveType.primitiveTypeFromSignature(
				resSignature, false);

		switch (pType) {
		case PRIM_LONG:
			RegisterType.verifyRegisterType(registerTypes, instruction.getA(),
					RegisterType.newInstance(kRegTypeLongLo), instruction);
			break;
		case PRIM_DOUBLE:
			RegisterType.verifyRegisterType(registerTypes, instruction.getA(),
					RegisterType.newInstance(kRegTypeDoubleLo), instruction);
			break;
		default:
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: invalid aput-wide on %s", resSignature));
		}

	}
}
