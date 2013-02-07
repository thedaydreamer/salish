package schilling.richard.dexlib.lang.clazz;

import java.util.EnumSet;

import schilling.richard.dexlib.lang.DexLoader;
import schilling.richard.dexlib.lang.clazz.util.ClassLoaderUtil;
import android.util.Log;

/**
 * This class produces various types of ClassDef extended classes based on a
 * class specifier.
 * 
 * @author rschilling
 * 
 */
public final class ClassDefFactory {

	public static final String LOG_TAG = "Finnr.ClassDefFactory";
	
	private ClassDefFactory() {
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
	public static ObjectClassDef createClassDef(DexLoader.TempClassInfo cInfo) {

		if (cInfo == null)
			throw new IllegalArgumentException("dl cannot be null.");

		ObjectClassDef result = new ObjectClassDef(cInfo);

		return result;

	}

	/**
	 * Creates a class definition object given a specifier.
	 * 
	 * @param signature
	 *            the class specifier string used to create the class definition
	 * @return a new clas definition
	 * @throws IllegalArgumentException
	 *             if the specifier cannot be resolved to a class def type.
	 */
	public static ClassDef createClassDef(String signature) {

		

		validateSignatureOrThrow(signature);

		// most common type that will be found.
		if (isPrimitiveClassSpecifier(signature)){
			// primitives can always be found in a PrimitiveEnum object.
			PrimitiveEnum pe = PrimitiveEnum.fromSignature(signature); 
			return pe.getPrimitiveClass();
		}

		Log.d(LOG_TAG, "Caching class definition " + signature);

		// second most common type that will be found.
		if (isArrayClassSpecifier(signature))
			return new ArrayClassDef(signature);

		boolean isObject = isObjectClassSpecifier(signature);
		if (isObjectClassSpecifier(signature)) {
			// the specifier is either a plain object specifier or a platform
			// object specifier.
			if (isPlatformClassSpecifier(signature)) {
				String className = ClassLoaderUtil
						.convertTypeToClassName(signature);
				try {
					// load the class definition but don't initialize it.
					return new PlatformClassDef(Class.forName(className, false, null)); 
				} catch (ClassNotFoundException ex) {
					throw new IllegalArgumentException(
							"unable to convert class signature " + signature + " to a platform class.");
				}

			} else
				return new ObjectClassDef(signature);

		}

		throw new IllegalArgumentException(
				"the specifier " + signature + " is not any recognizable class type.");

	}

	public static boolean isArrayClassSpecifier(String signature) {
		validateSignatureOrThrow(signature);
		return signature.charAt(0) == '[' ? true : false;
	}

	public static boolean isObjectClassSpecifier(String signature) {
		validateSignatureOrThrow(signature);

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
	 * @return true if the specifier points to a valid on device class or false
	 *         otherwise.
	 */
	public static boolean isPlatformClassSpecifier(String signature) {

		validateSignatureOrThrow(signature);

		if (isObjectClassSpecifier(signature)) {
			String className = ClassLoaderUtil
					.convertTypeToClassName(signature);

			try {
				Class.forName(className, false, null); // suppress initialization.
			} catch (ClassNotFoundException ex) {
				return false;
			}

			return true;
		}

		return false;

	}

	public static boolean isPrimitiveClassSpecifier(String signature) {
		validateSignatureOrThrow(signature);

		EnumSet<PrimitiveEnum> eSet = EnumSet.allOf(PrimitiveEnum.class);
		for (PrimitiveEnum en : eSet) {
			if (signature.charAt(0) == en.asSpecifier())
				return true;
		}

		return false;
	}

	public static ClassDef createClassDef(Class<? extends Object> c) {
		if (c == null)
			throw new NullPointerException("the specified class cannot be null");

		return new PlatformClassDef(c);
	}

	public static void validateSignatureOrThrow(String signature) {
		if (signature == null)
			throw new IllegalArgumentException("specifier must not be null");

		if (signature.trim().length() == 0)
			throw new IllegalArgumentException(
					"specifier cannot be an empty string");

	}
}
