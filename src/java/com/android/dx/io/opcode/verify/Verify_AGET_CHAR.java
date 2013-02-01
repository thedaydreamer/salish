package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;

public class Verify_AGET_CHAR extends VerifyAGet1NrCommon {
	public Verify_AGET_CHAR() {
		this.typeToCheck = kRegTypeChar;
	}

}
