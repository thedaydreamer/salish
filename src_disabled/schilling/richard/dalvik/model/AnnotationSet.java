
package schilling.richard.dalvik.model;

import java.util.LinkedList;

import android.util.SparseArray;

import com.android.dx.dex.SizeOf;
import com.android.dx.util.ByteArrayAnnotatedOutput;

/**
 * annotation_set_item
 * 
 * @author rschilling
 */
public class AnnotationSet extends LinkedList<Annotation> {

    /**
     * Returns the byte array representation of this class in the form required
     * by a DEX file. The annotation_off table is filled with values based on
     * position start.
     * 
     * @param start
     *            the offset that this set will start at.
     * @return the byte array for this annotation set.
     */
    public byte[] asByteArray(int start) {
        int annotationCount = size();

        int annotation_off = start;
        annotation_off += SizeOf.UINT; // count of offset indexes
        annotation_off += annotationCount * SizeOf.UINT; // amount of spave for
                                                         // offset indexes.

        // annotaton_off = first position of an annotation item.

        byte[] result = new byte[SizeOf.annotation_set_item(this)];

        ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput(result);
        out.writeInt(annotationCount); // size

        /* fill the offset list with values */
        int[] offsets = new int[annotationCount];
        int i = 0;
        for (Annotation annotation : this) {

            offsets[i] = annotation_off;
            out.writeInt(annotation_off);
            annotation_off += SizeOf.annotation_item(annotation);

            i++;

        }

        /* now write out the annotations themselves. */
        i = 0;
        for (Annotation annotation : this) {
            if (offsets[i] != out.getCursor())
                throw new IllegalStateException("Annotations were not created correctly.");
            out.write(annotation.asByteArray());
            i++;
        }

        return result;

    }
}
