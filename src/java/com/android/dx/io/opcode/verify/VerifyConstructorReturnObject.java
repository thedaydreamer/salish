package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import static schilling.richard.dalvik.vm.analysis.VerifyErrorCause.VERIFY_ERROR_GENERIC;
import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
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
public class VerifyConstructorReturnObject extends VerifyConstructorReturnVoid {

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
					RegisterCategory.kTypeCategoryRef);
		} catch (VerifyException ex) {
			throw new VerifyException(VERIFY_ERROR_GENERIC,
					"VFY: return-wide not expected", ex);
		}

		if (returnType.typeEnum() == kRegTypeZero)
			throw new IllegalStateException(
					"return type cannot be kRegTypeZero");

		if (RegisterType.WARN_UNINIT)
			return;
		/*
		 * Verify that the reference in vAA is an instance of the type in
		 * "returnType". The Zero type is allowed here. If the method is
		 * declared to return an interface, then any initialized reference is
		 * acceptable.
		 * 
		 * Note getClassFromRegister fails if the register holds an
		 * uninitialized reference, so we do not allow them to be returned.
		 */
		RegisterType resType = registerTypes.get(instruction.getA(),
				instruction);
		String resClassSignature = resType.getRefersTo(true);
		ClassDef resClass = instruction.getBuffer().getDef(resClassSignature);
		if (resClass == null)
			throw new IllegalStateException(
					"Register type for resolved reference does not have an associated class signature.");

		String declClassSignature = returnType.getRefersTo(true);
		ClassDef declClass = instruction.getBuffer().getDef(declClassSignature);
		if (declClass == null)
			throw new IllegalStateException(
					"Register type for declared reference does not have an associated class signature.");

		if (!declClass.dvmIsInterfaceClass()
				&& !resClass.dvmInstanceOf(declClass)) {
			throw new VerifyException(VERIFY_ERROR_GENERIC, String.format(
					"VFY: returning %s (cl=default), declared %s (cl=default)",
					resClass.getSignature(), declClass.getSignature()));
		}
	}

}
