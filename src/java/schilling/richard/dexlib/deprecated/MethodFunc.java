package schilling.richard.dexlib.deprecated;

import java.util.HashMap;

import schilling.richard.dexlib.Code.Analysis.DeodexUtil;

/**
 * All the code in this class will be deprecated.
 * 
 * @author rschilling
 * 
 */
public final class MethodFunc {
	// this maps a method name of the form method(III)Ljava/lang/String; to
	// an integer
	// If the value is non-negative, it is a vtable index
	// If it is -1, it is a non-static direct method,
	// If it is -2, it is a static method

	public static final HashMap<String, Integer> methodLookup = new HashMap<String, Integer>();
	public final static int ArrayClassDef = 0;
	public final static int PrimitiveClassDef = 1;
	public final static int UnresolvedClassDef = 2;

	private final static int DirectMethod = -1;
	private final static int StaticMethod = -2;

	public static int getMethodType(String method) {

		Integer val = methodLookup.get(method);
		if (val == null) {
			return -1;
		}
		if (val >= 0) {
			return DeodexUtil.Virtual;
		}
		if (val == DirectMethod) {
			return DeodexUtil.Direct;
		}
		if (val == StaticMethod) {
			return DeodexUtil.Static;
		}
		throw new RuntimeException("Unexpected method type");
	}
}
