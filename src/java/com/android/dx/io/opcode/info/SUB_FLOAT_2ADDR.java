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
 *      <code> 	public static final Info SUB_FLOAT_2ADDR = new Info(Opcodes.SUB_FLOAT_2ADDR, "sub-float/2addr", InstructionCodec.FORMAT_12X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class SUB_FLOAT_2ADDR extends OpcodeInfo.Info {
	public SUB_FLOAT_2ADDR() {
		super(Opcodes.SUB_FLOAT_2ADDR, "sub-float/2addr",
				InstructionCodec.FORMAT_12X, IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
