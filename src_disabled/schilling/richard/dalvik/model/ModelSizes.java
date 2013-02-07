package com.android.dx.dex;

import java.io.UTFDataFormatException;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.android.dx.dex.Clazz;
import com.android.dx.dex.DexModel;
import com.android.dx.dex.Field;
import com.android.dx.dex.Method;
import com.android.dx.dex.MethodPrototype;
import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.io.Code;
import com.android.dx.io.EncodedValue;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.util.Leb128Utils;
import com.android.dx.util.Mutf8;

public class ModelSizes {

	/**
	 * Return size in bytes for a class_def_item.
	 * 
	 * @param clazz
	 *            the class to get the data section size from.
	 * @return number of bytes the class data will occupy when written.
	 */
	public static final int class_def_item(Clazz clazz) {
		int result = 0;

		if (!clazz.hasClassData())
			return result;

		return CLASS_DEF_ITEM;

	}

	public static final int annotation_set_ref_list(
			List<AnnotationSet> annotationSetRefs) {

		int result = UINT; // size
		result += UINT * annotationSetRefs.size(); // offset list

		return result;
	}

	public static final int annotation_set_item(AnnotationSet annotations) {
		int result = 0;

		result += UINT; // list size
		result += UINT * annotations.size(); // annotation_off_item entries

		return result;

	}

	public static final int annotation_item(Annotation ann) {
		int result = 0;

		List<String> types = ann.getModel().typePool();
		List<String> strings = ann.getModel().stringPool();

		result += UBYTE; // visibility

		// type index
		int typeIndex = types.indexOf(ann.getSignature());
		int tableSize = ann.annotationValues.size();

		result += Leb128Utils.unsignedLeb128Size(typeIndex); // type_idx
		result += Leb128Utils.unsignedLeb128Size(tableSize); // size

		Set<Entry<String, EncodedValue>> set = ann.annotationValues.entrySet();

		for (Entry<String, EncodedValue> kvPair : set) {
			int keyIndex = strings.indexOf(kvPair.getKey());
			EncodedValue val = kvPair.getValue();

			result += Leb128Utils.unsignedLeb128Size(keyIndex); // name_idx
			result += val.getBytes().length; // value
		}

		return result;

	}

	/**
	 * Calculate the size of a class data item. The number returned is greater
	 * than the actual expected number of bytes that would be utilized because
	 * the code offset for each method cannot be determined. So, the value
	 * returned by this function is the upper bound of space usage.
	 * 
	 * @param clazz
	 *            the class to calculate space required for its class_data_item.
	 * @return the upper bound of byte counte needed to store class_data_item.
	 */
	public static final int class_data_item(Clazz clazz) {
		int result = 0;

		if (!clazz.hasClassData())
			return result;

		DexModel model = clazz.getModel();
		List<Field> fieldPool = model.fieldPool();
		List<Method> methodPool = model.methodPool();

		List<Field> staticFields = clazz.staticFields();
		List<Field> instanceFields = clazz.instanceFields();
		List<Method> directMethods = clazz.directMethods();
		List<Method> virtualMethods = clazz.virtualMethods();

		result += Leb128Utils.unsignedLeb128Size(staticFields.size());
		result += Leb128Utils.unsignedLeb128Size(instanceFields.size());
		result += Leb128Utils.unsignedLeb128Size(directMethods.size());
		result += Leb128Utils.unsignedLeb128Size(virtualMethods.size());

		int idx = -1;
		for (Field f : staticFields) {

			if (idx == -1)
				idx = fieldPool.indexOf(f);
			else
				idx = fieldPool.indexOf(f) - idx; // diff per docs

			result += Leb128Utils.unsignedLeb128Size(idx);
			result += Leb128Utils.unsignedLeb128Size(f.accessFlags);

		}

		idx = -1;
		for (Field f : instanceFields) {
			if (idx == -1)
				idx = fieldPool.indexOf(f);
			else
				idx = fieldPool.indexOf(f) - idx; // diff per docs

			result += Leb128Utils.unsignedLeb128Size(idx);
			result += Leb128Utils.unsignedLeb128Size(f.accessFlags);

		}

		idx = -1;
		for (Method m : directMethods) {
			if (idx == -1)
				idx = methodPool.indexOf(m);
			else
				idx = methodPool.indexOf(m) - idx; // diff per docs

			result += Leb128Utils.unsignedLeb128Size(idx);
			result += Leb128Utils.unsignedLeb128Size(m.accessFlags);

			result += 5; // for code offset utilize maximum possible bytes for
							// Uleb128

		}

		idx = -1;
		for (Method m : virtualMethods) {
			if (idx == -1)
				idx = methodPool.indexOf(m);
			else
				idx = methodPool.indexOf(m) - idx; // diff per docs

			result += Leb128Utils.unsignedLeb128Size(idx);
			result += Leb128Utils.unsignedLeb128Size(m.accessFlags);
			result += 5; // for code offset utilize maximum possible bytes for
							// Uleb128
		}

		return result;

	}

	public static final int annotations_directory_item(Clazz clazz) {

		int result = 0;

		if (!clazz.hasAnnotationDirectoryItems())
			return result;

		/*
		 * class_annotations_off, fields_size, annotated_methods_size,
		 * annotated_parameters_size
		 */
		result += SizeOf.UINT * 4;

		List<Field> fields = clazz.getAnnotatedFields();
		for (Field f : fields) {
			result += SizeOf.annotation_set_item(f.getAnnotations());
			result += SizeOf.UINT; // field_idx (field_annotation)
			result += SizeOf.UINT; // annotations_off (field_annotation)
		}

		List<Method> methods = clazz.getAnnotatedMethods();
		if (methods != null) {
			result += methods.size() * (SizeOf.UINT + SizeOf.UINT); // method_idx
																	// +
																	// annotations_off
		}

		if (clazz.annotations != null)
			result += clazz.annotations.size() * (SizeOf.UINT + SizeOf.UINT);

		return result;
	}
	
	
	 /**
     * Returns the number of bytes that the string pool would take up in a DEX
     * file. This is not the number of bytes that will be used in the data
     * section. To get the number of bytes taken up in the data section see
     * 
     * @return bytes that the string pool takes up.
     */
    public static int stringPoolSize(DexModel model) {
        return model.stringPool().size() * SizeOf.UINT;

    }

    public static int typePoolSize(DexModel model) {
        return model.typePool().size() * SizeOf.UINT;
    }

    public static int protoPoolSize(DexModel model) {
        return model.prototypePool().size() * SizeOf.PROTO_ID_ITEM;
    }

    public static int fieldPoolSize(DexModel model) {
        return model.fieldPool().size() * SizeOf.FIELD_ID_ITEM;
    }

    public static int methodPoolSize(DexModel model) {
        return model.methodPool().size() * SizeOf.METHOD_ID_ITEM;
    }

    public static int classDefPoolSize(DexModel model) {
        return model.classDefPoolCount() * SizeOf.CLASS_DEF_ITEM;
    }
    
    /**
     * Calculates the amount of space that prototype type lists will occupy in
     * the data section.
     * 
     * @param model
     *            the model to get prototypes from.
     * @return the number of bytes all prototype type_lists will occupy.
     */
    public static int dataSizePrototypes(DexModel model) {
        if (model == null)
            throw new IllegalArgumentException("model cannot be null");

        int result = 0;

        for (MethodPrototype prototype : model.prototypePool()) {
            List<String> parameters = prototype.getParameters();
            result += SizeOf.type_list(parameters);
        }

        return result;
    }

    public static int dataSizeClassDefs(DexModel model) {
        if (model == null)
            throw new IllegalArgumentException("model cannot be null");

        int result = 0;

        for (Clazz clazz : model.classPool(false)) {

            result += SizeOf.class_def_item(clazz);

        }

        return result;
    }
    
    /**
     * Calculates the amount of space that the values of the string pool will
     * occupy in the data section.
     * 
     * @param model
     *            the model to get strings from.
     * @return the number of bytes the string pool will occupy.
     */
    public static int stringDataBytes(DexModel model) {
        if (model == null)
            throw new IllegalArgumentException("model cannot be null");

        int result = 0;

        /* calculate the size for each string */
        for (String s : model.stringPool()) {
            result += SizeOf.string_data_item(s);
        }

        return result;

    }
    
    /**
     * Returns the size of a string_data_item that it will occupy when saved to
     * a data file.
     * <ul>
     * <li>uleb128 form of length.
     * <li>length of string bytes
     * <li>an extra byte of value 0, per Dalvik docs.
     * </ul>
     * 
     * @param s
     * @return
     */
    public static final int string_data_item(String s) {

        int result = 0;

        int length = s.length();
        result += Leb128Utils.unsignedLeb128Size(length);
        try {
            result += Mutf8.encode(s).length;
        } catch (UTFDataFormatException e) {
            throw new RuntimeException(e);
        }

        result++;

        return result;
    }

    /**
     * Inerprets the specified list as a list of type specifiers and returns the
     * size that the list will occupy in the data section when it is written. It
     * is assumed that all items are types. No checking is done.
     * <ul>
     * <li>uint list size
     * <li>ushort * list size bytes
     * </ul>
     * 
     * @param typeSignatures
     *            the type signatures.
     * @return the number of bytes the list will occupy when written.
     */
    public static final int type_list(List<String> typeSignatures) {

        int result = UINT * typeSignatures.size();
        for (String s : typeSignatures) {
            result += USHORT;
        }

        return result;

    }

    

    

    public static final int encoded_array_item(EncodedValue value) {
        return value.getBytes().length;
    }

    

    public static final int code(Code c) {
        int result = 0;
        if (c == null)
            return result;

        result += USHORT; // registers_size
        result += USHORT; // ins_size
        result += USHORT; // outs_size
        result += USHORT; // tries_size
        result += UINT; // debug_info_off
        result += UINT; // insns_size

        result += c.getInstructions().length * USHORT;

        if (c.getTries().length > 0) {

            if ((c.getInstructions().length % 2) == 1)
                result += USHORT; // padding

            // tries
            result += c.getTries().length * TRY_ITEM;

            // handlers

            // number of handlers
            result += Leb128Utils.unsignedLeb128Size(c.getCatchHandlers().length);
            for (CatchHandler ch : c.getCatchHandlers()) {

                // number of types caught for this handler.
                result += Leb128Utils.unsignedLeb128Size(ch.getTypeIndexes().length);

                for (int i = 0; i < ch.getTypeIndexes().length; i++) {
                    result += Leb128Utils.unsignedLeb128Size(ch.getTypeIndexes()[i]);
                    result += Leb128Utils.unsignedLeb128Size(ch.getAddresses()[i]);
                }

                if (ch.getCatchAllAddress() >= 0)
                    result += Leb128Utils.unsignedLeb128Size(ch.getCatchAllAddress());

            }

        }

        return result;
    }

   
    /**
     * uint - list size
     * for size elements ushort ushort uint uint
     * 
     * @return the number of bytes that a mapList occupies.
     */
    public static int mapList() {

        int result = UINT;
        result += TableOfContents.SECTION_COUNT * (USHORT + USHORT + UINT + UINT);

        return result;
    }

}
