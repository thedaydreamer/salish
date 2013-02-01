package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeCat1Const extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		/*
		 * The C code for CONST_* instruction verification uses getB, but in
		 * this implementation the value for vB is loaded into the literal.
		 */
		/* could be boolean, int, float, or a null reference */
		registerTypes.put(instruction.getA(),
				RegisterType.determineCat1Const(instruction.getLiteral()),
				instruction, null); // instruction.getB()));

	}

}
