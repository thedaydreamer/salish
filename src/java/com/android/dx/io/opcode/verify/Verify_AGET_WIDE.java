package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

public class Verify_AGET_WIDE extends VerifyAGet1NrCommon {
	public Verify_AGET_WIDE() {
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
					String.format("VFY: invalid aget-wide target %s",
							resSignature));

		/* make sure array type matches instruction */
		PrimitiveType pType = PrimitiveType.primitiveTypeFromSignature(
				resSignature, false);

		switch (pType) {
		case PRIM_LONG:
		case PRIM_DOUBLE:
			break;
		default:
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: invalid aget-wide on %s", resSignature));
		}

	}
}
