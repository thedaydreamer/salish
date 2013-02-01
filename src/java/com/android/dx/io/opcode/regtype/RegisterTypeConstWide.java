package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstHi;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstLo;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterTypeConstWide extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		/* could be long or double; resolved upon use */
		registerTypes.put(instruction.getA(),
				RegisterType.newInstance(kRegTypeConstLo), instruction, null);
		registerTypes.put(instruction.getA() + 1,
				RegisterType.newInstance(kRegTypeConstHi), instruction, null);

	}

}
