package schilling.richard.dalvik.vm.analysis;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeMAX;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUninit;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import schilling.richard.dalvik.vm.CodeVerifyConstants;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.oo.Clazz;
import schilling.richard.dalvik.vm.oo.ObjectClass;

/**
 * TODO: This class needs to be fixed. RegType holds information about the type
 * of data held in a register. For most types it's a simple enum. For reference
 * types it holds a pointer to the ClassObject, and for uninitialized references
 * it holds an index into the UninitInstanceMap.
 */
public class RegType {// extends RegisterType{

	// TODO: remove classObject and uninitialized;
	public RegisterType register;
	public Clazz classObject;
	public int unititialized = Integer.MIN_VALUE;

	public RegType() {

	}

	public RegType(RegisterType rt) {
		register = rt;
	}

	/* determine if "type" is actually an object reference (init/uninit/zero) */
	public boolean regTypeIsReference() {
		RegisterTypeEnum regEnum = register.typeEnum();
		int regOrdinal = regEnum.ordinal();

		return (regOrdinal > kRegTypeMAX.ordinal() || regEnum == kRegTypeUninit || regEnum == kRegTypeZero);
	}

	/* determine if "type" is an uninitialized object reference */
	public boolean regTypeIsUninitReference() {
		return ((register.typeEnum().ordinal() & CodeVerifyConstants.kRegTypeUninitMask) == kRegTypeUninit
				.ordinal());
	}

	/* convert the initialized reference "type" to a ClassObject pointer */
	/* (does not expect uninit ref types or "zero") */
	Clazz regTypeInitializedReferenceToClass() {
		return classObject;
	}

	/* extract the index into the uninitialized instance map table */
	public int regTypeToUninitIndex() {
		if (!regTypeIsUninitReference())
			throw new RuntimeException("reg type is not an uninit index.");
		return (register.typeEnum().ordinal() & ~CodeVerifyConstants.kRegTypeUninitMask) >> CodeVerifyConstants.kRegTypeUninitShift;
	}

	/* convert the reference "type" to a ClassObject pointer */
	public Clazz regTypeReferenceToClass(UninitInstanceMap uninitMap) {
		// assert(regTypeIsReference(type) && type != kRegTypeZero);
		if (!regTypeIsReference() || register.typeEnum() == kRegTypeZero)
			throw new RuntimeException("cannot convert");

		if (regTypeIsUninitReference()) {
			// assert(uninitMap != NULL);
			return (ObjectClass) uninitMap.get(regTypeToUninitIndex());
		} else {
			return classObject;
		}
	}

	/**
	 * Convert a const derived RegType to the equivalent non-const RegType
	 * value. Does nothing if the argument type isn't const derived.
	 */
	public static RegType constTypeToRegType(RegisterTypeEnum constType) {

		switch (constType) {
		case kRegTypeConstPosByte:
			return new RegType(RegisterType.newInstance(kRegTypePosByte));
		case kRegTypeConstByte:
			return new RegType(RegisterType.newInstance(kRegTypeByte));
		case kRegTypeConstPosShort:
			return new RegType(RegisterType.newInstance(kRegTypePosShort));
		case kRegTypeConstShort:
			return new RegType(RegisterType.newInstance(kRegTypeShort));
		case kRegTypeConstChar:
			return new RegType(RegisterType.newInstance(kRegTypeChar));
		case kRegTypeConstInteger:
			return new RegType(RegisterType.newInstance(kRegTypeInteger));
		default:
			return new RegType(RegisterType.newInstance(constType));

		}
	}

	/**
	 * Given a 32-bit constant, return the most-restricted RegType enum entry
	 * that can hold the value. The types used here indicate the value came from
	 * a const instruction, and may not correctly represent the real type of the
	 * value. Upon use, a constant derived type is updated with the type from
	 * the use, which will be unambiguous.
	 */
	public static RegType determineCat1Const(long value) {
		throw new UnsupportedOperationException(
				"use RegisterType.determineCat1Const instead");

	}

}
