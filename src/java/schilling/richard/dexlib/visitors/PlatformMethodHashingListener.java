
package schilling.richard.dexlib.visitors;

import org.gnu.salish.visitors.DexFileListener;

import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.io.MethodId;
import com.android.dx.io.dexbuffer.DexBuffer;

/**
 * Creates a mapping of all methods that are on the platform to their equivalent
 * enhancer methods.
 * 
 * @author rschilling
 */
public class PlatformMethodHashingListener extends DexFileListener {

    public static final boolean LOG_PLATFORM_CLASSES = true;
    public static final String LOG_TAG = "PlatformHashing";

    /**
     * key contains the method index of a method and the hash for the unique
     * method. key = method id of platform method, value = replacement value
     */
    private SparseIntArray platformMethodLinks = new SparseIntArray();

    /**
     * The buffer that is being iterated through.
     */
    private DexBuffer buffer;

    public PlatformMethodHashingListener(DexBuffer buffer) {

        this.buffer = buffer;

    }

    public SparseIntArray getPlatformMethodLinkTable() {
        return platformMethodLinks;
    }

    @Override
    public boolean shouldVisit(MethodId methodId) {
        return true;
    }

    /**
     * Adds a method index to the platformMethodLinks table if the enhanced
     * version of the method is there. If the enhanced version of the method
     * call is present then it must have been part of the helper class list. By
     * including the mapping in the platformMethodLinks table, the function will
     * become mapped.
     */
    @Override
    public void onMethodIdFound(MethodId methodId) {

        String declaringClass = methodId.getDeclaringClassSignature();
        String enhancedClassName = VisitorUtil
                .getHelperClassSignature(declaringClass);
        
        MethodId mapped = buffer.findMethodId(enhancedClassName,
                methodId.getName(), methodId.getProtoIndex());
        
        if (mapped == null)
            return;
        
        if (LOG_PLATFORM_CLASSES)
            Log.d(LOG_TAG, String.format(
                    "found platform method protoid %d for %s",
                    methodId.getMethodIdIndex(), methodId.getProtoIndex(),
                    methodId.toString()));

        platformMethodLinks.put(methodId.getMethodIdIndex(),
                mapped.getMethodIdIndex());

    }
}
