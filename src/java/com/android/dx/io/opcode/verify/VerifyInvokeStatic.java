package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyInvokeStatic extends VerifyInvoke {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		boolean isRange = instruction.isInvokeStaticRange();
		boolean isSuper = false;

		instruction.verifyInvocationArgs();

	}

}
