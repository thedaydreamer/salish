package com.android.dx.io.opcode;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * This class binds together instructions and their registers. It is the class
 * that code uses to extract register information from an instruction set. It is
 * also the class that is used to validate register usage among an instruction
 * set.
 * 
 * <p>
 * This class is not meant to be used to write instruction lists to file, so it
 * is not possible to retrieve the instructions in the form of an array. See the
 * Code class for that purpose.
 * 
 * <p>
 * Register information can be thought of a a multidimensional array:
 * 
 * <pre>
 * 
 * 
 *              REGISTER REFERENCE - by number
 *              A     B     C     D     E     
 * INSTRUCTION
 *           0  1     5     32    0     1
 *           1  1     6     18    45    9
 *           ...
 *           N
 *           
 *              REGISTER TYPE 
 *              A     B     C     D     E     
 * INSTRUCTION
 *           0  bool  unk   int   ref   ref
 *           1  int   unk   long  ref   ref
 *           ...
 *           N
 * 
 * </pre>
 * 
 * <p>
 * This class provides access to that register information without creating a
 * complete in-memory array: through sparse matrices and accessor functions.
 * 
 * <p>
 * <b>Verification</b>
 * <p>
 * Instructions and formats can provide register information: OpcodeInfo.Info
 * class:
 * <ul>
 * <li>The OpcodeInfo.Info class returns register information that can be
 * derived from the format itself.
 * <li>An instruction returns register information that can be derived from the
 * instruction once it's been filled in with specific data.
 * <li>the function getRegisterUsage returns information about all registers
 * used by the entire instruction set. For this reason when getRegisterUsage is
 * called, it automatically verifies the register usage by calling
 * verifyRegisters.
 * </ul>
 * 
 * The verifiation that happens when verifyRegisters is called is the same
 * verification logic that is ran in the C version of the verification process
 * in the Dalivk virtual machine. A flag can be passed to the function, however
 * to do extended register checks. s
 * 
 * @author rschilling
 * 
 */
public class RegisterMap {

	private final InstructionList instructions;

	public RegisterMap(InstructionList i) {
		instructions = i;
	}

	/**
	 * Returns the register number found in the specified instruciton and the
	 * specified reference item. The number returned is the register number
	 * referenced by the specified instruction (v0..vnnnn).
	 * 
	 * @param address
	 *            the instruction in the sparse array to examine (zero based).
	 * @param ref
	 *            the register reference to retrieve the number for.
	 * @return the register reference number.
	 * @throws IllegalArgumentException
	 *             if pos is not found in the instruction sparse array.
	 * @throws IllegalArgumentException
	 *             if ref is null.
	 * @throws IllegalArgumentException
	 *             if the instruction at pos does not use the specified register
	 *             ref.
	 */
	public int getRegisterNumber(int address, RegisterReference ref) {

		if (!hasInstructionAt(address))
			throw new IllegalArgumentException(
					"the instruction list does not have a decoded instruction at the specified address.");

		if (ref == null)
			throw new IllegalArgumentException(
					"the specified register reference cannot be null.");

		DecodedInstruction instruction = instructions.get(address);
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

		throw new IllegalStateException(
				"RegisterReference contains an enumeration type that needs to be handled by this function.");

	}

	/**
	 * Returns the register number found in the specified instruciton and the
	 * specified reference item. The number returned is the register number
	 * referenced by the specified instruction (v0..vnnnn).
	 * 
	 * @param address
	 *            the instruction in the sparse array to examine (zero based).
	 * @param ref
	 *            the register reference to retrieve the number for.
	 * @return the register reference number.
	 * @throws IllegalArgumentException
	 *             if pos is not found in the instruction sparse array.
	 * @throws IllegalArgumentException
	 *             if ref is null.
	 * @throws IllegalArgumentException
	 *             if the instruction at pos does not use the specified register
	 *             ref.
	 */
	public RegisterType getRegisterType(int address, RegisterReference ref) {

		if (!hasInstructionAt(address))
			throw new IllegalArgumentException(
					"the instruction list does not have a decoded instruction at the specified address.");

		if (ref == null)
			throw new IllegalArgumentException(
					"the specified register reference cannot be null.");

		DecodedInstruction instruction = instructions.get(address);
		switch (ref) {
		case A:
			return instruction.getTypeA();
		case B:
			return instruction.getTypeB();
		case C:
			return instruction.getTypeC();
		case D:
			return instruction.getTypeD();
		case E:
			return instruction.getTypeE();

		}

		throw new IllegalStateException(
				"RegisterReference contains an enumeration type that needs to be handled by this function.");

	}

	public boolean hasInstructionAt(int address) {
		return instructions.indexOfKey(address) >= 0 ? true : false;
	}

}
