package com.android.dx.io.opcode.regtype;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * The A reference slot is almost always the destination in moves and other
 * operations that require a destination register. This class sets the A
 * reference slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public abstract class RegisterTypeDest extends RegisterTypeSetter {

	protected RegisterTypeEnum typeToSet = null;

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		if (typeToSet == null)
			throw new IllegalStateException("subclass must set typeToSet");

		/*
		 * This applies many insructions. The call in the C code to
		 * 'setRegisterType(workLine, decInsn.vA,
		 * regTypeFromClass(gDvm.classJavaLangString));' simply has
		 * regTypeFromClass generate a register type that is a high number
		 * (memory location, which is interpreted as a Ref type.
		 */
		registerTypes.put(instruction.getA(),
				RegisterType.newInstance(typeToSet), instruction, null);

	}

}
