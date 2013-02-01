package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;

public class Verify_APUT_BYTE extends VerifyAPut1NrCommon {
	public Verify_APUT_BYTE() {
		this.typeToCheck = kRegTypeByte;
	}

}
