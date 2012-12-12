package org.gnu.salish.visitors;

import schilling.richard.dalvik.vm.InstructionList;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Marker interface for any class that is a DEX file listener.
 * 
 * @author rschilling
 * 
 */
public abstract class DexFileListener {
	/**
	 * Called whenever a ClassDef is found in a DEX file. The code offset of the
	 * class is not checked prior to calling this function.
	 * 
	 * @param cDef
	 *            the class definition that was found.
	 */
	public void onClassDefFound(ClassDef cDef) {
		throw new UnsupportedOperationException(
				"this method must be overridden if shouldVisit(ClassDef) returns true.");
	}

	/**
	 * Called whenever a new class is found so that it can be determined whether
	 * or not the class should be skipped. If this method returns false, then
	 * the class will be skipped for this listener entirely.
	 * 
	 * <p>
	 * {@code ClassDef.getClassDataOffset()} code offset will have already been
	 * checked and is guaranteed to be > 0.
	 * 
	 * @param cDef
	 *            the class definition to check.
	 * @return true if the class should be processed.
	 * @since 1.0
	 */
	public boolean shouldVisit(ClassDef cDef) {
		return false;
	}

	/**
	 * Called whenever a new method is found so that it can be determined
	 * whether or not the method should be passed to onMethodFound.
	 * 
	 * @param cDef
	 *            the class definition that defines the method.
	 * @param method
	 *            the method definition.
	 * @param mId
	 *            the method id definition
	 * @param pId
	 *            the method's prototype
	 * @since 1.0
	 */
	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {
		return false;
	}

	public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId, Code code) {
		return false;
	}

	/**
	 * Called whenever a new method in the DEX file is found. If this method is
	 * called it is because: 1. shouldVisit(ClassDef) returned true, 2.
	 * ClassDef.getDataOffset() > 0, 3. cDef.allMethods returned some values in
	 * it, 4. shouldVisit(ClassDef, Method, MethodId, ProtoId) returned true.
	 * 
	 * @param cDef
	 *            the class definition that defines the method.
	 * @param method
	 *            the method definition.
	 * @param mId
	 *            the method id definition
	 * @param pId
	 *            the method's prototype
	 * @since 1.0
	 */
	public void onMethodFound(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId) {

		throw new UnsupportedOperationException(
				"this method must be overridden if shouldVisit(ClassDef, Method, MethodId, ProtoId) returns true.");
	}

	/**
	 * If true, the DexBufferVisitor will iterate through each instruction in
	 * the code immediately after calling onCodeFound. The method
	 * onInstructionFound is called for each instruction in this case.
	 */
	public boolean doIterateCode() {
		return false;
	}

	/**
	 * Called by DexBufferVisitor whenever a method's Code is found.
	 * 
	 * @param cDef
	 *            the class definition where the code is defined
	 * @param method
	 *            the method where the code is defined.
	 * @param mId
	 *            the associated MethodId
	 * @param pId
	 *            the associated ProtoId.
	 * @param methodCode
	 *            the code object that contains the code.
	 */
	public void onCodeFound(DexBuffer buffer, ClassDef cDef, Method method,
			MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

		throw new UnsupportedOperationException(
				"this method must be overridden if shouldVisit(ClassDef, Method, MethodId, ProtoId, Code) returns true.");

	}

	/**
	 * Called by DexBufferVisitor whenever a method's Code is found. Only called
	 * if doIterateCode is set to true.
	 * 
	 * @param cDef
	 *            the class definition where the code is defined
	 * @param method
	 *            the method where the code is defined.
	 * @param mId
	 *            the associated MethodId
	 * @param pId
	 *            the associated ProtoId.
	 * @param address
	 *            the address in the code block of the instruction
	 * @param instruction
	 *            the instruction that was found.
	 */
	public void onInstructionFound(ClassDef cDef, Method method, MethodId mId,
			ProtoId pId, int address, DecodedInstruction instruction) {

		throw new UnsupportedOperationException(
				"this method must be overridden if doIterateCode returns true.");

	}

	public void onCodeVisited(DexBuffer buffer, ClassDef cDef, Method method,
			MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

		throw new UnsupportedOperationException(
				"this method must be overridden if shouldVisit(ClassDef, Method, MethodId, ProtoId, Code) returns true.");

	}

	/**
	 * Called by DexBufferListener.visitMethodIdentifiers to determine whether
	 * or not a method should be visited.
	 * 
	 * @param methodId
	 *            the method Id to check
	 * @return true if the visit should happen or false otherwise.
	 */
	public boolean shouldVisit(MethodId methodId) {
		return false;
	}

	public void onMethodIdFound(MethodId methodId) {
		throw new UnsupportedOperationException(
				"this method must be overridden if shouldVisit(MethodId) returns true.");

	}

}
