package com.android.dx.io.opcode.verify;

import java.util.List;

import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassData;
import com.android.dx.io.FieldId;
import com.android.dx.io.instructions.DecodedInstruction;

public class Verify_IGET_WIDE implements OpcodeVerifier {

	@Override
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException {

		List<FieldId> fieldIds = instruction.getBuffer().fieldIds;

		RegisterType objType = registerTypes.get(instruction.getB(),
				instruction);
		ClassData.Field instField = CodeVerify.getInstField(instruction,
				registerTypes, objType, instruction.getC());
		FieldId fId = fieldIds.get(instField.getFieldIndex());

		String instFieldSignature = fieldIds.get(instField.getFieldIndex())
				.getDeclaringClassType();

		/* check the type, which should be prim */
		switch (instFieldSignature.charAt(0)) {
		case 'D':
		case 'J':
			break;
		default:
			throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format("VFY: invalid iget-wide of %s.%s",
							fId.getDeclaringClassType(), fId.getName()));

		}

	}

}
