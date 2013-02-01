package schilling.richard.util;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility functions that deal with runtime.
 * 
 * @author rschilling
 * 
 */
public final class RuntimeUtil {

	private RuntimeUtil() {
	}

	/**
	 * Convenience wrapper for java.lang.Runtime.exec
	 * 
	 * @param progArray
	 *            the array containing the program to execute as well as any
	 *            arguments to the program.
	 * @returns the new Process object that represents the native process.
	 * @throws IOException
	 *             if the requested program can not be executed.
	 */
	public static final Process exec(ArrayList<String> progArray)
			throws IOException {

		return Runtime.getRuntime().exec(progArray.toArray(new String[0]));

	}

}
