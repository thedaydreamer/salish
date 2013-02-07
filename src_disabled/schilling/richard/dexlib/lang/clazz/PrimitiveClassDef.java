package schilling.richard.dexlib.lang.clazz;

import java.util.Hashtable;
import java.util.List;

public final class PrimitiveClassDef extends PlatformClassDef {



	public static final PrimitiveClassDef BYTE_DEF = new PrimitiveClassDef(
			byte.class);

	public static final PrimitiveClassDef SHORT_DEF = new PrimitiveClassDef(
			short.class);
	
	public static final PrimitiveClassDef INT_DEF = new PrimitiveClassDef(
			int.class);
	
	public static final PrimitiveClassDef LONG_DEF = new PrimitiveClassDef(
			long.class);
	
	public static final PrimitiveClassDef FLOAT_DEF = new PrimitiveClassDef(
			float.class);
	
	public static final PrimitiveClassDef DOUBLE_DEF = new PrimitiveClassDef(
			double.class);
	
	public static final PrimitiveClassDef CHAR_DEF = new PrimitiveClassDef(
			char.class);
	
	public static final PrimitiveClassDef BOOLEAN_DEF = new PrimitiveClassDef(
			boolean.class);

	private PrimitiveClassDef(Class<?> baseClass) {
		super(baseClass);
	}

	@Override
	public MethodDef findDirectMethod(String methodName) {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public MethodDef findVirtualMethod(String methodName) {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public int getChildCount() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public int getChildDepth() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public void resolveSuperclass() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public int getClassDepth() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public Hashtable<String, MethodDef> getDirectMethods() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public List<FieldDef> getFields() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public Hashtable<String, ObjectClassDef> getInterfaces(
			boolean includeSuperclasses) {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public ObjectClassDef getSuperclass() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public boolean isSuperclassResolved() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public void verifyInterfaceModifiersOrThrow() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}

	@Override
	public Hashtable<String, MethodDef> getVirtualMethods() {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}
	
	@Override
	public boolean isInterface() {
		return false;
	}

	@Override
	protected void setModifier(ModifierEnum mEnum) {
		throw new UnsupportedOperationException(
				"not supported for PrimitiveClassDef classes.");
	}
}
