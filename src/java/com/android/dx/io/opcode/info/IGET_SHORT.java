package com.android.dx.io.opcode.info;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestShort;
import com.android.dx.io.opcode.verify.VerifyIGet1NrCommon;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info IGET_SHORT = new Info(Opcodes.IGET_SHORT, "iget-short", InstructionCodec.FORMAT_22C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class IGET_SHORT extends OpcodeInfo.Info {
	public IGET_SHORT() {
		super(Opcodes.IGET_SHORT, "iget-short", InstructionCodec.FORMAT_22C,
				IndexType.FIELD_REF);

		this.verifier = new VerifyIGet1NrCommon() {
			{
				typeToCheck = kRegTypeShort;
			}

		};

		this.regTypeFactory = new RegisterTypeDestShort();
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
