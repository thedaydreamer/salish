package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.Verify_APUT_OBJECT;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info APUT_OBJECT = new Info(Opcodes.APUT_OBJECT, "aput-object", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class APUT_OBJECT extends OpcodeInfo.Info {
	public APUT_OBJECT() {
		super(Opcodes.APUT_OBJECT, "aput-object", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
		this.verifier = new Verify_APUT_OBJECT();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
