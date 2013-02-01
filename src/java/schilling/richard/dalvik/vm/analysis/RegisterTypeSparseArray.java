package schilling.richard.dalvik.vm.analysis;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;

import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.merge.MethodDefinition;

public class RegisterTypeSparseArray extends SparseArray<RegisterType> {


	public static final String LOG_TAG = "RegisterTypeSparseArray";

	/**
	 * This sparse array parallels the underlying array and keeps track of the
	 * read and write history of the array.
	 */
	private SparseArray<List<String>> history = new SparseArray<List<String>>();

	/**
	 * This is the pseudo register used by invoke instructions. Since invoke
	 * instructions don't store any data in a regular register, we place the
	 * register type here. In the C code, the position off the end of the
	 * registerType array is used but we can't do that in our implementation
	 * since we rely on the length of the sparse array.
	 */
	private RegisterType resultRegisterLow;
	private RegisterType resultRegisterHigh;

	private List<String> lowHistory = new LinkedList<String>();
	private List<String> highHistory = new LinkedList<String>();
	
	public void setResultRegisterTypeHigh(RegisterType type,
			DecodedInstruction writer) {
		resultRegisterHigh = type;
		if (highHistory == null)
			highHistory = new LinkedList<String>();

		StringBuilder builder = new StringBuilder();
		if (writer != null)
			builder.append(writer.toShortString());

		builder.append("w");
		highHistory.add(builder.toString());

	}

	public void setResultRegisterTypeLow(RegisterType type,
			DecodedInstruction writer) {
		resultRegisterLow = type;
		if (lowHistory == null)
			lowHistory = new LinkedList<String>();

		StringBuilder builder = new StringBuilder();
		if (writer != null)
			builder.append(writer.toShortString());

		builder.append("w");
		lowHistory.add(builder.toString());

	}

	public RegisterType getResultRegisterTypeHigh(DecodedInstruction reader) {

		if (highHistory == null)
			highHistory = new LinkedList<String>();

		StringBuilder builder = new StringBuilder();
		if (reader != null)
			builder.append(reader.toShortString());

		builder.append("r");
		highHistory.add(builder.toString());

		return resultRegisterHigh;

	}

	public RegisterType getResultRegisterTypeLow(DecodedInstruction reader) {

		if (lowHistory == null)
			lowHistory = new LinkedList<String>();

		StringBuilder builder = new StringBuilder();
		if (reader != null)
			builder.append(reader.toShortString());

		builder.append("r");
		lowHistory.add(builder.toString());

		return resultRegisterLow;

	}

	/**
	 * If there is a RegisterType at the specified key then the type is updated.
	 * If not, then the type is added.
	 * 
	 * @param key
	 *            the position to add/replace the register type.
	 * @param value
	 *            the value to udpate the array with.
	 */
	public void replace(int key, RegisterType value) {
		RegisterType cur = get(key);
		if (cur != null)
			cur.setTypeEnum(value.typeEnum());
		else
			put(key, value);
	}

	public RegisterType get(int key, DecodedInstruction instruction) {
		RegisterType result = super.get(key);
		if (result == null)
			return result;

		List<String> out = history.get(key);
		if (out == null) {
			out = new LinkedList<String>();
			history.put(key, out);
		}
		StringBuilder builder = new StringBuilder();
		if (instruction != null)
			builder.append(instruction.toShortString());

		builder.append("r");
		out.add(builder.toString());

		return result;
	}

	public void dumpHistory() {

		StringBuilder out;
		for (int i = 0; i < history.size(); i++) {
			out = new StringBuilder();
			int key = keyAt(i);
			List<String> h = history.valueAt(i);

			// TODO use String.format()
			out.append("v").append(Integer.toString(key)).append(": ");

			for (int j = 0; j < h.size(); j++) {
				out.append(h.get(j));
				if (j < h.size() - 1)
					out.append(", ");
			}
			if (MethodDefinition.LOG_INSTRUCTIONS)
				Log.d(LOG_TAG, out.toString());
		}

		if (MethodDefinition.LOG_INSTRUCTIONS) {
			out = new StringBuilder();
			out.append("vRh");

			if (highHistory == null) {
				out.append("N/A");
			} else {
				for (int j = 0; j < highHistory.size(); j++) {
					out.append(highHistory.get(j));
					if (j < highHistory.size() - 1)
						out.append(", ");
				}
			}

			out.append("\n");
			out.append("vRl");

			if (lowHistory == null) {
				out.append("N/A");
			} else {
				for (int j = 0; j < lowHistory.size(); j++) {
					out.append(lowHistory.get(j));
					if (j < lowHistory.size() - 1)
						out.append(", ");
				}
			}

			Log.d(LOG_TAG, out.toString());

		}

	}

	/**
	 * Puts a register type into the sparse array.
	 * 
	 * @param key
	 *            the register reference
	 * @param value
	 *            the value to set
	 * @param address
	 *            the address of the instruction performing the operation (if
	 *            -1, then the register was set by the signature)
	 * @param vSrc
	 *            if the put is part of a register move operation vSrc specifies
	 *            the source register.
	 */

	public void put(int key, RegisterType value,
			DecodedInstruction instruction, int vSrc) {
		put(key, value, instruction, Integer.toString(vSrc));

	}

	public void put(int key, RegisterType value,
			DecodedInstruction instruction, String vSrc) {
		put(key, value);

		List<String> out = history.get(key);
		if (out == null) {
			out = new LinkedList<String>();
			history.put(key, out);
		}

		StringBuilder builder = new StringBuilder();
		if (instruction != null)
			builder.append(instruction.toShortString());

		if (value == null) {
			builder.append(String.format("NULL"));
		} else {
			builder.append("w");
		}

		if (vSrc != null) {
			builder.append("<-");
			builder.append(vSrc.toString());
		}
		out.add(builder.toString());

	}

	@Override
	public void put(int key, RegisterType value) {
		if (value == null) {

			super.put(key, value);

		} else {

			RegisterType cur = get(key);
			if (cur != null) {
				cur.copyValuesFrom(value);
			} else {
				super.put(key, value);
			}
		}

	}



}
