package schilling.richard.dalvik.vm.oo;

import com.android.dx.io.ClassData;
import com.android.dx.io.FieldId;

/**
 * An in memory non-buffer read verson of ClassData.Field
 * 
 * @author rschilling
 * 
 */
public class Field implements Comparable<Field> {

	private AnnotationSet annotations;

	/**
	 * The name of the class that implements this field.
	 */
	private final Clazz parent;

	private final DexModel model;

	private final ClassData.Field fieldData;
	private final FieldId fieldId;

	/**
	 * The type of field this is.
	 */
	private String fieldType;

	/**
	 * The name of the field.
	 */
	private String fieldName;
	private final int fieldIdx;

	/**
	 * Creates a field and binds it to a parent class. Once this constructor is
	 * called, there is now an association beteen DexBuffer, Clazz, and Field
	 * objects.
	 * 
	 * @param definingClass
	 *            the class that defines this field.
	 * @param name
	 *            the name of the class.
	 * @param type
	 *            the type signature of this class.
	 */

	public Field(DexModel model, Clazz definingClass, ClassData.Field cdField,
			FieldId fieldId, int fieldIndex) {
		if (model == null)
			throw new IllegalArgumentException("model cannot be null");

		if (definingClass == null)
			throw new IllegalArgumentException("defining class cannot be null.");

		if (cdField == null)
			throw new IllegalArgumentException(
					"the class data field definition cannot be null.");

		if (fieldId == null)
			throw new IllegalArgumentException("field id cannot be null");

		this.fieldId = fieldId;
		this.fieldData = cdField;
		this.fieldName = fieldId.getName();
		this.fieldType = fieldId.getType();
		this.parent = definingClass;
		this.fieldIdx = fieldIndex;

		this.model = model;

		if (!model.isInTypePool(fieldType))
			throw new IllegalArgumentException(
					"the field type is not defined in the type pool");

		if (!model.isInStringPool(fieldName))
			throw new IllegalArgumentException(
					"the field name is not defined in the string pool");

	}

	public int getFieldIdx() {
		return fieldIdx;
	}

	/* package */void setAnnotations(AnnotationSet ann) {
		annotations = ann;
	}

	public AnnotationSet getAnnotations() {
		return annotations;
	}

	public Clazz getDefiningClass() {
		return parent;
	}

	public int getAccessFlags() {
		return fieldData.getAccessFlags();
	}

	/**
	 * Compares fields. Does not check accessFlags.
	 * 
	 * @param compareTo
	 * @return
	 */
	@Override
	public boolean equals(Object compareTo) {

		if (compareTo == null)
			return false;

		if (!(compareTo instanceof Field))
			return false;

		Field compareToField = (Field) compareTo;

		if (!parent.equals(compareToField.parent))
			return false;

		if (!fieldName.equals(compareToField.fieldName))
			return false;

		if (!fieldType.equals(compareToField.fieldType))
			return false;

		return true;
	}

	/**
	 * Compares fields without taking into account the parent class.
	 */
	@Override
	public int compareTo(Field another) {
		if (another == null)
			return 1;

		if (parent != another.parent)
			return parent.compareTo(another.parent);

		if (!fieldName.equals(another.fieldName))
			return fieldName.compareTo(another.fieldName);

		if (!fieldType.equals(another.fieldType))
			return fieldType.compareTo(another.fieldType);

		return 0;

	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("name=").append(fieldName).append(" ");
		result.append("type=").append(fieldType);

		return result.toString();

	}

	public String getName() {
		return fieldName;
	}

	public String getType() {
		return fieldType;
	}

	public Clazz getFieldClass() throws VerifyException {
		return model.lookupClassByDescriptor(getType());
	}

}
