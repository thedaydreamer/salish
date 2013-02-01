package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Verify a type resolve with a register type check.
 * 
 * @author rschilling
 * 
 */
public class VerifyNewInstance extends VerifyResolveClass {

	/**
	 * Make sure that a class can be resolved.
	 */
	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		super.verify(instruction, registerTypes);

		String resClassSignature = instruction.getReferencedData();
		ClassDef resClass = instruction.getBuffer().getDef(resClassSignature);
		if (resClass == null)
			return; // platform class

		if (resClass.dvmIsAbstractClass() || resClass.dvmIsInterfaceClass()) {
			throw new VerifyException(
					VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format(
							"VFY: new-instance on interface or abstract class %s",
							resClass.getSignature()));
		}

	}
}
