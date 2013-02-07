
package schilling.richard.dalvik.model;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.EncodedValue;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;

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
    private DexBuffer buffer;

    /**
     * The model that data is read into.
     */
    private DexModel model;

    public DexBufferInput(DexBuffer input) {
        this.buffer = input;
        model = new DexModel();
    }

    public DexBufferInput(DexBuffer input, DexModel appendTo) {
        this.buffer = input;
        this.model = appendTo;
    }

    public DexModel readModel() {
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
     */
    public DexModel readClassData() {
        Iterable<ClassDef> classDefs = buffer.classDefs();
        List<String> types = buffer.typeNames();

        // Add all classes in the file to the class table.
        for (ClassDef cDef : classDefs) {
            String signature = types.get(cDef.getTypeIndex());
            Clazz clazz = model.classTable.get(signature);

            if (clazz == null)
                throw new IllegalArgumentException(
                        "class "
                                + signature
                                + " in the DexBuffer is not in the class pool.  Did you call addClassDefinitionsFrom yet?");

            StringBuilder status = new StringBuilder();
            status.append("import of class data for class ").append(signature).append(" ");

            /* class_data_off */
            int classDataOff = cDef.getClassDataOffset();
            if (classDataOff > 0) {
                clazz.hasClassData(true);
                ClassData cData = buffer.readClassData(cDef);

                // get the original ists from class data
                ClassData.Field[] iFields = cData.getInstanceFields();
                ClassData.Field[] sFields = cData.getStaticFields();
                ClassData.Method[] dMethods = cData.getDirectMethods();
                ClassData.Method[] vMethods = cData.getVirtualMethods();

                // convert to model equivalents
                List<Field> instanceFields = bindFields(buffer, iFields);
                List<Field> staticFields = bindFields(buffer, sFields);
                List<Method> directMethods = bindMethods(buffer, dMethods);
                List<Method> virtualMethods = bindMethods(buffer, vMethods);

                // add to the class definition
                clazz.addInstanceFields(instanceFields);
                clazz.addStaticFields(staticFields);
                clazz.addDirectMethods(directMethods);
                clazz.addVirtualMethods(virtualMethods);
                status.append(" successful.");
                status.append(instanceFields.size()).append(" instance fields;");
                status.append(staticFields.size()).append(" static fields;");
                status.append(directMethods.size()).append(" direct methods;");
                status.append(virtualMethods.size()).append(" virtual methods");

                /* sanity check */
                for (Field f : instanceFields) {
                    if (f.getDefiningClass() != clazz)
                        throw new IllegalStateException(
                                "parent class to field did not get set properly.");
                }

                for (Field f : staticFields) {
                    if (f.getDefiningClass() != clazz)
                        throw new IllegalStateException(
                                "parent class to field did not get set properly.");
                }

                for (Method m : directMethods) {
                    if (m.getDeclaringClass() != clazz)
                        throw new IllegalStateException(
                                "parent class to meghod did not get set properly.");
                }

                for (Method m : virtualMethods) {
                    if (m.getDeclaringClass() != clazz)
                        throw new IllegalStateException(
                                "parent class to meghod did not get set properly.");
                }

            } else {
                status.append(" skipped (no data).");
                clazz.hasClassData(false);
            }
            Log.i(LOG_TAG, status.toString());

            /* static_values_off */
            Section sec = buffer.open(cDef.getStaticValuesOffset());
            clazz.setStaticInitialValues(sec.readEncodedArray());

        }

        return model;

    }

    public Method lookup(MethodId methodId) {
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();
        List<ProtoId> protoIds = buffer.protoIds();

        /*
         * construct relevant information so that the proper method definition
         * can be found.
         */

        // declaring class
        String declaringClassSignature = types.get(methodId.getDeclaringClassIndex());
        Clazz declaringClass = model.classTable.get(declaringClassSignature);
        if (declaringClass == null)
            throw new IllegalStateException(
                    "no parent class defined.  Did you call addClassDefinitionsFrom(buffer) ?");

        // method name
        String methodName = strings.get(methodId.getNameIndex());

        // method prototype
        ProtoId protoId = protoIds.get(methodId.getProtoIndex());
        String returnType = types.get(protoId.getReturnTypeIndex());
        String shortyName = protoId.getShortyString();
        List<String> parameters = protoId.getParameters();

        MethodPrototype methodProto = new MethodPrototype(model, shortyName, returnType);
        methodProto.setParameters(parameters);

        Method result = new Method(declaringClass, methodName, methodProto);

        int pos = model.methodPool().indexOf(result);

        if (pos == -1)
            throw new IllegalArgumentException("method id has no corresponding Method in the pool");

        result = model.methodPool().get(pos);

        return result;

    }

    public Field lookup(FieldId fieldId) {
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();

        String fieldName = strings.get(fieldId.getNameIndex());
        String fieldType = types.get(fieldId.getTypeIndex());
        String fieldParentSignature = types.get(fieldId.getDeclaringClassIndex());
        Clazz fieldParent = model.classTable.get(fieldParentSignature);

        Field result = new Field(fieldParent, fieldName, fieldType);
        int pos = model.fieldPool().indexOf(result);

        if (pos == -1) {
            // assume the field is legitimate.
            model.addToFieldPool(result);
            pos = model.fieldPool().indexOf(result);

        }
        result = model.fieldPool().get(pos);

        return result;
    }

    public MethodPrototype lookup(ProtoId protoId) {

        MethodPrototype result = new MethodPrototype(model, protoId.getShortyString(),
                protoId.getReturnTypeString());

        result.setParameters(protoId.getParameters());

        // get the index of the object in the prototype list so there
        // are no duplicates in memory.
        int pos = model.prototypePool().indexOf(result);
        if (pos == -1) {
            model.addToPrototypePool(result);
            pos = model.prototypePool().indexOf(result);
        }

        result = model.prototypePool().get(pos);

        return result;

    }

    /**
     * Returns a list of Field objects that correspond to the array of
     * ClassData.Field objects.
     * 
     * @param buffer
     *            the buffer to get names, types and fieldIds from.
     * @param fields
     *            the list of fields to lookup.
     * @return the list of in-memory Field objects that correspond to the each
     *         item in fields.
     * @throws IllegalArgumentException
     *             if one of the fields in the list is not found in the
     *             FieldPool.
     */
    public List<Field> bindFields(DexBuffer buffer, ClassData.Field[] fields) {
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();
        List<FieldId> fieldIds = buffer.fieldIds();

        List<Field> result = new LinkedList<Field>();
        if (fields == null)
            return result;

        for (ClassData.Field field : fields) {

            FieldId fieldId = fieldIds.get(field.getFieldIndex());
            Field f = lookup(fieldId);

            if (!model.fieldPool().contains(f))
                throw new IllegalArgumentException("the field pool does not contain field "
                        + f.toString() + ".  Did you call addFieldsFrom(DexBuffer)?");

            int pos = model.fieldPool().indexOf(f);
            f = model.fieldPool().get(pos);

            // update other structures
            f.accessFlags = field.getAccessFlags();

            result.add(f);

        }

        return result;

    }

    /**
     * Binds a list of methods in the method pool to the parent class. Also adds
     * the referenced code to the each method object and updates access flags.
     * Does not add the resulting method list to the parent class because it's
     * not possible to tell if the list of methods are virtual or direct
     * methods.
     * 
     * @param buffer
     *            the buffer to get names, types and methodIds from. Must be the
     *            same buffer that methods came from.
     * @param fields
     *            the list of methods to lookup.
     * @return the list of in-memory Method objects that correspond to the each
     *         item in methods.
     * @throws IllegalArgumentException
     *             if one of the methods in the list is not found in the Method
     *             pool.
     */
    private List<Method> bindMethods(DexBuffer buffer, ClassData.Method[] methods) {
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();
        List<MethodId> methodIds = buffer.methodIds();

        List<Method> result = new LinkedList<Method>();
        if (methods == null)
            return result;

        List<ProtoId> protoIds = buffer.protoIds();

        for (ClassData.Method method : methods) {

            MethodId methodId = methodIds.get(method.getMethodIndex());
            Method m = lookup(methodId);

            // with a valid method we can now retrieve the proper handle from
            // the methodpool.
            if (!model.methodPool().contains(m))
                throw new IllegalArgumentException("the method pool does not contain method "
                        + m.getMethodName());

            // get handle from the pool.
            int pos = model.methodPool().indexOf(m);
            m = model.methodPool().get(pos);
            model.methodPool().add(m);

            // update the retrieved method.
            m.accessFlags = method.getAccessFlags();
            if (method.getCodeOffset() > 0) {
                Code c = buffer.readCode(method);
                m.setCode(c);
            }

            result.add(m);

        }

        return result;

    }

    public DexModel readMethods() {
        List<MethodId> methods = buffer.methodIds();
        List<String> strings = buffer.strings();
        List<ProtoId> protoIds = buffer.protoIds();
        List<String> types = buffer.typeNames();

        if (methods != null) {
            for (MethodId method : methods) {
                String declaringClassSignature = types.get(method.getDeclaringClassIndex());
                Clazz declaringClass = model.classTable.get(declaringClassSignature);
                if (declaringClass == null) {
                    // assume the class signature is for a platform class.
                    // TODO add verification and analysis of platform class
                    // usage.
                    declaringClass = new Clazz.PlatformClazz(model, declaringClassSignature);
                    model.classTable.put(declaringClassSignature, declaringClass);
                }

                String methodName = strings.get(method.getNameIndex());

                ProtoId protoId = protoIds.get(method.getProtoIndex());

                MethodPrototype mPrototype = lookup(protoId);

                Method m = new Method(declaringClass, methodName, mPrototype);

                if (model.addToMethodPool(m)) {
                    Log.i(LOG_TAG, "import of method " + m.toString() + " successful");
                } else {
                    Log.i(LOG_TAG, "import of method " + m.toString() + " skipped (duplicate)");

                }

            }
        }

        return model;
    }

    public DexModel readFields() {
        List<FieldId> fields = buffer.fieldIds();
        List<String> strings = buffer.strings();
        List<String> types = buffer.typeNames();

        if (fields != null) {
            for (FieldId fId : fields) {

                // get the relevant class from the class table
                String parentSignature = types.get(fId.getDeclaringClassIndex());

                Clazz parent = model.classTable.get(parentSignature);
                if (parent == null)
                    throw new IllegalStateException(
                            "cannot import a field unless an associated class definition is already created. Did you call addClassDefinitionsFrom(buffer)?");

                Field f = new Field(parent, strings.get(fId.getNameIndex()), types.get(fId
                        .getTypeIndex()));

                if (model.fieldPool().add(f)) {
                    Log.i(LOG_TAG, "import of field " + f.toString() + " successful");
                } else {
                    Log.i(LOG_TAG, "import of field " + f.toString() + " skipped (duplicate)");

                }

            }

        }

        return model;

    }

    /**
     * Adds the basic definition of a class to the model. Does not import any of
     * the methods, fields, or static members (child definitions).
     * 
     * @param buffer
     *            the buffer to read classes from.
     */
    public DexModel readClassDefinitions() {

        Iterable<ClassDef> classDefs = buffer.classDefs();
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();

        // Add all classes in the file to the class table.
        for (ClassDef cDef : classDefs) {

            /* class_idx - type id */
            String signature = types.get(cDef.getTypeIndex()).intern();

            Clazz clazz = new Clazz(model, signature);

            if (model.classTable.containsKey(signature)) {
                Log.i(LOG_TAG, "import of class " + signature + " skipped (duplicate).");
                continue;
            }

            model.classTable.put(signature, clazz);

            /* access_flags */
            clazz.accessFlags = cDef.getAccessFlags();

            /* superclass_idx */
            int stIdx = cDef.getSupertypeIndex();
            if (stIdx != ClassDef.NO_INDEX)
                clazz.setSuperclass(types.get(stIdx));

            /* interfaces_off - also handles the case where interfaces_off is 0 */
            for (short idx : cDef.getInterfaces()) {
                clazz.addInterface(types.get(idx));
            }

            /* source_file_idx */
            int sfIdx = cDef.getSourceFileIndex();
            if (sfIdx != ClassDef.NO_INDEX)
                clazz.sourceFile = strings.get(sfIdx);

            /* annotations_off */
            int annOff = cDef.getAnnotationsOffset();
            if (annOff > 0)
                readAnnotationsDirectoryItem(clazz, annOff);

            Log.i(LOG_TAG, "import of class " + signature + " successful.");
        }

        return model;
    }

    public DexModel readPrototypes() {
        List<ProtoId> protoIds = buffer.protoIds();

        if (protoIds != null) {
            for (ProtoId pId : protoIds) {
                MethodPrototype mPrototype = new MethodPrototype(model, pId.getShortyString(),
                        pId.getReturnTypeString());
                mPrototype.setParameters(pId.getParameters());
                if (model.addToPrototypePool(mPrototype)) {
                    Log.i(LOG_TAG, "import of prototype " + mPrototype.toString() + " successful");
                } else {
                    Log.i(LOG_TAG, "import of prototype " + mPrototype.toString()
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

        Section section = buffer.open(offset);

        if (section == null)
            throw new IllegalArgumentException("section cannot be bull");

        int classAnnotationsOffset = section.readInt();
        int fieldSz = section.readInt();
        int methodSz = section.readInt();
        int paramSz = section.readInt();

        /*
         * if (classAnnotationsOffset > 0)
         * // get annotations that are applied directly to the class.
         * clazz.annotations =
         * convert_annotation_set_item(classAnnotationsOffset);
         * 
         * for (int i = 0; i < fieldSz; i++) {
         * 
         * int fieldIdx = section.readInt();
         * FieldId fid = fieldIds.get(fieldIdx);
         * Field field = lookup(fid);
         * 
         * int annotationOffset = section.readInt();
         * List<Annotation> fieldAnnotations =
         * convert_annotation_set_item(annotationOffset);
         * field.setAnnotations(fieldAnnotations);
         * 
         * }
         * 
         * for (int i = 0; i < methodSz; i++) {
         * 
         * int methodIdx = section.readInt();
         * MethodId mid = methodIds.get(methodIdx);
         * 
         * Method method = lookup(mid);
         * 
         * int annotationOffset = section.readInt();
         * List<Annotation> methodAnnotations =
         * convert_annotation_set_item(annotationOffset);
         * 
         * method.setAnnotations(methodAnnotations);
         * 
         * }
         * 
         * for (int i = 0; i < paramSz; i++) {
         * int methodIdx = section.readInt();
         * MethodId mid = methodIds.get(methodIdx);
         * 
         * Method method = lookup(mid);
         * 
         * int annotationSetRefOffset = section.readInt();
         * 
         * List<List<Annotation>> parameterAnnotations =
         * convert_annotation_set_ref_list(
         * buffer, annotationSetRefOffset);
         * 
         * method.setParameterAnnotations(parameterAnnotations);
         * }
         */

        return model;

    }

    public List<List<Annotation>> convert_annotation_set_ref_list(DexBuffer buffer, int offset) {
        List<List<Annotation>> result = new LinkedList<List<Annotation>>();

        Section section = buffer.open(offset);

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

        Section section = buffer.open(offset);
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();
        List<com.android.dx.io.Annotation> annotations = section.readAnnotationSet();

        for (com.android.dx.io.Annotation annotation : annotations) {
            Annotation convertedAnn = new Annotation(model, types.get(annotation.getTypeIndex()));
            convertedAnn.visibility = annotation.getVisibility();
            int[] names = annotation.getNames();
            EncodedValue[] values = annotation.getValues();

            for (int i = 0; i < names.length; i++) {
                String name = strings.get(names[i]);
                EncodedValue value = values[i];
                convertedAnn.annotationValues.put(name, value);
            }

            result.add(convertedAnn);

        }

        return result;
    }
}
