
package com.android.dx.io.dexbuffer;

import java.io.File;
import java.io.IOException;

/**
 * Creates DexBuffer objects.
 * 
 * @author rschilling
 */
public final class DexBufferFactory {
    private DexBufferFactory() {
    }

    /**
     * Creates a DexBuffer that is backed by a file.
     * 
     * @param file the file to open.
     * @return a DexBuffer backed by a file.
     */
    public static DexBuffer getDexBuffer(File file) {
        return new DexBufferRandomAccessFile(file);

    }

    public static DexBuffer getDexBuffer(byte[] data) {
        try {
            return new DexBufferArray(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
