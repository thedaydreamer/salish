
package schilling.richard.dalvik.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.android.dx.io.EncodedValue;
import com.android.dx.rop.code.AccessFlags;

/**
 * Represents a class.
 * TODO see dalvik.annotation.Signature. I may have to read these values as
 * well.
 * 
 * @author rschilling
 */
public class Clazz implements Comparable<Clazz> {

    /**
     * The model that this class definition belongs too. It's parent. Never
     * null.
     */
    private final DexModel model;

    /**
     * The class signature.
     */
    private final String signature; // TODO allow refactoring of signatures.
    public String sourceFile;
    public int accessFlags;
    private String superclassSignature;
    private boolean hasClassData = false;

    private final LinkedList<String> interfaces = new LinkedList<String>();
    private final LinkedList<Field> staticFields = new LinkedList<Field>();
    private final LinkedList<Field> instanceFields = new LinkedList<Field>();
    private final LinkedList<Method> directMethods = new LinkedList<Method>();
    private final LinkedList<Method> virtualMethods = new LinkedList<Method>();

    /* package */void hasClassData(boolean doesHave) {
        hasClassData = doesHave;
    }

    public boolean hasClassData() {
        return hasClassData;
    }

    public List<String> interfaces() {
        Collections.sort(interfaces);
        return Collections.unmodifiableList(interfaces);
    }

    public List<Field> staticFields() {
        Collections.sort(staticFields);
        return Collections.unmodifiableList(staticFields);
    }

    public List<Field> instanceFields() {
        Collections.sort(instanceFields);
        return Collections.unmodifiableList(instanceFields);
    }

    public List<Method> directMethods() {
        Collections.sort(directMethods);
        return Collections.unmodifiableList(directMethods);
    }

    public List<Method> virtualMethods() {
        Collections.sort(virtualMethods);
        return Collections.unmodifiableList(virtualMethods);
    }

    public List<Field> allFields() {
        LinkedList<Field> result = new LinkedList<Field>();
        result.addAll(staticFields);
        result.addAll(instanceFields);
        Collections.sort(result);
        return Collections.unmodifiableList(result);

    }

    public List<Method> allMethods() {
        LinkedList<Method> result = new LinkedList<Method>();
        result.addAll(directMethods);
        result.addAll(virtualMethods);
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

    /**
     * Annotations applied directly to this class.
     */
    public AnnotationSet annotations = null;

    /**
     * Initial values for static members stored in an encoded array.
     */
    private EncodedValue staticMembersInitialValues;

    public Clazz(DexModel model, String signature) {

        if (model == null)
            throw new IllegalArgumentException("model cannot be null");

        if (signature == null)
            throw new IllegalArgumentException("signature cannot be null");

        if (!model.isInTypePool(signature))
            throw new IllegalArgumentException("signature is not in the type pool");

        this.signature = signature.intern();
        this.model = model;
    }

    /**
     * return true if this class has an annotations directory
     * 
     * @return true if an annotations directory exists for this class.
     */
    public boolean hasAnnotationDirectoryItems() {
        if (annotations != null && annotations.size() > 0)
            return true;

        for (Method m : allMethods()) {
            List<Annotation> annotations = m.getAnnotations();
            if (annotations != null && annotations.size() > 0)
                return true;
        }

        for (Field f : allFields()) {
            List<Annotation> annotations = f.getAnnotations();
            if (annotations != null && annotations.size() > 0)
                return true;
        }

        return false;

    }

    public List<Field> getAnnotatedFields() {
        List<Field> result = new LinkedList<Field>();
        for (Field f : allFields()) {
            List<Annotation> annotations = f.getAnnotations();
            if (annotations == null || annotations.size() == 0)
                continue;
            result.add(f);
        }
        return result;
    }

    /**
     * Return methods with annotations.
     * 
     * @return
     */
    public List<Method> getAnnotatedMethods() {
        List<Method> result = new LinkedList<Method>();
        for (Method f : allMethods()) {

            List<Annotation> annotations = f.getAnnotations();
            if (annotations == null || annotations.size() == 0)
                continue;
            result.add(f);

        }
        return result;
    }

    /**
     * Return methods with parameter annotations.
     * 
     * @return
     */
    public List<Method> getAnnotatedParameterMethods() {
        List<Method> result = new LinkedList<Method>();
        for (Method f : allMethods()) {

            List<AnnotationSet> parameterAnnotations = f.getParameterAnnotations();
            if (parameterAnnotations == null || parameterAnnotations.size() == 0)
                continue;

            result.add(f);

        }
        return result;
    }

    /* package */void setSuperclass(String signature) {

        if (signature != null && !model.isInTypePool(signature))
            throw new IllegalArgumentException("the signature is not found in the type pool");

        this.superclassSignature = signature.intern();

    }

    /* package */void addInterface(String signature) {

        if (interfaces.contains(signature))
            throw new IllegalArgumentException("interface already added to interface table.");

        if (!model.isInTypePool(signature))
            throw new IllegalArgumentException("the signature is not found in the type pool");

        interfaces.add(signature.intern());

    }

    public String getSignature() {
        return signature;
    }

    /**
     * Sets the bytes that contain the initial values for static members. The
     * item passed is expected to contain an encoded array. See
     * Section.readEncodedArray.
     * 
     * @param encArr
     *            the encoded array to use as static values.
     */
    /* package */void setStaticInitialValues(EncodedValue encArr) {
        staticMembersInitialValues = encArr;
    }

    public EncodedValue getStaticInitialValues() {
        return staticMembersInitialValues;
    }

    /* package */void addStaticField(Field f) {
        if (f == null)
            throw new IllegalArgumentException("the specified field cannot be null.");

        if (!staticFields.contains(f))
            staticFields.add(f);

        Collections.sort(staticFields);
    }

    /**
     * Adds all fields from a list to the static field list held by this class.
     * Each field's parent is set to this class and the static fields list is
     * sorted if there are additions. Causes a sort operation to happen on the
     * static field list.
     * 
     * @param fields
     *            the fields to add to the static field list.
     */
    /* package */void addStaticFields(List<Field> fields) {
        if (fields == null)
            throw new IllegalArgumentException("fields cannot be null");

        if (fields.size() == 0)
            return;

        for (Field f : fields) {
            if (!staticFields.contains(f))
                staticFields.add(f);
        }

        Collections.sort(staticFields);
    }

    /* package */void addInstanceField(Field f) {
        if (f == null)
            throw new IllegalArgumentException("the specified field cannot be null.");

        if (!instanceFields.contains(f))
            instanceFields.add(f);

        Collections.sort(instanceFields);
    }

    /**
     * Adds all fields from a list to the instance field list held by this
     * class. Each field's parent is set to this class and the instance fields
     * list is sorted if there are additions. Causes a sort operation to happen
     * on the instance field list.
     * 
     * @param fields
     *            the fields to add to the instance field list.
     */
    /* package */void addInstanceFields(List<Field> fields) {
        if (fields == null)
            throw new IllegalArgumentException("fields must not be empty");

        if (fields.size() == 0)
            return;

        for (Field f : fields) {
            if (!instanceFields.contains(f))
                instanceFields.add(f);
        }

        Collections.sort(instanceFields);

    }

    public DexModel getModel() {
        return model;
    }

    /* package */void addDirectMethods(List<Method> methods) {
        if (methods == null)
            throw new IllegalArgumentException("methods must not be empty.");
        if (methods.size() == 0)
            return;

        for (Method m : methods) {
            if (!directMethods.contains(m))
                directMethods.add(m);
        }
    }

    /* package */void addVirtualMethods(List<Method> methods) {
        if (methods == null)
            throw new IllegalArgumentException("methods must not be empty.");

        if (methods.size() == 0)
            return;

        for (Method m : methods) {
            if (!virtualMethods.contains(m))
                virtualMethods.add(m);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(AccessFlags.classString(accessFlags)).append(" ");
        result.append(signature);
        if (superclassSignature != null) {
            result.append(" extends ").append(superclassSignature);
        }
        if (interfaces.size() > 0) {
            result.append(" implements ");
            for (int i = 0; i < interfaces.size(); i++) {
                result.append(interfaces.get(i));
                if (i < (interfaces.size() - 1))
                    result.append(", ");
            }
        }

        return result.toString();
    }

    // TODO when adding a class to the class table, check for volation of class
    // inheritance (e.g. no superclass circular references allowed).
    /**
     * Sorts according to the rules specified in class_defs in the Dalvik file
     * layout spec. This class is ordered after superclasses and interfaces that
     * it has.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Clazz another) {
        if (another == null)
            return 1;

        String anotherSignature = another.getSignature();

        if (signature.equals(anotherSignature))
            return 1; // super classes sort before this one.

        for (String iSignature : interfaces) {
            if (iSignature.equals(anotherSignature))
                return 1; // interfaces sort before this one.

        }

        return signature.compareTo(anotherSignature);
    }

    public boolean equals(Object toAnother) {
        if (!(toAnother instanceof Clazz))
            return false;

        Clazz anotherClass = (Clazz) toAnother;
        return signature.equals(anotherClass.signature);

    }

    public static class PlatformClazz extends Clazz {

        public PlatformClazz(DexModel model, String signature) {
            super(model, signature);
        }

    }

    public String getSuperclassSignature() {
        return superclassSignature;
    }

    public List<String> getInterfaces() {
        Collections.sort(interfaces);
        return Collections.unmodifiableList(interfaces);
    }

}
