package schilling.richard.dalvik.vm.oo;

/**
 * A class definition that is found on the platform. This class associates a
 * signature with a Class object. In addition, it requires that the signnature
 * be non-null. In Clazz, the signature can be null if the associated ClassDef
 * is null.
 * 
 * <p>
 * Because the compiler can generate a class reference to a non-existent class
 * during compile time, this class allows an Object class to be associated with
 * an arbitrary class signature.
 * 
 * @author rschilling
 * 
 */
public class PlatformClass extends ObjectClass {

	// TODO: add the default class definitions.

	private final Class<? extends Object> refersTo;
	private final String sig;

	/**
	 * Creates a platform class derfinition that wraps a class. The specified
	 * class cannot be null.
	 * 
	 * @param c
	 *            the class to wrap.
	 * @throws NullPointerException
	 *             if c is null.
	 */
	public PlatformClass(DexModel model, String signature,
			Class<? extends Object> c) {
		super(model, null, 1);

		if (signature == null)
			throw new IllegalArgumentException("signature cannot be null.");

		refersTo = c;
		sig = signature;
	}

	@Override
	public String getSignature() {
		return sig;
	}

	public Class<? extends Object> refersTo() {
		return refersTo;
	}

	// TODO define resolveSuperclass - use reflection to go all the way back to
	// java.lang.Object.

}
