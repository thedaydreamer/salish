package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeMove;
import com.android.dx.io.opcode.verify.VerifyMoveObject;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info MOVE_OBJECT_FROM16 = new Info(Opcodes.MOVE_OBJECT_FROM16, "move-object/from16", InstructionCodec.FORMAT_22X, IndexType.NONE); </code>
 * </pre>
 * 
 */
public final class MOVE_OBJECT_FROM16 extends OpcodeInfo.Info {
	public MOVE_OBJECT_FROM16() {
		super(Opcodes.MOVE_OBJECT_FROM16, "move-object/from16",
				InstructionCodec.FORMAT_22X, IndexType.NONE);
		this.verifier = new VerifyMoveObject();
		this.regTypeFactory = new RegisterTypeMove();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
