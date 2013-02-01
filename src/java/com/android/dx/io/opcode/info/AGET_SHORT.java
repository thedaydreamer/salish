package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestShort;
import com.android.dx.io.opcode.verify.Verify_AGET_SHORT;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info AGET_SHORT = new Info(Opcodes.AGET_SHORT, "aget-short", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class AGET_SHORT extends OpcodeInfo.Info {

	public AGET_SHORT() {
		super(Opcodes.AGET_SHORT, "aget-short", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
		this.verifier = new Verify_AGET_SHORT();
		this.regTypeFactory = new RegisterTypeDestShort();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
