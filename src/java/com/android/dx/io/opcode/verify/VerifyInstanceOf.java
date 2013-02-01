package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyInstanceOf extends VerifyResolveClass {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		super.verify(instruction, registerTypes);

		RegisterType tmpType = registerTypes.get(instruction.getB(),
				instruction);
		if (tmpType.typeEnum() != kRegTypeRef)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: vB not a reference (%d)", tmpType));

	}

}
