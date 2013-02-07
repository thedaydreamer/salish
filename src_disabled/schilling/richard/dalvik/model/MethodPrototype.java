
package schilling.richard.dalvik.model;

import java.util.Collections;
import java.util.List;

import com.android.dx.gen.Type;

public class MethodPrototype implements Comparable<MethodPrototype> {

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
     * All parameters.
     */
    private List<String> parameters = null;

    public MethodPrototype(DexModel model, String shorty, String returnType) {
        if (model == null)
            throw new IllegalArgumentException("model cannot be null");

        if (shorty == null)
            throw new IllegalArgumentException("shorty cannot be null");

        if (returnType == null)
            throw new IllegalArgumentException("returnType cannot be null");

        this.model = model;
        this.shortyString = shorty.intern();
        this.returnType = returnType.intern();

    }

    public String getShorty() {
        return shortyString;
    }

    public String getReturnType() {
        return returnType;
    }

    /* protected */void setParameters(List<String> parameters) {

        this.parameters = parameters;

    }

    @Override
    public String toString() {
        return returnType + " " + getParameterString();

    }

    public boolean equals(Object compareTo) {
        if (!(compareTo instanceof MethodPrototype))
            return false;

        MethodPrototype mp = (MethodPrototype) compareTo;

        if (this.returnType == null && mp.returnType != null)
            return false;
        if (this.returnType != null && mp.returnType == null)
            return false;

        if (this.shortyString == null && mp.shortyString != null)
            return false;
        if (this.shortyString != null && mp.shortyString == null)
            return false;

        if (this.parameters == null && mp.parameters != null)
            return false;
        if (this.parameters != null && mp.parameters == null)
            return false;

        if (!returnType.equals(mp.returnType))
            return false;

        if (!shortyString.equals(mp.shortyString))
            return false;

        // all members are either null or not null for this and mp
        if (parameters.size() != mp.parameters.size())
            return false;
        for (int i = 0; i < parameters.size(); i++) {
            if (!parameters.get(i).equals(mp.parameters.get(i)))
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

        return this.getParameterString().compareTo(another.getParameterString());

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
