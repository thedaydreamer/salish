/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dx.merge;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.analysis.DexSwapVerify;
import schilling.richard.dalvik.vm.oo.VerifyException;
import schilling.richard.dexlib.inject.InjectException;
import org.gnu.salish.debug.util.FinnrApp;
import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.merge.Updater.BaseUpdater;

/**
 * Combine two dex files into one.
 */
public final class DexInjectMerger extends DexMerger {

    public static final String AUT_PACKAGE = "master"; // "schilling.richard.finnr.TCK";
    public static final String LOG_TAG = "Finnr.DexInjectMerger";
    public static final String LOG_TAG_FAIL = "Finnr.Fail";
    public static final String LOG_TAG_DEBUG = "Finnr.DexInjectMerger.Debug";

    /**
     * Total method injections attempted.
     */
    public int totalMethods = 0;

    /**
     * Of totalMethods, this contains the number of methods actually modified.
     */
    public int updatedMethods = 0;

    // TODO: add feature: let the user mask out parts of the program to not
    // test.
    // TODO: add feature: tell the user what parts of the program will crash
    // while under test (e.g. the parts that can't be tested).
    // TODO: determine top level features of the application.
    // TODO: generate a top level use case.
    /**
     * Class signatures that begin with these names are included - all others
     * are excluded.
     */
    public static final String[] INCLUDED_PACKAGES = {
            /*
             * this set works - 82 functions
             * "Lcom/burstly/lib/component/AdaptorCache", "Lcom/rovio/ka3d/App",
             * "Lcom/rovio/angrybirds/", "La;" - 87 functions.
             */

            // for testing AngryBirds
            "Lcom/burstly/lib/component/AdaptorCache", "Lcom/rovio/ka3d/App",
            "Lcom/rovio/angrybirds/", "La;"

            // "Lcom/burstly"

            // class #480 only
            // "Lcom/rovio/ka3d/App"

            // for all classes
            // "L"

            // install fail:
            // "Lcom/burstly" (Invalid insn_count)
            // "Lcom/google/" PROCESS-74
            // "Lcom/greystripe" PROCESS-76
            // "Lcom/inmobi" (Invalid addr)
            // "Lcom/jumptap" (Invalid insn_count)
            // "Lcom/millennialmedia" (Invalid addr)
            // "Lcom/flurry" (Invalid addr)
            // "Lcom/fortumo/" (Invalid addr)
            // "Lcom/greystripe" (Invalid addr)
            // "Lcom/rovio" (Invalid catch_all_addr)
            /*
             * these classes - Invalid insn_count "Lb;", "Lc;", "Ld;", "Le;",
             * "Lf;", "Lg;", "Lh;", "Li;", "Lj;", "Lk;", "Ll;", "Lm;", "Ln;",
             * "Lo;", "Lp;", "Lq;", "Lr;", "Ls;", "Lt;", "Lu;", "Lv;", "Lw;",
             * "Lx;"
             */

    };

    /**
     * Classes in this package or sub classCount will not be injected with code.
     */
    public static final String INJECTOR_CLASS_PACKAGE = "Lschilling/richard/finnr/injector";

    /**
     * The signature of the class that contains the function that contains code
     * that gets injected.
     */
    public static final String INJECTOR_CLASS = INJECTOR_CLASS_PACKAGE
            + "/InjectorApp;";

    /**
     * The code to inject resides in this function. If this function must exist
     * in the class INJECTOR_CLASS. If either INJECTOR_CLASS or the function
     * INJECT is missing then an exception is thrown.
     */
    public static final String INJECT_FUNCTION = "INJECT";
    public static final String INJECT_STATIC_FUNCTION = "INJECTSTATIC";
    public static final String INJECT_HTTP_FUNCTION = "INJECTHTTPRESPONSEHANDLER";

    /**
     * If a function name contains any one of the strings in this array it will
     * not be injected with code. The test made on the function name is done
     * with String.indexOf. Functions in this list override INCLUDED_FUNCTIONS.
     */
    public static final String[] EXCLUDED_FUNCTIONS = {
            "<init>", "<clinit>",
            "$", "run", INJECT_FUNCTION, INJECT_STATIC_FUNCTION,
            INJECT_HTTP_FUNCTION, "handleMessage", "onKeyDown", "onDraw",
            "uncaughtException", "onKeyUp", "getInstance", "get"
    };

    /**
     * Methods with these opcodes among their instruction sets will be skipped.
     */
    public static final int[] EXCLUDED_OPCODES = {
            Opcodes.PACKED_SWITCH
    };

    /**
     * Classes that contain these strings are excluded.
     */
    public static final String[] EXCLUDED_PACKAGES = {
            "Lschilling/richard/finnr/injector/InjectorClass", "$",
            "Lcom/burstly/lib/component/preinitialize/",
            "Lcom/burstly/lib/network/beans/CookieManager"
    };
    // 06-09 21:46:32.308: E/AndroidRuntime(19009): java.lang.RuntimeException:
    // unable to verify modified method getValidCookies in class
    // Lcom/burstly/lib/network/beans/CookieManager;

    /*
     * "didLoad", "run", "adNetworkDismissFullScreen",
     * "adNetworkPresentFullScreen", "adNetworkWasClicked", "attemptingToLoad",
     * "failedToLoad", "finishRequestToServer", "startRequestToServer",
     * "didPrecacheAd", "onAnimationRepeat","onAnimationStart" };
     */

    public static final String[] INCLUDED_FUNCTIONS = {
            "onCreate", "onResume",
            "onStart", "onDestroy", "onPause"
    };

    public static final int REGISTER_PADDING = 2;
    public static final int REGISTER_LIMIT = 255;

    private int methodCount = 0;
    private int classCount = 0;
    private int fieldCount = 0;
    private String currentClassName;

    public DexInjectMerger(DexBuffer dexA, DexBuffer dexB,
            CollisionPolicy collisionPolicy) throws IOException {
        super(dexA, dexB, collisionPolicy, new WriterSizes(dexA, dexB,
                SECTION_SAFETY));
    }

    /**
     * Reads a class_def_item beginning at {@code in} and writes the index and
     * data.
     */
    @Override
    protected void transformClassDef(DexBuffer in, ClassDef classDef,
            IndexMap indexMap) {
        idsDefsOut.assertFourByteAligned();
        idsDefsOut.writeInt(classDef.getTypeIndex());
        idsDefsOut.writeInt(classDef.getAccessFlags());
        idsDefsOut.writeInt(classDef.getSupertypeIndex());
        idsDefsOut.writeInt(classDef.getInterfacesOffset());

        int sourceFileIndex = indexMap.adjustString(classDef
                .getSourceFileIndex());
        idsDefsOut.writeInt(sourceFileIndex);

        int annotationsOff = classDef.getAnnotationsOffset();
        idsDefsOut.writeInt(indexMap.adjustAnnotationDirectory(annotationsOff));

        int classDataOff = classDef.getClassDataOffset();
        if (classDataOff == 0) {
            idsDefsOut.writeInt(0);
        } else {
            idsDefsOut.writeInt(classDataOut.getPosition());
            ClassData classData = in.readClassData(classDef);

            boolean inject = false;
            String className = in.typeNames().get(classDef.getTypeIndex());

            for (String s : INCLUDED_PACKAGES) {
                if (className.startsWith(s))
                    inject = true;
            }

            if (inject) {
                for (String s : EXCLUDED_PACKAGES) {
                    if (className.contains(s))
                        inject = false;
                }
            }

            // if (className.startsWith(INJECTOR_CLASS_PACKAGE))
            // inject = false;
            if (DO_LOG)
                Log.i(LOG_TAG, String.format("processing class %s", className));
            currentClassName = className;
            classCount++;

            transformClassData(in, classData, indexMap, inject);
        }

        int staticValuesOff = classDef.getStaticValuesOffset();
        if (staticValuesOff == 0) {
            idsDefsOut.writeInt(0);
        } else {
            DexBuffer.Section staticValuesIn = in.open(staticValuesOff);
            idsDefsOut.writeInt(encodedArrayOut.getPosition());
            transformStaticValues(staticValuesIn, indexMap);
        }
    }

    /**
     * Transforms class data so that it can be transferred from one DEX file to
     * another.
     * <p>
     * Notice that this method is just slightly different than the private
     * method of the same name in the parent class. This does not override
     * transformClassData in the parent class or call it.
     * 
     * @param in
     * @param classData
     * @param indexMap
     * @param inject if the method should be injected with code.
     */
    private void transformClassData(DexBuffer in, ClassData classData,
            IndexMap indexMap, boolean inject) {
        contentsOut.classDatas.size++;

        ClassData.Field[] staticFields = classData.getStaticFields();
        ClassData.Field[] instanceFields = classData.getInstanceFields();
        ClassData.Method[] directMethods = classData.getDirectMethods();
        ClassData.Method[] virtualMethods = classData.getVirtualMethods();

        fieldCount += staticFields.length;
        fieldCount += instanceFields.length;

        FinnrApp.updateVariableCount(fieldCount);

        classDataOut.writeUleb128(staticFields.length);
        classDataOut.writeUleb128(instanceFields.length);
        classDataOut.writeUleb128(directMethods.length);
        classDataOut.writeUleb128(virtualMethods.length);

        transformFields(indexMap, staticFields);
        transformFields(indexMap, instanceFields);
        transformMethods(in, indexMap, directMethods, inject);
        transformMethods(in, indexMap, virtualMethods, inject);
    }

    /**
     * Transform methodCount. Notice this is a different signature than the
     * method of the same name in the parent class.
     * <p>
     * <ol>
     * <li>check to see if the class' package begins with INJECTOR_CLASS_PACKAGE
     * (transformClassDef)
     * <li>make sure the inject method has been set
     * <li>check to see if the inject method and the destination method have sam
     * static access modifier (transformMethods)
     * <li>make sure that the method does not contain any strings from
     * EXCLUDED_FUNCTIONS (transformMethods)
     * </ol>
     */
    private void transformMethods(DexBuffer in, IndexMap indexMap,
            ClassData.Method[] methods, boolean inject) {

        List<MethodId> methodIds = in.methodIds();

        int lastOutMethodIndex = 0;
        for (ClassData.Method method : methods) {

            boolean doInject = inject;

            int outMethodIndex = indexMap.adjustMethod(method.getMethodIndex());
            classDataOut.writeUleb128(outMethodIndex - lastOutMethodIndex);
            lastOutMethodIndex = outMethodIndex;

            classDataOut.writeUleb128(method.getAccessFlags());

            if (method.getCodeOffset() == 0) {
                classDataOut.writeUleb128(0);
            } else {
                codeOut.alignToFourBytes();
                classDataOut.writeUleb128(codeOut.getPosition());

                MethodDefinition target = new MethodDefinition(in,
                        method.getMethodIndex());
                String methodName = target.name();

                if (doInject) {

                    // check for excluded opcodes
                    for (int op : EXCLUDED_OPCODES) {
                        if (target.containsOpcode(op)) {
                            doInject = false;
                            if (DO_LOG)
                                Log.i(LOG_TAG,
                                        String.format(
                                                "Method %s in class %s will be skipped because it contains an unprocessable opcode (%d)",
                                                target.methodId().getName(),
                                                target.methodId()
                                                        .getDeclaringClassSignature(),
                                                op));
                        }

                    }
                }

                if (doInject) {

                    /*
                     * doInject = false; for (String s : INCLUDED_FUNCTIONS) {
                     * if (methodName.equals(s)) doInject = true; }
                     */

                    for (String s : EXCLUDED_FUNCTIONS) {
                        if (methodName.contains(s))
                            doInject = false;

                    }

                }

                transformCode(in, target, indexMap, doInject);

                this.functionsProcessed++;

            }
        }
    }

    /**
     * Before calling this function, the thisRegister member, if set is the
     * register number from source Instructions that needs to get mapped to the
     * this register in the target.
     * 
     * @param in
     * @param code
     * @param indexMap
     * @param inject
     * @param registers the total number of registers in the function.
     * @param the number of parameters that the target function has.
     * @param nonParamRegisters the number of local/non-parameter registers.
     *            Ignored
     */
    private void transformCode(DexBuffer in, MethodDefinition target,
            IndexMap indexMap, boolean inject) {

        this.totalMethods++;

        if (inject) {
            this.methodCount++;
            FinnrApp.updateMethodAttempted(this.classCount, this.totalMethods,
                    target);
        }

        // save the position of the register size count so it can be updated.
        // try {
        int oldRegSize = target.code().getRegistersSize();
        int oldInsSize = target.code().getInsSize();
        int oldOutsSize = target.code().getOutsSize();

        boolean modified = inject(in, target, indexMap, inject);
        if (modified) {
            this.updatedMethods++;

            FinnrApp.updateMethodModified(this.classCount, this.totalMethods,
                    this.updatedMethods, target);

            int newRegSize = target.code().getRegistersSize();
            int newInsSize = target.code().getInsSize();
            int newOutsSize = target.code().getOutsSize();

            /* update the number of registers for this code block */
            if (DO_LOG)
                Log.d(LOG_TAG, String.format(
                        "method %s modified: %d->%d reg, %d->%d ins, %d->%d outs",
                        target.methodId().getName(), oldRegSize, newRegSize,
                        oldInsSize, newInsSize, oldOutsSize, newOutsSize));
        }
        /*
         * } catch (IllegalStateException ex) { ex.printStackTrace(); // FIXME:
         * never catch throwable - this is here for debugging // purposes only.
         * } catch (NullPointerException npe) { npe.printStackTrace(); // FIXME:
         * resolve } catch (ClassCastException cce) { // FIXME: resolve
         * cce.printStackTrace(); } catch (UnsupportedOperationException ex) {
         * // FIXME: resolve ex.printStackTrace(); } catch
         * (NoSuchElementException ex) { // FixME: resolve ex.printStackTrace();
         * } catch (RuntimeException ex) { // FIXME: resolve
         * ex.printStackTrace(); }
         */

        short[] instructions = target.code().getInstructions();
        Code.Try[] tries = target.code().getTries();
        Code.CatchHandler[] catchHandlers = target.code().getCatchHandlers();

        contentsOut.codes.size++;
        codeOut.assertFourByteAligned();

        codeOut.writeUnsignedShort(target.code().getRegistersSize());
        codeOut.writeUnsignedShort(target.code().getInsSize());
        codeOut.writeUnsignedShort(target.code().getOutsSize());
        codeOut.writeUnsignedShort(tries.length);
        codeOut.writeInt(target.code().getDebugInfoOffset());
        codeOut.writeInt(instructions.length);
        codeOut.write(instructions);

        if (tries.length > 0) {
            if (instructions.length % 2 == 1) {
                codeOut.writeShort((short) 0); // padding
            }

            for (Code.Try tryItem : tries) {

                codeOut.writeInt(tryItem.getStartAddress());
                codeOut.writeUnsignedShort(tryItem.getInstructionCount());
                codeOut.writeUnsignedShort(tryItem.getHandlerOffset());
            }

            codeOut.writeUleb128(catchHandlers.length);
            for (Code.CatchHandler catchHandler : catchHandlers) {

                transformEncodedCatchHandler(catchHandler, indexMap);
            }
        }
        List<Integer> types = in.typeIds();

    }

    /**
     * Injects the code found in injectCode into the instructions array. A new
     * injected array of instructions is returned.
     * <P>
     * This version of inject relies on the following code being the code that
     * is in INJECT_FUNCTION:
     * <p>
     * 
     * <pre>
     * 
     * 
     * 
     * 
     * 
     * 
     * public void INJECT() {
     *     // InjectorClass.logIfContext(Object, int classId, int methodId);
     *     InjectorClass.logIfContext(this, 1, 2);
     * }
     * </pre>
     * <p>
     * At some future time, the code can be hardcoded into this class for
     * convenience. It might also be possible to setup a scenario where certain
     * registers are treated in a manner similar to this:
     * <ul>
     * <li>v0 - the class id.
     * <li>v1 - the method id.
     * <li>v2 - some other parameter ?
     * </ul>
     * The class ID and the method ID are the only pieces of information that
     * need to be transmitted.
     * 
     * @param registerCount the number of total registers used by the method
     *            that contains instructions.
     * @param nonParamRegisters the number of non-parameter (e.g. local)
     *            registers in the function.
     * @param instructions the instructions from the method.
     * @return true if the method was updated or false otherwise.
     */

    private boolean inject(DexBuffer in, MethodDefinition target,
            IndexMap indexMap, boolean inject) {

        boolean result = false;

        short[] instructions = target.instructions();
        InstructionTransformer transformer = (in == dexA) ? aInstructionTransformer
                : bInstructionTransformer;
        short[] transformedInstructions = transformer.transform(instructions);
        target.setInstructions(transformedInstructions);

        if (inject) {

            List<BaseUpdater> updaterList = BaseUpdater.getUpdater(target);
            for (BaseUpdater updater : updaterList) {

                MethodDefinition methodToInject = updater.getMethodToInject();

                // make sure total register count falls within limits.
                int combinedRegisters = target.code().getRegistersSize()
                        + methodToInject.code().getRegistersSize();

                if (updater.observeRegisterLimits()
                        && combinedRegisters > REGISTER_LIMIT) {
                    if (DO_LOG)
                        Log.d(LOG_TAG,
                                String.format(
                                        "skipping injection into method %s of class %s because combined register count would be %d",
                                        target.methodId().getName(), target
                                                .methodId()
                                                .getDeclaringClassSignature(),
                                        combinedRegisters));
                    continue;
                }

                List<Integer> addresses = updater.findInjectPosition(target);
                if (addresses.size() == 0)
                    continue; // nothing to inject

                for (Integer address : addresses) {
                    try {

                        boolean success = target.inject(methodToInject,
                                updater, address);
                        if (!result && success)
                            result = true;

                        if (result) {

                            try {
                                DexSwapVerify.swapTriesAndCatches(target
                                        .method(), target.code(), target
                                        .buffer().typeIds());
                            } catch (VerifyException ex) {
                                Log.e(LOG_TAG,
                                        "unable to verify modified method.", ex);
                                result = false;

                            }
                        }

                    } catch (InjectException e) {
                        throw new RuntimeException(e);
                    }
                }

                // save the methods that have been modified.
                this.methodList.addAll(updater.methodList());

            }

        }

        return result;

    }

    // TODO Use Ropper to do some runtime code analysis and as the intitial
    // virtual machine for testing.
    /**
     * Injects source instructions into target instructions at the specified
     * position. The parameterCount of the target instruction set is required so
     * that the register numbers for parameters can be respected. They always
     * appear as the last registers in the register set.
     * 
     * @param regisgterCount the number of registers that the function uses.
     * @param parameterCount the number of parameters in target instructions.
     * @param target the target instruction set.
     * @param pos the position to do the insertion at.
     * @param source the instructions to inject into target at pos.
     * @return the injected instruction set with register references properly
     *         mapped.
     */
    /*
     * private DecodedInstruction[] injectWithRegisterAdjust( int
     * targetRegisterCount, int paramCount, int nonParamRegisters,
     * DecodedInstruction[] target, int pos, DecodedInstruction[] source,
     * boolean targetIsStatic) { // this register position: see
     * getInvocationThis in CodeVerify.cpp // number of registers used by the
     * injection code. int injectRegisterCount = 0; if (targetIsStatic)
     * injectRegisterCount = injectCodeStatic.getRegistersSize(); else
     * injectRegisterCount = injectCode.getRegistersSize(); // get the registers
     * used in the injection function. SparseIntArray injectRegisters =
     * buildRegisterMap(source); // calculate the number of registers to adjust
     * the target registers by. // Add 2 for padding. int adjust =
     * injectRegisterCount + REGISTER_PADDING; Log.i(LOG_TAG,
     * "          injecting " + adjust + " registers (" + REGISTER_PADDING +
     * " for padding)"); // update the target register set. SparseIntArray
     * targetRegisters = buildRegisterMap(target); int instructionsUpdated = 0;
     * int referencesUpdated = 0; if (targetRegisters.size() > 0) { int[] keys =
     * new int[targetRegisters.size()]; Log.i(LOG_TAG, "          mapping " +
     * keys.length + " target registers."); StringBuilder builder = new
     * StringBuilder(); builder.append("          "); for (int i = 0; i <
     * targetRegisters.size(); i++) { keys[i] = targetRegisters.keyAt(i);
     * builder.append(" ").append(Integer.toString(keys[i])); }
     * builder.append("\n          "); targetRegisters.clear(); for (int i = 0;
     * i < keys.length; i++) { int value = keys[i] + adjust;
     * targetRegisters.put(keys[i], value);
     * builder.append(" ").append(Integer.toString(value)); }
     * builder.append("\n"); Log.i(LOG_TAG, builder.toString()); for
     * (DecodedInstruction instruction : target) { if (instruction == null)
     * continue; referencesUpdated += instruction.getFormat().updateRegisters(
     * instruction, targetRegisters, pos, source.length); instructionsUpdated++;
     * } Log.i(LOG_TAG, "          " + instructionsUpdated +
     * " instructions and " + referencesUpdated +
     * " register references updated."); } // target registers have been
     * updated. Update the any this reference in // injected registers. if
     * (targetIsStatic) { // inject static code. Log.i(LOG_TAG,
     * "          modifying static function to log."); } else if
     * (injectRegisters.size() > 0) { // inject non static code. // the register
     * for injection code that points to the this reference // of that function.
     * Integer thisInjectRegister = null; Integer thisTargetRegister = null; //
     * function being modified is not static. Log.i(LOG_TAG,
     * "          modifying non-static function to log."); Log.i(LOG_TAG,
     * "          updating this pointer of injected function."); // a this
     * pointer exists - adjust the inject registers to use it // if necessary.
     * thisInjectRegister = new Integer(injectRegisterCount -
     * injectParameterCount - 1); // zero based thisTargetRegister = new
     * Integer(targetRegisterCount - paramCount - 1 + adjust); // is the this
     * reference in the inject function used int value =
     * injectRegisters.get(thisInjectRegister.intValue(), -1); if (value != -1)
     * { // this is used .. update it.
     * injectRegisters.put(thisInjectRegister.intValue(),
     * thisTargetRegister.intValue()); Log.i(LOG_TAG, "               register "
     * + thisInjectRegister.intValue() + "->" + thisTargetRegister.toString());
     * } instructionsUpdated = 0; referencesUpdated = 0; for (DecodedInstruction
     * instruction : source) { if (instruction == null) continue;
     * referencesUpdated += instruction.getFormat().updateRegisters(
     * instruction, injectRegisters, pos, 0); // // referencesUpdated +=
     * instruction.getFormat().updateRegisters( // instruction, injectRegisters,
     * Integer.MAX_VALUE, 0); // instructionsUpdated++; } Log.i(LOG_TAG,
     * "          " + instructionsUpdated + " instructions updated and " +
     * referencesUpdated + " references updated."); } // output register map of
     * targetRegisters StringBuilder injectRegisterOutput = new StringBuilder();
     * injectRegisterOutput.append("          "); for (int i = 0; i <
     * injectRegisters.size(); i++) { injectRegisterOutput.append(" ").append(
     * Integer.toString(injectRegisters.keyAt(i))); }
     * injectRegisterOutput.append("\n          "); for (int i = 0; i <
     * injectRegisters.size(); i++) { injectRegisterOutput.append(" ").append(
     * Integer.toString(injectRegisters.valueAt(i))); } Log.i(LOG_TAG,
     * "Inject reister map:"); Log.i(LOG_TAG, injectRegisterOutput.toString());
     * DecodedInstruction[] result = InjectUtil .insertInto(target, pos,
     * source); return result; }
     */

    /**
     * Used by injectWithRegisterAdjust. Builds up a register list in a sparse
     * int array for a given set of instructions. The sparse array returned can
     * be use to adjust register numbers in the instructions.
     * 
     * @param instructions the instructions to map registers for.
     * @return a sparse array where the index is th register number and all
     *         values are zero.
     */
    public SparseIntArray buildRegisterMap(DecodedInstruction[] instructions) {
        SparseIntArray result = new SparseIntArray();

        for (DecodedInstruction ins : instructions) {

            if (ins == null)
                continue;

            int[] regs = ins.getRegisterReferences();
            for (int i : regs) {
                result.put(i, i);
            }
        }

        return result;
    }

    /**
     * An object used to track associated class id and method id values.
     * 
     * @author rschilling
     */
    public static class ClassMethodPair {

        public long classId;
        public long methodId;
        public long statusCode;

        public ClassMethodPair(long cId, long mId, long sCode) {
            classId = cId;
            methodId = mId;
            statusCode = sCode;
        }

        @Override
        public boolean equals(Object toAnother) {

            if (!(toAnother instanceof ClassMethodPair))
                return false;

            ClassMethodPair cmp = (ClassMethodPair) toAnother;

            return (this.classId == cmp.classId)
                    && (this.methodId == cmp.methodId)
                    && (cmp.statusCode == this.statusCode) ? true : false;

        }

        @Override
        public String toString() {
            return String.format("cId: %d, mId: %d, status: %d", classId,
                    methodId, statusCode);
        }
    }

    /**
     * A list of class and method id numbers that are found in the program. This
     * used as a part of code coverage calculations.
     */
    public LinkedList<ClassMethodPair> methodList = new LinkedList<ClassMethodPair>();

}
