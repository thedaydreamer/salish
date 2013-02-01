package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeMoveResult;
import com.android.dx.io.opcode.verify.VerifyMoveResult;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info MOVE_RESULT = new Info(Opcodes.MOVE_RESULT, "move-result", InstructionCodec.FORMAT_11X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MOVE_RESULT extends OpcodeInfo.Info {
	public MOVE_RESULT() {
		super(Opcodes.MOVE_RESULT, "move-result", InstructionCodec.FORMAT_11X,
				IndexType.NONE);
		this.verifier = new VerifyMoveResult();
		this.regTypeFactory = new RegisterTypeMoveResult();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
