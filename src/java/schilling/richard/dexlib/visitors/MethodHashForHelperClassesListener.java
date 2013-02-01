package schilling.richard.dexlib.visitors;

import java.util.List;

import schilling.richard.dexlib.proxy.ProxyMap;
import android.util.SparseArray;

import com.android.dx.io.MethodId;

/**
 * loads methodHash and classHash with hash and index information. Only methods
 * that are defined by classes within the DEX file are hashed.
 * 
 * @author rschilling
 * 
 */
public class MethodHashForHelperClassesListener extends ProxyMethodListener {

	private List<String> helperClassList;
	private ProxyMap classMap;
	private ProxyMap methodMap;
	private SparseArray methodHashes;

	public MethodHashForHelperClassesListener(ProxyMap classMap,
			ProxyMap methodMap, List<String> helperClassList,
			SparseArray methodHashes) {

		this.helperClassList = helperClassList;
		this.classMap = classMap;
		this.methodMap = methodMap;
		this.methodHashes = methodHashes;

	}

	@Override
	public boolean shouldVisit(MethodId methodId) {

		String classSignature = methodId.getDeclaringClassSignature().intern();

		if (helperClassList.contains(classSignature))
			return false;

		// check to see if the helper class is declared

		String proxyClassSignature = VisitorUtil
				.getHelperClassSignature(classSignature);

		if (!helperClassList.contains(proxyClassSignature))
			return false;

		// check to see if the a

		return true;

	}

	@Override
	public void onMethodIdFound(MethodId methodId) {
		String classSignature = methodId.getDeclaringClassSignature();
		String proxyClassSignature = VisitorUtil
				.getHelperClassSignature(classSignature);

		// store class if necessary
		if (!classMap.containsKey(classSignature))
			classMap.put(classSignature, proxyClassSignature);

		// store method if necessary - should be not stored yet.

		/*
		 * One of the rules of writing a proxy method: only <init> direct
		 * methods and virtual methods can be written. A check will be made for
		 * a valid method, however.
		 */
		String methodName = methodId.getName();

		String methodSignature = VisitorUtil.getHashString(classSignature,
				methodName, methodId.getPrototypeId());

		if (methodMap.contains(methodSignature))
			return; // already mapped

		if (methodHashes.indexOfValue(methodSignature) < 0)
			throw new IllegalStateException(String.format(
					"method hash not found: %s", methodSignature));

		String methodProxySignature = VisitorUtil.getHashString(
				proxyClassSignature, methodId.getName(),
				methodId.getPrototypeId());

		/*
		 * The method hash must be present
		 */
		if (methodHashes.indexOfValue(methodProxySignature) < 0)
			return; // the method has no proxy defined - won't be linked.

		methodMap.put(methodSignature, methodProxySignature);

	}
}
