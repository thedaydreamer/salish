package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;

public class Verify_APUT_CHAR extends VerifyAPut1NrCommon {
	public Verify_APUT_CHAR() {
		this.typeToCheck = kRegTypeChar;
	}

}
