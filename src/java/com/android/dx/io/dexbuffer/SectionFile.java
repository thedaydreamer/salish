
package com.android.dx.io.dexbuffer;

import java.io.IOException;

public class SectionFile extends Section {

    public SectionFile(DexBuffer buffer, String name, int position) {
        super(buffer, name, position, buffer.getLength());
        try {
            ((DexBufferRandomAccessFile) buffer).file.seek(position);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    @Override
    public void writeByte(int i) {
        try {
            ((DexBufferRandomAccessFile) buffer).file.write((byte) i);
        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    public void reset() {
        try {
            ((DexBufferRandomAccessFile) buffer).file.seek(start);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getPosition() {
        try {
            return (int) ((DexBufferRandomAccessFile) buffer).file.getFilePointer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte readByte() {
        try {
            return ((DexBufferRandomAccessFile) buffer).file.readByte();
        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }

    @Override
    public byte[] readByteArray(int length) {
        byte[] result = new byte[length];
        try {
            ((DexBufferRandomAccessFile) buffer).file.read(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void setPosition(int position) {
        try {
            ((DexBufferRandomAccessFile) buffer).file.seek(position);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void readByteArray(byte[] copied, int startPosition) {
        try {
            ((DexBufferRandomAccessFile) buffer).file.seek(startPosition);
            ((DexBufferRandomAccessFile) buffer).file.read(copied);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void write(byte[] bytes) {
        try {
            ((DexBufferRandomAccessFile) buffer).file.write(bytes);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void write(byte b) {
        try {
            ((DexBufferRandomAccessFile) buffer).file.write(b);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void write(short s) {

        try {
            ensureCapacity(2);
            ((DexBufferRandomAccessFile) buffer).file.write((byte) s);
            ((DexBufferRandomAccessFile) buffer).file.write((byte) (s >>> 8));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

   

}
