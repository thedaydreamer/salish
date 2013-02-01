package schilling.richard.dalvik.vm.analysis;

import android.util.SparseArray;

/**
 * The register table for this instruction list. This is kept for naming
 * compatability with the original C code.
 * 
 * <p>
 * TODO: remove this class and put members into CodeVerify.verifierData.
 * 
 * @author rschilling
 */
public class RegisterTable {

	public SparseArray<RegisterLine> registerLines = new SparseArray<RegisterLine>();

	/**
	 * Number of registers we track for each instruction. This is equal to the
	 * method's declared "registersSize" plus kExtraRegs.
	 */
	public int insnRegCountPlus;

	/**
	 * Storage for a register line we're currently working on.
	 */
	public RegisterLine workLine;

	/**
	 * Storage for a register line we're saving for later.
	 */
	public RegisterLine savedLine;

}
