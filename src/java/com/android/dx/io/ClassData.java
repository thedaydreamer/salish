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

import static com.android.dx.rop.code.AccessFlags.ACC_ABSTRACT;
import static com.android.dx.rop.code.AccessFlags.ACC_CONSTRUCTOR;
import static com.android.dx.rop.code.AccessFlags.ACC_DECLARED_SYNCHRONIZED;
import static com.android.dx.rop.code.AccessFlags.ACC_FINAL;
import static com.android.dx.rop.code.AccessFlags.ACC_MIRANDA;
import static com.android.dx.rop.code.AccessFlags.ACC_NATIVE;
import static com.android.dx.rop.code.AccessFlags.ACC_PRIVATE;
import static com.android.dx.rop.code.AccessFlags.ACC_PUBLIC;
import static com.android.dx.rop.code.AccessFlags.ACC_STATIC;
import static com.android.dx.rop.code.AccessFlags.ACC_SYNCHRONIZED;
import static com.android.dx.rop.code.AccessFlags.ACC_SYNTHETIC;

public final class ClassData {

	private final Field[] staticFields;
	private final Field[] instanceFields;
	private final Method[] directMethods;
	private final Method[] virtualMethods;

	public ClassData(Field[] staticFields, Field[] instanceFields,
			Method[] directMethods, Method[] virtualMethods) {
		this.staticFields = staticFields;
		this.instanceFields = instanceFields;
		this.directMethods = directMethods;
		this.virtualMethods = virtualMethods;
	}

	public Field[] getStaticFields() {
		return staticFields;
	}

	public Field[] getInstanceFields() {
		return instanceFields;
	}

	public Method[] getDirectMethods() {
		return directMethods;
	}

	public Method[] getVirtualMethods() {
		return virtualMethods;
	}

	public Field[] allFields() {
		Field[] result = new Field[staticFields.length + instanceFields.length];
		System.arraycopy(staticFields, 0, result, 0, staticFields.length);
		System.arraycopy(instanceFields, 0, result, staticFields.length,
				instanceFields.length);
		return result;
	}

	public Method[] allMethods() {
		Method[] result = new Method[directMethods.length
				+ virtualMethods.length];
		System.arraycopy(directMethods, 0, result, 0, directMethods.length);
		System.arraycopy(virtualMethods, 0, result, directMethods.length,
				virtualMethods.length);
		return result;
	}

	/**
	 * A field definition extracted from a ClassDef in the dex file.
	 * 
	 * @author rschilling
	 * 
	 */
	public static class Field {

		/**
		 * The index in the FieldId list for this field.
		 */
		private final int fieldIndex;

		/**
		 * The access flags for this field.
		 */
		private final int accessFlags;

		/**
		 * The type of field: static or instance. This item is never written to
		 * the DEX file directly. Set by DexBuffer when it reads the field from
		 * the file.
		 */
		private final FieldType fieldType;

		public Field(FieldType fieldType, int fieldIndex, int accessFlags) {
			this.fieldIndex = fieldIndex;
			this.accessFlags = accessFlags;
			this.fieldType = fieldType;
		}

		public FieldType getFieldType() {
			return fieldType;
		}

		public int getFieldIndex() {
			return fieldIndex;
		}

		public int getAccessFlags() {
			return accessFlags;
		}
	}

	public static class Method {

		/**
		 * The method index calculated from the first index (zero based).
		 */
		private final int methodIndex;

		/**
		 * The method index as read from the input stream.
		 */
		private final int methodIndexRaw;

		private final int accessFlags;
		/**
		 * The offset in the data file that the code for this method appears at.
		 */
		private final int codeOffset;

		/**
		 * the offset in the data file that this method appears at. Derived from
		 * the position of the file that the reading for this method was
		 * started.
		 */
		private final int methodOffset;

		/**
		 * The type of method: virtual or direct. This item is never written to
		 * the DEX file directly. Set by DexBuffer when it reads the method from
		 * the file.
		 */
		private final MethodType methodType;

		public Method(MethodType methodType, int methodIndex,
				int methodIndexRaw, int accessFlags, int codeOffset,
				int methodOffset) {

			this.methodIndex = methodIndex;
			this.methodIndexRaw = methodIndexRaw;
			this.accessFlags = accessFlags;
			this.codeOffset = codeOffset;
			this.methodOffset = methodOffset;
			this.methodType = methodType;
		}

		public MethodType getMethodType() {
			return methodType;
		}

		public boolean dvmIsStaticMethod() {
			return (getAccessFlags() & ACC_STATIC) != 0;
		}

		public boolean dvmIsPublicMethod() {
			return (getAccessFlags() & ACC_PUBLIC) != 0;
		}

		public boolean dvmIsPrivateMethod() {
			return (getAccessFlags() & ACC_PRIVATE) != 0;
		}

		public boolean dvmIsSynchronizedMethod() {
			return (getAccessFlags() & ACC_SYNCHRONIZED) != 0;
		}

		public boolean dvmIsDeclaredSynchronizedMethod() {
			return (getAccessFlags() & ACC_DECLARED_SYNCHRONIZED) != 0;
		}

		public boolean dvmIsFinalMethod() {
			return (getAccessFlags() & ACC_FINAL) != 0;
		}

		public boolean dvmIsNativeMethod() {
			return (getAccessFlags() & ACC_NATIVE) != 0;
		}

		public boolean dvmIsAbstractMethod() {
			return (getAccessFlags() & ACC_ABSTRACT) != 0;
		}

		public boolean dvmIsSyntheticMethod() {
			return (getAccessFlags() & ACC_SYNTHETIC) != 0;
		}

		public boolean dvmIsMirandaMethod() {
			return (getAccessFlags() & ACC_MIRANDA) != 0;
		}

		public boolean dvmIsConstructor() {
			return (getAccessFlags() & ACC_CONSTRUCTOR) != 0;
		}

		/**
		 * Returns true if this Method is a direct method. mId must be the
		 * MethodId that is defined for this method object.
		 */
		public boolean dvmIsDirectMethod(MethodId mId) {
			if (methodIndex != mId.getMethodIdIndex())
				throw new IllegalArgumentException(
						"mId must be the MethodId for this Method object.");

			return dvmIsPrivateMethod() || dvmIsStaticMethod()
					|| mId.isInitMethod();
		}

		public int getMethodOffset() {
			return methodOffset;
		}

		public int getMethodIndex() {
			return methodIndex;
		}

		public int getMethodIndexRaw() {
			return methodIndexRaw;
		}

		public int getAccessFlags() {
			return accessFlags;
		}

		public int getCodeOffset() {
			return codeOffset;
		}

	}

	/**
	 * Returns the number of methods defined in this class. If includeVirtual is
	 * true then virtual methods (e.g. methods without code) are included.
	 * 
	 * @param includeVirtual
	 *            if true include in the count the number of methods that have
	 *            no code associated with them.
	 * @return number of methods defined in the class associated with this
	 *         ClassData object.
	 */
	public int methodCount(boolean includeVirtual) {

		int result = 0;

		Method[] methods = allMethods();
		if (methods != null) {
			for (Method method : methods) {

				if (includeVirtual)
					result++;
				else if (method.getCodeOffset() > 0)
					result++;

			}
		}

		return result;
	}

}
