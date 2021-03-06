package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeMove;
import com.android.dx.io.opcode.verify.VerifyMove;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info MOVE_16 = new Info(Opcodes.MOVE_16, "move/16", InstructionCodec.FORMAT_32X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MOVE_16 extends OpcodeInfo.Info {
	public MOVE_16() {
		super(Opcodes.MOVE_16, "move/16", InstructionCodec.FORMAT_32X,
				IndexType.NONE);
		this.verifier = new VerifyMove();
		this.regTypeFactory = new RegisterTypeMove();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
