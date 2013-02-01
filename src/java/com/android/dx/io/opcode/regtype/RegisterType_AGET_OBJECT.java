package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Sets a register in slot A to the type specified in register slot B.
 * 
 * @author rschilling
 * 
 */
public class RegisterType_AGET_OBJECT extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		RegisterTypeEnum dstType = null;

		// FIXME the reference class index comes from instruction.getIndex(),
		// not instruction.getB().
		if (RegisterType.WARN_UNINIT) {
			dstType = kRegTypeUnknown;

		} else {
			String resSignature = registerTypes.get(instruction.getB(),
					instruction).getRefersTo(true);

			if (resSignature != null) {
				dstType = kRegTypeRef;
			} else {
				dstType = kRegTypeZero;
			}
		}
		RegisterType regType = RegisterType.newInstance(dstType);
		if (dstType == kRegTypeRef)
			regType.setRefersTo(instruction.getReferencedData(),
					instruction.getIndexType());

		registerTypes.put(instruction.getA(),
				RegisterType.newInstance(dstType), instruction, null);

	}

}
