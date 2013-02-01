package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyIfLt implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		RegisterType tmpType = registerTypes.get(instruction.getA(),
				instruction);

		if (tmpType.typeEnum().category() != RegisterCategory.kTypeCategory1nr
				|| tmpType.typeEnum() == kRegTypeFloat)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: args to 'if' must be cat-1nr and not float");

		tmpType = registerTypes.get(instruction.getB(), instruction);
		if (tmpType.typeEnum().category() != RegisterCategory.kTypeCategory1nr
				|| tmpType.typeEnum() == kRegTypeFloat)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: args to 'if' must be cat-1nr and not float");

	}

}
