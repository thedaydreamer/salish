package schilling.richard.dexlib.lang.clazz;

public final class JavaLangObjectClassDef extends PlatformClassDef{
	
	public static final String JAVA_LANG_OBJECT_SPECIFIER = "Ljava/lang/Object;";

	/**
	 * Singleton for java.lang.Object class definition.
	 * Top of the class object tree (e.g. java.lang.Object).
	 */
	public static final PlatformClassDef JAVA_LANG_OBJECT = new JavaLangObjectClassDef();

	/**
	 * The final static value is always used.  This class is never instantiated elsewhere.
	 */
	private JavaLangObjectClassDef() {
		super(java.lang.Object.class);
	}

	
	public ObjectClassDef getSuperclass() {
		return null;
	}
	
	@Override
	public int getClassDepth() {
		return 1;
	}
}
