package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Verify a type resolve with a register type check.
 * 
 * @author rschilling
 * 
 */
public class VerifyCheckCast extends VerifyResolveClass {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		super.verify(instruction, registerTypes);

		RegisterType origType = registerTypes.get(instruction.getA(),
				instruction);
		if (origType.typeEnum() != kRegTypeRef)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: check-cast on non-reference in v%u",
							instruction.getA()));

	}
}
