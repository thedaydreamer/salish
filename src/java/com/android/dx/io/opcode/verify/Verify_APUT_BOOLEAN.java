package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;

public class Verify_APUT_BOOLEAN extends VerifyAPut1NrCommon {
	public Verify_APUT_BOOLEAN() {
		this.typeToCheck = kRegTypeBoolean;
	}

}
