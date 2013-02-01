package schilling.richard.dalvik.vm.analysis;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory1nr;
import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategory2;
import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategoryRef;
import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategoryUnknown;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConflict;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstPosByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstPosShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeOne;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUninit;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;

import java.util.EnumSet;

import schilling.richard.dalvik.vm.CodeVerifyConstants;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.io.ClassDef;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.merge.MethodDefinition;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;

/**
 * Enumeration for register type values. The "hi" piece of a 64-bit value MUST
 * immediately follow the "lo" piece in the enumeration, so we can check that
 * hi==lo+1.
 * <p>
 * Assignment of constants:
 * 
 * <pre>
 *   [-MAXINT,-32768)   : integer
 *   [-32768,-128)      : short
 *   [-128,0)           : byte
 *   0                  : zero
 *   1                  : one
 *   [2,128)            : posbyte
 *   [128,32768)        : posshort
 *   [32768,65536)      : char
 *   [65536,MAXINT]     : integer
 * </pre>
 * <p>
 * Allowed "implicit" widening conversions:
 * 
 * <pre>
 *   zero -> boolean, posbyte, byte, posshort, short, char, integer, ref (null)
 *   one -> boolean, posbyte, byte, posshort, short, char, integer
 *   boolean -> posbyte, byte, posshort, short, char, integer
 *   posbyte -> posshort, short, integer, char
 *   byte -> short, integer
 *   posshort -> integer, char
 *   short -> integer
 *   char -> integer
 * </pre>
 * <p>
 * In addition, all of the above can convert to "float".
 * <p>
 * We're more careful with integer values than the spec requires. The motivation
 * is to restrict byte/char/short to the correct range of values. For example,
 * if a method takes a byte argument, we don't want to allow the code to load
 * the constant "1024" and pass it in.
 */

public class RegisterType { // TODO extends RegisterSpec 
	public static final String LOG_TAG = "RegisterTypeLog";

	/**
	 * When true errors are suppressed and replaced by warnings for UNINT
	 * issues.
	 */
	public static final boolean WARN_UNINIT = true;

	private RegisterTypeEnum typeEnum;

	private RegisterType(RegisterTypeEnum rte) {
		typeEnum = rte;
	}

	public void setTypeEnum(RegisterTypeEnum rte) {
		if (rte == null)
			throw new IllegalArgumentException("rte cannot be null");

		typeEnum = rte;
	}

	public RegisterTypeEnum typeEnum() {
		return typeEnum;
	}

	public static RegisterType newInstance(RegisterTypeEnum e) {
		if (e == null)
			throw new IllegalArgumentException("e cannot be null");

		return new RegisterType(e);
	}

	// TODO deprecate RegisterTypeEnum - replace with Type.
	public static enum RegisterTypeEnum {
		kRegTypeUnknown('_', kTypeCategoryUnknown), /*
													 * initial state; use
													 * value=0 so calloc works
													 */

		// TODO consider making kRegTypeUninit have category kTypeCategoryRef
		kRegTypeUninit('U', kTypeCategoryRef), /*
												 * MUST be odd to distinguish
												 * from pointer
												 */
		kRegTypeConflict('X', kTypeCategoryUnknown), /*
													 * merge clash makes this
													 * reg's type unknowable
													 */

		/**
		 * Category-1nr types. The order of these is chiseled into a couple of
		 * tables, so don't add, remove, or reorder if you can avoid it.
		 */
		kRegTypeZero('0', kTypeCategory1nr), /*
											 * 32-bit 0, could be Boolean, Int,
											 * Float, or Ref
											 */
		// TODO: make sure kRegTypeZero is not used as a reference type in the
		// code.
		kRegTypeOne('1', kTypeCategory1nr), /*
											 * 32-bit 1, could be Boolean, Int,
											 * Float
											 */
		kRegTypeBoolean('Z', kTypeCategory1nr), /* must be 0 or 1 */
		kRegTypeConstPosByte('y', kTypeCategory1nr), /*
													 * const derived byte, known
													 * positive
													 */
		kRegTypeConstByte('Y', kTypeCategory1nr), /* const derived byte */
		kRegTypeConstPosShort('h', kTypeCategory1nr), /*
													 * const derived short,
													 * known positive
													 */
		kRegTypeConstShort('H', kTypeCategory1nr), /* const derived short */
		kRegTypeConstChar('c', kTypeCategory1nr), /* const derived char */
		kRegTypeConstInteger('i', kTypeCategory1nr), /* const derived integer */
		kRegTypePosByte('b', kTypeCategory1nr), /*
												 * byte, known positive (can
												 * become char)
												 */
		kRegTypeByte('B', kTypeCategory1nr),
		kRegTypePosShort('s', kTypeCategory1nr), /*
												 * short, known positive (can
												 * become char)
												 */
		kRegTypeShort('S', kTypeCategory1nr),
		kRegTypeChar('C', kTypeCategory1nr),
		kRegTypeInteger('I', kTypeCategory1nr),
		kRegTypeFloat('F', kTypeCategory1nr),

		kRegTypeConstLo('N', kTypeCategory2), /*
											 * const derived wide, lower half
											 */
		kRegTypeConstHi('n', kTypeCategory2), /*
											 * const derived wide, upper half
											 */
		kRegTypeLongLo('J', kTypeCategory2), /*
											 * lower-numbered register;
											 * endian-independent
											 */
		kRegTypeLongHi('j', kTypeCategory2),
		kRegTypeDoubleLo('D', kTypeCategory2),
		kRegTypeDoubleHi('d', kTypeCategory2),

		/**
		 * Enumeration max; this is used with "full" (32-bit) RegType values.
		 * Anything larger than this is a ClassObject or uninit ref. Mask off
		 * all but the low 8 bits; if you're left with kRegTypeUninit, pull the
		 * uninit index out of the high 24. Because kRegTypeUninit has an odd
		 * value, there is no risk of a particular ClassObject pointer bit
		 * pattern being confused for it (assuming our class object allocator
		 * uses word alignment).
		 */
		kRegTypeMAX('.', kTypeCategoryRef),
		kRegTypeRef('.', kTypeCategoryRef),

		/**
		 * Some special register types that can be used to specify a category
		 * when the type is not known. See DecodedInstruction. This allows us to
		 * create a RegisterTypeSparseArray array without having to know the
		 * specific type of an instruction.
		 */
		kRegTypeAnyCategory1nr('[', kTypeCategory1nr),
		kRegTypeAnyCategory2(']', kTypeCategory2), // long, double

		/**
		 * Used for branch targets
		 * 
		 */
		kRegTypeBranch8('t', kTypeCategory1nr),
		kRegTypeBranch16('t', kTypeCategory1nr),
		kRegTypeBranch32('t', kTypeCategory1nr),
		kRegTypeNibble('x', kTypeCategory1nr);

		// TODO remove kRegTypeAnyRef and use kRegTypeRef instead.

		private RegisterTypeEnum(char val, RegisterCategory category) {
			this.val = val;
			this.category = category;
		}

		public RegisterCategory category() {
			return category;
		}

		public int asChar() {
			return val;
		}

		private char val;
		private RegisterCategory category;

		public TypeBearer asTypeBearer() {
			switch (this) {
			case kRegTypeBranch16:
			case kRegTypeBranch32:
			case kRegTypeBranch8:
			case kRegTypeConflict:
			case kRegTypeConstHi:
			case kRegTypeConstLo:
			case kRegTypeNibble:

				throw new IllegalStateException(String.format(
						"Unable to convert type %s", this.toString()));
			case kRegTypeBoolean:
				return Type.BOOLEAN;

			case kRegTypeByte:
				return Type.BYTE;
			case kRegTypeChar:
				return Type.CHAR;
			case kRegTypeConstPosByte:
			case kRegTypeConstByte:
				return Type.BYTE;
			case kRegTypeConstChar:
				return Type.CHAR;
			case kRegTypeConstInteger:
				return Type.INT;

			case kRegTypeConstShort:
			case kRegTypeConstPosShort:
				return Type.SHORT;
				
			case kRegTypeDoubleHi:
			case kRegTypeDoubleLo:
				return Type.DOUBLE;
				
			case kRegTypeFloat:
				return Type.FLOAT;
				
			case kRegTypeInteger:
				return Type.INT;
				
			case kRegTypeLongHi:
			case kRegTypeLongLo:
				return Type.LONG;
				
			// TODO finish
				
			
			
					

			}

			throw new IllegalStateException(String.format(
					"Type %s not processed", this.toString()));
		}
	}

	/**
	 * If this register type is a reference to a class, this value can hold the
	 * signature of the class.
	 * 
	 */
	private String refersTo;

	private IndexType refersToType;

	/**
	 * The address in the instruction set that initializes the reference in
	 * referenceClass. A call to
	 * DecodedInstruction.getUninitInstanceMap(uninitAddress) returns the
	 * ClassDef that is created at that address.
	 */
	private Integer uninitAddress;

	/**
	 * Sets the reference class signature. Passing null is valid and clears the
	 * reference.
	 * 
	 * @param refersToData
	 *            the signature to set.
	 */
	public void setRefersTo(String refersToData, IndexType iType) {
		if (refersToData != null && refersToData.trim().equals(""))
			throw new IllegalArgumentException(
					"signature cannot be empty when non-null.");

		if (this.typeEnum != kRegTypeRef)
			throw new UnsupportedOperationException(
					"this function can only be called on type kRegTypeRef.");

		refersTo = refersToData;
		refersToType = iType;

	}

	public void setUninitAddress(Integer address) {
		if (this.typeEnum != kRegTypeUninit)
			throw new UnsupportedOperationException(
					"this function can only be called on type kRegTypeUninit");

		uninitAddress = address;

	}

	/**
	 * Returns the data signature that this register type refers to. This call
	 * is only valid when the type is kRegTypeRef. Throws an exception if fail
	 * is true and no reference class was set.
	 * 
	 * <p>
	 * This function is only allowed on kRegTypeRef types.
	 * 
	 * <p>
	 * null is returned if no reference type has been set and fail is false.
	 * 
	 * 
	 * @return the signature of the reference class stored in this register
	 *         type.
	 * @throws UnsupportedOperationException
	 *             if this type is not kRegTypeRef.
	 * @throws IllegalStateException
	 *             if no fail is true and no reference class is set.
	 */
	public String getRefersTo(boolean fail) {

		if (this.typeEnum != kRegTypeRef)
			throw new UnsupportedOperationException(
					"this function can only be called on type kRegTypeRef.");

		if (fail && refersTo == null)
			throw new IllegalStateException("reference class for not set");

		return refersTo;

	}

	public IndexType getRefersToType(boolean fail) {
		if (this.typeEnum != kRegTypeRef)
			throw new UnsupportedOperationException(
					"this function can only be called on type kRegTypeRef.");

		if (fail && refersToType == null)
			throw new IllegalStateException(String.format(
					"reference class register type %s not set",
					this.typeEnum.toString()));

		return refersToType;

	}

	public Integer getUninitAddress() {
		return uninitAddress;
	}

	public static RegisterTypeEnum lookup(char c) {
		EnumSet<RegisterTypeEnum> set = EnumSet.allOf(RegisterTypeEnum.class);

		for (RegisterTypeEnum rt : set) {
			if (rt.asChar() == c)
				return rt;
		}

		throw new IllegalArgumentException("not found");
	}

	public static RegisterTypeEnum lookup(int i) {
		EnumSet<RegisterTypeEnum> set = EnumSet.allOf(RegisterTypeEnum.class);

		for (RegisterTypeEnum rt : set) {
			if (rt.ordinal() == i)
				return rt;
		}

		throw new IllegalArgumentException("not found");
	}

	/**
	 * Given a 32-bit constant, return the most-restricted RegType enum entry
	 * that can hold the value. The types used here indicate the value came from
	 * a const instruction, and may not correctly represent the real type of the
	 * value. Upon use, a constant derived type is updated with the type from
	 * the use, which will be unambiguous.
	 */
	public static RegisterType determineCat1Const(long value) {
		if (value < -32768)
			return RegisterType.newInstance(kRegTypeConstInteger);
		else if (value < -128)
			return RegisterType.newInstance(kRegTypeConstShort);
		else if (value < 0)
			return RegisterType.newInstance(kRegTypeConstByte);
		else if (value == 0)
			return RegisterType.newInstance(kRegTypeZero);
		else if (value == 1)
			return RegisterType.newInstance(kRegTypeOne);
		else if (value < 128)
			return RegisterType.newInstance(kRegTypeConstPosByte);
		else if (value < 32768)
			return RegisterType.newInstance(kRegTypeConstPosShort);
		else if (value < 65536)
			return RegisterType.newInstance(kRegTypeConstChar);
		else
			return RegisterType.newInstance(kRegTypeConstInteger);
	}

	/**
	 * Convert a const derived RegisterTypeEnum to the equivalent non-const
	 * RegisterTypeEnum value. Does nothing if the argument type isn't const
	 * derived.
	 */
	public static RegisterTypeEnum constTypeToRegType(RegisterTypeEnum constType) {
		switch (constType) {
		case kRegTypeConstPosByte:
			return kRegTypePosByte;
		case kRegTypeConstByte:
			return kRegTypeByte;
		case kRegTypeConstPosShort:
			return kRegTypePosShort;
		case kRegTypeConstShort:
			return kRegTypeShort;
		case kRegTypeConstChar:
			return kRegTypeChar;
		case kRegTypeConstInteger:
			return kRegTypeInteger;
		default:
			return constType;

		}
	}

	/*
	 * We're creating a new instance of class C at address A. Any registers
	 * holding instances previously created at address A must be initialized by
	 * now. If not, we mark them as "conflict" to prevent them from being used
	 * (otherwise, markRefsAsInitialized would mark the old ones and the new
	 * ones at the same time).
	 */
	public static void markUninitRefsAsInvalid(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes, int insnRegCount,
			RegisterTypeEnum uninitType) {

		for (int i = 0; i < insnRegCount; i++) {
			RegisterType type = registerTypes.get(i, instruction);
			if (type != null && type.typeEnum == uninitType) {
				registerTypes.put(i, newInstance(kRegTypeConflict),
						instruction, null);
			}
		}

	}

	/**
	 * Calls verifyRegisterType with a RegisterType created from checkType.
	 * 
	 * @param registerTypes
	 * @param vsrc
	 * @param checkType
	 * @param instruction
	 * @throws VerifyException
	 */
	public static void verifyRegisterType(
			RegisterTypeSparseArray registerTypes, int vsrc,
			RegisterTypeEnum checkType, DecodedInstruction instruction)
			throws VerifyException {
		verifyRegisterType(registerTypes, vsrc,
				RegisterType.newInstance(checkType), instruction);
	}

	/**
	 * Verify that the contents of the specified register have the specified
	 * type (or can be converted to it through an implicit widening conversion).
	 * Has the side effect of updating registerTypes.
	 * 
	 * <p>
	 * This will modify the type of the source register if it was originally
	 * derived from a constant to prevent mixing of int/float and long/double.
	 * 
	 * <p>
	 * If "vsrc" is a reference, both it and the "vsrc" register must be
	 * initialized ("vsrc" may be Zero). This will verify that the value in the
	 * register is an instance of checkType, or if checkType is an interface,
	 * verify that the register implements checkType.
	 * 
	 * TODO separate the logic that modifies the registerType sparse array into
	 * a different function.
	 * 
	 * @throws VerifyException
	 *             if verification fails.
	 */
	public static void verifyRegisterType(
			RegisterTypeSparseArray registerTypes, int vsrc,
			RegisterType checkType, DecodedInstruction instruction)
			throws VerifyException {
		RegisterType srcType = registerTypes.get(vsrc);
		RegisterTypeEnum srcTypeEnum = srcType.typeEnum;

		RegisterTypeEnum checkTypeEnum = checkType.typeEnum;

		switch (checkTypeEnum) {
		case kRegTypeFloat:
		case kRegTypeBoolean:
		case kRegTypePosByte:
		case kRegTypeByte:
		case kRegTypePosShort:
		case kRegTypeShort:
		case kRegTypeChar:
		case kRegTypeInteger:
			if (!CodeVerifyConstants
					.canConvertTo1nr(srcTypeEnum, checkTypeEnum)) {
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"VFY: register1 v%u type %d, wanted %d", vsrc,
								srcType, checkType));
			}
			/* Update type if result is float */
			if (checkTypeEnum == kRegTypeFloat) {
				srcType.typeEnum = checkTypeEnum;
			} else {
				/* Update const type to actual type after use */
				srcType.typeEnum = RegisterType
						.constTypeToRegType(srcType.typeEnum);
			}
			break;
		case kRegTypeLongLo:
		case kRegTypeDoubleLo:
			RegisterType srcTypeHigh = registerTypes.get(vsrc + 1);
			if (srcTypeHigh.typeEnum.ordinal() != srcType.typeEnum.ordinal() + 1)

				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"VFY: register2 v%d-%d values %d,%d", vsrc,
								vsrc + 1, srcType.typeEnum.ordinal(),
								srcTypeHigh.typeEnum.ordinal()));

			else if (!CodeVerifyConstants.canConvertTo2(srcTypeEnum,
					checkTypeEnum))
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"VFY: register2 v%u type %d, wanted %d", vsrc,
								srcType.typeEnum.ordinal(),
								checkTypeEnum.ordinal()));

			/* Update type if source is from const */
			if (srcType.typeEnum == kRegTypeConstLo)
				registerTypes.put(vsrc, checkType, instruction, null);

			break;
		case kRegTypeConstLo:
		case kRegTypeConstHi:
		case kRegTypeLongHi:
		case kRegTypeDoubleHi:
		case kRegTypeZero:
		case kRegTypeOne:
		case kRegTypeUnknown:
		case kRegTypeConflict:
			/* should never be checking for these explicitly */
			throw new IllegalArgumentException(
					"invalid to check for kRegTypeConflict");

		case kRegTypeUninit:
			if (WARN_UNINIT) {
				Log.w(LOG_TAG,
						"Uninit checks are disabled until Uninit object tracking is enabled.");
				break;
			}
		default:
			/* make sure checkType is initialized reference */
			if (checkTypeEnum.category() != kRegTypeRef.category())
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"VFY: unexpected check type %d",
								checkTypeEnum.ordinal()));

			if (checkTypeEnum == kRegTypeUninit)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						"VFY: uninitialized ref not expected as reg check");

			/* make sure srcType is initialized reference or always-NULL */
			if (srcType.typeEnum.category() != kRegTypeRef.category())
				if (WARN_UNINIT) {
					Log.w(LOG_TAG, String.format(
							"VFY: register1 v%d type %d, wanted ref", vsrc,
							srcType.typeEnum.ordinal()));
				} else {
					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC,
							String.format(
									"VFY: register1 v%d type %d, wanted ref",
									vsrc, srcType.typeEnum.ordinal()));
				}

			if (srcType.typeEnum == kRegTypeUninit) {
				if (WARN_UNINIT) {
					Log.d(LOG_TAG, String.format(
							"VFY: register1 v%u holds uninitialized ref", vsrc));
				} else {
					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC,
							String.format(
									"VFY: register1 v%u holds uninitialized ref",
									vsrc));
				}
			}

			/* if the register isn't Zero, make sure it's an instance of check */

			// TODO implement the check for instance - see the C code for more
			// information.

			if (srcType.typeEnum != kRegTypeZero) {

				if (WARN_UNINIT)
					break;

				String srcClass = srcType.getRefersTo(true);
				String checkClass = checkType.getRefersTo(true);

				ClassDef srcClassDef = instruction.getBuffer().getDef(srcClass);
				ClassDef checkClassDef = instruction.getBuffer().getDef(
						checkClass);

				if (checkClassDef.dvmIsInterfaceClass()) {
					/*
					 * All objects implement all interfaces as far as the
					 * verifier is concerned. The runtime has to sort it out.
					 * See comments above findCommonSuperclass.
					 */
					/*
					 * if (srcClass != checkClass && !dvmImplements(srcClass,
					 * checkClass)) { LOG_VFY("VFY: %s does not implement %s",
					 * srcClass->descriptor, checkClass->descriptor);pFailure =
					 * VERIFY_ERROR_GENERIC; }
					 */
				} else {
					if (!srcClassDef.isInstanceOf(checkClassDef.getSignature())) {
						throw new VerifyException(
								VerifyErrorCause.VERIFY_ERROR_GENERIC,
								String.format("VFY: %s is not instance of %s",
										srcClassDef.getSignature(),
										checkClassDef.getSignature()));

					}
				}
			}

		}
	}

	/**
	 * Convert a single-character signature value (i.e. a primitive type) to the
	 * corresponding RegType. This is intended for access to object fields
	 * holding primitive types.
	 * 
	 * @return kRegTypeUnknown for objects, arrays, and void.
	 */
	public static RegisterType primSigCharToRegType(char sigChar) {
		RegisterType result;

		switch (sigChar) {
		case 'I':
			result = newInstance(kRegTypeInteger);
			break;
		case 'C':
			result = newInstance(kRegTypeChar);
			break;
		case 'S':
			result = newInstance(kRegTypeShort);
			break;
		case 'B':
			result = newInstance(kRegTypeByte);
			break;
		case 'Z':
			result = newInstance(kRegTypeBoolean);
			break;
		case 'F':
			result = newInstance(kRegTypeFloat);
			break;
		case 'D':
			result = newInstance(kRegTypeDoubleLo);
			break;
		case 'J':
			result = newInstance(kRegTypeLongLo);
			break;
		case 'V':
		case 'L':
		case '[':
			result = newInstance(kRegTypeUnknown);
			break;
		default:
			assert (false);
			result = newInstance(kRegTypeUnknown);
			break;
		}

		return result;
	}

	/**
	 * Retrieves a class def from either the ununit map or from a dex buffer
	 * based on the register type. Register type of uninitref is retrieved from
	 * the ununitMap and kRegTypeRef types are resolved from the dex buffer.
	 * 
	 * <p>
	 * This function always returns a valid ClassDef. If it can't then a runtime
	 * exception is thrown.
	 * 
	 * @param type
	 *            the regisger type to interrogate
	 * @param uninitMap
	 *            the uninit map to get the class def from
	 * @param buffer
	 *            the buffer to get the class def from if it's initialized.
	 * @return the class def.
	 * @throws IllegalArgumentException
	 *             if any argument is null.
	 * @throws IllegalStateException
	 *             if a class could not be resolved.
	 */
	public static String regTypeReferenceToClass(RegisterType type,
			SparseArray<String> uninitMap, DexBuffer buffer) {

		if (type == null)
			throw new IllegalArgumentException("type cannot be null.");

		if (uninitMap == null)
			throw new IllegalArgumentException("ununitMap cannot be null");

		if (buffer == null)
			throw new IllegalArgumentException("buffer cannot be null.");

		if (type.typeEnum != kRegTypeRef && type.typeEnum != kRegTypeUninit)
			throw new IllegalArgumentException(
					"type must be either kRegTypeRef or kRegTypeUninit");

		String result = null;
		if (type.typeEnum == kRegTypeRef) {

			result = type.getRefersTo(true);

		} else {
			Integer address = type.getUninitAddress();
			if (address == null)
				throw new IllegalStateException(
						"the specified type does not have an uninitialized class ");

			result = uninitMap.get(address);
			if (result == null)
				throw new IllegalStateException(
						"uninitMap does not have an uninitialized class stored");

			// TODO: where is uninitMap entries cleared?
		}

		return result;
	}

	/**
	 * Dump the register state.
	 * 
	 * @param instruction
	 *            the instruction that modified the register array (may be null)
	 * @param registerTypes
	 *            the register array to dump.
	 */
	public static void dumpRegisterTypes(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) {
		StringBuilder output = new StringBuilder();
		output.append(instruction != null ? String.format("%s(%d)@%d",
				instruction.getOpcodeEnum().toString(),
				instruction.getOpcode(), instruction.getAddress()) : "N/A");

		if (instruction != null && instruction.isInvoke())
			output.append(String.format("/outsSize %d", instruction.getA()));

		output.append(": ");

		if (registerTypes == null) {
			output.append("null");
		} else {

			RegisterType resultType = registerTypes
					.getResultRegisterTypeLow(null);
			if (resultType != null)
				output.append(String.format("Rlo (%s) ", resultType.typeEnum()
						.toString()));

			resultType = registerTypes.getResultRegisterTypeHigh(null);
			if (resultType != null)
				output.append(String.format("Rhi (%s) ", resultType.typeEnum()
						.toString()));

			int sz = registerTypes.size();
			for (int i = 0; i < sz; i++) {

				if (i > 0 && i % 5 == 0)
					output.append("\n");

				int reg = registerTypes.keyAt(i);
				RegisterType type = registerTypes.valueAt(i);
				output.append(String.format(" v%d(%s", reg, type.typeEnum()
						.toString()));
				if (type.typeEnum() == kRegTypeRef) {
					output.append(" ");
					output.append(type.getRefersTo(true).toString());

				}
				output.append(")");

			}
		}

		if (MethodDefinition.LOG_REGISTERS)
			Log.i(LOG_TAG, output.toString());
	}

	/**
	 * Copies values from one register type into this one. Used by
	 * RegisterTypeArray in the put function so that information in an existing
	 * value is not lost when a new value is written into a location in the
	 * array that already has a value.
	 * 
	 * @param value
	 *            the register type to copy from.
	 */
	public void copyValuesFrom(RegisterType value) {

		String thisReferenceClass = this.typeEnum() == RegisterTypeEnum.kRegTypeRef ? this
				.getRefersTo(false) : null;

		String valReferenceClass = value.typeEnum() == RegisterTypeEnum.kRegTypeRef ? value
				.getRefersTo(false) : null;

		boolean setReference = false;
		if (thisReferenceClass == null && valReferenceClass != null)
			setReference = true;
		else if (thisReferenceClass != null && valReferenceClass == null)
			setReference = true;
		else if (thisReferenceClass != null && valReferenceClass != null
				&& !thisReferenceClass.equals(valReferenceClass))
			setReference = true;

		if (setReference)
			this.refersTo = value.refersTo;

		Integer thisUninitAddress = this.getUninitAddress();
		Integer valUninitAddress = value.getUninitAddress();
		boolean setAddress = false;

		if (thisUninitAddress == null && valUninitAddress != null)
			setAddress = true;
		else if (thisUninitAddress != null && valUninitAddress == null)
			setAddress = true;
		else if (thisUninitAddress != null && valUninitAddress != null
				&& thisUninitAddress.intValue() != valUninitAddress.intValue())
			setAddress = true;

		if (setAddress)
			this.uninitAddress = value.uninitAddress;

		if (this.typeEnum != value.typeEnum)
			this.typeEnum = value.typeEnum;

	}

	@Override
	public String toString() {
		return typeEnum.toString();
	}

}
