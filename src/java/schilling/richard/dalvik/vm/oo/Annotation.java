package schilling.richard.dalvik.vm.oo;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.android.dx.io.ClassDef;
import com.android.dx.io.EncodedValue;

/**
 * <h2>annotation_item</h2>
 * <ul>
 * <li>referenced from annotation_set_item
 * <li>appears in the data section
 * <li>alignment: none (byte-aligned)
 * </ul>
 * <H3>yields</h3> <h2>encoded_annotation</h2>
 * <ul>
 * <li>ubyte - visibility from this class
 * <li>uleb128 - type_idx derived from the superclass Clazz.classType.
 * <li>uleb128 - size derived from annotationValues.size.
 * <li>elements derived from annotationValues.
 * </ul>
 * 
 * @author rschilling
 */
public class Annotation extends Clazz {

	public Annotation(DexModel model, ClassDef classDef, int idx) {
		super(model, classDef, idx);
	}

	/**
	 * annotation_item -> visibility
	 */
	public byte visibility;

	/**
	 * String pool name and encoded value.
	 */
	public final Hashtable<String, EncodedValue> annotationValues = new Hashtable<String, EncodedValue>();

	@Override
	public int compareTo(Clazz another) {
		if (!(another instanceof Annotation))
			return 1;

		Annotation annotation = (Annotation) another;

		if (visibility != annotation.visibility)
			return (visibility - annotation.visibility);

		if (annotationValues.size() != annotation.annotationValues.size())
			return annotationValues.size() - annotation.annotationValues.size();

		Set<String> thisKeys = annotationValues.keySet();
		Set<String> anotherKeys = annotation.annotationValues.keySet();

		List<String> thisKeysList = new ArrayList(thisKeys);
		java.util.Collections.sort(thisKeysList);

		List<String> anotherKeysList = new ArrayList(anotherKeys);
		java.util.Collections.sort(anotherKeysList);

		for (int i = 0; i < thisKeysList.size(); i++) {

			String thisKey = thisKeysList.get(i);
			String anotherKey = anotherKeysList.get(i);

			if (!thisKey.equals(anotherKey))
				return thisKey.compareTo(anotherKey);
		}

		return 0;
	}

}
