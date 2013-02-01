package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;

/**
 * Sets the A referene slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public class RegisterTypeDestInteger extends RegisterTypeDest {

	public RegisterTypeDestInteger() {
		typeToSet = kRegTypeInteger;
	}

}
