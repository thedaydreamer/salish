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
 *      <code> 	public static final Info SPUT_WIDE = new Info(Opcodes.SPUT_WIDE, "sput-wide", InstructionCodec.FORMAT_21C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class SPUT_WIDE extends OpcodeInfo.Info {
	public SPUT_WIDE() {
		super(Opcodes.SPUT_WIDE, "sput-wide", InstructionCodec.FORMAT_21C,
				IndexType.FIELD_REF);
		this.verifier = new NoVerification();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
