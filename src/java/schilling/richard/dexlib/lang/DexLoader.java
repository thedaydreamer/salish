package schilling.richard.dexlib.lang;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import schilling.richard.dalvik.vm.oo.Clazz;
import schilling.richard.dalvik.vm.oo.ObjectClass;
import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;
import schilling.richard.dexlib.Code.Analysis.ValidationException;
import schilling.richard.dexlib.io.DexFile;
import schilling.richard.dexlib.io.DexFileFactory;
import schilling.richard.dexlib.io.DexStream;
import schilling.richard.dexlib.io.deserialize.ClassDataItem;
import schilling.richard.dexlib.io.deserialize.ClassDataItem.EncodedField;
import schilling.richard.dexlib.io.deserialize.ClassDataItem.EncodedMethod;
import schilling.richard.dexlib.io.deserialize.ClassDefItem;
import schilling.richard.dexlib.io.deserialize.TypeIdItem;
import schilling.richard.dexlib.io.deserialize.TypeListItem;
import schilling.richard.dexlib.io.util.AccessFlags;
import android.util.Log;

/**
 * This class operates in a similar way to a ClassLoader object. But it loads
 * class definitions from dex files. Contains singleton items for the entire
 * application.
 * 
 * @author rschilling
 * 
 */
public class DexLoader {

	public static final String LOG_TAG = "Finnr.DexLoader";

	/**
	 * Classes are loaded into this list first, and then lazy loaded into
	 * loadedClasses as needed. Contains only classes from the user's .dex file.
	 */
	private Hashtable<String, ClassDefItem> rawClassDefinitions = new Hashtable<String, ClassDefItem>();

	/**
	 * Class definitions and interfaces that have been loaded into memory. These
	 * are always ObjectClass classes.
	 */
	private Hashtable<String, ObjectClass> loadedClasses = new Hashtable<String, ObjectClass>();

	/**
	 * The input stream that is processed to load information.
	 */
	private DexStream inputStream;

	/**
	 * The dex file that is created from inputStream
	 */
	private DexFile dex;

	public DexLoader(DexStream is) {
		inputStream = is;
		FinnrApp.getApp().setDexLoader(this);
	}

	public void checkState() {
		if (inputStream == null)
			throw new IllegalStateException(
					"init(DexStream) must be called first");

	}

	public DexFile getDexFile() throws IOException {
		if (true)
			throw new UnsupportedOperationException(
					"this method is not currently used.");
		checkState();
		if (dex == null) {
			Log.i(LOG_TAG, "loading DEX input stream");
			dex = DexFileFactory.loadFromStream(inputStream);

			for (ClassDefItem classDefItem : dex.ClassDefsSection.getItems()) {

				rawClassDefinitions.put(classDefItem.getClassType()
						.getTypeDescriptor(), classDefItem);
			}

			/*
			 * capture class def items from the DEX file only. No processing is
			 * done to circular loading conditions
			 */
			for (ClassDefItem tClassInfo : rawClassDefinitions.values()) {

				// this class should not be a platform class.
				if (ClassLoaderUtil.isFoundOnPlatform(tClassInfo.getClassType()
						.getTypeDescriptor()))
					throw new IllegalArgumentException(
							"Class "
									+ tClassInfo.getClassType()
											.getTypeDescriptor()
									+ " was found in the dex file was also found on the platform.");

				// ObjectClass cDef = ClassDefFactory.createClassDef(new
				// DexModel(), tClassInfo.getClassType().getTypeDescriptor());

				// loadedClasses.put(tClassInfo.getClassType().getTypeDescriptor(),
				// cDef);
				Log.i(LOG_TAG, "Cached object "
						+ tClassInfo.getClassType().getTypeDescriptor()
						+ " from binary DEX file.");

			}

			/*
			 * Copy values to an array so that the underlying Hashtable won't be
			 * affected if classes are loaded into it. The underlying Hashtable
			 * may be modified when a platform class is loaded.
			 */

			/*
			 * ObjectClass[] valArray = loadedClasses.values().toArray( new
			 * ObjectClass[0]);
			 * 
			 * for (ObjectClass oClassDef : valArray) {
			 * 
			 * oClassDef.resolveSuperclass(); // calls this.loadClass if
			 * necessary. oClassDef.getDirectMethods();
			 * oClassDef.getVirtualMethods(); oClassDef.getInterfaces(true);
			 * oClassDef.getFields(); }
			 */

		} else {
			Log.i(LOG_TAG, "using raw cached DEX data.");
		}
		return dex;
	}

	/**
	 * This method checks if the given class has been loaded yet. If it has, it
	 * returns the loaded Clazz. If not, then it looks up the TempClassItem for
	 * the given class and (possibly recursively) loads the ClassDef.
	 * 
	 * @param classType
	 *            the class to load.
	 * 
	 * @return the existing or newly loaded ClassDef object for the given class,
	 *         or null if the class cannot be found
	 * 
	 * @throws ValidationException
	 * @throws ClassNotFoundException
	 */
	public Clazz loadClass(String classType) throws ClassNotFoundException,
			ValidationException {

		if (true)
			throw new UnsupportedOperationException("not used in this version");
		checkState();

		// Look to see if it's already loaded - .
		Clazz result = loadedClasses.get(classType);

		if (result != null)
			return result;

		/*
		 * only classes in the DEX are loaded at first. Platform and array class
		 * loading will fall through to the next section.
		 */

		// create a new definition and cache it.
		/*
		 * result = ClassDefFactory.createClassDef(classType); if (result
		 * instanceof ObjectClass) loadedClasses.put(classType, (ObjectClass)
		 * result);
		 */
		return result;
	}

	public Clazz getClassDef(TypeIdItem classType) throws ValidationException,
			ClassNotFoundException {
		return loadClass(classType.getTypeDescriptor());
	}

	// TODO: remove TempClassInfo definition.
	/**
	 * Contains a partial definition of a ClassDefItem so that it can be easily
	 * manipulated.
	 * <p>
	 * In some cases, classes can reference another class (i.e. a superclass or
	 * an interface) that is in a *later* boot class path entry. So we load all
	 * classes from all boot class path entries before starting to process them
	 */
	public static class TempClassInfo {

		/**
		 * The class definition item that was used to create this object.
		 */
		public final ClassDefItem classDef;
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

			if (classDefItem == null)
				throw new IllegalArgumentException(
						"classDefItem must not be null.");

			classDef = classDefItem;
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
