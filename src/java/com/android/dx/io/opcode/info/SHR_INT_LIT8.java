package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info SHR_INT_LIT8 = new Info(Opcodes.SHR_INT_LIT8, "shr-int/lit8", InstructionCodec.FORMAT_22B, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class SHR_INT_LIT8 extends OpcodeInfo.Info {
	public SHR_INT_LIT8() {
		super(Opcodes.SHR_INT_LIT8, "shr-int/lit8",
				InstructionCodec.FORMAT_22B, IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
