package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.RegisterReference;

public abstract class VerifySingleRegisterType implements OpcodeVerifier {

	protected RegisterReference ref = null;
	protected RegisterTypeEnum typeRequired = null;

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (ref == null)
			throw new IllegalStateException("the subclass must set member ref.");

		if (typeRequired == null)
			throw new IllegalStateException(
					"the subclass must set member typeRequired.");

		int reg = 0;

		switch (ref) {
		case A:
			reg = instruction.getA();
			break;
		case B:
			reg = instruction.getB();
			break;
		case C:
			reg = instruction.getC();
			break;
		case D:
			reg = instruction.getD();
			break;
		case E:
			reg = instruction.getE();
			break;
		}

		RegisterType.verifyRegisterType(registerTypes, reg, typeRequired,
				instruction);

	}

}
