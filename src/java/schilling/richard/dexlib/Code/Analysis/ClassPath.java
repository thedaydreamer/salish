/*
 * [The "BSD licence"]
 * Copyright (c) 2010 Ben Gruver (JesusFreke)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package schilling.richard.dexlib.Code.Analysis;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.deserialize.ClassDataItem;
import schilling.richard.dexlib.io.deserialize.ClassDataItem.EncodedField;
import schilling.richard.dexlib.io.deserialize.ClassDataItem.EncodedMethod;
import schilling.richard.dexlib.io.deserialize.ClassDefItem;
import schilling.richard.dexlib.io.deserialize.TypeIdItem;
import schilling.richard.dexlib.io.deserialize.TypeListItem;
import schilling.richard.dexlib.io.util.AccessFlags;
import android.util.Log;
import android.util.SparseArray;

// TODO: Get rid of this class.  It's not necessary. But it has to be refactored out.
/**
 * A utility to load class definitions. This is essentially a wrapper class for
 * a DexFile on Android since the only classes we're interacting with are either
 * in the DexFile or on the platform. Any use of a class not in either of those
 * two categories indicates a critical state failure.
 * <P>
 * To fix that problem either the input dex needs to be modified or the OS would
 * need to be modified. In either case, it indicates a serious error that
 * prevents the program from continuing.
 * 
 * @author rschilling
 */
public class ClassPath {

	public static final String LOG_TAG = "Finnr.ClassPath";

	private static ClassPath theClassPath = null;

	private final HashMap<String, ClassPathedClassDef> classDefs;
	protected ClassPathedClassDef javaLangObjectClassDef; // Ljava/lang/Object;

	/*
	 * This is only used while initialing the class path. It is set to null
	 * after initialization has finished.
	 */

	private static final Pattern dalvikCacheOdexPattern = Pattern
			.compile("@([^@]+)@classes.dex$"); // TODO
												// move
												// all
												// patterns
												// to
												// Patterns
												// class.

	public static interface ClassPathErrorHandler {

		void ClassPathError(String className, Exception ex);
	}

	/**
	 * Initialize the class path using the given boot class path entries
	 * 
	 * @param classPathDirs
	 *            The directories to search for boot class path files
	 * @param bootClassPath
	 *            A list of the boot class path entries to search for and load
	 * @param dexFilePath
	 *            The path of the dex file (used for error reporting purposes
	 *            only)
	 * @param dexFile
	 *            the DexFile to load
	 * @param errorHandler
	 *            a ClassPathErrorHandler object to receive and handle any
	 *            errors that occur while loading classes
	 */
	public static void InitializeClassPath(DexFile dexFile) {
		if (theClassPath != null) {
			throw new IllegalArgumentException(
					"Cannot initialize ClassPath multiple times");
		}

		theClassPath = new ClassPath();
		theClassPath.initClassPath(dexFile);
	}

	public static boolean isInitialized() {
		return theClassPath == null ? false : true;
	}

	private ClassPath() {
		classDefs = new HashMap<String, ClassPathedClassDef>();
	}

	private void initClassPath(DexFile dexFile) {
		LinkedHashMap<String, TempClassInfo> tempClasses = new LinkedHashMap<String, TempClassInfo>();

		if (dexFile != null) {
			for (ClassDefItem classDefItem : dexFile.ClassDefsSection
					.getItems()) {
				// TODO: need to check if the class already exists. (and if so,
				// what
				// to do about it?)
				TempClassInfo tempClassInfo = new TempClassInfo(classDefItem);

				tempClasses.put(tempClassInfo.classType, tempClassInfo);

			}
		}

		// tempClasses is filled.
		for (String classType : tempClasses.keySet()) {
			ClassPathedClassDef classDef = null;
			try {
				classDef = ClassPath.loadClassDef(classType);

				if (classDef == null) {

					classDef = new ClassPathedClassDef(
							tempClasses.get(classType));
					classDefs.put(classDef.getClassType(), classDef);
				}
			} catch (Exception ex) {

				throw new RuntimeException(String.format(
						"Error while loading ClassPath class %s", classType),
						ex);

			}

			if (classType.equals("Ljava/lang/Object;")) {
				this.javaLangObjectClassDef = classDef;
			}
		}

		for (String primitiveType : new String[] { "Z", "B", "S", "C", "I",
				"J", "F", "D" }) {
			ClassPathedClassDef classDef = new PrimitiveClassDef(primitiveType);
			classDefs.put(primitiveType, classDef);
		}

		tempClasses = null;
	}

	// TODO: Move ClassNotFoundException to its own class file.
	private static class ClassNotFoundException extends RuntimeException {

		public ClassNotFoundException(String message) {
			super(message);
		}
	}

	public static ClassPathedClassDef getClassDef(String classType) {
		return getClassDef(classType, true);
	}

	/**
	 * This method checks if the given class has been loaded yet. If it has, it
	 * returns the loaded ClassDef. If not, then it looks up the TempClassItem
	 * for the given class and (possibly recursively) loads the ClassDef.
	 * 
	 * @param classType
	 *            the class to load
	 * @return the existing or newly loaded ClassDef object for the given class,
	 *         or null if the class cannot be found
	 */
	private static ClassPathedClassDef loadClassDef(String classType) {
		// TODO: return PlatformClass if the class is found on the platform.
		ClassPathedClassDef classDef = getClassDef(classType, false);
		return classDef;
	}

	public static ClassPathedClassDef getClassDef(String classType,
			boolean createUnresolvedClassDef) {
		ClassPathedClassDef classDef = theClassPath.classDefs.get(classType);
		if (classDef == null) {
			// if it's an array class, try to create it
			if (classType.charAt(0) == '[') {
				return theClassPath.createArrayClassDef(classType);
			} else {
				if (createUnresolvedClassDef) {
					// TODO: we should output a warning
					return theClassPath.createUnresolvedClassDef(classType);
				} else {
					return null;
				}
			}
		}
		return classDef;
	}

	public static ClassPathedClassDef getClassDef(TypeIdItem classType) {
		return getClassDef(classType.getTypeDescriptor());
	}

	public static ClassPathedClassDef getClassDef(TypeIdItem classType,
			boolean creatUnresolvedClassDef) {
		return getClassDef(classType.getTypeDescriptor(),
				creatUnresolvedClassDef);
	}

	// 256 [ characters
	private static final String arrayPrefix = "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[["
			+ "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[["
			+ "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[";

	private static ClassPathedClassDef getArrayClassDefByElementClassAndDimension(
			ClassPathedClassDef classDef, int arrayDimension) {
		return getClassDef(arrayPrefix.substring(256 - arrayDimension)
				+ classDef.classType);
	}

	private ClassPathedClassDef createUnresolvedClassDef(String classType) {
		assert classType.charAt(0) == 'L';

		UnresolvedClassDef unresolvedClassDef = new UnresolvedClassDef(
				classType);
		classDefs.put(classType, unresolvedClassDef);
		return unresolvedClassDef;
	}

	private ClassPathedClassDef createArrayClassDef(String arrayClassName) {
		assert arrayClassName != null;
		assert arrayClassName.charAt(0) == '[';

		ArrayClassDef arrayClassDef = new ArrayClassDef(arrayClassName);
		if (arrayClassDef.elementClass == null) {
			return null;
		}

		classDefs.put(arrayClassName, arrayClassDef);
		return arrayClassDef;
	}

	public static ClassPathedClassDef getCommonSuperclass(
			ClassPathedClassDef class1, ClassPathedClassDef class2) {
		if (class1 == class2) {
			return class1;
		}

		if (class1 == null) {
			return class2;
		}

		if (class2 == null) {
			return class1;
		}

		// TODO: do we want to handle primitive types here? I don't think so..
		// (if not, add assert)

		if (class2.isInterface) {
			if (class1.implementsInterface(class2)) {
				return class2;
			}
			return theClassPath.javaLangObjectClassDef;
		}

		if (class1.isInterface) {
			if (class2.implementsInterface(class1)) {
				return class1;
			}
			return theClassPath.javaLangObjectClassDef;
		}

		if (class1 instanceof ArrayClassDef && class2 instanceof ArrayClassDef) {
			return getCommonArraySuperclass((ArrayClassDef) class1,
					(ArrayClassDef) class2);
		}

		// we've got two non-array reference types. Find the class depth of
		// each, and then move up the longer one
		// so that both classes are at the same class depth, and then move each
		// class up until they match

		// we don't strictly need to keep track of the class depth separately,
		// but it's probably slightly faster
		// to do so, rather than calling getClassDepth() many times
		int class1Depth = class1.getClassDepth();
		int class2Depth = class2.getClassDepth();

		while (class1Depth > class2Depth) {
			class1 = class1.superclass;
			class1Depth--;
		}

		while (class2Depth > class1Depth) {
			class2 = class2.superclass;
			class2Depth--;
		}

		while (class1Depth > 0) {
			if (class1 == class2) {
				return class1;
			}
			class1 = class1.superclass;
			class1Depth--;
			class2 = class2.superclass;
			class2Depth--;
		}

		return class1;
	}

	private static ClassPathedClassDef getCommonArraySuperclass(
			ArrayClassDef class1, ArrayClassDef class2) {
		assert class1 != class2;

		// If one of the arrays is a primitive array, then the only option is to
		// return java.lang.Object
		// TODO: might it be possible to merge something like int[] and short[]
		// into int[]? (I don't think so..)
		if (class1.elementClass instanceof PrimitiveClassDef
				|| class2.elementClass instanceof PrimitiveClassDef) {
			return theClassPath.javaLangObjectClassDef;
		}

		// if the two arrays have the same number of dimensions, then we should
		// return an array class with the
		// same number of dimensions, for the common superclass of the 2 element
		// classes
		if (class1.arrayDimensions == class2.arrayDimensions) {
			ClassPathedClassDef commonElementClass = getCommonSuperclass(
					class1.elementClass, class2.elementClass);
			return getArrayClassDefByElementClassAndDimension(
					commonElementClass, class1.arrayDimensions);
		}

		// something like String[][][] and String[][] should be merged to
		// Object[][]
		// this also holds when the element classes aren't the same (but are
		// both reference types)
		int dimensions = Math.min(class1.arrayDimensions,
				class2.arrayDimensions);
		return getArrayClassDefByElementClassAndDimension(
				theClassPath.javaLangObjectClassDef, dimensions);
	}

	public static class ArrayClassDef extends ClassPathedClassDef {

		private final ClassPathedClassDef elementClass;
		private final int arrayDimensions;

		protected ArrayClassDef(String arrayClassType) {
			super(arrayClassType, ClassPathedClassDef.ArrayClassDef);
			assert arrayClassType.charAt(0) == '[';

			int i = 0;
			while (arrayClassType.charAt(i) == '[')
				i++;

			String elementClassType = arrayClassType.substring(i);

			if (i > 256) {
				throw new RuntimeException(
						"Error while creating array class for element type "
								+ elementClassType
								+ " with "
								+ i
								+ " dimensions. The maximum number of dimensions is 256");
			}

			// TODO: in analysis, identify where exceptions are swallowed (e.g.
			// redirected somewhere else).

			elementClass = ClassPath.getClassDef(arrayClassType.substring(i));

			arrayDimensions = i;
		}

		/**
		 * Returns the "base" element class of the array. For example, for a
		 * multi-dimensional array of strings ([[Ljava/lang/String;), this
		 * method would return Ljava/lang/String;
		 * 
		 * @return the "base" element class of the array
		 */
		public ClassPathedClassDef getBaseElementClass() {
			return elementClass;
		}

		/**
		 * Returns the "immediate" element class of the array. For example, for
		 * a multi-dimensional array of stings with 2 dimensions
		 * ([[Ljava/lang/String;), this method would return [Ljava/lang/String;
		 * 
		 * @return the immediate element class of the array
		 */
		public ClassPathedClassDef getImmediateElementClass() {
			if (arrayDimensions == 1) {
				return elementClass;
			}
			return getArrayClassDefByElementClassAndDimension(elementClass,
					arrayDimensions - 1);
		}

		public int getArrayDimensions() {
			return arrayDimensions;
		}

		@Override
		public boolean extendsClass(ClassPathedClassDef superclassDef) {
			if (!(superclassDef instanceof ArrayClassDef)) {
				if (superclassDef == ClassPath.theClassPath.javaLangObjectClassDef) {
					return true;
				} else if (superclassDef.isInterface) {
					return this.implementsInterface(superclassDef);
				}
				return false;
			}

			ArrayClassDef arraySuperclassDef = (ArrayClassDef) superclassDef;
			if (this.arrayDimensions == arraySuperclassDef.arrayDimensions) {
				ClassPathedClassDef baseElementClass = arraySuperclassDef
						.getBaseElementClass();

				if (baseElementClass.isInterface) {
					return true;
				}

				return baseElementClass.extendsClass(arraySuperclassDef
						.getBaseElementClass());
			} else if (this.arrayDimensions > arraySuperclassDef.arrayDimensions) {
				ClassPathedClassDef baseElementClass = arraySuperclassDef
						.getBaseElementClass();
				if (baseElementClass.isInterface) {
					return true;
				}

				if (baseElementClass == ClassPath.theClassPath.javaLangObjectClassDef) {
					return true;
				}
				return false;
			}
			return false;
		}
	}

	public static class PrimitiveClassDef extends ClassPathedClassDef {

		protected PrimitiveClassDef(String primitiveClassType) {
			super(primitiveClassType, ClassPathedClassDef.PrimitiveClassDef);
			assert primitiveClassType.charAt(0) != 'L'
					&& primitiveClassType.charAt(0) != '[';
		}
	}

	public static class UnresolvedClassDef extends ClassPathedClassDef {

		protected UnresolvedClassDef(String unresolvedClassDef) {
			super(unresolvedClassDef, ClassPathedClassDef.UnresolvedClassDef);
			assert unresolvedClassDef.charAt(0) == 'L';
		}

		protected ValidationException unresolvedValidationException() {
			return new ValidationException(String.format(
					"class %s cannot be resolved.", this.getClassType()));
		}

		@Override
		public ClassPathedClassDef getSuperclass() {
			throw unresolvedValidationException();
		}

		@Override
		public int getClassDepth() {
			throw unresolvedValidationException();
		}

		@Override
		public boolean isInterface() {
			throw unresolvedValidationException();
		}

		@Override
		public boolean extendsClass(ClassPathedClassDef superclassDef) {
			if (superclassDef != theClassPath.javaLangObjectClassDef
					&& superclassDef != this) {
				throw unresolvedValidationException();
			}
			return true;
		}

		@Override
		public boolean implementsInterface(ClassPathedClassDef interfaceDef) {
			throw unresolvedValidationException();
		}

		@Override
		public boolean hasVirtualMethod(String method) {
			if (!super.hasVirtualMethod(method)) {
				throw unresolvedValidationException();
			}
			return true;
		}
	}

	public static class FieldDef {

		public final String definingClass;
		public final String name;
		public final String type;

		public FieldDef(String definingClass, String name, String type) {
			this.definingClass = definingClass;
			this.name = name;
			this.type = type;
		}
	}

	public static class ClassPathedClassDef implements
			Comparable<ClassPathedClassDef> {

		private String classType;
		private ClassPathedClassDef superclass;
		/**
		 * This is a list of all of the interfaces that a class implements,
		 * either directly or indirectly. It includes all interfaces implemented
		 * by the superclass, and all super-interfaces of any implemented
		 * interface. The intention is to make it easier to determine whether
		 * the class implements a given interface or not.
		 */
		private final TreeSet<ClassPathedClassDef> implementedInterfaces;

		private final boolean isInterface;

		private final int classDepth;

		private String[] vtable;

		// this maps a method name of the form method(III)Ljava/lang/String; to
		// an integer
		// If the value is non-negative, it is a vtable index
		// If it is -1, it is a non-static direct method,
		// If it is -2, it is a static method
		private HashMap<String, Integer> methodLookup;

		private SparseArray<FieldDef> instanceFields;

		public final static int ArrayClassDef = 0;
		public final static int PrimitiveClassDef = 1;
		public final static int UnresolvedClassDef = 2;

		private final static int DirectMethod = -1;
		private final static int StaticMethod = -2;

		/**
		 * The following fields are used only during the initial loading of
		 * classes, and are set to null afterwards TODO: free these
		 */

		// This is only the virtual methods that this class declares itself.
		private String[] virtualMethods;
		// this is a list of all the interfaces that the class implements
		// directory, or any super interfaces of those
		// interfaces. It is generated in such a way that it is ordered in the
		// same way as dalvik's ClassObject.iftable,
		private LinkedHashMap<String, ClassPathedClassDef> interfaceTable;

		/**
		 * This constructor is used for the ArrayClassDef, PrimitiveClassDef and
		 * UnresolvedClassDef subclasses
		 * 
		 * @param classType
		 *            the class type
		 * @param classFlavor
		 *            one of ArrayClassDef, PrimitiveClassDef or
		 *            UnresolvedClassDef
		 */
		protected ClassPathedClassDef(String classType, int classFlavor) {
			if (classFlavor == ArrayClassDef) {
				assert classType.charAt(0) == '[';
				this.classType = classType;
				this.superclass = ClassPath.theClassPath.javaLangObjectClassDef;
				implementedInterfaces = new TreeSet<ClassPathedClassDef>();
				implementedInterfaces.add(ClassPath
						.getClassDef("Ljava/lang/Cloneable;"));
				implementedInterfaces.add(ClassPath
						.getClassDef("Ljava/io/Serializable;"));
				isInterface = false;

				vtable = superclass.vtable;
				methodLookup = superclass.methodLookup;

				instanceFields = superclass.instanceFields;
				classDepth = 1; // 1 off from java.lang.Object

				virtualMethods = null;
				interfaceTable = null;
			} else if (classFlavor == PrimitiveClassDef) {
				// primitive type
				assert classType.charAt(0) != '[' && classType.charAt(0) != 'L';

				this.classType = classType;
				this.superclass = null;
				implementedInterfaces = null;
				isInterface = false;
				vtable = null;
				methodLookup = null;
				instanceFields = null;
				classDepth = 0; // TODO: maybe use -1 to indicate not
								// applicable?

				virtualMethods = null;
				interfaceTable = null;
			} else /* if (classFlavor == UnresolvedClassDef) */{
				assert classType.charAt(0) == 'L';
				this.classType = classType; // FIXME call this the specifier in
											// schilling.richard.dexlib.lang.clazz.ClassDef.

				implementedInterfaces = new TreeSet<ClassPathedClassDef>();
				isInterface = false;

				classDepth = 1; // 1 off from java.lang.Object

				virtualMethods = null;
				interfaceTable = null;

				if (superclass != null) {
					this.superclass = ClassPath.theClassPath.javaLangObjectClassDef;
					vtable = superclass.vtable;
					methodLookup = superclass.methodLookup;
					instanceFields = superclass.instanceFields;

				}
			}
		}

		protected ClassPathedClassDef(TempClassInfo classInfo) {
			classType = classInfo.classType; // see ClassDef.specifier
			isInterface = classInfo.isInterface; // see ClassDef.isInterface().

			superclass = loadSuperclass(classInfo); // see
													// ObjectClassDef.getClassDepth()
			if (superclass == null) {
				classDepth = 0;
			} else {
				classDepth = superclass.classDepth + 1;
			}

			implementedInterfaces = loadAllImplementedInterfaces(classInfo); // see
																				// ObjectClassDef.getInterfaces().

			// TODO: we can probably get away with only creating the interface
			// table for interface types
			interfaceTable = loadInterfaceTable(classInfo);
			virtualMethods = classInfo.virtualMethods;
			vtable = loadVtable(classInfo);

			int directMethodCount = 0;
			if (classInfo.directMethods != null) {
				directMethodCount = classInfo.directMethods.length;
			}
			methodLookup = new HashMap<String, Integer>(
					(int) Math
							.ceil(((vtable.length + directMethodCount) / .7f)),
					.75f);
			for (int i = 0; i < vtable.length; i++) {
				methodLookup.put(vtable[i], i);
			}
			if (directMethodCount > 0) {
				for (int i = 0; i < classInfo.directMethods.length; i++) {
					if (classInfo.staticMethods[i]) {
						methodLookup.put(classInfo.directMethods[i],
								StaticMethod);
					} else {
						methodLookup.put(classInfo.directMethods[i],
								DirectMethod);
					}
				}
			}

			instanceFields = loadFields(classInfo);
		}

		public String getClassType() {
			return classType;
		}

		public ClassPathedClassDef getSuperclass() {
			return superclass;
		}

		public int getClassDepth() {
			return classDepth;
		}

		public boolean isInterface() {
			return this.isInterface;
		}

		public boolean extendsClass(ClassPathedClassDef superclassDef) {
			if (superclassDef == null) {
				return false;
			}

			if (this == superclassDef) {
				return true;
			}

			if (superclassDef instanceof UnresolvedClassDef) {
				throw ((UnresolvedClassDef) superclassDef)
						.unresolvedValidationException();
			}

			int superclassDepth = superclassDef.classDepth;
			ClassPathedClassDef ancestor = this;
			while (ancestor.classDepth > superclassDepth) {
				ancestor = ancestor.getSuperclass();
			}

			return ancestor == superclassDef;
		}

		/**
		 * Returns true if this class implements the given interface. This
		 * searches the interfaces that this class directly implements, any
		 * interface implemented by this class's superclasses, and any
		 * super-interface of any of these interfaces.
		 * 
		 * @param interfaceDef
		 *            the interface
		 * @return true if this class implements the given interface
		 */
		public boolean implementsInterface(ClassPathedClassDef interfaceDef) {
			assert !(interfaceDef instanceof UnresolvedClassDef);
			return implementedInterfaces.contains(interfaceDef);
		}

		public boolean hasVirtualMethod(String method) {
			Integer val = methodLookup.get(method);
			if (val == null || val < 0) {
				return false;
			}
			return true;
		}

		public int getMethodType(String method) {
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

		public FieldDef getInstanceField(int fieldOffset) {
			return this.instanceFields.get(fieldOffset, null);
		}

		public String getVirtualMethod(int vtableIndex) {
			if (vtableIndex < 0 || vtableIndex >= vtable.length) {
				return null;
			}
			return this.vtable[vtableIndex];
		}

		private void swap(byte[] fieldTypes, FieldDef[] fields, int position1,
				int position2) {
			byte tempType = fieldTypes[position1];
			fieldTypes[position1] = fieldTypes[position2];
			fieldTypes[position2] = tempType;

			FieldDef tempField = fields[position1];
			fields[position1] = fields[position2];
			fields[position2] = tempField;
		}

		private ClassPathedClassDef loadSuperclass(TempClassInfo classInfo) {
			if (ClassLoaderUtil.isFoundOnPlatform(classInfo.superclassType)) {
				if (classInfo.classType.equals("Ljava/lang/Object;")) {
					if (classInfo.superclassType != null) {
						throw new RuntimeException("Invalid superclass "
								+ classInfo.superclassType
								+ " for Ljava/lang/Object;. "
								+ "The Object class cannot have a superclass");
					}

				}

				Log.d(LOG_TAG, "Class " + classInfo.classType
						+ " is a platform class. Skipping.");

				return null;
			} else {
				String superclassType = classInfo.superclassType;
				if (superclassType == null) {
					throw new RuntimeException(classInfo.classType
							+ " has no superclass");
				}

				ClassPathedClassDef superclass = ClassPath
						.loadClassDef(superclassType);
				if (superclass == null) {
					throw new ClassNotFoundException(String.format(
							"Could not find superclass %s", superclassType));
				}

				if (!isInterface && superclass.isInterface) {
					throw new ValidationException("Class " + classType
							+ " has the interface " + superclass.classType
							+ " as its superclass");
				}
				if (isInterface
						&& !superclass.isInterface
						&& superclass != ClassPath.theClassPath.javaLangObjectClassDef) {
					throw new ValidationException("Interface " + classType
							+ " has the non-interface class "
							+ superclass.classType + " as its superclass");
				}

				return superclass;
			}
		}

		/**
		 * Loads all interfaces that are not package classes. I think the
		 * originally written method assumed that all classes right down to
		 * java.lang.object would be available.
		 * 
		 * @param classInfo
		 * @return
		 */
		private TreeSet<ClassPathedClassDef> loadAllImplementedInterfaces(
				TempClassInfo classInfo) {
			assert classType != null;
			// TODO create constant out of the lava/lang/Object string.
			assert classType.equals("Ljava/lang/Object;") || superclass != null;
			assert classInfo != null;

			TreeSet<ClassPathedClassDef> implementedInterfaceSet = new TreeSet<ClassPathedClassDef>();

			if (superclass != null) {
				for (ClassPathedClassDef interfaceDef : superclass.implementedInterfaces) {
					implementedInterfaceSet.add(interfaceDef);
				}
			}

			if (classInfo.interfaces != null) {
				for (String interfaceType : classInfo.interfaces) {
					ClassPathedClassDef interfaceDef = ClassPath
							.loadClassDef(interfaceType);
					if (interfaceDef == null) {
						throw new ClassNotFoundException(String.format(
								"Could not find interface %s", interfaceType));
					}
					assert interfaceDef.isInterface();
					implementedInterfaceSet.add(interfaceDef);

					interfaceDef = interfaceDef.getSuperclass();
					while (interfaceDef != null
							&& !interfaceDef.getClassType().equals(
									"Ljava/lang/Object;")) {
						assert interfaceDef.isInterface();
						implementedInterfaceSet.add(interfaceDef);
						if (interfaceDef.getSuperclass() == null)
							Log.i(LOG_TAG, "superinterface is null.");
						interfaceDef = interfaceDef.getSuperclass();

					}
				}
			}

			return implementedInterfaceSet;
		}

		private LinkedHashMap<String, ClassPathedClassDef> loadInterfaceTable(
				TempClassInfo classInfo) {
			if (classInfo.interfaces == null) {
				return null;
			}

			LinkedHashMap<String, ClassPathedClassDef> interfaceTable = new LinkedHashMap<String, ClassPathedClassDef>();

			for (String interfaceType : classInfo.interfaces) {
				if (!interfaceTable.containsKey(interfaceType)) {
					ClassPathedClassDef interfaceDef = ClassPath
							.loadClassDef(interfaceType);
					if (interfaceDef == null) {
						throw new ClassNotFoundException(String.format(
								"Could not find interface %s", interfaceType));
					}
					interfaceTable.put(interfaceType, interfaceDef);

					if (interfaceDef.interfaceTable != null) {
						for (ClassPathedClassDef superInterface : interfaceDef.interfaceTable
								.values()) {
							if (!interfaceTable
									.containsKey(superInterface.classType)) {
								interfaceTable.put(superInterface.classType,
										superInterface);
							}
						}
					}
				}
			}

			return interfaceTable;
		}

		private String[] loadVtable(TempClassInfo classInfo) {
			// TODO: it might be useful to keep track of which class's
			// implementation is used for each virtual method. In other words,
			// associate the implementing class type with each vtable entry
			List<String> virtualMethodList = new LinkedList<String>();
			// use a temp hash table, so that we can construct the final lookup
			// with an appropriate
			// capacity, based on the number of virtual methods
			HashMap<String, Integer> tempVirtualMethodLookup = new HashMap<String, Integer>();

			// copy the virtual methods from the superclass
			int methodIndex = 0;
			if (superclass != null) {
				for (String method : superclass.vtable) {
					virtualMethodList.add(method);
					tempVirtualMethodLookup.put(method, methodIndex++);
				}

				assert superclass.instanceFields != null;
			}

			// iterate over the virtual methods in the current class, and only
			// add them when we don't already have the
			// method (i.e. if it was implemented by the superclass)
			if (!this.isInterface) {
				if (classInfo.virtualMethods != null) {
					for (String virtualMethod : classInfo.virtualMethods) {
						if (tempVirtualMethodLookup.get(virtualMethod) == null) {
							virtualMethodList.add(virtualMethod);
							tempVirtualMethodLookup.put(virtualMethod,
									methodIndex++);
						}
					}
				}

				if (interfaceTable != null) {
					for (ClassPathedClassDef interfaceDef : interfaceTable
							.values()) {
						if (interfaceDef.virtualMethods == null) {
							continue;
						}

						for (String virtualMethod : interfaceDef.virtualMethods) {
							if (tempVirtualMethodLookup.get(virtualMethod) == null) {
								virtualMethodList.add(virtualMethod);
								tempVirtualMethodLookup.put(virtualMethod,
										methodIndex++);
							}
						}
					}
				}
			}

			String[] vtable = new String[virtualMethodList.size()];
			for (int i = 0; i < virtualMethodList.size(); i++) {
				vtable[i] = virtualMethodList.get(i);
			}

			return vtable;
		}

		private int getNextFieldOffset() {
			if (instanceFields == null || instanceFields.size() == 0) {
				return 8;
			}

			int lastItemIndex = instanceFields.size() - 1;
			int fieldOffset = instanceFields.keyAt(lastItemIndex);
			FieldDef lastField = instanceFields.valueAt(lastItemIndex);

			switch (lastField.type.charAt(0)) {
			case 'J':
			case 'D':
				return fieldOffset + 8;
			default:
				return fieldOffset + 4;
			}
		}

		private SparseArray<FieldDef> loadFields(TempClassInfo classInfo) {
			// This is a bit of an "involved" operation. We need to follow the
			// same algorithm that dalvik uses to
			// arrange fields, so that we end up with the same field offsets
			// (which is needed for deodexing).
			// See mydroid/dalvik/vm/oo/Class.c - computeFieldOffsets()

			final byte REFERENCE = 0;
			final byte WIDE = 1;
			final byte OTHER = 2;

			FieldDef[] fields = null;
			// the "type" for each field in fields. 0=reference,1=wide,2=other
			byte[] fieldTypes = null;

			if (classInfo.instanceFields != null) {
				fields = new FieldDef[classInfo.instanceFields.length];
				fieldTypes = new byte[fields.length];

				for (int i = 0; i < fields.length; i++) {
					String[] fieldInfo = classInfo.instanceFields[i];

					String fieldName = fieldInfo[0];
					String fieldType = fieldInfo[1];

					fieldTypes[i] = getFieldType(fieldType);
					fields[i] = new FieldDef(classInfo.classType, fieldName,
							fieldType);
				}
			}

			if (fields == null) {
				fields = new FieldDef[0];
				fieldTypes = new byte[0];
			}

			// The first operation is to move all of the reference fields to the
			// front. To do this, find the first
			// non-reference field, then find the last reference field, swap
			// them and repeat
			int back = fields.length - 1;
			int front;
			for (front = 0; front < fields.length; front++) {
				if (fieldTypes[front] != REFERENCE) {
					while (back > front) {
						if (fieldTypes[back] == REFERENCE) {
							swap(fieldTypes, fields, front, back--);
							break;
						}
						back--;
					}
				}

				if (fieldTypes[front] != REFERENCE) {
					break;
				}
			}

			int startFieldOffset = 8;
			if (this.superclass != null) {
				startFieldOffset = this.superclass.getNextFieldOffset();
			}

			int fieldIndexMod;
			if ((startFieldOffset % 8) == 0) {
				fieldIndexMod = 0;
			} else {
				fieldIndexMod = 1;
			}

			// next, we need to group all the wide fields after the reference
			// fields. But the wide fields have to be
			// 8-byte aligned. If we're on an odd field index, we need to insert
			// a 32-bit field. If the next field
			// is already a 32-bit field, use that. Otherwise, find the first
			// 32-bit field from the end and swap it in.
			// If there are no 32-bit fields, do nothing for now. We'll add
			// padding when calculating the field offsets
			if (front < fields.length && (front % 2) != fieldIndexMod) {
				if (fieldTypes[front] == WIDE) {
					// we need to swap in a 32-bit field, so the wide fields
					// will be correctly aligned
					back = fields.length - 1;
					while (back > front) {
						if (fieldTypes[back] == OTHER) {
							swap(fieldTypes, fields, front++, back);
							break;
						}
						back--;
					}
				} else {
					// there's already a 32-bit field here that we can use
					front++;
				}
			}

			// do the swap thing for wide fields
			back = fields.length - 1;
			for (; front < fields.length; front++) {
				if (fieldTypes[front] != WIDE) {
					while (back > front) {
						if (fieldTypes[back] == WIDE) {
							swap(fieldTypes, fields, front, back--);
							break;
						}
						back--;
					}
				}

				if (fieldTypes[front] != WIDE) {
					break;
				}
			}

			int superFieldCount = 0;
			if (superclass != null) {
				superFieldCount = superclass.instanceFields.size();
			}

			// now the fields are in the correct order. Add them to the
			// SparseArray and lookup, and calculate the offsets
			int totalFieldCount = superFieldCount + fields.length;
			SparseArray<FieldDef> instanceFields = new SparseArray<FieldDef>(
					totalFieldCount);

			int fieldOffset;

			if (superclass != null && superFieldCount > 0) {

				// FIXME : what about field modifiers?
				for (int i = 0; i < superFieldCount; i++) {
					instanceFields.append(superclass.instanceFields.keyAt(i),
							superclass.instanceFields.valueAt(i));
				}

				fieldOffset = instanceFields.keyAt(superFieldCount - 1);

				FieldDef lastSuperField = superclass.instanceFields
						.valueAt(superFieldCount - 1);
				char fieldType = lastSuperField.type.charAt(0);
				if (fieldType == 'J' || fieldType == 'D') {
					fieldOffset += 8;
				} else {
					fieldOffset += 4;
				}
			} else {
				// the field values start at 8 bytes into the DataObject dalvik
				// structure
				fieldOffset = 8;
			}

			boolean gotDouble = false;
			for (int i = 0; i < fields.length; i++) {
				FieldDef field = fields[i];

				// add padding to align the wide fields, if needed
				if (fieldTypes[i] == WIDE && !gotDouble) {
					if (!gotDouble) {
						if (fieldOffset % 8 != 0) {
							assert fieldOffset % 8 == 4;
							fieldOffset += 4;
						}
						gotDouble = true;
					}
				}

				instanceFields.append(fieldOffset, field);
				if (fieldTypes[i] == WIDE) {
					fieldOffset += 8;
				} else {
					fieldOffset += 4;
				}
			}
			return instanceFields;
		}

		private byte getFieldType(String fieldType) {
			switch (fieldType.charAt(0)) {
			case '[':
			case 'L':
				return 0; // REFERENCE
			case 'J':
			case 'D':
				return 1; // WIDE
			default:
				return 2; // OTHER
			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof ClassPathedClassDef))
				return false;

			ClassPathedClassDef classDef = (ClassPathedClassDef) o;

			return classType.equals(classDef.classType);
		}

		@Override
		public int hashCode() {
			return classType.hashCode();
		}

		@Override
		public int compareTo(ClassPathedClassDef classDef) {
			return classType.compareTo(classDef.classType);
		}
	}

	/**
	 * In some cases, classes can reference another class (i.e. a superclass or
	 * an interface) that is in a *later* boot class path entry. So we load all
	 * classes from all boot class path entries before starting to process them
	 */
	private static class TempClassInfo {

		public final String dexFilePath;
		public final String classType;
		public final boolean isInterface;
		public final String superclassType;
		public final String[] interfaces;
		public final boolean[] staticMethods;
		public final String[] directMethods;
		public final String[] virtualMethods;
		public final String[][] instanceFields;

		public TempClassInfo(ClassDefItem classDefItem) {
			this.dexFilePath = null;

			classType = classDefItem.getClassType().getTypeDescriptor(); // TODO
																			// add
																			// some
																			// logging
																			// output
																			// to
																			// show
																			// what
																			// class
																			// is
																			// being
																			// cached.

			isInterface = (classDefItem.getAccessFlags() & AccessFlags.INTERFACE
					.getValue()) != 0;

			TypeIdItem superclassType = classDefItem.getSuperclass();
			if (superclassType == null) {
				this.superclassType = null;
			} else {
				this.superclassType = superclassType.getTypeDescriptor();
			}

			interfaces = loadInterfaces(classDefItem);

			ClassDataItem classDataItem = classDefItem.getClassData();
			if (classDataItem != null) {
				boolean[][] _staticMethods = new boolean[1][];
				directMethods = loadDirectMethods(classDataItem, _staticMethods); // TODO
																					// track
																					// methods.
																					// Constructors
																					// begin
																					// with
																					// "<init>"
																					// (is
																					// that
																					// the
																					// complete
																					// pattern?)
				staticMethods = _staticMethods[0]; // TODO use modifiers instead
													// of this array.
				virtualMethods = loadVirtualMethods(classDataItem);
				instanceFields = loadInstanceFields(classDataItem); // TODO what
																	// about
																	// static
																	// fields?
			} else {
				staticMethods = null;
				directMethods = null;
				virtualMethods = null;
				instanceFields = null;
			}
		}

		private String[] loadInterfaces(ClassDefItem classDefItem) {
			TypeListItem typeList = classDefItem.getInterfaces();
			if (typeList != null) {
				List<TypeIdItem> types = typeList.getTypes();
				if (types != null && types.size() > 0) {
					String[] interfaces = new String[types.size()];
					for (int i = 0; i < interfaces.length; i++) {
						interfaces[i] = types.get(i).getTypeDescriptor();
					}
					return interfaces;
				}
			}
			return null;
		}

		private String[] loadDirectMethods(ClassDataItem classDataItem,
				boolean[][] _staticMethods) {
			EncodedMethod[] encodedMethods = classDataItem.getDirectMethods();

			if (encodedMethods != null && encodedMethods.length > 0) {
				boolean[] staticMethods = new boolean[encodedMethods.length];
				String[] directMethods = new String[encodedMethods.length];
				for (int i = 0; i < encodedMethods.length; i++) {
					EncodedMethod encodedMethod = encodedMethods[i];

					if ((encodedMethod.accessFlags & AccessFlags.STATIC
							.getValue()) != 0) {
						staticMethods[i] = true;
					}
					directMethods[i] = encodedMethods[i].method
							.getVirtualMethodString();
				}
				_staticMethods[0] = staticMethods;
				return directMethods;
			}
			return null;
		}

		private String[] loadVirtualMethods(ClassDataItem classDataItem) {
			EncodedMethod[] encodedMethods = classDataItem.getVirtualMethods();
			if (encodedMethods != null && encodedMethods.length > 0) {
				String[] virtualMethods = new String[encodedMethods.length];
				for (int i = 0; i < encodedMethods.length; i++) {
					virtualMethods[i] = encodedMethods[i].method
							.getVirtualMethodString();
				}
				return virtualMethods;
			}
			return null;
		}

		private String[][] loadInstanceFields(ClassDataItem classDataItem) {
			EncodedField[] encodedFields = classDataItem.getInstanceFields();
			if (encodedFields != null && encodedFields.length > 0) {
				String[][] instanceFields = new String[encodedFields.length][2];
				for (int i = 0; i < encodedFields.length; i++) {
					EncodedField encodedField = encodedFields[i];
					instanceFields[i][0] = encodedField.field.getFieldName()
							.getStringValue();
					instanceFields[i][1] = encodedField.field.getFieldType()
							.getTypeDescriptor();
				}
				return instanceFields;
			}
			return null;
		}
	}
}
