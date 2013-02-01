package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestBoolean;
import com.android.dx.io.opcode.verify.VerifyCmplFloat;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CMPG_FLOAT = new Info(Opcodes.CMPG_FLOAT, "cmpg-float", InstructionCodec.FORMAT_23X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class CMPG_FLOAT extends OpcodeInfo.Info {
	public CMPG_FLOAT() {
		super(Opcodes.CMPG_FLOAT, "cmpg-float", InstructionCodec.FORMAT_23X,
				IndexType.NONE);
		this.verifier = new VerifyCmplFloat();
		this.regTypeFactory = new RegisterTypeDestBoolean();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
