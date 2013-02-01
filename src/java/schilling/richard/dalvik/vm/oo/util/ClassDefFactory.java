package schilling.richard.dalvik.vm.oo.util;

import java.util.EnumSet;

import schilling.richard.dalvik.vm.oo.ArrayClass;
import schilling.richard.dalvik.vm.oo.Clazz;
import schilling.richard.dalvik.vm.oo.DexModel;
import schilling.richard.dalvik.vm.oo.ObjectClass;
import schilling.richard.dalvik.vm.oo.PlatformClass;
import schilling.richard.dalvik.vm.oo.VerifyException;
import schilling.richard.dexlib.lang.clazz.PrimitiveEnum;
import android.util.Log;

import com.android.dx.io.ClassDef;
import com.android.dx.io.dexbuffer.DexBuffer;

/**
 * This class produces various types of ClassDef extended classes based on a
 * class specifier.
 * 
 * @author rschilling
 * 
 */
public final class ClassDefFactory {

	public static final String LOG_TAG = "Finnr.ClassDefFactory";
	private DexBuffer buffer;

	public ClassDefFactory(DexBuffer buff) {
		if (buff == null)
			throw new IllegalArgumentException("buffer cannot be null");

		buffer = buff;
	}

	/**
	 * Creates a class def from a TempClassInfo object. This constructor is used
	 * by DexLoader to create new class definitions when loading a DEX file.
	 * 
	 * @throws IllegalArgumentException
	 *             if dl is null.
	 * @param dl
	 *            a the temp class info to create a class def from.
	 * @return a new ClassDef object that contains all the values in the
	 *         specified TempClassInfo
	 */
	/*
	 * public ObjectClass createClassDef(DexModel model, DexLoader.TempClassInfo
	 * cInfo) {
	 * 
	 * if (cInfo == null) throw new
	 * IllegalArgumentException("dl cannot be null.");
	 * 
	 * ObjectClass result = new ObjectClass(model, cInfo.classDef);
	 * 
	 * return result;
	 * 
	 * }
	 */

	/**
	 * Creates a class definition object given a specifier.
	 * 
	 * @param signature
	 *            the class specifier string used to create the class definition
	 * @return a new class definition
	 * @throws IllegalArgumentException
	 *             if the specifier cannot be resolved to a class def type.
	 */
	public Clazz createClassDef(DexModel model, String signature) {

		try {
			ClassPathUtil.verifySignatureOrThrow(signature, false);
		} catch (VerifyException ex) {
			throw new RuntimeException(ex);
		}

		// most common type that will be found.
		// FIXME uncomment
		/*
		 * if (isPrimitiveClassSpecifier(signature)){
		 * 
		 * // primitives can always be found in a PrimitiveEnum object.
		 * PrimitiveEnum pe = PrimitiveEnum.fromSignature(signature); return
		 * pe.getPrimitiveClass(); }
		 */

		Log.d(LOG_TAG, "Caching class definition " + signature);

		// second most common type that will be found.
		if (isArrayClassSpecifier(signature)) {
			ClassDef cDef = buffer.getDef(signature);
			return new ArrayClass(model, 1, cDef); // FIXME use proper number
		}

		boolean isObject = isObjectClassSpecifier(signature);
		boolean isPlatformClass = isPlatformClassSpecifier(signature);

		if (isObject) {
			// the specifier is either a plain object specifier or a platform
			// object specifier.
			if (isPlatformClass) {
				String className = ClassLoaderUtil
						.convertTypeToClassName(signature);
				try {
					// load the class definition but don't initialize it.
					return createClassDef(model, signature,
							Class.forName(className, false, null));
				} catch (ClassNotFoundException ex) {
					throw new IllegalArgumentException(
							"unable to convert class signature " + signature
									+ " to a platform class.");
				}

			} else {
				ClassDef cDef = buffer.getDef(signature);
				return new ObjectClass(model, cDef, cDef.getTypeIndex());
			}

		}

		throw new IllegalArgumentException("the specifier " + signature
				+ " is not any recognizable class type.");

	}

	public static boolean isArrayClassSpecifier(String signature) {
		try {
			ClassPathUtil.verifySignatureOrThrow(signature, false);
		} catch (VerifyException ex) {
			throw new RuntimeException(ex);
		}
		return signature.charAt(0) == '[' ? true : false;
	}

	public static boolean isObjectClassSpecifier(String signature) {
		try {
			ClassPathUtil.verifySignatureOrThrow(signature, false);
		} catch (VerifyException ex) {
			throw new RuntimeException(ex);
		}

		if (!signature.trim().endsWith(";"))
			return false;
		char ch = signature.charAt(0);

		return signature.charAt(0) == 'L' ? true : false;
	}

	/**
	 * Returns true if the specifier is a valid class name and it can be
	 * resolved to a class that can be loaded by the class loader.
	 * 
	 * @param specifier
	 *            the specifier to check.
	 * @return true if the specifier points to a valid class on the device or
	 *         false otherwise.
	 */
	public static boolean isPlatformClassSpecifier(String signature) {

		try {
			ClassPathUtil.verifySignatureOrThrow(signature, false);
		} catch (VerifyException ex) {
			throw new RuntimeException(ex);
		}

		if (isObjectClassSpecifier(signature)) {
			String className = ClassLoaderUtil
					.convertTypeToClassName(signature);

			try {
				Class.forName(className, false, null); // suppress
														// initialization.
			} catch (ClassNotFoundException ex) {
				return false;
			}

			return true;
		}

		return false;

	}

	public static boolean isPrimitiveClassSpecifier(String signature) {
		try {
			ClassPathUtil.verifySignatureOrThrow(signature, false);
		} catch (VerifyException ex) {
			throw new RuntimeException(ex);
		}

		EnumSet<PrimitiveEnum> eSet = EnumSet.allOf(PrimitiveEnum.class);

		// FIXME uncomment
		/*
		 * for (PrimitiveEnum en : eSet) { if (signature.charAt(0) ==
		 * en.asSpecifier()) return true; }
		 */

		return false;
	}

	/**
	 * Creates a class definition object based on an underlying Class object.
	 * The resulting class only has its parent model set to the specified model.
	 * The class is not, however, included as part of the model itself.
	 * 
	 * @param model
	 *            the parent model that the class belongs to
	 * @param signature
	 *            the signature to associate with the class.
	 * @param c
	 *            the class object that will form the underlying referenced
	 *            class.
	 * @return the class object.
	 */
	public static Clazz createClassDef(DexModel model, String signature,
			Class<? extends Object> c) {
		if (c == null)
			throw new NullPointerException("the specified class cannot be null");

		return new PlatformClass(model, signature, c);
	}

}
