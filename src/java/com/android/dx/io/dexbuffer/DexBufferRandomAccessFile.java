
package com.android.dx.io.dexbuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.android.dx.dex.TableOfContents;

/**
 * A DexBuffer backed by a random access file. Not to be confused with DexFile,
 * which serves a different purpose.
 * 
 * @author rschilling
 */
public class DexBufferRandomAccessFile extends DexBuffer {
    public static final int BUFFER_SIZE = 1024;
    protected RandomAccessFile file;

    public DexBufferRandomAccessFile(File f) {
        try {
            file = new RandomAccessFile(f, "rw");
        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);

        }
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        synchronized (file) {
            byte[] buffer = new byte[BUFFER_SIZE];
            long savedPos = file.getFilePointer();
            file.seek(0);
            int bytesread = 0;
            while ((bytesread = file.read(buffer)) > 0) {
                out.write(buffer, 0, bytesread);

            }
            file.seek(savedPos);
        }

    }

    @Override
    public void writeTo(File dexOut) throws IOException {

        writeTo(new FileOutputStream(dexOut));

    }

    @Override
    public Section open(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Section appendSection(int maxByteCount, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void noMoreSections() {
        // TODO Auto-generated method stub

    }

    @Override
    public byte[] computeSignature() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int computeChecksum() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void calculateAdler32() {
        // TODO Auto-generated method stub

    }

}
