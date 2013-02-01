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
 *      <code> 	public static final Info GOTO_16 = new Info(Opcodes.GOTO_16, "goto/16", InstructionCodec.FORMAT_20T, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class GOTO_16 extends OpcodeInfo.Info {
	public GOTO_16() {
		super(Opcodes.GOTO_16, "goto/16", InstructionCodec.FORMAT_20T,
				IndexType.NONE);
		this.verifier = new NoVerification();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
