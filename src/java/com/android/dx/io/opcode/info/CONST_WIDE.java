package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeConstWide;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CONST_WIDE = new Info(Opcodes.CONST_WIDE, "const-wide", InstructionCodec.FORMAT_51L, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class CONST_WIDE extends OpcodeInfo.Info {
	public CONST_WIDE() {
		super(Opcodes.CONST_WIDE, "const-wide", InstructionCodec.FORMAT_51L,
				IndexType.NONE);
		this.verifier = new NoVerification();
		this.regTypeFactory = new RegisterTypeConstWide();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
