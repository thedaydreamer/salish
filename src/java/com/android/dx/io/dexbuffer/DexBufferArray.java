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
import java.util.AbstractList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import schilling.richard.dalvik.vm.DvmGlobals;
import schilling.richard.dalvik.vm.oo.util.ClassDefFactory;
import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.dex.DexFormat;
import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
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
import com.android.dx.io.ClassData.Field;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.Code.Try;
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
public final class DexBufferArray extends DexBuffer{

    public static final String LOG_TAG = "Finnr.DexBuffer";

    private byte[] data;
    private final TableOfContents tableOfContents = new TableOfContents();
    private int length = 0;

    private List<String> strings = null;

    /*
     * private final List<String> strings = new AbstractList<String>() {
     * @Override public String get(int index) { checkBounds(index,
     * tableOfContents.stringIds.size); return
     * open(tableOfContents.stringIds.off + (index * SizeOf.STRING_ID_ITEM))
     * .readString(); }
     * @Override public int size() { return tableOfContents.stringIds.size; } };
     */

    private final List<BufferedStringIdItem> bufferedStringIdItems = new AbstractList<BufferedStringIdItem>() {

        @Override
        public BufferedStringIdItem get(int index) {
            return new BufferedStringIdItem(tableOfContents.stringIds.off
                    + (index * SizeOf.STRING_ID_ITEM));

        }

        @Override
        public int size() {
            return tableOfContents.stringIds.size;
        }

    };

    private final List<Integer> typeIds = new AbstractList<Integer>() {

        @Override
        public Integer get(int index) {
            checkBounds(index, tableOfContents.typeIds.size);
            return open(
                    tableOfContents.typeIds.off + (index * SizeOf.TYPE_ID_ITEM))
                    .readInt();
        }

        @Override
        public int size() {
            return tableOfContents.typeIds.size;
        }
    };

    private List<String> typeNames;

    private List<ProtoId> protoIds = null;

    /*
     * private final List<ProtoId> protoIds = new AbstractList<ProtoId>() {
     * @Override public ProtoId get(int index) { checkBounds(index,
     * tableOfContents.protoIds.size); return open(tableOfContents.protoIds.off
     * + (SizeOf.PROTO_ID_ITEM * index)) .readProtoId(); }
     * @Override public int size() { return tableOfContents.protoIds.size; } };
     */

    private final List<BufferedProtoIdItem> bufferedProtoIds = new AbstractList<BufferedProtoIdItem>() {

        @Override
        public BufferedProtoIdItem get(int index) {
            return new BufferedProtoIdItem(tableOfContents.protoIds.off
                    + (SizeOf.PROTO_ID_ITEM * index));
        }

        @Override
        public int size() {
            return tableOfContents.protoIds.size;
        }

    };

    private final List<FieldId> fieldIds = new AbstractList<FieldId>() {

        @Override
        public FieldId get(int index) {
            checkBounds(index, tableOfContents.fieldIds.size);
            return open(
                    tableOfContents.fieldIds.off
                            + (SizeOf.MEMBER_ID_ITEM * index)).readFieldId();
        }

        @Override
        public int size() {
            return tableOfContents.fieldIds.size;
        }
    };

    private List<MethodId> methodIds = null;
    /*
     * private final List<MethodId> methodIds = new AbstractList<MethodId>() {
     * @Override public MethodId get(int index) { checkBounds(index,
     * tableOfContents.methodIds.size); return
     * open(tableOfContents.methodIds.off + (SizeOf.MEMBER_ID_ITEM * index))
     * .readMethodId(); }
     * @Override public int size() { return tableOfContents.methodIds.size; } };
     */

    private SparseArray<ClassData> classData = null;

    public SparseArray<ClassData> classData() {
        if (classData == null) {
            classData = new SparseArray<ClassData>();
            TableOfContents toc = getTableOfContents();
            Section in = open(toc.classDatas.off);
            for (int index = 0; index < toc.classDatas.size; index++) {
                int offset = in.getPosition();
                ClassData cData = in.readClassData();
                classData.put(offset, cData);
            }

        }

        return classData;
    }

    private final List<BufferedAnnotationSetRefItem> bufferedAnnotationSetRefItem = new AbstractList<BufferedAnnotationSetRefItem>() {

        @Override
        public BufferedAnnotationSetRefItem get(int index) {
            return new BufferedAnnotationSetRefItem(
                    tableOfContents.annotationSetRefLists.off
                            + (index * SizeOf.ANNOTATION_SET_REF_ITEM));

        }

        @Override
        public int size() {
            return tableOfContents.annotationSetRefLists.size;
        }

    };

    public List<BufferedAnnotationSetRefItem> bufferedAnnotationSetRefItems() {
        return bufferedAnnotationSetRefItem;
    }

    private final List<BufferedAnnotationSetItem> bufferedAnnotationSetItem = new AbstractList<BufferedAnnotationSetItem>() {

        @Override
        public BufferedAnnotationSetItem get(int index) {
            return new BufferedAnnotationSetItem(
                    tableOfContents.annotationSets.off
                            + (index * SizeOf.ANNOTATION_SET_ITEM));

        }

        @Override
        public int size() {
            return tableOfContents.annotationSetRefLists.size;
        }

    };

    public List<BufferedAnnotationSetItem> bufferedAnnotationSetItems() {
        return bufferedAnnotationSetItem;
    }

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

    private static void checkBounds(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index:" + index + ", length="
                    + length);
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(data);
    }

    public void writeTo(File dexOut) throws IOException {
        OutputStream out = new FileOutputStream(dexOut);
        writeTo(out);
        out.close();
    }

    public TableOfContents getTableOfContents() {
        return tableOfContents;
    }

    public Section open(int position) {
        if (position < 0 || position > length) {
            throw new IllegalArgumentException("position=" + position
                    + " length=" + length);
        }
        return new Section(position);
    }

    public Section appendSection(int maxByteCount, String name) {
        int limit = fourByteAlign(length + maxByteCount);
        Section result = new Section(name, length, limit);
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

    public byte[] getBytes() {
        return data;
    }

    public List<String> strings() {
        if (strings == null) {
            LinkedList<String> result = new LinkedList<String>();
            List<BufferedStringIdItem> bsidList = bufferedStringIds();

            for (BufferedStringIdItem item : bsidList) {
                List<AddressPointer> addresses = item.getAddressPointers();
                for (AddressPointer address : addresses) {
                    Section sec = new Section(address.getAddress());
                    String s = sec.readString();
                    result.add(s);

                }
            }

            strings = Collections.unmodifiableList(result);

        }

        return strings;

    }

    public List<BufferedStringIdItem> bufferedStringIds() {
        return bufferedStringIdItems;
    }

    public List<Integer> typeIds() {
        return typeIds;
    }

    public List<String> typeNames() {

        if (typeNames == null) {
            List<String> tList = new AbstractList<String>() {

                @Override
                public String get(int index) {
                    checkBounds(index, tableOfContents.typeIds.size);
                    return strings().get(typeIds.get(index));
                }

                @Override
                public int size() {
                    return tableOfContents.typeIds.size;
                }
            };

            typeNames = new LinkedList<String>();

            for (String s : tList) {
                typeNames.add(s);
            }

        }

        return typeNames;

    }

    public List<ProtoId> protoIds() {
        if (protoIds == null) {
            protoIds = new LinkedList<ProtoId>();

            Section pIdSection = open(tableOfContents.protoIds.off);

            for (int index = 0; index < tableOfContents.protoIds.size; index++) {
                ProtoId pId = pIdSection.readProtoId();
                protoIds.add(pId);
            }
            protoIds = Collections.unmodifiableList(protoIds);

        }
        return protoIds;
    }

    public List<BufferedProtoIdItem> bufferedProtoIds() {
        return bufferedProtoIds;
    }

    public List<FieldId> fieldIds() {
        return fieldIds;
    }

    public List<MethodId> methodIds() {
        if (methodIds == null) {
            methodIds = new LinkedList<MethodId>();
            Section mIdSection = open(tableOfContents.methodIds.off);

            for (int index = 0; index < tableOfContents.methodIds.size; index++) {
                MethodId mId = mIdSection.readMethodId();
                methodIds.add(mId);
            }

            methodIds = Collections.unmodifiableList(methodIds);

        }

        return methodIds;
    }

    /**
     * Writes the values of the map to the underlying byte array. Supports
     */
    public void writeMap() {

    }

    private List<ClassDef> classDefs = null;

    public List<ClassDef> classDefList() {
        if (classDefs == null) {
            classDefs = new LinkedList<ClassDef>();
            if (tableOfContents.classDefs.exists()) {
                DexBuffer.Section in = open(tableOfContents.classDefs.off);
                for (int index = 0; index < tableOfContents.classDefs.size; index++) {
                    classDefs.add(in.readClassDef());
                }
            }

            classDefs = Collections.unmodifiableList(classDefs);

        }
        return classDefs;
    }

    public Hashtable<String, ClassDef> classDefTable = null;

    /**
     * Returns the class definition for the specified type signature. Null is
     * returned if the specified type is found on the platform or is not found
     * in the DEX file because it's an array signature.
     * <p>
     * This method will return null if the signature specified is not found in
     * the DEX file. A null return value does not indicate an error because on
     * the mobile device, only class definitions defined in the DEX file are
     * present. But, MethodID objects can point to methods that are on the
     * platform and not in the DEX file. This distinction is necessary because
     * the class path is assumed to be proper. That is, if a class definition is
     * not returned then it's assumed that the class signature is for a class on
     * the platform.
     * <p>
     * TODO when the model is used, don't return null values, but return a
     * PlatformClass object instead.
     * 
     * @param type the type to get
     * @return a ClassDef object associated with the type if it's found in the
     *         DEX file. TODO: check to see that the class is on the platform.
     */
    public ClassDef getDef(String type) {

        if (type == null)
            throw new IllegalArgumentException("type cannot be null");

        if (type.trim().equals(""))
            throw new IllegalArgumentException("type cannot be empty");

        if (ClassLoaderUtil.isFoundOnPlatform(type))
            return null;

        if (classDefTable == null) {
            classDefTable = new Hashtable<String, ClassDef>();
            List<String> types = typeNames();
            List<ClassDef> cDefs = classDefList();
            for (ClassDef cDef : cDefs) {
                String tName = types.get(cDef.getTypeIndex());
                classDefTable.put(tName, cDef);
            }

        }

        ClassDef result = classDefTable.get(type);
        if (result == null) {
            if (type.startsWith("["))
                return null;
            else
                throw new IllegalArgumentException(String.format(
                        "type %s could not be found.", type));
        }

        return classDefTable.get(type);

    }

    public Iterable<ClassDef> classDefs() {
        return new Iterable<ClassDef>() {

            public Iterator<ClassDef> iterator() {
                return classDefList().iterator();
                /*
                 * if (!tableOfContents.classDefs.exists()) { return
                 * Collections.<ClassDef> emptySet().iterator(); } return new
                 * Iterator<ClassDef>() { private DexBuffer.Section in =
                 * open(tableOfContents.classDefs.off); private int count = 0;
                 * public boolean hasNext() { return count <
                 * tableOfContents.classDefs.size; } public ClassDef next() { if
                 * (!hasNext()) { throw new NoSuchElementException(); } count++;
                 * return in.readClassDef(); } public void remove() { throw new
                 * UnsupportedOperationException(); } };
                 */
            }
        };
    }

    public Iterable<BufferedClassDefItem> bufferedClassDefItems() {
        return new Iterable<BufferedClassDefItem>() {

            public Iterator<BufferedClassDefItem> iterator() {
                if (!tableOfContents.classDefs.exists()) {
                    return Collections.<BufferedClassDefItem> emptySet()
                            .iterator();
                }
                return new Iterator<BufferedClassDefItem>() {

                    private DexBuffer.Section in = open(tableOfContents.classDefs.off);
                    private int count = 0;

                    public boolean hasNext() {
                        return count < tableOfContents.classDefs.size;
                    }

                    public BufferedClassDefItem next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        count++;
                        int offset = tableOfContents.classDefs.off
                                + (count * SizeOf.CLASS_DEF_ITEM);
                        return new BufferedClassDefItem(offset);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public TypeList readTypeList(int offset) {
        if (offset == 0) {
            return TypeList.EMPTY;
        }
        return open(offset).readTypeList();
    }

    public ClassData readClassData(ClassDef classDef) {

        int offset = classDef.getClassDataOffset();
        if (offset == 0)
            throw new IllegalArgumentException("offset == 0");

        return classData().get(offset);
    }

    /**
     * Reads the code for the method from the buffer.
     * 
     * @param method the method to read code for. An IllegalArgumentException
     *            will be thrown if there is no code for the method stored in
     *            the buffer (mehod.getCodeOffset == 0). To avoid the exception
     *            make sure method.getCodeOffset() > 0 before calling this
     *            fucntion.
     * @return the code object for this method.
     * @throws IllegalArgumentException if there is no code in the buffer
     *             associated iwth the method.
     */
    public Code readCode(ClassData.Method method) {
        int offset = method.getCodeOffset();
        if (offset == 0) {
            throw new IllegalArgumentException("offset == 0");
        }
        return open(offset).readCode();
    }

    /**
     * Returns the ClassData.Field object that is associated with a particular
     * FieldId.
     * 
     * @param fId the FieldId to search for.
     * @return the ClassData.Field object that points to the FieldId.
     */
    public ClassData.Field getClassDataField(FieldId fId) {
        List<FieldId> fields = fieldIds();

        int fIdIdx = fields.indexOf(fId);
        if (fIdIdx == -1)
            throw new IllegalStateException(
                    "Field id not found in the field identifier list.");

        ClassDef cDef = getDef(fId.getDeclaringClassType());
        ClassData cData = readClassData(cDef);
        ClassData.Field[] cDataFields = cData.allFields();

        for (int i = 0; i < cDataFields.length; i++) {
            ClassData.Field cdField = cDataFields[i];
            if (cdField.getFieldIndex() == fIdIdx)
                return cdField;
        }

        throw new IllegalArgumentException(
                "unable to resolve field ID to a ClassData.Field object.");
    }

    public final class Section implements ByteInput, ByteOutput {

        private final String name;
        private int position;
        private final int limit;
        private final int start;

        private Section(String name, int position, int limit) {
            this.name = name;
            this.position = position;
            this.start = position;
            this.limit = limit;
        }

        private Section(int position) {
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
            return new TypeList(DexBuffer.this, types);
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
            return new FieldId(DexBuffer.this, declaringClassIndex, typeIndex,
                    nameIndex);
        }

        public MethodId readMethodId() {
            int declaringClassIndex = readUnsignedShort();
            int protoIndex = readUnsignedShort();
            int nameIndex = readInt();
            return new MethodId(DexBuffer.this, declaringClassIndex,
                    protoIndex, nameIndex);
        }

        public ProtoId readProtoId() {
            int shortyIndex = readInt();
            int returnTypeIndex = readInt();
            int parametersOffset = readInt();
            return new ProtoId(DexBuffer.this, shortyIndex, returnTypeIndex,
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
            return new ClassDef(DexBuffer.this, offset, type, accessFlags,
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
            Code.CatchHandler result = new Code.CatchHandler(typeIndexes,
                    typeSignatures, addresses, catchAllAddress);
            result.handlerOffset = handlerOffset;

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
            return new Annotation(DexBuffer.this, visibility, typeIndex, names,
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
                Section annotationSection = DexBuffer.this
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

    public final class BufferedClassDefItem extends BufferItem {

        protected BufferedClassDefItem(int offset) {
            super(offset);
        }

        public int getInterfacesOffset() {
            // interfaces offset is 12 bytes beyond Offset.
            Section sec = new Section(getOffset() + 12);
            return sec.readInt();
        }

        public void setInterfacesOffset(int offset) {
            Section sec = new Section(getOffset() + 12);
            sec.writeInt(offset);
        }

        public int getAnnotationsOffset() {
            // interfaces offset is 20 bytes beyond Offset.
            Section sec = new Section(getOffset() + 20);
            return sec.readInt();
        }

        public void setAnnotationsOffset(int offset) {
            Section sec = new Section(getOffset() + 20);
            sec.writeInt(offset);
        }

        public int getClassDataOffset() {
            // interfaces offset is 24 bytes beyond Offset.
            Section sec = new Section(getOffset() + 24);
            return sec.readInt();
        }

        public void setClassDataOffset(int offset) {
            Section sec = new Section(getOffset() + 24);
            sec.writeInt(offset);
        }

        public int getStaticValuesOffset() {
            // interfaces offset is 24 bytes beyond Offset.
            Section sec = new Section(getOffset() + 28);
            return sec.readInt();
        }

        public void setStaticValuesOffset(int offset) {
            Section sec = new Section(getOffset() + 28);
            sec.writeInt(offset);
        }

        @Override
        public List<AddressPointer> getAddressPointers() {
            List<AddressPointer> result = new LinkedList<AddressPointer>();
            result.add(new AddressPointer(getOffset() + 12)); // interfaces
            result.add(new AddressPointer(getOffset() + 20)); // annotations
            result.add(new AddressPointer(getOffset() + 24)); // class data
            result.add(new AddressPointer(getOffset() + 28)); // static values

            return result;
        }

    }

    public final class BufferedProtoIdItem extends BufferItem {

        protected BufferedProtoIdItem(int offset) {
            super(offset);
        }

        public int getParametersOffset() {
            // parameters offset is two integers beyond the offset.
            Section sec = new Section(getOffset() + 8);

            return sec.readInt();

        }

        public void setParametersOffset(int pOffset) {
            Section sec = new Section(getOffset() + 8);

            sec.writeInt(pOffset);
        }

        @Override
        public List<AddressPointer> getAddressPointers() {
            List<AddressPointer> result = new LinkedList<AddressPointer>();
            result.add(new AddressPointer(getOffset() + 8));
            return result;
        }

    }

    /**
     * Contains raw data about a string_id_item
     * 
     * @author rschilling
     */
    public final class BufferedStringIdItem extends BufferItem {

        public BufferedStringIdItem(int offset) {
            super(offset);
        }

        public int getStringOffset() {
            Section sec = new Section(getOffset());
            return sec.readInt();
        }

        public void setStringOffset(int offset) {
            Section sec = new Section(offset);
            sec.writeInt(offset);
        }

        @Override
        public List<AddressPointer> getAddressPointers() {

            List<AddressPointer> result = new LinkedList<AddressPointer>();
            AddressPointer ap = new AddressPointer(getOffset());
            result.add(ap);

            return result;
        }

    }

    public final class BufferedAnnotationSetRefItem extends BufferItem {

        public BufferedAnnotationSetRefItem(int offset) {
            super(offset);
        }

        public int getAnnotationsOffset() {
            Section sec = new Section(getOffset());
            return sec.readInt();
        }

        public void setAnnotationsOffset(int offset) {
            Section sec = new Section(getOffset());
            sec.writeInt(offset);
        }

        @Override
        public List<AddressPointer> getAddressPointers() {
            List<AddressPointer> result = new LinkedList<AddressPointer>();
            result.add(new AddressPointer(getOffset()));
            return result;
        }

    }

    public final class BufferedAnnotationSetItem extends BufferItem {

        public BufferedAnnotationSetItem(int offset) {
            super(offset);
        }

        public int getAnnotationOffset() {
            Section sec = new Section(getOffset());
            return sec.readInt();
        }

        public void setAnnotationOffset(int offset) {
            Section sec = new Section(getOffset());
            sec.writeInt(offset);
        }

        @Override
        public List<AddressPointer> getAddressPointers() {
            List<AddressPointer> result = new LinkedList<AddressPointer>();
            result.add(new AddressPointer(getOffset()));
            return result;
        }

    }

    public final class BufferedCodeItem extends BufferItem {

        public BufferedCodeItem(int offset) {
            super(offset);
        }

        public int getDebugInfoOffset() {
            // debug info offset is four shorts after offset
            Section sec = new Section(getOffset() + (SizeOf.USHORT * 4));
            return sec.readInt();
        }

        public void setDebugInfoOffset(int offset) {
            Section sec = new Section(getOffset() + (SizeOf.USHORT * 4));
            sec.writeInt(offset);
        }

        @Override
        public List<AddressPointer> getAddressPointers() {
            List<AddressPointer> result = new LinkedList<AddressPointer>();
            // debug info offset is four shorts after offset
            result.add(new AddressPointer(getOffset() + (SizeOf.USHORT * 4)));
            return result;
        }

    }

    public final class BufferedAnnotationDirectoryItem extends BufferItem {

        public BufferedAnnotationDirectoryItem(int offset) {
            super(offset);
        }

        public int getClassAnnotationsOffset() {
            Section sec = new Section(getOffset());
            return sec.readInt();
        }

        public void adjustOffsets(int position, int adjustment) {

            List<AddressPointer> pointers = getAddressPointers();

            for (AddressPointer pointer : pointers) {
                pointer.adjustReferencedAddress(position, adjustment);
            }

        }

        @Override
        public List<AddressPointer> getAddressPointers() {
            LinkedList<AddressPointer> result = new LinkedList<AddressPointer>();

            result.add(new AddressPointer(getOffset())); // the first address
                                                         // pointer is right
                                                         // at the beginning
                                                         // of this section.

            Section sec = new Section(getOffset()); // for reading the section

            // read through the section and collect address pointers.
            sec.readInt(); // burn it - we got the address already but need to
                           // advance the pointer.
            int fieldsSize = sec.readInt();
            int annotatedMethodsSize = sec.readInt();
            int annotatedParametersSize = sec.readInt();

            int readCount = fieldsSize + annotatedMethodsSize
                    + annotatedParametersSize;

            for (int i = 0; i < readCount; i++) {

                // read through readCount index/address pointer pairs.
                sec.readInt(); // skip over index
                result.add(new AddressPointer(sec.getPosition()));

            }

            return result;
        }
    }

    /**
     * Return all of the offset references in the entire file from all sections.
     * This ultra-handy routine allows all offset referencs in the entire DEX
     * file to be easily updated.
     * 
     * @return a list of all offset references in the file.
     */
    public List<AddressPointer> getAllAddressPointers() {

        LinkedList<AddressPointer> result = new LinkedList<AddressPointer>();

        for (BufferedStringIdItem item : bufferedStringIds()) {
            result.addAll(item.getAddressPointers());
        }

        for (BufferedProtoIdItem item : bufferedProtoIds()) {
            result.addAll(item.getAddressPointers());
        }

        for (BufferedClassDefItem item : bufferedClassDefItems()) {
            result.addAll(item.getAddressPointers());
        }

        for (BufferedAnnotationSetRefItem item : bufferedAnnotationSetRefItems()) {
            result.addAll(item.getAddressPointers());
        }

        for (BufferedAnnotationSetItem item : bufferedAnnotationSetItems()) {
            result.addAll(item.getAddressPointers());
        }

        return result;
    }

    public abstract class BufferItem {

        private int offset;

        protected BufferItem(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }

        public abstract List<AddressPointer> getAddressPointers();
    }

    /**
     * An address pointer. Reads and writes a single address to a location in
     * the buffer (an int value).
     * 
     * @author rschilling
     */
    public class AddressPointer {

        private final int address;

        public AddressPointer(int position) {
            address = position;
        }

        public int getAddress() {
            return address;
        }

        public int getContents() {
            Section sec = new Section(address);
            return sec.readInt();
        }

        public void setContents(int addr) {
            Section sec = new Section(address);
            sec.writeInt(addr);
        }

        public void adjustReferencedAddress(int position, int adjustment) {
            int contents = getContents();
            if (contents >= position) {
                int newAddress = contents + adjustment;
                Log.i(LOG_TAG, "transforming address stored at " + address
                        + " " + contents + "->" + newAddress);
                setContents(newAddress);
            }
        }
    }

    /**
     * Given two class signatures, find the common super class. If the class is
     * not found in the types list than an error is thrown. If the signature
     * does not resolve to a class then the signature for java.lang.Object is
     * returned.
     * 
     * @param signature1 the first signature
     * @param signature2 the second signature.
     * @return the signature of the common super class.
     */
    public String findCommonSuperclass(String c1, String c2) {

        if (c1.equals(c2))
            return c2;

        List<String> types = typeNames();
        ClassDef def1 = getDef(c1);
        ClassDef def2 = getDef(c2);

        return findCommonSuperclass(def1, def2);
    }

    public String findCommonSuperclass(ClassDef def1, ClassDef def2) {
        if (def1 == null || def2 == null)
            return DvmGlobals.JAVA_LANG_OBJECT_SIGNATURE;

        String c1 = def1.getSignature();
        String c2 = def2.getSignature();

        if (def1.dvmIsInterfaceClass() && def2.dvmImplements(def1)) {
            Log.d(LOG_TAG,
                    String.format("COMMON/I1: %s + %s --> %s", c1, c2, c1));

            return c1;
        }

        if (def2.dvmIsInterfaceClass() && def1.dvmImplements(c2)) {
            Log.d(LOG_TAG,
                    String.format("COMMON/I2: %s + %s --> %s", c1, c2, c2));
        }

        if (def1.dvmIsArrayClass() && def2.dvmIsArrayClass()) {
            return findCommonArraySuperClass(def1, def2);
        }

        return digForSuperclass(c1, c2);

    }

    /**
     * Merge two array classes. We can't use the general "walk up to the
     * superclass" merge because the superclass of an array is always Object. We
     * want String[] + Integer[] = Object[]. This works for higher dimensions as
     * well, e.g. String[][] + Integer[][] = Object[][].
     * <p>
     * If Foo1 and Foo2 are subclasses of Foo, Foo1[] + Foo2[] = Foo[].
     * <p>
     * If Class implements Type, Class[] + Type[] = Type[].
     * <p>
     * If the dimensions don't match, we want to convert to an array of Object
     * with the least dimension, e.g. String[][] + String[][][][] = Object[][].
     * <p>
     * Arrays of primitive types effectively have one less dimension when
     * merging. int[] + float[] = Object, int[] + String[] = Object, int[][] +
     * float[][] = Object[], int[][] + String[] = Object[].
     * <p>
     * The only time this function doesn't return signature with an array
     * specifier in front ('[') is when one of the arguments is a 1-dimensional
     * primitive array.
     * <p>
     * This gets a little awkward because we may have to ask the VM to create a
     * new array type with the appropriate element and dimensions. However, we
     * shouldn't be doing this often.
     * 
     * @param c1 the first array class
     * @param c2 the second array class
     * @return the signature of the common super class.
     * @throws IllegalArgumentException if any parameter is null or not an array
     *             class.
     */
    public String findCommonArraySuperClass(ClassDef c1, ClassDef c2) {

        if (c1 == null)
            throw new IllegalArgumentException("c1 cannot be null");

        if (c2 == null)
            throw new IllegalArgumentException("c2 cannot be null");

        if (!c1.dvmIsArrayClass())
            throw new IllegalArgumentException("c1 must be an array class.");

        if (!c2.dvmIsArrayClass())
            throw new IllegalArgumentException("c2 must be an array class.");

        String commonElem = DvmGlobals.JAVA_LANG_OBJECT_SIGNATURE;
        int numDims = 0;
        int arrayDim1 = c1.getArrayDimensions();
        int arrayDim2 = c2.getArrayDimensions();
        boolean hasPrimitive = false;

        if (c1.dvmIsPrimitiveClass()) {
            arrayDim1--;
            hasPrimitive = true;
        }
        if (c2.dvmIsPrimitiveClass()) {
            arrayDim2--;
            hasPrimitive = true;
        }

        if (!hasPrimitive && arrayDim1 == arrayDim2) {
            /*
             * Two arrays of reference types with equal dimensions. Try to find
             * a good match.
             */
            commonElem = findCommonSuperclass(c1, c2);
            numDims = arrayDim1;
        } else {
            /*
             * Mismatched array depths and/or array(s) of primitives. We want
             * Object, or an Object array with appropriate dimensions. We
             * initialize arrayClass to Object here, because it's possible for
             * us to set numDims=0.
             */
            if (arrayDim1 < arrayDim2)
                numDims = arrayDim1;
            else
                numDims = arrayDim2;
            // commonElem remains java.lang.Object
        }

        StringBuilder result = new StringBuilder();
        /*
         * Find an appropriately-dimensioned array class. This is easiest to do
         * iteratively, using the array class found by the current round as the
         * element type for the next round.
         */
        for (int i = 0; i < numDims; i++) {
            result.append('[');
        }
        result.append(commonElem);

        Log.v(LOG_TAG, String.format("ArrayMerge '%s' + '%s' --> '%s'",
                c1.getSignature(), c2.getSignature(), result.toString()));

        return result.toString();

    }

    /**
     * Given two classes, walk up the superclass tree to find a common ancestor.
     * (Called from findCommonSuperclass().) TODO: consider caching the class
     * depth in the class object so we don't have to search for it here. TODO:
     * move this to the DexModel.
     */
    public String digForSuperclass(String c1, String c2) {

        List<ClassDef> cDefs = classDefList();

        int depth1 = getClassDepth(c1);
        int depth2 = getClassDepth(c2);

        Log.v(LOG_TAG, String.format("COMMON: %s(%d) + %s(%d)", c1, depth1, c2,
                depth2));

        /* pull the deepest one up */
        if (depth1 > depth2) {
            while (depth1 > depth2) {
                c1 = getSuperclassOf(c1);
                depth1--;
            }
        } else {
            while (depth2 > depth1) {
                c2 = getSuperclassOf(c2);
                depth2--;
            }
        }

        /* walk up in lock-step */
        while (!c1.equals(c2)) {
            c1 = getSuperclassOf(c1);
            c2 = getSuperclassOf(c2);
        }

        Log.v(LOG_TAG, String.format("      : --> %s", c1));

        return c1;
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
     * Counts the number of functions with code in them.
     * 
     * @return
     */
    public int countFunctions() {
        int result = 0;

        List<ClassDef> classDefs = classDefList();
        for (ClassDef cDef : classDefs) {
            if (cDef.getClassDataOffset() > 0) {
                ClassData cData = readClassData(cDef);
                ClassData.Method[] methods = cData.allMethods();
                for (ClassData.Method method : methods) {
                    if (method.getCodeOffset() > 0)
                        result++;
                }

            }
        }
        return result;

    }

    /**
     * Finds a method or return null.
     * 
     * @param definingClassName the class that defines the method
     * @param methdodName the method name
     * @param prototypeId the prototype of the method
     * @return the corresponding method, or null if none found.
     */
    public MethodId findMethodId(String definingClassName, String methodName,
            int prototypeId) {

        List<MethodId> methodIds = methodIds();
        for (MethodId mId : methodIds) {
            if (!mId.getDeclaringClassSignature().equals(definingClassName))
                continue;
            if (!mId.getName().equals(methodName))
                continue;

            if (mId.getProtoIndex() != prototypeId)
                continue;

            return mId;

        }

        return null;
    }

}
