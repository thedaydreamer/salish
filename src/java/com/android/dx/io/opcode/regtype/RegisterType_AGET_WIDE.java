package com.android.dx.io.opcode.regtype;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;

import com.android.dx.io.ClassDef;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Sets a register in slot A to the type specified in register slot B.
 * 
 * @author rschilling
 * 
 */
public class RegisterType_AGET_WIDE extends RegisterTypeSetter {

	@Override
	public void setRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {

		String resSignature = registerTypes
				.get(instruction.getB(), instruction).getRefersTo(true);

		RegisterTypeEnum dstType = null;

		/* resClass can be null if the reg type is Zero */
		if (resSignature != null && !RegisterType.WARN_UNINIT) {

			ClassDef resClass = instruction.getBuffer().getDef(resSignature);
			if (resClass == null)
				throw new IllegalStateException(String.format(
						"Unable to resolve class %s", resSignature));

			/* make sure array type matches instruction */
			PrimitiveType pType = PrimitiveType.primitiveTypeFromSignature(
					resSignature, false);

			switch (pType) {
			case PRIM_LONG:
				dstType = kRegTypeLongLo;
				break;
			case PRIM_DOUBLE:
				dstType = kRegTypeDoubleLo;
				break;
			default:
				throw new IllegalStateException("dstType has not been set.");
			}

		} else {

			dstType = kRegTypeConstLo;

		}

		registerTypes.put(instruction.getA(),
				RegisterType.newInstance(dstType), instruction, null);

	}

}
