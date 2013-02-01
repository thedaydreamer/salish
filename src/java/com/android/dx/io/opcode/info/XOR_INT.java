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
 *      <code> 	public static final Info XOR_INT = new Info(Opcodes.XOR_INT, "xor-int", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class XOR_INT extends OpcodeInfo.Info {
	public XOR_INT() {
		super(Opcodes.XOR_INT, "xor-int", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}