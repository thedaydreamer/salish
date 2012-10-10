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

import com.android.dx.io.ClassDef;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.ClassData.Method;

/**
 * A listener that can be registered with {@link DexBufferVisitor
 * DexBufferVisitor} which will be called whenever a method is found in a DEX
 * file. {@code onMethodFound} is called when a method in the dex file if both
 * {@code shouldVisit(ClassDef, Method, MethodId, ProtoId)} and
 * {@code shouldVisit(ClassDef)} return true.
 * 
 * @author rschilling
 * @since 1.0
 * 
 */
public abstract class MethodListener {
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

	/**
	 * Called whenever a new class is found so that it can be determined whether
	 * or not the class should be skipped. If this method returns false, then
	 * the class will be skipped for this listener entirely.
	 * 
	 * @param cDef
	 *            the class definition to check.
	 * @return true if the class should be processed.
	 * @since 1.0
	 */
	public abstract boolean shouldVisit(ClassDef cDef);

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

}
