package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Does not update any registers. Sets the value isInvokeResult in all
 * RegisterTypes to false.
 * 
 * @author rschilling
 * 
 */
public class NoRegisters extends RegisterTypeSetter {

	@Override
	protected void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {
		// m-t
	}

}
