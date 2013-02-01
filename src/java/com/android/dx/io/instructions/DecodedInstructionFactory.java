package com.android.dx.io.instructions;

import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.format.InstructionCodec;

/**
 * Generates instructions.
 * 
 * @author rschilling
 * 
 */
public final class DecodedInstructionFactory {

	/**
	 * Creates a MOVE_OBJECT instruction with the specified source and
	 * destination register values.
	 * 
	 * @param vDst
	 *            the destination register
	 * @param vSrc
	 *            the source register
	 * @return a newly instantiated MOVE_OBJECT instruction (move-object vA, vB)
	 */
	public static DecodedInstruction MOVE_OBJECT(int address, int vDst, int vSrc) {
		DecodedInstruction result = new TwoRegisterDecodedInstruction(
				InstructionCodec.FORMAT_12X, Opcodes.MOVE_OBJECT, 0, null, 0,
				0, 0L, vDst, vSrc);
		result.setAddress(address);
		return result;
	}

	public static DecodedInstruction NOP(int address) {

		DecodedInstruction result = new ZeroRegisterDecodedInstruction(
				InstructionCodec.FORMAT_10X, Opcodes.NOP, 0, null, 0, 0, 0);
		result.setAddress(address);
		return result;

	}

	/**
	 * Creates a move instruction that can handle the registers. The result will
	 * be one of these instructions: MOVE, MOVE_FROM16, or MOVE_16.
	 * 
	 * @param vDst
	 *            the destination register.
	 * @param vSrc
	 *            the source register
	 * @return a move instruction
	 */
	public static DecodedInstruction MOVE_XX(int address, int vDst, int vSrc) {
		DecodedInstruction result = new TwoRegisterDecodedInstruction(
				InstructionCodec.FORMAT_12X, Opcodes.MOVE, 0, null, 0, 0, 0L,
				vDst, vSrc);
		
		result.setAddress(address);

		// make sure that the correct instruction is picked based on the
		// register value.
		InstructionCodec codec = result.getFormat();
		return codec.convert(result);
	}
	
	public static DecodedInstruction MOVE(int address, int vDst, int vSrc) {
		DecodedInstruction result = new TwoRegisterDecodedInstruction(
				InstructionCodec.FORMAT_12X, Opcodes.MOVE, 0, null, 0, 0, 0L,
				vDst, vSrc);
		
		result.setAddress(address);
		
		return result;
	}
	
	public static DecodedInstruction MOVE_WIDE_16(int address, int vDst, int vSrc) {
	
		DecodedInstruction result = new TwoRegisterDecodedInstruction(
				InstructionCodec.FORMAT_32X, Opcodes.MOVE_WIDE_16, 0, null, 0, 0, 0L,
				vDst, vSrc);
		
		result.setAddress(address);
		
		// no conversion necessary
		return result;
	}

}
