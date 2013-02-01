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
 *      <code> 	public static final Info FILL_ARRAY_DATA_PAYLOAD = new Info(Opcodes.FILL_ARRAY_DATA_PAYLOAD, "fill-array-data-payload", InstructionCodec.FORMAT_FILL_ARRAY_DATA_PAYLOAD, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class FILL_ARRAY_DATA_PAYLOAD extends OpcodeInfo.Info {
	public FILL_ARRAY_DATA_PAYLOAD() {
		super(Opcodes.FILL_ARRAY_DATA_PAYLOAD, "fill-array-data-payload",
				InstructionCodec.FORMAT_FILL_ARRAY_DATA_PAYLOAD, IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
