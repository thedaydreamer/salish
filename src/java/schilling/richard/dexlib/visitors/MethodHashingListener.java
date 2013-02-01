
package schilling.richard.dexlib.visitors;

import java.util.LinkedList;
import java.util.List;

import org.gnu.salish.visitors.DexFileListener;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dexlib.proxy.ProxyMap;
import android.util.Log;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Links target methods and class hashes with their proxies. Only the methods
 * that are going to be re-linked will be added to the hashtables. Here are the
 * hashtable that are build:
 * <ul>
 * <li>getClassLinkTable: a map of class hashes that proxies are created for
 * (key) to proxy classes. The hashes are the hash of the type signature of each
 * class.
 * <li>getMethodLinkTable: a map met method hashes that proxies are created for
 * (key) to proxy methods. The hashes are created from the class name, method
 * name, and parameter list.
 * <li>getChosenMethods: the list of methods that are chosen to have proxies
 * created for them.
 * </ul>
 * getClassLinkTable and getMethodLinkTable relate in this way:
 * getClassLinkTable contains only the hashes of class signatures, and
 * getMethodLinkTable contains the complete method hash. For each item in
 * getMethodLinkTable and getChosenMethods, there must be a class in
 * getClassLinkTable that identifies a class.
 * 
 * @author rschilling
 */
public class MethodHashingListener extends DexFileListener {

    public static final String LOG_TAG = "MethodHashingListener";
    public static final boolean LOG_HASHING = true;

    /**
     * The list of classes that won't get hashed. These classes are skipped over
     * during processing so they are left in the destination file as is.
     */
    public List<String> excludedClasses = null;

    /**
     * The list of packages that won't get hashed. Classes that begin with these
     * package names are skipped over during processing so they are left in the
     * destination file as is.
     */
    public List<String> excludedPackages = null;

    /**
     * key contains the class index (type index) of a class and the hash for the
     * class type.
     */
    private ProxyMap classLinks = new ProxyMap();

    /**
     * key contains the method index of a method and the hash for the unique
     * method.
     */
    private ProxyMap methodLinks = new ProxyMap();

    /**
     * The list of methods that are chosen to have proxies created for.
     */
    private List<Method> chosenMethods = new LinkedList<Method>();

    public ProxyMap getClassLinkTable() {
        return classLinks;
    }

    public ProxyMap getMethodLinkTable() {
        return methodLinks;
    }

    public List<Method> getChosenMethods() {
        return chosenMethods;
    }

    @Override
    public void onMethodFound(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId) {

        String methodName = mId.getName();
        String className = cDef.getSignature();

        String originalHash = VisitorUtil.getHashString(
                mId.getDeclaringClassSignature(), mId.getName(), pId);
        String linkedHash = VisitorUtil
                .getHashString(VisitorUtil.getProxySignature(mId
                        .getDeclaringClassSignature()), mId.getName(), pId);

        if (methodLinks.containsKey(originalHash))
            throw new IllegalStateException(String.format(
                    "method hash already present : %s %s in %s", originalHash,
                    methodName, className));

        if (methodLinks.containsValue(linkedHash))
            throw new IllegalStateException("proxy method hash already present");

        methodLinks.put(originalHash, linkedHash);
        if (LOG_HASHING)
            Log.d(Enhance.LOG_TAG, String.format(
                    "     method:proxy method mapping stored %s:%s",
                    originalHash, linkedHash));
        chosenMethods.add(method);

    }

    @Override
    public void onClassDefFound(ClassDef cDef) {
        // add to the class type hash array.

        String originalHash = cDef.getSignature().intern();
        String linkedHash = VisitorUtil.getProxySignature(originalHash);

        if (classLinks.containsKey(originalHash))
            throw new IllegalStateException("class hash already present as key");

        if (classLinks.containsValue(linkedHash))
            throw new IllegalStateException(
                    "class hash already present as value");

        classLinks.put(originalHash, linkedHash);
        if (VisitorUtil.LOG_VISITORS)
            Log.d(Enhance.LOG_TAG, String.format(
                    "class:proxy class mapping stored %s:%s", originalHash,
                    linkedHash));
    }

    @Override
    public void onCodeFound(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {
        // M-T

    }

    @Override
    public void onInstructionFound(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, int address, DecodedInstruction instruction) {
        // M-T

    }

    @Override
    public void onCodeVisited(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {
        // M-T

    }

    @Override
    public boolean doIterateCode() {
        return false;
    }

    @Override
    public boolean shouldVisit(ClassDef cDef) {

        String signature = cDef.getSignature().intern();
        if (excludedPackages != null) {
            for (String prefix : excludedPackages) {
                if (signature.startsWith(prefix)) {
                    Log.d(LOG_TAG, String.format(
                            "Excluding class %s because its starts with package %s.", signature,
                            prefix));
                    return false;
                }
            }
        }
        if (excludedClasses != null
                && excludedClasses.contains(signature)) {
            Log.d(LOG_TAG, String.format(
                    "Excluding class %s because it's in the list of excluded classes.", signature));
            return false;
        }

        // must not be an array class
        if (cDef.dvmIsArrayClass())
            return false;

        int accessFlags = cDef.getAccessFlags();

        // taken from DexMaker.declare

        if ((accessFlags & Enhance.UNSUPPORTED_CLASS_FLAGS) != 0) {

            Log.d(LOG_TAG, String.format(
                    "Excluding class %s because of it access flags.", signature));
            return false;
        }

        return true;
    }

    @Override
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId) {

        if (method.getCodeOffset() == 0)
            return false;

        int methodAccessFlags = method.getAccessFlags();
        if ((methodAccessFlags & Enhance.UNSUPPORTED_METHOD_FLAGS) != 0)
            return false;

        String methodName = mId.getName();

        if (methodName.equals("<clinit>"))
            return false;

        return true;
    }

    @Override
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, Code code) {

        return false;
    }

}
