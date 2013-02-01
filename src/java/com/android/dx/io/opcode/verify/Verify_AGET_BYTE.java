package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;

public class Verify_AGET_BYTE extends VerifyAGet1NrCommon {
	public Verify_AGET_BYTE() {
		this.typeToCheck = kRegTypeByte;
	}

}
