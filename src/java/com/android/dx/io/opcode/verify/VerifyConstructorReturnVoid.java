package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Check constraints on constructor return. Specifically, make sure that the
 * "this" argument got initialized.
 * 
 * <p>
 * The "this" argument to <init> uses code offset kUninitThisArgAddr, which puts
 * it at the start of the list in slot 0. If we see a register with an
 * uninitialized slot 0 reference, we know it somehow didn't get initialized.
 * 
 */
public class VerifyConstructorReturnVoid implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (instruction.getMethodId().isInitMethod())
			return;

		checkConstructorReturn(instruction, registerTypes);

		if (instruction.getProtoId().getMethodReturnType().typeEnum() != kRegTypeUnknown)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: return-void expected"); // original text: VFY:
													// return-void not expected
	}

	public static final boolean WARN_INIT_RETURN = true;

	/**
	 * <p>
	 * For the time being, until uninitialized register types are taken into
	 * account during verification, this function always returns true.
	 * 
	 * <p>
	 * TODO: implement uninitialized register types in RegisterType sparse
	 * arrays.
	 * 
	 * <p>
	 * Check constraints on constructor return. Specifically, make sure that the
	 * "this" argument got initialized.
	 * 
	 * <p>
	 * The "this" argument to <init> uses code offset kUninitThisArgAddr, which
	 * puts it at the start of the list in slot 0. If we see a register with an
	 * uninitialized slot 0 reference, we know it somehow didn't get
	 * initialized.
	 * 
	 * <p>
	 * Returns "true" if all is well.
	 * 
	 * @param instruction
	 * @param registerTypes
	 * @throws VerifyException
	 */
	protected void checkConstructorReturn(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {
		RegisterType thisType = registerTypes.get(0, instruction);

		// m-t

		/*
		 * if (VALIDATE_EXTRA) {
		 * 
		 * if (thisType != null && thisType.typeEnum() != kRegTypeRef) { if
		 * (WARN_INIT_RETURN) { Log.w(LOG_TAG,
		 * "VFY: <init> returning without calling superclass init"); } else {
		 * throw new VerifyException( VerifyErrorCause.VERIFY_ERROR_GENERIC,
		 * "VFY: <init> returning without calling superclass init"); } }
		 * 
		 * }
		 */

	}

}
