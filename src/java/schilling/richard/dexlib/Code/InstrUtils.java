package schilling.richard.dexlib.Code;

import static schilling.richard.dalvik.vm.InstructionFormat.kFmt00x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt10t;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt10x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt11n;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt11x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt12x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt20bc;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt20t;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt21c;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt21h;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt21s;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt21t;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt22b;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt22c;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt22cs;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt22s;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt22t;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt22x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt23x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt30t;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt31c;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt31i;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt31t;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt32x;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt35c;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt35mi;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt35ms;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt3rc;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt3rmi;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt3rms;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt40sc;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt41c;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt51l;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt52c;
import static schilling.richard.dalvik.vm.InstructionFormat.kFmt5rc;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanBranch;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanContinue;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanReturn;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanSwitch;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrCanThrow;
import static schilling.richard.dalvik.vm.OpcodeFlag.kInstrInvoke;
import schilling.richard.dalvik.vm.InstructionFormat;
import schilling.richard.dalvik.vm.InstructionIndexType;
import schilling.richard.dalvik.vm.analysis.op.GenDexOpcodes;

public final class InstrUtils {

	// all functions are static.
	private InstrUtils() {
	}

	/*
	 * Table that maps each opcode to the full width of instructions that use
	 * that opcode, in (16-bit) code units. Unimplemented opcodes as well as the
	 * "breakpoint" opcode have a width of zero.
	 */
	// @formatter:off
	public final static byte[] gInstructionWidthTable = {
			// BEGIN(libdex-widths); GENERATED AUTOMATICALLY BY opcode-gen
			1, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2,
			2, 3, 5, 2, 2, 3, 2, 1, 1, 2, 2, 1, 2, 2, 3, 3, 3, 1, 1, 2, 3, 3,
			3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0,
			0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 3, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 3, 3, 3, 1,
			2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 0, 4, 4, 5, 4, 5, 5, 5, 5,
			5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
			4, 4, 4, 4, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 4,
			4, 4, 4, 4, 4, 4,
	// END(libdex-widths)
	};
	// @formatter:on

	/*
	 * Table that maps each opcode to the flags associated with that opcode.
	 */

	// @formatter:off

	public final static byte gOpcodeFlagsTable[] = {
			// BEGIN(libdex-flags); GENERATED AUTOMATICALLY BY opcode-gen
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanReturn,
			kInstrCanReturn, kInstrCanReturn, kInstrCanReturn,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanThrow, kInstrCanBranch, kInstrCanBranch, kInstrCanBranch,
			(byte) (kInstrCanContinue | kInstrCanSwitch),
			(byte) (kInstrCanContinue | kInstrCanSwitch), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, (byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch),
			(byte) (kInstrCanContinue | kInstrCanBranch), 0, 0, 0, 0, 0, 0,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke), 0,
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke), 0, 0,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue, kInstrCanContinue,
			kInstrCanContinue, kInstrCanContinue,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), 0, kInstrCanThrow,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			kInstrCanReturn, (byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), 0,
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke), 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0,
			(byte) (kInstrCanContinue | kInstrCanThrow | kInstrInvoke),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow),
			(byte) (kInstrCanContinue | kInstrCanThrow), kInstrCanThrow,
	// END(libdex-flags)
	};

	// @formatter:on

	/**
	 * Table that maps each opcode to the instruction format associated that
	 * opcode.
	 */
	// @formatter:off
	public final static InstructionFormat[] gInstructionFormatTable = {
			// BEGIN(libdex-formats); GENERATED AUTOMATICALLY BY opcode-gen
			kFmt10x, kFmt12x, kFmt22x, kFmt32x, kFmt12x, kFmt22x, kFmt32x,
			kFmt12x, kFmt22x, kFmt32x, kFmt11x, kFmt11x, kFmt11x, kFmt11x,
			kFmt10x, kFmt11x, kFmt11x, kFmt11x, kFmt11n, kFmt21s, kFmt31i,
			kFmt21h, kFmt21s, kFmt31i, kFmt51l, kFmt21h, kFmt21c, kFmt31c,
			kFmt21c, kFmt11x, kFmt11x, kFmt21c, kFmt22c, kFmt12x, kFmt21c,
			kFmt22c, kFmt35c, kFmt3rc, kFmt31t, kFmt11x, kFmt10t, kFmt20t,
			kFmt30t, kFmt31t, kFmt31t, kFmt23x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt22t, kFmt22t, kFmt22t, kFmt22t, kFmt22t, kFmt22t,
			kFmt21t, kFmt21t, kFmt21t, kFmt21t, kFmt21t, kFmt21t, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt23x, kFmt23x,
			kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt22c, kFmt22c,
			kFmt22c, kFmt22c, kFmt22c, kFmt22c, kFmt22c, kFmt22c, kFmt22c,
			kFmt22c, kFmt22c, kFmt22c, kFmt22c, kFmt22c, kFmt21c, kFmt21c,
			kFmt21c, kFmt21c, kFmt21c, kFmt21c, kFmt21c, kFmt21c, kFmt21c,
			kFmt21c, kFmt21c, kFmt21c, kFmt21c, kFmt21c, kFmt35c, kFmt35c,
			kFmt35c, kFmt35c, kFmt35c, kFmt00x, kFmt3rc, kFmt3rc, kFmt3rc,
			kFmt3rc, kFmt3rc, kFmt00x, kFmt00x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x, kFmt23x,
			kFmt23x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x,
			kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt12x, kFmt22s, kFmt22s,
			kFmt22s, kFmt22s, kFmt22s, kFmt22s, kFmt22s, kFmt22s, kFmt22b,
			kFmt22b, kFmt22b, kFmt22b, kFmt22b, kFmt22b, kFmt22b, kFmt22b,
			kFmt22b, kFmt22b, kFmt22b, kFmt22c, kFmt22c, kFmt21c, kFmt21c,
			kFmt22c, kFmt22c, kFmt22c, kFmt21c, kFmt21c, kFmt00x, kFmt20bc,
			kFmt35mi, kFmt3rmi, kFmt35c, kFmt10x, kFmt22cs, kFmt22cs, kFmt22cs,
			kFmt22cs, kFmt22cs, kFmt22cs, kFmt35ms, kFmt3rms, kFmt35ms,
			kFmt3rms, kFmt22c, kFmt21c, kFmt21c, kFmt00x, kFmt41c, kFmt41c,
			kFmt52c, kFmt41c, kFmt52c, kFmt5rc, kFmt52c, kFmt52c, kFmt52c,
			kFmt52c, kFmt52c, kFmt52c, kFmt52c, kFmt52c, kFmt52c, kFmt52c,
			kFmt52c, kFmt52c, kFmt52c, kFmt52c, kFmt41c, kFmt41c, kFmt41c,
			kFmt41c, kFmt41c, kFmt41c, kFmt41c, kFmt41c, kFmt41c, kFmt41c,
			kFmt41c, kFmt41c, kFmt41c, kFmt41c, kFmt5rc, kFmt5rc, kFmt5rc,
			kFmt5rc, kFmt5rc, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x, kFmt00x,
			kFmt00x, kFmt00x, kFmt5rc, kFmt52c, kFmt52c, kFmt52c, kFmt52c,
			kFmt52c, kFmt52c, kFmt41c, kFmt41c, kFmt41c, kFmt41c, kFmt41c,
			kFmt41c, kFmt40sc,
	// END(libdex-formats)
	};

	// @formatter:on

	/**
	 * <p>
	 * Struct that includes a pointer to each of the opcode information tables.
	 * 
	 * <p>
	 * Note: We use "u1*" here instead of the names of the enumerated types to
	 * guarantee that elements don't use much space. We hold out hope for a
	 * standard way to indicate the size of an enumerated type that works for
	 * both C and C++, but in the mean time, this will suffice.
	 * 
	 * <p>
	 * Created for name compatability with the C library.
	 */
	public static class InstructionInfoTables {
		public InstructionFormat[] formats; /* InstructionFormat elements */
		public InstructionIndexType[] indexTypes; /* InstructionIndexType elements */
		public byte[] flags = gOpcodeFlagsTable;
		public byte[] widths = gInstructionWidthTable;
	};

	public static final InstructionInfoTables gDexOpcodeInfo = new InstructionInfoTables();

	/*
	 * Returns the flags for the specified opcode.
	 */
	public static byte dexGetFlagsFromOpcode(Opcode opcode) {
		if (opcode.value >= GenDexOpcodes.kNumPackedOpcodes)
			throw new IllegalStateException("opcode " + opcode.value
					+ " is greater than mas allowed "
					+ GenDexOpcodes.kNumPackedOpcodes);

		return gDexOpcodeInfo.flags[opcode.value];
	}

}
