package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.RegisterReference;

public abstract class VerifySingleRegisterCategory implements OpcodeVerifier {

	protected RegisterReference ref = null;
	protected RegisterCategory categoryRequired = null;
	protected String failMessage = null;

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (ref == null)
			throw new IllegalStateException("the subclass must set member ref.");

		if (categoryRequired == null)
			throw new IllegalStateException(
					"the subclass must set member typeRequired.");

		if (failMessage == null)
			throw new IllegalStateException(
					"the subclass must set the fail message");

		int reg = getRef(instruction);

		RegisterType rType = registerTypes.get(reg, instruction);
		if (rType.typeEnum().category() != categoryRequired)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					failMessage);

	}

	protected int getRef(DecodedInstruction instruction) {
		switch (ref) {
		case A:
			return instruction.getA();

		case B:
			return instruction.getB();
		case C:
			return instruction.getC();

		case D:
			return instruction.getD();

		case E:
			return instruction.getE();

		}

		throw new IllegalStateException("no value found for register");
	}

}
