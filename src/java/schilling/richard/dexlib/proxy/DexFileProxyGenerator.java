
package schilling.richard.dexlib.proxy;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.gnu.salish.visitors.DexBufferVisitor;

import schilling.richard.dexlib.visitors.MethodHashingListener;
import schilling.richard.dexlib.visitors.VisitorUtil;
import android.util.Log;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.rop.code.AccessFlags;
import com.google.dexmaker.DexMaker;
import com.google.dexmaker.Local;
import com.google.dexmaker.TypeId;

/**
 * Reads a DEX filea Generates DEX files that contain proxy class and methods
 * for another DEX file.
 * 
 * @author rschilling
 */
public class DexFileProxyGenerator {
    public static final String LOGGER_CLASS = "Lschilling/richard/finnr/logger/Logger;";
    public static final String LOGGER_FUNCTION = "log";
    /**
     * Prefix of all helper classes that are superclasses.
     */
    public static final String HELPER_PROXY_PREFIX = "Lsuperclass";

    /**
     * Prefix of all helper classes that contain target methods to map to.
     */
    public static final String TARGET_PROXY_PREFIX = "Ltargetclass";
    public static final String LOG_TAG = "DexFileProxyGenerator";
    public static final boolean LOG_PROXIES = true;

    /**
     * Stores class signatures that are proxied. The key is the original class
     * signature and the value is the proxy calss signature.
     */
    private ProxyMap proxyClasses;

    /**
     * Stores method key values that are proxied. The key is the original method
     * key and the value is the proxy key signature. A method key is a unique
     * string that defines a method as implemented when compared to all other
     * methods.
     */
    private ProxyMap proxyMethods;

    /**
     * Assigned a value by hashMethods;
     */
    private List<Method> chosenMethods;

    /**
     * The buffer that proxies are generated for.
     */
    private DexBuffer buffer;

    /**
     * The classes that should not have proxies generated for them. These are
     * also the helper classes that are found in the enhancer.dex file.
     */
    private List<String> helperClasses;

    private List<String> excludedPackages;

    /**
     * Creates a new proxy generator. helperClassList is the list of classes
     * that will be bypassed. Any class starting with a string from
     * excludedPackagesList will also be excluded.
     * 
     * @param buffer the buffer to generate proxies for.
     * @param helperClassList the list of helper classes that are excluded from
     *            processing.
     * @param excludedPackagesList the list of packages that are excluded from
     *            processing.
     */
    public DexFileProxyGenerator(DexBuffer buffer,
            List<String> helperClassList, List<String> excludedPackagesList) {
        this.buffer = buffer;
        this.helperClasses = helperClassList;
        excludedPackages = excludedPackagesList;

    }

    public ProxyMap getProxyMethodMap() {
        return proxyMethods;
    }

    public ProxyMap getProxyClassMap() {
        return proxyClasses;
    }

    /**
     * fills proxyMethods and links objects. When this function completes, the
     * links table contains keys with the target methods and the values contains
     * proxy methods. Sets the links hashtable.
     * 
     * @param buffer
     * @throws InterruptedException
     */
    private void calculateHashes() throws InterruptedException {

        DexBufferVisitor visitor = new DexBufferVisitor(buffer);
        MethodHashingListener listener = new MethodHashingListener();
        listener.excludedClasses = helperClasses;
        listener.excludedPackages = excludedPackages;
        visitor.registerListener(listener);
        visitor.visitClasses();

        proxyClasses = listener.getClassLinkTable();
        proxyMethods = listener.getMethodLinkTable();
        chosenMethods = listener.getChosenMethods();

    }

    /**
     * Generate the DexBuffer with proxy information in it. DexBuffer contains
     * just the proxy classes.
     * 
     * @throws IOException if an error occurred creating the DexBuffer file.
     * @throws InterruptedException
     * @return a dex buffer with proxy classes and methods in it.
     */
    public DexBuffer generateProxies() throws IOException, InterruptedException {

        calculateHashes();

        List<MethodId> methodIds = buffer.methodIds();
        List<ProtoId> protoIds = buffer.protoIds();
        List<String> declaredTypes = new LinkedList<String>();
        DexMaker maker = new DexMaker();

        /*
         * Iterate through the list of methods selected to be proxied.
         */
        for (Method m : chosenMethods) {

            throwIfInterrupted();

            MethodId mId = methodIds.get(m.getMethodIndex());

            ProtoId pId = protoIds.get(mId.getProtoIndex());
            List<String> methodParameters = pId.getParameters();

            // method declaring class will become the superclass
            String superClassType = mId.getDeclaringClassSignature();
            String classHash = mId.getDeclaringClassSignature();
            String methodName = mId.getName();
            String methodHash = VisitorUtil.getHashString(classHash,
                    methodName, pId);

            ClassDef cDef = buffer.getDef(superClassType);
            if (cDef == null) {
                Log.i(LOG_TAG, String.format("Super class %s is not found in the class table.",
                        superClassType));
                continue;
            }

            String classType = VisitorUtil.getProxySignature(superClassType);

            // create type ids for the new class
            TypeId<?> proxyType = TypeId.get(classType);
            TypeId<?> targetType = TypeId.get(superClassType);

            // return type of the function
            TypeId<?> returnType = TypeId.get(pId.getReturnTypeString());

            //

            List<TypeId<?>> parameterTypes = new LinkedList<TypeId<?>>();

            for (String param : methodParameters) {
                parameterTypes.add(TypeId.get(param));
            }

            if (!declaredTypes.contains(classType)) {

                /*
                 * sanity
                 */
                if (maker == null)
                    throw new NullPointerException("maker is null");
                if (targetType == null)
                    throw new NullPointerException("maker is null");

                maker.declare(proxyType, "R3.generated", cDef.getAccessFlags(),
                        targetType);
                declaredTypes.add(classType);
            }

            TypeId<?>[] parameterTypesArray = parameterTypes
                    .toArray(new TypeId<?>[0]);

            // generate a method with the same name as the super class
            // method
            com.google.dexmaker.MethodId<?, ?> targetMethodId = targetType
                    .getMethod(returnType, methodName, parameterTypesArray);

            com.google.dexmaker.MethodId<?, ?> proxyMethodId = proxyType
                    .getMethod(returnType, methodName, parameterTypesArray);

            com.google.dexmaker.Code code = maker.declare(proxyMethodId,
                    m.getAccessFlags() | AccessFlags.ACC_SYNTHETIC);

            addLogCode(code, returnType, parameterTypesArray, m,
                    targetMethodId, proxyType, targetType, methodName,
                    classHash, methodHash);

            if (LOG_PROXIES)
                Log.i(LOG_TAG, String.format("Proxy created for method %s", methodHash));

        }

        throwIfInterrupted();
        return new DexBuffer(maker.generate());

    }

    private void throwIfInterrupted() throws InterruptedException {

        if (Thread.interrupted())
            throw new InterruptedException("processing cancelled");

    }

    /**
     * Adds logging code to the function.
     */
    private void addLogCode(com.google.dexmaker.Code code,
            TypeId<?> returnType, TypeId<?>[] parameterTypesArray, Method m,
            com.google.dexmaker.MethodId<?, ?> targetMethodId,
            TypeId<?> proxyType, TypeId<?> targetType, String methodName,
            String classHash, String methodHash) {

        Local<String> cHash = code.newLocal(TypeId.STRING);
        Local<String> mHash = code.newLocal(TypeId.STRING);

        Local returnTypeLocal = null;
        if (!returnType.name.equals(TypeId.VOID.name))
            returnTypeLocal = code.newLocal(returnType);

        Local<?>[] parameterLocals = new Local<?>[parameterTypesArray.length];
        for (int localIdx = 0; localIdx < parameterLocals.length; localIdx++) {
            parameterLocals[localIdx] = code.getParameter(localIdx,
                    parameterTypesArray[localIdx]);
        }

        code.loadConstant(cHash, classHash);
        code.loadConstant(mHash, methodHash);
        TypeId<?> cHashTypeId = TypeId.get(LOGGER_CLASS);

        if (m.dvmIsStaticMethod()) {
            code.invokeStatic(targetMethodId, returnTypeLocal, parameterLocals);

            com.google.dexmaker.MethodId<?, Void> loggerMethod = cHashTypeId
                    .getMethod(TypeId.VOID, LOGGER_FUNCTION, TypeId.STRING);
            code.invokeStatic(loggerMethod, null, mHash);

        } else {
            Local thisLocal = code.getThis(proxyType);

            /*
             * calls to super in constructor: invoke-direct calls to super in
             * non-constructor: invoke-super
             */
            if ((m.getAccessFlags() & AccessFlags.ACC_CONSTRUCTOR) != 0) {
                code.invokeDirect(targetMethodId, returnTypeLocal, thisLocal,
                        parameterLocals);
            } else {
                code.invokeSuper(targetMethodId, returnTypeLocal, thisLocal,
                        parameterLocals);
            }

            com.google.dexmaker.MethodId<?, Void> loggerMethod = cHashTypeId
                    .getMethod(TypeId.VOID, LOGGER_FUNCTION, TypeId.OBJECT,
                            TypeId.STRING);
            code.invokeStatic(loggerMethod, null, thisLocal, mHash);

        }

        // call a utility function that dumps log output - a test for
        // now.

        // return values

        if (returnType.name.equals(TypeId.VOID.name))
            code.returnVoid();
        else
            code.returnValue(returnTypeLocal);

    }
}
