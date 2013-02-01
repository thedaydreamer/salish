package schilling.richard.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Utility functions to work with Android's logcat.
 * 
 * @author rschilling
 * 
 */
public final class LogCat {

	private LogCat() {
	}

	/**
	 * Programmatically runs "logcat -c" on the command line. The
	 * {@link Process} that was used to run the command is returned.
	 */
	public final static Process clearLogcat() throws IOException {

		ArrayList<String> commandLine = new ArrayList<String>();
		commandLine.add("logcat");
		commandLine.add("-c");

		return RuntimeUtil.exec(commandLine);

	}

	/**
	 * Collects logcat information. Optionally clears the log with 'logcat -c'
	 * before reading the log.
	 * 
	 * @author rschilling
	 * 
	 */
	public abstract static class CollectLogThread extends Thread {

		/**
		 * A variable that can be set to false whenever
		 */
		private boolean running = true;
		private boolean clearFirst = false;

		public CollectLogThread(boolean clear) {

			clearFirst = clear;

		}

		/**
		 * Tells the thread to stop collecting log data. The thread will be
		 * unusable after this function is called.
		 */
		public void stopRunning() {
			running = false;
		}

		/**
		 * Called whenever a new line from the log is read.
		 * 
		 * @param line
		 */
		public abstract void parseLine(String line);

		/**
		 * Called if an exception is thrown while reading log lines.
		 * 
		 * @param ex
		 */
		public abstract void handleException(IOException ex);

		@Override
		public void run() {

			try {

				/*
				 * Clear logcat
				 */
				if (clearFirst)
					clearLogcat();

				/*
				 * Capture logcat
				 */
				ArrayList<String> commandLine = new ArrayList<String>();
				commandLine.add("logcat");

				Process process = RuntimeUtil.exec(commandLine);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));

				while (running) {
					parseLine(bufferedReader.readLine());
				}

			} catch (IOException e) {
				handleException(e);
			}

		}
	}

	public static void logMasterHeader(Context ctx, FileWriter fWriter)
			throws IOException {

		ConnectivityManager connManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = connManager.getActiveNetworkInfo();
		NetworkInfo.State state = network.getState();
		TelephonyManager manager = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);

		fWriter.write("===================\n");
		fWriter.write("App Version: <detecting AUT version not supported yet.>\n");

		// manufacturer, model, "OS Version:" Release
		fWriter.write("Device: ");
		fWriter.write(android.os.Build.MANUFACTURER);
		fWriter.write(" ");
		fWriter.write(android.os.Build.MODEL);
		fWriter.write(" ");
		fWriter.write(android.os.Build.VERSION.RELEASE);
		fWriter.write("\n");

		// os.version
		Properties props = System.getProperties();
		fWriter.write("Kernel Version: ");
		fWriter.write(props.getProperty("os.version"));
		fWriter.write("\n");

		// Build Information: time, "Carrier:"
		// Telephony.network
		// operator name
		String uName = SystemUtil.getUname();
		if (uName == null)
			uName = "unable to read uname";
		fWriter.write(uName);
		fWriter.write(" ");
		fWriter.write("Carrier: ");
		fWriter.write(manager.getNetworkOperator());
		fWriter.write("\n");

		// "RadioType:" Network.Type: subtype
		fWriter.write("RadioType: ");
		fWriter.write(network == null ? "not available" : network
				.getSubtypeName());
		fWriter.write("\n");
		// masterLogFileWriter.write("RadioType: CDMA\n");

		// "ConnectionType: " Network.Type Name (network
		// state:
		// connected)
		fWriter.write("ConnectionType: ");
		fWriter.write(network == null ? "not available" : network.getTypeName());
		if (state != null) {
			fWriter.write(" (");
			fWriter.write(state.name());
			fWriter.write(")");
		}
		fWriter.write("\n");

		fWriter.write("\n\n\n");

	}

}
