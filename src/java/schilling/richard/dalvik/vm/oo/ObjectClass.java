package schilling.richard.dalvik.vm.oo;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.rop.code.AccessFlags;

/**
 * Classes that have Object as their ultimate parent.
 * 
 * @author rschilling
 * 
 */
public class ObjectClass extends Clazz {
	public static final String LOG_TAG = "ObjectClass";

	/**
	 * Data associated with this class.
	 */
	private ClassData classData;

	/**
	 * The superclass.
	 */
	private ObjectClass superclass;

	private final LinkedList<ObjectClass> interfaces = new LinkedList<ObjectClass>();
	private final LinkedList<Field> staticFields = new LinkedList<Field>();
	private final LinkedList<Field> instanceFields = new LinkedList<Field>();
	private final Hashtable<String, Method> directMethods = new Hashtable<String, Method>();
	private final Hashtable<String, Method> virtualMethods = new Hashtable<String, Method>();

	public ObjectClass(DexModel model, ClassDef cDef, int typeIdx) {
		super(model, cDef, typeIdx);
	}

	public Field findFieldByFieldIndex(int idx) throws VerifyException {
		for (Field f : staticFields) {
			if (f.getFieldIdx() == idx)
				return f;
		}

		for (Field f : instanceFields) {
			if (f.getFieldIdx() == idx)
				return f;
		}

		throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_ACCESS_FIELD,
				String.format("VFY: unable to resolve instance field %u", idx));
	}

	public Field findStaticFieldByFieldIndex(int idx) throws VerifyException {
		for (Field f : staticFields) {
			if (f.getFieldIdx() == idx)
				return f;
		}

		throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_ACCESS_FIELD,
				String.format("VFY: unable to resolve instance field %u", idx));
	}

	public boolean isInstanceFieldOf(Field f) {
		return instanceFields.contains(f);
	}

	/**
	 * Sets super class tree all the way up to java.lang.Object.
	 * 
	 * @throws VerifyException
	 */
	public void resolveSuperclass() throws VerifyException {

		/*
		 * TODO handle platform classes. All classes should be resolved, not
		 * just the ones in the dex file. The fact that a class in the pool is a
		 * PlatformClass will be a clear indicator that there is no code to
		 * write to the DexFile.
		 */

		// all types should have been loaded.
		if (!getModel().isInTypePool(getSuperclassSignature()))
			throw new IllegalStateException("the superclass type "
					+ getSuperclassSignature() + " is not in the type pool");

		Clazz superClazz = getModel().lookupClassByDescriptor(
				getSuperclassSignature());
		try {
			superclass = (ObjectClass) superClazz;
		} catch (ClassCastException ex) {
			throw new RuntimeException("superclass type in the class table is "
					+ superClazz.getSignature());
		}

		if (superclass == null)
			throw new IllegalStateException("the superclass "
					+ getSuperclassSignature() + " is not in the class pool");

		superclass.resolveSuperclass();

	}

	public void setData(DexBuffer buffer, ClassData cData) {
		if (cData == null)
			return;

		List<FieldId> fields = buffer.fieldIds;

		List<MethodId> methods = buffer.methodIds();
		List<ProtoId> protos = buffer.protoIds();

		List<Field> fieldPool = getModel().fieldPool();

		classData = cData;

		ClassData.Field[] sFields = classData.getStaticFields();
		if (sFields != null) {
			staticFields.clear();
			for (ClassData.Field sField : sFields) {

				FieldId fId = fields.get(sField.getFieldIndex());

				Field field = new Field(getModel(), this, sField, fId,
						sField.getFieldIndex());

				int pos = getModel().fieldPool().indexOf(field);
				if (pos == -1)
					throw new IllegalStateException("Field " + fId.getName()
							+ " is not in the field pool.");

				staticFields.add(fieldPool.get(pos));

			}

		}

		ClassData.Field[] iFields = classData.getInstanceFields();
		if (iFields != null) {
			instanceFields.clear();
			for (ClassData.Field iField : iFields) {
				FieldId fId = fields.get(iField.getFieldIndex());

				Field field = new Field(getModel(), this, iField, fId,
						iField.getFieldIndex());

				int pos = getModel().fieldPool().indexOf(field);
				if (pos == -1)
					throw new IllegalStateException("Field " + fId.getName()
							+ " is not in the field pool.");

				instanceFields.add(fieldPool.get(pos));

			}
		}

		ClassData.Method[] dMethods = classData.getDirectMethods();
		if (dMethods != null) {
			directMethods.clear();
			for (ClassData.Method dMethod : dMethods) {
				MethodId mId = methods.get(dMethod.getMethodIndex());
				ProtoId pId = protos.get(mId.getProtoIndex());

				MethodPrototype mp = new MethodPrototype(getModel(), pId);
				Code code = buffer.readCode(dMethod);
				Method method = new Method(getModel(), this, mp, mId, dMethod,
						code);
				int pos = getModel().methodPool().indexOf(method);
				if (pos == -1)
					throw new IllegalStateException("Method "
							+ method.getMethodName()
							+ " is not in the method pool");
				directMethods.put(method.getUniqueName(), method);
			}

		}

		ClassData.Method[] vMethods = classData.getVirtualMethods();
		if (vMethods != null) {
			virtualMethods.clear();
			for (ClassData.Method vMethod : vMethods) {
				MethodId mId = methods.get(vMethod.getMethodIndex());
				ProtoId pId = protos.get(mId.getProtoIndex());

				MethodPrototype mp = new MethodPrototype(getModel(), pId);
				Code code = buffer.readCode(vMethod);
				Method method = new Method(getModel(), this, mp, mId, vMethod,
						code);
				int pos = getModel().methodPool().indexOf(method);
				if (pos == -1)
					throw new IllegalStateException("Method "
							+ method.getMethodName()
							+ " is not in the method pool");
				virtualMethods.put(method.getUniqueName(), method);
			}

		}

	}

	public Hashtable<String, Method> getDirectMethods() {
		return directMethods;
	}

	public List<ObjectClass> interfaces() {
		Collections.sort(interfaces);
		return Collections.unmodifiableList(interfaces);
	}

	public List<Field> staticFields() {
		Collections.sort(staticFields);
		return Collections.unmodifiableList(staticFields);
	}

	public List<Field> instanceFields() {
		Collections.sort(instanceFields);
		return Collections.unmodifiableList(instanceFields);
	}

	public List<Method> directMethods() {
		Collection<Method> m = directMethods.values();
		List<Method> list = new LinkedList<Method>(m);
		return Collections.unmodifiableList(list);
	}

	public List<Method> virtualMethods() {
		Collection<Method> m = virtualMethods.values();
		List<Method> list = new LinkedList<Method>(m);
		return Collections.unmodifiableList(list);
	}

	public List<Field> allFields() {
		LinkedList<Field> result = new LinkedList<Field>();
		result.addAll(staticFields);
		result.addAll(instanceFields);
		Collections.sort(result);
		return Collections.unmodifiableList(result);

	}

	public List<Method> allMethods() {
		LinkedList<Method> result = new LinkedList<Method>();

		Collection<Method> mDirect = directMethods.values();
		Collection<Method> mVirtual = virtualMethods.values();

		result.addAll(mDirect);
		result.addAll(mVirtual);
		Collections.sort(result);
		return Collections.unmodifiableList(result);
	}

	/**
	 * return true if this class has an annotations directory
	 * 
	 * @return true if an annotations directory exists for this class.
	 */
	public boolean hasAnnotationDirectoryItems() {
		if (annotations != null && annotations.size() > 0)
			return true;

		for (Method m : allMethods()) {
			List<Annotation> annotations = m.getAnnotations();
			if (annotations != null && annotations.size() > 0)
				return true;
		}

		for (Field f : allFields()) {
			List<Annotation> annotations = f.getAnnotations();
			if (annotations != null && annotations.size() > 0)
				return true;
		}

		return false;

	}

	public List<Field> getAnnotatedFields() {
		List<Field> result = new LinkedList<Field>();
		for (Field f : allFields()) {
			List<Annotation> annotations = f.getAnnotations();
			if (annotations == null || annotations.size() == 0)
				continue;
			result.add(f);
		}
		return result;
	}

	/**
	 * Return methods with annotations.
	 * 
	 * @return
	 */
	public List<Method> getAnnotatedMethods() {
		List<Method> result = new LinkedList<Method>();
		for (Method f : allMethods()) {

			List<Annotation> annotations = f.getAnnotations();
			if (annotations == null || annotations.size() == 0)
				continue;
			result.add(f);

		}
		return result;
	}

	/**
	 * Return methods with parameter annotations.
	 * 
	 * @return
	 */
	public List<Method> getAnnotatedParameterMethods() {
		List<Method> result = new LinkedList<Method>();
		for (Method f : allMethods()) {

			List<AnnotationSet> parameterAnnotations = f
					.getParameterAnnotations();
			if (parameterAnnotations == null
					|| parameterAnnotations.size() == 0)
				continue;

			result.add(f);

		}
		return result;
	}

	/* package */void addInterface(ObjectClass c) {

		if (interfaces.contains(c))
			throw new IllegalArgumentException(
					"interface already added to interface table.");

		if (!getModel().isInTypePool(c.getSignature()))
			throw new IllegalArgumentException(
					"the signature is not found in the type pool");

		if (!getModel().isInClassPool(c))
			throw new IllegalArgumentException(
					"the signature is not found in the class pool");

		interfaces.add(c);

	}

	/* package */void addStaticField(Field f) {
		if (f == null)
			throw new IllegalArgumentException(
					"the specified field cannot be null.");

		if (!staticFields.contains(f))
			staticFields.add(f);

		Collections.sort(staticFields);
	}

	/**
	 * Adds all fields from a list to the static field list held by this class.
	 * Each field's parent is set to this class and the static fields list is
	 * sorted if there are additions. Causes a sort operation to happen on the
	 * static field list.
	 * 
	 * @param fields
	 *            the fields to add to the static field list.
	 */
	/* package */void addStaticFields(List<Field> fields) {
		if (fields == null)
			throw new IllegalArgumentException("fields cannot be null");

		if (fields.size() == 0)
			return;

		for (Field f : fields) {
			if (!staticFields.contains(f))
				staticFields.add(f);
		}

		Collections.sort(staticFields);
	}

	/* package */void addInstanceField(Field f) {
		if (f == null)
			throw new IllegalArgumentException(
					"the specified field cannot be null.");

		if (!instanceFields.contains(f))
			instanceFields.add(f);

		Collections.sort(instanceFields);
	}

	/**
	 * Adds all fields from a list to the instance field list held by this
	 * class. Each field's parent is set to this class and the instance fields
	 * list is sorted if there are additions. Causes a sort operation to happen
	 * on the instance field list.
	 * 
	 * @param fields
	 *            the fields to add to the instance field list.
	 */
	/* package */void addInstanceFields(List<Field> fields) {
		if (fields == null)
			throw new IllegalArgumentException("fields must not be empty");

		if (fields.size() == 0)
			return;

		for (Field f : fields) {
			if (!instanceFields.contains(f))
				instanceFields.add(f);
		}

		Collections.sort(instanceFields);

	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(AccessFlags.classString(getAccessFlags())).append(" ");
		result.append(getSignature());
		if (getSuperclassSignature() != null) {
			result.append(" extends ").append(getSuperclassSignature());
		}
		if (interfaces.size() > 0) {
			result.append(" implements ");
			for (int i = 0; i < interfaces.size(); i++) {
				result.append(interfaces.get(i));
				if (i < (interfaces.size() - 1))
					result.append(", ");
			}
		}

		return result.toString();
	}

	// TODO when adding a class to the class table, check for volation of class
	// inheritance (e.g. no superclass circular references allowed).
	/**
	 * Sorts according to the rules specified in class_defs in the Dalvik file
	 * layout spec. This class is ordered after superclasses and interfaces that
	 * it has.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Clazz anotherClass) {
		if (anotherClass == null)
			return 1;

		if (!(anotherClass instanceof ObjectClass))
			return 1;

		ObjectClass another = (ObjectClass) anotherClass;

		if (getSignature().equals(another))
			return 0; // super classes sort before this one.

		int szCompare = interfaces.size() - another.getInterfaces().size();
		if (szCompare != 0)
			return szCompare;

		for (ObjectClass iClass : interfaces) {
			for (ObjectClass oClass : another.getInterfaces()) {
				if (!iClass.equals(anotherClass))
					return iClass.compareTo(oClass);

			}

		}

		return getSignature().compareTo(another.getSignature());
	}

	public List<ObjectClass> getInterfaces() {
		Collections.sort(interfaces);
		return Collections.unmodifiableList(interfaces);
	}

	public boolean hasClassData() {
		return classData != null ? true : false;
	}

}
