package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;

public class Verify_APUT extends VerifyAPut1NrCommon {
	public Verify_APUT() {
		this.typeToCheck = kRegTypeInteger;
	}

}
