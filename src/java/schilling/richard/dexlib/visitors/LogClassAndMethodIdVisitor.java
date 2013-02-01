package schilling.richard.dexlib.visitors;

import org.gnu.salish.visitors.DexFileListener;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.instructions.DecodedInstruction;

public class LogClassAndMethodIdVisitor extends DexFileListener {

	private static final String LOG_TAG = "LogId";

	@Override
	public void onClassDefFound(ClassDef cDef) {
		if (VisitorUtil.LOG_VISITORS)
			Log.d(LOG_TAG, String.format("class id %d :  hash %d %s",
					cDef.getTypeIndex(), cDef.getSignature().hashCode(),
					cDef.getSignature()));

	}

	@Override
	public boolean shouldVisit(ClassDef cDef) {
		return true;
	}

	@Override
	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {

		return true;
	}

	@Override
	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId, Code code) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMethodFound(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {

		String classSignature = cDef.getSignature();
		String methodName = mId.getName();

		String uniqueString = VisitorUtil.getHashString(
				mId.getDeclaringClassSignature(), mId.getName(), pId);
		if (VisitorUtil.LOG_VISITORS)
			Log.d(LOG_TAG, String.format(
					"     method id %d :  hash %s %s in %s",
					method.getMethodIndex(), uniqueString, methodName,
					classSignature));

		// sanity
		if (!mId.getDeclaringClassSignature().equals(cDef.getSignature()))
			throw new IllegalArgumentException("declaring class mismatch");

	}

	@Override
	public boolean doIterateCode() {
		// TODO Auto-generated method stub
		return false;
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

}
