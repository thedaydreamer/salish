package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info FILL_ARRAY_DATA = new Info(Opcodes.FILL_ARRAY_DATA, "fill-array-data", InstructionCodec.FORMAT_31T, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class FILL_ARRAY_DATA extends OpcodeInfo.Info {
	public FILL_ARRAY_DATA() {
		super(Opcodes.FILL_ARRAY_DATA, "fill-array-data",
				InstructionCodec.FORMAT_31T, IndexType.NONE);

		// TODO: implement a verifier for this instruction. The code and
		// documentation are cryptic.
		this.verifier = new NoVerification();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
