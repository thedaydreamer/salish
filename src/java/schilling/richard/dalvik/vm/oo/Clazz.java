package schilling.richard.dalvik.vm.oo;

import com.android.dx.io.ClassDef;
import com.android.dx.io.EncodedValue;
import com.android.dx.rop.code.AccessFlags;

/**
 * Represents a class. Parent to all class types. TODO see
 * dalvik.annotation.Signature. I may have to read these values as well.
 * 
 * @author rschilling
 */
public abstract class Clazz implements Comparable<Clazz> {

	/**
	 * getTypeIndex() returns this value whenever the index has not been set.
	 * This is valid in for array classes and primitive classes, for example.
	 */
	public static final int NO_INDEX = Integer.MIN_VALUE;

	/**
	 * The model that this class definition belongs too. It's parent. Never
	 * null.
	 */
	private final DexModel model;

	/**
	 * The class signature.
	 */
	private final String signature;

	/**
	 * The name of the source file.
	 */
	private final String sourceFile;

	/**
	 * The signature of the superclass.
	 */
	private final String superclassSignature;

	/**
	 * The class definition as read from the DEX file.
	 */
	private final ClassDef classDef;

	private final int typeIndex;

	/**
	 * Annotations applied directly to this class.
	 */
	public AnnotationSet annotations = null;

	/**
	 * Initial values for static members stored in an encoded array.
	 */
	private EncodedValue staticMembersInitialValues;

	public int getTypeIndex() {
		return typeIndex;
	}

	public Clazz(DexModel model, ClassDef classDef, int typeIndex) {

		if (model == null)
			throw new IllegalArgumentException("model cannot be null");

		this.model = model;
		this.classDef = classDef;
		this.typeIndex = typeIndex;

		if (this.classDef != null) {

			signature = this.classDef.getSignature().intern();
			if (!model.isInTypePool(signature))
				throw new IllegalArgumentException(
						"class signature is not in the type pool");

			superclassSignature = this.classDef.getSupertypeSignature()
					.intern();
			if (!model.isInTypePool(superclassSignature))
				throw new IllegalArgumentException(
						"supertype signature is not in the type pool");

			sourceFile = this.classDef.getSourceFileName().intern();
			if (!model.isInStringPool(sourceFile))
				throw new IllegalArgumentException(
						"source file string is not in the string pool");

		} else {

			signature = null;
			superclassSignature = null;
			sourceFile = null;

		}
	}

	public int getAccessFlags() {
		return classDef.getAccessFlags();
	}

	public boolean equals(Object toAnother) {
		if (!(toAnother instanceof Clazz))
			return false;

		Clazz anotherClass = (Clazz) toAnother;
		return signature.equals(anotherClass.signature);

	}

	public String getSuperclassSignature() {
		return superclassSignature;
	}

	public DexModel getModel() {
		return model;
	}

	public String getSignature() {
		return signature;
	}

	/**
	 * Sets the bytes that contain the initial values for static members. The
	 * item passed is expected to contain an encoded array. See
	 * DexBuffer.Section.readEncodedArray.
	 * 
	 * @param encArr
	 *            the encoded array to use as static values.
	 */
	/* package */void setStaticInitialValues(EncodedValue values) {
		staticMembersInitialValues = values;
	}

	public EncodedValue getStaticInitialValues() {
		return staticMembersInitialValues;
	}

	public boolean dvmIsInterfaceClass() {
		return (getAccessFlags() & AccessFlags.ACC_INTERFACE) != 0;
	}

	public boolean dvmIsAbstractClass() {
		return (getAccessFlags() & AccessFlags.ACC_ABSTRACT) != 0;
	}

	public static final boolean CALC_CACHE_STATS = true;

	/*
	 * Determine whether "instance" is an instance of "clazz".
	 * 
	 * Returns 0 (false) if not, 1 (true) if so.
	 */
	public static boolean dvmInstanceof(ObjectClass instance, ObjectClass clazz) {
		if (instance == clazz) {

			return true;
		}

		// FIXME : this is not correct - it's just a place holder for now.
		return instance.getSignature().equals(clazz.getSignature());

		/*
		 * TODO implement this with CALC_CACHE_STATS if (instance == clazz) { if
		 * (CALC_CACHE_STATS) DvmGlobals.gDvm.instanceofCache->trivial++; return
		 * true; } else return dvmInstanceofNonTrivial(instance, clazz);
		 */
	}

	/**
	 * Verify that the class is an array class.
	 * 
	 * TODO (in the c code): there may be some performance advantage to setting
	 * a flag in the accessFlags field instead of chasing into the name string.
	 */
	public boolean dvmIsArrayClass() {
		return this instanceof ArrayClass;
		// return (clazz->descriptor[0] == '[');
	}

	private Clazz superclass;

	public void resolveSuperclass() throws VerifyException {
		if (superclass == null) {
			superclass = model.lookupClassByDescriptor(superclassSignature);
			superclass.resolveSuperclass();
		}
	}

	public Clazz getSuperclass() {

		if (superclass == null)
			throw new RuntimeException("resulveSuperclass not called.");

		return superclass;

	}
}
