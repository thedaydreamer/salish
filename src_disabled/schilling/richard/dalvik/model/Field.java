
package schilling.richard.dalvik.model;

import java.util.List;

public class Field implements Comparable<Field> {

    private AnnotationSet annotations;

    /**
     * Access flags for this field.
     * TODO: Make all accessFlags an EnumSet.
     */
    public int accessFlags;

    /**
     * The name of the class that implements this field.
     */
    private final Clazz parent;

    /**
     * The type of field this is.
     */
    private String fieldType;

    /**
     * The name of the field.
     */
    private String fieldName;

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
    public Field(Clazz definingClass, String name, String type) {

        if (definingClass == null)
            throw new IllegalArgumentException("defining class cannot be null.");

        if (name == null)
            throw new IllegalArgumentException("name cannot be null");

        if (name.trim().length() == 0)
            throw new IllegalArgumentException("name cannot be an empty string");

        if (type == null)
            throw new IllegalArgumentException("type cannot be null.");

        if (type.trim().length() == 0)
            throw new IllegalArgumentException("type cannot be an empty string.");

        parent = definingClass;
        String s0 = type.intern();
        this.fieldType = s0;
        this.fieldName = name.intern();
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

}
