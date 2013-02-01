package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.RegisterReference;

public class VerifyIfLtz extends VerifySingleRegisterCategory {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		super.verify(instruction, registerTypes);

		RegisterType regType = registerTypes.get(getRef(instruction),
				instruction);
		if (regType.typeEnum() == kRegTypeFloat)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: expected non-float cat-1 arg to if");

	}

	public VerifyIfLtz() {
		this.categoryRequired = RegisterCategory.kTypeCategory1nr;
		this.ref = RegisterReference.A;
		this.failMessage = "VFY: expected cat-1 arg to if";
	}
}
