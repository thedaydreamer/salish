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

import schilling.richard.dalvik.vm.InstructionList;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.instructions.DecodedInstruction;

public abstract class MethodCodeListener {

	/**
	 * If true, the DexBufferVisitor will iterate through each instruction in
	 * the code immediately after calling onCodeFound. The method
	 * onInstructionFound is called for each instruction in this case.
	 */
	public boolean doIterateCode = false;

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
