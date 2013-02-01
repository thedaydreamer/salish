package schilling.richard.dalvik.vm.analysis;

// TODO: is this enum replacable by RegisterTypCategory?

/**
 * Register type categories, for type checking.
 * 
 * The spec says category 1 includes boolean, byte, char, short, int, float,
 * reference, and returnAddress. Category 2 includes long and double.
 * 
 * We treat object references separately, so we have "category1nr". We don't
 * support jsr/ret, so there is no "returnAddress" type.
 */
public enum RegisterCategory {
	kTypeCategoryUnknown, kTypeCategory1nr, // boolean, byte, char, short, int,
											// float
	kTypeCategory2, // long, double
	kTypeCategoryRef; // object reference

}
