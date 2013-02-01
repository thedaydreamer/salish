package schilling.richard.dalvik.vm.oo;

import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BOOLEAN;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BYTE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_CHAR;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_DOUBLE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_FLOAT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_INTEGER;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_LONG;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_SHORT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_VOID;
import static schilling.richard.dalvik.vm.oo.predefined.PredefinedClasses.PREDEFINED_SIGNATURES;
import static schilling.richard.dalvik.vm.oo.predefined.PredefinedClasses.SIG_JAVA_LANG_CLASS;
import static schilling.richard.dalvik.vm.oo.predefined.PredefinedClasses.SIG_JAVA_LANG_OBJECT;
import static schilling.richard.dalvik.vm.oo.predefined.PredefinedClasses.SIG_JAVA_LANG_STRING;
import static schilling.richard.dalvik.vm.oo.predefined.PredefinedClasses.SIG_JAVA_LANG_THROWABLE;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.dex.DexFormat;
import com.android.dx.dex.DexOptions;
import com.android.dx.util.ByteArrayAnnotatedOutput;

/**
 * <p>
 * A DEX file is a linear representation of a tree structure. This class helps
 * work with that structure by converting between the flat/binary form and an
 * in-memory tree form.
 * 
 * <p>
 * This class builds up a DEX representation one element at a time. It supports
 * the ability to read a definition from a DexBuffer, modify the item and then
 * insert it into another DexBuffer.
 * 
 * <p>
 * This also serves as a simplified model of class structure. Most models we see
 * (e.g. DexFile) are more complicated than needed. And, working with a
 * DexBuffer directly forces us to interact with referenced and dereferenced
 * items like strings, types, proto_ids, and method_ids. It's more complicated
 * than it needs to be when you're trying to work with DEX data. Those formats
 * are requirements of the output file, but nothing says we have to stick with
 * that when the data is in memory.
 * 
 * <p>
 * string_ids:
 * 
 * @author rschilling
 */
public class DexModel {

	public static final String LOG_TAG = "Finnr.DexModel";

	private byte[] magic = null;

	/**
	 * Master list of Strings. The string constant pool
	 */
	private Vector<String> stringPool = new Vector<String>();

	/**
	 * The items in the types list that are identifiers. Every item in this list
	 * must also exist in the strings list. A type is just a string.
	 */
	private Vector<String> typePool = new Vector<String>();

	/**
	 * Method prototypes. A method object cannot be created until a prototype is
	 * inserted into this list.
	 */
	private Vector<MethodPrototype> prototypePool = new Vector<MethodPrototype>();

	private Vector<Field> fieldPool = new Vector<Field>();

	private Vector<Method> methodPool = new Vector<Method>();

	private final Hashtable<String, Clazz> classTable = new Hashtable<String, Clazz>();

	public final PrimitiveClassFactory PRIMITIVE_CLASS_FACTORY;

	// TODO add standard class definitions to the class table.

	public DexModel() {
		PRIMITIVE_CLASS_FACTORY = new PrimitiveClassFactory(this);

		for (String sig : PREDEFINED_SIGNATURES) {
			stringPool.add(sig);
			typePool.add(sig);
		}

		classTable.put(SIG_JAVA_LANG_OBJECT, new PlatformClass(this,
				SIG_JAVA_LANG_OBJECT, Object.class));
		classTable.put(SIG_JAVA_LANG_THROWABLE, new PlatformClass(this,
				SIG_JAVA_LANG_THROWABLE, Throwable.class));
		classTable.put(SIG_JAVA_LANG_CLASS, new PlatformClass(this,
				SIG_JAVA_LANG_CLASS, Class.class));
		classTable.put(SIG_JAVA_LANG_STRING, new PlatformClass(this,
				SIG_JAVA_LANG_STRING, String.class));
		classTable.put(SIG_BOOLEAN, PRIMITIVE_CLASS_FACTORY.getBooleanClass());
		classTable.put(SIG_BYTE, PRIMITIVE_CLASS_FACTORY.getByteClass());
		classTable.put(SIG_CHAR, PRIMITIVE_CLASS_FACTORY.getCharClass());
		classTable.put(SIG_DOUBLE, PRIMITIVE_CLASS_FACTORY.getDoubleClass());
		classTable.put(SIG_FLOAT, PRIMITIVE_CLASS_FACTORY.getFloatClass());
		classTable.put(SIG_INTEGER, PRIMITIVE_CLASS_FACTORY.getIntClass());
		classTable.put(SIG_LONG, PRIMITIVE_CLASS_FACTORY.getLongClass());
		classTable.put(SIG_SHORT, PRIMITIVE_CLASS_FACTORY.getShortClass());
		classTable.put(SIG_VOID, PRIMITIVE_CLASS_FACTORY.getVoidClass());

	}

	public void setMagic(byte[] magic) {
		this.magic = magic;
	}

	/**
	 * Returns the string constant pool as an ummutable list, sorted.
	 * 
	 * @return the string pool.
	 */
	public List<String> stringPool() {
		Collections.sort(stringPool);
		return Collections.unmodifiableList(stringPool);
	}

	/**
	 * Returns the number of strings in the string pool
	 * 
	 * @return the string pool count.
	 */
	public int stringPoolCount() {
		return stringPool.size();
	}

	/**
	 * Returns the number of strings in the string pool
	 * 
	 * @return the string pool count.
	 */
	public int typePoolCount() {
		return typePool.size();
	}

	public int protoPoolCount() {
		return prototypePool.size();
	}

	public int fieldPoolCount() {
		return fieldPool.size();
	}

	public int methodPoolCount() {
		return methodPool.size();
	}

	/**
	 * Returns the number of classes in this model that would be dumped to a DEX
	 * file. Only the count of classes defined in this model are included in the
	 * result. Platform classes are not.
	 * 
	 * @return defined classes count.
	 */
	public int classDefPoolCount() {
		return classPool(false).size();
	}

	/**
	 * Returns the type constant pool as an ummutable list, sorted.
	 * 
	 * @return the type pool.
	 */
	public List<String> typePool() {
		Collections.sort(typePool);
		return Collections.unmodifiableList(typePool);
	}

	/**
	 * Returns the prototype constant pool as an ummutable list, sorted.
	 * 
	 * @return the prototype pool.
	 */
	public List<MethodPrototype> prototypePool() {
		Collections.sort(prototypePool);
		return Collections.unmodifiableList(prototypePool);
	}

	/**
	 * Returns the field constant pool as an ummutable list, sorted.
	 * 
	 * @return the field pool.
	 */
	public List<Field> fieldPool() {
		Collections.sort(fieldPool);
		return Collections.unmodifiableList(fieldPool);
	}

	/**
	 * Returns the method constant pool as an ummutable list, sorted.
	 * 
	 * @return the method pool.
	 */
	public List<Method> methodPool() {
		Collections.sort(methodPool);
		return Collections.unmodifiableList(methodPool);
	}

	/**
	 * Adds a method to the method pool if it is not already present.
	 * 
	 * @param m
	 *            the method to add
	 * @return true if the method pool was changed or false otherwise.
	 */
	public boolean addToMethodPool(Method m) {
		if (methodPool.contains(m))
			return false;

		methodPool.add(m);
		return true;

	}

	/**
	 * Adds a field to the field pool if it is not already present.
	 * 
	 * @param m
	 *            the field to add
	 * @return true if the field pool was changed or false otherwise.
	 */
	public boolean addToFieldPool(Field f) {
		if (fieldPool.contains(f))
			return false;

		fieldPool.add(f);
		return true;

	}

	/**
	 * Adds a prototype to the prototype pool if it is not already present.
	 * 
	 * @param m
	 *            the prototype to add
	 * @return true if the prototype pool was changed or false otherwise.
	 */
	public boolean addToPrototypePool(MethodPrototype p) {
		if (prototypePool.contains(p))
			return false;

		prototypePool.add(p);
		return true;

	}

	public List<Clazz> classPool(boolean includePlatform) {

		List<Clazz> result = new LinkedList<Clazz>();
		for (Enumeration<Clazz> cEnum = classTable.elements(); cEnum
				.hasMoreElements();) {
			Clazz c = cEnum.nextElement();
			if (!includePlatform && (c instanceof PlatformClass))
				continue;
			result.add(c);

		}

		Collections.sort(result);

		return Collections.unmodifiableList(result);

	}

	public byte[] getMagic() {
		if (magic == null) {
			String m = DexFormat.apiToMagic(DexFormat.API_CURRENT);
			magic = new byte[8];
			// Write the magic number.
			for (int i = 0; i < 8; i++) {
				magic[i] = (byte) m.charAt(i);
			}

		}
		return magic;
	}

	public DexOptions getOptions() {

		DexOptions result = new DexOptions();
		result.targetApiLevel = DexFormat.magicToApi(getMagic());

		return result;

	}

	public int getApiLevel() {
		return DexFormat.magicToApi(getMagic());
	}

	/**
	 * Finds the index of the specified string in the string pool.
	 * 
	 * @param s
	 *            the string to look for.
	 * @return the position of the string.
	 * @throws IllegalArgumentException
	 *             if the string is not in the list.
	 */
	private int indexOfString(String s) {
		String s0 = s.intern();
		if (!stringPool.contains(s0))
			throw new IllegalArgumentException("string " + s
					+ " is not part of the string constant pool.");

		return stringPool.indexOf(s0);

	}

	private int indexOfType(String t) {
		String t0 = t.intern();
		if (!typePool.contains(t0))
			throw new IllegalArgumentException("string " + t
					+ " is not part of the master list of strings.");

		return typePool.indexOf(t0);
	}

	public boolean isInStringPool(String s) {
		if (s == null)
			return false;

		String s0 = s.intern();
		return stringPool.contains(s0);

	}

	public boolean isInFieldPool(Field f) {
		if (f == null)
			return false;
		return fieldPool.contains(f);
	}

	public boolean isInTypePool(String t) {
		if (t == null)
			return false;

		String t0 = t.intern();
		return typePool.contains(t0);
	}

	public void addToStringPool(String s) {
		verifyString(s);

		String s0 = s.intern();

		if (stringPool.contains(s0)) {
			Log.i(LOG_TAG, "import of string " + s0 + " skipped (duplicate)");
			return;
		}

		stringPool.add(s0);
		Log.i(LOG_TAG, "import of string " + s0 + " successful");
		Collections.sort(stringPool);
	}

	public void addToStringPool(List<String> strings) {
		for (String s : strings) {
			addToStringPool(s);
		}
	}

	public void addToTypePool(String t) {
		verifyString(t);

		String t0 = t.intern();

		if (!stringPool.contains(t0))
			throw new IllegalArgumentException("type " + t0
					+ " must be added as a string first");

		if (typePool.contains(t0)) {
			Log.i(LOG_TAG, "import of type " + t0 + " skipped (duplicate)");
			return;
		}

		typePool.add(t0);
		Log.i(LOG_TAG, "import of type " + t0 + " successful");
		Collections.sort(typePool);

	}

	public void addToTypePool(List<String> strings) {

		// TODO add a check for a properly formatted type string. See
		// TypeDescriptor section in DEX file doc.
		for (String s : strings) {
			addToTypePool(s);
		}
	}

	private void verifyString(String s) {
		if (s == null)
			throw new IllegalArgumentException("s cannot be null");

		if (s.trim().length() == 0)
			throw new IllegalArgumentException("s cannot be empty");

	}

	/**
	 * 
	 * TODO: define the loading sequence more precisely. It's still not clear
	 * where classes get loaded into the model, and at what point a simple
	 * lookup happens. The steps should be: 1. load all class defs in the Dex
	 * file. 2. Resolve all superclasses. 3. then when a class is needede, look
	 * it up. If it's not in the pool then an exception is thrown since the only
	 * classes that should be looked up are ones that are already defined.
	 * 
	 * TODO: build the class model so that superclasses also contain links to
	 * the classes that extend them (e.g. reverse class structure). This will be
	 * useful for analysis - for example, in finding the common super class for
	 * class definitons. It will be easier to do this from the top down. Or,
	 * perhaps we could just build up a tree structure with class signatures
	 * (Strings) that can be traversed. Perhaps that's the best thing to do.
	 * 
	 * 
	 * Look up a class reference given as a simple string descriptor.
	 * 
	 * <p>
	 * If we can't find it, return a generic substitute when possible.
	 * 
	 * <p>
	 * Retrieve a class from the class table based on the signature. A number of
	 * checks are performed to be certain there are no errors. The function name
	 * is the same as the one in the original C code.
	 * 
	 * <p>
	 * This function also handles the case where the javac compiler occasionally
	 * puts references to nonexistent classes in signatures.
	 * 
	 * <p>
	 * The javac compiler occasionally puts references to nonexistent classes in
	 * signatures. For example, if you have a non-static inner class with no
	 * constructor, the compiler provides a private <init> for you. Constructing
	 * the class requires <init>(parent), but the outer class can't call that
	 * because the method is private. So the compiler generates a package-scope
	 * <init>(parent,bogus) method that just calls the regular <init> (the
	 * "bogus" part being necessary to distinguish the signature of the
	 * synthetic method). Treating the bogus class as an instance of
	 * java.lang.Object allows the verifier to process the class successfully.
	 * 
	 * @param signature
	 *            the signature to lookup
	 * @return the resolved class.
	 */
	public Clazz lookupClassByDescriptor(String pDescriptor)
			throws VerifyException {

		Clazz clazz = classTable.get(pDescriptor);
		if (clazz == null) {
			String logString = String.format(
					"VFY: unable to find class referenced in signature (%s)",
					pDescriptor);
			Log.v(LOG_TAG, logString);

			/*
			 * // TODO: what's the difference between LOGV and LOG_VFY? if
			 * (strchr(pDescriptor, '$') != NULL) {
			 * LOGV("VFY: unable to find class referenced in signature (%s)",
			 * pDescriptor); } else {
			 * LOG_VFY("VFY: unable to find class referenced in signature (%s)",
			 * pDescriptor); }
			 */

			if (pDescriptor.charAt(0) == '[') {
				/* We are looking at an array descriptor. */

				/*
				 * There should never be a problem loading primitive arrays.
				 */
				if (pDescriptor.charAt(1) != 'L'
						&& pDescriptor.charAt(1) != '[') {

					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC,
							String.format(
									"VFY: invalid char in signature in '%s'",
									pDescriptor));

				}

				/*
				 * Try to continue with base array type. This will let us pass
				 * basic stuff (e.g. get array len) that wouldn't fly with an
				 * Object. This is NOT correct if the missing type is a
				 * primitive array, but we should never have a problem loading
				 * those. (I'm not convinced this is correct or even useful.
				 * Just use Object here?)
				 */
				// clazz = dvmFindClassNoInit("[Ljava/lang/Object;",
				// meth->clazz->classLoader);

				clazz = new ArrayClass(this, 1, pDescriptor);
			} else if (pDescriptor.charAt(0) == 'L') {
				if (!ClassLoaderUtil.isFoundOnPlatform(pDescriptor))
					throw new IllegalArgumentException("descriptor "
							+ pDescriptor
							+ " does not specify a platform class");
				/*
				 * We are looking at a non-array reference descriptor; try to
				 * continue with base reference type.
				 */
				clazz = new PlatformClass(this, pDescriptor, Object.class);
			} else {

				/* We are looking at a primitive type. */
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						String.format(
								"VFY: invalid char in signature in '%s'. This is a primitive type placeholder class signature generated by the compiler, which is not allowed.",
								pDescriptor));

			}

			classTable.put(pDescriptor, clazz);

		}

		if (dvmIsPrimitiveClass(clazz)) {
			throw new VerifyException(
					VerifyErrorCause.VERIFY_ERROR_GENERIC,
					String.format(
							"VFY: invalid use of primitive type '%s'.  Retrieval of primitive type is not allowed.",
							pDescriptor));

		}

		return clazz;
	}

	public Clazz lookupClassByTypeIndex(int typeIdx) {
		Collection<Clazz> coll = classTable.values();

		for (Iterator<Clazz> iter = coll.iterator(); iter.hasNext();) {
			Clazz c = iter.next();
			if (c.getTypeIndex() == typeIdx)
				return c;
		}
		return null;
	}

	/* package */void addClass(Clazz c) {
		if (!isInClassPool(c))
			classTable.put(c.getSignature(), c);
	}

	public static boolean dvmIsPrimitiveClass(Clazz c) {
		return c instanceof PrimitiveClass;
	}

	public boolean containsClass(String signature) throws VerifyException {
		return lookupClassByDescriptor(signature) == null ? false : true;
	}

	public boolean isInClassPool(Clazz c) {

		return classTable.containsValue(c);
	}

	public boolean isInClassPool(String specifier) {
		return classTable.containsKey(specifier.intern());
	}

	public void dump(boolean verbose) {

		// string

		StringBuilder builder = new StringBuilder();
		builder.append("---------------- DexModel Dump ----------------\n");
		builder.append("string pool count ").append(stringPool.size())
				.append(" \n");

		if (verbose) {
			int i = 0;
			for (String s : stringPool) {
				builder.append("  ").append(Integer.toString(i)).append(" ");
				builder.append(s).append("\n");
				i++;
			}
		}
		builder.append("\n");
		Log.i(LOG_TAG, builder.toString());

		// type
		builder = new StringBuilder();
		builder.append("type pool count ").append(typePool.size()).append("\n");

		if (verbose) {
			int i = 0;
			for (String s : typePool) {
				builder.append("  ").append(Integer.toString(i)).append(" ");
				builder.append(s).append("\n");
				i++;
			}
		}
		builder.append("\n");
		Log.i(LOG_TAG, builder.toString());

		// proto
		builder = new StringBuilder();
		builder.append("prototype count ").append(prototypePool.size())
				.append("\n");
		if (verbose) {
			int i = 0;
			for (MethodPrototype mp : prototypePool) {
				builder.append("  ").append(Integer.toString(i)).append(" ");
				builder.append(mp.toString()).append("\n");
				i++;
			}
		}
		Log.i(LOG_TAG, builder.toString());

		// field
		builder = new StringBuilder();
		builder.append("field count ").append(fieldPool.size()).append("\n");
		if (verbose) {
			int i = 0;
			for (Field f : fieldPool) {
				builder.append("  ").append(Integer.toString(i)).append(" ");
				builder.append(f.toString()).append("\n");
				i++;
			}
		}

		// method
		builder = new StringBuilder();
		builder.append("method count ").append(fieldPool.size()).append("\n");
		if (verbose) {
			int i = 0;
			for (Method m : methodPool) {
				builder.append("  ").append(Integer.toString(i)).append(" ");
				builder.append(m.toString()).append("\n");
				i++;
			}
		}

		// class
		builder = new StringBuilder();
		builder.append("class count ").append(classTable.size()).append("\n");

		if (verbose) {
			int i = 0;
			for (Enumeration<Clazz> values = classTable.elements(); values
					.hasMoreElements();) {

				builder.append("  ").append(Integer.toString(i)).append(" ");
				builder.append(values.nextElement().toString()).append("\n");
				i++;
			}
		}

		builder.append("\n");

		Log.i(LOG_TAG, builder.toString());

	}

	/**
	 * Generates a sparse array of strings where the index is the byte position
	 * of each string in the file. The first index value is the value returned
	 * from getDataOffset(). Once this item is retrieved, other lists can refer
	 * to this by either index or position, which is what the DEX file will
	 * require.
	 * 
	 * @return a sparse array of strings that appear in the data section.
	 */
	public SparseArray<String> positionStrings(int startingAt) {

		SparseArray<String> result = new SparseArray<String>();

		Collections.sort(stringPool);

		ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput(0);

		for (String s : stringPool) {
			int pos = out.getCursor();

		}

		return result;

	}

	public int mapListCount() {
		return 0;
	}

	/**
	 * Return the number of type lists in the model.
	 * 
	 * @return
	 */
	public int typeListCount() {
		return 0;
	}

	public boolean isInPrototypePool(MethodPrototype proto) {

		if (proto == null)
			throw new IllegalArgumentException("proto cannot be null.");

		return prototypePool.contains(proto);

	}

}
