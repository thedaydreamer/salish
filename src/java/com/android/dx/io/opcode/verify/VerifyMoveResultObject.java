package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.analysis.RegisterCategory.kTypeCategoryRef;

/**
 * The move-result instructions copy data out of a "pseudo-register" with the
 * results from the last method invocation. In practice we might want to hold
 * the result in an actual CPU register, so the Dalvik spec requires that these
 * only appear immediately after an invoke or filled-new-array.
 * 
 * These calls invalidate the "result" register. (This is now redundant with the
 * reset done below, but it can make the debug info easier to read in some
 * cases.)
 */
public class VerifyMoveResultObject extends VerifyMoveResult {

	public VerifyMoveResultObject() {
		categoryToCheck = kTypeCategoryRef;
	}

}
