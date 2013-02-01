package schilling.richard.dexlib.visitors;

import android.util.SparseArray;

import com.android.dx.io.MethodId;

/**
 * loads methodHash and classHash with hash and index information. All methods
 * and classes are referenced, even if they aren't defined in the buffer (but
 * only referenced).
 * 
 * @author rschilling
 * 
 */
public class ReloadAllReferencedHashesListener extends ProxyMethodListener {

	/**
	 * key = method.getMethodIndex(), value = method hash string.
	 */
	private SparseArray<String> methodHash = new SparseArray<String>();

	private SparseArray<String> classHash = new SparseArray<String>();

	public SparseArray<String> getMethodHash() {
		return methodHash;
	}

	public SparseArray<String> getClassHash() {
		return classHash;
	}

	@Override
	public boolean shouldVisit(MethodId methodId) {
		return true;
	}

	@Override
	public void onMethodIdFound(MethodId methodId) {

		String definingClass = methodId.getDeclaringClassSignature().intern();
		if (classHash.indexOfValue(definingClass) < 0)
			classHash.put(methodId.getDeclaringClassIndex(), definingClass);

		String methodHashString = VisitorUtil.getHashString(definingClass,
				methodId.getName(), methodId.getPrototypeId());

		if (methodHash.indexOfValue(methodHashString) > 0)
			throw new IllegalStateException(String.format(
					"method hash already exists: %s", methodHash));

		methodHash.put(methodId.getMethodIdIndex(), methodHashString);

	}

}
