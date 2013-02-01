package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.MethodId;
import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeInvokeRange extends RegisterTypeSetter {

	public RegisterTypeInvokeRange() {
		clearInvokeDirect = false;
	}

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		MethodId calledMethodId = instruction.getInvokedMethodId();

		RegisterType returnType;
		try {

			returnType = calledMethodId.getReturnType();

		} catch (VerifyException e) {
			throw new RuntimeException(e);
		}

		registerTypes.setResultRegisterTypeLow(returnType, instruction);
		
		int regCount = instruction.getRegisterCount();
		int vCur = instruction.getA();
		for (int i = 0; i < regCount; i++){
			registerTypes.put(vCur+i, RegisterType.newInstance(kRegTypeUnknown));
			
		}

	}

}
