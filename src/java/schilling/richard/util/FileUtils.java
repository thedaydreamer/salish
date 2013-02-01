/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * As per the Apache license requirements, this file has been modified
 * from its original state.
 *
 * Such modifications are Copyright (C) 2010 Ben Gruver, and are released
 * under the original license
 */

package schilling.richard.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import android.util.Log;

/**
 * File I/O utilities.
 */
public final class FileUtils {

    public static final String LOG_TAG = "Finnr.FileUtils";
    public static final boolean DO_LOG = false;
    public static final boolean WARN_RMDIR = false;
    public static final int INPUT_BUFFER_LENGTH = 1024;

    /**
     * This class is uninstantiable.
     */
    private FileUtils() {
        // This space intentionally left blank.
    }

    /**
     * Reads the named file, translating {@link IOException} to a
     * {@link RuntimeException} of some sort.
     * 
     * @param fileName non-null; name of the file to read
     * @return non-null; contents of the file
     */
    public static byte[] readFile(String fileName) throws IOException {
        File file = new File(fileName);
        return readFile(file);
    }

    /**
     * Reads the given file, translating {@link IOException} to a
     * {@link RuntimeException} of some sort.
     * 
     * @param file non-null; the file to read
     * @return non-null; contents of the file
     */
    public static byte[] readFile(File file) throws IOException {
        return readFile(file, 0, -1);
    }

    /**
     * Reads the specified block from the given file, translating
     * {@link IOException} to a {@link RuntimeException} of some sort.
     * 
     * @param file non-null; the file to read
     * @param offset the offset to begin reading
     * @param length the number of bytes to read, or -1 to read to the end of
     *            the file
     * @return non-null; contents of the file
     */
    public static byte[] readFile(File file, int offset, int length)
            throws IOException {
        if (!file.exists()) {
            throw new RuntimeException(file + ": file not found");
        }

        if (!file.isFile()) {
            throw new RuntimeException(file + ": not a file");
        }

        if (!file.canRead()) {
            throw new RuntimeException(file + ": file not readable");
        }

        long longLength = file.length();
        int fileLength = (int) longLength;
        if (fileLength != longLength) {
            throw new RuntimeException(file + ": file too long");
        }

        if (length == -1) {
            length = fileLength - offset;
        }

        if (offset + length > fileLength) {
            throw new RuntimeException(file + ": file too short");
        }

        FileInputStream in = new FileInputStream(file);

        int at = offset;
        while (at > 0) {
            long amt = in.skip(at);
            if (amt == -1) {
                throw new RuntimeException(file + ": unexpected EOF");
            }
            at -= amt;
        }

        byte[] result = readStream(in, length);

        in.close();

        return result;
    }

    public static byte[] readStream(InputStream in, int length)
            throws IOException {
        byte[] result = new byte[length];
        int at = 0;

        while (length > 0) {
            int amt = in.read(result, at, length);
            if (amt == -1) {
                throw new RuntimeException("unexpected EOF");
            }
            at += amt;
            length -= amt;
        }

        return result;
    }

    public static byte[] readStreamComplete(InputStream in) throws IOException {
        byte[] inputBuffer = new byte[1024];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = -1;
        while ((bytesRead = in.read(inputBuffer)) != -1)
            baos.write(inputBuffer);

        return baos.toByteArray();
    }

    /**
     * Creates a file and, if necessary, all the parent directories for that
     * file.
     * 
     * @param f the file to create
     * @throws IOException if the operation fails.
     */
    public static void createFile(File f) throws IOException {
        File parent = f.getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs())
                throw new IOException("Unable to create directory "
                        + parent.toString());

            else if (DO_LOG)
                Log.i(LOG_TAG, "Created directory " + parent.getCanonicalPath());

        }

        if (!f.exists()) {
            if (!f.createNewFile())
                throw new IOException("Unable to create file " + f.toString());
            else if (DO_LOG)
                Log.i(LOG_TAG, "Created file " + f.getCanonicalPath());
        }
    }

    /**
     * Finds the next available file name. If fileName exists, then numbers are
     * appended to fileName until a file is found.
     * 
     * @param dir the directory to look in.
     * @return the next available file.
     * @throws IOException if the search fails.
     */
    public static File getNextAvailableFile(File dir, String fileName,
            String extension) throws IOException {

        return new File(dir, getNextAvailableFileName(dir, fileName, extension));

    }

    /**
     * Remove directory, but remove all children first. If the operation could
     * not be completed than an IOException with the path of the deleted file
     * name is returned.
     * 
     * @param dir the directory to remove.
     * @return true if the remove succeeded.
     */
    public static void rmdir(File dirOrFile) throws IOException {
        if (!dirOrFile.exists()) {
            if (WARN_RMDIR)
                Log.w(LOG_TAG, "File does not exist");
            return;
        }

        if (dirOrFile.isDirectory()) {
            String[] children = dirOrFile.list();
            for (int i = 0; i < children.length; i++) {
                rmdir(new File(dirOrFile, children[i]));
            }
        }

        if (!dirOrFile.delete())
            throw new IOException(String.format("unable to delete file %s",
                    dirOrFile.getCanonicalPath()));
    }

    /**
     * Returns the name of the next available file in a directory.
     * 
     * @param dir the directory to look in.
     * @param fileName the first choice of file name
     * @param extension the extension to apply
     * @return the next available file name
     * @throws IOException if the search fails.
     */
    public static String getNextAvailableFileName(File dir, String fileName,
            String extension) throws IOException {
        if (dir == null | fileName == null || extension == null)
            throw new IllegalArgumentException("no parameter may be null");

        if (!dir.exists())
            throw new IOException(String.format("directory %s does not exist",
                    dir.getCanonicalPath()));

        File result = new File(dir, fileName + extension);

        int count = 1;
        while (result.exists()) {

            result = new File(dir, fileName + "_" + count + extension);
            count++;

        }

        return result.getName();
    }

    /**
     * Copies one file to another.
     * 
     * @param source the file to copy
     * @param dest the destination file.
     * @throws IOException
     */
    public static void copy(File source, File dest) throws IOException {

        // For Overwrite the file.
        OutputStream out = new FileOutputStream(dest);
        copy(source, out);
        
        out.close();

    }

    public static void copy(File source, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(source);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();

    }

    /**
     * Creates a backup copy of the input file.
     * 
     * @throws IOException if the backup fails.
     */
    public static void copy(File from, File to, boolean delete)
            throws IOException {

        if (to.exists()) {
            if (delete)
                to.delete();
            else
                throw new IOException("destination file exists.");
        }

        to.createNewFile();

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(from).getChannel();
            destination = new FileOutputStream(to).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {

            if (source != null) {

                source.close();

            }
            if (destination != null) {
                destination.close();
            }
        }

        Log.i(LOG_TAG, from.getName() + " backed up to " + to.getName());

    }
}
