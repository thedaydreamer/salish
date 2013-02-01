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
 *      <code> 	public static final Info MONITOR_EXIT = new Info(Opcodes.MONITOR_EXIT, "monitor-exit", InstructionCodec.FORMAT_11X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MONITOR_EXIT extends OpcodeInfo.Info {
	public MONITOR_EXIT() {
		super(Opcodes.MONITOR_EXIT, "monitor-exit",
				InstructionCodec.FORMAT_11X, IndexType.NONE);

		// TODO implement MONITOR_ENTER and MONITOR_EXIT instruction validation
		// and register processing, which don't affect registers.
		this.verifier = new NoVerification();
		this.regTypeFactory = new NoRegisters();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
