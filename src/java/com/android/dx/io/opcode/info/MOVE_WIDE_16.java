package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeMoveWide;
import com.android.dx.io.opcode.verify.VerifyMoveWide;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info MOVE_WIDE_16 = new Info(Opcodes.MOVE_WIDE_16, "move-wide/16", InstructionCodec.FORMAT_32X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MOVE_WIDE_16 extends OpcodeInfo.Info {
	public MOVE_WIDE_16() {
		super(Opcodes.MOVE_WIDE_16, "move-wide/16",
				InstructionCodec.FORMAT_32X, IndexType.NONE);
		this.verifier = new VerifyMoveWide();
		this.regTypeFactory = new RegisterTypeMoveWide();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
