package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyCmpLong implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		RegisterType.verifyRegisterType(registerTypes, instruction.getB(),
				RegisterType.newInstance(kRegTypeLongLo), instruction);
		RegisterType.verifyRegisterType(registerTypes, instruction.getC(),
				RegisterType.newInstance(kRegTypeLongLo), instruction);

	}

}
