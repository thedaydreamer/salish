package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.VerifyConstructorReturnVoid;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info RETURN_VOID = new Info(Opcodes.RETURN_VOID, "return-void", InstructionCodec.FORMAT_10X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class RETURN_VOID extends OpcodeInfo.Info {
	public RETURN_VOID() {
		super(Opcodes.RETURN_VOID, "return-void", InstructionCodec.FORMAT_10X,
				IndexType.NONE);
		this.verifier = new VerifyConstructorReturnVoid();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
