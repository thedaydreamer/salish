
package com.android.dx.io.dexbuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import schilling.richard.util.FileUtils;

import com.android.dx.dex.SizeOf;

/**
 * A DexBuffer backed by a random access file. Not to be confused with DexFile,
 * which serves a different purpose.
 * 
 * @author rschilling
 */
public class DexBufferRandomAccessFile extends DexBuffer {

    /**
     * File semaphore is used to synchronize all operations against the
     * underlying file. All instances of DexBufferRandomAccessFile will be
     * synchronized against the same semaphore.
     */
    /* package */static final Object SEMAPHORE = new Object();

    public static final int BUFFER_SIZE = 1024;

    /* package */final File file;

    public DexBufferRandomAccessFile(File f) {
        synchronized (SEMAPHORE) {
            try {

                file = f;
                this.tableOfContents.readFrom(this);

            } catch (FileNotFoundException e) {

                throw new RuntimeException(e);

            } catch (IOException e) {

                throw new RuntimeException(e);

            }
        }
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        synchronized (SEMAPHORE) {
            FileUtils.copy(file, out);
        }

    }

    @Override
    public Section open(int position) {

        return new SectionFile(this, "section", position);

    }

    @Override
    public Section appendSection(int maxByteCount, String name) {
        throw new UnsupportedOperationException(
                "appending sections for file based sections is not supported");
    }

    @Override
    public int getLength() {

        synchronized (SEMAPHORE) {
            return (int) file.length();
        }

    }

    @Override
    public void noMoreSections() {
        throw new UnsupportedOperationException(
                "terminating sections for file based sections is not supported");

    }

    @Override
    public byte[] computeSignature() throws IOException {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
        int offset = SIGNATURE_OFFSET + SIGNATURE_SIZE;

        byte[] fileBytes = extractFileBytes();
        digest.update(fileBytes, offset, fileBytes.length - offset);

        return digest.digest();

    }

    private byte[] extractFileBytes() throws IOException {
        synchronized (SEMAPHORE) {
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int size = 0;
            // Read the entire resource into a local byte buffer.
            byte[] buffer = new byte[1024];
            while ((size = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, size);
            }

            return outputStream.toByteArray();
        }

    }

    @Override
    public int computeChecksum() throws IOException {

        byte[] fileBytes = extractFileBytes();

        Adler32 adler32 = new Adler32();
        int offset = CHECKSUM_OFFSET + CHECKSUM_SIZE;

        adler32.update(fileBytes, offset, fileBytes.length - offset);
        return (int) adler32.getValue();
    }

    @Override
    public void calculateAdler32() {
        try {

            byte[] fileBytes = extractFileBytes();

            // recalculate Adler32 and store that value
            Adler32 adler = new Adler32();
            int pos = 8 + SizeOf.UINT;
            int byteCount = getLength() - pos;
            adler.update(fileBytes, pos, byteCount);

        } catch (IOException ex) {

            throw new RuntimeException(ex);

        }

    }

}
