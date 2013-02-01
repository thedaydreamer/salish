
package schilling.richard.dexlib.visitors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.gnu.salish.visitors.DexFileListener;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * See SuperClassRedefinitionListener. Works similarly, but it just relinks
 * method calls.
 * 
 * @author rschilling
 */
public class HelperMethodRelinkListener extends DexFileListener {

    public static final boolean LOG_RELINKING = true;
    public static final String LOG_TAG = "R3.HelperClassMethodLinking";

    private List<String> classList = new LinkedList<String>();
    private DexBuffer parent = null;
    private List<String> enhancerClasses;
    private List<MethodId> methodIds;

    /**
     * Set to true if an instruction gets modified. Reset to false every time a
     * new code block is found.
     * <p>
     * TODO can we use a CRC calculation instead?
     */
    private boolean codeModified = false;

    public List<String> getChangedClassList() {

        Collections.sort(classList);
        return classList;

    }

    public HelperMethodRelinkListener(DexBuffer parent,
            List<String> enhancerClasses) {
        this.parent = parent;
        this.enhancerClasses = enhancerClasses;
        this.methodIds = parent.methodIds();

    }

    /**
     * Visit all class definitions that have class data associated with them and
     * that are not helper classes.
     */
    @Override
    public boolean shouldVisit(ClassDef cDef) {

        String classDef = cDef.getSignature().intern();

        if (enhancerClasses.contains(classDef))
            return false;

        return (cDef.getClassDataOffset() > 0);

    }

    public void onClassDefFound(ClassDef cDef) {

        // M-T

    }

    public int getTotalChanges() {

        return classList.size();

    }

    /**
     * Visit all methods of the classes that have already been chosen.
     */
    @Override
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId) {

        return true;

    }

    /**
     * Visit all code blocks.
     */
    @Override
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, Code code) {
        return true;
    }

    @Override
    public void onMethodFound(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId) {

        // M-T

    }

    /**
     * Iterate through all the code.
     */
    @Override
    public boolean doIterateCode() {

        return true;

    }

    /*
     * (non-Javadoc)
     * @see
     * org.gnu.salish.visitors.DexFileListener#onCodeFound(com.android.dx.io
     * .DexBuffer, com.android.dx.io.ClassDef,
     * com.android.dx.io.ClassData.Method, com.android.dx.io.MethodId,
     * com.android.dx.io.ProtoId, com.android.dx.io.Code,
     * schilling.richard.dalvik.vm.InstructionList)
     */
    @Override
    public void onCodeFound(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

        codeModified = false;

    }

    /*
     * (non-Javadoc)
     * @see
     * org.gnu.salish.visitors.DexFileListener#onInstructionFound(com.android
     * .dx.io.ClassDef, com.android.dx.io.ClassData.Method,
     * com.android.dx.io.MethodId, com.android.dx.io.ProtoId, int,
     * com.android.dx.io.instructions.DecodedInstruction)
     */
    @Override
    public void onInstructionFound(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, int address, DecodedInstruction instruction) {

        if (!instruction.isInvoke())
            return;

        int methodIndex = instruction.getIndex();
        MethodId targetId = methodIds.get(methodIndex);
        String targetClass = targetId.getDeclaringClassSignature().intern();
        if (enhancerClasses.contains(targetClass))
            // the instruction already invokes a proxy or helper class.
            return;

        String targetClassName = VisitorUtil
                .getTargetClassSignature(targetClass);

        MethodId mapped = parent.findMethodId(targetClassName,
                targetId.getName(), targetId.getProtoIndex());

        if (mapped == null)
            return;

        String defClassSig = cDef.getSignature().intern();
        if (!classList.contains(defClassSig)) {
            classList.add(defClassSig);
            if (LOG_RELINKING)
                Log.i(LOG_TAG, "Class " + defClassSig);
        }

        int proxyIndex = mapped.getMethodIdIndex();

        instruction.setIndex(proxyIndex);
        if (LOG_RELINKING) {
            Log.i(LOG_TAG, "\tinstruction " + instruction.toString() + " from target method "
                    + targetClass + " . " + targetId.getName() + " to " + targetClassName
                    + " . " + mapped.getName());
        }

        codeModified = true;

    }

    @Override
    public void onCodeVisited(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

        if (codeModified) {
            /*
             * write the instruction list back out to the buffer
             */
            int pos = methodCode.getOffset();
            pos += ((SizeOf.USHORT * 4) + (SizeOf.UINT * 2));
            DexBuffer.Section section = parent.open(pos);
            short[] outBytes = iList.encode();
            section.write(iList.encode());
            if (VisitorUtil.LOG_VISITORS)
                Log.d(Enhance.LOG_TAG,
                        String.format(
                                "     Wrote %d shorts back out to buffer at position %d",
                                outBytes.length, pos));
            codeModified = false;
        }

    }
}
