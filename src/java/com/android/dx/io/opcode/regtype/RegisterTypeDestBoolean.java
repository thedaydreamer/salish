package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;

/**
 * Sets the A referene slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public class RegisterTypeDestBoolean extends RegisterTypeDest {

	public RegisterTypeDestBoolean() {
		typeToSet = kRegTypeByte;
	}

}
