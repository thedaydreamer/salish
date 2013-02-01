package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeMoveResultWide extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		int vDst = instruction.getA();

		RegisterType typel = registerTypes
				.getResultRegisterTypeLow(instruction);
		RegisterType typeh = registerTypes
				.getResultRegisterTypeHigh(instruction);

		if (typel == null)
			throw new IllegalStateException(
					"the low result register has not been set.");

		if (typeh == null)
			throw new IllegalStateException(
					"the high result register has not been set.");

		registerTypes.put(vDst, typel, instruction, "Rl");
		registerTypes.put(vDst + 1, typeh, instruction, "Rh");

	}

}
