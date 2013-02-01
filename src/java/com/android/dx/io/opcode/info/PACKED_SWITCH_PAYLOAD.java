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
 *      <code> 	public static final Info PACKED_SWITCH_PAYLOAD = new Info(Opcodes.PACKED_SWITCH_PAYLOAD, "packed-switch-payload", InstructionCodec.FORMAT_PACKED_SWITCH_PAYLOAD, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class PACKED_SWITCH_PAYLOAD extends OpcodeInfo.Info {
	public PACKED_SWITCH_PAYLOAD() {
		super(Opcodes.PACKED_SWITCH_PAYLOAD, "packed-switch-payload",
				InstructionCodec.FORMAT_PACKED_SWITCH_PAYLOAD, IndexType.NONE);
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
