
package schilling.richard.dexlib.visitors;

import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dexlib.proxy.ProxyMap;
import android.util.Log;
import android.util.SparseArray;

import com.android.dx.dex.SizeOf;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.rop.code.AccessFlags;

/**
 * Visits all classes, methods and code instructions to relink indexes
 * 
 * @author rschilling
 */
public class CodeRelinkingListener extends ProxyMethodListener {

    public static final boolean LOG_RELINK = true;
    public static final String LOG_TAG = "R3.Relink";
    /**
     * old and new hashes for classes. Contains only class def hashes for
     * classes that have been proxied. key - unproxied hash, value - proxied
     * hash
     */
    private final ProxyMap classLinks;

    /**
     * old and new hashes for methods. Contains only method def hashes for
     * methods that have been proxied. key - unproxied hash, value - proxied
     * hash
     */
    private final ProxyMap methodLinks;

    /**
     * Hashes for all methods in the merged DEX file.
     */
    private final SparseArray<String> methodHashtable;

    /**
     * Hashes for all class defs in the merged DEX file.
     */
    private final SparseArray<String> classHashtable;

    /*
     * Store registers that may contain objects that should not be relinked.
     */
    private LinkedList<Integer> unavailableRegs = new LinkedList<Integer>();
    private int changes = 0;
    private int totalChanges = 0;
    private Integer vThis = null;

    public CodeRelinkingListener(ProxyMap classLinks, ProxyMap methodLinks,
            SparseArray<String> methodHashtable,
            SparseArray<String> classHashtable) {

        this.classLinks = classLinks;
        this.methodLinks = methodLinks;
        this.methodHashtable = methodHashtable;
        this.classHashtable = classHashtable;

    }

    /**
     * Returns the number of instructions changed.
     * 
     * @return changed instruction count.
     */
    public int getChanges() {
        return changes;
    }

    public int getTotalChanges() {
        return totalChanges;
    }

    /**
     * Addresses that won't get linked.
     */
    private List<Integer> unavailableAddresses = new LinkedList<Integer>();

    @Override
    public void onCodeFound(DexBuffer buffer, ClassDef cDef, Method method,
            MethodId mId, ProtoId pId, Code methodCode, InstructionList iList) {

        unavailableAddresses.clear();
        unavailableRegs.clear();

        // calculate this pointer vThis
        int regSz = methodCode.getRegistersSize();
        int regIns = methodCode.getInsSize();

        int vFirstParam = regSz - regIns;

        if (!method.dvmIsStaticMethod())
            vThis = Integer.valueOf(vFirstParam);

        for (int i = vFirstParam; i < methodCode.getRegistersSize(); i++) {
            // make parameter registers unavailable for linking.
            unavailableRegs.add(Integer.valueOf(i));
        }

        List<MethodId> methodIds = buffer.methodIds();
        List<DecodedInstruction> newInstances = new LinkedList<DecodedInstruction>();
        List<DecodedInstruction> invokeConstructor = new LinkedList<DecodedInstruction>();

        /*
         * Match up new instance calls with invoke <init> calls. Notice the use
         * of invokeConstructor.add(0 .... This puts the <init> call at the top
         * of the list so it will appear in the same list position as the
         * new-instance instrucation that it matches.
         */
        for (int i = 0; i < iList.size(); i++) {

            DecodedInstruction instruction = iList.valueAt(i);

            if (instruction.isNewInstance())
                newInstances.add(instruction);

            if (instruction.isInvoke()
                    && (newInstances.size() > invokeConstructor.size())) {

                int methodIdx = instruction.getIndex();

                if (methodIds.get(methodIdx).getName().equals("<init>"))
                    invokeConstructor.add(0, instruction);

            }

        }

        if (newInstances.size() != invokeConstructor.size())
            throw new IllegalStateException(String.format(
                    "stack mismatch %d, %d in %s of %s", newInstances.size(),
                    invokeConstructor.size(), mId.getName(),
                    cDef.getSignature()));

        for (int i = 0; i < newInstances.size(); i++) {
            DecodedInstruction ni = newInstances.get(i);
            DecodedInstruction ic = invokeConstructor.get(i);

            int methodIdx = ic.getIndex();
            MethodId cMethodId = methodIds.get(methodIdx);
            if (cMethodId.isResolvable()) {
                Method cMethod = cMethodId.resolveMethod();
                int cMethodAccessFlags = cMethod.getAccessFlags();
                if (((cMethodAccessFlags & AccessFlags.ACC_PRIVATE) != 0)
                        || ((cMethodAccessFlags & AccessFlags.ACC_PROTECTED) != 0)) {

                    // address can come from i or from the instruction
                    // itself.

                    // add the instruction for current instruction and
                    // instructor call to unavailable addresses.
                    int iAddress = ni.getAddress();
                    int cAddress = ic.getAddress();
                    if (VisitorUtil.LOG_VISITORS)
                        Log.d(Enhance.LOG_TAG,
                                String.format(
                                        "unavailable instructions: %s and %s (private/protected constructor) in %s of %s",
                                        ni.toString(), ic.toString(),
                                        mId.getName(), cDef.getSignature()));

                    unavailableAddresses.add(iAddress);
                    unavailableAddresses.add(cAddress);

                }
            }

        }

        changes = 0;

    }

    /**
     * Mark the destination register as unavailable.
     * 
     * @param instruction
     */
    private void ifObjectGet(DecodedInstruction instruction) {
        if (instruction.isObjectGet()) {
            int vDest = instruction.getA();
            if (!unavailableRegs.contains(vDest))
                unavailableRegs.add(Integer.valueOf(vDest));
        }
    }

    /**
     * If the source of a move is from an unavailable register, than the
     * destination register is unavailable too.
     * 
     * @param instruction
     */
    private void ifMoveObject(DecodedInstruction instruction) {
        if (instruction.isMoveObject() && vThis != null) {

            Integer vSource = Integer.valueOf(instruction.getB());
            Integer vDest = Integer.valueOf(instruction.getA());

            // vThis and destinations of object-get will be in unavailableRegs
            // vThis will not be null
            if (unavailableRegs.contains(vSource)
                    && !unavailableRegs.contains(vDest))
                unavailableRegs.add(vDest);

        }

        if (instruction.isMoveResultObject()) {
            // the destination is not available.
            Integer vDest = Integer.valueOf(instruction.getA());
            if (!unavailableRegs.contains(vDest))
                unavailableRegs.add(vDest);

        }

    }

    // classLinks (class-to-class hash), methodLinks (method-to-method hash),
    // methodHashes (from new file - lookup) and classHashes (from new file -
    // lookup)

    private void ifNewInstance(String className, String methodName,
            DecodedInstruction instruction) {
        if (instruction.isNewInstance()) {

            /*
             * The destination register is now a candidate for re-linking.
             */
            Integer vDest = Integer.valueOf(instruction.getA());
            while (unavailableRegs.contains(vDest)) {
                int idx = unavailableRegs.indexOf(vDest);
                unavailableRegs.remove(idx);
            }

            /*
             * if the class index has been proxied, then replace the index with
             * the proxy index.
             */
            int oldIndex = instruction.getIndex();

            if (classHashtable.indexOfKey(oldIndex) < 0)
                // the class was not defined in this dex file or is not
                // available for linking.
                return;

            String oldClassSignature = classHashtable.get(oldIndex);

            if (!classLinks.containsKey(oldClassSignature))
                // no proxy exists for the class.
                return;

            String proxyClassSignature = classLinks.get(oldClassSignature);

            int proxyPosition = classHashtable
                    .indexOfValue(proxyClassSignature);
            if (proxyPosition < 0) {
                if (VisitorUtil.LOG_VISITORS)
                    Log.w(Enhance.LOG_TAG,
                            String.format(
                                    "skip class linking for instruction %s: proxy class %s not found in class hashtable.",
                                    instruction.toString(), proxyClassSignature));
            } else {
                int newIndex = classHashtable.keyAt(proxyPosition);

                instruction.setIndex(newIndex);

                changes++;
                if (VisitorUtil.LOG_VISITORS)
                    Log.d(Enhance.LOG_TAG,
                            String.format(
                                    "%d->%d changing new instance @ address %d: in method %s %s",
                                    oldIndex, newIndex,
                                    instruction.getAddress(), className,
                                    methodName));
            }

        }
    }

    /**
     * Visit all methods that are not proxy methods. The values of methodLinks
     * are all proxy methods.
     * 
     * @see schilling.richard.dexlib.visitors.ProxyMethodListener#shouldVisit(com
     *      .android.dx.io.ClassDef, com.android.dx.io.ClassData.Method,
     *      com.android.dx.io.MethodId, com.android.dx.io.ProtoId)
     */
    @Override
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId) {

        String methodHash = VisitorUtil.getHashString(
                mId.getDeclaringClassSignature(), mId.getName(), pId);

        if (mId.getName().equals("<clinit>"))
            return false;

        return !methodLinks.containsValue(methodHash) ? true : false;
    }

    /**
     * Visit all classes that are not proxy classes. The classLinks member
     * values contain the hashes of all proxy classes.
     * 
     * @see schilling.richard.dexlib.visitors.ProxyMethodListener#shouldVisit(com
     *      .android.dx.io.ClassDef)
     */
    @Override
    public boolean shouldVisit(ClassDef cDef) {
        // if the class is a proxy class ignore it.

        return !classLinks.containsValue(cDef.getSignature().intern()) ? true
                : false;

    }

    /**
     * If the instruction is an invoke instruction, perform some re-linking. if
     * the instruction is not an invoke instruction, then do nothing.
     * 
     * @param className the name of the class where the instruction is found.
     * @param methodName the name of the method where the instruction is found.
     * @param instruction the instruction.
     */
    private void ifInvoke(String className, String methodName,
            DecodedInstruction instruction) {

        if (!instruction.isInvoke())
            return;

        boolean relink = true;

        /*
         * if the method is non-static and the target object is this, then don't
         * re-link
         */
        if (vThis != null) {

            if (instruction.isInvokeInterface()) {
                relink = false;
            } else {
                int vA = instruction.getA();
                for (int vT : unavailableRegs) {
                    if (vA == vT)
                        relink = false;
                }
            }

        }

        if (relink) {

            /*
             * if the class index has been proxied, then replace the index with
             * the proxy index.
             */
            int oldIndex = instruction.getIndex();

            if (methodHashtable.indexOfKey(oldIndex) < 0)
                // the class was not defined in this dex file.
                return;

            String oldMethodSignature = methodHashtable.get(oldIndex);

            if (!methodLinks.containsKey(oldMethodSignature))
                // no proxy exists for the method.
                return;

            String proxyMethodSignature = methodLinks
                    .get(oldMethodSignature);

            int proxyPosition = methodHashtable
                    .indexOfValue(proxyMethodSignature);

            if (proxyPosition < 0) {
                if (VisitorUtil.LOG_VISITORS)
                    Log.w(Enhance.LOG_TAG,
                            String.format(
                                    "skip method linking for instruction %s: proxy method %s not found in method hashtable.",
                                    instruction.toString(),
                                    proxyMethodSignature));

            } else {
                int newIndex = methodHashtable.keyAt(proxyPosition);

                instruction.setIndex(newIndex);

                changes++;
                if (LOG_RELINK)
                    Log.d(LOG_TAG,
                            String.format(
                                    "%d->%d changing invoke @ address %d: in method %s %s. target changed %s -> %s",
                                    oldIndex, newIndex,
                                    instruction.getAddress(), className,
                                    methodName, oldMethodSignature, proxyMethodSignature));
            }

        }

    }

    @Override
    public void onInstructionFound(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, int address, DecodedInstruction instruction) {

        String className = cDef.getSignature();
        String methodName = mId.getName();

        if (unavailableAddresses.contains(address)) {
            if (VisitorUtil.LOG_VISITORS)
                Log.d(Enhance.LOG_TAG,
                        String.format(
                                "Skipping unavailable instruction %s of method %s in %s",
                                instruction.toString(), methodName, className));
            return;
        }

        ifObjectGet(instruction);
        ifMoveObject(instruction);
        ifNewInstance(className, methodName, instruction);
        ifInvoke(className, methodName, instruction);

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
                        "writing %d shorts of code at %d", output.length,
                        codeOffset));

            int codePosition = (SizeOf.USHORT * 4) + (SizeOf.UINT * 2);
            buffer.open(codeOffset + codePosition).write(output);
        }

        totalChanges += changes;

    }

    @Override
    /**
     * Visit all code
     */
    public boolean shouldVisit(ClassDef cDef, Method method, MethodId mId,
            ProtoId pId, Code code) {
        return true;
    }

    @Override
    public void onClassDefFound(ClassDef cDef) {
        // M-T

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
}
