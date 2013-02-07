
package schilling.richard.dalvik.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.Code;

public final class ModelVisitor {

    public static void visit(DexModel model, Visitor visitor) {
        if (model == null)
            return;
        if (visitor == null)
            return;

        visitClassesAndPrototypes(model, visitor);
        visitMethods(model, visitor);

        visitStrings(model, visitor);

    }

    public static void visitStrings(DexModel model, Visitor visitor) {
        List<String> strings = model.stringPool();
        for (String s : strings) {
            visitor.stringFound(s);
        }
    }

    public static void visitClassesAndPrototypes(DexModel model, Visitor visitor) {
        if (model == null)
            return;

        if (visitor == null)
            return;

        List<Clazz> classes = model.classPool(false);
        for (Clazz clazz : classes) {
            List<String> interfaces = clazz.getInterfaces();

            if (interfaces != null && interfaces.size() > 0) {
                visitor.typeListFound(clazz, interfaces);
            }

            if (clazz.hasClassData())
                visitor.classDataFound(clazz);
        }

        List<MethodPrototype> prototypes = model.prototypePool();
        for (MethodPrototype p : prototypes) {
            List<String> parameters = p.getParameters();
            if (parameters != null && parameters.size() > 0) {
                visitor.typeListFound(p, parameters);
            }
        }

    }

    /**
     * Visit annotations and annotation set ref lists. Found in class, field,
     * method and parameterannotations (methods).
     * 
     * @param model
     * @param visitor
     */
    public static void visitMethods(DexModel model, Visitor visitor) {
        if (model == null)
            return;

        if (visitor == null)
            return;

        // found in parameter annotations
        List<Method> methods = model.methodPool();
        for (Method m : methods) {
            List<AnnotationSet> aSetList = m.getParameterAnnotations();
            if (aSetList != null && aSetList.size() > 0) {
                visitor.annotationSetRefListFound(m, aSetList);
                for (AnnotationSet aSet : aSetList) {
                    visitor.annotationSetFound(aSetList, aSet);
                }
            }

            if (m.getCode() != null)
                visitor.codeFound(m);

        }

        List<Clazz> classes = model.classPool(false);
        for (Clazz clazz : classes) {
            if (clazz.hasAnnotationDirectoryItems()) {

                visitor.annotationDirectoryItemFound(clazz);
                /*
                 * if (clazz.annotations != null && clazz.annotations.size() >
                 * 0) {
                 * visitor.annotationSetRefListFound(clazz, clazz.annotations);
                 * for (AnnotationSet cAnn : clazz.annotations) {
                 * visitor.annotationSetFound(clazz, cAnn);
                 * 
                 * for (Annotation annotation : cAnn){
                 * visitor.annotationFound(cAnn, annotation);
                 * }
                 * }
                 * }
                 */

            }

            if (clazz.getStaticInitialValues() != null)
                visitor.encodedArrayFound(clazz);

        }
    }

    public static interface Visitor {

        public void typeListFound(Object parent, List<String> typeList);

        public void annotationSetRefListFound(Object parent, List<AnnotationSet> annotationSet);

        public void annotationSetFound(Object parent, AnnotationSet aSet);

        public void annotationFound(Object parent, Annotation aSet);

        public void classDataFound(Clazz clazz);

        public void codeFound(Method method);

        public void stringFound(String s);

        public void encodedArrayFound(Clazz parent);

        public void annotationDirectoryItemFound(Clazz parent);
    }

    public static class AllSectionsVisitor implements Visitor {

        private Hashtable<Object, List<String>> typeLists = new Hashtable<Object, List<String>>();
        private Hashtable<Object, List<AnnotationSet>> annotationSetRefLists = new Hashtable<Object, List<AnnotationSet>>();
        private Hashtable<Object, AnnotationSet> annotationSets = new Hashtable<Object, AnnotationSet>();
        private Hashtable<Object, Annotation> annotations = new Hashtable<Object, Annotation>();
        private Hashtable<String, Clazz> classDataTable = new Hashtable<String, Clazz>();
        private LinkedList<Method> methodsWithCode = new LinkedList<Method>();
        private LinkedList<Clazz> classesWithEncodedArrays = new LinkedList<Clazz>();
        private LinkedList<Clazz> classesWithAnnotationDirectories = new LinkedList<Clazz>();

        // type lists

        @Override
        public void typeListFound(Object parent, List<String> typeList) {
            typeLists.put(parent, typeList);

        }

        public int typeListsFound() {
            return typeLists.size();
        }

        public int typeListByteCount() {
            int result = 0;
            for (Enumeration<List<String>> sEnum = typeLists.elements(); sEnum.hasMoreElements();) {
                result += SizeOf.type_list(sEnum.nextElement());
            }

            return result;

        }

        // annotation set ref lists

        @Override
        public void annotationSetRefListFound(Object parent, List<AnnotationSet> annotationSet) {

            annotationSetRefLists.put(parent, annotationSet);

        }

        public int annotationSetRefListsFound() {
            return annotationSetRefLists.size();
        }

        public int annotationSetRefListsByteCount() {
            int result = 0;
            for (Enumeration<List<AnnotationSet>> sEnum = annotationSetRefLists.elements(); sEnum
                    .hasMoreElements();) {
                result += SizeOf.annotation_set_ref_list(sEnum.nextElement());
            }

            return result;
        }

        // annotation sets

        @Override
        public void annotationSetFound(Object parent, AnnotationSet aSet) {
            annotationSets.put(parent, aSet);

        }

        public int annotationSetsFound() {
            return annotationSets.size();
        }

        // TODO update SizeOf functions to only report on the size of the
        // specific item - exclude children.
        public int annotationSetByteCount() {
            int result = 0;
            for (Enumeration<AnnotationSet> sEnum = annotationSets.elements(); sEnum
                    .hasMoreElements();) {
                result += SizeOf.annotation_set_item(sEnum.nextElement());
            }

            return result;

        }

        // annotations

        @Override
        public void annotationFound(Object parent, Annotation ann) {
            annotations.put(parent, ann);

        }

        public int annotationsFound() {
            return annotations.size();
        }

        public int annotationsByteCount() {
            int result = 0;
            for (Enumeration<Annotation> sEnum = annotations.elements(); sEnum.hasMoreElements();) {
                result += SizeOf.annotation_item(sEnum.nextElement());
            }

            return result;
        }

        @Override
        public void classDataFound(Clazz clazz) {

            classDataTable.put(clazz.getSignature(), clazz);

        }

        public int classDatasFound() {

            return classDataTable.size();

        }

        public int classDataByteCount() {

            int result = 0;
            for (Enumeration<Clazz> sEnum = classDataTable.elements(); sEnum.hasMoreElements();) {
                result += SizeOf.class_data_item(sEnum.nextElement());
            }

            return result;

        }

        @Override
        public void codeFound(Method method) {
            if (!methodsWithCode.contains(method))
                methodsWithCode.add(method);

        }

        public int codeItemsFound() {
            return methodsWithCode.size();
        }

        public int codeBytes() {
            int result = 0;

            for (Method m : methodsWithCode) {

                result += SizeOf.code(m.getCode());
            }

            return result;
        }

        private int stringCount = 0;
        private int stringByteCount = 0;

        @Override
        public void stringFound(String s) {
            stringCount++;
            stringByteCount += SizeOf.string_data_item(s);

        }

        @Override
        public void encodedArrayFound(Clazz parent) {
            if (!classesWithEncodedArrays.contains(parent))
                classesWithEncodedArrays.add(parent);

        }

        public int encodedArrayCount() {
            return classesWithEncodedArrays.size();
        }

        public int encodedArrayBytes() {
            int result = 0;
            for (Clazz clazz : classesWithEncodedArrays) {
                result += SizeOf.encoded_array_item(clazz.getStaticInitialValues());
            }
            return result;
        }

        @Override
        public void annotationDirectoryItemFound(Clazz parent) {
            if (!classesWithAnnotationDirectories.contains(parent))
                classesWithAnnotationDirectories.add(parent);

        }

        public int annotationDirectoryCount() {
            return classesWithAnnotationDirectories.size();
        }

        public int annotationDirectoryBytes() {
            int result = 0;
            for (Clazz clazz : classesWithAnnotationDirectories) {

                result += SizeOf.annotations_directory_item(clazz);
            }

            return result;
        }

    }

}
