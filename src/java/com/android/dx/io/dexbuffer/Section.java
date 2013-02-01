
package com.android.dx.io.dexbuffer;

import java.io.UTFDataFormatException;
import java.util.LinkedList;
import java.util.List;

import com.android.dx.io.Annotation;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.EncodedValue;
import com.android.dx.io.EncodedValueReader;
import com.android.dx.io.FieldId;
import com.android.dx.io.FieldType;
import com.android.dx.io.MethodId;
import com.android.dx.io.MethodType;
import com.android.dx.io.ProtoId;
import com.android.dx.merge.TypeList;
import com.android.dx.util.ByteInput;
import com.android.dx.util.ByteOutput;
import com.android.dx.util.DexException;
import com.android.dx.util.Leb128Utils;
import com.android.dx.util.Mutf8;

// TODO Refactor Section to a better name
/**
 * Points to a region of the buffer. Reads and writes data to it.
 * 
 * @author rschilling
 */
public abstract class Section implements ByteInput, ByteOutput {
    protected final String name;
    protected final int limit;
    protected final int start;
    protected final DexBuffer buffer;

    public Section(DexBuffer buffer, String name, int startPosition, int limit) {
        this.name = name;

        this.start = startPosition;
        this.limit = limit;
        this.buffer = buffer;
    }

    /**
     * Sets the current position of the section for reading/writing to the start
     * position.
     */
    public abstract void reset();

    /**
     * Returns the position that will be set if reset() is called.
     * 
     * @return the start position of the section.
     */
    public final int getStartPosition() {
        return start;
    }

    /**
     * Gets the current position for reading/writing.
     * 
     * @return the current read/write position.
     */
    public abstract int getPosition();

    public final int readInt() {
        int result = (readByte() & 0xff)
                | (readByte() & 0xff) << 8
                | (readByte() & 0xff) << 16
                | (readByte() & 0xff) << 24;
        return result;
    }

    public final short readShort() {
        int result = (readByte() & 0xff)
                | (readByte() & 0xff) << 8;
        return (short) result;
    }

    public final int readUnsignedShort() {
        return readShort() & 0xffff;
    }

    public abstract byte readByte();

    public abstract byte[] readByteArray(int length);

    public final short[] readShortArray(int length) {
        short[] result = new short[length];
        for (int i = 0; i < length; i++) {
            result[i] = readShort();
        }
        return result;
    }

    /**
     * Writes 0x00 until the position is aligned to a multiple of 4.
     */
    public final void alignToFourBytes() {
        int unalignedPosition = getPosition();
        setPosition(DexBuffer.fourByteAlign(unalignedPosition));
        int alignedPosition = getPosition();
        for (int i = unalignedPosition; i < alignedPosition; i++) {
            write((byte) 0);
        }

    }

    /**
     * Returns the current size of the section which is the current position
     * minus the start position. This is the size of the section in bytes up to
     * its current writing position. In cases where this section is only being
     * written to, this can be interpreted as the number of bytes written to the
     * section.
     * 
     * @return the number of bytes in the section up to and including the
     *         position (getPosition() - getStartPosition()).
     */
    public final int getCurrentSize() {

        return getPosition() - getStartPosition();

    }

    public final int readUleb128() {
        return Leb128Utils.readUnsignedLeb128(this);
    }

    public final int readSleb128() {
        return Leb128Utils.readSignedLeb128(this);
    }

    public final TypeList readTypeList() {
        int size = readInt();
        short[] types = new short[size];
        for (int i = 0; i < size; i++) {
            types[i] = readShort();
        }
        alignToFourBytes();
        return new TypeList(buffer, types);
    }

    public abstract void setPosition(int position);

    public final String readString() {
        int offset = readInt();
        int savedPosition = getPosition();
        setPosition(offset);
        try {
            int expectedLength = readUleb128();
            String result = Mutf8.decode(this, new char[expectedLength]);
            if (result.length() != expectedLength) {
                throw new DexException("Declared length " + expectedLength
                        + " doesn't match decoded length of "
                        + result.length() + " of string '" + result + "'");
            }
            return result;
        } catch (UTFDataFormatException e) {
            throw new DexException(e);
        } finally {
            setPosition(savedPosition);
        }
    }

    public final FieldId readFieldId() {
        int declaringClassIndex = readUnsignedShort();
        int typeIndex = readUnsignedShort();
        int nameIndex = readInt();
        return new FieldId(buffer, declaringClassIndex, typeIndex,
                nameIndex);
    }

    public final MethodId readMethodId() {
        int declaringClassIndex = readUnsignedShort();
        int protoIndex = readUnsignedShort();
        int nameIndex = readInt();
        return new MethodId(buffer, declaringClassIndex,
                protoIndex, nameIndex);
    }

    public final ProtoId readProtoId() {
        int shortyIndex = readInt();
        int returnTypeIndex = readInt();
        int parametersOffset = readInt();
        return new ProtoId(buffer, shortyIndex, returnTypeIndex,
                parametersOffset);
    }

    public final ClassDef readClassDef() {
        int offset = getPosition();
        int type = readInt();
        int accessFlags = readInt();
        int supertype = readInt();
        int interfacesOffset = readInt();
        int sourceFileIndex = readInt();
        int annotationsOffset = readInt();
        int classDataOffset = readInt();
        int staticValuesOffset = readInt();
        return new ClassDef(buffer, offset, type, accessFlags,
                supertype, interfacesOffset, sourceFileIndex,
                annotationsOffset, classDataOffset, staticValuesOffset);
    }

    /**
     * This reads a code block from the current position. This function assumes
     * that the code block is properly formed.
     * 
     * @return A method code item.
     */
    public final Code readCode() {
        int start = getPosition();
        int registersSize = readUnsignedShort();
        int insSize = readUnsignedShort();
        int outsSize = readUnsignedShort();
        int triesSize = readUnsignedShort();
        int debugInfoOffset = readInt();
        int instructionsSize = readInt();
        short[] instructions = readShortArray(instructionsSize);
        Code.Try[] tries = new Code.Try[triesSize];
        Code.CatchHandler[] catchHandlers = new Code.CatchHandler[0];
        if (triesSize > 0) {
            if (instructions.length % 2 == 1) {
                readShort(); // padding
            }

            for (int i = 0; i < triesSize; i++) {
                int startAddress = readInt();
                int instructionCount = readUnsignedShort();
                int handlerOffset = readUnsignedShort();
                tries[i] = new Code.Try(startAddress, instructionCount,
                        handlerOffset);
            }

            int catchHandlersSize = readUleb128();
            catchHandlers = new Code.CatchHandler[catchHandlersSize];

            int startPosition = getPosition();
            for (int i = 0; i < catchHandlersSize; i++) {

                catchHandlers[i] = readCatchHandler(startPosition);

            }

        }

        int size = getCurrentSize();

        return new Code(registersSize, insSize, outsSize, debugInfoOffset,
                instructions, tries, catchHandlers, start, size);

    }

    public final Code.CatchHandler readCatchHandler(int startPosition) {
        List<String> types = buffer.typeNames();
        int size = readSleb128();

        int handlerOffset = getCurrentSize(); // see DEX
                                              // documentation
                                              // for the
                                              // try_item.handler_off
                                              // item.

        int handlersCount = Math.abs(size);
        int[] typeIndexes = new int[handlersCount];
        int[] addresses = new int[handlersCount];
        String[] typeSignatures = new String[handlersCount];

        for (int i = 0; i < handlersCount; i++) {
            typeIndexes[i] = readUleb128();
            addresses[i] = readUleb128();
            typeSignatures[i] = types.get(typeIndexes[i]);
        }

        int catchAllAddress = size <= 0 ? readUleb128() : -1;
        Code.CatchHandler result = new Code.CatchHandler(typeIndexes,
                typeSignatures, addresses, catchAllAddress, handlerOffset);

        return result;

    }

    public final ClassData readClassData() {
        int staticFieldsSize = readUleb128();
        int instanceFieldsSize = readUleb128();
        int directMethodsSize = readUleb128();
        int virtualMethodsSize = readUleb128();
        ClassData.Field[] staticFields = readFields(FieldType.STATIC,
                staticFieldsSize);
        ClassData.Field[] instanceFields = readFields(FieldType.INSTANCE,
                instanceFieldsSize);
        ClassData.Method[] directMethods = readMethods(MethodType.DIRECT,
                directMethodsSize);
        ClassData.Method[] virtualMethods = readMethods(MethodType.VIRTUAL,
                virtualMethodsSize);
        return new ClassData(staticFields, instanceFields, directMethods,
                virtualMethods);
    }

    public final ClassData.Field[] readFields(FieldType fieldType, int count) {
        ClassData.Field[] result = new ClassData.Field[count];
        int fieldIndex = 0;
        for (int i = 0; i < count; i++) {
            fieldIndex += readUleb128(); // field index diff
            int accessFlags = readUleb128();
            result[i] = new ClassData.Field(fieldType, fieldIndex,
                    accessFlags);
        }
        return result;
    }

    public final ClassData.Method[] readMethods(MethodType methodType, int count) {
        ClassData.Method[] result = new ClassData.Method[count];
        int methodIndex = 0;
        for (int i = 0; i < count; i++) {
            int methodOff = getPosition();
            int rawIndex = readUleb128();
            methodIndex += rawIndex; // method index diff
            int accessFlags = readUleb128();
            int codeOff = readUleb128();
            result[i] = new ClassData.Method(methodType, methodIndex,
                    rawIndex, accessFlags, codeOff, methodOff);
        }
        return result;
    }

    public final Annotation readAnnotation() {
        byte visibility = readByte();
        int typeIndex = readUleb128();
        int size = readUleb128();
        int[] names = new int[size];
        EncodedValue[] values = new EncodedValue[size];
        for (int i = 0; i < size; i++) {
            names[i] = readUleb128();
            values[i] = readEncodedValue();
        }
        return new Annotation(buffer, visibility, typeIndex, names,
                values);
    }

    /**
     * Reads an annotation_set_item from the current location.
     * 
     * @return a list of Annotation items.
     * @author coderroadie
     */
    public final List<Annotation> readAnnotationSet() {
        List<Annotation> result = new LinkedList<Annotation>();

        int setSz = readInt();
        for (int i = 0; i < setSz; i++) {
            int annotationOffset = readInt();
            Section annotationSection = buffer
                    .open(annotationOffset);
            result.add(annotationSection.readAnnotation());
        }

        return result;

    }

    public final EncodedValue readEncodedValue() {
        int start = getPosition();
        new EncodedValueReader(this).readValue();
        int end = getPosition();
        int len = end - start + 1;
        byte[] copied = new byte[len];
        readByteArray(copied, start);

        return new EncodedValue(copied);
    }

    /**
     * Reads copied.length bytes of the buffer into the array copied, starting
     * at a position position. If the buffer falls off the end of the file an
     * exception is thrown. At the end of the operation, the pointer is set to
     * startPosition + copied.length.
     * 
     * @param copied the array to fill
     * @param start the start position. TOTO throw exception if the buffer
     *            reaches past the end of the file.
     */
    public abstract void readByteArray(byte[] copied, int startPosition);

    public final EncodedValue readEncodedArray() {
        int start = getPosition();
        new EncodedValueReader(this).readArray();
        int end = getPosition();
        int len = end - start + 1;
        byte[] copied = new byte[len];
        readByteArray(copied, start);
        return new EncodedValue(copied);
    }

    public final void ensureCapacity(int size) {
        if (getPosition() + size > limit) {
            throw new DexException("Section limit " + limit
                    + " exceeded by " + name);
        }
    }

    public final void assertFourByteAligned() {
        if ((getPosition() & 3) != 0) {
            throw new IllegalStateException("Not four byte aligned!");
        }
    }

    public abstract void write(byte[] bytes);

    // FIXME consolidate the write functions to one class throughout the
    // whole project.
    public abstract void write(byte b);

    public abstract void write(short s);

    public final void writeUnsignedShort(int i) {
        short s = (short) i;
        if (i != (s & 0xffff)) {
            throw new IllegalArgumentException(
                    "Expected an unsigned short: " + i);
        }
        write(s);
    }

    public final void write(short[] shorts) {
        for (short s : shorts) {
            write(s);
        }
    }

    public final void writeInt(int i) {
        ensureCapacity(4);
        write((byte) i);
        write((byte) (i >>> 8));
        write((byte) (i >>> 16));
        write((byte) (i >>> 24));
    }

    public final void writeUleb128(int i) {
        try {
            Leb128Utils.writeUnsignedLeb128(this, i);
            ensureCapacity(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DexException("Section limit " + limit
                    + " exceeded by " + name);
        }
    }

    public final void writeSleb128(int i) {
        try {
            Leb128Utils.writeSignedLeb128(this, i);
            ensureCapacity(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DexException("Section limit " + limit
                    + " exceeded by " + name);
        }
    }

    public final void writeStringData(String value) {
        try {
            int length = value.length();
            writeUleb128(length);
            write(Mutf8.encode(value));
            writeByte(0);
        } catch (UTFDataFormatException e) {
            throw new AssertionError();
        }
    }

    public final void writeTypeList(TypeList typeList) {
        short[] types = typeList.getTypes();
        writeInt(types.length);
        for (short type : types) {
            write(type);
        }
        alignToFourBytes();
    }

    /**
     * Returns the number of bytes remaining in this section.
     */
    public final int remaining() {
        return limit - getPosition();
    }

};
