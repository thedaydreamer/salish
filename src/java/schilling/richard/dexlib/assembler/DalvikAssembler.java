package schilling.richard.dexlib.assembler;

import android.content.Context;

/**
 * This class converts the in-memory model to a valid DEX file. It can also
 * parse the assembler language defined by smali.
 * 
 * @author rschilling
 * 
 */
public class DalvikAssembler {

	/**
	 * Context used to get shared preferences and access to device resources.
	 */
	private Context context;

	public DalvikAssembler(Context ctx) {
		if (ctx == null)
			throw new IllegalArgumentException("context cannot be null.");

		context = ctx;

	}

}
