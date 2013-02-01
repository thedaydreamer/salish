package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;

public class Verify_AGET extends VerifyAGet1NrCommon {
	public Verify_AGET() {
		this.typeToCheck = kRegTypeInteger;
	}

}
