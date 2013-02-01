package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;

public class Verify_AGET_SHORT extends VerifyAGet1NrCommon {
	public Verify_AGET_SHORT() {
		this.typeToCheck = kRegTypeShort;
	}

}
