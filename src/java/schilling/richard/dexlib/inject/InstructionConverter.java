package schilling.richard.dexlib.inject;

import static com.android.dx.io.opcode.Opcodes.ARRAY_LENGTH;
import static com.android.dx.io.opcode.Opcodes.CONST;
import static com.android.dx.io.opcode.Opcodes.CONST_16;
import static com.android.dx.io.opcode.Opcodes.CONST_4;
import static com.android.dx.io.opcode.Opcodes.CONST_HIGH16;
import static com.android.dx.io.opcode.Opcodes.CONST_STRING;
import static com.android.dx.io.opcode.Opcodes.CONST_STRING_JUMBO;
import static com.android.dx.io.opcode.Opcodes.CONST_WIDE;
import static com.android.dx.io.opcode.Opcodes.CONST_WIDE_16;
import static com.android.dx.io.opcode.Opcodes.CONST_WIDE_32;
import static com.android.dx.io.opcode.Opcodes.CONST_WIDE_HIGH16;
import static com.android.dx.io.opcode.Opcodes.FILLED_NEW_ARRAY;
import static com.android.dx.io.opcode.Opcodes.FILLED_NEW_ARRAY_JUMBO;
import static com.android.dx.io.opcode.Opcodes.GOTO;
import static com.android.dx.io.opcode.Opcodes.GOTO_16;
import static com.android.dx.io.opcode.Opcodes.GOTO_32;
import static com.android.dx.io.opcode.Opcodes.MOVE;
import static com.android.dx.io.opcode.Opcodes.MOVE_16;
import static com.android.dx.io.opcode.Opcodes.MOVE_FROM16;
import static com.android.dx.io.opcode.Opcodes.MOVE_OBJECT;
import static com.android.dx.io.opcode.Opcodes.MOVE_OBJECT_16;
import static com.android.dx.io.opcode.Opcodes.MOVE_OBJECT_FROM16;
import static com.android.dx.io.opcode.Opcodes.MOVE_RESULT;
import static com.android.dx.io.opcode.Opcodes.MOVE_RESULT_OBJECT;
import static com.android.dx.io.opcode.Opcodes.MOVE_RESULT_WIDE;
import static com.android.dx.io.opcode.Opcodes.MOVE_WIDE;
import static com.android.dx.io.opcode.Opcodes.MOVE_WIDE_16;
import static com.android.dx.io.opcode.Opcodes.MOVE_WIDE_FROM16;
import static com.android.dx.io.opcode.Opcodes.RETURN;
import static com.android.dx.io.opcode.Opcodes.RETURN_OBJECT;
import static com.android.dx.io.opcode.Opcodes.RETURN_WIDE;
import static com.android.dx.io.opcode.RegisterReference.A;
import static com.android.dx.io.opcode.RegisterReference.B;
import static com.android.dx.io.opcode.format.InstructionCodec.FORMAT_22X;
import static com.android.dx.io.opcode.format.InstructionCodec.FORMAT_32X;

import java.util.EnumSet;

import android.util.SparseIntArray;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.TwoRegisterDecodedInstruction;
import com.android.dx.io.opcode.RegisterReference;

// TODO : implement the rest of this.

/**
 * This class contains static utility functions that analyze register maps and
 * converts DecodedInstructions into their alternate forms that will work with
 * the register values in the register map.
 * 
 * @author rschilling
 * 
 */
public class InstructionConverter {

	public static final int NIBBLE_MAX = 0xf; // vA
	public static final int BYTE_MAX = 0xff; // vAA
	public static final int SHORT_MAX = 0xffff; // vAAAA

	/**
	 * Given an instruction and a register map, converts the instruction to a
	 * form that can handle the register values in the register map.
	 * 
	 * <p>
	 * Each key in registerMap is expected to be register reference and each
	 * value is the register reference that the key maps to.
	 * 
	 * @param instruction
	 *            the instruction to convert.
	 * @param registerMap
	 *            the register map to determine what conversion should be done.
	 * @return the DecodedInstruction that takes the place of instruction so
	 *         that the register values can be handled.
	 */
	public static DecodedInstruction convert(DecodedInstruction instruction,
			SparseIntArray registerMap) {

		if (instruction == null)
			throw new IllegalArgumentException("instruction cannot be null.");

		if (registerMap == null)
			throw new IllegalArgumentException("registerMap cannot be null.");

		switch (instruction.getOpcode()) {
		case MOVE:
		case MOVE_FROM16:
		case MOVE_16:
		case MOVE_WIDE:
		case MOVE_WIDE_FROM16:
		case MOVE_WIDE_16:
			return convertMove(instruction, registerMap);

		case MOVE_OBJECT:
		case MOVE_OBJECT_FROM16:
		case MOVE_OBJECT_16:
			return convertMoveObject(instruction, registerMap);

		case MOVE_RESULT:
		case MOVE_RESULT_WIDE:
		case MOVE_RESULT_OBJECT:
			return convertMoveResult(instruction, registerMap);

		case RETURN:
		case RETURN_WIDE:
		case RETURN_OBJECT:
			return convertReturn(instruction, registerMap);

		case CONST_4:
		case CONST_16:
		case CONST:
		case CONST_HIGH16:
		case CONST_WIDE_16:
		case CONST_WIDE_32:
		case CONST_WIDE:
		case CONST_WIDE_HIGH16:
			return convertConst(instruction, registerMap);

		case CONST_STRING:
		case CONST_STRING_JUMBO:
			return convertConstString(instruction, registerMap);

		case ARRAY_LENGTH:
			return convertArrayLength(instruction, registerMap);

		case FILLED_NEW_ARRAY:
		case FILLED_NEW_ARRAY_JUMBO: // not identified on the webpage.
			return convertFilledNewArray(instruction, registerMap);

		case GOTO:
		case GOTO_16:
		case GOTO_32:
			return instruction;
		default:
			return checkRegisterValues(instruction, registerMap);

		}
	}

	private static DecodedInstruction convertFilledNewArray(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction checkRegisterValues(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertArrayLength(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertConstString(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertConst(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertReturn(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertMoveResult(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertMoveObject(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private static DecodedInstruction convertMove(
			DecodedInstruction instruction, SparseIntArray registerMap) {

		checkExists(EnumSet.of(A, B), instruction, registerMap);

		int regA = instruction.getA();
		int mapA = registerMap.get(regA);

		int regB = instruction.getB();
		int mapB = registerMap.get(regB);

		DecodedInstruction result = null;

		switch (instruction.getOpcode()) {

		case MOVE:
			if (mapA > BYTE_MAX || mapB > BYTE_MAX)
				// vAAAA
				result = new TwoRegisterDecodedInstruction(FORMAT_32X, MOVE_16,
						0, null, 0, 0, instruction.getLiteral(), mapA, mapB);
			else if (mapA > NIBBLE_MAX || mapB > NIBBLE_MAX)
				// vAA
				result = new TwoRegisterDecodedInstruction(FORMAT_22X,
						MOVE_FROM16, 0, null, 0, 0, 0L, mapA, mapB);
			else
				return instruction;

		default:
			return instruction;
		}
	}

	private static void checkExists(EnumSet<RegisterReference> eSet,
			DecodedInstruction instruction, SparseIntArray registerMap) {

		for (RegisterReference ref : eSet) {
			if (registerMap.get(instruction.getRegister(ref), -1) == -1)
				throw new IllegalStateException(
						String.format("Register reference %s not found in register map."));

		}

	}
}
