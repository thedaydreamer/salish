package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;

public class Verify_APUT_SHORT extends VerifyAPut1NrCommon {
	public Verify_APUT_SHORT() {
		this.typeToCheck = kRegTypeShort;
	}

}
