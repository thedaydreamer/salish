
package com.android.dx.merge;

import static org.gnu.salish.debug.util.DebugConstants.DO_LOG;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleHi;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongHi;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;

import java.util.LinkedList;
import java.util.List;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.DexSwapVerify;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;
import schilling.richard.dexlib.inject.InjectException;

import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.code.CatchBuilder;
import com.android.dx.dex.code.OutputCollector;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassData.Method;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.Code.Try;
import com.android.dx.io.IndexType;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.dexbuffer.DexBuffer;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.DecodedInstructionFactory;
import com.android.dx.io.instructions.PackedSwitchPayloadDecodedInstruction;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.merge.Updater.BaseUpdater;
import com.android.dx.rop.code.RegisterSpecList;

/**
 * A class that contains all the objects needed to define a method.
 * 
 * @author rschilling
 */
@Deprecated
public class MethodDefinition {

    /**
     * When a method with one of these names is processed the program
     * breakpoints.
     */
    public static final String[] BREAKPOINT_METHODS = {
            "onDestroy", "onKeyUp"
    };
    public static final String LOG_TAG = "MethodDefinition";
    public static final String LOG_TAG_INSTRUCTIONS = "MethodInstructions";
    public static final boolean LOG_INSTRUCTIONS = true;
    public static final boolean LOG_REGISTERS = true;
    public static final String LOG_TAG_MAP = "RegisterMap";

    /**
     * If true injected functions will try to use registers that are in the
     * target methods. If false, all injected functions are given their own
     * registers.
     */
    public static final boolean RESUSE_REGISTERS = false;
    private static final String LOG_TAG_OPCODE = "Opcode";

    /**
     * The buffer that the members originate from.
     */
    private DexBuffer buffer;

    /**
     * The Method that is associated with the methodId passed to the
     * constructor.
     */
    private Method method;

    /**
     * The code associated with this method.
     */
    private Code code;

    /**
     * The index into the method id pool passed to the constructor.
     */
    private int id;

    /**
     * The MethodId that is associated with the methodId passed to the
     * constructor.
     */
    private MethodId methodId;

    /**
     * The ProtoId that is associated with the methodId passed to the
     * constructor.
     */
    private ProtoId protoId;

    /**
     * The ClassDef of the class that defines the methodId passed to the
     * constructor.
     */
    private ClassDef classDef;

    /**
     * The ClassData of the class that defines the methodId passed to the
     * constructor.
     */
    private ClassData classData;

    /**
     * Creates a method definition using a MethodId index into the MethodId pool
     * 
     * @param buffer the buffer to read data form.
     * @param methodId the method id index.
     */
    public MethodDefinition(DexBuffer buffer, int methodId) {
        if (buffer == null)
            throw new IllegalArgumentException("buffer cannot be null");

        if (methodId < 0 || methodId >= buffer.methodIds().size())
            throw new IllegalArgumentException(String.format(
                    "methodId %d must be between 0 and %d", methodId, buffer
                            .methodIds().size()));

        this.buffer = buffer;
        this.id = methodId;

        if (hasClassData())
            original = (Code) code().clone();
    }

    /**
     * Returns the index passed to the constructor.
     * 
     * @return the MethodID index passed to the constructor.
     */
    public int getMethodIdIndex() {
        return id;
    }

    /**
     * Returns the method ID object for the method id index passed to the
     * constructor.
     * 
     * @return the method id object.
     */
    public MethodId methodId() {
        if (methodId == null)
            methodId = buffer.methodIds().get(id);

        return methodId;
    }

    /**
     * Returns the ClassDef of the class that defines the method.
     * 
     * @return the method's class definition.
     */
    public ClassDef classDef() {
        if (classDef == null)
            classDef = buffer.getDef(methodId().getDeclaringClassSignature());

        if (classDef == null) {
            if (DO_LOG)
                Log.w(LOG_TAG, String.format("Class %s not found.", methodId()
                        .getDeclaringClassSignature()));
        }

        return classDef;
    }

    /**
     * Returns true if the method has class data associated with it.
     * 
     * @return true if a call to classData will complete without throwing an
     *         exception or false otherwise.
     */
    public boolean hasClassData() {

        ClassDef cDef = classDef();
        if (cDef == null)
            return false;

        return cDef.getClassDataOffset() > 0;
    }

    /**
     * Returns the buffer that the method information is read from.
     * 
     * @return the method buffer that is passed to the constructor.
     */
    public DexBuffer buffer() {
        return buffer;
    }

    /**
     * Returns the ClassData object associated with the method if it exists. If
     * there is not class data an exception is thrown.
     * 
     * @return the class data for the class where the method is defined.
     * @throws IllegalArgumentException from buffer.readClassData(ClassDef) if
     *             there is no class data.
     */
    public ClassData classData() {
        if (classData == null)
            classData = buffer.readClassData(classDef());

        return classData;
    }

    /**
     * Returns the proto id from the method prototype associated with the
     * method.
     * 
     * @return the method's ProtoId object.
     */
    public ProtoId protoId() {
        if (protoId == null)
            protoId = buffer.protoIds().get(methodId().getProtoIndex());

        return protoId;
    }

    /**
     * Returns the method object assocaited with the methodId.
     * 
     * @return the Method object associated with the method Id.
     */
    public Method method() {
        if (method == null)
            method = methodId.resolveMethod();

        return method;
    }

    public Code code() {
        if (!hasClassData())
            throw new IllegalStateException(
                    String.format("method %s in class %s has no class data.",
                            methodId().getName(), methodId()
                                    .getDeclaringClassSignature()));

        if (code == null)
            code = buffer.readCode(method());

        return code;

    }

    public short[] instructions() {

        return code().getInstructions();
    }

    /**
     * Returns a new InstructionList instance with decoded instructions for the
     * underlying Code object.
     * 
     * @return a new instance for InstructionList
     */
    public InstructionList instructionsDecodedAsList() {
        return code().decodeAllAsList(this);
    }

    /**
     * Returns true if this method is one of the methods that gets injected
     * during testing.
     * 
     * @return true if this function will be injected by DexInjectedMerger.
     */
    public boolean isInjectMethod() {

        return false;

    }

    public String name() {
        return methodId().getName();
    }

    /**
     * Returns true if this method calls
     * 
     * @param statusMethod
     * @return
     */
    public boolean callsGetStatusLine(MethodDefinition statusMethod) {

        int methodId = statusMethod.getMethodIdIndex();

        Code code = code();
        InstructionList instructions = code.decodeAllAsList();

        int sz = instructions.size();
        for (int i = 0; i < sz; i++) {
            DecodedInstruction instruction = instructions.valueAt(i);
            if (instruction.getOpcode() != Opcodes.INVOKE_VIRTUAL)
                continue;

            if (instruction.getIndex() != methodId)
                continue;

            return true;
            // this is a couple of disassembled instructions for getting status
            // codes.
            /*
             * 03f9d6: 7210 f319 0d00 |004d: invoke-interface {v13},
             * Lorg/apache/http/StatusLine;.getStatusCode:()I // method@19f3
             * 03f9dc: 0a0c |0050: move-result v12 interpretation: the
             * invoke-interface call will indicate the interface and function id
             * that is associated with the status code. The interface class is
             * Lorg/apache/http/StatusLine; The function name is getStatusCode()
             * The function shorty shoule be I
             */

        }

        return false;
    }

    /**
     * Returns the addresses after calls to getStatusLine where insertion should
     * occur. The length of the result list will be the number of times that
     * code injections will occur to track HTTP request responses.
     * 
     * @param statusMethod
     * @return a list of addresses in the method where
     */
    public List<Integer> getStatusLinePostAddresses(
            MethodDefinition statusMethod) {

        int invokedMethodId = statusMethod.getMethodIdIndex();

        List<Integer> result = new LinkedList<Integer>();

        if (callsGetStatusLine(statusMethod)) {

            Code code = code();
            InstructionList instructions = code.decodeAllAsList();

            int sz = instructions.size();
            for (int i = 0; i < sz; i++) {
                DecodedInstruction instruction = instructions.valueAt(i);
                if (instruction.getOpcode() != Opcodes.INVOKE_VIRTUAL)
                    continue;

                if (instruction.getIndex() != invokedMethodId)
                    continue;

                // two instructions after this one is where the insertion can
                // occur.
                i += 2;
                result.add(instructions.keyAt(i));

            }

        }

        return result;

    }

    /**
     * Returns the register types for all instructions in this method. The
     * instructions used are taken from the current MethodDefinition.
     * 
     * @return the methods register types.
     */
    public RegisterTypeSparseArray generateRegisterTypes(boolean logTypes) {
        RegisterTypeSparseArray result = new RegisterTypeSparseArray();
        try {
            result = getTypesFromSignature();

            if (DO_LOG)
                if (LOG_INSTRUCTIONS)
                    Log.d(RegisterTypeSparseArray.LOG_TAG, String.format(
                            "method %s in class %s", methodId().getName(),
                            methodId().getDeclaringClassSignature()));

            OpcodeInfo.generateTypes(result, instructionsDecodedAsList(),
                    logTypes);

        } catch (VerifyException e) {
            throw new RuntimeException(e);
        }

        // sanity
        if (result.size() != code().getRegistersSize()) {
            // TODO: throw exception
            Log.w(LOG_TAG,
                    String.format(
                            "register type sparse array contains %d entries when it should contain %d",
                            result.size(), code().getRegistersSize()));
        }

        return result;

    }

    // TODO: implement ability to get RegisterSpecList so that types of a
    // register can be determined.
    public RegisterSpecList generateRegisterSpecList() {
        RegisterTypeSparseArray registerTypes = generateRegisterTypes(false);
        RegisterSpecList result = new RegisterSpecList(registerTypes.size());
        for (int i = 0; i < registerTypes.size(); i++) {
            // result.set(i, registerTypes.valueAt(i).asRegisterSpec());
        }
        throw new UnsupportedOperationException("not implemented (yet)");

    }

    /**
     * Injects a method (methodToInject) into this method at the specified
     * address of this method. This object is modified. If updater is null it is
     * ignored and methodToInject is not modified during the injection. If it is
     * not null then methodToInject is modified for the injection and then reset
     * back to its original state.
     * 
     * @param methodToInject the method to inject into this one.
     * @param updater the updater to apply to the method prior to injection.
     * @param address the address to inject at.
     * @throws InjectException if the injection fails.
     */
    public boolean inject(MethodDefinition methodToInject, BaseUpdater updater,
            int address) throws InjectException {

        if (methodToInject == null)
            throw new IllegalArgumentException("methodToInject cannot be null");

        if (address < 0 || address >= code().getInstructions().length)
            throw new InjectException("invalid address");

        if (!hasClassData())
            return false;

        boolean thisIsStatic = this.method().dvmIsStaticMethod();
        boolean injectedIsStatic = methodToInject.method().dvmIsStaticMethod();

        if (thisIsStatic && !injectedIsStatic)
            throw new InjectException(
                    "this method is not static.  methodToInject must be non static.");

        if (!thisIsStatic && injectedIsStatic)
            throw new InjectException(
                    "this method is static.  methodToInject must be static.");

        if (updater == null)
            // no address for injection can be obtained = no side effects
            return true;

        boolean result = false;

        try {
            this.mark(); // save state

            updater.preInjectUpdate(this, address, methodToInject);
            try {
                result = injectWithRegisterAdjust(methodToInject, address);
            } catch (Throwable ex) {

                // FIXME catching throwables is bad, but thie is a prototype.
                // This will be removed.
                if (DO_LOG)
                    Log.e(LOG_TAG,
                            String.format(
                                    "bailing on injection of %s into %s (class %s) due to DexException.",
                                    methodToInject.methodId().getName(), methodId()
                                            .getName(), methodId()
                                            .getDeclaringClassSignature()));
                ex.printStackTrace();
                result = false;
            }

            if (result)
                updater.postInjectUpdate(this, address);

            return result;

        } finally {

            // reverts any side effects of injection process.
            methodToInject.reset();

            if (!result)
                // injection may have left this method in an unpredictable
                // state.
                this.resetToMark();

        }

    }

    /**
     * Updates the registers for methodToInject.
     * 
     * @param methodToInject the method that is being injected into this one.
     * @returns the highest register number.
     */
    private int updateInjectedRegisters(int vTargetFirstParameter,
            MethodDefinition methodToInject) {
        InstructionList injectedInstructions = methodToInject
                .instructionsDecodedAsList();

        boolean notStatic = !methodToInject.method().dvmIsStaticMethod();

        int vInjectedFirstParemeter = methodToInject
                .getFirstParameterRegister(notStatic);

        SparseIntArray injectMap = methodToInject.mapRegisters(
                vTargetFirstParameter, false);

        if (LOG_REGISTERS) {
            String title = String.format(
                    "INJECTED REGISTER MAP (isStatic: %b, regs: %d, ins: %d)",
                    methodToInject.isStatic(), methodToInject.code()
                            .getRegistersSize(), methodToInject.code()
                            .getInsSize());

            dumpRegisterMap(injectMap, vInjectedFirstParemeter, title);
        }

        // localRegisterMap can now be used to modify registers in
        // mehodToInject.
        for (int i = 0; i < injectedInstructions.size(); i++) {
            DecodedInstruction instruction = injectedInstructions.valueAt(i);
            instruction.updateRegisters(injectMap);
        }

        int registersSize = maxValue(injectMap) + 1;
        methodToInject.setInstructions(registersSize, injectedInstructions);

        return registersSize;

    }

    /**
     * Updates the registers for the instructions in this method.
     * 
     * @param vInjectedFirstParemeter the register number of the first parameter
     *            register of the method being injected including vThis.
     * @return the number of registers that this method will need after s
     *         injection.
     */
    private int updateRegisters(int vInjectedFirstParemeter) {

        InstructionList targetInstructions = instructionsDecodedAsList();
        boolean notStatic = !method().dvmIsStaticMethod();
        int vTargetFirstParameter = getFirstParameterRegister(notStatic);

        SparseIntArray targetMap = mapRegisters(vInjectedFirstParemeter, true);

        if (LOG_REGISTERS) {
            String title = String.format(
                    "TARGET REGISTER MAP (isStatic: %b, regs: %d, ins: %d)",
                    isStatic(), code().getRegistersSize(), code().getInsSize());

            dumpRegisterMap(targetMap, vTargetFirstParameter, title);
        }

        for (int i = 0; i < targetInstructions.size(); i++) {

            DecodedInstruction instruction = targetInstructions.valueAt(i);
            instruction.updateRegisters(targetMap);

        }

        int registersSize = maxValue(targetMap) + 1;

        // convert instructions if needed and update try/catches
        convertInstructions();

        // don't use setInstructions(int, RegisterList) - we don't want to
        // convert yet.
        // set the code and update registers size
        replaceInstructions(targetInstructions);
        code().setRegistersSize(registersSize);

        return registersSize;

    }

    /**
     * Replaces the current instruction set with the specified one. Try and
     * catch blocks are updated. Instructions are expected to be the same
     * instruction set as is already here. A conversion operation is performed
     * on the instruction set before replacing the existing.
     * 
     * @param newInstructions the instructions to replace the existing ones.
     */
    private void replaceInstructions(InstructionList newInstructions) {

        code().clearDecodedSingleton();
        InstructionList currentInstructions = instructionsDecodedAsList();

        if (currentInstructions.size() != newInstructions.size())
            throw new IllegalArgumentException(
                    "new instruction length must be the same as the current instruction length");

        checkTries();

        // ensure that addresses are rebuilt.
        newInstructions.convert();
        newInstructions.recalibrateAddresses();

        SparseIntArray addressMap = new SparseIntArray();
        for (int i = 0; i < currentInstructions.size(); i++) {
            addressMap.put(currentInstructions.keyAt(i),
                    newInstructions.keyAt(i));
        }

        updateReferencedAddresses(addressMap);

        code().setInstructions(newInstructions.encode());

        // make sure that the tries are set properly for the instructions.
        checkTries();

    }

    private void updateReferencedAddresses(SparseIntArray addressMap) {
        updateTries(addressMap);
        updateCatches(addressMap);

        validateTries();

    }

    private void validateTries() {

        Code.CatchHandler[] handlers = code().getCatchHandlers();

        Try[] tries = code().getTries();
        if (tries.length > 0) {
            for (Try tryItem : tries) {
                DexSwapVerify.getMatchingHandler(tryItem, handlers);
                /*
                 * throws IllegalArgumentException on failure. This will cause
                 * injection for this function to stop.
                 */
            }
        }
    }

    public boolean hasInstructionAtPosition(int position) {

        if (position < 0)
            throw new IllegalArgumentException(
                    "position must be greater than zero.");

        return code().hasInstructionAtDecodedPosition(position);
    }

    /**
     * Checks arguments to an inject call.
     * 
     * @param methodToInject the method to inject into this one.
     * @param position the position to inject the method into.
     * @throws InjectException
     * @Throws Throwable if a problem exists or exists without side effect
     *         otherwise.
     */
    private void checkInjectArguments(MethodDefinition methodToInject,
            int position) throws InjectException {

        if (methodToInject.getParameters().size() > 0)
            throw new IllegalArgumentException(
                    "methodToInject cannot have any parameters.");

        if (methodToInject.code().getTries().length > 0)
            throw new IllegalArgumentException(
                    "methodToInject cannot have any try blocks.");

        if (methodToInject.code().getCatchHandlers().length > 0)
            throw new IllegalArgumentException(
                    "methodToInject cannot have any try catch handlers.");

        if (!hasInstructionAtPosition(position))
            throw new InjectException(
                    String.format(
                            "Position %d does not exist in the target's decoded instruction set.",
                            position));

    }

    public boolean shouldBreakpoint() {
        String name = methodId().getName();
        for (String s : BREAKPOINT_METHODS) {
            if (s.equals(name))
                return true;
        }

        return false;
    }

    /**
     * Injects source instructions into target instructions at the specified
     * position. The parameterCount of the target instruction set is required so
     * that the register numbers for parameters can be respected. They always
     * appear as the last registers in the register set.
     * 
     * @returns true if the injection was successful
     */
    private boolean injectWithRegisterAdjust(MethodDefinition methodToInject,
            int position) throws InjectException {

        checkInjectArguments(methodToInject, position);

        if (methodToInject.code().getInstructions().length == 0)
            return true; // nothing to inject but still a success condition.

        // notStatic = true means this method is not static
        boolean notStatic = !method().dvmIsStaticMethod();
        logOpcode(Opcodes.PACKED_SWITCH_PAYLOAD);

        // save first parameter register for both target and injected
        int vTargetFirstParameter = getFirstParameterRegister(notStatic);
        int vInjectFirstParameter = methodToInject
                .getFirstParameterRegister(notStatic);

        this.dumpInstructions();
        int targetRegs = updateRegisters(vInjectFirstParameter);
        this.dumpInstructions();

        logOpcode(Opcodes.PACKED_SWITCH_PAYLOAD);

        methodToInject.dumpInstructions();
        int injectRegs = updateInjectedRegisters(vTargetFirstParameter,
                methodToInject);
        methodToInject.dumpInstructions();

        int regSize = targetRegs >= injectRegs ? targetRegs : injectRegs;
        insertInstructions(regSize, methodToInject, position);
        if (DO_LOG) {
            if (LOG_REGISTERS) {

                Log.i(RegisterType.LOG_TAG,
                        "REGISTERS FOR THIS FUNCTION AFTER MODIFICATION");
                generateRegisterTypes(LOG_REGISTERS);

            }
        }

        logOpcode(Opcodes.PACKED_SWITCH_PAYLOAD);
        dumpInstructions();

        return true;
    }

    /**
     * Utility logging function for logging a specific opcode. Useful for
     * debugging.
     * 
     * @param opcode the opcode to log.
     */
    private void logOpcode(int opcode) {

        DecodedInstruction ins = getFirstOccurrence(opcode);
        if (ins == null)
            return;

        if (DO_LOG)
            Log.d(LOG_TAG_OPCODE,
                    String.format("in method %s: %s", methodId().getName(),
                            ins.toLongString()));

    }

    /**
     * Returns the first occurrence of an instruction in the method. Null is
     * returned if the instruction is not present.
     * 
     * @param opcode the opcode to search for.
     * @return the first occurrence of the opcode if present.
     */
    public DecodedInstruction getFirstOccurrence(int opcode) {
        InstructionList iList = instructionsDecodedAsList();
        int sz = iList.size();

        for (int i = 0; i < sz; i++) {
            if (iList.valueAt(i).getOpcode() == opcode)
                return iList.valueAt(i);
        }

        return null;
    }

    /**
     * Returns true if this method contains at least one occurrence of a
     * specific opcode.
     * 
     * @param opcode the opcode to search for.
     * @return true if the opcode is present.
     */
    public boolean containsOpcode(int opcode) {
        InstructionList iList = instructionsDecodedAsList();
        int sz = iList.size();

        // TODO figure out what would be encoded with iList.size() in the loop.

        // TODO include compiler behavior in the manual/book.

        for (int i = 0; i < sz; i++) {
            if (iList.valueAt(i).getOpcode() == opcode)
                return true;
        }

        return false;
    }

    /**
     * Inserts the instructions from methodToInject into this method's
     * instruction list at the specified instruction position.
     * 
     * @param methodToInject the method to get instructions from.
     * @param position the position to insert instructions into.
     */
    public void insertInstructions(int regSize,
            MethodDefinition methodToInject, int position) {

        checkTries();

        // ensure that addresses are rebuilt.
        code().clearDecodedSingleton();
        methodToInject.code().clearDecodedSingleton();

        InstructionList insertedInstructions = methodToInject
                .instructionsDecodedAsList();
        InstructionList targetInstructions = instructionsDecodedAsList();

        // capture the addresses
        SparseIntArray oldAddresses = new SparseIntArray();
        for (int i = 0; i < targetInstructions.size(); i++) {
            int addr = targetInstructions.keyAt(i);
            oldAddresses.put(addr, addr);
        }

        InstructionList newInstructions = new InstructionList();

        newInstructions.append(targetInstructions, 0, position, true);
        newInstructions.append(insertedInstructions, 0,
                insertedInstructions.size(), false);
        newInstructions.append(targetInstructions, position,
                targetInstructions.size() - position, false);

        newInstructions.recalibrateAddresses();

        // update blocks for positions: 0 to position-1, and position +
        // insertedInstructions.size() to newInstructions.size()-1
        int inserted = insertedInstructions.size();
        int start = position + inserted;
        for (int i = start; i < newInstructions.size(); i++) {
            int newAddress = newInstructions.keyAt(i);
            int oldAddressPosition = i - inserted;
            int oldAddress = oldAddresses.keyAt(oldAddressPosition);
            oldAddresses.put(oldAddress, newAddress);

        }

        updateReferencedAddresses(oldAddresses);

        // a target can appear anywhere in the new instruction list
        for (int j = 0; j < newInstructions.size(); j++) {

            DecodedInstruction insn = newInstructions.valueAt(j);
            if (!insn.getFormat().hasTarget())
                continue;

            StringBuilder builder = new StringBuilder();
            builder.append("updating target: ");
            builder.append(insn.toLongString());

            if (insn instanceof PackedSwitchPayloadDecodedInstruction) {
                PackedSwitchPayloadDecodedInstruction ins = (PackedSwitchPayloadDecodedInstruction) insn;
                // there will be several targets
                int[] targets = ins.getTargets();

                if (targets != null) {

                    for (int i = 0; i < targets.length; i++) {
                        if (oldAddresses.indexOfKey(targets[i]) >= 0) {
                            targets[i] = oldAddresses.get(targets[i]);
                        }
                    }
                }

            } else {
                int target = insn.getTarget();
                if (oldAddresses.indexOfKey(target) >= 0)
                    insn.setTarget(oldAddresses.get(target));
            }

            builder.append(insn.toLongString());

            if (DO_LOG)
                Log.d(LOG_TAG, builder.toString());
        }

        setInstructions(regSize, newInstructions);

        checkTries();
    }

    /**
     * Throws an exception if the tries or catch handler addresses are invalid.
     */
    public void checkTries() {

        code().clearDecodedSingleton();
        InstructionList iList = instructionsDecodedAsList();

        Try[] tries = code().getTries();
        for (Try t : tries) {
            int addr = t.getStartAddress();
            if (iList.indexOfKey(addr) < 0)
                throw new IllegalStateException("try block has invalid address");
        }

        CatchHandler[] handlers = code().getCatchHandlers();

        for (CatchHandler ch : handlers) {
            int[] chAddresses = ch.getAddresses();
            for (int i : chAddresses) {
                if (iList.indexOfKey(i) < 0)
                    throw new IllegalStateException(
                            "catch handler has invalid address");
            }

            int catchAllAddress = ch.getCatchAllAddress();
            if (catchAllAddress >= 0
                    && iList.indexOfKey(ch.getCatchAllAddress()) < 0)
                throw new IllegalStateException(String.format(
                        "catch all handler address %d is invalid",
                        ch.getCatchAllAddress()));
        }

    }

    private int getInstructionAddressOfPosition(int position) {
        if (!hasInstructionAtPosition(position))
            throw new IllegalArgumentException(
                    String.format(
                            "position %d is not in this method's decoded instruction set.",
                            position));

        return code().decodeAllAsList(this).keyAt(position);
    }

    /**
     * Adjusts the start address of try blocks if necessary based on the address
     * that was updated.
     * 
     * @param position the address of instruction where insertion occurred (not
     *            the position).
     * @param bytesAdded the number of bytes added to the function.
     */
    private void updateTries(SparseIntArray addressMap) {
        // get the address

        Try[] tries = code().getTries();
        if (tries.length > 0) {
            for (Try tryItem : tries) {
                int oldAddress = tryItem.getStartAddress();
                if (addressMap.indexOfKey(oldAddress) >= 0) {
                    int newAddress = addressMap.get(oldAddress);
                    tryItem.setStartAddress(newAddress);

                }

            }
        }

    }

    /**
     * Creates a register map by adding the specified increase to each register.
     * 
     * @param increase the amount to increase each register by.
     * @param parametersOnly if true then only parameter registers will be
     *            included (including vThis if applicable).
     * @return a mapping of register values with existing as keys and increased
     *         as values.
     */
    private SparseIntArray mapRegisters(int increase, boolean parametersOnly) {
        int vFirstParameter = getFirstParameterRegister(!this.isStatic());

        SparseIntArray result = new SparseIntArray();

        RegisterTypeSparseArray registerTypes = generateRegisterTypes(true);
        for (int i = 0; i < registerTypes.size(); i++) {
            int key = registerTypes.keyAt(i);
            int value = key;
            if (!parametersOnly || key >= vFirstParameter)
                value += increase;

            result.put(key, value);

        }
        return result;

    }

    @SuppressWarnings("unused")
    private void dumpRegisterMap(SparseIntArray localRegisterMap,
            Integer thisInjectedRegister, String title) {

        if (!DO_LOG)
            return;

        StringBuilder top = new StringBuilder();
        StringBuilder bottom = new StringBuilder();
        top.append(title).append("\n");

        for (int i = 0; i < localRegisterMap.size(); i++) {

            int key = localRegisterMap.keyAt(i);
            int value = localRegisterMap.valueAt(i);

            if (thisInjectedRegister != null && key == thisInjectedRegister) {
                top.append("*");
                bottom.append("*");
            }

            top.append(String.format("%-5d", key));
            bottom.append(String.format("%-5d", value));

        }

        top.append("\n");
        StringBuilder output = new StringBuilder();
        output.append(top.toString()).append(bottom.toString());
        if (LOG_REGISTERS)
            Log.d(LOG_TAG_MAP, output.toString());

    }

    @SuppressWarnings("unused")
    public void dumpInstructions() {
        if (!DO_LOG)
            return;

        if (LOG_INSTRUCTIONS) {
            if (DO_LOG)
                Log.i(LOG_TAG_INSTRUCTIONS, String.format(
                        "Method %s in class %s instructions", methodId.getName(),
                        methodId.getDeclaringClassSignature()));

            code().clearDecodedSingleton();

            InstructionList instructions = code().decodeAllAsList();
            for (int i = 0; i < instructions.size(); i++) {

                Log.i(LOG_TAG_INSTRUCTIONS, String.format("%4d: %s", i,
                        instructions.valueAt(i).toLongString()));

            }

            // output try and catch blocks
            StringBuilder builder = new StringBuilder();
            builder.append("try addresses: ");
            Try[] tries = code().getTries();
            if (tries.length == 0)
                builder.append("none");

            for (Try t : tries) {
                builder.append(String.format("%-5d", t.getStartAddress()));
            }
            Log.i(LOG_TAG_INSTRUCTIONS, builder.toString());

            builder = new StringBuilder();
            builder.append("catch handlers:");
            CatchHandler[] handlers = code().getCatchHandlers();
            if (handlers.length == 0)
                builder.append("none");

            for (int i = 0; i < handlers.length; i++) {
                CatchHandler ch = handlers[i];
                int[] addresses = ch.getAddresses();
                for (int a : addresses) {
                    builder.append(String.format("\n\t%d.%-5d", i, a));
                }
            }

            Log.i(LOG_TAG_INSTRUCTIONS, builder.toString());

            code().clearDecodedSingleton();

        }
    }

    /**
     * This obejct saves the original code object so that it can be modified
     * using updateMethod and then restored using resetInstructions().
     */
    private Code original;

    /**
     * This object saves
     */
    private Code mark;

    /**
     * Updates a method's instructions. This is usually called on a method that
     * is injected into another method to set parameters, etc. prior to the
     * injection.
     * 
     * @param updater the updater to apply against this MethodDefinition.
     */
    public void updateMethod(BaseUpdater updater) {

    }

    /**
     * Called to reset the code to the state it was when this method definition
     * was created (e.g. read from the DEX file). This can be called after
     * updateMethod to reset the state of the Code object.
     */
    public void reset() {
        code().resetTo(original);

    }

    /**
     * Called to force the method to mark its code with its current value. Once
     * this is done the code cannot be reset
     */
    public void mark() {
        mark = (Code) code.clone();

    }

    public void resetToMark() {
        if (mark == null)
            throw new IllegalStateException(
                    "mark has not been set. call mark() first");

        code().resetTo(mark);
    }

    /**
     * Returns the number of registers used by this method.
     * 
     * @return register count for this method.
     */
    public int getRegistersSize() {
        return code().getRegistersSize();
    }

    /**
     * Returns the number of registers passed into
     * 
     * @return
     */
    public int getArgumentRegisterCount() {
        return code().getInsSize();
    }

    /**
     * Returns the register number for the this register. See the C function
     * setTypesFromSignature in CodeVerify.cpp to see how the this register is
     * calculated. The this register is code().getRegistersSize() -
     * code().getInsSize().
     * 
     * @return the register for this pointer.
     * @throws IllegalStateException if this method is static.
     */
    public int getThisRegister() {
        return getFirstParameterRegister(true);

    }

    /**
     * Returns the number of registers used for parameters, not including the
     * this parameter. If this method is non static the result does not count
     * the this register.
     * 
     * @return the number of registers used by parameters.
     */
    public int getParameterRegisterCount() {
        int ins = code().getInsSize();
        return method().dvmIsStaticMethod() ? ins : ins - 1;
    }

    /**
     * Returns the first register number that contains a parameter register. If
     * includeThis is true then the value returned is the register that contains
     * the this pointer.
     * 
     * @param includeThis if the this register should be included in the
     *            calculation.
     * @return the first parameter register
     * @throws IllegalArgumentException if this method is static and includeThis
     *             is true.
     */
    public int getFirstParameterRegister(boolean includeThis) {
        if (includeThis && method().dvmIsStaticMethod())
            throw new IllegalStateException(
                    "this method is static and has no this register");

        Code thisCode = code();
        int result = thisCode.getRegistersSize() - thisCode.getInsSize();
        if (!method().dvmIsStaticMethod() && !includeThis)
            result++;

        return result;

    }

    /**
     * Returns the count of local registers used by this function. The register
     * values will be 0 to n-1, where n is the return value for this function.
     * 
     * @return the number registers reserved for local variables in this
     *         function.
     */
    public int getLocalRegisterCount() {
        Code thisCode = code();
        int registerCount = thisCode.getRegistersSize();
        int insCount = thisCode.getInsSize();

        return registerCount - insCount;

    }

    /**
     * Sets the register types of this instruction from the method parameters.
     * All registers assocaited with the signature are added, including low and
     * high registers for wide register types. Also performs some basic
     * validation to ensure that the number of arguments passed to the function
     * are valid.
     * <p>
     * The function of the same name (setTypesFromSignature) in the C version of
     * Dalvik performs the same function as this one. The main differences
     * between this implementation and the C one are:
     * <ul>
     * <li>the objects passed to this function already have much of the
     * verification performed on them. The C function does its verification
     * within the function itself.
     * <li>this function verifies argument counts by calling
     * verifyArgumentCounts. The C function does this verification within the
     * function itself.
     * </ul>
     * <p>
     * In the C function this was called setTypesFromSignature(). Leave this
     * comment in the doc so that the this function can be easily found using
     * the C code function name.
     * 
     * @param methodId the methodId this code is associated with.
     * @param meth the method this code is associated with.
     * @param protoId the protoId this code is associated with.
     * @throws VerifyException if the list of parameters from pId do not match
     *             the number of parameters specified by getInsSize().
     */
    public RegisterTypeSparseArray getTypesFromSignature()
            throws VerifyException {

        verifyArgumentCounts();

        RegisterTypeSparseArray regTypes = new RegisterTypeSparseArray();
        List<String> parameters = getParameters();

        boolean includeThis = false;
        if (!method().dvmIsStaticMethod())
            includeThis = true;
        int currentRegister = getFirstParameterRegister(includeThis);

        /*
         * Include the "this" pointer.
         */
        if (!method.dvmIsStaticMethod()) {

            // sanity
            int thisRegister = getThisRegister();
            if (thisRegister != currentRegister)
                throw new IllegalStateException(
                        String.format(
                                "thisRegister is %d and does not match the first parameter register %d",
                                thisRegister, currentRegister));

            RegisterType type = RegisterType.newInstance(kRegTypeRef);
            type.setRefersTo(methodId().getDeclaringClassSignature(),
                    IndexType.TYPE_REF);

            regTypes.put(thisRegister, type);
            currentRegister++;
        }

        for (String signature : parameters) {

            switch (signature.charAt(0)) {
                case 'L':
                case '[':
                    RegisterType type = RegisterType.newInstance(kRegTypeRef);
                    type.setRefersTo(signature, IndexType.TYPE_REF);
                    regTypes.put(currentRegister, type, null, null);
                    break;

                case 'Z':
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeBoolean), null, null);
                    break;

                case 'C':
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeChar), null, null);
                    break;

                case 'B':
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeByte), null, null);
                    break;

                case 'I':
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeInteger), null, null);
                    break;

                case 'S':
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeShort), null, null);
                    break;

                case 'F':
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeFloat), null, null);
                    break;

                case 'D':
                    // double takes up two registers
                    regTypes.put(currentRegister++,
                            RegisterType.newInstance(kRegTypeDoubleLo), null, null);
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeDoubleHi), null, null);
                    break;

                case 'J':
                    // long takes up two registers
                    regTypes.put(currentRegister++,
                            RegisterType.newInstance(kRegTypeLongLo), null, null);
                    regTypes.put(currentRegister,
                            RegisterType.newInstance(kRegTypeLongHi), null, null);
                    break;

                default:
                    throw new VerifyException(
                            VerifyErrorCause.VERIFY_ERROR_GENERIC,
                            String.format(
                                    "prototype %s has invalid parameter %s in function %s.%s",
                                    signature,
                                    methodId.getDeclaringClassSignature(),
                                    methodId.getName()));

            }

            currentRegister++;
        }

        return regTypes;
    }

    /**
     * Verifies that the number of arguments are appropriate for this method's
     * code object given its method and prototype. If all tests pass then this
     * function does nothing. If a test fails then this function throws and
     * exception.
     * 
     * @param method the method associated with this code object.
     * @param pId the prototype id associated with this code object.
     * @throws VerifyException if the number of arguments defined in this code
     *             object are not valid given the method definition and the
     *             number of parameters in the prototype.
     */
    public void verifyArgumentCounts() throws VerifyException {

        int expectedArgs = getParameterRegisterCount(); // getInsSize();
        int actualArgs = 0;

        List<String> signatures = getParameters();

        // iterate through the parameters so any offending signature can be
        // reported
        for (String signature : signatures) {
            actualArgs++;
            if (actualArgs > expectedArgs)
                throw new VerifyException(
                        VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
                                "VFY: expected %d args, found more (%s)",
                                expectedArgs, signature));
        }

        if (actualArgs != expectedArgs)
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("VFY: expected %d args, found %d",
                            expectedArgs, actualArgs));

    }

    public List<String> getParameters() {
        return protoId().getParameters();
    }

    /**
     * Instruction padding in shorts used when serializing an InstructionList to
     * prevent buffer overflow.
     */
    public static final int INSTRUCTION_PADDING = 10;

    /**
     * Sets the instructions in this method to the instructions in the list.
     * <p>
     * There is an important side effect to calling this function: The
     * instructions are checked to see if the register values require them to be
     * converted using InstructionCodec.convert(). If any instructions were
     * modified then the underlying code object will be udpated with proper
     * register counts.
     * <p>
     * Side effects: code().getRegistersSize() is updated; code().getOutsSize()
     * is updated.
     * <p>
     * Try and Catch blocks are not affected.
     * 
     * @param newInstructions the instructions to set.
     * @param registersSize the register count for this method.
     */
    public void setInstructions(int registersSize,
            InstructionList newInstructions) {

        if (newInstructions == null)
            throw new IllegalArgumentException("newInstructions cannot be null");

        if (newInstructions.size() == 0)
            throw new IllegalArgumentException(
                    "newInstructions cannot be empty");

        newInstructions.convert();
        code().setRegistersSize(registersSize);
        code().setOutsSize(newInstructions.calculateOutsSize());
        code().setInstructions(newInstructions.encode());

    }

    /**
     * Runs codec conversion on the instructions and updates the try and catch
     * handlers with new address information.
     */
    public void convertInstructions() {

        code().clearDecodedSingleton();
        InstructionList originalList = instructionsDecodedAsList();

        code().clearDecodedSingleton();
        InstructionList convertedList = instructionsDecodedAsList();
        convertedList.convert();

        /*
         * OriginalList and covertedList are equivalent instruction sets, with
         * the addresses potentially different.
         */

        SparseIntArray addressMap = new SparseIntArray();

        // update try and catch blocks
        for (int i = 0; i < originalList.size(); i++) {
            // sanity
            if (originalList.keyAt(i) != originalList.valueAt(i).getAddress())
                throw new IllegalStateException("address mismatch");

            if (convertedList.keyAt(i) != convertedList.valueAt(i).getAddress())
                throw new IllegalStateException("address mismatch");

            addressMap.put(originalList.keyAt(i), convertedList.keyAt(i));

        }

        updateReferencedAddresses(addressMap);

        for (int i = 0; i < convertedList.size(); i++) {
            DecodedInstruction insn = convertedList.valueAt(i);
            InstructionCodec codec = insn.getFormat();
            if (!codec.hasTarget())
                continue;

            if (insn instanceof PackedSwitchPayloadDecodedInstruction) {
                PackedSwitchPayloadDecodedInstruction ins = (PackedSwitchPayloadDecodedInstruction) insn;
                // there will be several targets
                int[] targets = ins.getTargets();

                if (targets != null) {

                    for (int j = 0; j < targets.length; j++) {
                        if (addressMap.indexOfKey(targets[j]) >= 0) {
                            targets[j] = addressMap.get(targets[j]);
                        }
                    }
                }

            } else {
                int target = insn.getTarget();
                if (addressMap.indexOfKey(target) >= 0)
                    insn.setTarget(addressMap.get(target));
            }

        }

        setInstructions(getRegistersSize(), convertedList);

    }

    private void updateCatches(SparseIntArray addressMap) {

        Code.CatchHandler[] handlers = code().getCatchHandlers();

        for (Code.CatchHandler handler : handlers) {
            handler.setAddresses(addressMap);
        }

    }

    /**
     * Sets the instructions of the underlying code object with no other side
     * effects.
     * 
     * @param transformedInstructions the instructions to set.
     */
    public void setInstructions(short[] newInstructions) {
        code().setInstructions(newInstructions);

    }

    public String shorty() {
        return protoId().getShortyString();
    }

    public boolean callsHttpExecute() {
        return false;
    }

    private int maxValue(SparseIntArray sia) {
        Integer result = null;
        for (int i = 0; i < sia.size(); i++) {
            int val = sia.valueAt(i);
            if (result == null || result.intValue() < val)
                result = new Integer(val);

        }

        return result;

    }

    public boolean isStatic() {
        if (!methodId().isResolvable())
            throw new IllegalStateException(
                    "cannot determine static because this method is not resolvable to ClassData.Method");

        return method().dvmIsStaticMethod();
    }

    @Override
    public String toString() {
        return String
                .format("MethodDefinition %s in class %s (vMax %d, vOuts %d, vIns %d, ins count %d",
                        methodId().getName(), methodId()
                                .getDeclaringClassSignature(), code()
                                .getRegistersSize(), code().getInsSize(),
                        code().getOutsSize(), code().getInstructions().length);
    }

    /**
     * Replaces the return instruction with a NOP address.
     * 
     * @param instructions
     */
    @Deprecated
    public void removeReturnInstruction() {

        throw new UnsupportedOperationException("no longer supported");

    }

    /**
     * Injects methosToInject into this one at the position using DavkCode
     * approach. This approach involves decompiling both this method and
     * methodToInject into an intermediate form of DalvCode, OutputFinisher, and
     * a CatchBuilder.
     * 
     * @param methodToInject the method to inject into this one.
     * @param position the position to inject the method contents into.
     * @return
     * @throws InjectException
     */
    private boolean injectUsingDalvCode(MethodDefinition methodToInject,
            int position) throws InjectException {

        checkInjectArguments(methodToInject, position);

        OutputCollector unprocessedInsns = getOutputCollector();
        CatchBuilder unprocessedCatches = getUnprocessedCatches();

        if (methodToInject.code().getInstructions().length == 0)
            return true; // nothing to inject but still a success condition.

        // notStatic = true means this method is not static
        boolean notStatic = !method().dvmIsStaticMethod();
        logOpcode(Opcodes.PACKED_SWITCH_PAYLOAD);

        // save first parameter register for both target and injected
        int vTargetFirstParameter = getFirstParameterRegister(notStatic);
        int vInjectFirstParameter = methodToInject
                .getFirstParameterRegister(notStatic);

        this.dumpInstructions();
        int targetRegs = updateRegisters(vInjectFirstParameter);
        this.dumpInstructions();

        logOpcode(Opcodes.PACKED_SWITCH_PAYLOAD);

        methodToInject.dumpInstructions();
        int injectRegs = updateInjectedRegisters(vTargetFirstParameter,
                methodToInject);
        methodToInject.dumpInstructions();

        int regSize = targetRegs >= injectRegs ? targetRegs : injectRegs;

        insertInstructions(regSize, methodToInject, position);

        if (DO_LOG) {
            if (LOG_REGISTERS) {

                Log.i(RegisterType.LOG_TAG,
                        "REGISTERS FOR THIS FUNCTION AFTER MODIFICATION");
                generateRegisterTypes(LOG_REGISTERS);

            }
        }

        logOpcode(Opcodes.PACKED_SWITCH_PAYLOAD);
        dumpInstructions();
        return true;
    }

    private CatchBuilder getUnprocessedCatches() {
        throw new UnsupportedOperationException("not implemented (yet)");
    }

    public int getInstructionCount() {
        return instructionsDecodedAsList().size();
    }

    /**
     * Extracts the instructions from this method and stores them in an
     * OutputFinisher and OutputCollector.
     * 
     * @return a new instance of output collector loaded with instructions
     */
    private OutputCollector getOutputCollector() {

        // default API and opcode usage information based on
        // DexFormat.API_CURRENT
        DexOptions options = new DexOptions();
        InstructionList iList = instructionsDecodedAsList();
        int instructionCount = iList.size();
        int suffixInitialCapacity = instructionCount * 3; // an estimation - see
                                                          // RopTranslator
                                                          // constructor

        OutputCollector collector = new OutputCollector(options,
                getInstructionCount(), suffixInitialCapacity,
                getRegistersSize());

        for (int i = 0; i < iList.size(); i++) {
            DecodedInstruction ins = iList.valueAt(i);

            collector.add(ins.asDalvInsn());
        }

        return collector;
    }
}
