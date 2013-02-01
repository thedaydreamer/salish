
package schilling.richard.dexlib.visitors;

import java.util.List;

import schilling.richard.dalvik.vm.oo.util.ClassPathUtil;
import schilling.richard.dexlib.proxy.DexFileProxyGenerator;
import schilling.richard.r3.app.Enhance;

import com.android.dx.io.ProtoId;

public final class VisitorUtil {
    /**
     * A convenience constant that can be used to turn on and off logging of
     * visitors. See the individual visitor class for usage.
     */
    public static final boolean LOG_VISITORS = false;

    private VisitorUtil() {
    }

    /**
     * Converts a perfectly good signature into a proxied signature.
     * 
     * @param signauture the signature to converd
     * @return the same signature but with a class name prepended with
     *         proxyPrefix.
     */
    public static String getProxySignature(String signature) {

        int pos = signature.lastIndexOf('/') + 1;
        StringBuilder result = new StringBuilder();

        result.append(signature.substring(0, pos));
        result.append(Enhance.PROXY_PREFIX);
        result.append(ClassPathUtil.getClassName(signature));
        return result.toString().intern();

    }

    public static String getHashString(String declaringClass,
            String methodName, ProtoId pId) {

        List<String> methodParameters = pId.getParameters();
        StringBuilder builder = new StringBuilder();
        builder.append(declaringClass).append("!");
        builder.append(methodName).append("!");
        builder.append(pId.getReturnTypeString()).append("!");

        for (int i = 0; i < methodParameters.size(); i++) {
            builder.append(methodParameters.get(i));
        }

        return builder.toString().intern();

    }

    public static String getHelperClassSignature(String signature) {

        StringBuilder builder = new StringBuilder();
        builder.append(DexFileProxyGenerator.HELPER_PROXY_PREFIX).append("/")
                .append(signature.substring(1));

        return builder.toString().intern();
    }
    
    public static String getTargetClassSignature(String signature) {

        StringBuilder builder = new StringBuilder();
        builder.append(DexFileProxyGenerator.TARGET_PROXY_PREFIX).append("/")
                .append(signature.substring(1));

        return builder.toString().intern();
    }
}
