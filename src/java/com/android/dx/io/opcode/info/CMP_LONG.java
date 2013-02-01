package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestBoolean;
import com.android.dx.io.opcode.verify.VerifyCmpLong;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CMP_LONG = new Info(Opcodes.CMP_LONG, "cmp-long", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class CMP_LONG extends OpcodeInfo.Info {
	public CMP_LONG() {
		super(Opcodes.CMP_LONG, "cmp-long", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
		this.verifier = new VerifyCmpLong();
		this.regTypeFactory = new RegisterTypeDestBoolean();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
