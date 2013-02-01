package schilling.richard.dalvik.vm.oo;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;

/**
 * Utility class to convert a DexBuffer into a DexModel.
 * 
 * @author rschilling
 */
public class DexBufferInput {

	public static final String LOG_TAG = "Finnr.DexBufferInput";

	/**
	 * The buffer to read data from.
	 */
	private final DexBuffer buffer;

	/**
	 * The model that data is read into.
	 */
	private final DexModel model;

	public DexBufferInput(DexBuffer input) {
		this(input, null);
	}

	public DexBufferInput(DexBuffer input, DexModel appendTo) {

		this.buffer = input;
		this.model = appendTo == null ? new DexModel() : appendTo;
	}

	public DexModel readModel() throws VerifyException {

		// TODO create and use a DexBuffer visitor to accomplish this.

		readStrings();
		readTypes();
		readPrototypes();

		// import class definitions before fields and methods.
		readClassDefinitions();
		readFields();
		readMethods();

		// import class data after class definitions, fields and methods.
		readClassData();

		return model;
	}

	/**
	 * Adds class data from the specified buffer to the model. All of the
	 * supporting object must already be added to this model.
	 * 
	 * @param buffer
	 *            the buffer to read class data from.
	 * @throws VerifyException
	 */
	public DexModel readClassData() throws VerifyException {
		Iterable<ClassDef> classDefs = buffer.classDefs();
		List<String> types = buffer.typeNames();

		// Add all classes in the file to the class table.
		for (ClassDef cDef : classDefs) {
			String signature = types.get(cDef.getTypeIndex());
			Clazz tableClazz = model.lookupClassByDescriptor(signature);

			if (tableClazz == null)
				throw new IllegalArgumentException(
						"class "
								+ signature
								+ " in the DexBuffer is not in the class pool.  Did you call addClassDefinitionsFrom yet?");

			StringBuilder status = new StringBuilder();
			status.append("import of class data for class ").append(signature)
					.append(" ");

			/* class_data_off */
			int classDataOff = cDef.getClassDataOffset();
			if (classDataOff > 0) {

				ClassData cData = buffer.readClassData(cDef);
				if (!(tableClazz instanceof ObjectClass))
					throw new IllegalStateException(
							"Class data is being associated with a non-object class in the class table.  The table is corrupt.");

				ObjectClass clazz = (ObjectClass) tableClazz;
				clazz.setData(buffer, cData);

				status.append(" successful.");
				status.append(clazz.instanceFields().size()).append(
						" instance fields;");
				status.append(clazz.staticFields().size()).append(
						" static fields;");
				status.append(clazz.directMethods().size()).append(
						" direct methods;");
				status.append(clazz.virtualMethods().size()).append(
						" virtual methods");

				/* sanity check */
				for (Field f : clazz.instanceFields()) {
					if (f.getDefiningClass() != clazz)
						throw new IllegalStateException(
								"parent class to field did not get set properly.");
				}

				for (Field f : clazz.staticFields()) {
					if (f.getDefiningClass() != clazz)
						throw new IllegalStateException(
								"parent class to field did not get set properly.");
				}

				for (Method m : clazz.directMethods()) {
					if (m.getDeclaringClass() != clazz)
						throw new IllegalStateException(
								"parent class to meghod did not get set properly.");
				}

				for (Method m : clazz.virtualMethods()) {
					if (m.getDeclaringClass() != clazz)
						throw new IllegalStateException(
								"parent class to meghod did not get set properly.");
				}

			} else {
				status.append(" skipped (no data).");
			}
			Log.i(LOG_TAG, status.toString());

			/* static_values_off */
			DexBuffer.Section sec = buffer.open(cDef.getStaticValuesOffset());
			tableClazz.setStaticInitialValues(sec.readEncodedArray());

		}

		return model;

	}

	public Method lookup(ClassData.Method cdMethod, MethodId methodId)
			throws VerifyException {

		List<String> types = buffer.typeNames();
		List<ProtoId> protoIds = buffer.protoIds();

		/*
		 * construct relevant information so that the proper method definition
		 * can be found.
		 */

		// declaring class
		String declaringClassSignature = types.get(methodId
				.getDeclaringClassIndex());
		Clazz declaringClass = model
				.lookupClassByDescriptor(declaringClassSignature);

		if (declaringClass == null)
			throw new IllegalStateException(
					"no parent class defined.  Did you call addClassDefinitionsFrom(buffer) ?");

		// extract additional data prototype
		ProtoId protoId = protoIds.get(methodId.getProtoIndex());
		Code code = buffer.readCode(cdMethod);

		MethodPrototype methodProto = new MethodPrototype(model, protoId);

		Method result = new Method(model, declaringClass, methodProto,
				methodId, cdMethod, code);

		int pos = model.methodPool().indexOf(result);

		if (pos == -1)
			throw new IllegalArgumentException(
					"method id has no corresponding Method in the pool");

		result = model.methodPool().get(pos);

		return result;

	}

	public Field lookup(ClassData.Field cdField, FieldId fieldId)
			throws VerifyException {

		List<String> types = buffer.typeNames();

		String fieldParentSignature = types.get(fieldId
				.getDeclaringClassIndex());
		Clazz fieldParent = model.lookupClassByDescriptor(fieldParentSignature);

		Field result = new Field(model, fieldParent, cdField, fieldId,
				cdField.getFieldIndex());
		int pos = model.fieldPool().indexOf(result);

		if (pos == -1) {
			throw new IllegalStateException(
					"field id has no corresponding field in the pool");
			// assume the field is legitimate.
			// model.addToFieldPool(result);
			// pos = model.fieldPool().indexOf(result);

		}
		result = model.fieldPool().get(pos);

		return result;
	}

	public MethodPrototype lookup(ProtoId protoId) {

		MethodPrototype result = new MethodPrototype(model, protoId);
		// get the index of the object in the prototype list so there
		// are no duplicates in memory.
		int pos = model.prototypePool().indexOf(result);
		if (pos == -1) {
			throw new IllegalStateException(
					"the prototype id does not exist in the prototype pool");
			// model.addToPrototypePool(result);
			// pos = model.prototypePool().indexOf(result);
		}

		result = model.prototypePool().get(pos);

		return result;

	}

	/**
	 * Read method definitions from the buffer into the model.
	 * 
	 * @return
	 * @throws VerifyException
	 */
	public DexModel readMethods() throws VerifyException {
		List<MethodId> methods = buffer.methodIds();
		List<String> strings = buffer.strings();
		List<ProtoId> protoIds = buffer.protoIds();
		List<String> types = buffer.typeNames();

		if (methods != null) {
			for (MethodId method : methods) {

				String declaringClassSignature = types.get(method
						.getDeclaringClassIndex());

				Clazz declaringClass = model
						.lookupClassByDescriptor(declaringClassSignature);

				if (declaringClass == null)
					throw new IllegalStateException("lookup class failed.");

				ProtoId protoId = protoIds.get(method.getProtoIndex());
				MethodPrototype mPrototype = lookup(protoId);

				Method m = new Method(model, declaringClass, mPrototype,
						method, null, null);

				if (model.addToMethodPool(m)) {
					Log.i(LOG_TAG, "import of method " + m.toString()
							+ " successful");
				} else {
					Log.i(LOG_TAG, "import of method " + m.toString()
							+ " skipped (duplicate)");

				}

			}
		}

		return model;
	}

	/**
	 * Read all fields from the buffer into the model. Not all fields are
	 * expected to have a parent ClassData item. Fields can be referenced naked
	 * - that is associated with a platform class def.
	 * 
	 * @return the model that was added to.
	 * @throws VerifyException
	 */
	public DexModel readFields() throws VerifyException {
		List<FieldId> fields = buffer.fieldIds();
		List<String> types = buffer.typeNames();

		if (fields != null) {
			for (FieldId fId : fields) {

				// get the relevant class from the class table
				String parentSignature = types
						.get(fId.getDeclaringClassIndex());

				Clazz parent = model.lookupClassByDescriptor(parentSignature);
				if (parent == null)
					throw new IllegalStateException(
							"cannot import a field unless an associated class definition is already created. Did you call addClassDefinitionsFrom(buffer)?");

				ClassData.Field cdField = buffer.getClassDataField(fId);

				Field f = new Field(model, parent, cdField, fId,
						cdField.getFieldIndex());

				if (model.fieldPool().add(f)) {
					Log.i(LOG_TAG, "import of field " + f.toString()
							+ " successful");
				} else {
					Log.i(LOG_TAG, "import of field " + f.toString()
							+ " skipped (duplicate)");

				}

			}

		}

		return model;

	}

	/**
	 * Adds the basic definition from the buffer to the model. Does not import
	 * any of the methods, fields, or static members (child definitions).
	 * 
	 * @param buffer
	 *            the buffer to read classes from.
	 * @throws VerifyException
	 */
	public DexModel readClassDefinitions() throws VerifyException {

		Iterable<ClassDef> classDefs = buffer.classDefs();
		List<String> types = buffer.typeNames();
		List<String> strings = buffer.strings();

		// Add all classes in the file to the class table.
		for (ClassDef cDef : classDefs) {

			/* class_idx - type id */
			String signature = types.get(cDef.getTypeIndex()).intern();

			if (model.isInClassPool(signature)) {
				Log.i(LOG_TAG, "import of class " + signature
						+ " skipped (duplicate).");
				continue;
			}

			// TODO handle array classes, primitive classes and platform
			// classes.

			ObjectClass clazz = new ObjectClass(model, cDef,
					cDef.getTypeIndex()); // FIXME correct the call to
											// .getTypeIndex. I'm tired for now.

			/* interfaces_off - also handles the case where interfaces_off is 0 */
			for (short idx : cDef.getInterfaces()) {

				clazz.addInterface((ObjectClass) model
						.lookupClassByDescriptor(types.get(idx)));
			}

			/* annotations_off */
			int annOff = cDef.getAnnotationsOffset();
			if (annOff > 0)
				readAnnotationsDirectoryItem(clazz, annOff);

			model.addClass(clazz);
			Log.i(LOG_TAG, "import of class " + signature + " successful.");
		}

		return model;
	}

	public DexModel readPrototypes() {
		List<ProtoId> protoIds = buffer.protoIds();

		if (protoIds != null) {
			for (ProtoId pId : protoIds) {
				MethodPrototype mPrototype = new MethodPrototype(model, pId);
				if (model.addToPrototypePool(mPrototype)) {
					Log.i(LOG_TAG,
							"import of prototype " + mPrototype.toString()
									+ " successful");
				} else {
					Log.i(LOG_TAG,
							"import of prototype " + mPrototype.toString()
									+ " skipped (duplicate)");
				}

			}
		}

		return model;

	}

	/**
	 * Adds the types from in a DexBuffer to thetype pook in this model. Does
	 * not check to see if each type string is in the string pool first. Each
	 * type string must have been already added to the string pool with a call
	 * to addToStringPool(). Otherwise an exception will be thrown.
	 * 
	 * @param buffer
	 */
	public DexModel readTypes() {
		model.addToTypePool(buffer.typeNames());
		return model;
	}

	/**
	 * Adds the strings found in a DexBuffer to the string pool in this model.
	 * 
	 * @param buffer
	 *            the buffer to read strings from.
	 */
	public DexModel readStrings() {
		model.addToStringPool(buffer.strings());
		return model;
	}

	/**
	 * Reads the annotations_directory_item for a given class. The specified
	 * section is expected to be pointing at the class'
	 * annotations_directory_item.
	 * 
	 * @param section
	 *            the section to read an annotations directory from.
	 */
	private DexModel readAnnotationsDirectoryItem(Clazz clazz, int offset) {
		// TODO check for null params.

		List<FieldId> fieldIds = buffer.fieldIds();
		List<MethodId> methodIds = buffer.methodIds();

		DexBuffer.Section section = buffer.open(offset);

		if (section == null)
			throw new IllegalArgumentException("section cannot be bull");

		int classAnnotationsOffset = section.readInt();
		int fieldSz = section.readInt();
		int methodSz = section.readInt();
		int paramSz = section.readInt();

		/*
		 * if (classAnnotationsOffset > 0) // get annotations that are applied
		 * directly to the class. clazz.annotations =
		 * convert_annotation_set_item(classAnnotationsOffset);
		 * 
		 * for (int i = 0; i < fieldSz; i++) {
		 * 
		 * int fieldIdx = section.readInt(); FieldId fid =
		 * fieldIds.get(fieldIdx); Field field = lookup(fid);
		 * 
		 * int annotationOffset = section.readInt(); List<Annotation>
		 * fieldAnnotations = convert_annotation_set_item(annotationOffset);
		 * field.setAnnotations(fieldAnnotations);
		 * 
		 * }
		 * 
		 * for (int i = 0; i < methodSz; i++) {
		 * 
		 * int methodIdx = section.readInt(); MethodId mid =
		 * methodIds.get(methodIdx);
		 * 
		 * Method method = lookup(mid);
		 * 
		 * int annotationOffset = section.readInt(); List<Annotation>
		 * methodAnnotations = convert_annotation_set_item(annotationOffset);
		 * 
		 * method.setAnnotations(methodAnnotations);
		 * 
		 * }
		 * 
		 * for (int i = 0; i < paramSz; i++) { int methodIdx =
		 * section.readInt(); MethodId mid = methodIds.get(methodIdx);
		 * 
		 * Method method = lookup(mid);
		 * 
		 * int annotationSetRefOffset = section.readInt();
		 * 
		 * List<List<Annotation>> parameterAnnotations =
		 * convert_annotation_set_ref_list( buffer, annotationSetRefOffset);
		 * 
		 * method.setParameterAnnotations(parameterAnnotations); }
		 */

		return model;

	}

	public List<List<Annotation>> convert_annotation_set_ref_list(
			DexBuffer buffer, int offset) {
		List<List<Annotation>> result = new LinkedList<List<Annotation>>();

		DexBuffer.Section section = buffer.open(offset);

		int sz = section.readInt();

		for (int i = 0; i < sz; i++) {
			int setPosition = section.readInt();
			if (setPosition > 0) {
				List<Annotation> annList = convert_annotation_set_item(setPosition);
				result.add(annList);
			}
		}

		return result;

	}

	/**
	 * Reads an annotation_set_item
	 * 
	 * @param section
	 */
	private List<Annotation> convert_annotation_set_item(int offset) {
		List<Annotation> result = new LinkedList<Annotation>();

		DexBuffer.Section section = buffer.open(offset);
		List<String> types = buffer.typeNames();
		List<String> strings = buffer.strings();
		List<ClassDef> classDefs = buffer.classDefList();
		List<com.android.dx.io.Annotation> annotations = section
				.readAnnotationSet();

		for (com.android.dx.io.Annotation annotation : annotations) {
			ClassDef cDef = buffer.getDef(types.get(annotation.getTypeIndex()));
			// FIXME uncomment
			/*
			 * Annotation convertedAnn = new Annotation(model, cDef);
			 * convertedAnn.visibility = annotation.getVisibility(); int[] names
			 * = annotation.getNames(); EncodedValue[] values =
			 * annotation.getValues();
			 * 
			 * for (int i = 0; i < names.length; i++) { String name =
			 * strings.get(names[i]); EncodedValue value = values[i];
			 * convertedAnn.annotationValues.put(name, value); }
			 * 
			 * 
			 * result.add(convertedAnn);
			 */

		}

		return result;
	}

}
