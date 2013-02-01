
package schilling.richard.util;

import java.io.File;
import java.io.IOException;

import schilling.richard.dexlib.util.Patterns;
import android.os.Environment;
import android.util.Log;

public final class FileSystemUtil {
    
    public static final String LOG_TAG = "FileSystemUtil";

    /**
     * Never instantiate.
     */
    private FileSystemUtil() {
    }

    /**
     * Return true if the file system where the specified directory resides is
     * case sensitive. This code is here just in case there is some day when
     * Android file systems become case in-sensitive. It is possible in theory
     * that the flash of the device may be case sensitive while the SD card is
     * not.
     * 
     * @return true if the file system is case sensitive.
     */
    public static boolean isCaseSensitive(File directory) throws IOException {

        if (!directory.isDirectory())
            throw new IOException("specified file must be a directory.");

        File f1, f2;
        int num = 1;

        /** find a file that does not exist. */
        do {
            f1 = new File(directory, "test." + num);
            f2 = new File(directory, "TEST." + num++);
        } while (f1.exists() || f2.exists());

        /* create the first file, then see if the second file is recognized. */
        try {

            if (!f1.createNewFile())
                throw new IOException(
                        "unable to create test file when checking for case sensitivity.");

            if (f2.exists())
                return false;

            if (f2.createNewFile())
                return true;
            else
                throw new IOException(
                        "unable to create test file when checking for case sensitivity.");

        } finally {
            f1.delete();
            f2.delete();
        }
    }

    public static boolean isReservedFileName(String className) {
        return Patterns.RESERVED_FILE_NAMES.matcher(className).matches();
    }

    public static boolean isExternalStorageAvailable() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        return mExternalStorageAvailable && mExternalStorageWriteable ? true
                : false;

    }

    /**
     * Create output directory and return the path.
     * 
     * @return the path to the output directory.
     */
    public static File createExternalOutputDirectory() throws IOException {

        /* create the output directory if it doesn't exist. */
        File outputDirectory = FinnrApp.getApp().getExternalOutputDirectory();
        if (!outputDirectory.exists()) {
            Log.i(LOG_TAG, "Creating external output directory " + outputDirectory);
            if (!outputDirectory.mkdirs()) {
                throw new IllegalStateException(
                        "Can't create the output directory " + outputDirectory);

            }
        }

        return outputDirectory;

    }

    public static File createInternalOutputDirectory() {

        /* create the output directory if it doesn't exist. */
        File outputDirectory = FinnrApp.getApp().getInternalOutputDirectory();
        if (!outputDirectory.exists()) {
            Log.i(LOG_TAG, "Creating internal output directory " + outputDirectory);
            if (!outputDirectory.mkdirs()) {
                throw new IllegalStateException(
                        "Can't create the output directory " + outputDirectory);

            }
        }

        return outputDirectory;

    }

}
