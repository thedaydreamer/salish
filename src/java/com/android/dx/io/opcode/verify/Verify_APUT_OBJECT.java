package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

public class Verify_APUT_OBJECT extends VerifyAGet1NrCommon {
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

		if (!resClass.dvmIsArrayClass())
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: invalid aput-object on %s",
							resSignature));

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
