package schilling.richard.dalvik.vm.analysis;

import static schilling.richard.dalvik.vm.CodeVerifyConstants.checkMergeTab;
import static schilling.richard.dalvik.vm.analysis.DumpRegisterType.DRT_SHOW_LOCALS;
import static schilling.richard.dalvik.vm.analysis.DumpRegisterType.DRT_SHOW_REF_TYPES;
import static schilling.richard.dalvik.vm.analysis.DumpRegisterType.SHOW_REG_DETAILS;
import static schilling.richard.dalvik.vm.analysis.RegisterMapMode.kRegisterMapModeLivePrecise;
import static schilling.richard.dalvik.vm.analysis.RegisterTrackingMode.kTrackRegsBranches;
import static schilling.richard.dalvik.vm.analysis.RegisterTrackingMode.kTrackRegsGcPoints;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.OP_NOP;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.kArrayDataSignature;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.kPackedSwitchSignature;
import static schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes.kSparseSwitchSignature;

import java.util.List;

import schilling.richard.dalvik.vm.BitVector;
import schilling.richard.dalvik.vm.CodeVerifyConstants;
import schilling.richard.dalvik.vm.DvmGlobals;
import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.oo.ArrayClass;
import schilling.richard.dalvik.vm.oo.Clazz;
import schilling.richard.dalvik.vm.oo.DexModel;
import schilling.richard.dalvik.vm.oo.Field;
import schilling.richard.dalvik.vm.oo.Method;
import schilling.richard.dalvik.vm.oo.MethodType;
import schilling.richard.dalvik.vm.oo.ObjectClass;
import schilling.richard.dalvik.vm.oo.PrimitiveClass;
import schilling.richard.dalvik.vm.oo.VerifyException;
import schilling.richard.dalvik.vm.oo.predefined.PredefinedClasses;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.FieldId;
import com.android.dx.io.instructions.DecodedInstruction;

import dalvik.bytecode.Opcodes;

/**
 * 
 * <p>
 * Used the objects in the package schilling.richard.dalvik.vm.oo to analyze the
 * code.
 * <p>
 * Dalvik bytecode structural verifier. The only public entry point (except for
 * a few shared utility functions) is dvmVerifyCodeFlow().
 * 
 * TODO: might benefit from a signature-->class lookup cache. Could avoid some
 * string-peeling and wouldn't need to compute hashes.
 * 
 * <p>
 * An implementation of the CodeVerify.cpp module in Java. This module is found
 * in the Dalvik VM and is used to verify code that is loaded into the VM. Any
 * code we touch needs to pass the tests in CodeVerify.cpp and, ergo, this file
 * as well.
 * <p>
 * This is as much of a direct port of the logic as possible.
 * <p>
 * Other projects have tried to do something similar (e.g. smali), but I think
 * the approach found in the .cpp module is a little more straightforward.
 * 
 * 
 * 
 * 
 * @author rschilling
 */
public class CodeVerify {

	/**
	 * Table that maps uninitialized instances to classes, based on the address
	 * of the new-instance instruction. One per method.
	 * <p>
	 * The integer is the code offset, or -1 for method ara ("this").
	 * <p>
	 * The object is the class created at that address.
	 */
	public SparseArray<Clazz> UninitInstanceMap = new SparseArray<Clazz>();

	/*
	 * InsnFlags is a 32-bit integer with the following layout: 0-15 instruction
	 * length (or 0 if this address doesn't hold an opcode) 16-31 single bit
	 * flags: InTry: in "try" block; exceptions thrown here may be caught
	 * locally BranchTarget: other instructions can branch to this instruction
	 * GcPoint: this instruction is a GC safe point Visited: verifier has
	 * examined this instruction at least once Changed: set/cleared as bytecode
	 * verifier runs
	 */
	// typedef u4 InsnFlags;
	// TODO verify that an int is 4 bytes.

	/**
	 * Various bits of data used by the verifier and register map generator.
	 */
	public static final class VerifierData {

		/*
		 * The method we're working on.
		 */
		public Method method;

		/**
		 * Uninitialized instance map, used for tracking the movement of objects
		 * that have been allocated but not initialized.
		 */
		public UninitInstanceMap uninitMap = new UninitInstanceMap();

		/**
		 * Array of RegisterLine structs, one entry per code unit. We only need
		 * entries for code units that hold the start of an "interesting"
		 * instruction. For register map generation, we're only interested in GC
		 * points.
		 */
		public SparseArray<RegisterLine> registerLines = new SparseArray<RegisterLine>();

		// TODO: What's the purpose of having a regType sparse array here and
		// also in registerLines?
		public SparseArray<RegType> regTypes = new SparseArray<RegType>();

		/*
		 * The number of occurrences of specific opcodes.
		 */
		int newInstanceCount;
		public int monitorEnterCount;

		/*
		 * Array of pointers to basic blocks, one entry per code unit. Used for
		 * liveness analysis.
		 */
		public SparseArray<VfyBasicBlock> basicBlocks;

		/*
		 * Number of code units of instructions in the method. A cache of the
		 * value calculated by dvmGetMethodInsnsSize().
		 */
		public int insnsSize() {
			return method.instructions().length;
		}

		/*
		 * Number of registers we track for each instruction. This is equal to
		 * the method's declared "registersSize". (Does not include the pending
		 * return value.)
		 */
		public int insnRegCount() {
			return method.getRegistersSize();
		}

	};

	/**
	 * Create a new uninitialized instance map.
	 * <p>
	 * The map is allocated and populated with address entries. The addresses
	 * appear in ascending order to allow binary searching.
	 * <p>
	 * Very few methods have 10 or more new-instance instructions; the majority
	 * have 0 or 1. Occasionally a static initializer will have 200+.
	 * <p>
	 * TODO: merge this into the static pass or initRegisterTable; want to avoid
	 * walking through the instructions yet again just to set up this table
	 */
	SparseArray<Clazz> dvmCreateUninitInstanceMap(Method meth, int[] insnFlags,
			short[] insns, int newInstanceCount) {

		InstructionList instructions = InstructionList.allocate(insns);

		SparseArray<Clazz> uninitMap = new SparseArray<Clazz>();
		boolean isInit = false;

		if (meth.isInitMethod()) {
			newInstanceCount++;
			isInit = true;
		}

		/*
		 * Run through and find the new-instance instructions.
		 */

		for (int i = 0; i < instructions.size(); i++) {
			int addr = instructions.keyAt(i);
			DecodedInstruction inst = (DecodedInstruction) instructions.get(i);
			int opcode = inst.getOpcode();
			if (opcode == Opcodes.OP_NEW_INSTANCE
					|| opcode == Opcodes.OP_NEW_INSTANCE_JUMBO)
				uninitMap.put(addr, null);

		}

		return uninitMap;
	}

	/*
	 * Release the storage associated with an UninitInstanceMap.
	 */
	void dvmFreeUninitInstanceMap(SparseArray<Clazz> uninitMap) {
		throw new UnsupportedOperationException(
				"this is a no-op function in this version.");
	}

	public static final String LOG_TAG = "CodeVerify";

	/*
	 * Verify bytecode in "meth". "insnFlags" should be populated with
	 * instruction widths and "in try" flags. Entry point for the detailed
	 * code-flow analysis of a single method. <p> In the C program, the pointer
	 * to the start of the Dex File is
	 */
	boolean dvmVerifyCodeFlow(VerifierData vdata) throws VerifyException {

		boolean result = false;
		boolean generateRegisterMap = DvmGlobals.gDvm.generateRegisterMaps;

		int insnsSize = vdata.insnsSize();
		Method meth = vdata.method;
		int regSize = meth.getRegistersSize();
		InstructionList instructions = meth.instructionList();

		RegisterTable regTable = new RegisterTable();
		vdata.registerLines = regTable.registerLines;

		if (VERIFIER_STATS) {
			DvmGlobals.gDvm.verifierStats.methodsExamined++;
			if (vdata.monitorEnterCount > 0)
				DvmGlobals.gDvm.verifierStats.monEnterMethods++;
		}

		/*
		 * TODO: move this elsewhere -- we don't need to do this for every
		 * method
		 */
		verifyPrep();

		if (regSize * insnsSize > CODE_SIZE_WARN_LEVEL) {
			Log.w(LOG_TAG, "VFY: warning: method is huge (regs=" + regSize
					+ " insnsSize=" + insnsSize + ")");
			/* might be bogus data, might be some huge generated method */
		}

		/*
		 * Create register lists, and initialize them to "Unknown". If we're
		 * also going to create the register map, we need to retain the register
		 * lists for a larger set of addresses.
		 */
		if (!initRegisterTable(vdata, regTable,
				generateRegisterMap ? kTrackRegsGcPoints : kTrackRegsBranches))
			return false;

		/*
		 * Perform liveness analysis.
		 * 
		 * We can do this before or after the main verifier pass. The choice
		 * affects whether or not we see the effects of verifier instruction
		 * changes, i.e. substitution of throw-verification-error.
		 * 
		 * In practice the ordering doesn't really matter, because T-V-E just
		 * prunes "can continue", creating regions of dead code (with
		 * corresponding register map data that will never be used).
		 */
		if (generateRegisterMap
				&& DvmGlobals.gDvm.registerMapMode == kRegisterMapModeLivePrecise) {
			/*
			 * Compute basic blocks and predecessor lists.
			 */
			if (!VfyBasicBlock.dvmComputeVfyBasicBlocks(vdata))
				return result;

			/*
			 * Compute liveness.
			 */
			if (!Liveness.dvmComputeLiveness(vdata))
				return result;
		}

		/*
		 * Initialize the types of the registers that correspond to the method
		 * arguments. We can determine this from the method signature.
		 */
		if (!setTypesFromSignature(vdata))
			return result;

		/*
		 * Run the verifier.
		 */
		if (!doCodeVerification(vdata, regTable))
			return result;

		return true;
	}

	/**
	 * Set the register types for the first instruction in the method based on
	 * the method signature.
	 * 
	 * This has the side-effect of validating the signature.
	 * 
	 * Returns "true" on success.
	 * 
	 * @deprecated
	 */
	boolean setTypesFromSignature(VerifierData vdata) throws VerifyException {
		throw new UnsupportedOperationException("deprecated");
	}

	/**
	 * return the RegType for the uninitialized reference in slot "uidx"
	 * 
	 * @deprecated
	 */
	static RegType regTypeFromUninitIndex(int uidx) {
		throw new UnsupportedOperationException("deprecated");
	}

	/**
	 * convert the ClassObject pointer to an (initialized) register type
	 * 
	 * @deprecated
	 */
	public static RegType regTypeFromClass(Clazz clazz) {
		throw new UnsupportedOperationException("deprecated");

	}

	/**
	 * Initialize the RegisterTable.
	 * 
	 * Every instruction address can have a different set of information about
	 * what's in which register, but for verification purposes we only need to
	 * store it at branch target addresses (because we merge into that).
	 * 
	 * By zeroing out the regType storage we are effectively initializing the
	 * register information to kRegTypeUnknown.
	 * 
	 * We jump through some hoops here to minimize the total number of
	 * allocations we have to perform per method verified.
	 * 
	 * @deprecated
	 */
	static boolean initRegisterTable(VerifierData vdata,
			RegisterTable regTable, RegisterTrackingMode trackRegsFor) {
		throw new UnsupportedOperationException("deprecated");
	}

	public static final int CODE_SIZE_WARN_LEVEL = 4 * 1024 * 1024;
	public static final boolean DEAD_CODE_SCAN = true;

	/*
	 * One-time preparation.
	 */
	static void verifyPrep() {
		/* only need to do this if the table was updated */
		checkMergeTab();
	}

	/**
	 * @deprecated
	 * @param registerLine
	 * @param pDecInsn
	 * @return
	 * @throws VerifyException
	 */
	public static RegType getInvocationThis(RegisterLine registerLine,
			DecodedInstruction pDecInsn) throws VerifyException {
		throw new UnsupportedOperationException("deprecated");

	}

	/**
	 * Verify that the contents of the specified register have the specified
	 * type (or can be converted to it through an implicit widening conversion).
	 * 
	 * This will modify the type of the source register if it was originally
	 * derived from a constant to prevent mixing of int/float and long/double.
	 * 
	 * If "vsrc" is a reference, both it and the "vsrc" register must be
	 * initialized ("vsrc" may be Zero). This will verify that the value in the
	 * register is an instance of checkType, or if checkType is an interface,
	 * verify that the register implements checkType.
	 * 
	 * @throws VerifyException
	 * @deprecated
	 */
	static void verifyRegisterType(RegisterLine registerLine, int vsrc,
			RegType checkType) throws VerifyException {
		throw new UnsupportedOperationException("deprecated");
	}

	public static final boolean DEBUG_VERBOSE_VERIFICATION = true;
	public static final boolean DEBUG_VERBOSE = true;

	/**
	 * Grind through the instructions.
	 * 
	 * <p>
	 * The basic strategy is as outlined in v3 4.11.1.2: set the "changed" bit
	 * on the first instruction, process it (setting additional "changed" bits),
	 * and repeat until there are no more.
	 * 
	 * <pre>
	 * v3 4.11.1.1
	 * - (N/A) operand stack is always the same size
	 * - operand stack [registers] contain the correct types of values
	 * - local variables [registers] contain the correct types of values
	 * - methods are invoked with the appropriate arguments
	 * - fields are assigned using values of appropriate types
	 * - opcodes have the correct type values in operand registers
	 * - there is never an uninitialized class instance in a local variable in
	 *   code protected by an exception handler (operand stack is okay, because
	 *   the operand stack is discarded when an exception is thrown) [can't
	 *   know what's a local var w/o the debug info -- should fall out of
	 *   register typing]
	 * 
	 * v3 4.11.1.2
	 * - execution cannot fall off the end of the code
	 * </pre>
	 * 
	 * <p>
	 * (We also do many of the items described in the "static checks" sections,
	 * because it's easier to do them here.)
	 * 
	 * <p>
	 * We need an array of RegType values, one per register, for every
	 * instruction. If the method uses monitor-enter, we need extra data for
	 * every register, and a stack for every "interesting" instruction. In
	 * theory this could become quite large -- up to several megabytes for a
	 * monster function.
	 * 
	 * <p>
	 * NOTE:
	 * <p>
	 * The spec forbids backward branches when there's an uninitialized
	 * reference in a register. The idea is to prevent something like this:
	 * 
	 * <pre>
	 *   loop:
	 *     move r1, r0
	 *     new-instance r0, MyClass
	 *     ...
	 *     if-eq rN, loop  // once
	 *   initialize r0
	 * </pre>
	 * 
	 * <p>
	 * This leaves us with two different instances, both allocated by the same
	 * instruction, but only one is initialized. The scheme outlined in v3
	 * 4.11.1.4 wouldn't catch this, so they work around it by preventing
	 * backward branches. We achieve identical results without restricting code
	 * reordering by specifying that you can't execute the new-instance
	 * instruction if a register contains an uninitialized instance created by
	 * that same instrutcion.
	 * 
	 * @throws VerifyException
	 */
	static boolean doCodeVerification(VerifierData vdata, RegisterTable regTable)
			throws VerifyException {

		int insnsSize = vdata.insnsSize();
		boolean result = false;
		int insnIdx, startGuess;

		InstructionList iList = vdata.method.instructionList();
		/*
		 * Begin by marking the first instruction as "changed".
		 */
		iList.dvmInsnSetChanged(0, true);

		if (DEBUG_VERBOSE_VERIFICATION) { // if
											// (dvmWantVerboseVerification(meth))
											// {
			String logString = String.format(
					"Now verifying: %s.%s %s (ins=%d regs=%d)", vdata.method
							.getDeclaringClass().getSignature(), vdata.method
							.getMethodName(), vdata.method.getPrototype()
							.toString(), vdata.method.getInsSize(),
					vdata.method.getRegistersSize());
			Log.i(LOG_TAG,
					" ------ [0    4    8    12   16   20   24   28   32   36");
		}

		startGuess = 0;

		/*
		 * Continue until no instructions are marked "changed".
		 */
		while (true) {
			/*
			 * Find the first marked one. Use "startGuess" as a way to find one
			 * quickly.
			 */
			for (insnIdx = startGuess; insnIdx < insnsSize; insnIdx++) {
				if (iList.dvmInsnIsChanged(insnIdx))
					break;
			}

			if (insnIdx == insnsSize) {
				if (startGuess != 0) {
					/* try again, starting from the top */
					startGuess = 0;
					continue;
				} else {
					/* all flags are clear */
					break;
				}
			}

			/*
			 * We carry the working set of registers from instruction to
			 * instruction. If this address can be the target of a branch (or
			 * throw) instruction, or if we're skipping around chasing "changed"
			 * flags, we need to load the set of registers from the table.
			 * 
			 * Because we always prefer to continue on to the next instruction,
			 * we should never have a situation where we have a stray "changed"
			 * flag set on an instruction that isn't a branch target.
			 */
			if (iList.dvmInsnIsBranchTarget(insnIdx)) {
				try {
					RegisterLine workLine = (RegisterLine) regTable.registerLines
							.get(insnIdx).clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e);
				}
			} else {
				/*
				 * Sanity check: retrieve the stored register line (assuming a
				 * full table) and make sure it actually matches.
				 */
				RegisterLine registerLine = regTable.registerLines.get(insnIdx);

				if (regTable.registerLines.get(insnIdx).compareTo(
						regTable.workLine) != 0) {
					String logString = String.format(
							"HUH? workLine diverged in %s.%s %s", vdata.method
									.getDeclaringClass().getSignature(),
							vdata.method.getMethodName(), vdata.method
									.getPrototype().toString());

					dumpRegTypes(vdata, registerLine, 0, "work",
							vdata.uninitMap, DRT_SHOW_REF_TYPES
									| DRT_SHOW_LOCALS);
					dumpRegTypes(vdata, registerLine, 0, "insn",
							vdata.uninitMap, DRT_SHOW_REF_TYPES
									| DRT_SHOW_LOCALS);

					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC, logString);

				}
			}
			if (DEBUG_VERBOSE) {
				dumpRegTypes(vdata, regTable.workLine, insnIdx, null,
						vdata.uninitMap, SHOW_REG_DETAILS);
				Log.i(LOG_TAG, String.format("process %s.%s %s %d",
						vdata.method.getDeclaringClass().getSignature(),
						vdata.method.getMethodName(), vdata.method.getShorty(),
						insnIdx));
			}

			if (!verifyInstruction(vdata.method, iList.get(insnIdx), regTable,
					insnIdx, vdata.uninitMap, startGuess))

				throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
						"+++ %s bailing at %d", vdata.method.getMethodName(),
						insnIdx));

			/*
			 * Clear "changed" and mark as visited.
			 */
			iList.dvmInsnSetVisited(insnIdx, true);
			iList.dvmInsnSetChanged(insnIdx, false);
		}

		if (DEAD_CODE_SCAN && !vdata.method.IS_METHOD_FLAG_SET()) { // !IS_METHOD_FLAG_SET(meth,
																	// METHOD_ISWRITABLE))
																	// {
			/*
			 * Scan for dead code. There's nothing "evil" about dead code
			 * (besides the wasted space), but it indicates a flaw somewhere
			 * down the line, possibly in the verifier.
			 * 
			 * If we've substituted "always throw" instructions into the stream,
			 * we are almost certainly going to have some dead code.
			 */
			int deadStart = -1;
			for (int i = 0; i < iList.size(); i++) {
				insnIdx = iList.keyAt(i);

			}
			for (int i = 0; i < iList.size(); i++) {
				insnIdx = iList.keyAt(i);
				DecodedInstruction instr = iList.valueAt(i);

				/*
				 * Switch-statement data doesn't get "visited" by scanner. It
				 * may or may not be preceded by a padding NOP (for alignment).
				 */

				// this logic is crap.

				if (instr.getOpcode() == kPackedSwitchSignature
						|| instr.getOpcode() == kSparseSwitchSignature
						|| instr.getOpcode() == kArrayDataSignature
						|| (instr.getOpcode() == OP_NOP && (iList.get(
								insnIdx + 1).getOpcode() == kPackedSwitchSignature
								|| iList.get(insnIdx + 1).getOpcode() == kSparseSwitchSignature || iList
								.get(insnIdx + 1).getOpcode() == kArrayDataSignature))) {
					iList.dvmInsnSetVisited(insnIdx, true);
				}

				if (!iList.dvmInsnIsVisited(insnIdx)) {
					if (deadStart < 0)
						deadStart = insnIdx;
				} else if (deadStart >= 0) {
					Log.d(LOG_TAG, String.format(
							"VFY: dead code 0x%04x-%04x in %s.%s %s",
							deadStart, insnIdx - 1, vdata.method
									.getDeclaringClass().getSignature(),
							vdata.method.getMethodName(), vdata.method
									.getPrototype().toString()));

					deadStart = -1;
				}
			}
			if (deadStart >= 0) {

				Log.d(LOG_TAG, String.format(
						"VFY: dead code 0x%04x-%04x in %s.%s %s", deadStart,
						insnIdx - 1, vdata.method.getDeclaringClass()
								.getSignature(), vdata.method.getMethodName(),
						vdata.method.getPrototype().toString()));

			}
		}

		result = true;

		bail: return result;
	}

	public static final boolean VERIFIER_STATS = true;

	/**
	 * @deprecated
	 * @param meth
	 * @param decInsn
	 * @param workLine
	 * @return
	 * @throws VerifyException
	 */
	private static Clazz aget_1nr_common(Method meth,
			DecodedInstruction decInsn, RegisterLine workLine)
			throws VerifyException {
		throw new UnsupportedOperationException("deprecated");
	}

	/**
	 * Perform verification for a single instruction.
	 * 
	 * <p>
	 * This requires fully decoding the instruction to determine the effect it
	 * has on registers.
	 * 
	 * <p>
	 * Finds zero or more following instructions and sets the "changed" flag if
	 * execution at that point needs to be (re-)evaluated. Register changes are
	 * merged into "regTypes" at the target addresses. Does not set or clear any
	 * other flags in "insnFlags".
	 * 
	 * <p>
	 * This may alter meth.insns if we need to replace an instruction with
	 * throw-verification-error.
	 */
	private static boolean verifyInstruction(Method meth,
			DecodedInstruction instruction, RegisterTable regTable,
			int insnIdx, UninitInstanceMap uninitMap, int pStartGuess) {
		throw new UnsupportedOperationException("not implemented");
	}

	public static ObjectClass findCommonSuperclass(DexModel model,
			ObjectClass c1, ObjectClass c2) throws VerifyException {
		return (ObjectClass) model
				.lookupClassByDescriptor(PredefinedClasses.SIG_JAVA_LANG_OBJECT);
		// TODO implement properly - woul a reverse heirarchy tree work? Or,
		// perhaps the class pool should be loaded for all classes up to Object?
	}

	static void dumpRegTypes(VerifierData vdata, RegisterLine registerLine,
			int addr, String addrName, UninitInstanceMap uninitMap,
			int displayFlags) {
		Log.d(LOG_TAG, "TODO: dump register types.");
	}

	/**
	 * Convenience function for throwing an exception if the category of a type
	 * does not match the specified category.
	 * 
	 * @param type
	 *            the type to check
	 * @param cat
	 *            the category to compare the types' category with.
	 * @throws VerifyException
	 *             if the type's category does not match the specified category.
	 * 
	 */
	public static void checkTypeCategory(RegisterTypeEnum type,
			RegisterCategory cat) throws VerifyException {
		if (type.category() != cat)
			throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
					"VFY: copy1 type=%d cat=%d", type, cat));
	}

	/**
	 * See if "type" matches "cat". All we're really looking for here is that
	 * we're not mixing and matching 32-bit and 64-bit quantities, and we're not
	 * mixing references with numerics. (For example, the arguments to "a < b"
	 * could be integers of different sizes, but they must both be integers.
	 * Dalvik is less specific about int vs. float, so we treat them as
	 * equivalent here.)
	 * 
	 * <p>
	 * For category 2 values, "type" must be the "low" half of the value.
	 * 
	 * <p>
	 * Sets "*pFailure" if something looks wrong.
	 * 
	 * @throws VerifyException
	 * @deprecated
	 */
	public static void checkTypeCategory(RegType type, RegisterCategory cat)
			throws VerifyException {
		throw new UnsupportedOperationException("deprecated");
	}

	/**
	 * Set the type of register N, verifying that the register is valid. If
	 * "newType" is the "Lo" part of a 64-bit value, register N+1 will be set to
	 * "newType+1".
	 * 
	 * The register index was validated during the static pass, so we don't need
	 * to check it here.
	 * 
	 * TODO: clear mon stack bits
	 * 
	 * @deprecated
	 */
	static void setRegisterType(RegisterLine registerLine, int vdst,
			RegType newType) {
		throw new UnsupportedOperationException("deprecated");

		// TODO implement monitor entries
	}

	/**
	 * <p>
	 * For a category 2 register pair, verify that "typeh" is the appropriate
	 * high part for "typel".
	 * 
	 * <p>
	 * Does not verify that "typel" is in fact the low part of a 64-bit register
	 * pair.
	 * 
	 * <p>
	 * In the Java port this always returns true, and this function is retained
	 * for name compatability.s
	 */
	static void checkWidePair(RegType typel, RegType typeh) {
		// no-op, throw no exception

	}

	public static void checkWidePair(RegisterTypeEnum typel,
			RegisterTypeEnum typeh) throws VerifyException {
		if (typel == null)
			throw new IllegalStateException("typel cannot be null.");

		if (typeh == null)
			throw new IllegalStateException("typeh cannot be null.");

		if (typeh.ordinal() != typeh.ordinal() + 1)
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"the low register type must be adjacent to the high type.");
	}

	/**
	 * This function is not necessary .. it is here for name compatatbility with
	 * the C library only.
	 * 
	 * TODO: remove this function. Some calls have been removed already.
	 * 
	 * @param registerLine
	 * @param vsrc
	 * @return
	 */
	public static RegType getRegisterType(final RegisterLine registerLine,
			int vsrc) {
		return registerLine.regTypes.get(vsrc);
	}

	/**
	 * <P>
	 * For the "move-exception" instruction at "insnIdx", which must be at an
	 * exception handler address, determine the first common superclass of all
	 * exceptions that can land here. (For javac output, we're probably looking
	 * at multiple spans of bytecode covered by one "try" that lands at an
	 * exception-specific "catch", but in general the handler could be shared
	 * for multiple exceptions.)
	 * 
	 * <P>
	 * Returns NULL if no matching exception handler can be found, or if the
	 * exception is not a subclass of Throwable.
	 * 
	 * @throws VerifyException
	 */
	static ObjectClass getCaughtExceptionType(final Method meth, int insnIdx)
			throws VerifyException {
		VerifyError localFailure;
		ObjectClass commonSuper = null;
		CatchHandler[] catchHandlers = meth.getCatchHandlers();

		int handlersSize = 0;

		if (meth.getTries().length != 0)
			handlersSize = catchHandlers.length;
		else
			handlersSize = 0;

		for (int i = 0; i < handlersSize; i++) {

			CatchHandler handler = catchHandlers[i];
			int[] addresses = handler.getAddresses();
			int[] typeIndexes = handler.getTypeIndexes();
			String[] typeSignatures = handler.getTypeSignatures();

			for (int addr = 0; addr < addresses.length; addr++) {
				int handlerAddress = addresses[addr];
				int typeIdx = typeIndexes[addr];
				String type = typeSignatures[addr];

				if (handler == null) {
					break;
				}

				if (handlerAddress == insnIdx) {
					ObjectClass clazz;

					if (typeIdx == kDexNoIndex)
						clazz = (ObjectClass) meth
								.getModel()
								.lookupClassByDescriptor(
										PredefinedClasses.SIG_JAVA_LANG_THROWABLE);
					else
						clazz = (ObjectClass) meth.getModel()
								.lookupClassByDescriptor(typeSignatures[addr]); // dvmOptResolveClass(meth.getDeclaringClass(),
																				// typeIdx
																				// );

					if (clazz == null) {
						throw new VerifyException(
								VerifyErrorCause.VERIFY_ERROR_GENERIC,
								String.format(
										"VFY: unable to resolve exception class %u (%s)",
										typeIdx, type));
						/*
						 * we don't want to keep going. If we don't fail this we
						 * run the risk of having a non-Throwable introduced at
						 * runtime. However, that won't pass an instanceof test,
						 * so is essentially harmless.
						 */
					} else {
						if (commonSuper == null)
							commonSuper = clazz;
						else
							commonSuper = findCommonSuperclass(meth.getModel(),
									clazz, commonSuper);
					}
				}
			}

		}

		if (commonSuper == null) {
			/* no catch blocks, or no catches with classes we can find */
			throw new VerifyException(
					VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format(
							"VFY: unable to find exception handler at addr %#x for method %s",
							insnIdx, meth.getMethodName()));

		} else {
			// TODO: verify the class is an instance of Throwable?
		}

		return commonSuper;
	}

	/**
	 * Check constraints on constructor return. Specifically, make sure that the
	 * "this" argument got initialized.
	 * 
	 * The "this" argument to <init> uses code offset kUninitThisArgAddr, which
	 * puts it at the start of the list in slot 0. If we see a register with an
	 * uninitialized slot 0 reference, we know it somehow didn't get
	 * initialized.
	 * 
	 * Returns "true" if all is well.
	 */
	static boolean checkConstructorReturn(final Method meth,
			final RegisterLine registerLine, final int insnRegCount) {
		int i;

		if (!meth.isInitMethod())
			return true;

		RegType uninitThis = regTypeFromUninitIndex(CodeVerifyConstants.kUninitThisArgSlot);

		for (i = 0; i < insnRegCount; i++) {
			if (registerLine.regTypes.get(i) == uninitThis) {
				Log.e(LOG_TAG,
						"VFY: <init> returning without calling superclass init");
				return false;
			}
		}
		return true;
	}

	/*
	 * Handle a monitor-enter instruction.
	 */
	public static void handleMonitorEnter(RegisterLine workLine, int regIdx,
			int insnIdx) throws VerifyException {
		if (!getRegisterType(workLine, regIdx).regTypeIsReference()) {
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"VFY: monitor-enter on non-object");
		}

		if (workLine.monitorStack.size() == 0) {
			throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
					"VFY: monitor-enter stack overflow (%d)",
					CodeVerifyConstants.kMaxMonitorStackDepth));
		}

		/*
		 * Push an entry on the stack, and set a bit in the register flags to
		 * indicate that it's associated with this register.
		 */
		workLine.monitorEntries.set(regIdx);
		BitVector bv = new BitVector();
		bv.set(insnIdx);
		workLine.monitorStack.push(bv);
	}

	/*
	 * Handle a monitor-exit instruction.
	 */
	public static void handleMonitorExit(RegisterLine workLine, int regIdx,
			int insnIdx) throws VerifyException {
		if (!getRegisterType(workLine, regIdx).regTypeIsReference()) {
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"VFY: monitor-exit on non-object");
		}

		if (workLine.monitorStack.size() == 0) {
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"VFY: monitor-exit stack underflow");
		}

		/*
		 * Confirm that the entry at the top of the stack is associated with the
		 * register. Pop the top entry off.
		 * 
		 * FIXME: implement correctly.
		 */
		BitVector bv = workLine.monitorStack.pop();
		// #ifdef BUG_3215458_FIXED
		/*
		 * TODO: This code can safely be enabled if know we are working on a dex
		 * file of format version 036 or later. (That is, we'll need to add a
		 * check for the version number.)
		 */
		/*
		 * if ((workLine->monitorEntries[regIdx] & (1 <<
		 * workLine->monitorStackTop)) == 0) {
		 * LOG_VFY("VFY: monitor-exit bit %d not set: addr=0x%04x (bits[%d]=%#x)"
		 * , workLine->monitorStackTop, insnIdx, regIdx,
		 * workLine->monitorEntries[regIdx]);pFailure = VERIFY_ERROR_GENERIC;
		 * return; }
		 */
		// #endif
		// workLine->monitorStack[workLine->monitorStackTop] = 0;

		/*
		 * Clear the bit from the register flags.
		 */
		workLine.monitorEntries.clear(regIdx); // &= ~(1 <<
												// workLine->monitorStackTop);
	}

	/**
	 * Verify that the arguments in a filled-new-array instruction are valid.
	 * 
	 * <p>
	 * "resClass" is the class refered to by pDecInsn->vB. See
	 * VerifyFilledNewArray class.
	 * 
	 * @deprecated
	 */
	static void verifyFilledNewArrayRegs(Method meth,
			RegisterLine registerLine, final DecodedInstruction pDecInsn,
			ArrayClass resClass, boolean isRange) throws VerifyException {
		int argCount = pDecInsn.getA();
		RegType expectedType;
		int ui;

		ArrayClass arrResClass = (ArrayClass) resClass;
		if (arrResClass.elementClass() instanceof PrimitiveClass) {
			expectedType = PrimitiveType
					.primitiveTypeToRegType(((PrimitiveClass) arrResClass
							.elementClass()).primitiveTypeEnum());
		} else {
			expectedType = regTypeFromClass(arrResClass.elementClass());
		}

		Log.i(LOG_TAG,
				String.format("filled-new-array: %s -> %d",
						resClass.getSignature(), expectedType));

		/*
		 * Verify each register. If "argCount" is bad, verifyRegisterType() will
		 * run off the end of the list and fail. It's legal, if silly, for
		 * argCount to be zero.
		 */
		for (ui = 0; ui < argCount; ui++) {
			int getReg;

			if (isRange)
				getReg = pDecInsn.getC() + ui;
			else
				getReg = pDecInsn.arg[ui];

			try {
				verifyRegisterType(registerLine, getReg, expectedType);
			} catch (VerifyException ex) {
				throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
						"VFY: filled-new-array arg %u(%u) not valid", ui,
						getReg));
			}
		}
	}

	// NOTE: RESULT_REGISTER does nothing. it is a C macro that simply replaces
	// an integer with itself.

	/*
	 * Set the type of the "result" register.
	 */
	private static void setResultRegisterType(RegisterLine registerLine,
			final int insnRegCount, RegType newType) {
		setRegisterType(registerLine, insnRegCount, newType);
	}

	/*
	 * Determine whether or not "instrType" and "targetType" are compatible, for
	 * purposes of getting or setting a value in a field or array. The idea is
	 * that an instruction with a category 1nr type (say, aget-short or
	 * iput-boolean) is accessing a static field, instance field, or array
	 * entry, and we want to make sure sure that the operation is legal.
	 * 
	 * At a minimum, source and destination must have the same width. We further
	 * refine this to assert that "short" and "char" are not compatible, because
	 * the sign-extension is different on the "get" operations.
	 * 
	 * We're not considering the actual contents of the register, so we'll never
	 * get "pseudo-types" like kRegTypeZero or kRegTypePosShort. We could get
	 * kRegTypeUnknown in "targetType" if a field or array class lookup failed.
	 * Category 2 types and references are checked elsewhere.
	 */
	private static boolean checkFieldArrayStore1nr(RegType instrType,
			RegType targetType) {
		return (instrType.register == targetType.register);
	}

	private static void aput_1nr_common(Method meth, RegType tmpType,
			RegisterLine workLine, DecodedInstruction decInsn)
			throws VerifyException {
		throw new UnsupportedOperationException();
	}

	private static void iget_1nr_common(Method meth,
			UninitInstanceMap uninitMap, RegisterLine workLine,
			DecodedInstruction decInsn) throws VerifyException {
		Field instField;
		RegType objType, fieldType;

		objType = getRegisterType(workLine, decInsn.getB());
		instField = getInstField(meth, uninitMap, objType, decInsn.getC());

		/* make sure the field's type is compatible with expectation */
		// TODO uncomment fieldType =
		// primSigCharToRegType(instField.getType().charAt(0));

		/* correct if float */
		// TODO uncomment if (fieldType.register == kRegTypeFloat
		// TODO uncomment && tmpType.register == kRegTypeInteger)
		// TODO uncomment tmpType.register = kRegTypeFloat;

		// TODO uncomment if (fieldType.register == kRegTypeUnknown
		// TODO uncomment || !checkFieldArrayStore1nr(tmpType, fieldType)) {
		// TODO uncomment throw VerifyException(VERIFY_ERROR_GENERIC,
		// String.format(
		// TODO uncomment "VFY: invalid iget-1nr of %s.%s (inst=%d field=%d)",
		// TODO uncomment instField.getDefiningClass().getSignature(),
		// TODO uncomment instField.getName(), tmpType, fieldType));

		// TODO uncomment break;
		// TODO uncomment }

		// TODO uncomment setRegisterType(workLine, decInsn.vA, tmpType);
	}

	public static final int kDexNoIndex = 0xffffffff;
	public static final boolean WARN_NULL_REGISTERS = true;
	public static final boolean WARN_UNKNOWN_REGISTERS = true;

	/**
	 * Look up an instance field, specified by "fieldIdx", that is going to be
	 * accessed in object "objType". This resolves the field and then verifies
	 * that the class containing the field is an instance of the reference in
	 * "objType".
	 * 
	 * It is possible for "objType" to be kRegTypeZero, meaning that we might
	 * have a null reference. This is a runtime problem, so we allow it,
	 * skipping some of the type checks.
	 * 
	 * In general, "objType" must be an initialized reference. However, we allow
	 * it to be uninitialized if this is an "<init>" method and the field is
	 * declared within the "objType" class.
	 * 
	 * Returns an InstField on success, returns NULL and sets "*pFailure" on
	 * failure.
	 * 
	 * @deprecated
	 */
	private static Field getInstField(final Method meth,
			final UninitInstanceMap uninitMap, RegType objType, int fieldIdx)
			throws VerifyException {
		throw new UnsupportedOperationException("deprecated");

	}

	private static void iput_1nr_common(RegisterLine workLine,
			DecodedInstruction decInsn, RegType tmpType) {
		throw new UnsupportedOperationException();
	}

	private static void sget_1nr_common(Method meth, DecodedInstruction decInsn) {
		throw new UnsupportedOperationException();
	}

	private static void sput_1nr_common(Method meth, RegisterLine workLine,
			DecodedInstruction decInsn, RegType tmpType) {
		throw new UnsupportedOperationException();
	}

/**
	 * Verify the arguments to a method.  We're executing in "method", making
	 * a call to the method reference in vB.
	 *
	 * If this is a "direct" invoke, we allow calls to <init>.  For calls to
	 * <init>, the first argument may be an uninitialized reference.  Otherwise,
	 * calls to anything starting with '<' will be rejected, as will any
	 * uninitialized reference arguments.
	 *
	 * For non-static method calls, this will verify that the method call is
	 * appropriate for the "this" argument.
	 *
	 * The method reference is in vBBBB.  The "isRange" parameter determines
	 * whether we use 0-4 "args" values or a range of registers defined by
	 * vAA and vCCCC.
	 *
	 * Widening conversions on integers and references are allowed, but
	 * narrowing conversions are not.
	 *
	 * Returns the resolved method on success, NULL on failure (with *pFailure
	 * set appropriately).
	 */
	private static Method verifyInvocationArgs(final Method meth,
			RegisterLine registerLine, final int insnRegCount,
			final DecodedInstruction pDecInsn, UninitInstanceMap uninitMap,
			MethodType methodType, boolean isRange, boolean isSuper) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Update all registers holding "uninitType" to instead hold the
	 * corresponding initialized reference type. This is called when an
	 * appropriate <init> method is invoked -- all copies of the reference must
	 * be marked as initialized.
	 * 
	 * @throws VerifyException
	 */
	private static void markRefsAsInitialized(RegisterLine registerLine,
			int insnRegCount, UninitInstanceMap uninitMap, RegType uninitType)
			throws VerifyException {
		SparseArray<RegType> insnRegs = registerLine.regTypes;
		ObjectClass clazz;
		RegType initType;
		int i, changed;

		clazz = (ObjectClass) uninitMap.get(uninitType.regTypeToUninitIndex());
		if (clazz == null) {
			throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
					"VFY: unable to find type=%#x (idx=%d)", uninitType,
					uninitType.regTypeToUninitIndex()));
		}
		initType = regTypeFromClass(clazz);

		changed = 0;
		for (i = 0; i < insnRegCount; i++) {
			if (insnRegs.get(i) == uninitType) {
				insnRegs.put(i, initType);
				changed++;
			}
		}
		Log.d(LOG_TAG, String.format("VFY: marked %d registers as initialized",
				changed));

		return;
	}

	/*
	 * Verify types for a simple two-register instruction (e.g. "neg-int").
	 * "dstType" is stored into vA, and "srcType" is verified against vB.
	 */
	static void checkUnop(RegisterLine registerLine,
			DecodedInstruction pDecInsn, RegType dstType, RegType srcType)
			throws VerifyException {
		verifyRegisterType(registerLine, pDecInsn.getB(), srcType);
		setRegisterType(registerLine, pDecInsn.getA(), dstType);
	}

	/**
	 * We're performing an operation like "and-int/2addr" that can be performed
	 * on booleans as well as integers. We get no indication of boolean-ness,
	 * but we can infer it from the types of the arguments.
	 * 
	 * Assumes we've already validated reg1/reg2.
	 * 
	 * TODO: consider generalizing this. The key principle is that the result of
	 * a bitwise operation can only be as wide as the widest of the operands.
	 * You can safely AND/OR/XOR two chars together and know you still have a
	 * char, so it's reasonable for the compiler or "dx" to skip the int-to-char
	 * instruction. (We need to do this for boolean because there is no
	 * int-to-boolean operation.)
	 * 
	 * Returns true if both args are Boolean, Zero, or One.
	 * 
	 * @deprecated
	 */
	static boolean upcastBooleanOp(RegisterLine registerLine, int reg1, int reg2) {
		RegType type1, type2;

		throw new UnsupportedOperationException("deprecated");

	}

	/**
	 * Look up an instance field, specified by "fieldIdx", that is going to be
	 * accessed in object "objType". This resolves the field and then verifies
	 * that the class containing the field is an instance of the reference in
	 * "objType".
	 * 
	 * <p>
	 * It is possible for "objType" to be kRegTypeZero, meaning that we might
	 * have a null reference. This is a runtime problem, so we allow it,
	 * skipping some of the type checks.
	 * 
	 * <p>
	 * In general, "objType" must be an initialized reference. However, we allow
	 * it to be uninitialized if this is an "<init>" method and the field is
	 * declared within the "objType" class.
	 * 
	 * <p>
	 * Returns an InstField on success, returns NULL and sets "*pFailure" on
	 * failure.
	 * 
	 */
	public static ClassData.Field getInstField(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes, RegisterType objType,
			int fieldId) throws VerifyException {

		RegisterTypeEnum objTypeEnum = objType.typeEnum();

		if (objTypeEnum != kRegTypeRef)
			throw new VerifyException(
					VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format(
							"VFY: attempt to access field in non-reference type %d",
							objType));

		boolean mustBeLocal = false;
		ClassData.Method meth = instruction.getMethod();
		String declaringClassSig = instruction.getMethodId()
				.getDeclaringClassSignature();
		ClassDef declaringClass = instruction.getBuffer().getDef(
				declaringClassSig);
		ClassData cData = instruction.getBuffer().readClassData(declaringClass);
		ClassData.Field[] cFields = cData.allFields();
		List<FieldId> fieldIds = instruction.getBuffer().fieldIds();

		ClassData.Field instField = null;
		for (ClassData.Field field : cFields) {
			if (field.getFieldIndex() == fieldId) {
				instField = field;
				break;
			}
		}

		if (instField == null)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: unable to resolve instance field %u",
							fieldId));
		if (objTypeEnum == kRegTypeZero)
			return instField;

		/*
		 * TODO: double check the field resolve in the C code to be sure we've
		 * covered all the edge cases. This function assumes that we're only
		 * looking for an instance field, so the logic is simplified.
		 */
		//

		/*
		 * Access to fields in uninitialized objects is allowed if this is the
		 * <init> method for the object and the field in question is declared by
		 * this class.
		 */
		/*
		 * String objClassSig = RegisterType.regTypeReferenceToClass(objType,
		 * instruction.getUninitInstanceMap(), instruction.getBuffer());
		 * 
		 * if (objTypeEnum == kRegTypeUninit) { if (!meth.dvmIsStaticMethod() ||
		 * !declaringClassSig.equals(objClassSig) ) { throw new VerifyException(
		 * VerifyErrorCause.VERIFY_ERROR_GENERIC,
		 * "VFY: attempt to access field via uninitialized ref"); // throw new
		 * // VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC, //
		 * String.format
		 * ("VFY: invalid field access (field %s.%s, through %s ref)", //
		 * declaringClassSig, fid.getName()));
		 * 
		 * } mustBeLocal = true; }
		 * 
		 * FieldId fId = fieldIds.get(instField.getFieldIndex()); ClassDef
		 * instFieldClass = instruction.getBuffer().getDef(
		 * fId.getDeclaringClassType()); ClassDef objClass =
		 * instruction.getBuffer().getDef( objClassSig); if
		 * (!objClass.dvmInstanceOf(instFieldClass)) throw new VerifyException(
		 * VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
		 * "VFY: invalid field access (field %s.%s, through %s ref)",
		 * instFieldClass.getSignature(), fId.getName(), objClassSig));
		 * 
		 * if (mustBeLocal) { // TODO implement check for init function - see
		 * the original C // function. Log.i(LOG_TAG,
		 * "bypassing local field check on <init> function. TODO: implement this check."
		 * ); }
		 */
		return instField;

	}

}
