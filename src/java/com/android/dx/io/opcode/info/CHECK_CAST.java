package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestRef;
import com.android.dx.io.opcode.verify.VerifyCheckCast;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CHECK_CAST = new Info(Opcodes.CHECK_CAST, "check-cast", InstructionCodec.FORMAT_21C, IndexType.TYPE_REF); </code>
 * </pre>
 * 
 */
public final class CHECK_CAST extends OpcodeInfo.Info {
	public CHECK_CAST() {
		super(Opcodes.CHECK_CAST, "check-cast", InstructionCodec.FORMAT_21C,
				IndexType.TYPE_REF);
		this.verifier = new VerifyCheckCast();
		this.regTypeFactory = new RegisterTypeDestRef();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
