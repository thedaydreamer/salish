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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import android.util.SparseArray;

import com.android.dx.dex.TableOfContents;
import com.android.dx.io.ClassData.Field;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.dexbuffer.Section;
import com.android.dx.merge.TypeList;

/**
 * The bytes of a dex file in memory for reading and writing. All int offsets
 * are unsigned.
 */
public class ByteArrayDexBuffer extends DexBuffer {

    @Override
    public SparseArray<ClassData> classData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeTo(File dexOut) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public TableOfContents getTableOfContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Section open(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Section appendSection(int maxByteCount, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<String> strings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Integer> typeIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> typeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProtoId> protoIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<FieldId> fieldIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MethodId> methodIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ClassDef> classDefList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClassDef getDef(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TypeList readTypeList(int offset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Code readCode(Method method) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Field getClassDataField(FieldId fId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MethodId findMethodId(String definingClassName, String methodName, int prototypeId) {
        // TODO Auto-generated method stub
        return null;
    }

}
