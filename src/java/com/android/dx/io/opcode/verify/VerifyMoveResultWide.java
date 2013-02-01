package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory2;
import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * The move-result instructions copy data out of a "pseudo-register" with the
 * results from the last method invocation. In practice we might want to hold
 * the result in an actual CPU register, so the Dalvik spec requires that these
 * only appear immediately after an invoke or filled-new-array.
 * 
 * These calls invalidate the "result" register. (This is now redundant with the
 * reset done below, but it can make the debug info easier to read in some
 * cases.)
 */
public class VerifyMoveResultWide implements OpcodeVerifier {

	/**
	 * Verifies register move operation: the source register must be type 1nr.
	 */
	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		// copyResultRegister1(workLine, insnRegCount, decInsn.vA,
		// kTypeCategory1nr, &failure);

		int vSrc = instruction.getRegisterCount() - 1;
		int vDst = instruction.getA();

		if (vDst >= vSrc)
			throw new IllegalStateException(
					String.format(
							"destination register %d is greater or equal than the max register count of %d for instruction %s(%d)",
							vDst, vSrc, instruction.getOpcodeEnum().toString(),
							instruction.getOpcode()));

		RegisterType typel = registerTypes.get(vSrc, instruction);
		RegisterType typeh = registerTypes.get(vSrc + 1, instruction);

		if (typel == null)
			throw new IllegalStateException(
					"the low source register has not been set.");

		if (typeh == null)
			throw new IllegalStateException(
					"the high source register has not been set.");

		if (typel.typeEnum().category() != kTypeCategory2) {
			// create a cause exception so we generate the same information as
			// the C code.
			VerifyException cause = new VerifyException(VERIFY_ERROR_GENERIC,
					String.format("VFY: copy1 type=%d cat=%d", typel,
							kTypeCategory2.toString()));

			throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
					"VFY: copy1 v%u<-v%u type=%d cat=%d", vDst, vSrc, typel,
					kTypeCategory2.toString()), cause);

		}

		CodeVerify.checkWidePair(typel.typeEnum(), typeh.typeEnum());

		if (VALIDATE_EXTRA) {

			if (typeh.typeEnum().category() != kTypeCategory2) {
				VerifyException cause = new VerifyException(
						VERIFY_ERROR_GENERIC, String.format(
								"VFY: copy1 type=%d cat=%d", typeh,
								kTypeCategory2.toString()));

				throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
						"VFY: copy1 v%u<-v%u type=%d cat=%d", vDst, vSrc,
						typeh, kTypeCategory2.toString()), cause);
			}

		}

	}

}
