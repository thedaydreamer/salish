package schilling.richard.dexlib.visitors;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * loads methodHash and classHash with hash and index information. Only methods
 * that are defined by classes within the DEX file are hashed.
 * 
 * @author rschilling
 * 
 */
public class ReloadDexDefinedHasesListener extends ProxyMethodListener {

	/**
	 * key = method.getMethodIndex(), value = method hash string.
	 */
	private SparseArray<String> methodHash = new SparseArray<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * schilling.richard.dexlib.visitors.ProxyMethodListener#shouldVisit(com
	 * .android.dx.io.ClassDef, com.android.dx.io.ClassData.Method,
	 * com.android.dx.io.MethodId, com.android.dx.io.ProtoId)
	 */
	@Override
	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * schilling.richard.dexlib.visitors.ProxyMethodListener#shouldVisit(com
	 * .android.dx.io.ClassDef)
	 */
	@Override
	public boolean shouldVisit(ClassDef cDef) {

		return true;
	}

	@Override
	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId, Code code) {

		return false;
	}

	private SparseArray<String> classHash = new SparseArray<String>();

	public SparseArray<String> getMethodHash() {
		return methodHash;
	}

	public SparseArray<String> getClassHash() {
		return classHash;
	}

	@Override
	public void onMethodFound(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {

		String hash = VisitorUtil.getHashString(
				mId.getDeclaringClassSignature(), mId.getName(), pId);

		if (methodHash.indexOfValue(hash) >= 0)
			throw new IllegalStateException("duplicate method hash");

		if (VisitorUtil.LOG_VISITORS)
			Log.d("LogId", String.format(
					"     loaded method hash %d (methodId %d): %s",
					method.getMethodIndex(), mId.getMethodIdIndex(), hash));

		methodHash.put(method.getMethodIndex(), hash);

	}

	@Override
	public void onClassDefFound(ClassDef cDef) {

		String hash = cDef.getSignature().intern();
		if (classHash.indexOfValue(hash) >= 0)
			throw new IllegalStateException("duplicate class hash");

		classHash.put(cDef.getTypeIndex(), hash);

		if (VisitorUtil.LOG_VISITORS)
			Log.d("LogId",
					String.format("loaded class hash %d: %s",
							cDef.getTypeIndex(), hash));

	}

	@Override
	public void onCodeFound(DexBuffer buffer, ClassDef cDef, Method method,
			MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

	}

	@Override
	public void onInstructionFound(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId, int address, DecodedInstruction instruction) {

	}

	@Override
	public void onCodeVisited(DexBuffer buffer, ClassDef cDef, Method method,
			MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

	}

	@Override
	public boolean doIterateCode() {
		return false;
	}
}
