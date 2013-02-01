package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.VerifyIfLtz;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info IF_LEZ = new Info(Opcodes.IF_LEZ, "if-lez", InstructionCodec.FORMAT_21T, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class IF_LEZ extends OpcodeInfo.Info {
	public IF_LEZ() {
		super(Opcodes.IF_LEZ, "if-lez", InstructionCodec.FORMAT_21T,
				IndexType.NONE);
		this.verifier = new VerifyIfLtz();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
