package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestRef;
import com.android.dx.io.opcode.verify.VerifyResolveClass;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CONST_CLASS = new Info(Opcodes.CONST_CLASS, "const-class", InstructionCodec.FORMAT_21C, IndexType.TYPE_REF); </code>
 * </pre>
 * 
 */
public final class CONST_CLASS extends OpcodeInfo.Info {

	public CONST_CLASS() {

		super(Opcodes.CONST_CLASS, "const-class", InstructionCodec.FORMAT_21C,
				IndexType.TYPE_REF);

		this.verifier = new VerifyResolveClass();
		this.regTypeFactory = new RegisterTypeDestRef();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {

		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");

	}
}
