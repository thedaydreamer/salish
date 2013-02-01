package schilling.richard.util;

import java.util.LinkedList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * Utility functions related to the ActivityManager on Android. This will help
 * simplify calling code and reduce the potential for mistakes by keeping
 * working blocks of utility code in one place to manage.
 * 
 * @author rschilling
 * 
 */
public final class ActivityManagerUtil {

	private ActivityManagerUtil() {
	}

	/**
	 * Returns the process number associated with a package name.
	 * 
	 * @return the process number for the application under test or null if not
	 *         found.
	 * @author rschilling
	 * 
	 */
	public static Integer getProcessNumber(Context ctx, String packageName) {

		List<RunningAppProcessInfo> procInfos = getRunningAppProcesses(ctx);

		if (procInfos == null)
			return null;

		for (RunningAppProcessInfo rapi : procInfos) {
			if (rapi.processName.equals(packageName))
				return new Integer(rapi.pid);
		}
		return null;
	}

	/**
	 * Returns the process number associated with a package name.
	 * 
	 * @return the process number for the application under test or null if not
	 *         found.
	 * @author rschilling
	 * 
	 */
	public static List<String> getRunningProcessNames(Context ctx) {

		List<String> result = new LinkedList<String>();

		List<RunningAppProcessInfo> procInfos = getRunningAppProcesses(ctx);

		if (procInfos != null) {
			for (RunningAppProcessInfo rapi : procInfos) {
				result.add(rapi.processName);
			}
		}

		return result;
	}

	/**
	 * Returns the process number associated with a package name.
	 * 
	 * @return the process number for the application under test or null if not
	 *         found.
	 * @author rschilling
	 * 
	 */
	public static List<RunningAppProcessInfo> getRunningAppProcesses(Context ctx) {

		ActivityManager actvityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);

		return actvityManager.getRunningAppProcesses();
	}

}
