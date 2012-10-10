/*
 * Copyright 2012 Richard Schilling
 * coderroadie@gmail.com
 * 
 * The Salish Library is licensed under the GPLv3 license, which is reproduced below:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gnu.salish.visitors;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.SparseArray;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;

/**
 * Visits artifacts in a dex buffer. The current version only visits methods.
 * 
 * @author rschilling
 * 
 * @since 1.0
 * 
 */
abstract public class DexBufferVisitor {

	/**
	 * The DEX buffer that is being visited.
	 */
	private final DexBuffer buffer;

	/**
	 * Method listeners
	 */
	private List<MethodListener> methodListeners = new LinkedList<MethodListener>();

	/**
	 * Creates a visitor that will process a DEX buffer.
	 * 
	 * @param buffer
	 *            the buffer to visit
	 * @since 1.0
	 */
	public DexBufferVisitor(DexBuffer buffer) {

		if (buffer == null)
			throw new IllegalArgumentException("buffer cannot be null");

		this.buffer = buffer;

	}

	/**
	 * Creates a visitor that will process DEX file.
	 * 
	 * @param inputFile
	 *            the input file to open - must be a DEX file.
	 * @since 1.0
	 * 
	 */
	public DexBufferVisitor(File inputFile) throws IOException {

		if (inputFile == null)
			throw new IllegalArgumentException("inputFile cannot be null");

		if (inputFile.getName().endsWith(".dex"))
			throw new IllegalArgumentException(
					"the file name must have a .dex extension");

		this.buffer = new DexBuffer(inputFile);
	}

	/**
	 * Registers a method listener. This function has no effect if the specified
	 * listener is already registered.
	 * 
	 * @param listener
	 *            the listener to register.
	 * @since 1.0
	 */
	public void registerListener(MethodListener listener) {
		synchronized (MethodListener.class) {

			if (listener == null)
				return;

			if (!methodListeners.contains(listener))
				methodListeners.add(listener);
		}
	}

	/**
	 * Unregisters a method listener. This function has no effect if listener is
	 * null or if it's not currently registered.
	 * 
	 * @param listener
	 *            the listener to un-register.
	 * @return the same parameter passed to the call.
	 * @since 1.0
	 */
	public MethodListener unregisterListener(MethodListener listener) {

		synchronized (MethodListener.class) {
			if (listener == null)
				return null;

			if (!methodListeners.contains(listener))
				return null;
			methodListeners.remove(listener);

		}
		return listener;

	}

	/**
	 * Visits all the methods in the DEX file. The number of methods visited are
	 * returned. This mehthod does nothing if no {@link MethodListener
	 * MethodListener} objects are registers.
	 * 
	 * @return the number of methods visited by at least one visitor.
	 * @since 1.0
	 */
	public int visitMethods() {

		if (methodListeners.size() == 0)
			return 0;

		int result = 0;

		SparseArray<ClassData> classData = buffer.classData();
		Iterable<ClassDef> cIterable = buffer.classDefs();
		List<MethodId> methods = buffer.methodIds();
		List<ProtoId> protoIds = buffer.protoIds();

		for (Iterator<ClassDef> iter = cIterable.iterator(); iter.hasNext();) {
			ClassDef cDef = iter.next();
			int dataOffset = cDef.getClassDataOffset();

			if (dataOffset == 0)
				continue;

			/*
			 * Fill a list with listeners where shouldVisit(ClassDef) return
			 * true.
			 */
			List<MethodListener> activeListeners = new LinkedList<MethodListener>();

			for (MethodListener listener : methodListeners) {
				if (listener.shouldVisit(cDef))
					activeListeners.add(listener);
			}

			if (activeListeners.size() == 0)
				continue;

			ClassData cData = classData.get(dataOffset);
			Method[] classMethods = cData.allMethods();
			for (Method m : classMethods) {

				int mIdx = m.getMethodIndex();
				MethodId mId = methods.get(mIdx);
				ProtoId pId = protoIds.get(mId.getProtoIndex());

				boolean found = false;
				for (MethodListener listener : activeListeners) {
					if (listener.shouldVisit(cDef, m, mId, pId)) {
						found = true;
						listener.onMethodFound(cDef, m, mId, pId);
					}
				}
				if (found)
					result++;

			}

		}

		return result;

	}
}
