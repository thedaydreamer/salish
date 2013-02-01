package com.android.dx.io.opcode;

/**
 * This enumeration specifies register reference slots in a Dalvik instruction.
 * Every dalvik instruction has up to five register references: A, B, C, D, and
 * E. This enumeration allows code to specify one of these references.
 * 
 * @author rschilling
 * 
 */
public enum RegisterReference {
	A(1), B(2), C(3), D(4), E(5);

	private RegisterReference(int regsRequired) {
		minRegistersRequired = regsRequired;
	}

	/**
	 * The number of registers that an instruction must use in order to utilize
	 * this register reference.
	 */
	private int minRegistersRequired = 0;

	/**
	 * Returns the number of registers that the instruction must utilize in
	 * order to reference this register type.
	 * 
	 * @return registers required to use by the instruction.
	 */
	public int minRegistersRequired() {
		return minRegistersRequired;

	}
}
