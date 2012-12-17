
package com.android.dx.io.dexbuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;

import android.util.SparseArray;

import com.android.dx.dex.TableOfContents;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Field;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.merge.TypeList;

/**
 * A DexBuffer backed by a random access file. Not to be confused with DexFile,
 * which serves a different purpose.
 * 
 * @author rschilling
 */
public class DexBufferRandomAccessFile extends DexBuffer {
    private RandomAccessFile file;

    public DexBufferRandomAccessFile(File f) {
        try {
            file = new RandomAccessFile(f, "rw");
        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);

        }
    }

    

    @Override
    public void writeTo(OutputStream out) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeTo(File dexOut) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public TableOfContents getTableOfContents() {
        // TODO Auto-generated method stub
        return null;
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
