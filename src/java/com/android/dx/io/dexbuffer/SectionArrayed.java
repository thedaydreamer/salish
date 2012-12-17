
package com.android.dx.io.dexbuffer;


/**
 * A section of a DexBufferArray object.
 * 
 * @author rschilling
 */
public class SectionArrayed extends Section {

    protected int position;

    SectionArrayed(DexBufferArray buffer, String name, int position, int limit) {
        super(buffer, name, position, limit);
    }

    SectionArrayed(DexBufferArray buffer, int position) {
        this(buffer, "section", position, buffer.getLength());
    }

    @Override
    public void reset() {
        position = start;
    }

    @Override
    public int getCurrentSize() {

        return position - start;

    }

    @Override
    public int getStartPosition() {
        return start;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public int readInt() {
        int result = (readByte() & 0xff)
                | (readByte() & 0xff) << 8
                | (readByte() & 0xff) << 16
                | (readByte() & 0xff) << 24;
        return result;
    }

    @Override
    public short readShort() {
        int result = (readByte() & 0xff)
                | (readByte() & 0xff) << 8;
        return (short) result;
    }

    @Override
    public int readUnsignedShort() {
        return readShort() & 0xffff;
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
    public short[] readShortArray(int length) {
        short[] result = new short[length];
        for (int i = 0; i < length; i++) {
            result[i] = readShort();
        }
        return result;
    }

    @Override
    public void alignToFourBytes() {
        int unalignedCount = position;
        position = DexBuffer.fourByteAlign(position);
        for (int i = unalignedCount; i < position; i++) {
            ((DexBufferArray) buffer).data[i] = 0;
        }
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
        // TODO Auto-generated method stub

    }
    
    

}
