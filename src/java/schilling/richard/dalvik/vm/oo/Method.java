package schilling.richard.dalvik.vm.oo;

import static com.android.dx.rop.code.AccessFlags.ACC_ABSTRACT;
import static com.android.dx.rop.code.AccessFlags.ACC_DECLARED_SYNCHRONIZED;
import static com.android.dx.rop.code.AccessFlags.ACC_FINAL;
import static com.android.dx.rop.code.AccessFlags.ACC_MIRANDA;
import static com.android.dx.rop.code.AccessFlags.ACC_NATIVE;
import static com.android.dx.rop.code.AccessFlags.ACC_PRIVATE;
import static com.android.dx.rop.code.AccessFlags.ACC_PUBLIC;
import static com.android.dx.rop.code.AccessFlags.ACC_STATIC;
import static com.android.dx.rop.code.AccessFlags.ACC_SYNCHRONIZED;
import static com.android.dx.rop.code.AccessFlags.ACC_SYNTHETIC;

import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.RegisterTable;

import com.android.dx.io.ClassData;
import com.android.dx.io.Code;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.Code.Try;
import com.android.dx.io.MethodId;

/**
 * An in-memory, non buffered version of ClassData.Method.
 * 
 * @author rschilling
 * 
 */
public class Method implements Comparable<Method> {
	/**
	 * The model that this method belongs to.
	 */
	private final DexModel model;

	/********* DEX File related structures *************/

	/**
	 * The method identifier as read form the DEX file.
	 */
	private final MethodId methodId;

	/**
	 * The code as read from the DEX file.
	 */
	private final Code methodCode;

	/**
	 * The method as it was encoded from the underlying DexFile.
	 */
	private final ClassData.Method classDataMethod;

	/********
	 * Model related structures - in memory equivalets to DEX file related
	 * structures
	 **************/
	/**
	 * The class that declared this method.
	 */
	private final Clazz declaringClass;

	/**
	 * The prototype lookup information as read from the DEX file.
	 */
	private final MethodPrototype prototype;

	/**
	 * Annotation set on the method itself.
	 */
	private AnnotationSet annotations;

	/**
	 * Annotations set on the method parameters.
	 */
	private List<AnnotationSet> paramAnnotations;

	/**
	 * Shorty string.
	 */
	public String shorty;

	/********* structures used in method verification **********/
	public RegisterTable registerTable;

	/********** local members extracted from DEX file related structures *********/

	/**
	 * The name of the method.
	 */
	private String methodName;

	/**
	 * The method's instructions
	 */
	private InstructionList instructionList;

	public static final int METHOD_EXTRA_LINES = 2;
	public static final int kExtraRegs = 2;

	/**
	 * All Method objects are created from their Dex file counterparts, so
	 * MethodId, ClassData.Method and MethodPrototype are required.
	 * 
	 * @param declaringClass
	 *            the declaring class
	 * @param methodId
	 *            the method id from the dex file that is associated with this
	 *            method
	 * @param method
	 *            the method data from the dex file that is associated with this
	 *            method.
	 * @param prototype
	 *            the method prototype, which was created from a ProtoId object
	 *            in the Dex file.
	 */
	public Method(DexModel model, Clazz declaringClass,
			MethodPrototype prototype, MethodId methodId,
			ClassData.Method cdMethod, Code methodCode) {

		if (model == null)
			throw new IllegalArgumentException("model cannot be null");

		if (declaringClass == null)
			throw new IllegalArgumentException("declaringClass cannot be null");

		if (prototype == null)
			throw new IllegalArgumentException("prototype cannot be null.");

		if (methodId == null)
			throw new IllegalArgumentException("methodId cannot be null.");

		if (cdMethod == null)
			throw new IllegalArgumentException("cdMethod cannot be null.");

		if (cdMethod.getCodeOffset() > 0 && methodCode == null)
			throw new IllegalArgumentException(
					"methodCode cannot be null when the code offset is > 0.");

		this.model = model;

		this.methodId = methodId;
		this.methodCode = methodCode;
		this.classDataMethod = cdMethod;

		this.declaringClass = declaringClass;
		this.prototype = prototype;
		this.annotations = new AnnotationSet();
		this.paramAnnotations = new LinkedList<AnnotationSet>();

		// check for the method name
		try {
			methodName = model.stringPool().get(methodId.getNameIndex());

			if (methodName == null)
				throw new IllegalStateException(
						"the string pool does not contain the method name");

		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalStateException(
					"the string pool does not contain the method name.", ex);
		}

		// make sure that the related object are already loaded into the model
		if (!this.model.isInClassPool(this.declaringClass))
			throw new IllegalArgumentException(
					"the declaring class must be added to the model first.");

		if (!this.model.isInPrototypePool(this.prototype))
			throw new IllegalArgumentException(
					"the prototype pool does not contain the specified prototype");
	}

	public int getAccessFlags() {
		return classDataMethod.getAccessFlags();
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

	public Clazz getDeclaringClass() {
		return declaringClass;
	}

	public MethodPrototype getPrototype() {
		return prototype;
	}

	@Override
	public int compareTo(Method another) {

		// order by defining type, name and then prototype
		if (declaringClass != null && another.declaringClass != null) {
			if (declaringClass.getSignature().equals(
					another.getDeclaringClass().getSignature()))
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

	public String getUniqueName() {
		StringBuilder result = new StringBuilder();
		result.append(Integer.toString(getAccessFlags())).append(" ");
		result.append(declaringClass.getSignature()).append(" ");
		result.append(methodName).append(" ");
		result.append(shorty);

		return result.toString();
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

	public InstructionList rebuildInstructionList() {
		instructionList = null;
		return instructionList();
	}

	/**
	 * Returns the instruction list as a sparse array of DecodedInstructions.
	 * 
	 * @return a sparse array with all the instructions decoded.
	 */
	public InstructionList instructionList() {
		if (instructionList == null) {

			instructionList = InstructionList.allocate(instructions());
		}

		return instructionList;

	}

	public RegisterTable registerTable() {
		if (registerTable == null)
			registerTable = new RegisterTable();

		return registerTable;
	}

	/*
	 * Get the associated code struct for a method. This returns NULL for
	 * non-bytecode methods.
	 */
	public Code dvmGetMethodCode() {
		if (dvmIsBytecodeMethod()) {
			/*
			 * The insns field for a bytecode method actually points at
			 * &(DexCode.insns), so we can subtract back to get at the DexCode
			 * in front.
			 */
			return methodCode;
		} else {
			return null;
		}
	}

	/*
	 * Get whether the given method has associated bytecode. This is the case
	 * for methods which are neither native nor abstract.
	 */
	public boolean dvmIsBytecodeMethod() {
		return (getAccessFlags() & (ACC_NATIVE | ACC_ABSTRACT)) == 0;
	}

	public short[] instructions() {
		return methodCode.getInstructions();
	}

	public int getInsSize() {
		return methodCode.getInsSize();
	}

	public int getOutsSize() {
		return methodCode.getOutsSize();
	}

	public CatchHandler[] getCatchHandlers() {
		return methodCode.getCatchHandlers();
	}

	public Try[] getTries() {
		return methodCode.getTries();
	}

	public int getRegistersSize() {
		return methodCode.getRegistersSize();
	}

	public int getCodeOffset() {
		return methodCode.getOffset();
	}

	public Code getCode() {
		return methodCode;
	}

	public List<String> getParameters() {
		return prototype.getParameters();
	}

	public DexModel getModel() {
		return model;
	}

	public String getReturnType() {
		return prototype.getReturnType();
	}

	public String getShorty() {
		return prototype.getShorty();
	}

	public int insnsSize() {
		return instructions().length;
	}

	public boolean IS_METHOD_FLAG_SET() {
		return (getAccessFlags() & METHOD_ISWRITABLE) != 0 ? true : false;
	}

	/*
	 * Is this method a constructor?
	 */
	public boolean isInitMethod() {
		throw new UnsupportedOperationException("Use MethodId.isInitMethod");
	}

	public boolean dvmIsStaticMethod() {
		return (getAccessFlags() & ACC_STATIC) != 0;
	}

	public boolean dvmIsPublicMethod() {
		return (getAccessFlags() & ACC_PUBLIC) != 0;
	}

	public boolean dvmIsPrivateMethod() {
		return (getAccessFlags() & ACC_PRIVATE) != 0;
	}

	public boolean dvmIsSynchronizedMethod() {
		return (getAccessFlags() & ACC_SYNCHRONIZED) != 0;
	}

	public boolean dvmIsDeclaredSynchronizedMethod() {
		return (getAccessFlags() & ACC_DECLARED_SYNCHRONIZED) != 0;
	}

	public boolean dvmIsFinalMethod() {
		return (getAccessFlags() & ACC_FINAL) != 0;
	}

	public boolean dvmIsNativeMethod() {
		return (getAccessFlags() & ACC_NATIVE) != 0;
	}

	public boolean dvmIsAbstractMethod() {
		return (getAccessFlags() & ACC_ABSTRACT) != 0;
	}

	public boolean dvmIsSyntheticMethod() {
		return (getAccessFlags() & ACC_SYNTHETIC) != 0;
	}

	public boolean dvmIsMirandaMethod() {
		return (getAccessFlags() & ACC_MIRANDA) != 0;
	}

	// FIXME implement
	public boolean dvmIsConstructorMethod() {
		return getMethodName().startsWith("<");
	}

	/* Dalvik puts private, static, and constructors into non-virtual table */
	public boolean dvmIsDirectMethod() {
		return dvmIsPrivateMethod() || dvmIsStaticMethod()
				|| dvmIsConstructorMethod();
	}

	public static final int METHOD_ISWRITABLE = (1 << 31);

}
