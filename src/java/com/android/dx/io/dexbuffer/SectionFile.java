
package com.android.dx.io.dexbuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import static com.android.dx.io.dexbuffer.DexBufferRandomAccessFile.SEMAPHORE;

public final class SectionFile extends Section {
    // TODO research the semantics of multiple RandomAccessFile objects using
    // the same underlying physical files. I assume it's Linux behavior.

    private RandomAccessFile raFile;

    /* package */SectionFile(DexBufferRandomAccessFile buffer, String name, int position) {
        super(buffer, name, position, buffer.getLength());

        synchronized (SEMAPHORE) {
            try {

                raFile = new RandomAccessFile(buffer.file, "rw");
                raFile.seek(position);

            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void writeByte(int i) {
        synchronized (SEMAPHORE) {
            try {
                raFile.write((byte) i);
            } catch (IOException e) {

                throw new RuntimeException(e);

            }
        }

    }

    @Override
    public void reset() {
        synchronized (SEMAPHORE) {
            try {
                raFile.seek(start);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public int getPosition() {
        synchronized (SEMAPHORE) {
            try {
                return (int) raFile.getFilePointer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public byte readByte() {
        synchronized (SEMAPHORE) {
            try {
                return raFile.readByte();
            } catch (IOException e) {

                throw new RuntimeException(e);

            }
        }
    }

    @Override
    public byte[] readByteArray(int length) {
        synchronized (SEMAPHORE) {
            byte[] result = new byte[length];
            try {
                raFile.read(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return result;
        }
    }

    @Override
    public void setPosition(int position) {
        synchronized (SEMAPHORE) {
            try {
                raFile.seek(position);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void readByteArray(byte[] copied, int startPosition) {
        synchronized (SEMAPHORE) {
            try {
                raFile.seek(startPosition);
                raFile.read(copied);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public void write(byte[] bytes) {
        synchronized (SEMAPHORE) {
            try {
                raFile.write(bytes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public void write(byte b) {
        synchronized (SEMAPHORE) {
            try {
                raFile.write(b);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public void write(short s) {
        synchronized (SEMAPHORE) {
            try {
                ensureCapacity(2);
                raFile.write((byte) s);
                raFile.write((byte) (s >>> 8));

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

}
