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

package com.android.dx.dex;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.util.Log;

import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.dexbuffer.Section;
import com.android.dx.util.DexException;

// TODO: create a read-only version of DexFile, where the objects are simply memory mapped.
/**
 * The file header and map.
 */
public final class TableOfContents {

    public static final String LOG_TAG = "R3.TOC";

    public static final int SECTION_COUNT = 18;

    /*
     * TODO: factor out ID constants.
     */

    /* constant pool */
    public final TOCSection header = new TOCSection(0x0000);
    public final TOCSection stringIds = new TOCSection(0x0001);
    public final TOCSection typeIds = new TOCSection(0x0002);
    public final TOCSection protoIds = new TOCSection(0x0003);
    public final TOCSection fieldIds = new TOCSection(0x0004);
    public final TOCSection methodIds = new TOCSection(0x0005);
    public final TOCSection classDefs = new TOCSection(0x0006);

    /* data section */
    public final TOCSection mapList = new TOCSection(0x1000);
    public final TOCSection typeLists = new TOCSection(0x1001);
    public final TOCSection annotationSetRefLists = new TOCSection(0x1002);
    public final TOCSection annotationSets = new TOCSection(0x1003);
    public final TOCSection classDatas = new TOCSection(0x2000);
    public final TOCSection codes = new TOCSection(0x2001);
    public final TOCSection stringDatas = new TOCSection(0x2002);
    public final TOCSection debugInfos = new TOCSection(0x2003);
    public final TOCSection annotations = new TOCSection(0x2004);
    public final TOCSection encodedArrays = new TOCSection(0x2005);
    public final TOCSection annotationsDirectories = new TOCSection(0x2006);

    public final TOCSection[] sections = {
            header, stringIds, typeIds, protoIds,
            fieldIds, methodIds, classDefs, mapList, typeLists,
            annotationSetRefLists, annotationSets, classDatas, codes,
            stringDatas, debugInfos, annotations, encodedArrays,
            annotationsDirectories
    };

    /**
     * Calculated.
     */
    public int checksum;

    /**
     * Calculated
     */
    public byte[] signature;

    /**
     * Must equal the sum of byteCount for all sections + linkSize
     */
    public int fileSize;

    /**
     * Zero if the file is not statically linked.
     */
    public int linkSize;

    /**
     * Zero if linkSize == 0. The link section is used by runtime
     * implementations.
     */
    public int linkOff;

    /**
     * Size of the data section. Should equal to the sum of byteCount for
     * mapList, typeLists, annotationSetRefLists, annotationSets, classDatas,
     * codes, stringDatas, debugInfos, annotations, encodedArrays,
     * annotationsDirectories
     */
    public int dataSize;

    /**
     * The offset from the beginning of the file that the data section begins.
     * Should be equal to ClassDefs.off + ClassDefs.byteCount (ClassDefs is the
     * last section in the constant pool).
     */
    public int dataOff;

    /**
     * Doesn't every cool file have to have one?
     */
    public byte[] magic;

    /**
     * The API version that the underlying DEX file supports. Calculated from
     * magic.
     */
    public int apiTarget;

    /**
     * The header size stored in the header section of this file. Useful to
     * check for file structure compatability.
     */
    public int headerSize;

    /**
     * Endian tag read in from the header section. Used to test for file
     * stucture validity.
     */
    public int endianTag;

    /**
     * Creates a table of contents.
     */
    public TableOfContents() {
        signature = new byte[20];

        // sanity
        if (sections.length != SECTION_COUNT)
            throw new IllegalStateException(
                    "constant SECTION_COUNT must equal sections.length");
    }

    /**
     * Loads the table of contents values from a buffer.
     * 
     * @param buffer
     * @throws IOException
     */
    public void readFrom(DexBuffer buffer) throws IOException { // TODO add
                                                                // sanity check
                                                                // for buffer,
                                                                // and then
                                                                // process.
        if (DO_LOG)
            Log.i(LOG_TAG, "Loading Table of Contents from a DexBuffer ...");

        readHeader(buffer.open(0));
        readMap(buffer.open(mapList.off));
        computeSizesFromOffsets();
    }

    private void readHeader(Section headerIn) // FIXME contains no
                                              // explicit order
                                              // requirement -
                                              // readHeader must be
                                              // called before
                                              // readMap.
            throws UnsupportedEncodingException {
        magic = headerIn.readByteArray(8);
        apiTarget = DexFormat.magicToApi(magic);

        if (apiTarget < 0) {
            throw new DexException("Unexpected magic: "
                    + Arrays.toString(magic));
        }

        checksum = headerIn.readInt();
        signature = headerIn.readByteArray(20);
        fileSize = headerIn.readInt();
        headerSize = headerIn.readInt();
        if (headerSize != SizeOf.HEADER_ITEM) {
            throw new DexException("Unexpected header: 0x"
                    + Integer.toHexString(headerSize));
        }
        endianTag = headerIn.readInt();
        if (endianTag != DexFormat.ENDIAN_TAG) {
            throw new DexException("Unexpected endian tag: 0x"
                    + Integer.toHexString(endianTag));
        }
        linkSize = headerIn.readInt();
        linkOff = headerIn.readInt();
        mapList.off = headerIn.readInt();
        if (mapList.off == 0) {
            throw new DexException(
                    "Cannot read dex files that do not contain a map");
        }
        stringIds.size = headerIn.readInt();
        stringIds.off = headerIn.readInt();
        typeIds.size = headerIn.readInt();
        typeIds.off = headerIn.readInt();
        protoIds.size = headerIn.readInt();
        protoIds.off = headerIn.readInt();
        fieldIds.size = headerIn.readInt();
        fieldIds.off = headerIn.readInt();
        methodIds.size = headerIn.readInt();
        methodIds.off = headerIn.readInt();
        classDefs.size = headerIn.readInt();
        classDefs.off = headerIn.readInt();
        dataSize = headerIn.readInt();
        dataOff = headerIn.readInt();
        // dumpHeaderInfo();
    }

    /**
     * Read section data from the map section of the file. The values read from
     * the map section will override the values already stored from the header.
     * A check is made to ensure that the values in the header and the values in
     * the map are consistent.
     */
    private void readMap(Section in) throws IOException {
        int mapSize = in.readInt();
        TOCSection previous = null;
        for (int i = 0; i < mapSize; i++) {
            short type = in.readShort();
            in.readShort(); // unused
            TOCSection section = getSection(type);
            int size = in.readInt();
            int offset = in.readInt();

            if ((section.size != 0 && section.size != size)
                    || (section.off != -1 && section.off != offset)) {
                throw new DexException("Unexpected map value for 0x"
                        + Integer.toHexString(type));
            }

            section.size = size;
            section.off = offset;

            /*
             * Log.i(LOG_TAG, "read section from map: " +
             * Section.toName(section.type) + " offset:size " + section.off +
             * ":" + section.size);
             */
            if (previous != null && previous.off > section.off) {
                throw new DexException("Map is unsorted at " + previous + ", "
                        + section);
            }

            previous = section;
        }
        Arrays.sort(sections);
    }

    @SuppressWarnings("unused")
    public void dumpHeaderInfo() {
        if (!DO_LOG)
            return;

        Log.i(LOG_TAG,
                "----------- header information from table of contents ----------");

        Log.i(LOG_TAG, "link " + linkOff + ":" + linkSize);
        Log.i(LOG_TAG, "data " + dataOff + ":" + dataSize);

        for (TOCSection s : sections) {
            Log.i(LOG_TAG, TOCSection.toName(s.type) + " offset:size " + s.off
                    + ":" + s.size);

        }

    }

    public int getEnd() {
        return dataOff + dataSize; // FIXME - I think
    }

    public void computeSizesFromOffsets() {
        /*
         * Log.i(LOG_TAG + ".size",
         * "=============================================="); Log.i(LOG_TAG +
         * ".size", "compting sizes");
         */
        Arrays.sort(sections);
        int end = getEnd();
        for (int i = sections.length - 1; i >= 0; i--) {
            TOCSection section = sections[i];
            if (section.off == -1) {
                continue;
            }

            if (section.off > end)
                throw new DexException("Map is unsorted at " + section);

            section.byteCount = end - section.off;

            /*
             * Log.i(LOG_TAG + ".size", "computing size of section " +
             * Section.toName(section.type) + ": " + section.byteCount);
             */

            end = section.off;
        }
    }

    private TOCSection getSection(short type) {
        for (TOCSection section : sections) {
            if (section.type == type) {
                return section;
            }
        }
        throw new IllegalArgumentException("No such map item: " + type);
    }

    public void writeHeader(Section out) throws IOException {
        out.write(DexFormat.apiToMagic(DexFormat.API_CURRENT).getBytes("UTF-8"));
        out.writeInt(checksum);
        out.write(signature);
        out.writeInt(fileSize);
        out.writeInt(SizeOf.HEADER_ITEM);
        out.writeInt(DexFormat.ENDIAN_TAG);
        out.writeInt(linkSize);
        out.writeInt(linkOff);
        out.writeInt(mapList.off);
        out.writeInt(stringIds.size);
        out.writeInt(stringIds.off);
        out.writeInt(typeIds.size);
        out.writeInt(typeIds.off);
        out.writeInt(protoIds.size);
        out.writeInt(protoIds.off);
        out.writeInt(fieldIds.size);
        out.writeInt(fieldIds.off);
        out.writeInt(methodIds.size);
        out.writeInt(methodIds.off);
        out.writeInt(classDefs.size);
        out.writeInt(classDefs.off);
        out.writeInt(dataSize);
        out.writeInt(dataOff);
    }

    public void writeMap(Section out) throws IOException {
        int count = 0;
        for (TOCSection section : sections) {
            if (section.exists()) {
                count++;
            }
        }

        out.writeInt(count);
        for (TOCSection section : sections) {
            if (section.exists()) {
                out.write(section.type);
                out.write((short) 0);
                out.writeInt(section.size);
                out.writeInt(section.off);
            }
        }
    }

    public static class TOCSection implements Comparable<TOCSection> {

        public final short type;

        /**
         * The number of records of a particular type in this section.
         */
        public int size = 0;

        /**
         * The byte position in the buffer that this section begins.
         */
        public int off = -1;

        /**
         * The number of bytes in this section. This is a calculated value that
         * is set whenever computeSizesFromOffsets is called.
         */
        public int byteCount = 0;

        public TOCSection(int type) {
            this.type = (short) type;
        }

        public boolean exists() {
            return size > 0;
        }

        public int compareTo(TOCSection section) {
            if (off != section.off) {
                return off < section.off ? -1 : 1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return String.format("Section[type=%#x,off=%#x,size=%#x]", type,
                    off, size);
        }

        public String getName() {
            return TOCSection.toName(type);
        }

        /**
         * Returns the name of a section given the type. TODO: Make type an
         * enum.
         * 
         * @param type the type of section to retrieve the name for
         * @return the name of the section.
         */
        public static String toName(int type) {
            switch (type) {
                case 0x0000:
                    return "header";
                case 0x0001:
                    return "string IDs";
                case 0x0002:
                    return "type IDs";
                case 0x0003:
                    return "proto IDs";
                case 0x0004:
                    return "field IDs";
                case 0x0005:
                    return "method IDs";
                case 0x0006:
                    return "class Defs";
                case 0x1000:
                    return "map list";
                case 0x1001:
                    return "type Lists";
                case 0x1002:
                    return "annotation set refs";
                case 0x1003:
                    return "annotation sets";
                case 0x2000:
                    return "class datas";
                case 0x2001:
                    return "codes";
                case 0x2002:
                    return "string datas";
                case 0x2003:
                    return "debug infos";
                case 0x2004:
                    return "encoed arrays";
                case 0x2005:
                    return "encoded arrays";
                case 0x2006:
                    return "annotations directories";
                default:
                    return "undefined";

            }
        }
    }

}
