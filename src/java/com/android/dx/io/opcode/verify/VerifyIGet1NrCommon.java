package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory1nr;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;

import java.util.List;

import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassData;
import com.android.dx.io.FieldId;
import com.android.dx.io.instructions.DecodedInstruction;

public abstract class VerifyIGet1NrCommon implements OpcodeVerifier {

	protected RegisterTypeEnum typeToCheck = null;

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		if (typeToCheck == null)
			throw new IllegalStateException("subclass must set tmpType");

		RegisterTypeEnum tmpType = typeToCheck;

		RegisterType objType = registerTypes.get(instruction.getB(),
				instruction);
		ClassData.Field instField = CodeVerify.getInstField(instruction,
				registerTypes, objType, instruction.getC());
		List<FieldId> fields = instruction.getBuffer().fieldIds;
		FieldId fId = fields.get(instField.getFieldIndex());
		String fieldSig = fId.getType();

		RegisterType fieldType = RegisterType.primSigCharToRegType(fieldSig
				.charAt(0));

		/* correct if float */
		if (fieldType.typeEnum() == kRegTypeFloat && tmpType == kRegTypeInteger)
			tmpType = kRegTypeFloat;

		if (fieldType.typeEnum() == kRegTypeUnknown
				|| tmpType != fieldType.typeEnum()) // !checkFieldArrayStore1nr(tmpType,
		// fieldType))
		{

			throw new VerifyException(
					VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format(
							"VFY: invalid iget-1nr of %s.%s (inst=%d field=%d)",
							fId.getDeclaringClassType(), fId.getName(),
							tmpType.toString(), fieldType.toString()));
		}

	}

	/*
	 * there is no reason to have this function. private boolean
	 * checkFieldArrayStore1nr(RegisterType instrType, RegisterType targetType){
	 * return instrType == targetType; }
	 */

	protected void checkArrayIndexType(RegisterType regType)
			throws VerifyException {

		if (regType == null)
			throw new IllegalArgumentException("regType cannot be null.");

		if (regType.typeEnum().category() != kTypeCategory1nr)
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("Invalid reg type for array index (%d)",
							regType.toString()));

	}

}
