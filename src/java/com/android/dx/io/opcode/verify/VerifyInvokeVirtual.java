package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyInvokeVirtual extends VerifyInvoke {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		boolean isRange = instruction.isInvokeRange();
		boolean isSuper = instruction.isInvokeSuper();

		instruction.verifyInvocationArgs();

	}

}
