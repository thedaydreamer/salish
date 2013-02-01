package schilling.richard.dexlib.lang.clazz;

import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Set;

/**
 * An enumeration that corresponds to Java language modifiers.
 * 
 * @author rschilling
 * 
 */
public enum ModifierEnum {

	ABSTRACT(Modifier.ABSTRACT),
	FINAL(Modifier.FINAL),
	INTERFACE(Modifier.INTERFACE),
	NATIVE(Modifier.NATIVE),
	PRIVATE(Modifier.PRIVATE),
	PROTECTED(Modifier.PROTECTED),
	PUBLIC(Modifier.PUBLIC),
	STATIC(Modifier.STATIC),
	STRICT(Modifier.STRICT),
	SYNCHRONIZED(Modifier.SYNCHRONIZED),
	TRANSIENT(Modifier.TRANSIENT),
	VOLATILE(Modifier.VOLATILE),
	BRIDGE(0x40),
	VARARGS(0x80),
	STRICTFP(0x800),
	SYNTHETIC(0x1000),
	ANNOTATION(0x2000),
	ENUM(0x4000),
	CONSTRUCTOR(0x10000),
	DECLARED_SYNCHRONIZED(0x20000);
	
	/**
	 * The integer equivalent to this enumerator.
	 */
	private int intValue;

	private ModifierEnum(int val) {
		intValue = val;
	}

	/**
	 * Returns the integer for this modifier. Maps the integer found in Modifier
	 * to this enum value.
	 * 
	 * @return the integer equivalent to this enum.
	 */
	public int bitmap() {
		return intValue;
	}

	/**
	 * Converts a modifier set to an integer bitmap.
	 * @param set the set to convert
	 * @return an integer bitmap
	 */
	public static int modifierSetToBitmap(Set<ModifierEnum> set) {
		if (set == null)
			throw new IllegalArgumentException("set cannot be null");

		int result = 0;

		EnumSet<ModifierEnum> eSet = EnumSet.allOf(ModifierEnum.class);
		for (ModifierEnum en : eSet) {
			if (set.contains(en))
				result |= en.bitmap();

		}

		return result;

	}

	/**
	 * Converts a bitmap to a modifier enumeration set.
	 * @param bitmap the bitmap to convert
	 * @return the enumset of the modifiers.
	 */
	public static Set<ModifierEnum> botmapToModifierSet(int bitmap) {
		Set<ModifierEnum> result = EnumSet.noneOf(ModifierEnum.class);

		EnumSet<ModifierEnum> eSet = EnumSet.allOf(ModifierEnum.class);
		for (ModifierEnum en : eSet) {
			if ((bitmap & en.bitmap()) > 0) {
				result.add(en);
			}
		}

		return result;

	}
}
