
package com.android.dx.io.dexbuffer;

/**
 * A section of a DexBufferArray object.
 * 
 * @author rschilling
 */
public class SectionArrayed extends Section {

    protected int position;

    SectionArrayed(DexBufferArray buffer, String name, int startPosition, int limit) {
        super(buffer, name, startPosition, limit);
        this.position = startPosition;
    }

    SectionArrayed(DexBufferArray buffer, int startPosition) {
        this(buffer, "section", startPosition, buffer.getLength());
    }

    @Override
    public void reset() {
        position = start;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public byte readByte() {
        return (byte) (((DexBufferArray) buffer).data[position++] & 0xff);
    }

    @Override
    public byte[] readByteArray(int length) {
        byte[] result = new byte[length];
        System.arraycopy(((DexBufferArray) buffer).data, position, result, 0, length);
        position += length;
        return result;
    }

    @Override
    public void write(byte[] bytes) {
        ensureCapacity(bytes.length);
        System.arraycopy(bytes, 0, ((DexBufferArray) buffer).data, position, bytes.length);
        position += bytes.length;
    }

    @Override
    public void writeByte(int b) {
        ensureCapacity(1);
        write((byte) b);
    }

    public void writeShort(short i) {
        ensureCapacity(2);
        ((DexBufferArray) buffer).data[position] = (byte) i;
        ((DexBufferArray) buffer).data[position + 1] = (byte) (i >>> 8);
        position += 2;
    }

    @Override
    public void setPosition(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void readByteArray(byte[] copied, int start2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(byte b) {
        ((DexBufferArray) buffer).data[position++] = b;

    }

    @Override
    public void write(short s) {
        ensureCapacity(2);
        ((DexBufferArray) buffer).data[position] = (byte) s;
        ((DexBufferArray) buffer).data[position + 1] = (byte) (s >>> 8);
        position += 2;

    }

}
