
package com.android.dx.io.opcode.verify;

import static schilling.richard.dalvik.vm.oo.MethodType.METHOD_DIRECT;
import static schilling.richard.dalvik.vm.oo.MethodType.METHOD_STATIC;
import static schilling.richard.dalvik.vm.oo.MethodType.METHOD_VIRTUAL;
import static schilling.richard.r3.app.DebugConstants.DO_LOG;

import java.util.List;

import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.MethodType;
import schilling.richard.dalvik.vm.oo.VerifyException;
import android.util.Log;

import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.instructions.DecodedInstruction;

public abstract class VerifyInvoke implements OpcodeVerifier {
    public static final boolean WARN_INIT = false;
    public static final boolean WARN_UNRESOLVED_PARAMETERS = true;

    /**
     * Alternate version of dvmResolveMethod().
     * <p>
     * Doesn't throw exceptions, and checks access on every lookup.
     * <p>
     * On failure, returns NULL, and sets *pFailure if pFailure is not NULL.
     * 
     * @throws VerifyException
     */
    ClassData.Method dvmOptResolveMethod(ClassDef referrer, int methodIdx,
            MethodType methodType, DecodedInstruction instruction)
            throws VerifyException {
        DexBuffer pDvmDex = instruction.getBuffer();
        List<MethodId> methodIds = pDvmDex.methodIds();
        List<ProtoId> protoIds = pDvmDex.protoIds();
        MethodId pMethodId = methodIds.get(methodIdx); // TODO rename to
                                                       // resMethodId

        Method resMethod = pMethodId.resolveMethod();
        ClassDef resClass = pDvmDex.getDef(pMethodId
                .getDeclaringClassSignature());

        assert (methodType == METHOD_DIRECT || methodType == METHOD_VIRTUAL || methodType == METHOD_STATIC);

        if (DO_LOG)
            Log.v(LOG_TAG, String.format("--- resolving method %u (referrer=%s)",
                    methodIdx, referrer.getSignature()));

        if (resClass.dvmIsInterfaceClass()) {
            /* method is part of an interface; this is wrong method for that */
            Log.w(LOG_TAG, "DexOpt: method is in an interface");
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    "DexOpt: method is in an interface");
        }

        if (methodType == METHOD_STATIC) {

            if (!resMethod.dvmIsStaticMethod()) {
                String msg = String.format(
                        "DexOpt: wanted static, got instance for method %s.%s",
                        resClass.getSignature(), pMethodId.getName());
                Log.d(LOG_TAG, msg);
                throw new VerifyException(
                        VerifyErrorCause.VERIFY_ERROR_GENERIC, msg);
            }

        } else if (methodType == METHOD_VIRTUAL) {

            if (resMethod.dvmIsStaticMethod()) {
                String msg = String.format(
                        "DexOpt: wanted instance, got static for method %s.%s",
                        resClass.getSignature(), pMethodId.getName());
                Log.d(LOG_TAG, msg);
                throw new VerifyException(
                        VerifyErrorCause.VERIFY_ERROR_GENERIC, msg);
            }
        }

        /* see if this is a pure-abstract method */

        // FIXME in the C code pMethodId->nameIdx is what is used and so is
        // resMethod-> name. But aren't they the same? I think this was written
        // by someone else.
        // TODO for the book - explain how to avoid issues where two people are
        // working on the same code base and they're not talking the same
        // language or using the same definitions.

        if (resMethod.dvmIsAbstractMethod() && !resClass.dvmIsAbstractClass()) {
            String msg = String.format(
                    "DexOpt: pure-abstract method '%s' in %s",
                    pMethodId.getName(), resClass.getSignature());

            Log.w(LOG_TAG, msg);
        }

        Log.v(LOG_TAG, String.format("--- found method %d (%s.%s)", methodIdx,
                resClass.getSignature(), pMethodId.getName()));

        /* access allowed? */

        boolean allowed = dvmCheckMethodAccess(referrer, resMethod);
        /*
         * TODO: activate this logic when dvmCheckMethodAccess is implemented if
         * (!allowed) { IF_LOGI() { char* desc =
         * dexProtoCopyMethodDescriptor(&resMethod->prototype);
         * LOGI("DexOpt: illegal method access (call %s.%s %s from %s)",
         * resMethod->clazz->descriptor, resMethod->name, desc,
         * referrer->descriptor); free(desc); } if (pFailure != NULL)pFailure =
         * VERIFY_ERROR_ACCESS_METHOD; return NULL; }
         */

        return resMethod;
    }

    /*
     * Get the "this" pointer from a non-static method invocation. This returns
     * the RegType so the caller can decide whether it needs the reference to be
     * initialized or not. (Can also return kRegTypeZero if the reference can
     * only be zero at this point.) The argument count is in vA, and the first
     * argument is in vC, for both "simple" and "range" versions. We just need
     * to make sure vA is >= 1 and then return vC.
     */
    public static RegisterType getInvocationThis(
            DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, DecodedInstruction pDecInsn)
            throws VerifyException {

        if (pDecInsn.getA() < 1)
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    "VFY: invoke lacks 'this'");

        /* get the element type of the array held in vsrc */
        RegisterType thisType = registerTypes.get(pDecInsn.getC(), instruction);
        if (thisType.typeEnum().category() != RegisterCategory.kTypeCategoryRef)
            throw new VerifyException(
                    VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: tried to get class from non-ref register v%d (type=%d)",
                            pDecInsn.getC(), thisType.toString()));

        return thisType;
    }

    private boolean dvmCheckMethodAccess(ClassDef referrer, Method resMethod) {
        // TODO implement elsewhere. We don't need to do this check right now.
        return true;
    }

    /*
     * See if the method matches the MethodType.
     */
    protected boolean isCorrectInvokeKind(MethodType methodType,
            Method resMethod, MethodId resMethodId) {

        if (resMethod.getMethodIndex() != resMethodId.getMethodIdIndex())
            throw new IllegalArgumentException(
                    "resMethod and resMethodId must refer to the same method.");

        switch (methodType) {
            case METHOD_DIRECT:
                return resMethod.dvmIsStaticMethod();
            case METHOD_STATIC:
                return resMethod.dvmIsStaticMethod();
            case METHOD_VIRTUAL:
            case METHOD_INTERFACE:
                return !resMethod.dvmIsDirectMethod(resMethodId);
            default:
                return false;
        }
    }

}
