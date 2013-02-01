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

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;

import java.util.List;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.util.Unsigned;

public final class MethodId implements Comparable<MethodId> {

	private final DexBuffer buffer;
	private final int declaringClassIndex;
	private final int protoIndex;
	private final int nameIndex;

	/**
	 * The name of the method. Do not write to the file.
	 */
	private String methodName;

	/**
	 * The name of the class that defines this method. Do not write to the file.
	 */
	private String declaringClassSignature;

	public MethodId(DexBuffer buffer, int declaringClassIndex, int protoIndex,
			int nameIndex) {
		this.buffer = buffer;
		this.declaringClassIndex = declaringClassIndex;
		this.protoIndex = protoIndex;
		this.nameIndex = nameIndex;
	}

	/**
	 * Returns true if this method is a constructor/init method. Replaces
	 * dvmIsConstructorMethod.
	 * 
	 * @return true if this si a constructor method.
	 */
	public boolean isInitMethod() {
		String name = getName();
		return (name.charAt(0) == '<') && name.endsWith("init>") ? true : false;
	}

	public int getDeclaringClassIndex() {
		return declaringClassIndex;
	}

	public String getDeclaringClassSignature() {

		if (declaringClassSignature == null) {
			List<String> types = buffer.typeNames();
			declaringClassSignature = types.get(declaringClassIndex);
		}

		return declaringClassSignature;
	}

	public int getProtoIndex() {
		return protoIndex;
	}

	public ProtoId getPrototypeId() {
		return buffer.protoIds().get(protoIndex);
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public String getName() {
		if (methodName == null)
			methodName = buffer.strings().get(nameIndex);

		return methodName;
	}

	public int compareTo(MethodId other) {
		if (declaringClassIndex != other.declaringClassIndex) {
			return Unsigned.compare(declaringClassIndex,
					other.declaringClassIndex);
		}
		if (nameIndex != other.nameIndex) {
			return Unsigned.compare(nameIndex, other.nameIndex);
		}
		return Unsigned.compare(protoIndex, other.protoIndex);
	}

	public void writeTo(DexBuffer.Section out) {
		out.writeUnsignedShort(declaringClassIndex);
		out.writeUnsignedShort(protoIndex);
		out.writeInt(nameIndex);
	}

	@Override
	public String toString() {
		if (buffer == null) {
			return declaringClassIndex + " " + protoIndex + " " + nameIndex;
		}
		return buffer.typeNames().get(declaringClassIndex)
				+ "."
				+ buffer.strings().get(nameIndex)
				+ buffer.readTypeList(buffer.protoIds().get(protoIndex)
						.getParametersOffset());
	}

	/*
	 * ClassData.Method methodIndex
	 * 
	 * ProtoId parameters returnType shorty
	 * 
	 * MethodId protoIndex
	 * 
	 * ClassData.Method provides MethodId which provides ProtoId.
	 */

	public DexBuffer getBuffer() {
		return buffer;
	}

	/**
	 * The index value of this object. Returns the position in the buffer of
	 * this method id in the method id pool
	 * 
	 * @return the index used to retrieve this method ID from the method ID pool
	 */
	public int getMethodIdIndex() {
		return buffer.methodIds().indexOf(this);
	}

	/**
	 * Determine whether the "accessFrom" class is allowed to get at "method".
	 */
	boolean dvmCheckMethodAccess(ClassDef accessFrom) {
		// TODO implement properly.
		return true;
	}

	/**
	 * Returns the method associated with this method ID. Always returns a valid
	 * ClassData.Method object. If it can't then an exception is thrown.
	 * 
	 * @return the ClassData.Method that this MethodID points to.
	 * @throws IllegalStateException
	 *             if the DEX file does not contain the
	 */
	public ClassData.Method resolveMethod() {

		// get the index of this method ID.
		int thisMethodId = getMethodIdIndex();
		String typeName = getDeclaringClassSignature();

		ClassDef cDef = buffer.getDef(typeName);
		if (cDef == null)
			throw new IllegalStateException(
					String.format(
							"The class definition associated with method %s in class %s is not found in this DEX file.",
							getName(), typeName));

		ClassData cData = buffer.readClassData(cDef);
		if (cData == null)
			throw new IllegalStateException(
					"There is no class data associated with this method.");

		ClassData.Method[] methods = cData.allMethods();
		for (ClassData.Method method : methods) {
			if (method.getMethodIndex() == thisMethodId)
				return method;
		}

		throw new IllegalStateException(
				"the associated ClassData.Method for this MethodId object was not found.");

	}

	/**
	 * Returns true if a call to resolveMethod() will complete without throwing
	 * an exception, false otherwise.
	 * 
	 * @return true if the Method class can be resolved with a call to
	 *         resolveMethod.
	 */
	public boolean isResolvable() {
		// get the index of this method ID.
		String typeName = getDeclaringClassSignature();

		ClassDef cDef = buffer.getDef(typeName);
		if (cDef == null)
			return false;

		if (cDef.getClassDataOffset() <= 0)
			return false;

		ClassData cData = buffer.readClassData(cDef);
		ClassData.Method[] methods = cData.allMethods();

		int thisMethodId = getMethodIdIndex();
		for (ClassData.Method method : methods) {
			if (method.getMethodIndex() == thisMethodId)
				return true;
		}

		return false;

	}

	/**
	 * Return the register type for the return value of this method. If it's a
	 * reference type we need to do the class lookup.
	 * 
	 * <p>
	 * Returned references are assumed to be initialized.
	 * 
	 * <p>
	 * Returns kRegTypeUnknown for "void".
	 */
	public RegisterType getReturnType() throws VerifyException {

		String signature = buffer.protoIds().get(protoIndex)
				.getReturnTypeString();

		switch (signature.charAt(0)) {
		case 'I':
			return RegisterType.newInstance(kRegTypeInteger);

		case 'C':
			return RegisterType.newInstance(kRegTypeChar);

		case 'S':
			return RegisterType.newInstance(kRegTypeShort);

		case 'B':
			return RegisterType.newInstance(kRegTypeByte);

		case 'Z':
			return RegisterType.newInstance(kRegTypeBoolean);

		case 'V':
			return RegisterType.newInstance(kRegTypeUnknown);

		case 'F':
			return RegisterType.newInstance(kRegTypeFloat);

		case 'D':
			return RegisterType.newInstance(kRegTypeDoubleLo);

		case 'J':
			return RegisterType.newInstance(kRegTypeLongLo);

		case 'L':
		case '[':
			RegisterType type = RegisterType.newInstance(kRegTypeRef);
			ProtoId pId = getBuffer().protoIds().get(getProtoIndex());
			type.setRefersTo(pId.getReturnTypeString(), IndexType.TYPE_REF);
			return type;

		default:
			// something else happened.
			throw new IllegalStateException(String.format(
					"the return type %s for method %s in class %s is invalid.",
					signature, getName(), getDeclaringClassSignature()));

		}

	}

	public MethodType getMethodType() {
		if (!isResolvable())
			return MethodType.PLATFORM;

		return resolveMethod().getMethodType();

	}

}
