package com.android.dx.io.opcode.info;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.io.opcode.regtype.RegisterTypeDestRef;
import com.android.dx.io.opcode.verify.NoVerification;

/**
 * processed by opcode-generator.awk. Original line from OpcodeInfo:
 * 
 * <pre>
 *      <code> 	public static final Info CONST_STRING_JUMBO = new Info(Opcodes.CONST_STRING_JUMBO, "const-string/jumbo", InstructionCodec.FORMAT_31C, IndexType.STRING_REF); </code>
 * </pre>
 * 
 */
public final class CONST_STRING_JUMBO extends OpcodeInfo.Info {
	public CONST_STRING_JUMBO() {
		super(Opcodes.CONST_STRING_JUMBO, "const-string/jumbo",
				InstructionCodec.FORMAT_31C, IndexType.STRING_REF);
		this.verifier = new NoVerification();
		/*
		 * see comments in RegisterTypeMoveException for the explaination as to
		 * why it is used to calculate register types.
		 */
		this.regTypeFactory = new RegisterTypeDestRef() {
			@Override
			public void setRegisterTypes(DecodedInstruction instruction,
					RegisterTypeSparseArray registerTypes) {

				super.setRegisterTypes(instruction, registerTypes);
				
				RegisterType regType = RegisterType.newInstance(kRegTypeRef);
				regType.setRefersTo(instruction.getReferencedData(),
							instruction.getIndexType());
				registerTypes.put(instruction.getA() + 1,
						regType, instruction,
						null);

			}
		};
	}

	@Override
	public RegisterType getRegisterType(RegisterReference ref) {
		throw new UnsupportedOperationException(
				"TODO define, but call return super.getFormatRegisterTypeOrThrow(ref); first");
	}
}
