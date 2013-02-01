package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.Opcodes;

/**
 * The A reference slot is almost always the destination in moves and other
 * operations that require a destination register. This class sets the A
 * reference slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public class RegisterTypeDestRef extends RegisterTypeDest {

	public RegisterTypeDestRef() {
		typeToSet = kRegTypeRef;
	}

	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {
		super.setRegisterTypes(instruction, registerTypes);

		if (instruction.getOpcode() == Opcodes.MOVE_EXCEPTION)
			registerTypes.get(instruction.getA()).setRefersTo(
					"Ljava/lang/Throwable;", IndexType.TYPE_REF);
		else
			registerTypes.get(instruction.getA())
					.setRefersTo(instruction.getReferencedData(),
							instruction.getIndexType());
	}

}
