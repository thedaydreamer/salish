package schilling.richard.dalvik.vm.analysis;

import java.util.Stack;

import schilling.richard.dalvik.vm.BitVector;
import android.util.SparseArray;

/**
 * During verification, we associate one of these with every "interesting"
 * instruction. We track the status of all registers, and (if the method has any
 * monitor-enter instructions) maintain a stack of entered monitors (identified
 * by code unit offset).
 * 
 * <p>
 * If live-precise register maps are enabled, the "liveRegs" vector will be
 * populated. Unlike the other lists of registers here, we do not track the
 * liveness of the method result register (which is not visible to the GC).
 */
public class RegisterLine implements Cloneable, Comparable<RegisterLine> {

	@Override
	protected Object clone() throws CloneNotSupportedException {

		RegisterLine result = new RegisterLine();
		result.monitorEntries = (BitVector) this.monitorEntries.clone();
		result.monitorStack = (Stack<BitVector>) this.monitorStack.clone();
		result.liveRegs = (BitVector) this.liveRegs.clone();
		return result;

	}

	public SparseArray<RegType> regTypes = new SparseArray<RegType>();

	/**
	 * A bit vector indicating which entries in the monitor stack are associated
	 * with this register. The low bit corresponds to the stack's bottom-most
	 * entry.
	 */
	public BitVector monitorEntries = new BitVector();

	Stack<BitVector> monitorStack = new Stack<BitVector>();

	public BitVector liveRegs = new BitVector();

	@Override
	public int compareTo(RegisterLine another) {

		int result;

		result = this.monitorEntries.compareTo(another.monitorEntries);

		if (result != 0)
			return result;

		result = this.monitorStack.size() - another.monitorStack.size();
		if (result != 0)
			return result;

		result = this.monitorStack.peek()
				.compareTo(another.monitorStack.peek());
		if (result != 0)
			return result;

		int sz = this.monitorStack.size();

		for (int i = 0; i < sz; i++) {
			result = this.monitorStack.get(i).compareTo(
					another.monitorStack.get(i));
			if (result != 0)
				return result;

		}
		result = this.liveRegs.compareTo(another.liveRegs);
		if (result != 0)
			return result;

		return 0;
	}

}
