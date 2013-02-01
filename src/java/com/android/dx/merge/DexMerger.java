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

package com.android.dx.merge;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gnu.salish.debug.util.FinnrApp;
import android.util.Log;

import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.io.Annotation;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.DexHasher;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.util.DexException;

/**
 * Combine two dex files into one.
 */
public class DexMerger {

    /**
     * Turn on and off logging for this class' activities.
     */
    public static final boolean LOG_MERGE = false;

    /**
     * The number of functions found in the dex file.
     */
    public int functionCount = 0;

    /**
     * The number of functions processed.
     */
    public int functionsProcessed = 0;

    /**
     * The number of seconds it took to complete the merge.
     */
    public long elapsedSeconds = 0;

    /**
     * Add these many bytes to a section to ensure that we don't run out of
     * space when appending.
     */
    public static final int SECTION_SAFETY = 4096;

    /**
     * After compacting each section, add these number of bytes to each section
     * that might need it.
     */
    public static final int SECTION_PADDING = 2;

    public static final String LOG_TAG = "DexMerger";

    protected final DexBuffer dexA;
    protected final DexBuffer dexB;
    protected final CollisionPolicy collisionPolicy;
    protected final WriterSizes writerSizes;

    protected final DexBuffer dexOut = new DexBuffer();

    protected final DexBuffer.Section headerOut;

    /** All IDs and definitions sections */
    protected final DexBuffer.Section idsDefsOut;

    protected final DexBuffer.Section mapListOut;

    protected final DexBuffer.Section typeListOut;

    protected final DexBuffer.Section classDataOut;

    protected final DexBuffer.Section codeOut;

    protected final DexBuffer.Section stringDataOut;

    protected final DexBuffer.Section debugInfoOut;

    protected final DexBuffer.Section encodedArrayOut;

    /** annotations directory on a type */
    protected final DexBuffer.Section annotationsDirectoryOut;

    /** sets of annotations on a member, parameter or type */
    protected final DexBuffer.Section annotationSetOut;

    /** parameter lists */
    protected final DexBuffer.Section annotationSetRefListOut;

    /** individual annotations, each containing zero or more fields */
    protected final DexBuffer.Section annotationOut;

    protected final TableOfContents contentsOut;

    protected final IndexMap aIndexMap;
    protected final IndexMap bIndexMap;
    protected final InstructionTransformer aInstructionTransformer;
    protected final InstructionTransformer bInstructionTransformer;

    /**
     * minimum number of wasted bytes before it's worthwhile to compact the
     * result
     */
    private int compactWasteThreshold = 1;

    private void throwIfInterrupted() throws InterruptedException {

        if (Thread.interrupted())
            throw new InterruptedException("processing cancelled");

    }

    public DexMerger(DexBuffer dexA, DexBuffer dexB,
            CollisionPolicy collisionPolicy) throws IOException {
        this(dexA, dexB, collisionPolicy, new WriterSizes(dexA, dexB,
                SECTION_SAFETY));
    }

    protected DexMerger(DexBuffer dexA, DexBuffer dexB,
            CollisionPolicy collisionPolicy, WriterSizes writerSizes)
            throws IOException {

        this.dexA = dexA;
        this.dexB = dexB;
        this.collisionPolicy = collisionPolicy;
        this.writerSizes = writerSizes;

        TableOfContents aContents = dexA.getTableOfContents();
        TableOfContents bContents = dexB.getTableOfContents();
        aIndexMap = new IndexMap(dexOut, aContents);
        bIndexMap = new IndexMap(dexOut, bContents);
        aInstructionTransformer = new InstructionTransformer(aIndexMap);
        bInstructionTransformer = new InstructionTransformer(bIndexMap);

        headerOut = dexOut.appendSection(writerSizes.header, "header");
        idsDefsOut = dexOut.appendSection(writerSizes.idsDefs, "ids defs");

        contentsOut = dexOut.getTableOfContents();
        contentsOut.dataOff = dexOut.getLength();

        contentsOut.mapList.off = dexOut.getLength();
        contentsOut.mapList.size = 1;
        mapListOut = dexOut.appendSection(writerSizes.mapList, "map list");

        contentsOut.typeLists.off = dexOut.getLength();
        typeListOut = dexOut.appendSection(writerSizes.typeList, "type list");

        contentsOut.annotationSetRefLists.off = dexOut.getLength();
        annotationSetRefListOut = dexOut.appendSection(
                writerSizes.annotationsSetRefList, "annotation set ref list");

        contentsOut.annotationSets.off = dexOut.getLength();
        annotationSetOut = dexOut.appendSection(writerSizes.annotationsSet,
                "annotation sets");

        contentsOut.classDatas.off = dexOut.getLength();
        classDataOut = dexOut
                .appendSection(writerSizes.classData, "class data");

        contentsOut.codes.off = dexOut.getLength();
        codeOut = dexOut.appendSection(writerSizes.code, "code");

        contentsOut.stringDatas.off = dexOut.getLength();
        stringDataOut = dexOut.appendSection(writerSizes.stringData,
                "string data");

        contentsOut.debugInfos.off = dexOut.getLength();
        debugInfoOut = dexOut
                .appendSection(writerSizes.debugInfo, "debug info");

        contentsOut.annotations.off = dexOut.getLength();
        annotationOut = dexOut.appendSection(writerSizes.annotation,
                "annotation");

        contentsOut.encodedArrays.off = dexOut.getLength();
        encodedArrayOut = dexOut.appendSection(writerSizes.encodedArray,
                "encoded array");

        contentsOut.annotationsDirectories.off = dexOut.getLength();
        annotationsDirectoryOut = dexOut.appendSection(
                writerSizes.annotationsDirectory, "annotations directory");

        dexOut.noMoreSections();
        contentsOut.dataSize = dexOut.getLength() - contentsOut.dataOff;
    }

    public void setCompactWasteThreshold(int compactWasteThreshold) {
        this.compactWasteThreshold = compactWasteThreshold;
    }

    public WriterSizes getSizes() {
        return writerSizes;
    }

    // TODO Update mergeDexBuffers so that the destination buffer doesn't need
    // to know how much space to take up (stretchy sections).
    private DexBuffer mergeDexBuffers(String statusPrefix) throws IOException,
            InterruptedException {

        String prefix = statusPrefix != null ? statusPrefix + ": " : null;
        if (prefix != null)
            FinnrApp.updateProgressBarMessage(prefix + "strings");

        mergeStringIds();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            // FinnrApp.updateProgressBarMessage(prefix + "types");
        }

        mergeTypeIds();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            // FinnrApp.updateProgressBarMessage(prefix + "type lists");
        }

        mergeTypeLists();
        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            FinnrApp.updateProgressBarMessage(prefix + "methods");
        }

        mergeProtoIds();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            FinnrApp.updateProgressBarMessage(prefix + "fields");
        }

        mergeFieldIds();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            FinnrApp.updateProgressBarMessage(prefix + "classes"); // "method IDs");
        }

        mergeMethodIds();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            // FinnrApp.updateProgressBarMessage(prefix + "annotations ");
        }
        mergeAnnotations();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            // FinnrApp.updateProgressBarMessage(prefix
            // + "annotation sets/directories");
        }

        unionAnnotationSetsAndDirectories();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            // FinnrApp.updateProgressBarMessage(prefix + "class defs");
        }
        mergeClassDefs();

        if (prefix != null) {
            FinnrApp.incrementProgressBarProgress(1);
            // FinnrApp.updateProgressBarMessage(prefix + "saving");
        }

        // write the header
        contentsOut.header.off = 0;
        contentsOut.header.size = 1;
        contentsOut.fileSize = dexOut.getLength();
        contentsOut.computeSizesFromOffsets();
        contentsOut.writeHeader(headerOut);
        contentsOut.writeMap(mapListOut);

        // generate and write the hashes
        new DexHasher().writeHashes(dexOut);
        FinnrApp.incrementProgressBarProgress(1);

        return dexOut;
    }

    public DexBuffer merge(String statusPrefix) throws IOException,
            InterruptedException {
        long start = System.nanoTime();

        DexBuffer result = mergeDexBuffers(statusPrefix);

        /*
         * Create a WriterSizes object that contains the exact amount of space
         * needed for both files, now that we know how much space that is.
         */
        WriterSizes compactedSizes = new WriterSizes(this);

        int wastedByteCount = writerSizes.size() - compactedSizes.size();
        if (wastedByteCount > +compactWasteThreshold) {

            if (statusPrefix != null) {
                FinnrApp.incrementProgressBarProgress(1);
                FinnrApp.updateProgressBarMessage(statusPrefix + ": optimizing");
            }

            DexMerger compacter = new DexMerger(dexOut, new DexBuffer(),
                    CollisionPolicy.FAIL, compactedSizes);
            result = compacter.mergeDexBuffers(null);
            System.out
                    .printf("Result compacted from %.1fKiB to %.1fKiB to save %.1fKiB%n",
                            dexOut.getLength() / 1024f,
                            result.getLength() / 1024f, wastedByteCount / 1024f);
        }

        long elapsed = System.nanoTime() - start;
        float elapsedSeconds = elapsed / 1000000000f;
        if (DO_LOG)
            Log.i(LOG_TAG,
                    String.format(
                            "Merged dex A (%d defs/%.1fKiB) with dex B "
                                    + "(%d defs/%.1fKiB). Result is %d defs/%.1fKiB. Took %.1fs%n",
                            dexA.getTableOfContents().classDefs.size,
                            dexA.getLength() / 1024f,
                            dexB.getTableOfContents().classDefs.size,
                            dexB.getLength() / 1024f,
                            result.getTableOfContents().classDefs.size,
                            result.getLength() / 1024f, elapsedSeconds));

        return result;
    }

    /**
     * iterates through the DEX file and counts the functions. Also sets
     * this.functionCount
     * 
     * @return
     */
    public int countFunctions() {
        int result = 0;
        SortableType[] types = getSortedTypes();
        contentsOut.classDefs.off = idsDefsOut.getPosition();
        contentsOut.classDefs.size = types.length;

        for (SortableType type : types) {
            DexBuffer in = type.getBuffer();
            IndexMap indexMap = (in == dexA) ? aIndexMap : bIndexMap;
            result += countFunctionsInClassDefs(in, type.getClassDef(),
                    indexMap);
        }

        this.functionCount = result;
        return result;
    }

    /**
     * Counts the functions in the ClassDef.
     * 
     * @param in the buffer to read data from
     * @param classDef the class def to process
     * @param indexMap ??
     * @return the number of functions encountered.
     */
    private int countFunctionsInClassDefs(DexBuffer in, ClassDef classDef,
            IndexMap indexMap) {

        int result = 0;
        int classDataOff = classDef.getClassDataOffset();
        if (classDataOff == 0) {
            result = 0;
        } else {
            ClassData classData = in.readClassData(classDef);
            result = countFunctionsInClassData(in, classData, indexMap);
        }

        return result;

    }

    private int countFunctionsInClassData(DexBuffer in, ClassData classData,
            IndexMap indexMap) {

        int result = 0;
        ClassData.Method[] directMethods = classData.getDirectMethods();
        ClassData.Method[] virtualMethods = classData.getVirtualMethods();
        result += countMethods(in, indexMap, directMethods);
        result += countMethods(in, indexMap, virtualMethods);

        return result;
    }

    private int countMethods(DexBuffer in, IndexMap indexMap, Method[] methods) {
        int result = 0;

        for (ClassData.Method method : methods) {

            if (method.getCodeOffset() > 0)
                result++;
        }
        return result;
    }

    /**
     * Reads an IDs section of two dex files and writes an IDs section of a
     * merged dex file. Populates maps from old to new indices in the process.
     */
    abstract class IdMerger<T extends Comparable<T>> {

        private final DexBuffer.Section out;

        protected IdMerger(DexBuffer.Section out) {
            this.out = out;
        }

        /**
         * Merges already-sorted sections, reading only two values into memory
         * at a time.
         * 
         * @throws InterruptedException if processing has been interrupted.
         */
        public final void mergeSorted() throws InterruptedException {
            TableOfContents.Section aSection = getSection(dexA
                    .getTableOfContents());
            TableOfContents.Section bSection = getSection(dexB
                    .getTableOfContents());
            getSection(contentsOut).off = out.getPosition();

            DexBuffer.Section inA = aSection.exists() ? dexA.open(aSection.off)
                    : null;
            DexBuffer.Section inB = bSection.exists() ? dexB.open(bSection.off)
                    : null;
            int aOffset = -1;
            int bOffset = -1;
            int aIndex = 0;
            int bIndex = 0;
            int outCount = 0;
            T a = null;
            T b = null;

            while (true) {

                if (a == null && aIndex < aSection.size) {
                    aOffset = inA.getPosition();
                    a = read(inA, aIndexMap, aIndex);
                }
                if (b == null && bIndex < bSection.size) {
                    bOffset = inB.getPosition();
                    b = read(inB, bIndexMap, bIndex);
                }

                // Write the smaller of a and b. If they're equal, write only
                // once
                boolean advanceA;
                boolean advanceB;
                if (a != null && b != null) {
                    int compare = a.compareTo(b);
                    advanceA = compare <= 0;
                    advanceB = compare >= 0;
                } else {
                    advanceA = (a != null);
                    advanceB = (b != null);
                }

                T toWrite = null;
                if (advanceA) {
                    toWrite = a;
                    updateIndex(aOffset, aIndexMap, aIndex++, outCount);
                    a = null;
                    aOffset = -1;
                }
                if (advanceB) {
                    toWrite = b;
                    updateIndex(bOffset, bIndexMap, bIndex++, outCount);
                    b = null;
                    bOffset = -1;
                }
                if (toWrite == null) {
                    break; // advanceA == false && advanceB == false
                }
                write(toWrite);
                outCount++;
                throwIfInterrupted();
            }

            getSection(contentsOut).size = outCount;
        }

        /**
         * Merges unsorted sections by reading them completely into memory and
         * sorting in memory.
         * 
         * @throws InterruptedException if the thread running this code has been
         *             interrupted.
         */
        public final void mergeUnsorted() throws InterruptedException {
            getSection(contentsOut).off = out.getPosition();

            List<UnsortedValue> all = new ArrayList<UnsortedValue>();
            all.addAll(readUnsortedValues(dexA, aIndexMap));
            all.addAll(readUnsortedValues(dexB, bIndexMap));
            Collections.sort(all);

            throwIfInterrupted();

            int outCount = 0;
            for (int i = 0; i < all.size();) {
                UnsortedValue e1 = all.get(i++);
                updateIndex(e1.offset, getIndexMap(e1.source), e1.index,
                        outCount - 1);

                while (i < all.size() && e1.compareTo(all.get(i)) == 0) {
                    UnsortedValue e2 = all.get(i++);
                    updateIndex(e2.offset, getIndexMap(e2.source), e2.index,
                            outCount - 1);
                }

                write(e1.value);
                outCount++;
                throwIfInterrupted();
            }

            getSection(contentsOut).size = outCount;
        }

        private List<UnsortedValue> readUnsortedValues(DexBuffer source,
                IndexMap indexMap) {
            TableOfContents.Section section = getSection(source
                    .getTableOfContents());
            if (!section.exists()) {
                return Collections.emptyList();
            }

            List<UnsortedValue> result = new ArrayList<UnsortedValue>();
            DexBuffer.Section in = source.open(section.off);
            for (int i = 0; i < section.size; i++) {
                int offset = in.getPosition();
                T value = read(in, indexMap, 0);
                result.add(new UnsortedValue(source, indexMap, value, i, offset));
            }
            return result;
        }

        abstract TableOfContents.Section getSection(
                TableOfContents tableOfContents);

        abstract T read(DexBuffer.Section in, IndexMap indexMap, int index);

        abstract void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                int newIndex);

        abstract void write(T value);

        class UnsortedValue implements Comparable<UnsortedValue> {

            final DexBuffer source;
            final IndexMap indexMap;
            final T value;
            final int index;
            final int offset;

            UnsortedValue(DexBuffer source, IndexMap indexMap, T value,
                    int index, int offset) {
                this.source = source;
                this.indexMap = indexMap;
                this.value = value;
                this.index = index;
                this.offset = offset;
            }

            public int compareTo(UnsortedValue unsortedValue) {
                return value.compareTo(unsortedValue.value);
            }
        }
    }

    private IndexMap getIndexMap(DexBuffer dexBuffer) {
        if (dexBuffer == dexA) {
            return aIndexMap;
        } else if (dexBuffer == dexB) {
            return bIndexMap;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void mergeStringIds() throws InterruptedException {
        new IdMerger<String>(idsDefsOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.stringIds;
            }

            @Override
            String read(DexBuffer.Section in, IndexMap indexMap, int index) {
                return in.readString();
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.stringIds[oldIndex] = newIndex;
            }

            @Override
            void write(String value) {
                contentsOut.stringDatas.size++;
                idsDefsOut.writeInt(stringDataOut.getPosition());
                stringDataOut.writeStringData(value);
            }
        }.mergeSorted();
    }

    private void mergeTypeIds() throws InterruptedException {
        new IdMerger<Integer>(idsDefsOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.typeIds;
            }

            @Override
            Integer read(DexBuffer.Section in, IndexMap indexMap, int index) {
                int stringIndex = in.readInt();
                return indexMap.adjustString(stringIndex);
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.typeIds[oldIndex] = (short) newIndex;
            }

            @Override
            void write(Integer value) {
                idsDefsOut.writeInt(value);
            }
        }.mergeSorted();
    }

    private void mergeTypeLists() throws InterruptedException {
        new IdMerger<TypeList>(typeListOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.typeLists;
            }

            @Override
            TypeList read(DexBuffer.Section in, IndexMap indexMap, int index) {
                return indexMap.adjustTypeList(in.readTypeList());
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.putTypeListOffset(offset, typeListOut.getPosition());
            }

            @Override
            void write(TypeList value) {
                typeListOut.writeTypeList(value);
            }
        }.mergeUnsorted();
    }

    private void mergeProtoIds() throws InterruptedException {
        new IdMerger<ProtoId>(idsDefsOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.protoIds;
            }

            @Override
            ProtoId read(DexBuffer.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readProtoId());
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.protoIds[oldIndex] = (short) newIndex;
            }

            @Override
            void write(ProtoId value) {
                value.writeTo(idsDefsOut);
            }
        }.mergeSorted();
    }

    private void mergeFieldIds() throws InterruptedException {
        new IdMerger<FieldId>(idsDefsOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.fieldIds;
            }

            @Override
            FieldId read(DexBuffer.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readFieldId());
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.fieldIds[oldIndex] = (short) newIndex;
            }

            @Override
            void write(FieldId value) {
                value.writeTo(idsDefsOut);
            }
        }.mergeSorted();
    }

    private void mergeMethodIds() throws InterruptedException {
        new IdMerger<MethodId>(idsDefsOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.methodIds;
            }

            @Override
            MethodId read(DexBuffer.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readMethodId());
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.methodIds[oldIndex] = (short) newIndex;
            }

            @Override
            void write(MethodId methodId) {
                methodId.writeTo(idsDefsOut);
            }
        }.mergeSorted();
    }

    private void mergeAnnotations() throws InterruptedException {
        new IdMerger<Annotation>(annotationOut) {

            @Override
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.annotations;
            }

            @Override
            Annotation read(DexBuffer.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readAnnotation());
            }

            @Override
            void updateIndex(int offset, IndexMap indexMap, int oldIndex,
                    int newIndex) {
                indexMap.putAnnotationOffset(offset,
                        annotationOut.getPosition());
            }

            @Override
            void write(Annotation value) {
                value.writeTo(annotationOut);
            }
        }.mergeUnsorted();
    }

    private void mergeClassDefs() throws InterruptedException {
        SortableType[] types = getSortedTypes();
        contentsOut.classDefs.off = idsDefsOut.getPosition();
        contentsOut.classDefs.size = types.length;

        for (SortableType type : types) {
            DexBuffer in = type.getBuffer();
            IndexMap indexMap = (in == dexA) ? aIndexMap : bIndexMap;
            transformClassDef(in, type.getClassDef(), indexMap);
            throwIfInterrupted();
        }
    }

    /**
     * Returns the union of classes from both files, sorted in order such that a
     * class is always preceded by its supertype and implemented interfaces.
     */
    private SortableType[] getSortedTypes() {
        // size is pessimistic; doesn't include arrays
        SortableType[] sortableTypes = new SortableType[contentsOut.typeIds.size];
        readSortableTypes(sortableTypes, dexA, aIndexMap);
        readSortableTypes(sortableTypes, dexB, bIndexMap);

        /*
         * Populate the depths of each sortable type. This makes D iterations
         * through all N types, where 'D' is the depth of the deepest type. For
         * example, the deepest class in libcore is Xalan's KeyIterator, which
         * is 11 types deep.
         */
        while (true) {
            boolean allDone = true;
            for (SortableType sortableType : sortableTypes) {
                if (sortableType != null && !sortableType.isDepthAssigned()) {
                    allDone &= sortableType.tryAssignDepth(sortableTypes);
                }
            }
            if (allDone) {
                break;
            }
        }

        // Now that all types have depth information, the result can be sorted
        Arrays.sort(sortableTypes, SortableType.NULLS_LAST_ORDER);

        // Strip nulls from the end
        int firstNull = Arrays.asList(sortableTypes).indexOf(null);
        if (firstNull == -1)
            return sortableTypes;

        SortableType[] result = new SortableType[firstNull];
        System.arraycopy(sortableTypes, 0, result, 0, firstNull);
        return result;

    }

    /**
     * Reads just enough data on each class so that we can sort it and then find
     * it later.
     */
    private void readSortableTypes(SortableType[] sortableTypes,
            DexBuffer buffer, IndexMap indexMap) {
        for (ClassDef classDef : buffer.classDefs()) {
            SortableType sortableType = indexMap.adjust(new SortableType(
                    buffer, classDef));
            int t = sortableType.getTypeIndex();
            if (sortableTypes[t] == null) {
                sortableTypes[t] = sortableType;
            } else if (collisionPolicy != CollisionPolicy.KEEP_FIRST) {
                throw new DexException("Multiple dex files define "
                        + buffer.typeNames().get(classDef.getTypeIndex()));
            }
        }
    }

    /**
     * Copy annotation sets from each input to the output. TODO: this may write
     * multiple copies of the same annotation set. We should shrink the output
     * by merging rather than unioning
     */
    private void unionAnnotationSetsAndDirectories() {
        transformAnnotationSets(dexA, aIndexMap);
        transformAnnotationSets(dexB, bIndexMap);
        transformAnnotationDirectories(dexA, aIndexMap);
        transformAnnotationDirectories(dexB, bIndexMap);
    }

    private void transformAnnotationSets(DexBuffer in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationSets;
        if (section.exists()) {
            DexBuffer.Section setIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationSet(indexMap, setIn);
            }
        }
    }

    private void transformAnnotationDirectories(DexBuffer in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationsDirectories;
        if (section.exists()) {
            DexBuffer.Section directoryIn = in.open(section.off);
            for (int i = 0; i < section.size; i++) {
                transformAnnotationDirectory(in, directoryIn, indexMap);
            }
        }
    }

    /**
     * Reads a class_def_item beginning at {@code in} and writes the index and
     * data.
     */
    protected void transformClassDef(DexBuffer in, ClassDef classDef,
            IndexMap indexMap) {

        if (DO_LOG)
            if (LOG_MERGE)
                Log.d(LOG_TAG,
                        String.format("transforming class %s",
                                classDef.getSignature()));

        idsDefsOut.assertFourByteAligned();
        idsDefsOut.writeInt(classDef.getTypeIndex());
        idsDefsOut.writeInt(classDef.getAccessFlags());
        idsDefsOut.writeInt(classDef.getSupertypeIndex());
        idsDefsOut.writeInt(classDef.getInterfacesOffset());

        int sourceFileIndex = indexMap.adjustString(classDef
                .getSourceFileIndex());
        idsDefsOut.writeInt(sourceFileIndex);

        int annotationsOff = classDef.getAnnotationsOffset();
        idsDefsOut.writeInt(indexMap.adjustAnnotationDirectory(annotationsOff));

        int classDataOff = classDef.getClassDataOffset();
        if (classDataOff == 0) {
            idsDefsOut.writeInt(0);
        } else {
            idsDefsOut.writeInt(classDataOut.getPosition());
            ClassData classData = in.readClassData(classDef);
            transformClassData(in, classData, indexMap);
        }

        int staticValuesOff = classDef.getStaticValuesOffset();
        if (staticValuesOff == 0) {
            idsDefsOut.writeInt(0);
        } else {
            DexBuffer.Section staticValuesIn = in.open(staticValuesOff);
            idsDefsOut.writeInt(encodedArrayOut.getPosition());
            transformStaticValues(staticValuesIn, indexMap);
        }
    }

    /**
     * Transform all annotations on a class.
     */
    private void transformAnnotationDirectory(DexBuffer in,
            DexBuffer.Section directoryIn, IndexMap indexMap) {
        contentsOut.annotationsDirectories.size++;
        annotationsDirectoryOut.assertFourByteAligned();
        indexMap.putAnnotationDirectoryOffset(directoryIn.getPosition(),
                annotationsDirectoryOut.getPosition());

        int classAnnotationsOffset = indexMap.adjustAnnotationSet(directoryIn
                .readInt());
        annotationsDirectoryOut.writeInt(classAnnotationsOffset);

        int fieldsSize = directoryIn.readInt();
        annotationsDirectoryOut.writeInt(fieldsSize);

        int methodsSize = directoryIn.readInt();
        annotationsDirectoryOut.writeInt(methodsSize);

        int parameterListSize = directoryIn.readInt();
        annotationsDirectoryOut.writeInt(parameterListSize);

        for (int i = 0; i < fieldsSize; i++) {
            // field index
            annotationsDirectoryOut.writeInt(indexMap.adjustField(directoryIn
                    .readInt()));

            // annotations offset
            annotationsDirectoryOut.writeInt(indexMap
                    .adjustAnnotationSet(directoryIn.readInt()));
        }

        for (int i = 0; i < methodsSize; i++) {
            // method index
            annotationsDirectoryOut.writeInt(indexMap.adjustMethod(directoryIn
                    .readInt()));

            // annotation set offset
            annotationsDirectoryOut.writeInt(indexMap
                    .adjustAnnotationSet(directoryIn.readInt()));
        }

        for (int i = 0; i < parameterListSize; i++) {
            contentsOut.annotationSetRefLists.size++;
            annotationSetRefListOut.assertFourByteAligned();

            // method index
            annotationsDirectoryOut.writeInt(indexMap.adjustMethod(directoryIn
                    .readInt()));

            // annotations offset
            annotationsDirectoryOut.writeInt(annotationSetRefListOut
                    .getPosition());
            DexBuffer.Section refListIn = in.open(directoryIn.readInt());

            // parameters
            int parameterCount = refListIn.readInt();
            annotationSetRefListOut.writeInt(parameterCount);
            for (int p = 0; p < parameterCount; p++) {
                annotationSetRefListOut.writeInt(indexMap
                        .adjustAnnotationSet(refListIn.readInt()));
            }
        }
    }

    /**
     * Transform all annotations on a single type, member or parameter.
     */
    private void transformAnnotationSet(IndexMap indexMap,
            DexBuffer.Section setIn) {
        contentsOut.annotationSets.size++;
        annotationSetOut.assertFourByteAligned();
        indexMap.putAnnotationSetOffset(setIn.getPosition(),
                annotationSetOut.getPosition());

        int size = setIn.readInt();
        annotationSetOut.writeInt(size);

        for (int j = 0; j < size; j++) {
            annotationSetOut
                    .writeInt(indexMap.adjustAnnotation(setIn.readInt()));
        }
    }

    private void transformClassData(DexBuffer in, ClassData classData,
            IndexMap indexMap) {
        contentsOut.classDatas.size++;

        ClassData.Field[] staticFields = classData.getStaticFields();
        ClassData.Field[] instanceFields = classData.getInstanceFields();
        ClassData.Method[] directMethods = classData.getDirectMethods();
        ClassData.Method[] virtualMethods = classData.getVirtualMethods();

        classDataOut.writeUleb128(staticFields.length);
        classDataOut.writeUleb128(instanceFields.length);
        classDataOut.writeUleb128(directMethods.length);
        classDataOut.writeUleb128(virtualMethods.length);

        transformFields(indexMap, staticFields);
        transformFields(indexMap, instanceFields);
        transformMethods(in, indexMap, directMethods);
        transformMethods(in, indexMap, virtualMethods);
    }

    protected void transformFields(IndexMap indexMap, ClassData.Field[] fields) {
        int lastOutFieldIndex = 0;
        for (ClassData.Field field : fields) {
            int outFieldIndex = indexMap.adjustField(field.getFieldIndex());
            classDataOut.writeUleb128(outFieldIndex - lastOutFieldIndex);
            lastOutFieldIndex = outFieldIndex;
            classDataOut.writeUleb128(field.getAccessFlags());
        }
    }

    private void transformMethods(DexBuffer in, IndexMap indexMap,
            ClassData.Method[] methods) {

        List<MethodId> methodIds = in.methodIds();

        int lastOutMethodIndex = 0;
        for (ClassData.Method method : methods) {

            MethodId methodId = methodIds.get(method.getMethodIndex());

            int outMethodIndex = indexMap.adjustMethod(method.getMethodIndex());
            classDataOut.writeUleb128(outMethodIndex - lastOutMethodIndex);
            lastOutMethodIndex = outMethodIndex;

            classDataOut.writeUleb128(method.getAccessFlags());

            if (method.getCodeOffset() == 0) {
                if (DO_LOG)
                    if (LOG_MERGE)
                        Log.d(LOG_TAG, String.format(
                                "method %s has no instructions.",
                                methodId.getName()));
                classDataOut.writeUleb128(0);
            } else {
                codeOut.alignToFourBytes();
                classDataOut.writeUleb128(codeOut.getPosition());
                transformCode(in, in.readCode(method), indexMap);
                this.functionsProcessed++;
            }
        }
    }

    private void transformCode(DexBuffer in, Code code, IndexMap indexMap) {
        contentsOut.codes.size++;
        codeOut.assertFourByteAligned();

        codeOut.writeUnsignedShort(code.getRegistersSize());
        codeOut.writeUnsignedShort(code.getInsSize());
        codeOut.writeUnsignedShort(code.getOutsSize());

        Code.Try[] tries = code.getTries();
        codeOut.writeUnsignedShort(tries.length);

        // TODO: retain debug info
        // code.getDebugInfoOffset();
        codeOut.writeInt(0);

        short[] instructions = code.getInstructions();
        InstructionTransformer transformer = (in == dexA) ? aInstructionTransformer
                : bInstructionTransformer;
        short[] newInstructions = transformer.transform(instructions);
        codeOut.writeInt(newInstructions.length);
        codeOut.write(newInstructions);

        if (tries.length > 0) {
            if (newInstructions.length % 2 == 1) {
                codeOut.writeShort((short) 0); // padding
            }
            for (Code.Try tryItem : tries) {
                codeOut.writeInt(tryItem.getStartAddress());
                codeOut.writeUnsignedShort(tryItem.getInstructionCount());
                codeOut.writeUnsignedShort(tryItem.getHandlerOffset());
            }
            Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
            codeOut.writeUleb128(catchHandlers.length);
            for (Code.CatchHandler catchHandler : catchHandlers) {
                transformEncodedCatchHandler(catchHandler, indexMap);
            }
        }
    }

    protected void transformEncodedCatchHandler(Code.CatchHandler catchHandler,
            IndexMap indexMap) {
        int catchAllAddress = catchHandler.getCatchAllAddress();
        int[] typeIndexes = catchHandler.getTypeIndexes();
        int[] addresses = catchHandler.getAddresses();

        if (catchAllAddress != -1) {
            codeOut.writeSleb128(-typeIndexes.length);
        } else {
            codeOut.writeSleb128(typeIndexes.length);
        }

        for (int i = 0; i < typeIndexes.length; i++) {
            codeOut.writeUleb128(indexMap.adjustType(typeIndexes[i]));
            codeOut.writeUleb128(addresses[i]);
        }

        if (catchAllAddress != -1) {
            codeOut.writeUleb128(catchAllAddress);
        }
    }

    protected void transformStaticValues(DexBuffer.Section in, IndexMap indexMap) {
        contentsOut.encodedArrays.size++;
        indexMap.adjustEncodedArray(in.readEncodedArray()).writeTo(
                encodedArrayOut);
    }

    /**
     * Byte counts for the sections written when creating a dex. Target sizes
     * are defined in one of two ways:
     * <ul>
     * <li>By pessimistically guessing how large the union of dex files will be.
     * We're pessimistic because we can't predict the amount of duplication
     * between dex files, nor can we predict the length of ULEB-encoded offsets
     * or indices.
     * <li>By exactly measuring an existing dex.
     * </ul>
     */
    public static class WriterSizes implements Cloneable {

        /* package */final int header = SizeOf.HEADER_ITEM;
        /* package */int idsDefs = 0;
        /* package */final int mapList; // always a fixed size.
        /* package */int typeList = 0;
        /* package */int classData = 0;
        /* package */int code = 0;
        /* package */int stringData = 0;
        /* package */int debugInfo = 0;
        /* package */int encodedArray = 0;
        /* package */int annotationsDirectory = 0;
        /* package */int annotationsSet = 0;
        /* package */int annotationsSetRefList = 0;
        /* package */int annotation = 0;

        /**
         * Compute sizes for merging a and b. This constructor assumes that the
         * sizes in each buffer's table of contents is the acutal size of each
         * section. It also assumes that the length of each of the buffers has
         * not changed since they were read in from the disk file.
         * 
         * @param a the first first buffer whose sizes will be added to this.
         * @param b the second buffer that will be added to this.
         */
        public WriterSizes(DexBuffer a, DexBuffer b, int padding) {
            plus(a.getTableOfContents(), false);
            plus(b.getTableOfContents(), false);

            if (a.getTableOfContents().sections.length != b
                    .getTableOfContents().sections.length)
                throw new IllegalStateException(
                        "varying sections in table of contents are not supported in this version");

            // mapList space is not addative and depends on class design of
            // TableOfContents
            mapList = SizeOf.UINT
                    + (a.getTableOfContents().sections.length * SizeOf.MAP_ITEM);

            typeList += padding;
            stringData += padding;
            debugInfo += padding;
            annotationsDirectory += padding;
            annotationsSet += padding;
            annotationsSetRefList += padding;
            code += padding;
            classData += padding;
            encodedArray += padding;
            annotation += padding;

        }

        /**
         * Compute sizes from a dex merger object. The sizes will be set
         * according to the current write position of each section defined in
         * DexMerger. This is useful for calculating proper sizes of a buffer
         * once a merger has been done, and does not assume that the lengths of
         * each section have remained the same.
         * <p>
         * This feature supports code enhancement.
         * 
         * @param merger the merger state to read sizes from.
         */
        public WriterSizes(DexMerger merger) {

            // used to get the count of sections only.
            TableOfContents contents = merger.contentsOut;
            mapList = SizeOf.UINT
                    + (contents.sections.length * SizeOf.MAP_ITEM);

            idsDefs += calcIdDefSz(contents);

            typeList = merger.typeListOut.getCurrentSize();
            classData = merger.classDataOut.getCurrentSize();
            code = merger.codeOut.getCurrentSize();
            stringData = merger.stringDataOut.getCurrentSize();
            debugInfo = merger.debugInfoOut.getCurrentSize();
            encodedArray = merger.encodedArrayOut.getCurrentSize();
            annotationsDirectory = merger.annotationsDirectoryOut
                    .getCurrentSize();
            annotationsSet = merger.annotationSetOut.getCurrentSize();
            annotationsSetRefList = merger.annotationSetRefListOut
                    .getCurrentSize();
            annotation = merger.annotationOut.getCurrentSize();

        }

        private int calcIdDefSz(TableOfContents contents) {

            return contents.stringIds.size * SizeOf.STRING_ID_ITEM
                    + contents.typeIds.size * SizeOf.TYPE_ID_ITEM
                    + contents.protoIds.size * SizeOf.PROTO_ID_ITEM
                    + contents.fieldIds.size * SizeOf.MEMBER_ID_ITEM
                    + contents.methodIds.size * SizeOf.MEMBER_ID_ITEM
                    + contents.classDefs.size * SizeOf.CLASS_DEF_ITEM;

        }

        @Override
        public WriterSizes clone() {
            try {
                return (WriterSizes) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public void addCode(int val) {
            code += val;
        }

        /**
         * Adds the size of the TableOfContents to the sizes kept in this class.
         * Does not affect the fields header or mapList.
         * 
         * @param contents the contents to read sizes from
         * @param exact
         */
        public void plus(TableOfContents contents, boolean exact) {
            idsDefs += calcIdDefSz(contents);

            typeList += contents.typeLists.byteCount;
            stringData += contents.stringDatas.byteCount;
            debugInfo += contents.debugInfos.byteCount;
            annotationsDirectory += contents.annotationsDirectories.byteCount;
            annotationsSet += contents.annotationSets.byteCount;
            annotationsSetRefList += contents.annotationSetRefLists.byteCount;

            if (exact) {
                code += contents.codes.byteCount;
                classData += contents.classDatas.byteCount;
                encodedArray += contents.encodedArrays.byteCount;
                annotation += contents.annotations.byteCount;
            } else {
                // at most 1/4 of the bytes in a code section are uleb/sleb
                code += (int) Math.ceil(contents.codes.byteCount * 1.25);
                // at most 1/3 of the bytes in a class data section are
                // uleb/sleb
                classData += (int) Math
                        .ceil(contents.classDatas.byteCount * 1.34);
                // all of the bytes in an encoding arrays section may be
                // uleb/sleb
                encodedArray += contents.encodedArrays.byteCount * 2;
                // at most 1/3 of the bytes in an encoding arrays section are
                // uleb/sleb
                annotation += (int) Math
                        .ceil(contents.annotations.byteCount * 1.34);
            }
        }

        public void minusWaste(DexMerger dexMerger) {

            idsDefs -= dexMerger.idsDefsOut.remaining();
            typeList -= dexMerger.typeListOut.remaining();
            classData -= dexMerger.classDataOut.remaining();
            code -= dexMerger.codeOut.remaining();
            stringData -= dexMerger.stringDataOut.remaining();
            debugInfo -= dexMerger.debugInfoOut.remaining();
            encodedArray -= dexMerger.encodedArrayOut.remaining();
            annotationsDirectory -= dexMerger.annotationsDirectoryOut
                    .remaining();
            annotationsSet -= dexMerger.annotationSetOut.remaining();
            annotationsSetRefList -= dexMerger.annotationSetRefListOut
                    .remaining();
            annotation -= dexMerger.annotationOut.remaining();
        }

        public int size() {
            return header + idsDefs + mapList + typeList + classData + code
                    + stringData + debugInfo + encodedArray
                    + annotationsDirectory + annotationsSet
                    + annotationsSetRefList + annotation;
        }
    }

    /**
     * Merges files with the path in the fist two parameters and output the
     * results into the filename stored in the third parameter.
     * 
     * @param args the three file names to work with.
     * @throws IOException if an error occurs.
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {
        if (args.length != 3) {
            printUsage();
            return;
        }

        DexBuffer dexA = new DexBuffer(new File(args[1]));
        DexBuffer dexB = new DexBuffer(new File(args[2]));
        DexBuffer merged = new DexMerger(dexA, dexB, CollisionPolicy.KEEP_FIRST)
                .merge(null);
        merged.writeTo(new File(args[0]));
    }

    private static void printUsage() {
        System.out.println("Usage: DexMerger <out.dex> <a.dex> <b.dex>");
        System.out.println();
        System.out
                .println("If both a and b define the same classes, a's copy will be used.");
    }
}
