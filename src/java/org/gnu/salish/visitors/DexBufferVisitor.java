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

import schilling.richard.dalvik.vm.InstructionList;
import android.util.SparseArray;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;

/**
 * Visits artifacts in a dex buffer. The current version only visits methods.
 * 
 * @author rschilling
 * 
 * @since 1.0
 * 
 */
public class DexBufferVisitor {

	/**
	 * The DEX buffer that is being visited.
	 */
	private final DexBuffer buffer;

	/**
	 * Method listeners
	 */
	private List<DexFileListener> listeners = new LinkedList<DexFileListener>();

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
	 * Registers a code listener. This function has no effect if the specified
	 * listener is already registered.
	 * 
	 * @param listener
	 *            the listener to register.
	 * @since 1.0
	 */
	public void registerListener(DexFileListener listener) {
		synchronized (DexFileListener.class) {

			if (listener == null)
				return;

			if (!listeners.contains(listener))
				listeners.add(listener);
		}
	}

	/**
	 * Unregisters a class def listener. This function has no effect if listener
	 * is null or if it's not currently registered.
	 * 
	 * @param listener
	 *            the listener to un-register.
	 * @return the same parameter passed to the call.
	 * @since 1.0
	 */
	public boolean unregisterListener(DexFileListener listener) {

		synchronized (DexFileListener.class) {

			if (listener == null)
				return false;

			return listeners.remove(listener);

		}

	}

	private void throwIfInterrupted() throws InterruptedException {

		if (Thread.interrupted())
			throw new InterruptedException("processing cancelled");

	}

	/**
	 * Visits all the classes defined in the DEX file, their methods and their
	 * code. The number of classes visited are returned, which may be less than
	 * the number of ClassDef items in the file (See
	 * DexFileListener.shouldVisit(ClassDef). This method does nothing if no
	 * {@link DexFileListener DexFileListener} objects are registered.
	 * 
	 * @return the number of methods visited by at least one visitor.
	 * @throws InterruptedException
	 * @since 1.0
	 */
	public int visitClasses() throws InterruptedException {

		if (listeners.size() == 0)
			return 0;

		int result = 0;
		List<DexFileListener> activeListeners = new LinkedList<DexFileListener>();
		SparseArray<ClassData> classData = buffer.classData();
		Iterable<ClassDef> cIterable = buffer.classDefs();
		List<MethodId> methods = buffer.methodIds();
		List<ProtoId> protoIds = buffer.protoIds();

		for (Iterator<ClassDef> iter = cIterable.iterator(); iter.hasNext();) {

			throwIfInterrupted();

			ClassDef cDef = iter.next();
			int dataOffset = cDef.getClassDataOffset();

			/*
			 * Fill a list with listeners where shouldVisit(ClassDef) return
			 * true. Changes for every class.
			 */
			activeListeners.clear();

			for (DexFileListener listener : listeners) {
				throwIfInterrupted();
				if (listener.shouldVisit(cDef)) {
					activeListeners.add(listener);
					listener.onClassDefFound(cDef);
					result++;
				}
			}

			if (activeListeners.size() == 0)
				continue;

			if (dataOffset == 0)
				continue;

			ClassData cData = classData.get(dataOffset);

			Method[] classMethods = cData.allMethods();
			for (Method m : classMethods) {

				throwIfInterrupted();

				// TODO add shouldVisit(Method)

				int mIdx = m.getMethodIndex();
				MethodId mId = methods.get(mIdx);
				ProtoId pId = protoIds.get(mId.getProtoIndex());

				for (DexFileListener listener : activeListeners) {
					if (listener.shouldVisit(cDef, m, mId, pId)) {

						throwIfInterrupted();

						listener.onMethodFound(cDef, m, mId, pId);

						if (m.getCodeOffset() == 0)
							continue;

						Code code = buffer.readCode(m);
						if (listener.shouldVisit(cDef, m, mId, pId, code)) {

							throwIfInterrupted();

							InstructionList iList = code.decodeAllAsList();
							listener.onCodeFound(buffer, cDef, m, mId, pId,
									code, iList);
							if (listener.doIterateCode()) {

								throwIfInterrupted();

								for (int i = 0; i < iList.size(); i++) {

									listener.onInstructionFound(cDef, m, mId,
											pId, iList.keyAt(i),
											iList.valueAt(i));

								}

								listener.onCodeVisited(buffer, cDef, m, mId,
										pId, code, iList);
							}

						}

					}
				}

			}

		}

		return result;

	}

	/**
	 * Encoded methods are read in from Class Data. MethodIdentifiers don't have
	 * to be. So while each encoded method (Method) will have a MethodId, only
	 * some MethodId items will have an assocaited ClassData.Method.
	 * 
	 * @throws InterruptedException
	 */
	public int visitMethodIdentifiers() throws InterruptedException {
		int result = 0;

		if (listeners.size() == 0)
			return result;

		List<MethodId> methodIds = buffer.methodIds();

		for (MethodId mId : methodIds) {
			for (DexFileListener listener : listeners) {
				throwIfInterrupted();
				if (listener.shouldVisit(mId)) {
					listener.onMethodIdFound(mId);
					result++;
				}
			}
		}

		return result;
	}

}
