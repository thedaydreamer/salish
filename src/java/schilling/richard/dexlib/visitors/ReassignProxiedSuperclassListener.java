
package schilling.richard.dexlib.visitors;

import java.util.Hashtable;
import java.util.List;

import org.gnu.salish.visitors.DexFileListener;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.r3.app.Enhance;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.instructions.DecodedInstruction;

public class ReassignProxiedSuperclassListener extends DexFileListener {

    public static final boolean LOG_PROXY = true;
    public static final String LOG_TAG_PROXY = "R3.Proxy";
    
    private Hashtable<String, String> proxiedClasses;
    private DexBuffer parent = null;
    private SparseArray<String> methodHashes;
    private List<MethodId> methodIds;
    private List<ProtoId> protoIds;
    private int changes = 0;

    public ReassignProxiedSuperclassListener(DexBuffer parent,
            Hashtable<String, String> proxiedClasses,
            SparseArray<String> methodHashes) {
        this.proxiedClasses = proxiedClasses;
        this.parent = parent;
        this.methodHashes = methodHashes;
        this.methodIds = parent.methodIds();
        this.protoIds = parent.protoIds();
    }

    @Override
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId) {
        return true;
    }

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

    @Override
    public boolean doIterateCode() {
        return true;
    }

    /**
     * If an instruction is an invoke-direct, invoke-virtual, or invoke-super,
     * the list of proxy classes are searched. If a proxy class and proxy method
     * is found for the target method call, then the method call is re-directed
     * through the proxy.
     */
    @Override
    public void onInstructionFound(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, int address, DecodedInstruction instruction) {

        String superClass = cDef.getSupertypeSignature();
        String className = cDef.getSignature();

        if (instruction.isInvokeDirect() || instruction.isInvokeVirtual()
                || instruction.isInvokeSuper()) {
            int curIndex = instruction.getIndex();

            MethodId targetMethodId = methodIds.get(curIndex);
            ProtoId targetProtoId = protoIds
                    .get(targetMethodId.getProtoIndex());
            String targetClass = targetMethodId.getDeclaringClassSignature();

            if (superClass.equals(targetClass)) {

                String newMethodHash = VisitorUtil.getHashString(
                        VisitorUtil.getProxySignature(targetClass),
                        targetMethodId.getName(), targetProtoId);

                // call to super constructor.
                int newIdxPos = methodHashes.indexOfValue(newMethodHash);

                /*
                 * If newIdxPos < 0 (not found), then it was not defined in the
                 * DEX file.
                 */
                if (newIdxPos >= 0) {
                    int newIdx = methodHashes.keyAt(newIdxPos);
                    instruction.setIndex(newIdx);
                    changes++;
                    if (LOG_PROXY)
                        Log.d(LOG_TAG_PROXY, String.format(
                                "     %s changed from %s to %s", instruction
                                        .toString(), targetMethodId.toString(),
                                methodIds.get(newIdx).toString()));
                }

            }

        }
    }

    @Override
    public void onClassDefFound(ClassDef cDef) {

        String supertype = cDef.getSupertypeSignature().intern();
        String newSupertype = proxiedClasses.get(supertype);
        ClassDef superDef = parent.getDef(newSupertype);

        int offset = cDef.getOffset();

        DexBuffer.Section section = parent.open(offset + (SizeOf.UINT * 2));
        section.writeInt(superDef.getTypeIndex());
        if (VisitorUtil.LOG_VISITORS)
            Log.d(Enhance.LOG_TAG,
                    String.format("Changed supertype of %s from %s to %s",
                            cDef.getSignature(), supertype, newSupertype));
    }

    @Override
    public boolean shouldVisit(ClassDef cDef) {

        if (proxiedClasses.containsValue(cDef.getSignature().intern()))
            return false;

        return proxiedClasses
                .containsKey(cDef.getSupertypeSignature().intern());

    }

    @Override
    public void onCodeFound(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

        changes = 0;
    }

    @Override
    public void onCodeVisited(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {
        if (changes > 0) {
            int codeOffset = method.getCodeOffset();
            short[] output = iList.encode();
            if (output.length != methodCode.getInstructions().length)
                throw new RuntimeException(String.format(
                        "code length not the same: %d (expected %d)",
                        output.length, methodCode.getInstructions().length));
            if (VisitorUtil.LOG_VISITORS)
                Log.d(Enhance.LOG_TAG, String.format(
                        "     writing %d shorts of code at %d", output.length,
                        codeOffset));

            int codePosition = (SizeOf.USHORT * 4) + (SizeOf.UINT * 2);
            buffer.open(codeOffset + codePosition).write(output);
        }

    }

}
