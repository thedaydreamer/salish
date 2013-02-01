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
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConflict;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.oo.VerifyException;
import schilling.richard.dalvik.vm.oo.util.ClassPathUtil;

import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.util.Unsigned;

public final class ProtoId implements Comparable<ProtoId> {

	private final DexBuffer buffer;
	private final int shortyIndex;
	private final int returnTypeIndex;
	private final int parametersOffset;

	/**
	 * Resolved parameter list for this prototype. Do not write to the buffer.
	 */
	private List<String> parameters;
	private String returnTypeString;
	private String shortyString;

	public ProtoId(DexBuffer buffer, int shortyIndex, int returnTypeIndex,
			int parametersOffset) {
		this.buffer = buffer;
		this.shortyIndex = shortyIndex;
		this.returnTypeIndex = returnTypeIndex;
		this.parametersOffset = parametersOffset;

		try {
			verifyReturnType();
		} catch (VerifyException ex) {
			throw new IllegalStateException(
					"buffer contains a prototype with a return type signature that is invalid.",
					ex);
		}

		try {
			verifyParameters();
		} catch (VerifyException ex) {
			throw new IllegalStateException(
					"buffer contains a parameter with an invalid signature.",
					ex);
		}

	}

	public int compareTo(ProtoId other) {
		if (returnTypeIndex != other.returnTypeIndex) {
			return Unsigned.compare(returnTypeIndex, other.returnTypeIndex);
		}
		return Unsigned.compare(parametersOffset, other.parametersOffset);
	}

	public int getShortyIndex() {
		return shortyIndex;
	}

	public int getReturnTypeIndex() {
		return returnTypeIndex;
	}

	public int getParametersOffset() {
		return parametersOffset;
	}

	public String getShortyString() {

		if (shortyString == null)
			shortyString = buffer.strings().get(shortyIndex);

		return shortyString;
	}

	public String getReturnTypeString() {

		if (returnTypeString == null)
			returnTypeString = buffer.typeNames().get(returnTypeIndex);
		return returnTypeString;
	}

	/**
	 * Returns the list of parameter strings form the prototype. If no
	 * parameters are defined the list returned has a size of zero.
	 * 
	 * @return the list of parameters, or 0 if there are no parameters.
	 */
	public List<String> getParameters() {
		if (parameters == null) {
			parameters = new LinkedList<String>();

			List<String> types = buffer.typeNames();

			if (parametersOffset > 0) {
				DexBuffer.Section section = buffer.open(parametersOffset);
				int sz = section.readInt();
				for (int i = 0; i < sz; i++) {
					int tId = section.readUnsignedShort();
					parameters.add(types.get(tId));
				}
			}
		}

		return Collections.unmodifiableList(parameters);

	}

	public void verifyReturnType() throws VerifyException {
		ClassPathUtil.verifySignatureOrThrow(getReturnTypeString(), true);

	}

	/**
	 * Verifies that the parameter list associate with this method prototype
	 * contains valid signatures. Void signatures (type 'V' are not allowed). If
	 * the signatures of the parameters pass all tests then this function does
	 * nothing (has no side effect). If any of the signatures fails a test then
	 * this function throws an exception.
	 * 
	 * @throws VerifyException
	 *             if a parameter is invalid.
	 */
	public void verifyParameters() throws VerifyException {
		List<String> parameters = getParameters();
		for (String signature : parameters) {
			ClassPathUtil.verifySignatureOrThrow(signature, false);
		}

	}

	public void writeTo(DexBuffer.Section out) {
		out.writeInt(shortyIndex);
		out.writeInt(returnTypeIndex);
		out.writeInt(parametersOffset);
	}

	@Override
	public String toString() {
		if (buffer == null) {
			return shortyIndex + " " + returnTypeIndex + " " + parametersOffset;
		}

		return buffer.strings().get(shortyIndex) + ": "
				+ buffer.typeNames().get(returnTypeIndex) + " "
				+ buffer.readTypeList(parametersOffset);
	}

	/**
	 * Return the register type for the method. We can't just use the
	 * already-computed DalvikJniReturnType, because if it's a reference type we
	 * need to do the class lookup.
	 * 
	 * Returned references are assumed to be initialized.
	 * 
	 * Returns kRegTypeUnknown for "void".
	 */
	public RegisterType getMethodReturnType() {
		String retType = getReturnTypeString();
		RegisterType type = RegisterType.newInstance(kRegTypeUnknown);

		switch (retType.charAt(0)) {
		case 'I':
			type.setTypeEnum(kRegTypeInteger);
			break;
		case 'C':
			type.setTypeEnum(kRegTypeChar);
			break;
		case 'S':
			type.setTypeEnum(kRegTypeShort);
			break;
		case 'B':
			type.setTypeEnum(kRegTypeByte);
			break;
		case 'Z':
			type.setTypeEnum(kRegTypeBoolean);
			break;
		case 'V':
			type.setTypeEnum(kRegTypeUnknown);
			break;
		case 'F':
			type.setTypeEnum(kRegTypeFloat);
			break;
		case 'D':
			type.setTypeEnum(kRegTypeDoubleLo);
			break;
		case 'J':
			type.setTypeEnum(kRegTypeLongLo);
			break;
		case 'L':
		case '[':

			type.setTypeEnum(kRegTypeRef);
			type.setRefersTo(retType, IndexType.TYPE_REF);

			break;
		default:
			/* we verified signature return type earlier, so this is impossible */
			type.setTypeEnum(kRegTypeConflict);
			break;
		}

		return type;
	}

	public String dexProtoCopyMethodDescriptor() {
		StringBuilder result = new StringBuilder();
		result.append('(');
		List<String> parameters = getParameters();
		for (String param : parameters) {
			result.append(param);
			result.append(' ');
		}

		result.append(')');

		return result.toString();
	}

	public DexBuffer getBuffer() {
		return buffer;
	}
}
