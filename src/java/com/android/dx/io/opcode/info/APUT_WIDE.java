package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.Verify_APUT_WIDE;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info APUT_WIDE = new Info(Opcodes.APUT_WIDE, "aput-wide", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class APUT_WIDE extends OpcodeInfo.Info {
	public APUT_WIDE() {
		super(Opcodes.APUT_WIDE, "aput-wide", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
		this.verifier = new Verify_APUT_WIDE();
		this.regTypeFactory = new NoRegisters();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
