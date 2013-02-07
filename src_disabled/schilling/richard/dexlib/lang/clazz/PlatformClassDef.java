package schilling.richard.dexlib.lang.clazz;

import java.util.Hashtable;
import java.util.List;

/**
 * The definition for a class that is found on the platform. That is, it is not
 * loaded from a DEX file but from the classloader.
 * 
 * @author rschilling
 * 
 */
public class PlatformClassDef extends ObjectClassDef {

	// TODO : decision - is it better to limit the platform class wrapping to a
	// simple reference, or should we build up the platform class model with
	// PlatformClassDef objects as well?

	private final Class<? extends Object> refersTo;

	/**
	 * Creates a platform class derfinition that wraps a class. The specified
	 * class cannot be null.
	 * 
	 * @param c
	 *            the class to wrap.
	 * @throws NullPointerException
	 *             if c is null.
	 */
	public PlatformClassDef(Class<? extends Object> c) {
		super(c.getName());
		refersTo = c;
	}

	public Class<? extends Object> refersTo() {
		return refersTo;
	}

	@Override
	public MethodDef findDirectMethod(String methodName) {
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");

	}

	@Override
	public MethodDef findVirtualMethod(String methodName) {
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");

	}

	@Override
	public int getChildCount() {
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");
	}

	@Override
	public int getChildDepth() {
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");
	}

	@Override
	public void resolveSuperclass() {
	}

	@Override
	public int getClassDepth() {

		// find the number of superclasses
		int result = 1;

		Class<?> cur = refersTo.getSuperclass();
		while (cur != null) {
			result++;
			cur = cur.getSuperclass();
		}

		return result;

	}

	@Override
	public Hashtable<String, MethodDef> getDirectMethods() {
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");
	}

	@Override
	public List<FieldDef> getFields() {
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");
	}

	@Override
	public Hashtable<String, ObjectClassDef> getInterfaces(
			boolean includeSuperclasses){
		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.");
	}

	@Override
	public ObjectClassDef getSuperclass() {

		throw new UnsupportedOperationException(
				"not supported for PlatformClassDef objects.  Use refersTo().getSuperclass()");

	}

	@Override
	public boolean isSuperclassResolved() {
		return true;
	}

	@Override
	public void verifyInterfaceModifiersOrThrow() {
		// do nothing.
	}

}
