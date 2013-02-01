package schilling.richard.dalvik.vm;

import static schilling.richard.dalvik.vm.CodeVerifyConstants.kInsnFlagBranchTarget;
import static schilling.richard.dalvik.vm.CodeVerifyConstants.kInsnFlagChanged;
import static schilling.richard.dalvik.vm.CodeVerifyConstants.kInsnFlagGcPoint;
import static schilling.richard.dalvik.vm.CodeVerifyConstants.kInsnFlagInTry;
import static schilling.richard.dalvik.vm.CodeVerifyConstants.kInsnFlagVisited;
import static schilling.richard.dalvik.vm.CodeVerifyConstants.kInsnFlagWidthMask;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_MOVE_EXCEPTION;
import schilling.richard.dalvik.vm.analysis.RegisterTable;
import schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.ShortArrayCodeOutput;
import com.android.dx.merge.MethodDefinition;

/**
 * A list of instructions.
 * <p>
 * This corresponds to the instructions that are found in a DEX
 * <code>Code</code> object. The key to the sparse array is the instruction
 * address.
 * 
 * <p>
 * The keys of each instruction correspond to each instruction's relative
 * position to each other. When decoded, the keys are conveniently set to each
 * instruction's address. However, this does not prevent instructions from being
 * added in any particular ordder. When the instructions are encoded they will
 * be encoded in the order of the list and the keys are ignored.
 * 
 * <p>
 * The state of the keys therefore is undefined. Do not rely on them unless you
 * know for sure that the list was not modified since it was first created.
 * 
 * @author rschilling
 */
public class InstructionList extends SparseArray<DecodedInstruction> {

	public static final boolean LOG_CONVERTED_INSTRUCTIONS = true;
	public static final boolean WARN_CONVERTED_INSTRUCTIONS = true;
	public static final String LOG_TAG_CONVERTED = "InstructionConversion";
	public static final boolean LOG_INSTRUCTIONS = false;

	/**
	 * Returns "true" if the flags indicate that this address holds the start of
	 * an instruction.
	 */
	public boolean dvmInsnIsOpcode(int addr) {
		return (get(addr).insnFlags & kInsnFlagWidthMask) != 0;
	}

	/**
	 * Extract the unsigned 16-bit instruction width from "flags".
	 */
	public int dvmInsnGetWidth(int addr) {
		return get(addr).insnFlags & kInsnFlagWidthMask;
	}

	/*
	 * Changed?
	 */
	public boolean dvmInsnIsChanged(int addr) {
		return (get(addr).insnFlags & kInsnFlagChanged) != 0;
	}

	public void dvmInsnSetChanged(int addr, boolean changed) {
		if (changed)
			get(addr).insnFlags |= kInsnFlagChanged;
		else
			get(addr).insnFlags &= ~kInsnFlagChanged;
	}

	/*
	 * Visited?
	 */
	public boolean dvmInsnIsVisited(int addr) {
		return (get(addr).insnFlags & kInsnFlagVisited) != 0;
	}

	public void dvmInsnSetVisited(int addr, boolean changed) {
		if (changed)
			get(addr).insnFlags |= kInsnFlagVisited;
		else
			get(addr).insnFlags &= ~kInsnFlagVisited;
	}

	/*
	 * Visited or changed?
	 */
	public boolean dvmInsnIsVisitedOrChanged(int addr) {
		return (get(addr).insnFlags & (kInsnFlagVisited | kInsnFlagChanged)) != 0;
	}

	/*
	 * In a "try" block?
	 */
	public boolean dvmInsnIsInTry(int addr) {
		return (get(addr).insnFlags & kInsnFlagInTry) != 0;
	}

	void dvmInsnSetInTry(int addr, boolean inTry) {
		if (inTry)
			get(addr).insnFlags |= kInsnFlagInTry;
		else
			get(addr).insnFlags &= ~kInsnFlagInTry;
	}

	/*
	 * Instruction is a branch target or exception handler?
	 */
	public boolean dvmInsnIsBranchTarget(int addr) {
		return (get(addr).insnFlags & kInsnFlagBranchTarget) != 0;
	}

	public void dvmInsnSetBranchTarget(int addr, boolean isBranch) {
		if (isBranch)
			get(addr).insnFlags |= kInsnFlagBranchTarget;
		else
			get(addr).insnFlags &= ~kInsnFlagBranchTarget;
	}

	/*
	 * Instruction is a GC point?
	 */
	public boolean dvmInsnIsGcPoint(int addr) {
		return (get(addr).insnFlags & kInsnFlagGcPoint) != 0;
	}

	public void dvmInsnSetGcPoint(int addr, boolean isGcPoint) {

		if (isGcPoint)
			get(addr).insnFlags |= kInsnFlagGcPoint;
		else
			get(addr).insnFlags &= ~kInsnFlagGcPoint;
	}

	private RegisterTable registerTable;

	public RegisterTable getRegisterTable() {
		if (registerTable == null)
			registerTable = new RegisterTable();
		return registerTable;
	}

	public static InstructionList allocate(DecodedInstruction[] instructions) {

		InstructionList result = new InstructionList();

		if (instructions == null)
			return result;

		for (int i = 0; i < instructions.length; i++) {

			if (instructions[i] == null)
				continue;

			result.put(i, instructions[i]);
		}

		return result;

	}

	public static InstructionList allocate(short[] instructions) {
		InstructionList result = new InstructionList();

		if (instructions == null)
			return result;

		DecodedInstruction[] decoded = DecodedInstruction
				.decodeAll(instructions);

		return allocate(decoded);
	}

	// TODO what can be done - what tool can be used to help developers write
	// clearly readable code. Remembering that code is written for other
	// developers? Enforce the selection of structures and objects subject to
	// constraints like the amount of memory used. The developer should "fee" as
	// though things are very productive when interacting with the tool.

	/**
	 * Checks all instructions and converts them to forms that can handle the
	 * register contents. The keys are untouched, but the values may change.
	 * 
	 * <p>
	 * The only side effect of this function call is that some of the
	 * instructions in the list may be modified.
	 * 
	 * <p>
	 * Keys will be modified to match the addresses in the instructions.
	 * 
	 * @returns the a list of positions that were modified.
	 */
	public void convert() {

		int modified = 0;
		for (int i = 0; i < size(); i++) {
			DecodedInstruction unconverted = valueAt(i);
			DecodedInstruction converted = unconverted.getFormat().convert(
					unconverted);

			if (unconverted != converted) {
				converted.setMethodDef(unconverted.getMethodDef());
				setValueAt(i, converted);
				modified++;

				if (LOG_CONVERTED_INSTRUCTIONS && unconverted != converted)
					Log.i(LOG_TAG,
							String.format("%d: %s -> %s", i,
									unconverted.toLongString(),
									converted.toLongString()));

			}

		}

		if (modified > 0) {

			MethodDefinition mDef = valueAt(0).getMethodDef(); // same for all

			// change occurred. Recalibrate the addresses
			InstructionList iList = DecodedInstruction
					.decodeAll(DecodedInstruction.decodeAll(mDef, this.encode()));
			clear();
			append(iList, 0, iList.size(), true);
		}

	}

	/**
	 * Encodes all the instructions in the list.
	 * 
	 * @return the instructions in this list in encoded form.
	 */
	public short[] encode() {

		// use the safe case for guessing resulting buffer length - resulting
		// array will never be longer than size() *
		// MAX_CODE_UNITS_PER_INSTRUCTION
		ShortArrayCodeOutput out = new ShortArrayCodeOutput(size()
				* DecodedInstruction.MAX_CODE_UNITS_PER_INSTRUCTION);

		// TODO: analyze memory usage.

		for (int i = 0; i < size(); i++) {

			DecodedInstruction ins = valueAt(i);

			if (LOG_INSTRUCTIONS)
				Log.i(LOG_TAG, String.format("%d: %s", i, ins.toLongString()));

			if (keyAt(i) != out.cursor()) {

				// can happen as a result of post inject instruction
				// manipulation. See BaseUpdater.

				if (InstructionList.WARN_CONVERTED_INSTRUCTIONS)
					Log.d(InstructionList.LOG_TAG_CONVERTED,
							String.format(
									"Address of instruction %s in position %d is out of sync with output cursor position %d",
									valueAt(i).toString(), i, out.cursor()));
				else
					throw new IllegalStateException(
							String.format(
									"Address of instruction %s is out of sync with output cursor position %d",
									valueAt(i).toString(), out.cursor()));

			}

			ins.encode(out);
		}

		return out.getArray();

	}

	/*
	 * Returns "true" if the instruction represents a data chunk, such as a
	 * switch statement block.
	 */
	public boolean isDataChunk(int idx) {
		int insn = get(idx).getOpcode();
		return (insn == GenDexOpcodes.kPackedSwitchSignature
				|| insn == GenDexOpcodes.kSparseSwitchSignature || insn == GenDexOpcodes.kArrayDataSignature);
	}

	/**
	 * Verify that the target instruction is not "move-exception". It's
	 * important that the only way to execute a move-exception is as the first
	 * instruction of an exception handler.
	 * 
	 * Returns "true" if all is well, "false" if the target instruction is
	 * move-exception.
	 */
	public boolean checkMoveException(int insnIdx) {

		if ((get(insnIdx).getOpcode() & 0xff) == OP_MOVE_EXCEPTION) {
			Log.e(LOG_TAG, "VFY: invalid use of move-exception");
			return false;
		}
		return true;
	}

	public static final String LOG_TAG = "InstructionList";

	/**
	 * Returns the maximum number of registers that are used in calls to other
	 * functions.
	 * 
	 * @return the outsSize parameter of the instruction set.
	 */
	public int calculateOutsSize() {

		int result = 0;
		int cur = 0;

		for (int i = 0; i < this.size(); i++) {
			DecodedInstruction instruction = this.valueAt(i);
			if (instruction.isInvokeRange())
				cur = instruction.getRegisterCount();
			else if (instruction.isInvoke())
				cur = instruction.getA();

			if (cur > result)
				result = cur;

		}

		return result;

	}

	/**
	 * Appends instructions from the specified list to the end of this list.
	 * After this operation is complete, keys will no longer match addresses of
	 * values.
	 * 
	 * @param from
	 *            the instruction list to read from
	 * @param position
	 *            the position in addFrom to start reading.
	 * @param length
	 *            the number of instructions to append.
	 * @param preserveKeys
	 *            if true, the keys in addFrom will be copied and not modified.
	 */
	public void append(InstructionList addFrom, int position, int length,
			boolean preserveKeys) {

		if (position >= addFrom.size())
			throw new IllegalArgumentException(
					"position is too big.  Must be less than size of addFrom");

		if (position < 0)
			throw new IllegalArgumentException("position is must be >= 0");

		int keyDiff = size() > 0 ? maxKey() + 1 : 0;

		for (int i = position; i < position + length; i++) {
			int newKey = preserveKeys ? addFrom.keyAt(i) : keyDiff
					+ (i - position);
			put(newKey, addFrom.valueAt(i));
		}

	}

	/**
	 * Returns the highest key value.
	 * 
	 * @return the highest key value.
	 */
	public int maxKey() {
		if (size() == 0)
			throw new IllegalStateException(
					"this list is empty. cannot determine maximum key value.");

		return keyAt(size() - 1);
	}

	/**
	 * Returns the last instruction in the list.
	 * 
	 * @return the last instruction in the list.
	 */
	public DecodedInstruction maxValue() {
		if (size() == 0)
			throw new IllegalStateException(
					"this list is empty. cannot determine maximum key value.");

		return valueAt(size() - 1);
	}

	public void recalibrateAddresses() {
		if (size() == 0)
			return;

		MethodDefinition mDef = valueAt(0).getMethodDef();
		InstructionList recalibrated = DecodedInstruction
				.decodeAll(DecodedInstruction.decodeAll(mDef, this.encode()));
		clear();
		append(recalibrated, 0, recalibrated.size(), true);
	}

}
