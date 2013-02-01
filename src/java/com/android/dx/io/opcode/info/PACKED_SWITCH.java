package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.VerifySwitch;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info PACKED_SWITCH = new Info(Opcodes.PACKED_SWITCH, "packed-switch", InstructionCodec.FORMAT_31T, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class PACKED_SWITCH extends OpcodeInfo.Info {
	public PACKED_SWITCH() {
		super(Opcodes.PACKED_SWITCH, "packed-switch",
				InstructionCodec.FORMAT_31T, IndexType.NONE);
		this.verifier = new VerifySwitch();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
