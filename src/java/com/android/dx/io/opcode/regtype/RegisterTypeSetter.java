package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Generates register types given information abou an instruction. Code in
 * sub-classes that run when VALIDATE_EXTRA are not included in the C code but
 * make sense.
 * 
 * @author rschilling
 * 
 */
public abstract class RegisterTypeSetter {
	public static final String LOG_TAG = "RegisterTypeSetter";

	protected boolean clearInvokeDirect = true;

	/**
	 * Given the information in the specified DecodedInstruction, update a
	 * register type sparse array. No verification is done, and the
	 * registerTypes is assumed to be reliable. If for some reason a register
	 * type cannot be determined based on the information in the registerTypes
	 * sparse array, then an exception is thrown.
	 * 
	 * @param instruction
	 *            the instruction to read information from.
	 * @param registerTypes
	 *            the retgister types to update.
	 * @throws VerifyException
	 *             if the update cannot succeed.
	 */
	protected abstract void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes);

	public void set(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {
		setRegisterTypes(instruction, registerTypes);

		if (clearInvokeDirect) {
			registerTypes.setResultRegisterTypeLow(null, instruction);
			registerTypes.setResultRegisterTypeHigh(null, instruction);
		}

	}

}
