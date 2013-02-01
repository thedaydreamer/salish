package com.android.dx.io.opcode.info;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestChar;
import com.android.dx.io.opcode.verify.VerifyIGet1NrCommon;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info IGET_CHAR_JUMBO = new Info(Opcodes.IGET_CHAR_JUMBO, "iget-char/jumbo", InstructionCodec.FORMAT_52C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class IGET_CHAR_JUMBO extends OpcodeInfo.Info {
	public IGET_CHAR_JUMBO() {
		super(Opcodes.IGET_CHAR_JUMBO, "iget-char/jumbo",
				InstructionCodec.FORMAT_52C, IndexType.FIELD_REF);
		this.verifier = new VerifyIGet1NrCommon() {
			{
				typeToCheck = kRegTypeChar;
			}

		};

		this.regTypeFactory = new RegisterTypeDestChar();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
