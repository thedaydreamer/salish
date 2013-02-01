package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;

/**
 * Sets the A referene slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public class RegisterTypeDestShort extends RegisterTypeDestRef {

	public RegisterTypeDestShort() {
		typeToSet = kRegTypeShort;
	}

}
