
package schilling.richard.dexlib.visitors;

import java.util.LinkedList;
import java.util.List;

import org.gnu.salish.visitors.DexFileListener;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dexlib.proxy.AUTGenerator;
import schilling.richard.dexlib.proxy.ProxyMap;
import schilling.richard.r3.app.Enhance;
import android.util.Log;

import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Discovers what classes are inaccessable because they have inaccessable inner
 * classes defined.
 * 
 * @author rschilling
 */
public class InaccessableInnerClassListener extends DexFileListener {

    public static final String LOG_TAG = "InaccessableInnerClassListener";

    public final List<String> inaccessableClasses = new LinkedList<String>();

    @Override
    public void onClassDefFound(ClassDef cDef) {

        int accessFlags = cDef.getAccessFlags();

        // taken from DexMaker.declare

        if ((accessFlags & Enhance.UNSUPPORTED_CLASS_FLAGS) != 0) {
            String sig = cDef.getSignature();

            // shouldVisit guarantees that the signature has a '$' character.
            int idx = sig.indexOf('$');

            String enclosingSig = sig.substring(0, idx + 1);
            enclosingSig = enclosingSig.intern();

            String enclosingClassSig = sig.substring(0, idx) + ";";
            enclosingClassSig = enclosingClassSig.intern();

            if (!inaccessableClasses.contains(enclosingSig))
                inaccessableClasses.add(enclosingSig);

            if (!inaccessableClasses.contains(enclosingClassSig))
                inaccessableClasses.add(enclosingClassSig);
            
        }

    }

    @Override
    public boolean shouldVisit(ClassDef cDef) {

        // must not be an array class
        if (cDef.dvmIsArrayClass())
            return false;

        String signature = cDef.getSignature();

        // only looking for inner classes.
        if (signature.indexOf('$') == -1)
            return false;

        return true;
    }

    public void appendResultsTo(List<String> list) {

        if (list == null)
            return;

        for (String s : inaccessableClasses) {
            Log.d(AUTGenerator.LOG_TAG_EXCLUDED,
                    String.format("%s inaccessable - access flags unsupported.", s));

        }

        list.addAll(inaccessableClasses);

    }
}
