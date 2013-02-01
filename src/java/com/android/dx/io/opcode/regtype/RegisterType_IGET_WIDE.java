package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;

import java.util.List;

import schilling.richard.dalvik.vm.analysis.CodeVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassData;
import com.android.dx.io.FieldId;
import com.android.dx.io.instructions.DecodedInstruction;

public class RegisterType_IGET_WIDE extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		List<FieldId> fieldIds = instruction.getBuffer().fieldIds();

		RegisterType objType = registerTypes.get(instruction.getB(),
				instruction);

		ClassData.Field instField;
		try {
			instField = CodeVerify.getInstField(instruction, registerTypes,
					objType, instruction.getC());
		} catch (VerifyException ex) {
			throw new RuntimeException(ex);
		}

		String instFieldSignature = fieldIds.get(instField.getFieldIndex())
				.getDeclaringClassType();
		RegisterTypeEnum dstType;

		/* check the type, which should be prim */
		switch (instFieldSignature.charAt(0)) {
		case 'D':
			dstType = kRegTypeDoubleLo;
			break;
		case 'J':
			dstType = kRegTypeLongLo;
			break;
		default:
			throw new UnsupportedOperationException(
					"only double and long are supported");
		}

		registerTypes.put(instruction.getA(),
				RegisterType.newInstance(dstType), instruction, null);
	}
}
