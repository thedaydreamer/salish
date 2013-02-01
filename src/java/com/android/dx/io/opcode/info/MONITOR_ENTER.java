package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info MONITOR_ENTER = new Info(Opcodes.MONITOR_ENTER, "monitor-enter", InstructionCodec.FORMAT_11X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MONITOR_ENTER extends OpcodeInfo.Info {
	public MONITOR_ENTER() {
		super(Opcodes.MONITOR_ENTER, "monitor-enter",
				InstructionCodec.FORMAT_11X, IndexType.NONE);

		this.verifier = new NoVerification();
		this.regTypeFactory = new NoRegisters();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
