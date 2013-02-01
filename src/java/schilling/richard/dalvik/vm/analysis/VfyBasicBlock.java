package schilling.richard.dalvik.vm.analysis;

import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanBranch;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanContinue;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanReturn;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanSwitch;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanThrow;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_GOTO;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_GOTO_16;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_GOTO_32;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_EQ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_EQZ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_GE;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_GEZ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_GT;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_GTZ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_LE;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_LEZ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_LT;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_LTZ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_NE;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_IF_NEZ;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_PACKED_SWITCH;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_SPARSE_SWITCH;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.dexOpcodeFromCodeUnit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import schilling.richard.dalvik.vm.BitVector;
import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.CodeVerify.VerifierData;
import schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes;
import schilling.richard.dexlib.Code.InstrUtils;
import schilling.richard.dexlib.Code.Opcode;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.Code.Try;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Structure representing a basic block.
 * 
 * This is used for liveness analysis, which is a reverse-flow algorithm, so we
 * need to mantain a list of predecessors for each block.
 * 
 * "liveRegs" indicates the set of registers that are live at the end of the
 * basic block (after the last instruction has executed). Successor blocks will
 * compare their results with this to see if this block needs to be
 * re-evaluated. Note that this is not the same as the contents of the
 * RegisterLine for the last instruction in the block (which reflects the state
 * *before* the instruction has executed).
 */
public class VfyBasicBlock {
	public int firstAddr; /* address of first instruction */
	public int lastAddr; /* address of last instruction */
	public SparseArray<VfyBasicBlock> predecessors = new SparseArray<VfyBasicBlock>(); /*
																						 * set
																						 * of
																						 * basic
																						 * blocks
																						 * that
																						 * can
																						 * flow
																						 * here
																						 */
	public BitVector liveRegs; /* liveness for each register */
	public boolean changed; /* input set has changed, must re-eval */
	public boolean visited; /* block has been visited at least once */

	public static final String LOG_TAG = "VfyBasicBlock";
	public static final boolean verbose = true;

	/*
	 * Returns "true" if the instruction represents a data chunk, such as a
	 * switch statement block.
	 */
	public static boolean isDataChunk(short insn) {
		return (insn == GenDexOpcodes.kPackedSwitchSignature
				|| insn == GenDexOpcodes.kSparseSwitchSignature || insn == GenDexOpcodes.kArrayDataSignature);
	}

	public static boolean isDataChunk(DecodedInstruction insn) {
		return isDataChunk(insn.getOpcodeUnit());
	}

	/*
	 * Extract the relative offset from a branch instruction.
	 * 
	 * Returns "false" on failure (e.g. this isn't a branch instruction).
	 */
	static boolean dvmGetBranchOffset(VerifierData vdata, int curOffset,
			AtomicInteger pOffset, AtomicBoolean pConditional) {

		short[] insns = vdata.method.instructions();

		switch (insns[curOffset] & 0xff) {
		case OP_GOTO:
			pOffset.set(insns[0] >> 8);
			pConditional.set(false);
			break;
		case OP_GOTO_32:
			pOffset.set(insns[1] | (((int) insns[2]) << 16));
			pConditional.set(false);
			break;
		case OP_GOTO_16:
			pOffset.set(insns[1]);
			pConditional.set(false);
			break;
		case OP_IF_EQ:
		case OP_IF_NE:
		case OP_IF_LT:
		case OP_IF_GE:
		case OP_IF_GT:
		case OP_IF_LE:
		case OP_IF_EQZ:
		case OP_IF_NEZ:
		case OP_IF_LTZ:
		case OP_IF_GEZ:
		case OP_IF_GTZ:
		case OP_IF_LEZ:
			pOffset.set(insns[1]);
			pConditional.set(true);
			break;
		default:
			return false;

		}

		return true;
	}

	/*
	 * Generate a list of basic blocks and related information.
	 * 
	 * On success, returns "true" with vdata->basicBlocks initialized.
	 */
	public static boolean dvmComputeVfyBasicBlocks(VerifierData vdata) {

		boolean result = false;

		int numHandlers = 0;
		int blockStartAddr;

		InstructionList iList = vdata.method.instructionList();
		int insnsSize = vdata.method.insnsSize();
		Try[] pTries = vdata.method.getTries();
		CatchHandler[] handlerList = vdata.method.getCatchHandlers();

		if (Log.isLoggable(LOG_TAG, Log.VERBOSE)) {
			String logString = String.format("Basic blocks for %s.%s:%s",
					vdata.method.getDeclaringClass().getSignature(),
					vdata.method.getMethodName(), vdata.method.getShorty());
			Log.i(LOG_TAG, logString);
		}

		/*
		 * Allocate a data structure that allows us to map from an address to
		 * the corresponding basic block. Initially all pointers are NULL. They
		 * are populated on demand as we proceed (either when we reach a new BB,
		 * or when we need to add an item to the predecessor list in a
		 * not-yet-reached BB).
		 * 
		 * Only the first instruction in the block points to the BB structure;
		 * the rest remain NULL.
		 */
		vdata.basicBlocks = new SparseArray<VfyBasicBlock>();

		/*
		 * The "tries" list is a series of non-overlapping regions with a list
		 * of "catch" handlers. Rather than do the "find a matching try block"
		 * computation at each step, we just walk the "try" list in parallel.
		 * 
		 * Not all methods have "try" blocks. If this one does, we init tryEnd
		 * to zero, so that the (exclusive bound) range check trips immediately.
		 */
		int tryIndex = 0, tryStart = 0, tryEnd = 0;

		int debugBBIndex = 0;

		/*
		 * The address associated with a basic block is the start address.
		 */
		blockStartAddr = 0;
		for (int i = 0; i < iList.size(); i++) {
			int idx = iList.keyAt(i);
			/*
			 * Make sure we're pointing at the right "try" block. It should not
			 * be possible to "jump over" a block, so if we're no longer in the
			 * correct one we can just advance to the next.
			 */
			if (pTries != null && idx >= tryEnd) {
				if (tryIndex == pTries.length) {
					/* no more try blocks in this method */
					pTries = null;
					numHandlers = 0;
				} else {
					/*
					 * Extract the set of handlers. We want to avoid doing this
					 * for each block, so we copy them to local storage. If it
					 * doesn't fit in the small stack area, we'll use the heap
					 * instead.
					 * 
					 * It's rare to encounter a method with more than half a
					 * dozen possible handlers.
					 */
					numHandlers = handlerList.length;

					String vString = String.format(
							"+++ start=%x end=%x numHan=%d", tryStart, tryEnd,
							numHandlers);
					Log.v(LOG_TAG, vString);

					tryIndex++;
				}
			}

			/*
			 * Check the current instruction, and possibly aspects of the next
			 * instruction, to see if this instruction ends the current basic
			 * block.
			 * 
			 * Instructions that can throw only end the block if there is the
			 * possibility of a local handler catching the exception.
			 */
			DecodedInstruction inst = iList.get(idx);
			Opcode opcode = inst.getOpcodeEnum();
			byte opFlags = InstrUtils.dexGetFlagsFromOpcode(opcode);

			// FIXME: no check for end of array was made in the original code.
			int nextIdx = iList.keyAt(i + 1);
			boolean endBB = false;
			boolean ignoreInstr = false;

			if ((opFlags & kInstrCanContinue) == 0) {
				/* does not continue */
				endBB = true;
			} else if ((opFlags & (kInstrCanBranch | kInstrCanSwitch)) != 0) {
				/* conditionally branches elsewhere */
				endBB = true;
			} else if ((opFlags & kInstrCanThrow) != 0
					&& iList.dvmInsnIsInTry(idx)) {
				/* throws an exception that might be caught locally */
				endBB = true;
			} else if (iList.isDataChunk(idx)) {
				/*
				 * If this is a data chunk (e.g. switch data) we want to skip
				 * over it entirely. Set endBB so we don't carry this along as
				 * the start of a block, and ignoreInstr so we don't try to open
				 * a basic block for this instruction.
				 */
				endBB = ignoreInstr = true;
			} else if (iList.dvmInsnIsBranchTarget(nextIdx)) {
				/*
				 * We also need to end it if the next instruction is a branch
				 * target. Note we've tagged exception catch blocks as such.
				 * 
				 * If we're this far along in the "else" chain, we know that
				 * this isn't a data-chunk NOP, and control can continue to the
				 * next instruction, so we're okay examining "nextIdx".
				 */
				if (nextIdx >= insnsSize)
					throw new IllegalStateException(
							"processing error - index >= instructionCount");

				endBB = true;
			} else if (opcode.value == GenDexOpcodes.OP_NOP
					&& iList.isDataChunk(nextIdx)) {
				/*
				 * Handle an odd special case: if this is NOP padding before a
				 * data chunk, also treat it as "ignore". Otherwise it'll look
				 * like a block that starts and doesn't end.
				 */
				endBB = ignoreInstr = true;
			} else {
				/* check: return ops should be caught by absence of can-continue */
				assert ((opFlags & kInstrCanReturn) == 0);
			}

			if (verbose) {
				char btc = iList.dvmInsnIsBranchTarget(idx) ? '>' : ' ';
				char tryc = (pTries != null && idx >= tryStart && idx < tryEnd) ? 't'
						: ' ';
				boolean startBB = (idx == blockStartAddr);
				String startEnd;

				if (ignoreInstr)
					startEnd = "IGNORE";
				else if (startBB && endBB)
					startEnd = "START/END";
				else if (startBB)
					startEnd = "START";
				else if (endBB)
					startEnd = "END";
				else
					startEnd = "-";

				String logString = String.format("%04x: %c%c%s #%d", idx, tryc,
						btc, startEnd, debugBBIndex);

				Log.i(LOG_TAG, logString);
				if (pTries != null && idx == tryStart) {
					if (numHandlers <= 0)
						throw new RuntimeException("no handlers");
					// assert(numHandlers > 0);

					Log.i(LOG_TAG, logString);

				}
			}

			// FIXME: the original C code should have never used arrays, but
			// rather sparse arrays.
			if (idx != blockStartAddr) {
				/* should not be a basic block struct associated with this addr */
				// assert(vdata.basicBlocks[idx] == NULL);
				if (vdata.basicBlocks.get(idx) != null)
					throw new RuntimeException(
							"there should not be a basic block structure associated with this address.");
			}
			if (endBB) {
				if (!ignoreInstr) {
					/*
					 * Create a new BB if one doesn't already exist.
					 */
					VfyBasicBlock curBlock = vdata.basicBlocks
							.get(blockStartAddr);
					if (curBlock == null) {
						curBlock = new VfyBasicBlock();
						vdata.basicBlocks.put(blockStartAddr, curBlock);
					}

					curBlock.firstAddr = blockStartAddr;
					curBlock.lastAddr = idx;

					setPredecessors(vdata, curBlock, idx, opFlags, nextIdx,
							handlerList, numHandlers);

				}

				blockStartAddr = nextIdx;
				debugBBIndex++;
			}

			idx = nextIdx;
		}

		result = true;

		// TOO implement dumpBasicBlocks
		// if (verbose) dumpBasicBlocks(vdata);

		return result;
	}

	/**
	 * Add ourselves to the predecessor list in all blocks we might transfer
	 * control to.
	 * 
	 * <p>
	 * There are four ways to proceed to a new instruction: (1) continue to the
	 * following instruction (2) [un]conditionally branch to a specific location
	 * (3) conditionally branch through a "switch" statement (4) throw an
	 * exception
	 * 
	 * <p>
	 * Returning from the method (via a return statement or an uncaught
	 * exception) are not interesting for liveness analysis.
	 */
	private static void setPredecessors(VerifierData vdata,
			VfyBasicBlock curBlock, int curIdx, byte opFlags, int nextIdx,
			CatchHandler[] handlerList, int numHandlers) {

		short[] insns = vdata.method.instructions();
		InstructionList iList = vdata.method.instructionList();

		// add the current block to all catch handlers.
		for (CatchHandler cHandler : handlerList) {
			// TODO: make sure that specifying the catch all address is
			// appropriate. That's the only thing that makes sense so far.

			addToPredecessor(vdata, curBlock, cHandler.getCatchAllAddress());
		}

		if ((opFlags & kInstrCanContinue) != 0) {
			addToPredecessor(vdata, curBlock, nextIdx);
		}

		if ((opFlags & kInstrCanBranch) != 0) {
			boolean unused = false, gotBranch;
			int branchOffset = 0, absOffset;

			AtomicInteger aiBranchOffset = new AtomicInteger(branchOffset);
			AtomicBoolean abUnused = new AtomicBoolean(unused);
			gotBranch = dvmGetBranchOffset(vdata, curIdx, aiBranchOffset,
					abUnused);
			branchOffset = aiBranchOffset.get();
			unused = abUnused.get();

			if (!gotBranch)
				throw new RuntimeException("fetch of branch failed");
			// assert(gotBranch);

			absOffset = curIdx + branchOffset;
			assert (absOffset >= 0 && absOffset < vdata.insnsSize());

			addToPredecessor(vdata, curBlock, absOffset);
		}

		if ((opFlags & kInstrCanSwitch) != 0) {
			DecodedInstruction dInstr = iList.get(curIdx);
			int dataPtr;

			int offsetToData = (int) insns[curIdx + 1]
					| ((int) insns[curIdx + 2]) << 16;
			dataPtr = curIdx + offsetToData;

			/*
			 * dataPtr points to the start of the switch data. The first item is
			 * the NOP+magic, the second is the number of entries in the switch
			 * table.
			 */
			int switchCount = insns[dataPtr + 1];

			/*
			 * Skip past the ident field, size field, and the first_key field
			 * (for packed) or the key list (for sparse).
			 */
			if (dexOpcodeFromCodeUnit(insns[curIdx]) == OP_PACKED_SWITCH) {
				dataPtr += 4;
			} else {
				assert (dexOpcodeFromCodeUnit(insns[curIdx]) == OP_SPARSE_SWITCH);
				dataPtr += 2 + 2 * switchCount;
			}

			int switchIdx;
			for (switchIdx = 0; switchIdx < switchCount; switchIdx++) {
				int offset, absOffset;

				offset = insns[dataPtr + (switchIdx * 2)]
						| (insns[dataPtr + (switchIdx * 2) + 1] << 16);
				absOffset = curIdx + offset;
				if (absOffset < 0 || absOffset >= insns.length)
					throw new IllegalStateException("absOffset has a bad value");

				addToPredecessor(vdata, curBlock, absOffset);
			}
		}

		if (curBlock.predecessors.size() > 256) {
			String logString = String.format(
					"Lots of preds at 0x%04x in %s.%s:%s", curIdx, vdata.method
							.getDeclaringClass().getSignature(), vdata.method
							.getMethodName(), vdata.method.getShorty());
			Log.w(LOG_TAG, logString);
		}
	}

	/**
	 * Add "curBlock" to the predecessor list in "targetIdx".
	 * <p>
	 * The original function only returned false if the target block could not
	 * be allocated. We don't have to check for that condition in Java.
	 */
	private static void addToPredecessor(VerifierData vdata,
			VfyBasicBlock curBlock, int targetIdx) {

		/*
		 * Allocate the target basic block if necessary. This will happen on
		 * e.g. forward branches.
		 * 
		 * We can't fill in all the fields, but that will happen automatically
		 * when we get to that part of the code.
		 */
		VfyBasicBlock targetBlock = vdata.basicBlocks.get(targetIdx);
		if (targetBlock == null) {

			targetBlock = new VfyBasicBlock();

			vdata.basicBlocks.put(targetIdx, targetBlock);
			// TODO modify basicBlocks to always return a value if necessary.
		}

		SparseArray<VfyBasicBlock> preds = targetBlock.predecessors;

		if (preds.get(targetIdx) != null) {

			/*
			 * This happens sometimes for packed-switch instructions, where the
			 * same target address appears more than once. Also, a (pointless)
			 * conditional branch to the next instruction will trip over this.
			 */
			String logString = String
					.format("ODD: point set for targ=0x%04x (%p) already had block fir=0x%04x (%p)",
							targetIdx, targetBlock, curBlock.firstAddr,
							curBlock);
			Log.v(LOG_TAG, logString);

		} else {
			preds.put(targetIdx, curBlock);
		}
	}

}
