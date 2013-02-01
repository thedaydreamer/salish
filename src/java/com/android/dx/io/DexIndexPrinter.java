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

package com.android.dx.io;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.util.Log;

import com.android.dx.dex.TableOfContents;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.dexbuffer.DexBufferRandomAccessFile;
import com.android.dx.io.dexbuffer.Section;

/**
 * Executable that prints all indices of a dex file.
 */
public final class DexIndexPrinter {

    public static final String LOG_TAG = "Finnr.DexIndexPrinter";
    private final DexBuffer dexBuffer;
    private final TableOfContents tableOfContents;

    public DexIndexPrinter(File file) throws IOException {
        this.dexBuffer = new DexBufferRandomAccessFile(file);
        this.tableOfContents = dexBuffer.getTableOfContents();
    }

    private void printMap() {
        for (TableOfContents.TOCSection section : tableOfContents.sections) {
            if (section.off != -1) {
                if (DO_LOG)
                    Log.i(LOG_TAG, "section "
                            + Integer.toHexString(section.type) + " "
                            + TableOfContents.TOCSection.toName(section.type)
                            + " off=" + Integer.toHexString(section.off) + " size="
                            + Integer.toHexString(section.size) + " byteCount="
                            + Integer.toHexString(section.byteCount));
            }
        }
    }

    private void printStrings() throws IOException {
        List<String> strings = dexBuffer.strings();
        int index = 0;
        for (String string : dexBuffer.strings()) {
            if (DO_LOG)
                Log.i(LOG_TAG, "string " + index + ": " + string);
            index++;
        }
    }

    private void printTypeIds() throws IOException {
        int index = 0;
        for (Integer type : dexBuffer.typeIds) {
            if (DO_LOG)
                Log.i(LOG_TAG, "type " + index + ": "
                        + dexBuffer.strings().get(type));
            index++;
        }
    }

    private void printProtoIds() throws IOException {
        int index = 0;
        for (ProtoId protoId : dexBuffer.protoIds()) {
            if (DO_LOG)
                Log.i(LOG_TAG, "proto " + index + ": " + protoId);
            index++;
        }
    }

    private void printFieldIds() throws IOException {
        int index = 0;
        for (FieldId fieldId : dexBuffer.fieldIds) {
            if (DO_LOG)
                Log.i(LOG_TAG, "field " + index + ": " + fieldId);
            index++;
        }
    }

    private void printMethodIds() throws IOException {
        int index = 0;
        for (MethodId methodId : dexBuffer.methodIds()) {
            if (DO_LOG)
                Log.i(LOG_TAG, "methodId " + index + ": " + methodId);
            index++;
        }
    }

    private void printTypeLists() throws IOException {
        if (tableOfContents.typeLists.off == -1) {
            if (DO_LOG)
                Log.i(LOG_TAG, "No type lists");
            return;
        }
        Section in = dexBuffer.open(tableOfContents.typeLists.off);
        for (int i = 0; i < tableOfContents.typeLists.size; i++) {
            int size = in.readInt();
            if (DO_LOG)
                Log.i(LOG_TAG, "Type list i=" + i + ", size=" + size
                        + ", elements=");
            for (int t = 0; t < size; t++) {
                System.out.print(" "
                        + dexBuffer.typeNames().get((int) in.readShort()));
            }
            if (size % 2 == 1) {
                in.readShort(); // retain alignment
            }
            if (DO_LOG)
                Log.i(LOG_TAG, "\n");
        }
    }

    private void printClassDefs() {
        int index = 0;
        for (ClassDef classDef : dexBuffer.classDefs()) {
            System.out.println("class def " + index + ": " + classDef);
            index++;
        }
    }

    private void printSections() {
        StringBuilder builder = new StringBuilder();
        builder.append("----------- DEX Sections --------------------\n");
        builder.append("    output format for sections is offset:byte count:item count\n");
        builder.append("header size ").append(tableOfContents.headerSize)
                .append(" bytes\n");
        builder.append("endian tag ").append(tableOfContents.endianTag)
                .append("\n");
        builder.append("link size ").append(tableOfContents.linkSize)
                .append("\n");
        builder.append("link offset ").append(tableOfContents.linkOff)
                .append("\n");
        builder.append("map list ").append(tableOfContents.mapList.off)
                .append(":").append(tableOfContents.mapList.byteCount)
                .append(":").append(tableOfContents.mapList.size).append("\n");
        builder.append("string_ids ").append(tableOfContents.stringIds.off)
                .append(":").append(tableOfContents.stringIds.byteCount)
                .append(":").append(tableOfContents.stringIds.size)
                .append("\n");
        builder.append("type_ids ").append(tableOfContents.typeIds.off)
                .append(":").append(tableOfContents.typeIds.byteCount)
                .append(":").append(tableOfContents.typeIds.size).append("\n");
        builder.append("proto_ids ").append(tableOfContents.protoIds.off)
                .append(":").append(tableOfContents.protoIds.byteCount)
                .append(":").append(tableOfContents.protoIds.size).append("\n");
        builder.append("field_ids ").append(tableOfContents.fieldIds.off)
                .append(":").append(tableOfContents.fieldIds.byteCount)
                .append(":").append(tableOfContents.fieldIds.size).append("\n");
        builder.append("method_ids ").append(tableOfContents.methodIds.off)
                .append(":").append(tableOfContents.methodIds.byteCount)
                .append(":").append(tableOfContents.methodIds.size)
                .append("\n");
        builder.append("class_defs ").append(tableOfContents.classDefs.off)
                .append(":").append(tableOfContents.classDefs.byteCount)
                .append(":").append(tableOfContents.classDefs.size)
                .append("\n");
        builder.append("data ").append(tableOfContents.dataOff).append(":")
                .append(tableOfContents.dataSize).append("\n");

        System.out.println(builder.toString());

    }

    private void printMetadata() {
        StringBuilder builder = new StringBuilder();
        builder.append("----------- metadata --------------------\n");
        builder.append("magic ").append(tableOfContents.magic).append("\n");

        builder.append("api target ").append(tableOfContents.apiTarget)
                .append("\n");
        builder.append("checksum ").append(tableOfContents.checksum)
                .append("\n");
        builder.append("signature ").append(tableOfContents.signature)
                .append("\n");
        builder.append("file size ").append(tableOfContents.fileSize)
                .append("\n");
        System.out.println(builder.toString());

    }

    public static void main(String[] args) throws IOException {
        DexIndexPrinter indexPrinter = new DexIndexPrinter(new File(args[0]));
        indexPrinter.printMetadata();
        indexPrinter.printSections();
        indexPrinter.printMap();
        indexPrinter.printStrings();
        indexPrinter.printTypeIds();
        indexPrinter.printProtoIds();
        indexPrinter.printFieldIds();
        indexPrinter.printMethodIds();
        indexPrinter.printTypeLists();
        indexPrinter.printClassDefs();
    }
}
