package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyIfEqz implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		RegisterType tmpType = registerTypes.get(instruction.getA(),
				instruction);
		if (tmpType.typeEnum() == kRegTypeRef)
			return;

		if (tmpType.typeEnum().category() != RegisterCategory.kTypeCategory1nr)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: expected cat-1 arg to if");

		if (tmpType.typeEnum() == kRegTypeFloat)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: expected non-float cat-1 arg to if");
	}

}
