package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyArrayLength implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (RegisterType.WARN_UNINIT)
			return;

		RegisterType bType = registerTypes.get(instruction.getB(), instruction);
		String resClassSig = bType.getRefersTo(true);
		if (resClassSig == null)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: class not associated with register reference vB");

		ClassDef resClass = instruction.getBuffer().getDef(resClassSig);
		if (resClass == null)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: class associated with register vB was not found");

		if (!resClass.dvmIsArrayClass())
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: array-length on non-array");

	}

}
