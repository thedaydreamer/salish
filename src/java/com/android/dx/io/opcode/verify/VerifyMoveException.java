package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategoryRef;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyMoveException implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (!instruction.canResolve())
			throw new IllegalStateException(
					"Unable to determine caught exception type");

		/*
		 * This statement can only appear as the first instruction in an
		 * exception handler (though not all exception handlers need to have one
		 * of these). We verify that as part of extracting the exception type
		 * from the catch block list.
		 * 
		 * "resClass" will hold the closest common superclass of all exceptions
		 * that can be handled here.
		 */
		instruction.getCaughtExceptionType();

		// if no exception is thrown then verification passes

		if (VALIDATE_EXTRA) {
			// register reference A will be the register that gets set to a ref
			// type.
			RegisterType regType = registerTypes.get(instruction.getA(),
					instruction);
			if (regType != null
					&& regType.typeEnum().category() != kTypeCategoryRef)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						"destination register must be category kTypeCategoryRef");

		}

	}

}
