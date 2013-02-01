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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.DvmGlobals;
import schilling.richard.dalvik.vm.PrimitiveType;
import schilling.richard.dalvik.vm.oo.util.ClassDefFactory;
import schilling.richard.dalvik.vm.oo.util.ClassLoaderUtil;

import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.rop.code.AccessFlags;

/**
 * A type definition.
 */
public final class ClassDef {

	public static final int NO_INDEX = -1;
	private final DexBuffer buffer;
	/**
	 * The position in the underlying buffer that this class is stored at.
	 */
	private final int offset; // *
	private final int typeIndex; // *
	private final int accessFlags; // *
	private final int supertypeIndex; // *
	private final int interfacesOffset;
	private final int sourceFileIndex; // *
	private final int annotationsOffset;
	private final int classDataOffset;
	private final int staticValuesOffset;

	/**
	 * do not save this to disk file.
	 */
	private List<String> interfacesList = null;

	public ClassDef(DexBuffer buffer, int offset, int typeIndex,
			int accessFlags, int supertypeIndex, int interfacesOffset,
			int sourceFileIndex, int annotationsOffset, int classDataOffset,
			int staticValuesOffset) {
		this.buffer = buffer;
		this.offset = offset;
		this.typeIndex = typeIndex;
		this.accessFlags = accessFlags;
		this.supertypeIndex = supertypeIndex;
		this.interfacesOffset = interfacesOffset;
		this.sourceFileIndex = sourceFileIndex;
		this.annotationsOffset = annotationsOffset;
		this.classDataOffset = classDataOffset;
		this.staticValuesOffset = staticValuesOffset;
	}

	public int getOffset() {
		return offset;
	}

	public int getTypeIndex() {
		return typeIndex;
	}

	public int getSupertypeIndex() {
		return supertypeIndex;
	}

	public int getInterfacesOffset() {
		return interfacesOffset;
	}

	private short[] interfaces;

	public short[] getInterfaces() {

		if (interfaces == null)
			interfaces = buffer.readTypeList(interfacesOffset).getTypes();

		return interfaces;
	}

	public int getAccessFlags() {
		return accessFlags;
	}

	public int getSourceFileIndex() {
		return sourceFileIndex;
	}

	public int getAnnotationsOffset() {
		return annotationsOffset;
	}

	public int getClassDataOffset() {
		return classDataOffset;
	}

	public int getStaticValuesOffset() {
		return staticValuesOffset;
	}

	public String getSignature() {
		return buffer.typeNames().get(typeIndex);
	}

	public String getSupertypeSignature() {
		return buffer.typeNames().get(supertypeIndex);
	}

	public String getSourceFileName() {
		return buffer.strings().get(sourceFileIndex);
	}

	@Override
	public String toString() {
		if (buffer == null) {
			return typeIndex + " " + supertypeIndex;
		}

		StringBuilder result = new StringBuilder();
		result.append(buffer.typeNames().get(typeIndex));
		if (supertypeIndex != NO_INDEX) {
			result.append(" extends ").append(
					buffer.typeNames().get(supertypeIndex));
		}
		return result.toString();
	}

	public boolean dvmIsInterfaceClass() {
		return (getAccessFlags() & AccessFlags.ACC_INTERFACE) != 0;
	}

	/**
	 * Returns true if this class implements the specified interface.
	 * 
	 * @param signature
	 *            the signature to check.
	 * @return true if this class implements the interface
	 */
	public boolean dvmImplements(String signature) {
		List<String> interfaces = getImplementedInterfaces();
		return interfaces.contains(signature) ? true : false;

	}

	public boolean dvmExtends(String signature) {
		List<String> classes = getExtendedClasses();
		return classes.contains(signature) ? true : false;
	}

	public List<String> getImplementedInterfaces() {
		if (interfacesList == null) {
			interfacesList = new LinkedList<String>();

			List<String> types = buffer.typeNames();
			short[] interfaces = getInterfaces();

			for (short ifaceType : interfaces) {
				interfacesList.add(types.get(ifaceType));
			}

		}
		return Collections.unmodifiableList(interfacesList);

	}

	private List<String> extendedSuperclasses;

	/**
	 * Returns a list of class types that this class definition extends.
	 * Includes platform classes as well, all the way back to java.lang.Object.
	 * 
	 * @return the list of classes that this definition implements, in order.
	 */
	public List<String> getExtendedClasses() {
		if (extendedSuperclasses == null) {
			extendedSuperclasses = new LinkedList<String>();

			// check for a few obvious cases first.
			String signature = getSignature();
			if (signature.equals(DvmGlobals.JAVA_LANG_OBJECT_SIGNATURE))
				return extendedSuperclasses;

			if (signature.charAt(0) == '[') {
				extendedSuperclasses.add(DvmGlobals.JAVA_LANG_OBJECT_SIGNATURE);
				return extendedSuperclasses;
			}

			// add all the superclasses that are found in the DexBuffer.
			String superSignature = getSupertypeSignature();
			ClassDef superDef = buffer.getDef(superSignature);

			while (superDef != null) {
				extendedSuperclasses.add(superSignature);

				superDef = buffer.getDef(superDef.getSupertypeSignature());
				superSignature = superDef.getSignature();
			}

			// add all the classes that are found on the platform.
			if (superSignature != null) {
				// sanity check
				boolean isPlatform = ClassDefFactory
						.isPlatformClassSpecifier(superSignature);
				if (!isPlatform)
					// no ClassDef and not found on the platform. Critical
					// error.
					throw new IllegalArgumentException(
							String.format("signature %s could not be found."));

				// convert to format for use with Class.forName();
				superSignature = ClassLoaderUtil
						.convertTypeToClassName(superSignature);

			}

			while (superSignature != null) {

				extendedSuperclasses.add(superSignature);

				if (superSignature.equals(DvmGlobals.JAVA_LANG_OBJECT)) {
					superSignature = null;
				} else {

					try {
						Class<?> superClass = Class.forName(superSignature);
						superSignature = superClass.getSuperclass()
								.getCanonicalName();

						StringBuilder sBuilder = new StringBuilder();
						sBuilder.append("L");
						sBuilder.append(superSignature.replace('.', '/'));
						sBuilder.append(";");
						superSignature = sBuilder.toString();
					} catch (ClassNotFoundException e) {
						throw new IllegalArgumentException(
								"unable to load class signature "
										+ superSignature);
					}

				}

			}
		}

		return extendedSuperclasses;

	}

	public boolean dvmImplements(ClassDef cDef) {
		return dvmImplements(cDef.getSignature());
	}

	public boolean dvmInstanceOf(ClassDef cDef) {
		return dvmInstanceOf(cDef.getSignature());

	}

	/**
	 * Returns true if this class def is an instance of the specified signature.
	 * 
	 * @param signature
	 *            the signature to check for.
	 * @return true if this class def is an instance of the specified signature.
	 */
	public boolean dvmInstanceOf(String signature) {
		if (signature.equals(getSignature()))
			return true;

		if (dvmImplements(signature))
			return true;

		if (dvmExtends(signature))
			return true;

		return false;

	}

	/**
	 * Returns all the classes that this definition implements. The list
	 * includes the interfaces first followed by the classes in order.
	 * 
	 * @return implemented interfaces and classes.
	 */
	public List<String> getSuperClasses() {
		List<String> result = new LinkedList<String>();

		result.addAll(getExtendedClasses());
		result.addAll(getImplementedInterfaces());

		return result;

	}

	public boolean isInstanceOf(String sig) {
		return getSuperClasses().contains(sig);
	}

	/**
	 * Returns true if this class definition is for an array class. This
	 * function determines whether or not the definition is for an array class
	 * by the value returned from getSignature().
	 * 
	 * @return true if this is an array class.
	 */
	public boolean dvmIsArrayClass() {
		List<String> types = buffer.typeNames();
		String type = types.get(typeIndex);
		if (type == null)
			throw new IllegalStateException(
					String.format(
							"The class definition for %s does not have a type in the type constant pool.",
							typeIndex));

		return type.charAt(0) == '[' ? true : false;
	}

	/**
	 * If this class def is for an array, return the number of dimensions. The
	 * number returned is also the index of the first character after all the
	 * brackets ('[').
	 * 
	 * @return the number of dimensions.
	 * @throws IllegalStateException
	 *             if this class def is not for an array.
	 */
	public int getArrayDimensions() {

		if (!dvmIsArrayClass())
			throw new IllegalStateException("class def is not an array class");

		return getSignature().lastIndexOf('[') + 1;

		// TODO where are the checks for maximum array dimension size.

	}

	/**
	 * Return true if this class is a primitive class.
	 * 
	 * @return
	 */
	public boolean dvmIsPrimitiveClass() {

		return PrimitiveType.dvmIsPrimitiveClass(getSignature(), false);

	}

	/**
	 * Returns the signature of the class type that is stored as part of the
	 * array (e.g. the element class).
	 * 
	 * @return the element class name.
	 * @throws IllegalStateException
	 *             if this class def is not an array class def.
	 */
	public String getElementClass() {
		if (!dvmIsArrayClass())
			throw new IllegalStateException("class def is not an array class");

		return getSignature().substring(getArrayDimensions());

	}

	public boolean dvmIsAbstractClass() {
		return (accessFlags & AccessFlags.ACC_ABSTRACT) != 0 ? true : false;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ClassDef))
			return false;

		ClassDef cDef = (ClassDef) obj;

		return this.getSignature().equals(cDef.getSignature());

	}
}
