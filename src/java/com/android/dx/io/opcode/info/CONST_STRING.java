package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestRef;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CONST_STRING = new Info(Opcodes.CONST_STRING, "const-string", InstructionCodec.FORMAT_21C, IndexType.STRING_REF); </code>
 * </pre>
 * 
 */
public final class CONST_STRING extends OpcodeInfo.Info {
	public CONST_STRING() {
		super(Opcodes.CONST_STRING, "const-string",
				InstructionCodec.FORMAT_21C, IndexType.STRING_REF);
		this.verifier = new NoVerification();
		/*
		 * see comments in RegisterTypeMoveException for the explaination as to
		 * why it is used to calculate register types.
		 */
		this.regTypeFactory = new RegisterTypeDestRef();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
