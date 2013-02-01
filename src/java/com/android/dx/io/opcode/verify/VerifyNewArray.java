package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyNewArray extends VerifyResolveClass {

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

		String type = instruction.getBuffer().typeNames().get(instruction.getIndex());
		if (type.charAt(0) != '[')
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: new-array on non-array class");

		RegisterType.verifyRegisterType(registerTypes, instruction.getB(),
				kRegTypeInteger, instruction);

	}
}
