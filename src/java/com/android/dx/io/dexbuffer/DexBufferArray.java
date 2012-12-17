/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.dx.io.dexbuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import schilling.richard.dalvik.vm.DvmGlobals;
import schilling.richard.dalvik.vm.oo.util.ClassDefFactory;
import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;

import com.android.dx.dex.DexFormat;
import com.android.dx.dex.SizeOf;
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
import com.android.dx.util.FileUtils;
import com.android.dx.util.Leb128Utils;
import com.android.dx.util.Mutf8;

/**
 * Array backed DexBuffer. The bytes of a dex file in memory for reading and
 * writing. All int offsets are unsigned.
 */
public final class DexBufferArray extends DexBuffer {

    public static final String LOG_TAG = "Finnr.DexBuffer";

    protected byte[] data;

    private int length = 0;

    /*
     * private final List<String> strings = new AbstractList<String>() {
     * @Override public String get(int index) { checkBounds(index,
     * tableOfContents.stringIds.size); return
     * open(tableOfContents.stringIds.off + (index * SizeOf.STRING_ID_ITEM))
     * .readString(); }
     * @Override public int size() { return tableOfContents.stringIds.size; } };
     */

    /*
     * private final List<ProtoId> protoIds = new AbstractList<ProtoId>() {
     * @Override public ProtoId get(int index) { checkBounds(index,
     * tableOfContents.protoIds.size); return open(tableOfContents.protoIds.off
     * + (SizeOf.PROTO_ID_ITEM * index)) .readProtoId(); }
     * @Override public int size() { return tableOfContents.protoIds.size; } };
     */

    /*
     * private final List<MethodId> methodIds = new AbstractList<MethodId>() {
     * @Override public MethodId get(int index) { checkBounds(index,
     * tableOfContents.methodIds.size); return
     * open(tableOfContents.methodIds.off + (SizeOf.MEMBER_ID_ITEM * index))
     * .readMethodId(); }
     * @Override public int size() { return tableOfContents.methodIds.size; } };
     */

    /**
     * Creates a new dex buffer defining no classes.
     */
    public DexBufferArray() {
        this.data = new byte[0];
    }

    /**
     * Creates a new dex buffer that reads from {@code data}. It is an error to
     * modify {@code data} after using it to create a dex buffer.
     */
    public DexBufferArray(byte[] data) throws IOException {
        this.data = data;
        this.length = data.length;
        this.tableOfContents.readFrom(this);
    }

    /**
     * Creates a new dex buffer of the dex in {@code in}, and closes {@code in}.
     */
    public DexBufferArray(InputStream in) throws IOException {
        loadFrom(in);
    }

    // FIXME : don't read and generate IOExceptions from the constructor.

    /**
     * Creates a new dex buffer from the dex file {@code file}.
     */
    public DexBufferArray(File file) throws IOException {
        if (FileUtils.hasArchiveSuffix(file.getName())) {
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry(DexFormat.DEX_IN_JAR_NAME);
            if (entry != null) {
                loadFrom(zipFile.getInputStream(entry));
                zipFile.close();
            } else {
                throw new DexException("Expected " + DexFormat.DEX_IN_JAR_NAME
                        + " in " + file);
            }
        } else if (file.getName().endsWith(".dex")) {
            loadFrom(new FileInputStream(file));
        } else {
            throw new DexException("unknown output extension: " + file);
        }
    }

    private void loadFrom(InputStream in) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];

        int count;
        while ((count = in.read(buffer)) != -1) {
            bytesOut.write(buffer, 0, count);
        }
        in.close();

        this.data = bytesOut.toByteArray();
        this.length = data.length; // TODO remove this.length. Not necessary.
        this.tableOfContents.readFrom(this);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(data);
    }

    public void writeTo(File dexOut) throws IOException {
        OutputStream out = new FileOutputStream(dexOut);
        writeTo(out);
        out.close();
    }

    public Section open(int position) {
        if (position < 0 || position > length) {
            throw new IllegalArgumentException("position=" + position
                    + " length=" + length);
        }
        return new SectionArrayed(this, position);
    }

    public Section appendSection(int maxByteCount, String name) {
        int limit = fourByteAlign(length + maxByteCount);
        Section result = new SectionArrayed(this, name, length, limit);
        length = limit;
        return result;
    }

    public void noMoreSections() {
        data = new byte[length];
    }

    public int getLength() {
        return length;
    }

    public static int fourByteAlign(int position) {
        return (position + 3) & ~3;
    }

    public List<Integer> typeIds() {
        return typeIds;
    }

    /**
     * Writes the values of the map to the underlying byte array. Supports
     */
    public void writeMap() {

    }

    public ClassData readClassData(ClassDef classDef) {

        int offset = classDef.getClassDataOffset();
        if (offset == 0)
            throw new IllegalArgumentException("offset == 0");

        return classData().get(offset);
    }

    public final class SectionBackup implements ByteInput, ByteOutput {

        private final String name;
        private int position;
        private final int limit;
        private final int start;

        private SectionBackup(String name, int position, int limit) {
            this.name = name;
            this.position = position;
            this.start = position;
            this.limit = limit;
        }

        private SectionBackup(int position) {
            this("section", position, data.length);
        }

        /**
         * Sets the current position of the section for reading/writing to the
         * start position.
         */
        public void reset() {
            position = start;
        }

        /**
         * Returns the current size of the section which is the current position
         * minus the start position. This is the size of the section in bytes up
         * to its current writing position. In cases where this section is only
         * being written to, this can be interpreted as the number of bytes
         * written to the section.
         * 
         * @return the number of bytes in the section up to and including the
         *         position (getPosition() - getStartPosition()).
         */
        public int getCurrentSize() {

            return position - start;

        }

        /**
         * Returns the position that will be set if reset() is called.
         * 
         * @return the start position of the section.
         */
        public int getStartPosition() {
            return start;
        }

        /**
         * Gets the current position for reading/writing.
         * 
         * @return the current read/write position.
         */
        public int getPosition() {
            return position;
        }

        public int readInt() {
            int result = (data[position] & 0xff)
                    | (data[position + 1] & 0xff) << 8
                    | (data[position + 2] & 0xff) << 16
                    | (data[position + 3] & 0xff) << 24;
            position += 4;
            return result;
        }

        public short readShort() {
            int result = (data[position] & 0xff)
                    | (data[position + 1] & 0xff) << 8;
            position += 2;
            return (short) result;
        }

        public int readUnsignedShort() {
            return readShort() & 0xffff;
        }

        public byte readByte() {
            return (byte) (data[position++] & 0xff);
        }

        public byte[] readByteArray(int length) {
            byte[] result = new byte[length];
            System.arraycopy(data, position, result, 0, length);
            position += length;
            return result;
        }

        public short[] readShortArray(int length) {
            short[] result = new short[length];
            for (int i = 0; i < length; i++) {
                result[i] = readShort();
            }
            return result;
        }

        public int readUleb128() {
            return Leb128Utils.readUnsignedLeb128(this);
        }

        public int readSleb128() {
            return Leb128Utils.readSignedLeb128(this);
        }

        public TypeList readTypeList() {
            int size = readInt();
            short[] types = new short[size];
            for (int i = 0; i < size; i++) {
                types[i] = readShort();
            }
            alignToFourBytes();
            return new TypeList(DexBufferArray.this, types);
        }

        public String readString() {
            int offset = readInt();
            int savedPosition = position;
            position = offset;
            try {
                int expectedLength = readUleb128();
                String result = Mutf8.decode(this, new char[expectedLength]);
                if (result.length() != expectedLength) {
                    throw new DexException("Declared length " + expectedLength
                            + " doesn't match decoded length of "
                            + result.length());
                }
                return result;
            } catch (UTFDataFormatException e) {
                throw new DexException(e);
            } finally {
                position = savedPosition;
            }
        }

        public FieldId readFieldId() {
            int declaringClassIndex = readUnsignedShort();
            int typeIndex = readUnsignedShort();
            int nameIndex = readInt();
            return new FieldId(DexBufferArray.this, declaringClassIndex, typeIndex,
                    nameIndex);
        }

        public MethodId readMethodId() {
            int declaringClassIndex = readUnsignedShort();
            int protoIndex = readUnsignedShort();
            int nameIndex = readInt();
            return new MethodId(DexBufferArray.this, declaringClassIndex,
                    protoIndex, nameIndex);
        }

        public ProtoId readProtoId() {
            int shortyIndex = readInt();
            int returnTypeIndex = readInt();
            int parametersOffset = readInt();
            return new ProtoId(DexBufferArray.this, shortyIndex, returnTypeIndex,
                    parametersOffset);
        }

        public ClassDef readClassDef() {
            int offset = getPosition();
            int type = readInt();
            int accessFlags = readInt();
            int supertype = readInt();
            int interfacesOffset = readInt();
            int sourceFileIndex = readInt();
            int annotationsOffset = readInt();
            int classDataOffset = readInt();
            int staticValuesOffset = readInt();
            return new ClassDef(DexBufferArray.this, offset, type, accessFlags,
                    supertype, interfacesOffset, sourceFileIndex,
                    annotationsOffset, classDataOffset, staticValuesOffset);
        }

        /**
         * This reads a code block from the current position. This function
         * assumes that the code block is properly formed.
         * 
         * @return A method code item.
         */
        private Code readCode() {
            int start = position;
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

            int size = position - start;

            return new Code(registersSize, insSize, outsSize, debugInfoOffset,
                    instructions, tries, catchHandlers, start, size);

        }

        private Code.CatchHandler readCatchHandler(int startPosition) {
            List<String> types = typeNames();
            int size = readSleb128();

            int handlerOffset = getPosition() - startPosition; // see DEX
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
            Code.CatchHandler result = new Code.CatchHandler(handlerOffset,
                    typeIndexes,
                    typeSignatures, addresses, catchAllAddress);

            return result;

        }

        private ClassData readClassData() {
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

        private ClassData.Field[] readFields(FieldType fieldType, int count) {
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

        private ClassData.Method[] readMethods(MethodType methodType, int count) {
            ClassData.Method[] result = new ClassData.Method[count];
            int methodIndex = 0;
            for (int i = 0; i < count; i++) {
                int methodOff = this.position;
                int rawIndex = readUleb128();
                methodIndex += rawIndex; // method index diff
                int accessFlags = readUleb128();
                int codeOff = readUleb128();
                result[i] = new ClassData.Method(methodType, methodIndex,
                        rawIndex, accessFlags, codeOff, methodOff);
            }
            return result;
        }

        public Annotation readAnnotation() {
            byte visibility = readByte();
            int typeIndex = readUleb128();
            int size = readUleb128();
            int[] names = new int[size];
            EncodedValue[] values = new EncodedValue[size];
            for (int i = 0; i < size; i++) {
                names[i] = readUleb128();
                values[i] = readEncodedValue();
            }
            return new Annotation(DexBufferArray.this, visibility, typeIndex, names,
                    values);
        }

        /**
         * Reads an annotation_set_item from the current location.
         * 
         * @return a list of Annotation items.
         * @author coderroadie
         */
        public List<Annotation> readAnnotationSet() {
            List<Annotation> result = new LinkedList<Annotation>();

            int setSz = readInt();
            for (int i = 0; i < setSz; i++) {
                int annotationOffset = readInt();
                Section annotationSection = DexBufferArray.this
                        .open(annotationOffset);
                result.add(annotationSection.readAnnotation());
            }

            return result;

        }

        public EncodedValue readEncodedValue() {
            int start = position;
            new EncodedValueReader(this).readValue();
            int end = position;
            int len = end - start + 1;
            byte[] copied = new byte[len];
            System.arraycopy(data, start, copied, 0, len);
            return new EncodedValue(copied);
        }

        public EncodedValue readEncodedArray() {
            int start = position;
            new EncodedValueReader(this).readArray();
            int end = position;
            int len = end - start + 1;
            byte[] copied = new byte[len];
            System.arraycopy(data, start, copied, 0, len);
            return new EncodedValue(copied);
        }

        private void ensureCapacity(int size) {
            if (position + size > limit) {
                throw new DexException("Section limit " + limit
                        + " exceeded by " + name);
            }
        }

        /**
         * Writes 0x00 until the position is aligned to a multiple of 4.
         */
        public void alignToFourBytes() {
            int unalignedCount = position;
            position = DexBuffer.fourByteAlign(position);
            for (int i = unalignedCount; i < position; i++) {
                data[i] = 0;
            }
        }

        public void assertFourByteAligned() {
            if ((position & 3) != 0) {
                throw new IllegalStateException("Not four byte aligned!");
            }
        }

        public void write(byte[] bytes) {
            ensureCapacity(bytes.length);
            System.arraycopy(bytes, 0, data, position, bytes.length);
            position += bytes.length;
        }

        // FIXME consolidate the write functions to one class throughout the
        // whole project.
        public void writeByte(int b) {
            ensureCapacity(1);
            data[position++] = (byte) b;
        }

        public void writeShort(short i) {
            ensureCapacity(2);
            data[position] = (byte) i;
            data[position + 1] = (byte) (i >>> 8);
            position += 2;
        }

        public void writeUnsignedShort(int i) {
            short s = (short) i;
            if (i != (s & 0xffff)) {
                throw new IllegalArgumentException(
                        "Expected an unsigned short: " + i);
            }
            writeShort(s);
        }

        public void write(short[] shorts) {
            for (short s : shorts) {
                writeShort(s);
            }
        }

        public void writeInt(int i) {
            ensureCapacity(4);
            data[position] = (byte) i;
            data[position + 1] = (byte) (i >>> 8);
            data[position + 2] = (byte) (i >>> 16);
            data[position + 3] = (byte) (i >>> 24);
            position += 4;
        }

        public void writeUleb128(int i) {
            try {
                Leb128Utils.writeUnsignedLeb128(this, i);
                ensureCapacity(0);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new DexException("Section limit " + limit
                        + " exceeded by " + name);
            }
        }

        public void writeSleb128(int i) {
            try {
                Leb128Utils.writeSignedLeb128(this, i);
                ensureCapacity(0);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new DexException("Section limit " + limit
                        + " exceeded by " + name);
            }
        }

        public void writeStringData(String value) {
            try {
                int length = value.length();
                writeUleb128(length);
                write(Mutf8.encode(value));
                writeByte(0);
            } catch (UTFDataFormatException e) {
                throw new AssertionError();
            }
        }

        public void writeTypeList(TypeList typeList) {
            short[] types = typeList.getTypes();
            writeInt(types.length);
            for (short type : types) {
                writeShort(type);
            }
            alignToFourBytes();
        }

        /**
         * Returns the number of bytes remaining in this section.
         */
        public int remaining() {
            return limit - position;
        }
    }

    /**
     * Calls getDef(String).
     * 
     * @deprecated
     */
    public ClassDef getFromSignature(String signature) {
        return getDef(signature);
    }

    /**
     * Returns the super class signature of the specified signature. Signature
     * may be for a class found in a ClassDef or on the platform. If the class
     * found is a platform class, then the signature of the superclass is
     * formatted properly to begin with a 'L' character and end with a ';'
     * character.
     * 
     * @param signature the signature to find the superclass of
     * @return the signature of the superclass.
     */
    public String getSuperclassOf(String signature) {

        if (signature == null)
            throw new IllegalArgumentException("signature cannot be null");

        if (signature.trim().length() == 0)
            throw new IllegalArgumentException("signature cannot be empty");

        ClassDef cDef = getDef(signature);

        if (cDef != null)
            return cDef.getSupertypeSignature();

        boolean isPlatform = ClassDefFactory
                .isPlatformClassSpecifier(signature);
        if (!isPlatform)
            // no ClassDef and not found on the platform. Critical error.
            throw new IllegalArgumentException(
                    String.format("signature %s could not be found."));

        try {
            String cName = ClassLoaderUtil.convertTypeToClassName(signature);
            Class<?> supClass = Class.forName(cName, false, null)
                    .getSuperclass();
            if (supClass == null)
                return null;
            String supName = supClass.getCanonicalName();
            supName.replace('.', '/');
            supName += 'L' + supName + ';';

            return supName;

        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                    "unable to convert class signature " + signature
                            + " to a platform class.");
        }

    }

    /**
     * Returns the class depth of a class' signature. If the class signature
     * cannot be resolved to a ClassDef object or a platform class (Class type),
     * then an exception is thrown.
     * 
     * @param signature the signature of the class to calcualte the depth for.
     * @return the number of superclasses the specified class has.
     */
    public int getClassDepth(String signature) {

        if (signature.equals(DvmGlobals.JAVA_LANG_OBJECT_SIGNATURE))
            return 0;

        if (signature.charAt(0) == '[')
            return 1;

        int result = 0;
        ClassDef cDef = getDef(signature);
        while (cDef != null) {
            // all classes referenced in the DexFile have a ClassDef.
            result++;
            cDef = getDef(cDef.getSupertypeSignature());
        }

        // TODO: determine if this form of statement generates fewer
        // instructrions than this form : if
        // (ClassDefFactory.isPlatformClassSpecifier(signature).

        boolean isPlatform = ClassDefFactory
                .isPlatformClassSpecifier(signature);
        if (!isPlatform)
            // no ClassDef and not found on the platform. Critical error.
            throw new IllegalArgumentException(
                    String.format("signature %s could not be found."));

        String cName = ClassLoaderUtil.convertTypeToClassName(signature);
        try {

            // load the class definition but don't initialize it.
            result += ClassLoaderUtil.getClassDepth(Class.forName(cName, false,
                    null));

        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                    "unable to convert class signature " + signature
                            + " to a platform class.");
        }

        return result;

    }

    /**
     * Returns the signature of all but the first 32 bytes of {@code dex}. The
     * first 32 bytes of dex files are not specified to be included in the
     * signature.
     */
    public byte[] computeSignature() throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
        int offset = SIGNATURE_OFFSET + SIGNATURE_SIZE;

        digest.update(data, offset, data.length - offset);
        return digest.digest();
    }

    /**
     * Returns the checksum of all but the first 12 bytes of {@code dex}.
     */
    public int computeChecksum() throws IOException {
        Adler32 adler32 = new Adler32();
        int offset = CHECKSUM_OFFSET + CHECKSUM_SIZE;

        adler32.update(data, offset, data.length - offset);
        return (int) adler32.getValue();
    }

    @Override
    public void calculateAdler32() {
        // recalculate Adler32 and store that value
        Adler32 adler = new Adler32();
        int pos = 8 + SizeOf.UINT;
        int byteCount = getLength() - pos;
        adler.update(data, pos, byteCount);
        open(8).writeInt((int) adler.getValue());

    }

}
