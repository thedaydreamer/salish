package schilling.richard.dexlib.inject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.dex.TableOfContents;
import com.android.dx.dex.TableOfContents.Section;
import com.android.dx.dex.file.DexFile;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.DexBuffer.AddressPointer;
import com.android.dx.io.DexBuffer.BufferedStringIdItem;
import com.android.dx.io.MethodId;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.merge.DexInjectMerger;
import com.android.dx.merge.MethodDefinition;

/**
 * This class contains code that searches through a DEX buffer and extracts the
 * bits of code that is to be injected into every function of the application.
 * 
 * @author rschilling
 */
public final class InjectUtil {

	public final static String LOG_TAG = "Finnr.InjectUtil";

	/**
	 * Returns the method id of the first method that exists in the package that
	 * starts with packagePrefix and the name that equals methodName
	 * 
	 * @return the the method id of the function found function
	 * @throws InjectException
	 *             if the method INJECT_FUNCTION_NAME is not found.
	 */
	public static int findFirstOccurrence(DexBuffer buffer,
			String signaturePrefix, String methodName) throws InjectException {

		if (signaturePrefix == null)
			throw new IllegalArgumentException("packagePrefix cannot be null");

		if (methodName == null)
			throw new IllegalArgumentException("methodName cannot be null");

		for (Iterator<ClassDef> iter = buffer.classDefs().iterator(); iter
				.hasNext();) {

			ClassDef cDef = iter.next();
			if (cDef.getClassDataOffset() == 0)
				continue;

			ClassData cData = buffer.readClassData(cDef);

			int typeIdx = cDef.getTypeIndex();
			String className = buffer.typeNames().get(typeIdx); // leave for
																// reference
			if (!className.startsWith(signaturePrefix))
				continue;

			// ClassData.Method only contains lookup information into the
			// buffer.
			ClassData.Method[] methods = cData.allMethods();
			if (methods != null) {
				for (ClassData.Method method : methods) {

					// if code offset is zero, then there is no code.
					if (method.getCodeOffset() > 0) {

						// get the method identifier - index into the method
						// identifiers section.
						int methodIdx = method.getMethodIndex();
						MethodId mId = buffer.methodIds().get(methodIdx);

						int nameIdx = mId.getNameIndex();

						String name = buffer.strings().get(nameIdx);
						if (name.equals(methodName))
							return methodIdx;

					}

				}
			}

		}

		throw new InjectException("the method "
				+ DexInjectMerger.INJECT_FUNCTION + " was not found.");

	}

	/**
	 * Calculate the number of Code objects in the dex file. Inject functions
	 * themselves are not counted. If all the booleans are set to true, then the
	 * result is the total number of code objects in the dex file.
	 * 
	 * @param buffer
	 *            the buffer to read code blocks from.
	 * @param includeInject
	 *            to include the number of code objects that are affected by
	 *            inject function.
	 * @param includeInjectStatic
	 *            to include the number of code objects that are affected by
	 *            inject static functions.
	 * @param includeInjectHttp
	 *            to include the number of code objecs that are affected by
	 *            inject http functions.
	 * @param includeInjectors
	 *            to include the injector functions themselves.
	 * @return the number of code objects in the dex file.
	 */
	public static int countCodeObjects(DexBuffer buffer, boolean byInject,
			boolean byInjectStatic, boolean byInjectHttp,
			boolean includeInjectors) {
		int result = 0;

		for (Iterator<ClassDef> iter = buffer.classDefs().iterator(); iter
				.hasNext();) {
			ClassDef cDef = iter.next();

			if (cDef.getClassDataOffset() == 0)
				continue;

			ClassData cData = buffer.readClassData(cDef);

			// ClassData.Method only contains lookup information into the
			// buffer.
			ClassData.Method[] methods = cData.allMethods();
			if (methods != null) {
				for (ClassData.Method method : methods) {

					// get the method identifier - index into the method
					// identifiers section.
					int methodIdx = method.getMethodIndex();
					MethodDefinition mDef = new MethodDefinition(buffer,
							methodIdx);

					// increasd count if this is an injecte method
					if (mDef.isInjectMethod()) {
						if (includeInjectors)
							result++;
						continue;

					}

					// if code offset is zero, then there is no code.
					if (method.getCodeOffset() == 0)
						continue;

					// TODO analysis - all logic functions can be expressed in
					// standardized ways so that whenever a variable is used in
					// a test that variable will be expressed in the same
					// manner.

					if (!method.dvmIsStaticMethod()) {
						if (byInject)
							result++;
						/*
						 * TODO uncomment if (byInjectHttp &&
						 * mDef.code().callsHttpExecute()) result++;
						 */

					} else if (byInjectStatic) {
						result++;

					}
				}
			}
		}

		return result;

	}

	/**
	 * Ensures that the dex file contains exactly one inject method for each
	 * method name. If sanity checks fail, this method throws a
	 * RuntimeException.
	 */
	public static void injectMethodSanity(DexBuffer buffer) {

		int count = getMethodCount(buffer,
				DexInjectMerger.INJECTOR_CLASS_PACKAGE,
				DexInjectMerger.INJECT_FUNCTION);
		if (count > 1)
			throw new RuntimeException(
					String.format(
							"there are %d functions with name %s in package %s.  There should only be one.",
							count, DexInjectMerger.INJECT_FUNCTION,
							DexInjectMerger.INJECTOR_CLASS_PACKAGE));

		count = getMethodCount(buffer, DexInjectMerger.INJECTOR_CLASS_PACKAGE,
				DexInjectMerger.INJECT_HTTP_FUNCTION);
		if (count > 1)
			throw new RuntimeException(
					String.format(
							"there are %d functions with name %s in package %s.  There should only be one.",
							count, DexInjectMerger.INJECT_HTTP_FUNCTION,
							DexInjectMerger.INJECTOR_CLASS_PACKAGE));

		count = getMethodCount(buffer, DexInjectMerger.INJECTOR_CLASS_PACKAGE,
				DexInjectMerger.INJECT_STATIC_FUNCTION);
		if (count > 1)
			throw new RuntimeException(
					String.format(
							"there are %d functions with name %s in package %s.  There should only be one.",
							count, DexInjectMerger.INJECT_STATIC_FUNCTION,
							DexInjectMerger.INJECTOR_CLASS_PACKAGE));

	}

	/**
	 * Counts the number of methods in pacakges that have names that start with
	 * packagePrefix and a name that matches methodName
	 * 
	 * @param buffer
	 * @param packagePrefix
	 * @param methodName
	 * @return
	 */
	public static int getMethodCount(DexBuffer buffer, String signaturePrefix,
			String methodName) {

		if (signaturePrefix == null)
			throw new IllegalArgumentException("packagePrefix cannot be null");

		if (methodName == null)
			throw new IllegalArgumentException("methodName cannot be null");

		int result = 0;

		for (Iterator<ClassDef> iter = buffer.classDefs().iterator(); iter
				.hasNext();) {

			ClassDef cDef = iter.next();
			if (cDef.getClassDataOffset() == 0)
				continue;

			ClassData cData = buffer.readClassData(cDef);

			int typeIdx = cDef.getTypeIndex();
			String className = buffer.typeNames().get(typeIdx); // leave for
																// reference
			if (!className.startsWith(signaturePrefix))
				continue;

			// ClassData.Method only contains lookup information into the
			// buffer.
			ClassData.Method[] methods = cData.allMethods();
			if (methods != null) {
				for (ClassData.Method method : methods) {

					// if code offset is zero, then there is no code.
					if (method.getCodeOffset() > 0) {

						// get the method identifier - index into the method
						// identifiers section.
						int methodIdx = method.getMethodIndex();
						MethodId mId = buffer.methodIds().get(methodIdx);

						int nameIdx = mId.getNameIndex();

						String name = buffer.strings().get(nameIdx);
						if (name.equals(methodName))
							result++;

					}

				}
			}

		}

		return result;

	}

	/**
	 * Count all the code objects in the dex file.
	 * 
	 * @return the number of code objects in the dex file.
	 */
	public static int countCodeObjects() {
		// TODO implement.
		return 0;

	}

	public static int countCodeSize(DexBuffer buffer, boolean includeInject,
			boolean includeInjectStatic, boolean includeInjectHttp) {

		int result = 0;

		for (Iterator<ClassDef> iter = buffer.classDefs().iterator(); iter
				.hasNext();) {
			ClassDef cDef = iter.next();
			ClassData cData = buffer.readClassData(cDef);

			// ClassData.Method only contains lookup information into the
			// buffer.
			ClassData.Method[] methods = cData.allMethods();
			if (methods != null) {
				for (ClassData.Method method : methods) {

					// get the method identifier - index into the method
					// identifiers section.
					int methodIdx = method.getMethodIndex();
					MethodId mId = buffer.methodIds().get(methodIdx);

					int nameIdx = mId.getNameIndex();

					String name = buffer.strings().get(nameIdx);

					// if code offset is zero, then there is no code.
					if (method.getCodeOffset() > 0) {
						if (!name.equals(DexInjectMerger.INJECT_FUNCTION)) {

							// the result is found.
							method.getCodeOffset();

							Code code = buffer.readCode(method);

							result += code.getSizeOf();
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Creates a new array. The result contains the values of sArray with short
	 * val inserted at position pos count times. The length of the returned
	 * value is sArray.length + count. If pos equals sArray.length, then val is
	 * appended onto the end of sArray count times.
	 * 
	 * @param sArray
	 *            the array to use as the base
	 * @param val
	 *            the value to insert into the resulting array
	 * @param pos
	 *            the position in sArray that the values should be inserted.
	 * @param count
	 *            the number of times that val should be inserted.
	 * @return a duplicate sArray with the value val inserted count times.
	 */
	public static byte[] insertInto(byte[] bArray, int pos, byte val, int count) {

		if (pos < 0)
			throw new IndexOutOfBoundsException("pos cannot be < 0");

		if (count < 0)
			throw new IllegalArgumentException("count cannot be < 0");

		if (bArray == null)
			return null;

		if (pos > bArray.length)
			throw new IndexOutOfBoundsException(
					"pos cannot be greater than the length of sArray.");

		byte[] result = new byte[bArray.length + count];
		for (int i = 0; i < result.length; i++) {
			result[i] = Byte.MIN_VALUE + 1;
		}

		System.arraycopy(bArray, 0, result, 0, pos);
		for (int i = 0; i < count; i++) {
			result[pos + i] = val;
		}

		int remainderStart = pos + count;
		int remainderLength = bArray.length - pos;

		if (remainderLength > 0)
			System.arraycopy(bArray, pos, result, remainderStart,
					remainderLength);
		byte[] resultSection = new byte[4];
		System.arraycopy(result, 3909, resultSection, 0, 4);

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < resultSection.length; i++) {
			builder.append(Byte.toString(resultSection[i])).append(" ");
		}

		Log.i(LOG_TAG, builder.toString());

		return result;

	}

	/**
	 * Creates a new array. The result contains the values of sArray with short
	 * val inserted at position pos count times. The length of the returned
	 * value is sArray.length + count. If pos equals sArray.length, then val is
	 * appended onto the end of sArray count times.
	 * 
	 * @param sArray
	 *            the array to use as the base
	 * @param val
	 *            the value to insert into the resulting array
	 * @param pos
	 *            the position in sArray that the values should be inserted.
	 * @param count
	 *            the number of times that val should be inserted.
	 * @return a duplicate sArray with the value val inserted count times.
	 */
	public static short[] insertInto(short[] sArray, int pos, short val,
			int count) {

		if (pos < 0)
			throw new IndexOutOfBoundsException("pos cannot be < 0");

		if (count < 0)
			throw new IllegalArgumentException("count cannot be < 0");

		if (sArray == null)
			return null;

		if (pos > sArray.length)
			throw new IndexOutOfBoundsException(
					"pos cannot be greater than the length of sArray.");

		short[] result = new short[sArray.length + count];

		System.arraycopy(sArray, 0, result, 0, pos);
		int i;
		for (i = pos; i < count; i++) {
			result[i] = val;
		}

		int remainder = sArray.length - pos;

		if (remainder > 0) {
			System.arraycopy(sArray, pos, result, i, remainder);

		}

		return result;

	}

	public static short[] insertInto(short[] target, int pos,
			short[] valuesToInsert) {

		if (pos < 0)
			throw new IndexOutOfBoundsException("pos cannot be < 0");

		if (target == null)
			return null;

		if (valuesToInsert == null)
			valuesToInsert = new short[0];

		if (pos > target.length)
			throw new IndexOutOfBoundsException(
					"pos cannot be greater than the length of sArray.");

		short[] result = new short[target.length + valuesToInsert.length];

		System.arraycopy(target, 0, result, 0, pos);
		System.arraycopy(valuesToInsert, 0, result, pos, valuesToInsert.length);

		int remainder = target.length - pos; // amount left to copy from sArray.
		// don't copy more in the case of appending.
		if (remainder > 0) {
			int destPos = pos + valuesToInsert.length; // dest array state =
			// sArray[0..pos] concatenated
			// with values[all].
			System.arraycopy(target, pos, result, destPos, remainder);

		}

		return result;

	}

	public static DecodedInstruction[] insertInto(DecodedInstruction[] target,
			int pos, DecodedInstruction[] valuesToInsert) {

		if (pos < 0)
			throw new IndexOutOfBoundsException("pos cannot be < 0");

		if (target == null)
			return null;

		if (valuesToInsert == null)
			valuesToInsert = new DecodedInstruction[0];

		if (pos > target.length)
			throw new IndexOutOfBoundsException(
					"pos cannot be greater than the length of sArray.");

		DecodedInstruction[] result = new DecodedInstruction[target.length
				+ valuesToInsert.length];

		System.arraycopy(target, 0, result, 0, pos);
		System.arraycopy(valuesToInsert, 0, result, pos, valuesToInsert.length);

		int remainder = target.length - pos; // amount left to copy from sArray.
		// don't copy more in the case of appending.
		if (remainder > 0) {
			int destPos = pos + valuesToInsert.length; // dest array state =
			// sArray[0..pos] concatenated
			// with values[all].
			System.arraycopy(target, pos, result, destPos, remainder);

		}

		return result;

	}

	/**
	 * Clones this DexBuffer and simultaneously makes the following changes: 1.
	 * adjusts all offset references throughout the buffer, and 2. inserts
	 * adjustment blank spaces at position.
	 * 
	 * @param buff
	 *            the buffer to adjust.
	 * @param position
	 *            the position after which all offset references should be
	 *            adjusted for
	 * @param adjustment
	 *            the amount to add to the offset (must be >= 0).
	 * @throws IOException
	 */
	public static DexBuffer cloneBufferWithInsertion(DexBuffer buff,
			int position, int adjustment, byte insertedValue)
			throws IOException {

		if (adjustment < 0)
			throw new IllegalArgumentException(
					"Adjustment must be positive or zero"); // TODO
															// allow
															// negative
															// values

		if (buff == null)
			throw new IllegalArgumentException("buff cannot be null.");

		byte[] buffBytes = buff.getBytes();

		if (position < 0 || position >= buffBytes.length)
			throw new IllegalArgumentException("position cannot be null.");

		// see DexBuffer.appendSection - all adjustments to sizes seem to be on
		// a four byte boundary. Excess is then removed later ...
		int alignedAdjustment = adjustment; // DexBuffer.fourByteAlign(adjustment);
											// // TODO
											// what
											// about
											// adjustment
											// values
											// < 0.
											// Check.
		// clone the buffer
		byte[] copyBytes = new byte[buffBytes.length];
		System.arraycopy(buffBytes, 0, copyBytes, 0, buffBytes.length);

		DexBuffer copyBuffer = new DexBuffer(copyBytes);
		byte[] copyBufferBytes = copyBuffer.getBytes(); // same as copyBytes?

		DexBuffer.Section existing = copyBuffer.open(112);
		int existingint = existing.readInt();

		// test reloation of strings...
		for (BufferedStringIdItem item : copyBuffer.bufferedStringIds()) {
			List<AddressPointer> apList = item.getAddressPointers();
			if (apList.size() != 1)
				throw new IllegalStateException(
						"String Ids should only have one pointer");

			for (AddressPointer ap : apList) {

				int addr = ap.getAddress();

				String s = copyBuffer.open(addr).readString();

				Log.i(LOG_TAG, "Address Pointer to string : " + ap.getAddress()
						+ "->" + ap.getContents() + " (" + s + ")");

			}

		}

		/*
		 * adjust pointers in the dex file
		 */

		List<AddressPointer> pointers = copyBuffer.getAllAddressPointers();
		for (AddressPointer pointer : pointers) {
			pointer.adjustReferencedAddress(position, alignedAdjustment);
		}

		DexBuffer.Section changed = copyBuffer.open(112);
		int changedInt = changed.readInt();

		int expected = existingint + alignedAdjustment;

		if (changedInt != expected)
			throw new IllegalStateException("the adjustment failed.");

		// modify positions of sections themselves - this will reflect in the
		// map and the header.

		// now the header. FIXME changes addresses!
		adjustHeader(copyBuffer, position, alignedAdjustment);

		DexBuffer.Section resultSection1 = copyBuffer.open(112);
		int resultInt1 = resultSection1.readInt();

		// state - all references to addresses are updated. expand the array.

		byte[] resultBytes = InjectUtil.insertInto(copyBuffer.getBytes(),
				position, insertedValue, alignedAdjustment);

		DexBuffer result = new DexBuffer(resultBytes);

		byte[] resultBufferBytes2 = result.getBytes();
		DexBuffer.Section resultSection2 = copyBuffer.open(112);
		int resultInt2 = resultSection2.readInt();

		if (changedInt != resultInt2)
			throw new IllegalStateException("insert fail.");

		// test reloation of strings...
		for (BufferedStringIdItem item : result.bufferedStringIds()) {
			List<AddressPointer> apList = item.getAddressPointers();
			if (apList.size() != 1)
				throw new IllegalStateException(
						"String Ids should only have one pointer");

			for (AddressPointer ap : apList) {

				int addr = ap.getAddress();

				String s = result.open(addr).readString();

				Log.i(LOG_TAG, "Address Pointer to string : " + ap.getAddress()
						+ "->" + ap.getContents() + " (" + s + ")");

			}

		}

		TableOfContents resultToc = result.getTableOfContents();
		return result;

	}

	/**
	 * Update the header offsets by adjustment bytes. All offsets found in the
	 * DEX header are in a TableOfContents object. This is the last thing that
	 * should be called during the update procedure.
	 * <p>
	 * For the time being, we'll assume that we're adding adjustments to the
	 * data section. This is uint aligned data. See DexFile for more information
	 * on how to add to an arbitrary section. TODO add to an arbitrary section.
	 * 
	 * @param buffer
	 *            the buffer to update offsets for.
	 * @param position
	 *            the position after which an offset should be adjusted.
	 * @param adjustment
	 *            the amount to adjust things by.
	 */
	private static void adjustHeader(DexBuffer buffer, int position,
			int adjustment) {

		if (buffer == null)
			throw new IllegalArgumentException("buffer cannot be null.");

		if (!adjustmentSanityCheck(buffer, position, adjustment))
			throw new IllegalArgumentException(
					"bufer cannnot be adjusted without error.");

		TableOfContents toc = buffer.getTableOfContents();
		int oldMapOffset = toc.mapList.off;

		// toc.magic - ignore
		// toc.checksum - see below (the reason this function is called last).
		// toc.signature - see below (the reason this function is called last).
		toc.fileSize += adjustment;

		// toc.headerSize - ignore (fixed value)
		// toc.endian_tag - ignore (we don't want to change this).
		// toc.linkSize - ignore (we don't want to change this).
		if (toc.linkSize > 0 && toc.linkOff >= position)
			// file is statically linked if the linkSize > 0.
			toc.linkOff += adjustment;

		if (toc.dataOff >= position)
			toc.dataOff += adjustment;
		toc.dataSize = buffer.getBytes().length - toc.dataOff + adjustment; // FIXME
																			// adjustment
																			// always
																			// happen
																			// in
																			// data
																			// section.
																			// Make
																			// this
																			// arbitrary.

		for (TableOfContents.Section s : toc.sections) {
			// this will also include the map offset.
			if (s.off >= position) {
				Log.i(LOG_TAG,
						"adjusting offset of section " + Section.toName(s.type)
								+ " " + s.off + "->"
								+ Integer.toString(s.off + adjustment));
				s.off += adjustment;
			}

		}

		try {
			toc.writeHeader(buffer.open(0)); // save the header
			toc.writeMap(buffer.open(oldMapOffset)); // save the map data
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		/* calculate checksum and signature last. */
		DexFile.calcChecksum(buffer.getBytes());
		DexFile.calcSignature(buffer.getBytes());

	}

	// TODO create DEX file sanity check. use the rues in adjustmentSanityCheck.
	/**
	 * Checks to see if a buffer's offsets can be modified without causing an
	 * error.
	 * 
	 * @param buff
	 *            the buffer to check.
	 * @param position
	 *            the position of the offset adjustment
	 * @param adjustment
	 *            the adjustment that offsets will be made by.
	 * @return true if the adjustment can be made without error or false
	 *         otherwise.
	 */
	private static boolean adjustmentSanityCheck(DexBuffer buff, int position,
			int adjustment) {
		TableOfContents toc = buff.getTableOfContents();

		// make sure the position is in the data section.
		if ((position < toc.dataOff)
				|| (position >= (toc.dataOff + toc.dataSize)))
			throw new IllegalArgumentException(
					"position must be in the data section");

		if (toc.linkSize > 0 && (toc.linkOff >= position)
				&& (toc.linkOff + adjustment) < 0)
			return false;

		if (toc.mapList.off > 0 && (toc.mapList.off >= position)
				&& (toc.mapList.off + adjustment) < 0)
			return false;

		if (toc.stringIds.off >= position) {
			if (toc.stringIds.off + adjustment < 0)
				return false;

			if ((toc.stringIds.off + adjustment) < toc.headerSize) // header
																	// size is
																	// also the
																	// position
																	// in
																	// the file
																	// that
																	// is one
																	// byte
																	// after the
																	// header
																	// ends.
				return false;

		}

		if (toc.typeIds.off >= position && (toc.typeIds.off + adjustment) < 0)
			return false;

		if (toc.protoIds.off >= position && (toc.protoIds.off + adjustment) < 0)
			return false;

		return true;
	}

	public static MethodDefinition extractinjectionMethod(DexBuffer buff)
			throws InjectException {
		return new MethodDefinition(buff, InjectUtil.findFirstOccurrence(buff,
				DexInjectMerger.INJECTOR_CLASS_PACKAGE,
				DexInjectMerger.INJECT_FUNCTION));

	}

	public static MethodDefinition extractStaticInjectionMethod(DexBuffer buff)
			throws InjectException {
		return new MethodDefinition(buff, InjectUtil.findFirstOccurrence(buff,
				DexInjectMerger.INJECTOR_CLASS_PACKAGE,
				DexInjectMerger.INJECT_STATIC_FUNCTION));
	}

	public static MethodDefinition extractHttpInjectionMethod(DexBuffer buff)
			throws InjectException {
		return new MethodDefinition(buff, InjectUtil.findFirstOccurrence(buff,
				DexInjectMerger.INJECTOR_CLASS_PACKAGE,
				DexInjectMerger.INJECT_HTTP_FUNCTION));
	}

	/**
	 * Returns true if any instruciton in mDef makes a call to mId.
	 * 
	 * @param mDef
	 *            the method definition to iterate through (instructions).
	 * @param mId
	 *            the method id to check for a call to.
	 * @return true if mDef calls mId or false otherwise.
	 */
	public static boolean makesCallTo(MethodDefinition mDef, MethodId mId) {

		if (mDef == null)
			throw new IllegalArgumentException("mDef cannot be null.");

		if (mId == null)
			throw new IllegalArgumentException("mId cannot be null.");

		if (!mDef.methodId().isResolvable())
			return false;

		InstructionList iList = mDef.instructionsDecodedAsList();
		for (int i = 0; i < iList.size(); i++) {
			DecodedInstruction instruction = iList.valueAt(i);

			// register reference B is always the method id index of the target.
			if (instruction.isInvoke()
					&& instruction.getIndex() == mId.getMethodIdIndex())
				return true;
		}

		return false;
	}

	public static final String STATUS_LINE_SIGNATURE = "Lorg/apache/http/StatusLine;";
	public static final String STATUS_LINE_GETSTATUSCODE_NAME = "getStatusCode";
	public static final String STATUS_LINE_GETSTATUSCODE_SHORTY = "I";

	/**
	 * Returns the MethodDeclaration for StatusLine.getStatusCode().
	 * 
	 * @param buffer
	 *            the buffer to search
	 * @return the class id of the
	 */
	public static MethodDefinition getStatusLineMethod(DexBuffer buffer) {

		ClassDef cDef = buffer.getDef(STATUS_LINE_SIGNATURE);
		if (cDef == null)
			throw new IllegalStateException(String.format(
					"class definition for %s is not present in the buffer",
					STATUS_LINE_SIGNATURE));

		// find the method id
		List<MethodId> methodIds = buffer.methodIds();

		for (MethodId methodId : methodIds) {

			MethodDefinition result = new MethodDefinition(buffer,
					methodId.getMethodIdIndex());

			if (result.callsHttpExecute())
				return result;

		}

		throw new IllegalStateException(String.format(
				"class definition for %s is not present in the buffer",
				STATUS_LINE_SIGNATURE));

	}

	

	/**
	 * Returns the address (key) of the instruction that contains the return
	 * instruction.
	 * 
	 * @param instructions
	 *            the instructions to find the return instruction in.
	 * @return the address of the return instruction
	 * @throws IllegalArgumentException
	 *             if a return statement is not found in the list.
	 * 
	 */
	public static int findReturnAddress(InstructionList instructions) {

		if (instructions == null)
			throw new IllegalArgumentException("instructions cannot be null");

		for (int i = 0; i < instructions.size(); i++) {
			DecodedInstruction instruction = instructions.valueAt(i);

			if (instruction.isReturnInstruction())
				return instructions.keyAt(i);
		}

		throw new IllegalArgumentException("return instruction not found.");

	}

	/**
	 * Given a register map, converts an instruction to a form of the
	 * instruction that can handle the registers, if necessary.
	 * 
	 * @param instruction
	 *            the instruction to convert
	 * @param registerMap
	 *            the register map used to indicate how wide registers are.
	 * @return the same instruction if no conversion is needed or an equivalent
	 *         instruction of the same type that can handle those registers.
	 */
	public static DecodedInstruction promoteInstruction(
			DecodedInstruction instruction, SparseIntArray registerMap) {
		// TODO implement
		return instruction;
	}

}
