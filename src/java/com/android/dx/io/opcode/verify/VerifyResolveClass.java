package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Verify a class resolve, where the index to the class is in vB. Also works in
 * place of dvmOptResolveClass because we're not requesting optimized classes in
 * this version.
 * 
 * @author rschilling
 * 
 */
public class VerifyResolveClass implements OpcodeVerifier {

	/**
	 * Make sure that a class can be resolved.
	 */
	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		try {

			instruction.getReferencedData();

		} catch (IndexOutOfBoundsException ex) {

			// unable to resolve the ClassDef so try to resolve the class name
			// so we can report it.

			String badClassDesc = null;

			IllegalStateException ise = null;
			try {

				badClassDesc = instruction.getBuffer().typeNames()
						.get(instruction.getIndex());

			} catch (IndexOutOfBoundsException iofbe) {

				// unable to resolve the name of the class either for reporting
				// an error.

				ise = new IllegalStateException(
						String.format(
								"unable to resolve string for type ID %d from instruction index in instruction %s",
								instruction.getIndex(), instruction
										.getOpcodeEnum().toString()), iofbe);
			}

			VerifyException thrown = new VerifyException(
					VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
							"VFY: unable to resolve %d (%s) in %s", instruction
									.getIndex(), badClassDesc, instruction
									.getMethodId().getName()));
			if (ise != null)
				thrown.initCause(ise);

			throw thrown;

		}

	}
}
