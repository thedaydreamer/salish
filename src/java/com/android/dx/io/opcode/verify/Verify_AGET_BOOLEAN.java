package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;

public class Verify_AGET_BOOLEAN extends VerifyAGet1NrCommon {
	public Verify_AGET_BOOLEAN() {
		this.typeToCheck = kRegTypeBoolean;
	}

}
