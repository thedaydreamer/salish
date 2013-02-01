package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyIfEq implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		RegisterType type1 = registerTypes.get(instruction.getA(), instruction);
		RegisterType type2 = registerTypes.get(instruction.getB(), instruction);

		if (type1.typeEnum() == kRegTypeRef && type2.typeEnum() == kRegTypeRef)
			return;

		/* both category-1nr? */
		if (type1.typeEnum().category() != RegisterCategory.kTypeCategory1nr
				|| type2.typeEnum().category() != RegisterCategory.kTypeCategory1nr)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: args to if-eq/if-ne must both be refs or cat1");

	}

}
