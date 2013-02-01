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
 *      <code> 	public static final Info USHR_INT_2ADDR = new Info(Opcodes.USHR_INT_2ADDR, "ushr-int/2addr", InstructionCodec.FORMAT_12X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class USHR_INT_2ADDR extends OpcodeInfo.Info {
	public USHR_INT_2ADDR() {
		super(Opcodes.USHR_INT_2ADDR, "ushr-int/2addr",
				InstructionCodec.FORMAT_12X, IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
