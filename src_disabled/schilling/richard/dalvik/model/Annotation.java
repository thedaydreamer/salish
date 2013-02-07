
package schilling.richard.dalvik.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.EncodedValue;
import com.android.dx.util.ByteArrayAnnotatedOutput;

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

    public Annotation(DexModel model, String signature) {
        super(model, signature);
    }

    /**
     * annotation_item -> visibility
     */
    public byte visibility;

    /**
     * String pool name and encoded value.
     */
    public final Hashtable<String, EncodedValue> annotationValues = new Hashtable<String, EncodedValue>();

    /**
     * Converts this class to a byte array suitable for storing in a DexBuffer.
     */
    public byte[] asByteArray() {
        List<String> types = getModel().typePool();
        List<String> strings = getModel().stringPool();

        int len = SizeOf.annotation_item(this);
        byte[] result = new byte[len];

        ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput(result);

        out.writeByte(visibility);	// visibility
        out.writeUleb128(types.indexOf(getSignature())); // type_idx
        out.writeUleb128(annotationValues.size()); // size

        Set<Entry<String, EncodedValue>> entrySet = annotationValues.entrySet();
        for (Entry<String, EncodedValue> entry : entrySet) {
            int name_idx = strings.indexOf(entry.getKey());
            EncodedValue value = entry.getValue();

            out.writeUleb128(name_idx);
            out.write(value.getBytes());
        }

        if (out.getCursor() != result.length)
            throw new IllegalStateException("annotated output size calculation was incorrect.");

        return result;
    }

}
