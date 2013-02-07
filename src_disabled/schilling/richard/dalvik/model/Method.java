
package schilling.richard.dalvik.model;

import java.util.List;

import static com.android.dx.rop.code.AccessFlags.*;
import com.android.dx.io.Code;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;

public class Method implements Comparable<Method> {

    /**
     * The class that declared this method.
     */
    private Clazz declaringClass;

    /**
     * The access flags for this method.
     */
    public int accessFlags;

    /**
     * The name of the method.
     */
    private MethodPrototype prototype = null;

    private Code code;
    
    

    private String methodName;

    private AnnotationSet annotations;

    private List<AnnotationSet> paramAnnotations;

    /*
     * For concrete virtual methods, this is the offset of the method
     * in "vtable".
     *
     * For abstract methods in an interface class, this is the offset
     * of the method in "iftable[n]->methodIndexArray".
     */
    short             methodIndex;

    /*
     * Method bounds; not needed for an abstract method.
     *
     * For a native method, we compute the size of the argument list, and
     * set "insSize" and "registerSize" equal to it.
     */
    public short              registersSize;  /* ins + locals */
    short              outsSize;
    short              insSize;
    
    public static final int METHOD_EXTRA_LINES = 2;
    public static final int kExtraRegs = 2;
    
    
    // FIXME there are TWO RegisterLine sparse arrays: one in VerifierData and another in in RegisterTabl.
    public RegisterTable registerTable;

    
    /**
     * Shorty string.  TODO make private
     */
    public String	shorty;

    
    public Method(Clazz declaringClass, String name, MethodPrototype prototype) {

        if (declaringClass == null)
            throw new IllegalArgumentException("declaringClass cannot be null");

        if (name == null)
            throw new IllegalArgumentException("name cannot be null");

        if (prototype == null)
            throw new IllegalArgumentException("prototype cannot be null");

        this.declaringClass = declaringClass;
        this.prototype = prototype;
        this.methodName = name;
    }

    /* package */void setAnnotations(AnnotationSet ann) {
        annotations = ann;
    }

    public AnnotationSet getAnnotations() {
        return annotations;
    }

    /* package */void setParameterAnnotations(List<AnnotationSet> ann) {
        paramAnnotations = ann;
    }

    public List<AnnotationSet> getParameterAnnotations() {
        return paramAnnotations;
    }

    /* package */void setDeclaringClass(Clazz c) {
        declaringClass = c;
    }

    /* package */void setCode(Code c) {

        this.code = c;

    }

    public Clazz getDeclaringClass() {
        return declaringClass;
    }

    public Code getCode() {
        return code;
    }

    public MethodPrototype getPrototype() {
        return prototype;
    }

    @Override
    public int compareTo(Method another) {

        // order by defining type, name and then prototype
        if (declaringClass != null && another.declaringClass != null) {
            if (declaringClass.getSignature().equals(another.getDeclaringClass().getSignature()))
                return declaringClass.getSignature().compareTo(
                        another.getDeclaringClass().getSignature());
        }

        if (!methodName.equals(another.methodName))
            return methodName.compareTo(another.methodName);

        if (prototype != another.prototype)
            return prototype.compareTo(another.prototype);

        return 0;

    }

    public String getMethodName() {
        return methodName;
    }

    /**
     * Compares Method objects. Does not compare access flags.
     */
    public boolean equals(Object another) {

        if (!(another instanceof Method))
            return false;

        // check defining type, name and prototype.

        Method anotherMethod = (Method) another;

        // declaring class may or may not be set.
        if (declaringClass != null && anotherMethod.declaringClass != null)
            if (!declaringClass.equals(anotherMethod.declaringClass))
                return false;

        if (!methodName.equals(anotherMethod.methodName))
            return false;

        if (!prototype.equals(anotherMethod.prototype))
            return false;

        return true;

    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        if (declaringClass != null) {
            result.append(" declared in ");
            result.append(declaringClass.getSignature());
        }
        return result.toString();
    }

    public CstMethodRef asCstMethodRef() {

        CstType definingClassType = new CstType(Type.intern(getDeclaringClass().getSignature()));
        String methodName = getMethodName();
        String methodDescriptor = getPrototype().descriptor();
        CstNat methodNat = new CstNat(new CstString(methodName), new CstString(methodDescriptor));

        return new CstMethodRef(definingClassType, methodNat);
    }
    
    private InstructionList instructionList;
    
    public InstructionList rebuildInstructionList(){
    	instructionList = null;
    	return instructionList();
    }
    
    public InstructionList instructionList(){
    	if (instructionList == null){
    		instructionList = InstructionList.allocate(getCode()
        			.getInstructions());
    	}
        
        return instructionList;

    }
    
    
    /*
	 * Get the associated code struct for a method. This returns NULL for
	 * non-bytecode methods.
	 */
	public static Code dvmGetMethodCode(Method meth) {
	    if (dvmIsBytecodeMethod(meth)) {
	        /*
	         * The insns field for a bytecode method actually points at
	         * &(DexCode.insns), so we can subtract back to get at the
	         * DexCode in front.
	         */
	        return meth.getCode();
	    } else {
	        return null;
	    }
	}

	/*
	 * Get whether the given method has associated bytecode. This is the case
	 * for methods which are neither native nor abstract.
	 */
	public static boolean dvmIsBytecodeMethod(Method method) {
		return (method.accessFlags & (ACC_NATIVE | ACC_ABSTRACT)) == 0;
	}




}
