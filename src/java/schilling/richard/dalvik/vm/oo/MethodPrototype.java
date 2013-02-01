package schilling.richard.dalvik.vm.oo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.android.dx.io.ProtoId;

public class MethodPrototype implements Comparable<MethodPrototype> {

	/**
	 * The model that this prototype belongs to. Not used in the present
	 * implementation but kept for future use.
	 */
	private DexModel model;

	/**
	 * shortyString is the short form representation of a method prototype,
	 * including return and parameter types.
	 */
	private String shortyString = null;

	/**
	 * The signature of the return type.
	 */
	private String returnType = null;

	/**
	 * The prototype parameters.
	 */
	private List<String> parameters;

	private final ProtoId prototypeId;

	public MethodPrototype(DexModel model, ProtoId pId) {

		if (model == null)
			throw new IllegalArgumentException("model cannot be null");

		if (pId == null)
			throw new IllegalArgumentException("proto ID item cannot be null");

		prototypeId = pId;

		this.model = model;

		shortyString = prototypeId.getShortyString().intern();
		if (!model.isInStringPool(shortyString))
			throw new IllegalArgumentException("shorty string " + shortyString
					+ " is not in the string pool");

		returnType = prototypeId.getReturnTypeString().intern();
		if (!model.isInTypePool(returnType))
			throw new IllegalArgumentException("type string " + returnType
					+ " is not in the type pool.");

		setParameters(pId.getParameters());

	}

	public String getShorty() {
		return shortyString;
	}

	public String getReturnType() {
		return returnType;
	}

	private void setParameters(List<String> params) {

		if (params == null) {
			parameters = null;
			return;
		}
		parameters = new LinkedList<String>();

		for (String str : params) {

			if (!model.isInTypePool(str))
				throw new IllegalArgumentException("parameter type string "
						+ returnType + " is not in the type pool");

			parameters.add(str.intern());
		}

	}

	@Override
	public String toString() {
		return returnType + " " + getParameterString();

	}

	public boolean equals(Object compareTo) {
		if (!(compareTo instanceof MethodPrototype))
			return false;

		MethodPrototype mp = (MethodPrototype) compareTo;

		// The constructor makes return type, shorty and parameters non-null.

		if (!returnType.equals(mp.returnType))
			return false;

		if (!shortyString.equals(mp.shortyString))
			return false;

		if (parameters.size() != mp.parameters.size())
			return false;

		for (String str : parameters) {
			if (!mp.parameters.contains(str))
				return false;
		}

		return true;

	}

	@Override
	public int compareTo(MethodPrototype another) {
		if (this.equals(another))
			return 0;

		// sort by return type and then by shorty
		int typeResult = this.returnType.compareTo(another.returnType);
		if (typeResult != 0)
			return typeResult;

		int shortyResult = this.shortyString.compareTo(another.shortyString);
		if (shortyResult != 0)
			return shortyResult;

		return this.getParameterString()
				.compareTo(another.getParameterString());

	}

	public String getParameterString() {
		StringBuilder result = new StringBuilder();

		result.append("(");

		if (parameters != null) {
			for (String s : parameters) {
				result.append(s);
			}
		}

		result.append(")");

		return result.toString();
	}

	public List<String> getParameters() {
		return parameters;
	}

	/**
	 * Returns a descriptor like "(Ljava/lang/Class;[I)Ljava/lang/Object;".
	 * Taken from com.android.dx.gen.MethodId.
	 */
	public String descriptor() {

		Collections.sort(parameters);

		StringBuilder result = new StringBuilder();
		result.append("(");

		for (String s : parameters) {
			result.append(s);
		}
		result.append(")");
		result.append(returnType);
		return result.toString();
	}

}
