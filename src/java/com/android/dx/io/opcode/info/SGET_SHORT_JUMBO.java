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
 *      <code> 	public static final Info SGET_SHORT_JUMBO = new Info(Opcodes.SGET_SHORT_JUMBO, "sget-short/jumbo", InstructionCodec.FORMAT_41C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class SGET_SHORT_JUMBO extends OpcodeInfo.Info {
	public SGET_SHORT_JUMBO() {
		super(Opcodes.SGET_SHORT_JUMBO, "sget-short/jumbo",
				InstructionCodec.FORMAT_41C, IndexType.FIELD_REF);
		this.verifier = new NoVerification();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
