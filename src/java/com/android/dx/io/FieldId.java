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

import com.android.dx.util.Unsigned;

public final class FieldId implements Comparable<FieldId> {

	private final DexBuffer buffer;
	private final int declaringClassIndex;
	private final int typeIndex;
	private final int nameIndex;

	public FieldId(DexBuffer buffer, int declaringClassIndex, int typeIndex,
			int nameIndex) {
		this.buffer = buffer;
		this.declaringClassIndex = declaringClassIndex;
		this.typeIndex = typeIndex;
		this.nameIndex = nameIndex;
	}

	public int getDeclaringClassIndex() {
		return declaringClassIndex;
	}

	public int getTypeIndex() {
		return typeIndex;
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public String getName() {
		return buffer.strings().get(nameIndex);
	}

	public String getType() {
		return buffer.typeNames().get(typeIndex);
	}

	public String getDeclaringClassType() {
		return buffer.typeNames().get(declaringClassIndex);
	}

	public int compareTo(FieldId other) {
		if (declaringClassIndex != other.declaringClassIndex) {
			return Unsigned.compare(declaringClassIndex,
					other.declaringClassIndex);
		}
		if (nameIndex != other.nameIndex) {
			return Unsigned.compare(nameIndex, other.nameIndex);
		}
		return Unsigned.compare(typeIndex, other.typeIndex); // should always be
																// 0
	}

	public void writeTo(DexBuffer.Section out) {
		out.writeUnsignedShort(declaringClassIndex);
		out.writeUnsignedShort(typeIndex);
		out.writeInt(nameIndex);
	}

	@Override
	public String toString() {
		if (buffer == null) {
			return declaringClassIndex + " " + typeIndex + " " + nameIndex;
		}
		return buffer.typeNames().get(typeIndex) + "."
				+ buffer.strings().get(nameIndex);
	}
}
