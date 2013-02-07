
package schilling.richard.dalvik.model;

import java.util.List;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.dex.TableOfContents.Section;
import com.android.dx.dex.file.EncodedField;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.EncodedValue;
import com.android.dx.merge.TypeList;

/**
 * Writes information into a DexBuffer. The only kind of writing this class
 * currently supports is writing to a new buffer. Use this class to convert a
 * DexModel into a DexBuffer.
 * 
 * @author rschilling
 */
public class DexBufferOutput {

    public static final String LOG_TAG = "Finnr.DexBufferOutput";

    private DexBuffer buffer = null;

    /* constant pool sections */
    Section headerSection;
    Section stringIdSection;
    Section typeIdSection;
    Section protoIdSection;
    Section fieldIdSection;
    Section methodIdSection;
    Section classDefSection;

    /* data sections - in order */
    Section mapSection;
    Section typeSection;
    Section annotationSetRefSection;
    Section annotationSetSection;
    Section classDataSection;
    Section codesSection;
    Section stringsSection;
    Section annotationsSection;
    Section encodedArraySection;
    Section annotationDirectorySection;

    /* data sections */
    Section map;

    public DexBuffer getBuffer() {
        return buffer;
    }

    /**
     * Write a DexModel to the output sink.
     * 
     * @param model
     *            the model to write.
     */
    public void write(DexModel model) {

        if (buffer != null)
            throw new IllegalStateException("models can only be written once.");

        if (model == null)
            return;

        createBuffer(model);

        writeStringPool(model);
        writeTypePool(model);
        writeProtoPool(model);
        writeFieldPool(model);
        writeMethodPool(model);
        writeClassDefPool(model);

    }

    /**
     * Writes strings to the data section and saves the offsets of each string
     * in the stringIdSection.
     * 
     * @param model
     *            the model to read the strings from.
     * @return the model passed in to support chain calling.
     */
    public DexModel writeStringPool(DexModel model) {
        List<String> strings = model.stringPool();

        SparseArray<String> locations = writeToDataSection(strings);

        for (int i = 0; i < locations.size(); i++) {
            stringIdSection.writeInt(locations.keyAt(i));
        }

        if (stringIdSection.remaining() > 0)
            Log.w(LOG_TAG, stringIdSection.remaining()
                    + " bytes in string pool output remain unused.");

        return model;
    }

    // TODO why is type_id_item.descriptor_idx an unsigned int and
    // field_id_item.class_idx an unsigned short?
    /**
     * For each type defined in the model, the index of the type in the string
     * pool is written to typeIdSection.
     * 
     * @param model
     *            the model that types are from.
     * @return the model passed in to support chaing calling.
     */
    public DexModel writeTypePool(DexModel model) {

        List<String> strings = model.stringPool();
        List<String> types = model.typePool();

        for (String t : types) {
            // index into the string_ids for the descriptor string of this type.
            int descriptor_idx = strings.indexOf(t);
            typeIdSection.writeInt(descriptor_idx);
        }

        if (typeIdSection.remaining() > 0)
            Log.w(LOG_TAG, typeIdSection.remaining() + " bytes in type pool output remain unused.");

        return model;

    }

    /**
     * Populates the protoIdSection with proto_id_item structures. The type
     * indexes for the parameter list is written to the data section.
     * 
     * @return the model passed in to support chaing calling.
     */
    public DexModel writeProtoPool(DexModel model) {
        List<String> strings = model.stringPool();
        List<String> types = model.typePool();
        List<MethodPrototype> prototypes = model.prototypePool();

        for (MethodPrototype prototype : prototypes) {

            String shorty = prototype.getShorty();
            String returnType = prototype.getReturnType();
            List<String> parameters = prototype.getParameters();

            SparseIntArray offsetMap = writeTypeList(types, parameters);

            /*
             * index into the string_ids list for the short-form descriptor
             * string of this prototype
             */
            int shorty_idx = strings.indexOf(shorty);

            /*
             * index into the type_ids list for the return type of this
             * prototype
             */
            int return_type_idx = types.indexOf(returnType); // FIXME add checks
                                                             // for -1 for
                                                             // indexOf
                                                             // calls.
                                                             // Indicates an
                                                             // error.

            /*
             * save the offset from the start of the file to the list of
             * parameter types for this prototype, or 0 if this prototype has no
             * parameters
             */
            int parameters_off = parameters.size() == 0 ? 0 : offsetMap.keyAt(0);

            // write proto_id_item
            protoIdSection.writeInt(shorty_idx);
            protoIdSection.writeInt(return_type_idx);
            protoIdSection.writeInt(parameters_off);

        }

        if (protoIdSection.remaining() > 0)
            Log.w(LOG_TAG, protoIdSection.remaining()
                    + " bytes in prototype pool output remain unused.");

        return model;
    }

    public DexModel writeFieldPool(DexModel model) {
        List<Field> fields = model.fieldPool();
        List<String> types = model.typePool();
        List<String> strings = model.stringPool();

        for (Field f : fields) {
            short class_idx = (short) types.indexOf(f.getDefiningClass().getSignature());
            short type_idx = (short) types.indexOf(f.getType());
            int name_idx = strings.indexOf(f.getName());

            fieldIdSection.writeUnsignedShort(class_idx);
            fieldIdSection.writeUnsignedShort(type_idx);
            fieldIdSection.writeInt(name_idx);
        }

        if (fieldIdSection.remaining() > 0)
            Log.w(LOG_TAG, fieldIdSection.remaining()
                    + " bytes in field id pool output remain unused.");

        return model;
    }

    public DexModel writeMethodPool(DexModel model) {
        List<String> types = model.typePool();
        List<String> strings = model.stringPool();
        List<Method> methods = model.methodPool();
        List<MethodPrototype> prototypes = model.prototypePool();

        for (Method method : methods) {
            short class_idx = (short) types.indexOf(method.getDeclaringClass().getSignature());
            short proto_idx = (short) prototypes.indexOf(method.getPrototype());
            int name_idx = strings.indexOf(method.getMethodName());

            methodIdSection.writeUnsignedShort(class_idx);
            methodIdSection.writeUnsignedShort(proto_idx);
            methodIdSection.writeInt(name_idx);
        }

        if (methodIdSection.remaining() > 0)
            Log.w(LOG_TAG, fieldIdSection.remaining()
                    + " bytes in method id pool output remain unused.");

        return model;
    }

    public DexModel writeClassDefPool(DexModel model) {
        List<Clazz> classes = model.classPool(false);
        List<String> types = model.typePool();
        List<String> strings = model.stringPool();

        for (Clazz clazz : classes) {
            List<String> interfaces = clazz.getInterfaces();

            // class_idx
            classDefSection.writeInt(types.indexOf(clazz.getSignature()));

            // access_flags

            classDefSection.writeInt(clazz.accessFlags);

            // superclass_idx
            String superclassSignature = clazz.getSuperclassSignature();
            int superclass_idx = superclassSignature == null ? ClassDef.NO_INDEX : types
                    .indexOf(superclassSignature);
            classDefSection.writeInt(superclass_idx);

            // interfaces_off

            int interfaces_off = 0;
            if (interfaces.size() > 0) {
                // write interface types
                SparseIntArray offsets = writeTypeList(types, interfaces);
                interfaces_off = offsets.keyAt(0);
            }
            classDefSection.writeInt(interfaces_off);

            // source_file_idx

            int source_file_idx = clazz.sourceFile == null ? ClassDef.NO_INDEX : strings
                    .indexOf(clazz.sourceFile);
            classDefSection.writeInt(source_file_idx);

            // annotations_off (annotations directory location)

            int annotations_off = clazz.hasAnnotationDirectoryItems() ? writeAnnotations(model,
                    clazz) : 0;
            classDefSection.writeInt(annotations_off);

            // class_data_off

            int class_data_off = clazz.hasClassData() ? writeClassData(model, clazz) : 0;
            classDefSection.writeInt(class_data_off);

            // static_values_off
            EncodedValue staticValues = clazz.getStaticInitialValues();
            int static_values_off = 0;
            if (staticValues != null) {
                static_values_off = encodedArraySection.getPosition();
                encodedArraySection.write(staticValues.getBytes());
            }
            classDefSection.writeInt(static_values_off);
        }
        return model;
    }

    public int writeAnnotations(DexModel model, Clazz clazz) {

        if (!clazz.hasAnnotationDirectoryItems())
            return 0;

        int result = annotationDirectorySection.getPosition();

        int class_annotations_off = write(clazz.annotations);
        SparseIntArray fieldOffsets = writeFieldAnnotations(model, clazz.getAnnotatedFields());
        SparseIntArray methodOffsets = writeMethodAnnotations(model, clazz.getAnnotatedMethods());
        SparseIntArray paramOffsets = writeMethodParameterAnnotations(model,
                clazz.getAnnotatedParameterMethods());

        annotationDirectorySection.writeInt(class_annotations_off);
        annotationDirectorySection.writeInt(fieldOffsets.size());
        annotationDirectorySection.writeInt(methodOffsets.size());
        annotationDirectorySection.writeInt(paramOffsets.size());

        for (int i = 0; i < fieldOffsets.size(); i++) {
            annotationDirectorySection.writeInt(fieldOffsets.valueAt(i));
            annotationDirectorySection.writeInt(fieldOffsets.keyAt(i));

        }

        for (int i = 0; i < methodOffsets.size(); i++) {
            annotationDirectorySection.writeInt(methodOffsets.valueAt(i));
            annotationDirectorySection.writeInt(methodOffsets.keyAt(i));

        }

        for (int i = 0; i < paramOffsets.size(); i++) {
            annotationDirectorySection.writeInt(paramOffsets.valueAt(i));
            annotationDirectorySection.writeInt(paramOffsets.keyAt(i));

        }

        return result;
    }

    public int writeClassData(DexModel model, Clazz clazz) {

        int result = classDataSection.getPosition();

        List<Method> methodPool = model.methodPool();

        List<Field> staticFields = clazz.staticFields();
        List<Field> instanceFields = clazz.instanceFields();
        List<Method> directMethods = clazz.directMethods();
        List<Method> virtualMethods = clazz.virtualMethods();

        classDataSection.writeUleb128(staticFields.size());
        classDataSection.writeUleb128(instanceFields.size());
        classDataSection.writeUleb128(directMethods.size());
        classDataSection.writeUleb128(virtualMethods.size());

        writeEncodedFields(model, staticFields);
        writeEncodedFields(model, instanceFields);
        writeEncodedMethods(model, directMethods);
        writeEncodedMethods(model, virtualMethods);

        int idx = -1;
        for (Method m : directMethods) {
            if (idx == -1)
                idx = methodPool.indexOf(m);
            else
                idx = methodPool.indexOf(m) - idx; // diff per doc

            classDataSection.writeUleb128(idx);
            classDataSection.writeUleb128(m.accessFlags);
            // classDataSection.writeUleb128(code_off); // TODO write code.

        }

        return result;
    }

    public int writeEncodedMethods(DexModel model, List<Method> methods) {
        int result = classDataSection.getPosition();
        List<Method> methodPool = model.methodPool();

        int prev_idx = -1;
        for (Method m : methods) {

            int method_idx = methodPool.indexOf(m);
            if (method_idx == -1)
                throw new IllegalArgumentException("method not found in method pool ("
                        + m.toString() + ")");

            // the index to write is the difference between this index and the
            // previous unless this is the first in the list.
            if (prev_idx == -1)
                prev_idx = method_idx;
            else
                method_idx = method_idx - prev_idx;

            Code c = m.getCode();
            int code_off = m.getCode() == null ? 0 : writeCode(c);

            classDataSection.writeUleb128(method_idx);
            classDataSection.writeUleb128(m.accessFlags);
            classDataSection.writeUleb128(code_off);

        }
        return result;
    }

    /**
     * Writes a code obejct to the codesSection
     * 
     * @param c
     *            the code to write
     * @return the position that the code was written to.
     */
    public int writeCode(Code code) {
        if (code == null)
            throw new IllegalArgumentException("code cannot be null.");

        codesSection.assertFourByteAligned();

        int result = codesSection.getPosition();

        // registers_size
        codesSection.writeUnsignedShort(code.getRegistersSize());

        // ins_size
        codesSection.writeUnsignedShort(code.getInsSize());

        // outs_size
        codesSection.writeUnsignedShort(code.getOutsSize());

        // tries_size
        codesSection.writeUnsignedShort(code.getTries().length);
        // TODO check for null pointers. The code in DexMerger does not.

        // debug_info_off
        // this version of the library does not support debug info
        codesSection.writeInt(0);

        short[] instructions = code.getInstructions();

        return result;

    }

    /**
     * Writes a list of encoded field entries to the class data section. The
     * field pool from the specified model is used to determine indexes of each
     * field. If a field is not found in the model's field pool then an
     * exception is thrown. Because Field.compareTo is defined it is possible to
     * use this function to write fields from a different model that have the
     * same definition.
     * 
     * @param model
     *            the model the list of fields came from.
     * @param fields
     *            the list of fields too write
     * @return the position that the fields were written to.
     * @throws IllegalArgumentException
     *             if a field in the list is not found in the model's field
     *             pool.
     */
    public int writeEncodedFields(DexModel model, List<Field> fields) {
        int result = classDataSection.getPosition();
        List<Field> fieldPool = model.fieldPool();

        int prev_idx = -1;
        for (Field f : fields) {

            int field_idx = fieldPool.indexOf(f);
            if (field_idx == -1)
                throw new IllegalArgumentException("field not found in field pool (" + f.toString()
                        + ")");

            // the index to write is the difference between this index and the
            // previous unless this is the first in the list.
            if (prev_idx == -1)
                prev_idx = field_idx;
            else
                field_idx = field_idx - prev_idx;

            classDataSection.writeUleb128(field_idx);
            classDataSection.writeUleb128(f.accessFlags);

        }
        return result;

    }

    /**
     * Write annotations belonging to the field to the buffer. Return the number
     * of fields saved.
     * 
     * @param fields
     *            the fieldsto walk through.
     * @return the offsets where the fields were written.
     */
    public SparseIntArray writeFieldAnnotations(DexModel model, List<Field> fields) {
        SparseIntArray result = new SparseIntArray();

        if (fields == null || fields.size() == 0)
            return result;

        List<Field> fieldPool = model.fieldPool();

        for (Field field : fields) {
            int annotation_off = write(field.getAnnotations());
            int field_idx = fieldPool.indexOf(field);
            result.put(annotation_off, field_idx);

        }

        return result;

    }

    public SparseIntArray writeMethodAnnotations(DexModel model, List<Method> methods) {

        SparseIntArray result = new SparseIntArray();

        if (methods == null || methods.size() == 0)
            return result;

        List<Method> methodPool = model.methodPool();

        for (Method method : methods) {
            int annotation_off = write(method.getAnnotations());
            int method_idx = methodPool.indexOf(method);
            result.put(annotation_off, method_idx);
        }

        return result;

    }

    public SparseIntArray writeMethodParameterAnnotations(DexModel model, List<Method> methods) {

        SparseIntArray result = new SparseIntArray();

        if (methods == null || methods.size() == 0)
            return result;

        List<Method> methodPool = model.methodPool();
        for (Method method : methods) {

            List<AnnotationSet> parameterAnnotations = method.getParameterAnnotations();
            int annotations_off = write(parameterAnnotations);
            int method_idx = methodPool.indexOf(method);

            result.put(annotations_off, method_idx);

        }

        return result;
    }

    /**
     * Writes an annotation_set_ref_list structure to the buffer.
     * 
     * @param setRefList
     *            the set list to write.
     * @return the offset in the buffer where the set was written.
     */
    public int write(List<AnnotationSet> setRefList) {
        int result = annotationSetRefSection.getPosition();

        annotationSetRefSection.writeInt(setRefList.size());
        for (AnnotationSet aSet : setRefList) {
            int offset = write(aSet);
            annotationSetRefSection.writeInt(offset);
        }

        return result;

    }

    /**
     * Write an annotation set to the underlying buffer. Annotations are written
     * to annotationsSection.
     * 
     * @param aSet
     *            the set to write
     * @return the offset where the annotation set was written.
     */
    public int write(AnnotationSet aSet) {

        int result = annotationSetSection.getPosition();

        SparseArray<Annotation> offsets = new SparseArray<Annotation>();

        if (aSet != null && aSet.size() > 0) {

            for (Annotation annotation : aSet) {
                int writtenAt = write(annotation);
                offsets.put(writtenAt, annotation);
            }

            annotationSetSection.writeInt(offsets.size()); // offsets.size() ==
                                                           // aSet.size()
            for (int i = 0; i < offsets.size(); i++) {
                annotationSetSection.writeInt(offsets.keyAt(i));
            }
        }

        return result;

    }

    /**
     * Write an annotation to the annotationsSection.
     * 
     * @param annotation
     *            the annotation to write.
     * @return the position in the underlying buffer that the annotation was
     *         written to.
     */
    public int write(Annotation annotation) {
        int result = annotationsSection.getPosition();
        annotationsSection.write(annotation.asByteArray());
        return result;
    }

    private void createBuffer(DexModel model) {
        buffer = new DexBuffer();

        TableOfContents toc = buffer.getTableOfContents();
        toc.readFrom(model);

        headerSection = buffer.appendSection(SizeOf.HEADER_ITEM, toc.header.getName());

        stringIdSection = buffer.appendSection(toc.stringIds.byteCount, toc.stringIds.getName());

        typeIdSection = buffer.appendSection(toc.typeIds.byteCount, toc.typeIds.getName());

        protoIdSection = buffer.appendSection(toc.protoIds.byteCount, toc.protoIds.getName());

        fieldIdSection = buffer.appendSection(toc.fieldIds.byteCount, toc.fieldIds.getName());

        methodIdSection = buffer.appendSection(toc.methodIds.byteCount, toc.methodIds.getName());

        classDefSection = buffer.appendSection(toc.classDefs.byteCount, toc.classDefs.getName());

        mapSection = buffer.appendSection(toc.mapList.byteCount, toc.mapList.getName());
        typeSection = buffer.appendSection(toc.typeLists.byteCount, toc.typeLists.getName());
        annotationSetRefSection = buffer.appendSection(toc.annotationSetRefLists.byteCount,
                toc.annotationSetRefLists.getName());
        annotationSetSection = buffer.appendSection(toc.annotationSets.byteCount,
                toc.annotationSets.getName());
        classDataSection = buffer.appendSection(toc.classDatas.byteCount, toc.classDatas.getName());
        codesSection = buffer.appendSection(toc.codes.byteCount, toc.codes.getName());
        stringsSection = buffer.appendSection(toc.stringDatas.byteCount, toc.stringDatas.getName());
        annotationsSection = buffer.appendSection(toc.annotations.byteCount,
                toc.annotations.getName());
        annotationSetSection = buffer.appendSection(toc.encodedArrays.byteCount,
                toc.encodedArrays.getName());
        annotationSetSection = buffer.appendSection(toc.annotationsDirectories.byteCount,
                toc.annotationsDirectories.getName());

        buffer.noMoreSections();

        Log.i(LOG_TAG, "created a DexBuffer with " + buffer.getLength() + " bytes");

    }

    /**
     * Opens a section on the buffer for the entire dataSection.
     * 
     * @return a read/write section on the data area of the buffer.
     */
    public Section openDataSection() {

        return buffer.open(mapSection.getStartPosition());

    }

    // TODO analysis: determine what refactoring can be done to make a binary
    // file shorter. Generate a list of changes. Get the carriers to require
    // companies to adopt those changes!

    /**
     * Generates a DexBuffer from a DexModel.
     * 
     * @return
     */
    public DexBuffer asBuffer() {
        DexBuffer result = new DexBuffer();

        return result;
    }

    /**
     * Writes a list of strings to the data section current location and returns
     * an array indicating what offsets each item was written to.
     * 
     * @param list
     *            the list to write into the current location of the data
     *            section
     * @return a sparse array where the keys are the offsets that each string
     *         was written to.
     */
    private SparseArray<String> writeToDataSection(List<String> list) {
        if (list == null)
            throw new IllegalArgumentException("list cannot be null.");

        SparseArray<String> result = new SparseArray<String>();
        /*
         * int pos = dataSection.getPosition(); for (String s : list) {
         * dataSection.writeStringData(s); result.put(pos, s); pos =
         * dataSection.getPosition(); }
         */
        return result;
    }

    /**
     * Writes a list of types to the data seection. No effect if types is null
     * or has a length of zero. The first item of the returned sparse array
     * indicates the offset where the size of the array was written.
     * <p>
     * The keys of the returned SparseArray indicate the offset where each item
     * was written. The integer value of each item indicates what was written in
     * that location. The first item in the returned list is the size of the
     * typed array.
     * <p>
     * This function always returns a non-null value. If the SparseIntArray has
     * a size of 0 it means that the dataSection was unaffected.
     * 
     * @param model
     *            the model to get the type pool from.
     * @param types
     *            the list of types to write.
     * @return a sparse array where the keys indicate the file offset where each
     *         item was written.
     * @deprecated
     */

    public SparseIntArray writeTypeList(List<String> typePool, List<String> types) {

        SparseIntArray result = new SparseIntArray();

        if (types == null)
            return result;

        int size = types.size();
        if (size == 0)
            return result;

        /*
         * int pos = dataSection.getPosition(); result.put(pos, size);
         * 
         * dataSection.writeInt(size); for (String s : types) { pos =
         * dataSection.getPosition(); short type_idx = (short)
         * typePool.indexOf(s); result.put(pos, type_idx);
         * 
         * dataSection.writeUnsignedShort(type_idx);
         * 
         * }
         */
        return result;
    }
}
