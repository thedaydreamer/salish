package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.NoRegisters;
import com.android.dx.io.opcode.verify.Verify_APUT;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info APUT = new Info(Opcodes.APUT, "aput", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class APUT extends OpcodeInfo.Info {
	public APUT() {
		super(Opcodes.APUT, "aput", InstructionCodec.FORMAT_23X, IndexType.NONE);
		this.verifier = new Verify_APUT();
		this.regTypeFactory = new NoRegisters();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
