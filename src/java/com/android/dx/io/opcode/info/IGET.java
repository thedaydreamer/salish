package com.android.dx.io.opcode.info;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import schilling.richard.dalvik.vm.analysis.RegisterType;

import com.android.dx.io.IndexType;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestInteger;
import com.android.dx.io.opcode.verify.VerifyIGet1NrCommon;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info IGET = new Info(Opcodes.IGET, "iget", InstructionCodec.FORMAT_22C, IndexType.FIELD_REF); </code>
 * </pre>
 * 
 */
public final class IGET extends OpcodeInfo.Info {
	public IGET() {
		super(Opcodes.IGET, "iget", InstructionCodec.FORMAT_22C,
				IndexType.FIELD_REF);
		this.verifier = new VerifyIGet1NrCommon() {
			{
				typeToCheck = kRegTypeInteger;
			}

		};

		this.regTypeFactory = new RegisterTypeDestInteger();

	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
