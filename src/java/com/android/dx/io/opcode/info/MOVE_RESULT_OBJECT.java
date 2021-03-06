package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeMoveResult;
import com.android.dx.io.opcode.verify.VerifyMoveResultObject;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info MOVE_RESULT_OBJECT = new Info(Opcodes.MOVE_RESULT_OBJECT, "move-result-object", InstructionCodec.FORMAT_11X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MOVE_RESULT_OBJECT extends OpcodeInfo.Info {

	public MOVE_RESULT_OBJECT() {
		super(Opcodes.MOVE_RESULT_OBJECT, "move-result-object",
				InstructionCodec.FORMAT_11X, IndexType.NONE);
		this.verifier = new VerifyMoveResultObject();
		this.regTypeFactory = new RegisterTypeMoveResult();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}

}
