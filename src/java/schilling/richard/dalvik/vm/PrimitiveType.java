package schilling.richard.dalvik.vm;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BOOLEAN;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BYTE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_CHAR;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_DOUBLE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_FLOAT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_INTEGER;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_LONG;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_SHORT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_VOID;

import java.util.EnumSet;

import schilling.richard.dalvik.vm.analysis.RegType;
import schilling.richard.dalvik.vm.analysis.RegisterType;

/**
 * A representation of privitive types. The names of each enum is prefixed with
 * PRIM_ so that it's visually easy to see that the enumeration refers to a
 * primitive.
 * 
 * @author rschilling
 */
public enum PrimitiveType {
	PRIM_BOOLEAN(1, SIG_BOOLEAN),
	PRIM_BYTE(8, SIG_BYTE),
	PRIM_SHORT(16, SIG_SHORT),
	PRIM_CHAR(16, SIG_CHAR),
	PRIM_INT(32, SIG_INTEGER),
	PRIM_LONG(64, SIG_LONG),
	PRIM_FLOAT(64, SIG_FLOAT),
	PRIM_DOUBLE(64, SIG_DOUBLE),
	PRIM_VOID(0, SIG_VOID);

	/**
	 * Number of bits this object takes up in memory.
	 */
	private int bitCount;

	/**
	 * The signature of the object.
	 */
	private String classSignature;

	private PrimitiveType(int bits, String sig) {
		bitCount = bits;
		classSignature = sig;
	}

	public int sizeOf() {
		return bitCount;
	}

	public String signature() {
		return classSignature;
	}

	/**
	 * Convert a VM PrimitiveType enum value to the equivalent RegType value.
	 * Use primtiveTypeToRegisterType instead.
	 * 
	 * @deprecated
	 */
	public static RegType primitiveTypeToRegType(PrimitiveType primType) {
		throw new UnsupportedOperationException(
				"deprecated.  Use primitiveTypeToRegType(PrimitiveType)");
	}

	/**
	 * Convert a VM PrimitiveType enum value to the equivalent RegType value.
	 */
	public static RegisterType primitiveTypeToRegisterType(
			PrimitiveType primType) {
		switch (primType) {
		case PRIM_BOOLEAN:
			return RegisterType.newInstance(kRegTypeBoolean);
		case PRIM_BYTE:
			return RegisterType.newInstance(kRegTypeByte);
		case PRIM_SHORT:
			return RegisterType.newInstance(kRegTypeShort);
		case PRIM_CHAR:
			return RegisterType.newInstance(kRegTypeChar);
		case PRIM_INT:
			return RegisterType.newInstance(kRegTypeInteger);
		case PRIM_LONG:
			return RegisterType.newInstance(kRegTypeLongLo);
		case PRIM_FLOAT:
			return RegisterType.newInstance(kRegTypeFloat);
		case PRIM_DOUBLE:
			return RegisterType.newInstance(kRegTypeDoubleLo);
		case PRIM_VOID:
		default: {
			assert (false);
			return RegisterType.newInstance(kRegTypeUnknown);
		}
		}
	}

	/**
	 * Returns true if the class signature is a primitive type.
	 * 
	 * @param signature
	 *            the signature to check for
	 * @return true if the signature is a primitive type.
	 */
	public static boolean dvmIsPrimitiveClass(String signature,
			boolean includeVoid) {

		if (signature == null || signature.trim().equals(""))
			return false;

		EnumSet<PrimitiveType> allSet = EnumSet.allOf(PrimitiveType.class);

		for (PrimitiveType pType : allSet) {

			if (pType == PRIM_VOID && !includeVoid)
				continue;

			if (pType.signature().equals(signature))
				return true;

		}

		return false;
	}

	/**
	 * Returns the primitive type associated with the signature. Does not check
	 * to see if the signature is valid. As long as the signature contains a
	 * primitive type string the associated primitive type is returned.
	 * 
	 * @param signature
	 *            the signature to check.
	 * @param includeVoid
	 *            whether or not to look for Void.
	 * @return true if the primitive type is present in signature.
	 */
	public static PrimitiveType primitiveTypeFromSignature(String signature,
			boolean includeVoid) {
		if (signature == null)
			throw new IllegalArgumentException("signature cannot be null.");

		if (signature.trim().equals(""))
			throw new IllegalArgumentException("signature cannot be empty.");

		EnumSet<PrimitiveType> allSet = EnumSet.allOf(PrimitiveType.class);

		for (PrimitiveType pType : allSet) {

			if (pType == PRIM_VOID && !includeVoid)
				continue;

			if (signature.contains(pType.signature()))
				return pType;

		}

		return null;

	}
}
