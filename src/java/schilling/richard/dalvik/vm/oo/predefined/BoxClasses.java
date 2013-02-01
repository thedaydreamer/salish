package schilling.richard.dalvik.vm.oo.predefined;

public final class BoxClasses {

	private BoxClasses() {
	}

	public static final String SIG_BOOLEAN = "Ljava/lang/Boolean;";
	public static final String SIG_CHAR = "Ljava/lang/Character;";
	public static final String SIG_FLOAT = "Ljava/lang/Float;";
	public static final String SIG_DOUBLE = "Ljava/lang/Double;";
	public static final String SIG_BYTE = "Ljava/lang/Byte;";
	public static final String SIG_SHORT = "Ljava/lang/Short;";
	public static final String SIG_INTEGER = "Ljava/lang/Integer;";
	public static final String SIG_LONG = "Ljava/lang/Long;";

	/**
	 * While not strictly a primitive, we treat Void pointer as if it is one.
	 */
	public static final String SIG_VOID = "Ljava/lang/Void;";
}
