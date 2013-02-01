package schilling.richard.dalvik.vm.oo;

import java.io.UTFDataFormatException;
import java.util.List;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.EncodedValue;
import com.android.dx.util.Leb128Utils;
import com.android.dx.util.Mutf8;

public class ModelSizes {

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
	public static final int class_data_item(ObjectClass clazz) {
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
			result += Leb128Utils.unsignedLeb128Size(f.getAccessFlags());

		}

		idx = -1;
		for (Field f : instanceFields) {
			if (idx == -1)
				idx = fieldPool.indexOf(f);
			else
				idx = fieldPool.indexOf(f) - idx; // diff per docs

			result += Leb128Utils.unsignedLeb128Size(idx);
			result += Leb128Utils.unsignedLeb128Size(f.getAccessFlags());

		}

		idx = -1;
		for (Method m : directMethods) {
			if (idx == -1)
				idx = methodPool.indexOf(m);
			else
				idx = methodPool.indexOf(m) - idx; // diff per docs

			result += Leb128Utils.unsignedLeb128Size(idx);
			result += Leb128Utils.unsignedLeb128Size(m.getAccessFlags());

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
			result += Leb128Utils.unsignedLeb128Size(m.getAccessFlags());
			result += 5; // for code offset utilize maximum possible bytes for
							// Uleb128
		}

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

	public static final int encoded_array_item(EncodedValue value) {
		return value.getBytes().length;
	}

}
