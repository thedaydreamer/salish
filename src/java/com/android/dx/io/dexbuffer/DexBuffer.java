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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Adler32;

import schilling.richard.dalvik.vm.DvmGlobals;
import schilling.richard.dalvik.vm.oo.util.ClassDefFactory;
import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.dex.TableOfContents;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.merge.TypeList;

/**
 * The bytes of a dex file in memory for reading and writing. All int offsets
 * are unsigned.
 */
public abstract class DexBuffer { // TODO Rename manually to DexBuffer.

    private static final int CHECKSUM_OFFSET = 8;
    private static final int CHECKSUM_SIZE = 4;
    private static final int SIGNATURE_OFFSET = CHECKSUM_OFFSET + CHECKSUM_SIZE;
    private static final int SIGNATURE_SIZE = 20;

    public static final String LOG_TAG = "Finnr.DexBuffer";

    /**
     * Returns all the class data objects in the dex buffer.
     * 
     * @return
     */
    public abstract SparseArray<ClassData> classData();

    /**
     * Writes the binary representation of the dex buffer to an output stream.
     * 
     * @param out
     * @throws IOException
     */
    public abstract void writeTo(OutputStream out) throws IOException;

    public abstract void writeTo(File dexOut) throws IOException;

    public abstract TableOfContents getTableOfContents();

    public abstract Section open(int position);

    public abstract Section appendSection(int maxByteCount, String name);

    public abstract int getLength();

    public abstract List<String> strings();

    public abstract List<Integer> typeIds();

    /**
     * Returns the list of type names found in the DEX file in the order in
     * which they are declared in the file.
     * 
     * @return the type names in the DEX file.
     */
    public abstract List<String> typeNames();

    /**
     * Returns all the proto id objects found in the dex file.
     * 
     * @return
     */
    public abstract List<ProtoId> protoIds();

    public abstract List<FieldId> fieldIds();

    public abstract List<MethodId> methodIds();

    public abstract List<ClassDef> classDefList();

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
    public abstract ClassDef getDef(String type);

    public Iterable<ClassDef> classDefs() {
        return new Iterable<ClassDef>() {

            public Iterator<ClassDef> iterator() {
                return classDefList().iterator();
            }
        };
    }

    public abstract TypeList readTypeList(int offset);

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
    public abstract Code readCode(ClassData.Method method);

    /**
     * Returns the ClassData.Field object that is associated with a particular
     * FieldId.
     * 
     * @param fId the FieldId to search for.
     * @return the ClassData.Field object that points to the FieldId.
     */
    public abstract ClassData.Field getClassDataField(FieldId fId);

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

    public ClassData readClassData(ClassDef classDef) {

        int offset = classDef.getClassDataOffset();
        if (offset == 0)
            throw new IllegalArgumentException("offset == 0");

        return classData().get(offset);
    }

    /**
     * Finds a method or return null.
     * 
     * @param definingClassName the class that defines the method
     * @param methdodName the method name
     * @param prototypeId the prototype of the method
     * @return the corresponding method, or null if none found.
     */
    public abstract MethodId findMethodId(String definingClassName, String methodName,
            int prototypeId);

    public static int fourByteAlign(int position) {
        return (position + 3) & ~3;
    }

    /**
     * This function is called when no more sections are going to be added to
     * this buffer. Performs cleanup.
     */
    public abstract void noMoreSections();

    /**
     * Returns the signature of all but the first 32 bytes of {@code dex}. The
     * first 32 bytes of dex files are not specified to be included in the
     * signature.
     */
    public abstract byte[] computeSignature() throws IOException;

    /**
     * Returns the checksum of all but the first 12 bytes of {@code dex}.
     */
    public abstract int computeChecksum(DexBuffer dex) throws IOException;
}
