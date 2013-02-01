package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterType_AGET_WIDE;
import com.android.dx.io.opcode.verify.Verify_AGET_WIDE;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info AGET_WIDE = new Info(Opcodes.AGET_WIDE, "aget-wide", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class AGET_WIDE extends OpcodeInfo.Info {
	public AGET_WIDE() {
		super(Opcodes.AGET_WIDE, "aget-wide", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
		this.verifier = new Verify_AGET_WIDE();
		this.regTypeFactory = new RegisterType_AGET_WIDE();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
