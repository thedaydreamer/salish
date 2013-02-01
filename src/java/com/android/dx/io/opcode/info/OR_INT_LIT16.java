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
 *      <code> 	public static final Info OR_INT_LIT16 = new Info(Opcodes.OR_INT_LIT16, "or-int/lit16", InstructionCodec.FORMAT_22S, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class OR_INT_LIT16 extends OpcodeInfo.Info {
	public OR_INT_LIT16() {
		super(Opcodes.OR_INT_LIT16, "or-int/lit16",
				InstructionCodec.FORMAT_22S, IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
