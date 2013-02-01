package schilling.richard.android.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class AndroidUtil {

	/**
	 * Get the username
	 * 
	 * @return
	 * @throws IOException
	 */
	private static String getProcVersion() throws IOException {

		FileReader fReader = null;

		try {

			fReader = new FileReader(new File("/proc/version"));
			BufferedReader bufferedReader = new BufferedReader(fReader);
			return bufferedReader.readLine();

		} finally {
			if (fReader != null)
				fReader.close();

		}

	}

	public static String logcatDump(File outfile) throws IOException {

		ArrayList<String> commandLine = new ArrayList<String>();
		commandLine.add("logcat");
		commandLine.add("-d");
		commandLine.add("-f ");
		commandLine.add(outfile.getCanonicalPath());

		Process process = Runtime.getRuntime().exec(
				commandLine.toArray(new String[0]));
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

		return bufferedReader.readLine();

	}

	public static void logcatClear() throws IOException {
		
		ArrayList<String> commandLine = new ArrayList<String>();
		commandLine.add("logcat");
		commandLine.add("-c");

		Runtime.getRuntime().exec(commandLine.toArray(new String[0]));
	}

}
