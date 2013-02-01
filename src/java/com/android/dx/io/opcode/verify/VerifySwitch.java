package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;

import com.android.dx.io.opcode.RegisterReference;

public class VerifySwitch extends VerifySingleRegisterType {

	public VerifySwitch() {

		this.ref = RegisterReference.A;
		this.typeRequired = kRegTypeInteger;
	}

}
