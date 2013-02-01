
package com.android.dx.dex.file;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;

import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.android.dx.dex.DexFormat;
import com.android.dx.dex.DexOptions;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.FieldId;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;

/**
 * Consumes a DexBuffer and creates a DexFile from it.
 * 
 * @author rschilling
 */
public final class DexFileBuilder {

    public static final String LOG_TAG = "R3.DexFileBuilder";

    private DexFile dFile;

    public DexFileBuilder(byte[] magic) {
        DexOptions options = new DexOptions();
        options.targetApiLevel = DexFormat.magicToApi(magic);
        dFile = new DexFile(options);
    }

    /**
     * Clone strings from the specified buffer into the DexFile.
     * 
     * @param buffer the buffer to read string_ids
     */
    public void add_string_ids(DexBuffer buffer) {
        List<String> strings = buffer.strings();
        for (String s : strings) {
            dFile.internIfAppropriate(new CstString(s));

            if (DO_LOG)
                Log.i(LOG_TAG, "String '" + s + "' added to string constant pool.");
        }
    }

    public void add_type_ids(DexBuffer buffer) {
        List<Integer> typeIds = buffer.typeIds;
        List<String> strings = buffer.strings();
        for (Integer typeId : typeIds) {
            String s = strings.get(typeId.intValue());

            // check for VOID type as it causes an exception
            if (!s.equals(Type.VOID.getDescriptor())) {

                CstType ct = new CstType(Type.intern(s));
                dFile.internIfAppropriate(ct);
                if (DO_LOG)
                    Log.i(LOG_TAG, "Type " + s + " added to type_ids");
            } else {
                if (DO_LOG)
                    Log.i(LOG_TAG, "Type VOID skipped.");
            }

        }
    }

    public void add_proto_ids(DexBuffer buffer) {

        List<ProtoId> protoIds = buffer.protoIds();

        for (ProtoId id : protoIds) {
            String shortyName = id.getShortyString();
            String returnType = id.getReturnTypeString();
            List<String> parameters = id.getParameters();
        }

        // FIXME work on this after methods are defined.
    }

    public void add_field_ids(DexBuffer buffer) {
        List<FieldId> fIdentifiers = buffer.fieldIds;
        List<String> types = buffer.typeNames();
        List<String> strings = buffer.strings();

        for (FieldId fId : fIdentifiers) {
            String classType = types.get(fId.getDeclaringClassIndex());
            String fieldDescriptor = types.get(fId.getTypeIndex());
            String fieldName = strings.get(fId.getNameIndex());

            CstNat nameAndType = new CstNat(new CstString(fieldName),
                    new CstString(fieldDescriptor));
            CstType cstType = new CstType(Type.intern(classType));

            CstFieldRef cfRef = new CstFieldRef(cstType, nameAndType);

            dFile.internIfAppropriate(nameAndType);
            dFile.internIfAppropriate(cstType);
            dFile.getFieldIds().intern(cfRef);

            FieldIdItem fIdItem = new FieldIdItem(cfRef);
            fIdItem.addContents(dFile);

            if (DO_LOG)
                Log.i(LOG_TAG, "Field id contents for " + classType + "."
                        + fieldName + " added to field_ids");

        }
    }

    public void add_method_ids(DexBuffer buffer) {
        List<com.android.dx.io.MethodId> methodIds = buffer.methodIds();
        List<String> types = buffer.typeNames();
        List<ProtoId> protos = buffer.protoIds();

        for (com.android.dx.io.MethodId mId : methodIds) {
            String definingClass = types.get(mId.getDeclaringClassIndex());
            String methodName = types.get(mId.getNameIndex());

            ProtoId pId = protos.get(mId.getProtoIndex());
            List<String> methodParameters = pId.getParameters();
            StringBuilder paramString = new StringBuilder();
            paramString.append("(");
            for (String param : methodParameters) {
                paramString.append(param);
            }
            paramString.append(")");

            CstType definingClassCstType = new CstType(
                    Type.intern(definingClass));

            CstNat cstNat = new CstNat(new CstString(methodName),
                    new CstString(paramString.toString())); // name, parameter
                                                            // list ??? FIXME be
                                                            // sure this is
                                                            // correct

            CstMethodRef cmRef = new CstMethodRef(definingClassCstType, cstNat);

            dFile.internIfAppropriate(definingClassCstType);
            dFile.internIfAppropriate(cstNat);
            dFile.internIfAppropriate(cmRef);

            if (DO_LOG)
                Log.i(LOG_TAG, "method " + definingClass + "." + methodName
                        + " return type " + pId.getReturnTypeString()
                        + " added to method_ids");

        }

    }

    public void add_class_defs(DexBuffer buffer) {
        // create ClassDefItem objects from the buffer.
        for (Iterator<ClassDef> iter = buffer.classDefs().iterator(); iter
                .hasNext();) {
            ClassDef cDef = iter.next();
            ClassData cData = buffer.readClassData(cDef);

            CstType thisClassName = internClassName(buffer, cDef);
            CstType superClassName = internSuperclassName(buffer, cDef);
            CstString sourceFile = createSourceFileName(buffer, cDef);

            int accessFlags = cDef.getAccessFlags();
            TypeList interfaces = createInterfaceTypes(buffer, cDef);
            ClassDefItem clazz = new ClassDefItem(thisClassName, accessFlags,
                    superClassName, interfaces, sourceFile);

            addDirectMethodsTo(clazz, cData, buffer); // clazz.addDirectMethod(method)

            /*
             * addVirtualMethodsTo(clazz, buffer); //
             * clazz.addVirtualMethod(method) addInstanceFieldsTo(clazz,
             * buffer); // clazz.addInstanceField(field)
             * addStaticFieldsTo(clazz, buffer); // clazz.addStaticField(field,
             * value) addParameterAnnotationsTo(clazz, buffer); //
             * clazz.addParameterAnnotations(method, list)
             * addMethodAnnotationsTo(clazz, buffer); //
             * clazz.addMethodAnnotations(method, annotations)
             * addFieldAnnotationsTo(clazz, buffer); //
             * clazz.addFieldAnnotations(field, annotations)
             */

            dFile.internIfAppropriate(thisClassName);
            dFile.internIfAppropriate(superClassName);
            dFile.internIfAppropriate(sourceFile);
            clazz.addContents(dFile);

        }
    }

    private void addDirectMethodsTo(ClassDefItem clazz, ClassData cData,
            DexBuffer buffer) {
        ClassData.Method[] methods = cData.getDirectMethods();
        List<String> types = buffer.typeNames();
        List<MethodId> methodIds = buffer.methodIds();
        List<String> strings = buffer.strings();
        List<ProtoId> protoIds = buffer.protoIds();

        for (Method method : methods) {

            int accessFlags = method.getAccessFlags();

            MethodId mid = methodIds.get(method.getMethodIndex());
            ProtoId protoid = protoIds.get(mid.getProtoIndex());

            /*
             * method.getMethodIndex() String methodName =
             * types.get(method.getNameIndex()); clazz.addDirectMethod(method)
             */

            String definingCclassName = buffer.typeNames().get(
                    mid.getDeclaringClassIndex());
            CstType definingClass = new CstType(Type.intern(definingCclassName));

            String methodName = strings.get(mid.getNameIndex());
            String methodReturnType = protoid.getReturnTypeString();

            CstNat methodNat = new CstNat(new CstString(methodName),
                    new CstString(methodReturnType));

            CstMethodRef cstMethodRef = new CstMethodRef(definingClass,
                    methodNat);
            /*
             * DalvCode dCode = new DalvCode(); TypeList tList; EncodedMethod
             * eMethod = new EncodedMethod(cstMethodRef, accessFlags, dCode,
             * tList);
             */
            // clazz.addDirectMethod(EncodedMethod);
        }

    }

    @SuppressWarnings("unused")
    public void dumpStats() {
        if (!DO_LOG)
            return;

        Statistics stats = dFile.getStatistics();
        Log.i(LOG_TAG, "DEX FILE STATISTICS-------------------");
        Log.i(LOG_TAG, stats.toHuman());

    }

    /**
     * Creates a DexFile from a DexBuffer
     * 
     * @param buffer
     * @return
     */
    public DexFile build(DexBuffer buffer) {
        DexOptions options = new DexOptions();
        options.targetApiLevel = DexFormat.magicToApi(buffer
                .getTableOfContents().magic);

        DexFile result = new DexFile(options);

        // create ClassDefItem objects from the buffer.
        for (Iterator<ClassDef> iter = buffer.classDefs().iterator(); iter
                .hasNext();) {
            ClassDef cDef = iter.next();
            ClassData cData = buffer.readClassData(cDef);

            int accessFlags = cDef.getAccessFlags();
            CstType thisClass = internClassName(buffer, cDef);
            CstType superClass = internSuperclassName(buffer, cDef);
            TypeList interfaces = createInterfaceTypes(buffer, cDef);
            CstString sourceFile = createSourceFileName(buffer, cDef);

            ClassDefItem clazz = new ClassDefItem(thisClass, accessFlags,
                    superClass, interfaces, sourceFile);

            result.add(clazz);

        }

        return result;

    }

    private CstString createSourceFileName(DexBuffer buffer, ClassDef cDef) {
        int sourceFileIndex = cDef.getSourceFileIndex();
        if (sourceFileIndex >= 0) {
            String sourceFile = buffer.strings().get(sourceFileIndex);
            return new CstString(sourceFile);
        }

        return null;
    }

    private TypeList createInterfaceTypes(DexBuffer buffer, ClassDef cDef) {
        TypeList result = null;

        short[] interfaceIds = cDef.getInterfaces();
        if (interfaceIds != null) {
            for (short s : interfaceIds) {

                String className = buffer.typeNames().get(s);
                if (result == null)
                    result = StdTypeList.make(Type.intern(className));
                else
                    result = result.withAddedType(Type.intern(className));

            }
        }
        return result;
    }

    private CstType internClassName(DexBuffer buffer, ClassDef cDef) {
        int typeIdx = cDef.getTypeIndex();
        String className = buffer.typeNames().get(typeIdx);

        return new CstType(Type.intern(className));

    }

    private CstType internSuperclassName(DexBuffer buffer, ClassDef cDef) {
        int typeIdx = cDef.getSupertypeIndex();
        String className = buffer.typeNames().get(typeIdx);
        return new CstType(Type.intern(className));
    }
}
