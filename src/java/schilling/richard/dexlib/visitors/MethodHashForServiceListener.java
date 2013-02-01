package schilling.richard.dexlib.visitors;

import android.content.Context;
import android.content.Intent;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;

/**
 * loads methodHash and classHash with hash and index information. Only methods
 * that are defined by classes within the DEX file are hashed.
 * 
 * @author rschilling
 * 
 */
public class MethodHashForServiceListener extends ProxyMethodListener {
	private Context ctx;

	public MethodHashForServiceListener(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {

		return true;

	}

	@Override
	public void onClassDefFound(ClassDef cDef) {
		// M-T
	}

	@Override
	public boolean shouldVisit(ClassDef cDef) {
		return true;
	}

	@Override
	public void onMethodFound(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {
		String hashString = VisitorUtil.getHashString(
				mId.getDeclaringClassSignature(), mId.getName(), pId);

		MethodHash mh = new MethodHash(hashString);

		Intent i = new Intent(FinnrService.LOG_SERVICE);
		i.putExtra(FinnrService.EXTRA_NEW_HASH, mh);
		ctx.startService(i);

	}

}
