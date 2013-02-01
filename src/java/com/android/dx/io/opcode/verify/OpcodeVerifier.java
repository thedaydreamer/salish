package com.android.dx.io.opcode.verify;

import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Used by OpcodeInfo.Info objects. Reads information about a DecodedInstruction
 * and verifies that the instruction passes specific tests.
 * 
 */
public interface OpcodeVerifier {

	public static final boolean VALIDATE_EXTRA = false;

	/**
	 * If true warnings will be issued for invocation functions as opposed to
	 * throwing exceptions.
	 */
	public static final boolean WARN_INVOCATION_THIS = true;
	public static final boolean WARN_VERIFY = true;
	
	public static final String LOG_TAG = "OpcodeVerifier";

	/**
	 * Verifies that the instruction is used properly given the known register
	 * types of all the other instructions in the function.
	 * 
	 * @param instruction
	 *            the instruction to verify.
	 * @param registerTypes
	 *            the register types of the instructions from the rest of the
	 *            function.
	 * @throws VerifyException
	 *             if verification fails.F
	 */
	public void verify(DecodedInstruction instruction,
			RegisterTypeSparseArray registerTypes) throws VerifyException;

}
