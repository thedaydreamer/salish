package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;

/**
 * The A reference slot is almost always the destination in moves and other
 * operations that require a destination register. This class sets the A
 * reference slot to type kRegTypeRef.
 * 
 * @author rschilling
 * 
 */
public class RegisterTypeDestChar extends RegisterTypeDest {

	public RegisterTypeDestChar() {
		typeToSet = kRegTypeChar;
	}

}
