package schilling.richard.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class SystemUtil {
	public static final String PATH_PROC_VERSION = "/proc/version";
	public static final File FILE_PROC_VERSION = new File(PATH_PROC_VERSION);

	private SystemUtil() {
	}

	/**
	 * Returns the user name from the version file on the system.
	 * 
	 * @return the user name
	 * @throws IOException
	 *             if the file cannot be read.
	 */
	public final static String getUname() throws IOException {

		FileReader fReader = null;
		BufferedReader bufferedReader = null;

		String result = null;

		try {

			fReader = new FileReader(FILE_PROC_VERSION);
			bufferedReader = new BufferedReader(fReader);
			result = bufferedReader.readLine();

		} finally {
			if (fReader != null) {
				try {
					fReader.close();
				} catch (IOException e) {
				}
			}

		}

		return result;

	}
}
