package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestRef;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info SGET_OBJECT = new Info(Opcodes.SGET_OBJECT, "sget-object", InstructionCodec.FORMAT_21C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class SGET_OBJECT extends OpcodeInfo.Info {
	public SGET_OBJECT() {
		super(Opcodes.SGET_OBJECT, "sget-object", InstructionCodec.FORMAT_21C,
				IndexType.FIELD_REF);
		this.verifier = new NoVerification();
		this.regTypeFactory = new RegisterTypeDestRef();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
