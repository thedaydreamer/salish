package schilling.richard.dexlib.lang.clazz;

import static schilling.richard.dexlib.lang.clazz.ModifierEnum.INTERFACE;
import static schilling.richard.dexlib.lang.clazz.ModifierEnum.STATIC;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import schilling.richard.dexlib.Code.Analysis.ValidationException;
import schilling.richard.dexlib.lang.DexLoader;
import schilling.richard.dexlib.lang.clazz.FieldDef.FieldCategoryEnum;
import schilling.richard.finnr.FinnrApp;

/**
 * Represents a class definition that ultimately resolves to the
 * java.lang.Object definition
 * 
 * @author rschilling
 * 
 */
public class ObjectClassDef extends ClassDef {

	/**
	 * This is set by default until something else re-defines the value. This
	 * value should never be null.
	 */
	private ObjectClassDef superclass = JavaLangObjectClassDef.JAVA_LANG_OBJECT;

	/**
	 * The signature of the super class. This value should never be null.
	 */
	private String superclassSignature = JavaLangObjectClassDef.JAVA_LANG_OBJECT_SPECIFIER;

	/**
	 * Used to traverse the tree from the Object level downward and perform
	 * other useful tasks.
	 */
	private Hashtable<String, ObjectClassDef> children = new Hashtable<String, ObjectClassDef>();

	/**
	 * The set of class definitions that contain all the interfaces that this
	 * class defines. It does not include any interfaces that the super class
	 * defines. To get a list of all interfaces that this class and its
	 * superclasses implement, call getInterfaces(true).
	 * 
	 */
	private Hashtable<String, ObjectClassDef> interfaceTable = null;

	/**
	 * If not null then this class was loaded from this item. It is stored here
	 * for lazy loading of data structures.
	 */
	private DexLoader.TempClassInfo loadedFrom = null;

	private Hashtable<String, MethodDef> virtualMethods = null;

	private Hashtable<String, MethodDef> directMethods = null;

	public ObjectClassDef(String specifier) {
		super(specifier);
	}

	/**
	 * Creates a class definition from a TempClassInfo object. If this
	 * parameter's class specifier points to a platform class then an error will
	 * be generated. The superclass is not loaded.
	 * 
	 * @param tClassInfo
	 *            the class information object to copy information from.
	 * @throws IllegalArgumentException
	 *             if tClassInfo is null.
	 */
	public ObjectClassDef(DexLoader.TempClassInfo tClassInfo) {
		super(tClassInfo.classType);

		loadedFrom = tClassInfo;
		if (loadedFrom.isInterface)
			this.setModifier(INTERFACE);

	}

	/**
	 * Checks to see if the superclass has been resovled for this definition.
	 * This function also
	 * 
	 * @return true if the superclass is resolved or false otherwise.
	 */
	public boolean isSuperclassResolved() {
		if (loadedFrom != null) {
			if (loadedFrom.classType == null)
				throw new IllegalStateException(
						"the DexLoader.TempClassInfo object this class was defined from does not have a superclass specified.");

			if (!superclassSignature.equals(loadedFrom.classType))
				superclassSignature = loadedFrom.classType;
		}

		return superclassSignature.equals(superclass.getSignature()) ? true
				: false;
	}

	/**
	 * Recursively resolves the superclass tree up to java.lang.Object.
	 * 
	 * @throws ClassNotFoundException
	 *             if any superclass could not be found.
	 * @throws ValidationException
	 *             TODO figure out why.
	 */
	public void resolveSuperclass() {

		if (loadedFrom.classType == null)
			throw new IllegalStateException(
					"the DexLoader.TempClassInfo object this class was defined from does not have a superclass specified.");

		if (loadedFrom != null
				&& !superclassSignature.equals(loadedFrom.superclassType))
			superclassSignature = loadedFrom.superclassType;

		if (!superclassSignature.equals(superclass.getSignature())) {

			DexLoader loader = FinnrApp.getApp().getDexLoader();

			try {
				superclass = (ObjectClassDef) loader
						.loadClass(superclassSignature);

			} catch (ClassCastException ex) {

				throw new IllegalStateException("the super class to "
						+ getSignature() + ", (" + superclassSignature
						+ ") is not an object class.", ex);

			} catch (ValidationException e) {
				throw new RuntimeException("TODO: what do we do with this?", e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(
						"the super class could not be loaded", e);
			}

		}

		superclass.resolveSuperclass();

	}

	/**
	 * Returns the super class. See javaLangObjectClassDef for more information.
	 * 
	 * @return the superclass of ths object.
	 */
	public ObjectClassDef getSuperclass() {

		resolveSuperclass();

		return superclass;
	}

	/**
	 * Returns the number of super classes this one has. The count includes
	 * java.lang.Object;
	 * 
	 * @return
	 */
	public int getClassDepth() {

		int superDepth = getSuperclass().getClassDepth();
		int thisDepth = superDepth + 1;
		return thisDepth;

	}

	/**
	 * Returns the number of subclasses for the entire class tree. This recurses
	 * all of the child classes that extend this one. Calling this may cause the
	 * stack to run out of room if it is called on a memory constrained device.
	 * 
	 * @return the number of classes in the entire class tree that extend this
	 *         one.
	 */
	public int getChildDepth() {

		int result = 0;

		for (ObjectClassDef ch : children.values()) {

			result += ch.getChildDepth(); // recursive.

		}

		return result;
	}

	/**
	 * Returns the number of classes that extends this one directly.
	 * 
	 * @return the number of classes that extend this one.
	 */
	public int getChildCount() {

		return children.size();

	}

	public void verifyInterfaceModifiersOrThrow() {
		for (ClassDef cDef : interfaceTable.values()) {
			if (cDef instanceof PlatformClassDef){
				PlatformClassDef pcDef = (PlatformClassDef)cDef;
				if (!pcDef.refersTo().isInterface())
					throw new IllegalStateException("platform class interface "
							+ cDef.getSignature()
							+ " is not an interface but it's in the interface table " + 
							" for class " + cDef.getSignature());
					
			} else if (!cDef.isInterface())
				throw new IllegalStateException("interface "
						+ cDef.getSignature()
						+ " does not have a modifier of INTERFACE but it's in " 
						+ " the interface table for class " + cDef.getSignature());
		}

	}

	/**
	 * Eeturns a list of interface class definitions that this class def
	 * implements. If includeSuperclasses is true then all the interfaces that
	 * any super class implements will also be included.
	 * 
	 * <p>
	 * If this function is overridden and the function is not supported then it
	 * should throw UnsupportedOperationException.
	 * 
	 * @param includeSuperclasses
	 *            if true, all superclass interfaces will be included in the
	 *            list and if false only this class' interfaces.
	 * 
	 * @return the interfaces, if any, that this class declares in is class
	 *         definition.
	 * 
	 * @throws UnsupportedOperationException
	 *             if an overriding method does not support this call.
	 */
	public Hashtable<String, ObjectClassDef> getInterfaces(
			boolean includeSuperclasses) {

		if (interfaceTable == null) {

			interfaceTable = new Hashtable<String, ObjectClassDef>();

			if (loadedFrom != null && loadedFrom.interfaces != null) {
				DexLoader loader = FinnrApp.getApp().getDexLoader();

				for (String iName : loadedFrom.interfaces) {
					try {

						ObjectClassDef oClass = (ObjectClassDef) loader
								.loadClass(iName);
						interfaceTable.put(oClass.getSignature(), oClass);

					} catch (ClassCastException ex) {
						throw new RuntimeException(
								getSignature()
										+ " implements an interface that does not resolve to an ObjectClassDef instance.",
								ex);
					} catch (ClassNotFoundException ex) {
						throw new RuntimeException(getSignature()
								+ " has an interface " + iName
								+ " that could not be loaded.");
					}

				}
			}
		}

		// now that all interfaces are loaded, resolve the superinterfaces..
		for (ObjectClassDef oClass : interfaceTable.values()) {
			oClass.resolveSuperclass();

		}

		verifyInterfaceModifiersOrThrow();

		Hashtable<String, ObjectClassDef> result = (Hashtable<String, ObjectClassDef>) interfaceTable
				.clone();

		if (!includeSuperclasses)
			return interfaceTable;

		try {
			Hashtable<String, ObjectClassDef> superclassInterfaces = superclass
					.getInterfaces(includeSuperclasses);
			result.putAll(superclassInterfaces);
		} catch (UnsupportedOperationException ex) {
			// do nothing - we just won't include the superclass' methods which
			// is acceptable
		}

		return result;
	}

	public Hashtable<String, MethodDef> getVirtualMethods() {

		if (virtualMethods == null) {
			virtualMethods = new Hashtable<String, MethodDef>();
			if (loadedFrom != null && loadedFrom.virtualMethods != null) {
				for (String s : loadedFrom.virtualMethods) {
					virtualMethods.put(s, new MethodDef(this, s));
				}
			}
		}

		Hashtable<String, MethodDef> result = new Hashtable<String, MethodDef>();
		result.putAll(virtualMethods);

		return result;
	}

	public Hashtable<String, MethodDef> getDirectMethods() {
		if (directMethods == null) {
			directMethods = new Hashtable<String, MethodDef>();

			if (loadedFrom != null && loadedFrom.directMethods != null) {

				for (int i = 0; i < loadedFrom.directMethods.length; i++) {
					MethodDef md = new MethodDef(this,
							loadedFrom.directMethods[i]);

					if (loadedFrom.staticMethods[i])
						md.setModifier(STATIC);
					directMethods.put(loadedFrom.directMethods[i],
							new MethodDef(this, loadedFrom.directMethods[i]));

				}
			}
		}

		Hashtable<String, MethodDef> result = new Hashtable<String, MethodDef>();
		result.putAll(directMethods);

		return result;
	}

	public MethodDef findVirtualMethod(String methodName) {
		Hashtable<String, MethodDef> vMethods = getVirtualMethods();

		if (vMethods.containsKey(methodName))
			return vMethods.get(methodName);

		return superclass.findVirtualMethod(methodName);

	}

	public MethodDef findDirectMethod(String methodName) {
		Hashtable<String, MethodDef> dMethods = getDirectMethods();
		if (dMethods.containsKey(methodName))
			return dMethods.get(methodName);

		return superclass.findDirectMethod(methodName);
	}

	private List<FieldDef> classFields = null;

	public List<FieldDef> getFields() {

		if (classFields == null) {
			classFields = new LinkedList<FieldDef>();

			if (loadedFrom != null && loadedFrom.instanceFields != null) {
				for (String[] fieldInfo : loadedFrom.instanceFields) {
					String fieldName = fieldInfo[0];
					FieldCategoryEnum category = FieldCategoryEnum
							.getFieldType(fieldInfo[1]);

					ClassDef fieldClass = ClassDefFactory
							.createClassDef(fieldInfo[1]);

					FieldDef fDef = new FieldDef(fieldClass, fieldName,
							category);
					
					fDef.setDeclaringClass(this);

					// TODO what about field modifiers?
					classFields.add(fDef);

				}

			}

			// TODO for output: 1. sort the classFields array - REFERENCE first,
			// WIDE next, OTHER last; 2: calculate offsets. See
			// ClassPath.loadFields()
			// TODO offsets must be calculated the same way Dalvik calculates
			// them: see comment below

			/*
			 * // This is a bit of an "involved" operation. We need to follow
			 * the // same algorithm that dalvik uses to // arrange fields, so
			 * that we end up with the same field offsets // (which is needed
			 * for deodexing). // See mydroid/dalvik/vm/oo/Class.c -
			 * computeFieldOffsets()
			 */

		}

		return classFields;
	}

}
