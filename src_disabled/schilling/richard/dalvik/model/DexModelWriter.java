
package com.android.dx.dex.file;

import java.util.List;

import schilling.richard.dalvik.model.Clazz;
import schilling.richard.dalvik.model.DexModel;
import schilling.richard.dalvik.model.Field;
import schilling.richard.dalvik.model.Method;
import schilling.richard.dalvik.model.MethodPrototype;

import com.android.dx.dex.SizeOf;
import com.android.dx.dex.TableOfContents;
import com.android.dx.io.DexBuffer;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;

/**
 * Converts a DexModel to a DexFile.
 * 
 * @author rschilling
 */
public class DexModelWriter {

    private DexModel model;

    public DexModelWriter(DexModel model) {
        this.model = model;
    }

    public DexFile asDexFile() {
        DexFile result = new DexFile(model.getOptions());

        // string_ids
        for (String s : model.stringPool()) {
            CstString cstString = new CstString(s);
            result.internIfAppropriate(cstString);
        }

        // type_ids
        for (String s : model.typePool()) {
            CstType cstType = new CstType(Type.intern(s));
            result.internIfAppropriate(cstType);
        }

        // proto_ids
        for (MethodPrototype p : model.prototypePool()) {
            String descriptor = p.descriptor();
            Prototype prototype = Prototype.intern(descriptor);
            result.getProtoIds().intern(prototype);
        }

        // field_ids
        for (Field f : model.fieldPool()) {
            Type definingClassType = Type.intern(f.getDefiningClass().getSignature());
            CstType cstType = CstType.intern(definingClassType);
            CstNat cstNat = new CstNat(new CstString(f.getName()), new CstString(f.getType()));

            CstFieldRef fRef = new CstFieldRef(cstType, cstNat);
            result.internIfAppropriate(fRef);

        }

        // method_ids
        for (Method m : model.methodPool()) {

            result.internIfAppropriate(m.asCstMethodRef());

        }

        // class_defs

        for (Clazz c : model.classPool(true)) {
            CstType cType = new CstType(Type.intern(c.getSignature()));
            CstType superType = new CstType(Type.intern(c.getSuperclassSignature()));

            List<String> interfaces = c.getInterfaces();

            TypeList iTypes = null;
            for (String s : interfaces) {

                if (iTypes == null)
                    iTypes = StdTypeList.make(Type.intern(s));
                else
                    iTypes = iTypes.withAddedType(Type.intern(s));

            }

            // class def
            /*
             * ClassDefItem cDefItem = new ClassDefItem(cType, c.accessFlags,
             * superType, iTypes, new CstString(c.sourceFile));
             * 
             * for (Method m : c.directMethods){
             * 
             * CstMethodRef cstMethod = m.asCstMethodRef();
             * 
             * EncodedMethod encodedMethod = new EncodedMethod(method,
             * accessFlags, code, throwsList)
             * 
             * encodedMethod.addContents(result);
             * cDefItem.addDirectMethod(encodedMethod);
             * 
             * }
             * 
             * cDefItem.addContents(result);
             */

            /* class data */
            /*
             * ClassDataItem cdItem = new ClassDataItem(cType);
             * cdItem.
             */

        }

        return result;

    }

}
