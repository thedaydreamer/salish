package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategoryRef;

public class VerifyMoveObject extends VerifyMove {

	public VerifyMoveObject() {
		categoryToCheck = kTypeCategoryRef;
	}

}
