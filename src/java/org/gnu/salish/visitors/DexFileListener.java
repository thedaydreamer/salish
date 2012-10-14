package org.gnu.salish.visitors;

import schilling.richard.dalvik.vm.InstructionList;

import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Marker interface for any class that is a DEX file listener.
 * 
 * @author rschilling
 * 
 */
public abstract class DexFileListener {
	/**
	 * Called whenever a ClassDef is found in a DEX file.
	 * 
	 * @param cDef
	 *            the class definition that was found.
	 */
	public abstract void onClassDefFound(ClassDef cDef);

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
	public abstract boolean shouldVisit(ClassDef cDef);

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
	public abstract boolean shouldVisit(ClassDef cDef, Method method,
			MethodId mId, ProtoId pId);

	public abstract boolean shouldVisit(ClassDef cDef, Method method,
			MethodId mId, ProtoId pId, Code code);

	/**
	 * Called whenever a new method in the DEX file is found.
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
	public abstract void onMethodFound(ClassDef cDef, Method method,
			MethodId mId, ProtoId pId);

	/**
	 * If true, the DexBufferVisitor will iterate through each instruction in
	 * the code immediately after calling onCodeFound. The method
	 * onInstructionFound is called for each instruction in this case.
	 */
	public abstract boolean doIterateCode();

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
	public abstract void onCodeFound(DexBuffer buffer, ClassDef cDef,
			Method method, MethodId mId, ProtoId pId, Code methodCode,
			InstructionList iList);

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
	public abstract void onInstructionFound(ClassDef cDef, Method method,
			MethodId mId, ProtoId pId, int address,
			DecodedInstruction instruction);

	public abstract void onCodeVisited(DexBuffer buffer, ClassDef cDef,
			Method method, MethodId mId, ProtoId pId, Code methodCode,
			InstructionList iList);
}
