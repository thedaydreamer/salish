package schilling.richard.dalvik.vm.oo;

import com.android.dx.io.ClassDef;

/**
 * Classes that represent arrays. The class that is passed to the constructor is
 * the type of class that the array is composed of.
 * 
 * @author rschilling
 * 
 */
public class ArrayClass extends Clazz {

	// TODO - does ArrayClass need to have all of the underlying members from
	// Clazz? If not, then rework the class heirarchy.
	/**
	 * The number of dimensions in this array.
	 */
	private final int dimensions;

	public int getarrayDim() {
		return dimensions;
	}

	/**
	 * The signature associated with this array.
	 */
	private final String sig;

	private Clazz elementClass;

	public ArrayClass(DexModel model, int dimensions, ClassDef classDef) {
		// TODO : do Arrays even have a ClassDef associated with them?
		super(model, classDef, Clazz.NO_INDEX);
		sig = classDef.getSignature();
		this.dimensions = dimensions;
		try {
			this.elementClass = model.lookupClassByDescriptor(classDef
					.getSignature());
		} catch (VerifyException ex) {
			throw new IllegalArgumentException("classDef is not valid.", ex);
		}
	}

	public ArrayClass(DexModel model, int dimensions, String signature) {

		super(model, null, Clazz.NO_INDEX);
		sig = signature.intern();
		if (sig == null)
			throw new IllegalArgumentException("signature cannot be null");

		this.dimensions = dimensions;

		try {
			this.elementClass = model.lookupClassByDescriptor(signature);
		} catch (VerifyException ex) {
			throw new IllegalArgumentException("classDef is not valid.", ex);
		}
	}

	public Clazz elementClass() {
		return elementClass;
	}

	@Override
	public String getSignature() {
		// FIXME generate the signature properly based on the class def and the
		// dimensions.
		return sig;
	}

	@Override
	public int compareTo(Clazz another) {
		if (!(another instanceof ArrayClass))
			return 1;

		ArrayClass otherArrayClass = (ArrayClass) another;

		if (dimensions != otherArrayClass.dimensions)
			return (dimensions - otherArrayClass.dimensions);

		return getSignature().compareTo(otherArrayClass.getSignature());
	}

}
