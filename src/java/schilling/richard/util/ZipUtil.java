
package schilling.richard.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

/**
 * A class with utility functions that support ZIP compression and decompression
 * operations.
 * 
 * @author rschilling
 */
public final class ZipUtil {

    public static final String LOG_TAG = "ZipUtil";
    public static final String kNoCompressExt[] = {
            ".ogg"
            // ".jpg", ".jpeg", ".png", ".gif",
            // ".wav", ".mp2", ".mp3", ".ogg", ".aac",
            // ".mpg", ".mpeg", ".mid", ".midi", ".smf", ".jet",
            // ".rtttl", ".imy", ".xmf", ".mp4", ".m4a",
            // ".m4v", ".3gp", ".3gpp", ".3g2", ".3gpp2",
            // ".amr", ".awb", ".wma", ".wmv"
    };

    private ZipUtil() {

    }

    /**
     * Returns true if the file can be compressed.
     * 
     * @param pathName the full path to compress
     * @return true if the file can be compressed.
     */
    public static boolean okayToCompress(String pathName) {
        for (String ext : kNoCompressExt) {
            if (pathName.endsWith(ext))
                return false;
        }
        return true;

    }

    public static void compressDirectory(File dir, File dest,
            AtomicBoolean whileTrue) throws IOException {
        if (dir == null || dest == null)
            throw new IllegalArgumentException("no parameter may be null");

        Log.v(LOG_TAG,
                String.format("Compressing directory %s to file %s",
                        dir.getCanonicalPath(), dest.getCanonicalPath()));
        FileOutputStream fos = new FileOutputStream(dest);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(fos));

        FinnrApp.updateProgressBarMessage("Compressing final APK");
        FinnrApp.incrementProgressBarProgress(1);

        String topLevelPath = dir.getCanonicalPath();
        if (!topLevelPath.endsWith("/"))
            topLevelPath += "/";

        compressDirectory(out, topLevelPath, dir, whileTrue);
        out.close();
    }

    private static void compressDirectory(ZipOutputStream zip,
            String topLevelPath, File inputDir, AtomicBoolean whileTrue)
            throws IOException {
        if (inputDir == null || zip == null)
            return;

        if (!inputDir.exists()) {
            Log.w(LOG_TAG, "Log directory does not exist");
            return;
        }

        if (!inputDir.isDirectory())
            throw new IllegalArgumentException(inputDir
                    + " is not a directory.");

        File[] inputFiles = inputDir.listFiles();
        if (inputFiles == null || inputFiles.length == 0)
            return;

        byte[] buffer = new byte[FileUtils.INPUT_BUFFER_LENGTH];

        for (int i = 0; i < inputFiles.length; i++) {

            if (inputFiles[i].isDirectory()) {

                // recurse
                compressDirectory(zip, topLevelPath, inputFiles[i], whileTrue);

            } else {
                FileInputStream fi = new FileInputStream(inputFiles[i]);
                BufferedInputStream origin = new BufferedInputStream(fi,
                        FileUtils.INPUT_BUFFER_LENGTH);

                ByteArrayBuffer fileData = new ByteArrayBuffer(FileUtils.INPUT_BUFFER_LENGTH);
                int count;
                while ((count = origin.read(buffer, 0,
                        FileUtils.INPUT_BUFFER_LENGTH)) != -1) {
                    fileData.append(buffer, 0, count);

                }
                origin.close();
                origin = null;

                byte[] fileDataArray = fileData.toByteArray();
                fileData = null;

                System.gc(); // hint at GC.

                // fileData now contains all the file contents

                String unzipDirPath = inputDir.getCanonicalPath();
                if (!unzipDirPath.endsWith("/"))
                    unzipDirPath += "/";

                // inputFiles[i] path is always longer than unzipDirPath;
                // unzipDirPath always is at front of string.
                String entryName = inputFiles[i].getCanonicalPath().substring(
                        topLevelPath.length());

                ZipEntry ze = new ZipEntry(entryName);

                boolean ok = okayToCompress(entryName);
                int method = ok ? ZipEntry.DEFLATED : ZipEntry.STORED;
                ze.setMethod(method);
                if (method == ZipEntry.STORED) {
                    long sz = inputFiles[i].length();
                    ze.setCompressedSize(sz);
                    ze.setSize(sz);
                    CRC32 crc = new CRC32();
                    crc.update(fileDataArray);
                    ze.setCrc(crc.getValue());

                }

                // zip.setMethod(method);

                String logString = "Adding: " + entryName + " ";
                if (!ok)
                    logString += "(uncompressed) ";
                logString += Integer.toString(fileDataArray.length) + " bytes";

                Log.v(LOG_TAG, logString);

                zip.putNextEntry(ze);
                zip.write(fileDataArray, 0, fileDataArray.length);

                zip.closeEntry();

            }

        }
    }
}
