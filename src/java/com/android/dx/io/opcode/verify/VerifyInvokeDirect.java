package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUninit;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassDef;
import com.android.dx.io.MethodId;
import com.android.dx.io.instructions.DecodedInstruction;

public class VerifyInvokeDirect extends VerifyInvoke {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		// TODO implement
	}

	/**
	 * Verifies that the init function is appropriate. It also checks to ensure
	 * that the register type associated with the called method this pointer is
	 * uninitialized. If the check passes then this function has no side effect.
	 * 
	 * <p>
	 * Some additional checks when calling <init>. We know from the invocation
	 * arg check that the "this" argument is an instance of calledMethod->clazz.
	 * Now we further restrict that to require that calledMethod->clazz is the
	 * same as this->clazz or this->super, allowing the latter only if the
	 * "this" argument is the same as the "this" argument to this method (which
	 * implies that we're in <init> ourselves).
	 * 
	 * 
	 * 
	 * 
	 * @param instruction
	 *            the instruction being tested.
	 * @param registerTypes
	 *            the register types for the function
	 * @param calledMethodId
	 *            the method id of the function being called.
	 * @throws VerifyException
	 *             if the check fails.
	 */
	private void checkInitCase(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes, MethodId calledMethodId)
			throws VerifyException {

		// the this pointer from a non static method invocation.
		RegisterType calledArgThisType = instruction
				.getInvocationThis(registerTypes);

		checkNotTypeZero(calledArgThisType);

		String calledArgThisTypeSignature = calledArgThisType.getRefersTo(true);

		ClassDef calledArgThisClassDef = instruction.getBuffer().getDef(
				calledArgThisTypeSignature);

		if (calledArgThisClassDef == null)
			return; // platform class

		checkCalledMethodClass(instruction.getMethodId(), calledMethodId,
				calledArgThisClassDef);

		/*
		 * arg must be an uninitialized reference - not allowed to be anything
		 * else
		 */
		checkTypeUninit(calledArgThisType.typeEnum());

	}

	/**
	 * Checks to ensure that typeEnum is kRegTypeUninit.
	 * 
	 * @param typeEnum
	 * @throws VerifyException
	 */
	private void checkTypeUninit(RegisterTypeEnum typeEnum)
			throws VerifyException {
		/* arg must be an uninitialized reference */
		if (typeEnum != kRegTypeUninit)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: can only initialize the uninitialized");

	}

	/**
	 * Checks to see if the called method and the class type from the 'this'
	 * argument are appropriate. If the check passes then this function has no
	 * side effects. must be in same class or in superclass.
	 * 
	 * @param insnMethodId
	 *            the method ID of the calling instruction under verification
	 * @param calledMethod
	 *            the method that the instruction is calling.
	 * @param thisClass
	 *            the class definition of the 'this' reference in the called
	 *            method.
	 * @throws VerifyException
	 *             if the check fails.
	 */
	private void checkCalledMethodClass(MethodId insnMethodId,
			MethodId calledMethod, ClassDef calledArgClassDef)
			throws VerifyException {

		String calledClassType = calledMethod.getDeclaringClassSignature();

		// called method's declaring class == invoked class type ?
		if (calledClassType.equals(calledArgClassDef.getSupertypeSignature())) {

			// this reference class must = the instruction's declaring class
			if (!calledArgClassDef.getSignature().equals(
					insnMethodId.getDeclaringClassSignature()))
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						"VFY: invoke-direct <init> on super only allowed for 'this' in <init>");

		} else if (!calledClassType.equals(calledArgClassDef.getSignature())) {
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: invoke-direct <init> must be on current class or super");
		}

	}

	/**
	 * Throws an exception if the specified type is kRegTypeZero.
	 * 
	 * @param type
	 *            the type to check
	 * @throws VerifyException
	 *             if type is kRegTypeZero.
	 */
	private void checkNotTypeZero(RegisterType type) throws VerifyException {
		/* no null refs allowed (?) */
		if (type.typeEnum() == kRegTypeZero)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					"VFY: unable to initialize null ref");

	}

}
