
package schilling.richard.dexlib.visitors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.gnu.salish.visitors.DexFileListener;

import schilling.richard.dalvik.vm.InstructionList;
import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.instructions.DecodedInstruction;

/**
 * Builds up a list of classes defined in the enhancer DEX file. The strings in
 * classList are interned.
 * 
 * @author rschilling
 */
public class SuperClassRedefinitionListener extends DexFileListener {

    public static final boolean LOG_REDEFINITION = true;
    public static final String LOG_TAG = "R3.SuperclassDefinition";

    private List<String> classList = new LinkedList<String>();
    private DexBuffer parent = null;
    private List<String> enhancerClasses;
    private SparseIntArray platformMethodMap;

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

    public SuperClassRedefinitionListener(DexBuffer parent,
            List<String> enhancerClasses, SparseIntArray platformMethodMap) {
        this.parent = parent;
        this.enhancerClasses = enhancerClasses;
        this.platformMethodMap = platformMethodMap;
    }

    /**
     * A class definition is visited if it's super class has a helper class.
     */
    @Override
    public boolean shouldVisit(ClassDef cDef) {

        if (enhancerClasses.contains(cDef.getSignature().intern()))
            // also means that any compiler generated classes will be skipped by
            // this visitor.
            return false;

        if (cDef.getClassDataOffset() <= 0)
            return false;

        String superType = cDef.getSupertypeSignature();
        String check = VisitorUtil.getHelperClassSignature(superType);
        if (!enhancerClasses.contains(check.toString().intern()))
            return false;

        if (LOG_REDEFINITION)
            Log.d(LOG_TAG,
                    String.format("Superclass mapping for %s: %s -> %s",
                            cDef.getSignature(), superType, check));
        return true;

    }

    public void onClassDefFound(ClassDef cDef) {

        // determine the mapped class identifier
        String superType = cDef.getSupertypeSignature();
        String mapToType = VisitorUtil.getHelperClassSignature(superType);
        ClassDef mapToDef = parent.getDef(mapToType);

        int offset = cDef.getOffset();
        DexBuffer.Section section = parent.open(offset + (SizeOf.UINT * 2));
        section.writeInt(mapToDef.getTypeIndex());
        classList.add(cDef.getSignature().intern());

        if (LOG_REDEFINITION)
            Log.d(LOG_TAG,
                    String.format("   %d -> %d", cDef.getTypeIndex(),
                            mapToDef.getTypeIndex()));

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

        if (instruction.isInvoke()) {
            if (instruction.isInvokeSuper() || instruction.isInvokeDirect()) {
                int methodIndex = instruction.getIndex();
                if (platformMethodMap.indexOfKey(methodIndex) < 0)
                    return;

                int proxyIndex = platformMethodMap.get(methodIndex);
                instruction.setIndex(proxyIndex);
                if (VisitorUtil.LOG_VISITORS)
                    Log.d(Enhance.LOG_TAG,
                            String.format(
                                    "     Method index mapped %d -> %d on instruction %s in method %s of %s",
                                    methodIndex, proxyIndex,
                                    instruction.toString(), mId.getName(),
                                    cDef.getSignature()));
                codeModified = true;
            }

        }

    }

    /*
     * (non-Javadoc)
     * @see
     * org.gnu.salish.visitors.DexFileListener#onCodeVisited(com.android.dx.
     * io.DexBuffer, com.android.dx.io.ClassDef,
     * com.android.dx.io.ClassData.Method, com.android.dx.io.MethodId,
     * com.android.dx.io.ProtoId, com.android.dx.io.Code,
     * schilling.richard.dalvik.vm.InstructionList)
     */
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
