package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;

/**
 * Sets the A referene slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public class RegisterTypeDestByte extends RegisterTypeDest {

	public RegisterTypeDestByte() {
		typeToSet = kRegTypeBoolean;
	}

}
