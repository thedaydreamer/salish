package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeFilledNewArray extends RegisterTypeSetter {

	public RegisterTypeFilledNewArray() {
		clearInvokeDirect = false;
	}

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		RegisterType type = RegisterType.newInstance(kRegTypeRef);
		type.setRefersTo(instruction.getReferencedData(),
				instruction.getIndexType());
		registerTypes.setResultRegisterTypeLow(type, instruction);
	}

}
