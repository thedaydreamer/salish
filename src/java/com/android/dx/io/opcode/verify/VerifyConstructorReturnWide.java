package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
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
public class VerifyConstructorReturnWide extends VerifyConstructorReturnVoid {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (instruction.getMethodId().isInitMethod())
			return;

		checkConstructorReturn(instruction, registerTypes);

		RegisterType returnType = instruction.getProtoId()
				.getMethodReturnType();
		try {
			CodeVerify.checkTypeCategory(returnType.typeEnum(),
					RegisterCategory.kTypeCategory2);
		} catch (VerifyException ex) {
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"VFY: return-wide not expected", ex);
		}

		try {
			RegisterType.verifyRegisterType(registerTypes, instruction.getA(),
					returnType, instruction);
		} catch (VerifyException ex) {
			throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
					"VFY: return-1nr on invalid register v%d",
					instruction.getA()));
		}

	}

}
