package com.android.dx.io.opcode.info;

import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeFilledNewArray;
import com.android.dx.io.opcode.verify.VerifyFilledNewArray;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info FILLED_NEW_ARRAY_RANGE = new Info(Opcodes.FILLED_NEW_ARRAY_RANGE, "filled-new-array/range", InstructionCodec.FORMAT_3RC, IndexType.TYPE_REF); </code>
 * </pre>
 * 
 */
public final class FILLED_NEW_ARRAY_RANGE extends OpcodeInfo.Info {
	public FILLED_NEW_ARRAY_RANGE() {
		super(Opcodes.FILLED_NEW_ARRAY_RANGE, "filled-new-array/range",
				InstructionCodec.FORMAT_3RC, IndexType.TYPE_REF);
		this.verifier = new VerifyFilledNewArray();
		this.regTypeFactory = new RegisterTypeFilledNewArray();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
