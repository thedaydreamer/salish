package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestBoolean;
import com.android.dx.io.opcode.verify.VerifyInstanceOf;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info INSTANCE_OF = new Info(Opcodes.INSTANCE_OF, "instance-of", InstructionCodec.FORMAT_22C, IndexType.TYPE_REF); </code>
 * </pre>
 * 
 */
public final class INSTANCE_OF extends OpcodeInfo.Info {
	public INSTANCE_OF() {
		super(Opcodes.INSTANCE_OF, "instance-of", InstructionCodec.FORMAT_22C,
				IndexType.TYPE_REF);
		this.verifier = new VerifyInstanceOf();
		this.regTypeFactory = new RegisterTypeDestBoolean();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
