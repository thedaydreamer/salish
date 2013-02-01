package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
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
public class VerifyConstructorReturn extends VerifyConstructorReturnVoid {

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
					RegisterCategory.kTypeCategory1nr);
		} catch (VerifyException ex) {
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"VFY: return-1nr not expected", ex);
		}

		/*
		 * javac generates synthetic functions that write byte values into
		 * boolean fields. Also, it may use integer values for boolean, byte,
		 * short, and character return types.
		 */
		RegisterType srcType = registerTypes.get(instruction.getA(),
				instruction);
		if ((returnType.typeEnum() == kRegTypeBoolean && srcType.typeEnum() == kRegTypeByte)
				|| ((returnType.typeEnum() == kRegTypeBoolean
						|| returnType.typeEnum() == kRegTypeByte
						|| returnType.typeEnum() == kRegTypeShort || returnType
						.typeEnum() == kRegTypeChar) && srcType.typeEnum() == kRegTypeInteger))
			returnType = srcType;

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
