package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterType_IGET_WIDE;
import com.android.dx.io.opcode.verify.Verify_IGET_WIDE;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info IGET_WIDE = new Info(Opcodes.IGET_WIDE, "iget-wide", InstructionCodec.FORMAT_22C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class IGET_WIDE extends OpcodeInfo.Info {
	public IGET_WIDE() {
		super(Opcodes.IGET_WIDE, "iget-wide", InstructionCodec.FORMAT_22C,
				IndexType.FIELD_REF);
		this.verifier = new Verify_IGET_WIDE();
		this.regTypeFactory = new RegisterType_IGET_WIDE();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
