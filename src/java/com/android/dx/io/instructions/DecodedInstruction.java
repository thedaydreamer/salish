/*
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

package com.android.dx.io.instructions;

// TODO: put all register verification code in separate classes so it can be managed properly.  Keep it separate from the code that actually sets register types.
import static com.android.dx.io.opcode.Opcodes.FILLED_NEW_ARRAY_JUMBO;
import static com.android.dx.io.opcode.Opcodes.FILLED_NEW_ARRAY_RANGE;
import static schilling.richard.dalvik.vm.analysis.RegisterType.constTypeToRegType;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeAnyCategory2;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeOne;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import static schilling.richard.r3.app.DebugConstants.DO_LOG;

import java.io.EOFException;
import java.util.List;
import java.util.NoSuchElementException;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.RegisterCategory;
import schilling.richard.dalvik.vm.analysis.RegisterLine;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;
import schilling.richard.dalvik.vm.analysis.RegisterTypeSparseArray;
import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;
import schilling.richard.dalvik.vm.oo.VerifyException;
import schilling.richard.dexlib.Code.Opcode;
import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dop;
import com.android.dx.dex.code.Dops;
import com.android.dx.io.ClassData;
import com.android.dx.io.ClassDef;
import com.android.dx.io.Code;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.DexBuffer;
import com.android.dx.io.FieldId;
import com.android.dx.io.IndexType;
import com.android.dx.io.MethodId;
import com.android.dx.io.ProtoId;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.io.opcode.format.InstructionCodec;
import com.android.dx.merge.MethodDefinition;
import com.android.dx.util.DexException;
import com.android.dx.util.Hex;

// TODO add method to return a RegisterType sparse array for the registers without any checks or assignment.
// TODO do we really need to track uninitialized classes for our purposes - the already written code can be uncommented already if that becomes the case

/**
 * A Dalvik instruction, or code unit that resides in
 * <code>code_data_item</> in a DEX File.
 * 
 * <p>A decoded Dalvik instruction. This consists of a format codec, a numeric
 * opcode, an optional index type, and any additional arguments of the
 * instruction. The additional arguments (if any) are represented as
 * uninterpreted data.
 * <p>
 * <b>Note:</b> The names of the arguments are <i>not</i> meant to match the
 * names given in the Dalvik instruction format specification, specification
 * which just names fields (somewhat) arbitrarily alphabetically from A. In this
 * class, non-register fields are given descriptive names and register fields
 * are consistently named alphabetically.
 * </p>A comment in InstrUtils.h in the definition of DecodedInstruction seems to suggest that vC/D/E/F/G is used exclusively for INVOKE or FILLED-NEW-ARRAY:
 * <pre>
 * 
 * // Holds the contents of a decoded instruction.
 * 
 * struct DecodedInstruction {
 *    u4      vA;
 *    u4      vB;
 *    u8      vB_wide;        // for kFmt51l
 *    u4      vC;
 *    u4      arg[5];         // vC/D/E/F/G in invoke or filled-new-array
 *    Opcode  opcode;
 *    InstructionIndexType indexType;
 * };
 * </pre>
 */
public abstract class DecodedInstruction {

    /**
     * The highest register reference value that an instruction can access.
     */
    public static final int MAX_REG_VALUE = 65535;

    public static final String LOG_TAG = "DecodedInstruction";

    /**
     * The maximum number of shorts that any opcode could possibly have. This is
     * an over estimation to ensure the safe case is handled for things like
     * encoding instructions.
     */
    public static final int MAX_CODE_UNITS_PER_INSTRUCTION = 10;

    /**
     * The method definition that is the parent of this instruction.
     */
    private MethodDefinition methodDef;
    private boolean canResolve = false;

    /**
     * If this is true, then the register values were read using
     * InstructionCodec.decodeRegisterList. And, consequently, they will be
     * encoded using InstructionCodec.encodeRegisterList. All the registers are
     * set to default values of 0. checkRegisterUsage() will also ignore the
     * check for RegisterReference.minRegistersRequired(). This object is
     * created when the instruction is decoded.
     */
    private boolean decodedFromRegisterList = false;

    /**
     * The number of bytes read during the decoding of this instruction.
     */
    private int size = 0;

    public int getSize() {
        return size;
    }

    /**
     * All instructions of a method share the same uninitInstanceMap instance so
     * that verification can know what instructions have uninitialized object
     * classes. This object is created when the instruction is decoded.
     */
    // private SparseArray<String> uninitInstanceMap;

    /**
     * Initialized class sparse array. When initialized, a class reference is
     * moved from uninitInstanceMap to initInstanceMap. This object is created
     * when the instruction is decoded.
     */
    /*
     * private SparseArray<String> initInstanceMap; public SparseArray<String>
     * getUninitInstanceMap() { return uninitInstanceMap; } public
     * SparseArray<String> getInitInstanceMap() { return initInstanceMap; }
     */
    public Code getCode() {
        if (!canResolve())
            throw new IllegalStateException(
                    "code not available.  Call this function only when canResolve() return true.");
        return methodDef.code();
    }

    public ProtoId getProtoId() {
        if (!canResolve())
            throw new IllegalStateException(
                    "proto id not available.  Call this function only when canResolve() return true.");
        return methodDef.protoId();
    }

    public MethodId getMethodId() {
        if (!canResolve())
            throw new IllegalStateException(
                    "method id not available.  Call this function only when canResolve() return true.");
        return methodDef.methodId();
    }

    public void setMethodDef(MethodDefinition mDef) {
        methodDef = mDef;
        canResolve = (methodDef != null);
    }

    public MethodDefinition getMethodDef() {
        if (!canResolve())
            throw new IllegalStateException(
                    "method not available.  Call this function only when canResolve() return true.");
        return methodDef;
    }

    public ClassData.Method getMethod() {
        if (!canResolve())
            throw new IllegalStateException(
                    "method not available.  Call this function only when canResolve() return true.");
        return methodDef.method();
    }

    /** non-null; instruction format / codec */
    private final InstructionCodec format;

    /**
     * A convenience member that contains the enumeration of the value in
     * opcode.
     */
    private final int opcode;
    private final Opcode opcodeEnum;

    /** constant index argument */
    private int index;

    /** null-ok; index type */
    private final IndexType indexType;

    /**
     * target address argument. This is an absolute address, not just a signed
     * offset. <b>Note:</b> The address is unsigned, even though it is stored in
     * an {@code int}.
     */
    private int target;

    /**
     * The relative address associated with {@code target}.
     */
    private int relativeTarget;

    /**
     * literal value argument; also used for special verification error
     * constants (formats 20bc and 40sc) as well as should-be-zero values
     * (formats 10x, 20t, 30t, and 32x)
     */
    private long literal;

    /**
     * Instruction widths and flags, one entry per code unit.
     */

    public int insnFlags = 0;

    /**
     * The value of register reference A. If the value is null, then an
     * exception will be generated if it's accessed. This prevents registers
     * from being used unless they are set.
     */
    private Integer regRefA;

    /**
     * The value of register reference B. If the value is null, then an
     * exception will be generated if it's accessed. This prevents registers
     * from being used unless they are set.
     */
    private Integer regRefB;

    /**
     * The value of register reference C. If the value is null, then an
     * exception will be generated if it's accessed. This prevents registers
     * from being used unless they are set.
     */
    private Integer regRefC;

    /**
     * The value of register reference D. If the value is null, then an
     * exception will be generated if it's accessed. This prevents registers
     * from being used unless they are set.
     */
    private Integer regRefD;

    /**
     * The value of register reference E. If the value is null, then an
     * exception will be generated if it's accessed. This prevents registers
     * from being used unless they are set.
     */
    private Integer regRefE;

    /**
     * Register type for register reference A. That is, what kind of register is
     * referenced.
     */
    private RegisterType regTypeA = RegisterType.newInstance(kRegTypeUnknown);

    /**
     * Register type for register reference B. That is, what kind of register is
     * referenced.
     */
    private RegisterType regTypeB = RegisterType.newInstance(kRegTypeUnknown);

    /**
     * Register type for register reference C. That is, what kind of register is
     * referenced.
     */
    private RegisterType regTypeC = RegisterType.newInstance(kRegTypeUnknown);

    /**
     * Register type for register reference D. That is, what kind of register is
     * referenced.
     */
    private RegisterType regTypeD = RegisterType.newInstance(kRegTypeUnknown);

    /**
     * Register type for register reference E. That is, what kind of register is
     * referenced.
     */
    private RegisterType regTypeE = RegisterType.newInstance(kRegTypeUnknown);

    private Integer address = null;

    public void setAddress(Integer a) {
        address = a;
    }

    public int getAddress() {
        if (address == null)
            throw new IllegalStateException(
                    "the address of this instruction is not set.");

        return address.intValue();
    }

    /**
     * @deprecated
     */
    public RegisterLine registerLine = null;

    public int[] arg = new int[5];

    public DexBuffer getBuffer() {
        if (!canResolve())
            throw new IllegalStateException(
                    "buffer not available.  You must use constructor decodeAll(MethodDefinition).");

        return methodDef.buffer();
    }

    /**
     * Returns true if the dex buffer members (e.g. method, buffer, protoid,
     * etc...) have been set.
     * 
     * @return
     */
    public boolean canResolve() {
        return canResolve;
    }

    /**
     * @deprecated
     */
    public void clearRegisterLine() {
        registerLine = null;
    }

    /**
     * @deprecated
     */
    public boolean hasRegisterLine() {
        return registerLine != null ? true : false;
    }

    /**
     * Decodes an instruction from the given input source.
     */
    public static DecodedInstruction decode(CodeInput in) throws EOFException {
        int address = in.cursor();
        int opcodeUnit = in.read();
        int opcode = Opcodes.extractOpcodeFromUnit(opcodeUnit);

        InstructionCodec format = OpcodeInfo.getFormat(opcode);
        DecodedInstruction result = format.decode(opcodeUnit, in);
        result.address = address;
        result.size = in.cursor() - address;

        return result;
    }

    /**
     * Decodes an array of instructions. The result has non-null elements at
     * each offset that represents the start of an instruction. Changing the
     * elements in the result array will on affect the encodedInstructions array
     * contents.
     */
    public static DecodedInstruction[] decodeAll(short[] encodedInstructions) {
        if (encodedInstructions == null || encodedInstructions.length == 0)
            return new DecodedInstruction[0];

        int size = encodedInstructions.length;
        DecodedInstruction[] decoded = new DecodedInstruction[size];
        ShortArrayCodeInput in = new ShortArrayCodeInput(encodedInstructions);

        try {
            while (in.hasMore()) {

                decoded[in.cursor()] = DecodedInstruction.decode(in);

            }
        } catch (EOFException ex) {
            RuntimeException error = new RuntimeException("shouldn't happen");
            error.initCause(ex);
            throw error;
        }

        return decoded;
    }

    /**
     * converts an array of decoded instructions into an InstructionList.
     * 
     * @param decodedInstructions the instructions to read.
     * @return an InstructionList with the instruction in it.
     */
    public static InstructionList decodeAll(
            DecodedInstruction[] decodedInstructions) {
        InstructionList result = new InstructionList();

        for (int i = 0; i < decodedInstructions.length; i++) {
            if (decodedInstructions[i] == null)
                continue;
            result.put(decodedInstructions[i].getAddress(),
                    decodedInstructions[i]);

        }

        return result;
    }

    /**
     * Performs the same function as decodeAll(short), except that the parent
     * buffer is set for each instruction. The array returned is the same length
     * as the secified encodedInstructions. But, some elements will contain null
     * values.
     * 
     * @param buffer the buffer that this instructions were read from.
     * @param methodDef the method to decode instructions from.
     * @return a list of decoded instructions.
     */
    public static DecodedInstruction[] decodeAll(MethodDefinition methodDef,
            short[] encodedInstructions) {

        // TODO: call this function instead of the other to load instructions.
        DecodedInstruction[] result = decodeAll(encodedInstructions);

        for (int i = 0; i < result.length; i++) {

            if (result[i] == null)
                continue;

            result[i].methodDef = methodDef;
            result[i].canResolve = true;

        }

        return result;
    }

    /**
     * Constructs an instance.
     */
    public DecodedInstruction(InstructionCodec format, int opcode, int index,
            IndexType indexType, int target, int relativeTarget, long literal) {
        if (format == null) {
            throw new NullPointerException("format == null");
        }

        if (!Opcodes.isValidShape(opcode)) {
            throw new IllegalArgumentException("invalid opcode");
        }

        this.format = format;
        this.opcode = opcode;

        // handle special cases FIXME: rework OpcodeEnums to handle all
        // instructions.
        if (opcode == Opcodes.NEW_ARRAY_JUMBO)
            this.opcodeEnum = Opcode.NEW_ARRAY_JUMBO;
        else
            this.opcodeEnum = Opcode.getOpcodeByValue((short) opcode);

        this.index = index;
        this.indexType = indexType;
        this.target = target;
        this.relativeTarget = relativeTarget;
        this.literal = literal;
    }

    public DecodedInstruction(InstructionCodec format, int opcode, int index,
            IndexType indexType, int target, int relativeTarget, long literal,
            boolean registerList) {
        this(format, opcode, index, indexType, target, relativeTarget, literal);
        decodedFromRegisterList = registerList;
        if (decodedFromRegisterList) {
            regRefA = new Integer(0);
            regRefB = new Integer(0);
            regRefC = new Integer(0);
            regRefD = new Integer(0);
            regRefE = new Integer(0);

        }
    }

    public final InstructionCodec getFormat() {
        return format;
    }

    // TODO name this functions getOpcodeValue
    public final int getOpcode() {
        return opcode;
    }

    // TODO name this function getOpcode()
    public final Opcode getOpcodeEnum() {
        return opcodeEnum;

    }

    /**
     * Gets the opcode, as a code unit.
     */
    public final short getOpcodeUnit() {
        return (short) opcode;
    }

    public final int getIndex() {
        return index;
    }

    /**
     * Gets the index, as a code unit.
     */
    public final short getIndexUnit() {
        return (short) index;
    }

    public final IndexType getIndexType() {
        return indexType;
    }

    /**
     * Gets the raw target.
     */
    public final int getTarget() {
        return target;
    }

    /**
     * Gets the relative target.
     */
    public final int getRelativeTarget() {
        return relativeTarget;
    }

    /**
     * Gets the target as a relative offset from the given address.
     */
    public final int getTarget(int baseAddress) {
        return target - baseAddress;
    }

    /**
     * Sets the target address.
     */
    public void setTarget(int target) {

        this.target = target;
    }

    /**
     * Gets the target as a relative offset from the given base address, as a
     * code unit. This will throw if the value is out of the range of a signed
     * code unit.
     */
    public final short getTargetUnit(int baseAddress) {
        int relativeTarget = getTarget(baseAddress);

        if (relativeTarget != (short) relativeTarget) {
            throw new DexException("Target out of range: "
                    + Hex.s4(relativeTarget));
        }

        return (short) relativeTarget;
    }

    /**
     * Gets the target as a relative offset from the given base address, masked
     * to be a byte in size. This will throw if the value is out of the range of
     * a signed byte.
     */
    public final int getTargetByte(int baseAddress) {
        int relativeTarget = getTarget(baseAddress);

        if (relativeTarget != (byte) relativeTarget) {
            throw new DexException("Target out of range: "
                    + Hex.s4(relativeTarget));
        }

        return relativeTarget & 0xff;
    }

    public final long getLiteral() {
        return literal;
    }

    public void setLiteral(long literal) {
        this.literal = literal;

    }

    /**
     * Gets the literal value, masked to be an int in size. This will throw if
     * the value is out of the range of a signed int.
     */
    public final int getLiteralInt() {
        if (literal != (int) literal) {
            throw new DexException("Literal out of range: " + Hex.u8(literal));
        }

        return (int) literal;
    }

    /**
     * Gets the literal value, as a code unit. This will throw if the value is
     * out of the range of a signed code unit.
     */
    public final short getLiteralUnit() {
        if (literal != (short) literal) {
            throw new DexException("Literal out of range: " + Hex.u8(literal));
        }

        return (short) literal;
    }

    /**
     * Gets the literal value, masked to be a byte in size. This will throw if
     * the value is out of the range of a signed byte.
     */
    public final int getLiteralByte() {
        if (literal != (byte) literal) {
            throw new DexException("Literal out of range: " + Hex.u8(literal));
        }

        return (int) literal & 0xff;
    }

    /**
     * Gets the literal value, masked to be a nibble in size. This will throw if
     * the value is out of the range of a signed nibble.
     */
    public final int getLiteralNibble() {
        if ((literal < -8) || (literal > 7)) {
            throw new DexException("Literal out of range: " + Hex.u8(literal));
        }

        return (int) literal & 0xf;
    }

    /**
     * Returns the number of registers used by the instruction.
     * 
     * @return registers used by the instruction.
     */
    public abstract int getRegisterCount();

    /**
     * Returns the value of register A. If setA() has not already been called
     * this function throws an exception.
     * 
     * @return the register number referenced.
     * @throws IllegalStateException if a call to setA(Integer) has not been
     *             called before.
     */
    public final int getA() {
        checkRegisterUsage(RegisterReference.A);

        if (regRefA == null)
            throw new NoSuchElementException(
                    "setA(Integer) must be called first.");

        return regRefA;
    }

    public final int getB() {
        checkRegisterUsage(RegisterReference.B);
        if (regRefB == null)
            throw new NoSuchElementException(
                    "setB(Integer) must be called first.");

        return regRefB;
    }

    public final int getC() {
        checkRegisterUsage(RegisterReference.C);
        if (regRefC == null)
            throw new NoSuchElementException(
                    "setC(Integer) must be called first.");

        return regRefC;
    }

    public final int getD() {
        checkRegisterUsage(RegisterReference.D);
        if (regRefD == null)
            throw new NoSuchElementException(
                    "setD(Integer) must be called first.");

        return regRefD;
    }

    public final int getE() {
        checkRegisterUsage(RegisterReference.E);
        if (regRefE == null)
            throw new NoSuchElementException(
                    "setE(Integer) must be called first.");

        return regRefE;
    }

    /**
     * Returns the register type used by register reference A.
     * 
     * @return the register type used by the instruction for reference A.
     * @throws UnsupportedOperationException if the instruction does not use
     *             this register reference.
     * @throws NoSuchElementException if this instruction does not use this
     *             register reference.
     */
    public final RegisterType getTypeA() {
        checkRegisterUsage(RegisterReference.A);

        return RegisterType.newInstance(kRegTypeUnknown);
    }

    /**
     * Returns the register type used by register reference B.
     * 
     * @return the register type used by the instruction for reference B.
     * @throws UnsupportedOperationException if the instruction does not use
     *             this register reference.
     */
    public final RegisterType getTypeB() {

        checkRegisterUsage(RegisterReference.B);
        return RegisterType.newInstance(kRegTypeUnknown);
    }

    /**
     * Returns the register type used by register reference C.
     * 
     * @return the register type used by the instruction for reference C.
     * @throws UnsupportedOperationException if the instruction does not use
     *             this register reference.
     */
    public final RegisterType getTypeC() {

        checkRegisterUsage(RegisterReference.C);
        return RegisterType.newInstance(kRegTypeUnknown);
    }

    /**
     * Returns the register type used by register reference D.
     * 
     * @return the register type used by the instruction for reference D.
     * @throws UnsupportedOperationException if the instruction does not use
     *             this register reference.
     */
    public final RegisterType getTypeD() {
        checkRegisterUsage(RegisterReference.D);
        return RegisterType.newInstance(kRegTypeUnknown);
    }

    /**
     * Returns the register type used by register reference E.
     * 
     * @return the register type used by the instruction for reference E.
     * @throws UnsupportedOperationException if the instruction does not use
     *             this register reference.
     */
    public final RegisterType getTypeE() {

        checkRegisterUsage(RegisterReference.E);
        return RegisterType.newInstance(kRegTypeUnknown);
    }

    /**
     * Returns the value of register reference A. If this function is not
     * overridden, then a runtime exception is thrown to indicate to the
     * developer that the instruction does not handle this register reference.
     * 
     * @param val the value to set this register reference to.
     * @throws UnsupportedOperationException if this function is not overridden
     *             by an instruction class tha uses this register reference.
     */
    public final void setA(Integer val) {
        checkRegisterUsage(RegisterReference.A);
        checkRegisterValue(val);
        regRefA = val;
    };

    /**
     * Returns the value of register reference B. If this function is not
     * overridden, then a runtime exception is thrown to indicate to the
     * developer that the instruction does not handle this register reference.
     * 
     * @param val the value to set this register reference to.
     * @throws UnsupportedOperationException if this function is not overridden
     *             by an instruction class tha uses this register reference.
     */
    public final void setB(Integer val) {
        checkRegisterUsage(RegisterReference.B);
        checkRegisterValue(val);
        regRefB = val;
    };

    /**
     * Returns the value of register reference C. If this function is not
     * overridden, then a runtime exception is thrown to indicate to the
     * developer that the instruction does not handle this register reference.
     * 
     * @param val the value to set this register reference to.
     * @throws UnsupportedOperationException if this function is not overridden
     *             by an instruction class tha uses this register reference.
     */
    public final void setC(Integer val) {
        checkRegisterUsage(RegisterReference.C);
        checkRegisterValue(val);
        regRefC = val;
    };

    /**
     * Returns the value of register reference D. If this function is not
     * overridden, then a runtime exception is thrown to indicate to the
     * developer that the instruction does not handle this register reference.
     * 
     * @param val the value to set this register reference to.
     * @throws UnsupportedOperationException if this function is not overridden
     *             by an instruction class tha uses this register reference.
     */
    public final void setD(Integer val) {
        checkRegisterUsage(RegisterReference.D);
        checkRegisterValue(val);
        regRefD = val;
    };

    /**
     * Returns the value of register reference E. If this function is not
     * overridden, then a runtime exception is thrown to indicate to the
     * developer that the instruction does not handle this register reference.
     * 
     * @param val the value to set this register reference to.
     * @throws UnsupportedOperationException if this function is not overridden
     *             by an instruction class tha uses this register reference.
     */
    public final void setE(Integer val) {
        checkRegisterUsage(RegisterReference.E);
        checkRegisterValue(val);
        regRefE = val;
    };

    /**
     * Gets the register count, as a code unit. This will throw if the value is
     * out of the range of an unsigned code unit.
     */
    public final short getRegisterCountUnit() {
        int registerCount = getRegisterCount();

        if ((registerCount & ~0xffff) != 0) {
            throw new DexException("Register count out of range: "
                    + Hex.u8(registerCount));
        }

        return (short) registerCount;
    }

    /**
     * Gets the A register number, as a code unit. This will throw if the value
     * is out of the range of an unsigned code unit.
     */
    public final short getAUnit() {
        int a = getA();

        if ((a & ~0xffff) != 0) {
            throw new DexException("Register A out of range: " + Hex.u8(a));
        }

        return (short) a;
    }

    /**
     * Gets the A register number, as a byte. This will throw if the value is
     * out of the range of an unsigned byte.
     */
    public final short getAByte() {
        int a = getA();

        if ((a & ~0xff) != 0) {
            throw new DexException("Register A out of range: " + Hex.u8(a));
        }

        return (short) a;
    }

    /**
     * Gets the A register number, as a nibble. This will throw if the value is
     * out of the range of an unsigned nibble.
     */
    public final short getANibble() {
        int a = getA();

        if ((a & ~0xf) != 0) {
            throw new DexException("Register A out of range: " + Hex.u8(a));
        }

        return (short) a;
    }

    /**
     * Gets the B register number, as a code unit. This will throw if the value
     * is out of the range of an unsigned code unit.
     */
    public final short getBUnit() {
        int b = getB();

        if ((b & ~0xffff) != 0) {
            throw new DexException("Register B out of range: " + Hex.u8(b));
        }

        return (short) b;
    }

    /**
     * Gets the B register number, as a byte. This will throw if the value is
     * out of the range of an unsigned byte.
     */
    public final short getBByte() {
        int b = getB();

        if ((b & ~0xff) != 0) {
            throw new DexException("Register B out of range: " + Hex.u8(b));
        }

        return (short) b;
    }

    /**
     * Gets the B register number, as a nibble. This will throw if the value is
     * out of the range of an unsigned nibble.
     */
    public final short getBNibble() {
        int b = getB();

        if ((b & ~0xf) != 0) {
            throw new DexException("Register B out of range: " + Hex.u8(b));
        }

        return (short) b;
    }

    /**
     * Gets the C register number, as a code unit. This will throw if the value
     * is out of the range of an unsigned code unit.
     */
    public final short getCUnit() {
        int c = getC();

        if ((c & ~0xffff) != 0) {
            throw new DexException("Register C out of range: " + Hex.u8(c));
        }

        return (short) c;
    }

    /**
     * Gets the C register number, as a byte. This will throw if the value is
     * out of the range of an unsigned byte.
     */
    public final short getCByte() {
        int c = getC();

        if ((c & ~0xff) != 0) {
            throw new DexException("Register C out of range: " + Hex.u8(c));
        }

        return (short) c;
    }

    /**
     * Gets the C register number, as a nibble. This will throw if the value is
     * out of the range of an unsigned nibble.
     */
    public final short getCNibble() {
        int c = getC();

        if ((c & ~0xf) != 0) {
            throw new DexException("Register C out of range: " + Hex.u8(c));
        }

        return (short) c;
    }

    /**
     * Gets the D register number, as a code unit. This will throw if the value
     * is out of the range of an unsigned code unit.
     */
    public final short getDUnit() {
        int d = getD();

        if ((d & ~0xffff) != 0) {
            throw new DexException("Register D out of range: " + Hex.u8(d));
        }

        return (short) d;
    }

    /**
     * Gets the D register number, as a byte. This will throw if the value is
     * out of the range of an unsigned byte.
     */
    public final short getDByte() {
        int d = getD();

        if ((d & ~0xff) != 0) {
            throw new DexException("Register D out of range: " + Hex.u8(d));
        }

        return (short) d;
    }

    /**
     * Gets the D register number, as a nibble. This will throw if the value is
     * out of the range of an unsigned nibble.
     */
    public final short getDNibble() {
        int d = getD();

        if ((d & ~0xf) != 0) {
            throw new DexException("Register D out of range: " + Hex.u8(d));
        }

        return (short) d;
    }

    /**
     * Gets the E register number, as a nibble. This will throw if the value is
     * out of the range of an unsigned nibble.
     */
    public final short getENibble() {
        int e = getE();

        if ((e & ~0xf) != 0) {
            throw new DexException("Register E out of range: " + Hex.u8(e));
        }

        return (short) e;
    }

    /**
     * Encodes this instance to the given output.
     */
    public final void encode(CodeOutput out) {
        format.encode(this, out);
    }

    /**
     * Returns an instance just like this one, except with the index replaced
     * with the given one.
     */
    public abstract DecodedInstruction withIndex(int newIndex);

    public int[] getRegisterReferences() {

        int[] result = new int[getRegisterCount()];
        for (int i = 0; i < result.length; i++) {

            switch (i) {
                case 0:
                    result[i] = getA();
                    break;
                case 1:
                    result[i] = getB();
                    break;

                case 2:
                    result[i] = getC();
                    break;

                case 3:
                    result[i] = getD();
                    break;

                case 4:
                    result[i] = getE();
                    break;

            }
        }

        return result;

    }

    /**
     * <p>
     * Implement category-1 "move" instructions. Determine register types for
     * the copy copy of a 32-bit value from the source register (getB()) to a
     * destination register (getA()).
     * <p>
     * Fills the specified registerTypes sparse array with the register types
     * appropriate to a narrow copy operation (a copy of a single register
     * move). This is also known as a If this instructions register numbers
     * don't appear in registerTypes they are added.
     * <p>
     * If a register already exists in registerTypes, then the registers in the
     * operation are checked against the register in registerTypes. A check for
     * compatibility is also made. The particular registers checked depend on
     * the value from getOpcode(). If the register cannot be copied due to
     * register type incompatability, then an exception is thrown and the
     * registerType is left in an undefined state.
     * 
     * @param registerTypes the register types to fill.
     * @param defaultType the default register
     * @throws VerifyException
     */
    protected void copyRegister1(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, RegisterTypeEnum defaultType)
            throws VerifyException {
        // getA() is the destination, getB() is the source.
        // source and destination categories need to be the same
        int destReg = getA();
        int sourceReg = getB();
        RegisterType destType = registerTypes.get(destReg);
        RegisterType srcType = registerTypes.get(sourceReg, instruction);

        if (srcType == null)
            registerTypes.put(sourceReg, RegisterType.newInstance(defaultType),
                    instruction, null);
        else if (srcType.typeEnum().category() != defaultType.category())
            // if defaultTYpe is not the same as what
            // was found, throw an exception.
            throw new VerifyException(
                    VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: copy1 error in source register v%u<-v%u type=%d cat=%d",
                            destReg, sourceReg, srcType.toString(), srcType
                                    .typeEnum().category().toString()));

        if (destType == null)
            registerTypes.put(destReg, RegisterType.newInstance(defaultType),
                    instruction, new Integer(sourceReg));
        else if (destType.typeEnum().category() != defaultType.category())
            // if the register category specified is not the same as what
            // was found, throw an exception.
            throw new VerifyException(
                    VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: copy1 error in destination register v%u<-v%u type=%d cat=%d",
                            destReg, sourceReg, srcType.toString(), srcType
                                    .typeEnum().category().toString()));
    }

    /**
     * <p>
     * Implement category-1 "move" instructions. Determine register types for
     * the copy copy of a 64-bit value from the source register (getB()) to a
     * destination register (getA()).
     * <p>
     * Fills the specified registerTypes sparse array with the register types
     * appropriate to a narrow copy operation (a copy of a single register
     * move). This is also known as a If this instructions register numbers
     * don't appear in registerTypes they are added.
     * <p>
     * If a register already exists in registerTypes, then the registers in the
     * operation are checked against the register in registerTypes. A check for
     * compatibility is also made. The particular registers checked depend on
     * the value from getOpcode(). If the register cannot be copied due to
     * register type incompatability, then an exception is thrown and the
     * registerType is left in an undefined state.
     * <p>
     * For this function, the default register type is
     * RegisterType.kRegTypeAnyCategory2
     * 
     * @param registerTypes the register types to fill.
     * @throws VerifyException
     */
    protected void copyRegister2(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes) throws VerifyException {
        // getA() is the destination, getB() is the source.
        // source and destination categories need to be the same
        int destReg = getA();
        int srcReg = getB();

        int destRegLow = destReg;
        int destRegHigh = destReg + 1;

        int srcRegLow = srcReg;
        int srcRegHigh = srcReg + 1;

        RegisterType destRegTypeLow = registerTypes.get(destRegLow);
        RegisterType destRegTypeHigh = registerTypes.get(destRegHigh);

        RegisterType srcRegTypeLow = registerTypes.get(srcRegLow, instruction);
        RegisterType srcRegTypeHigh = registerTypes
                .get(srcRegHigh, instruction);

        /*
         * The original C code only checks these first two conditions
         */
        if (srcRegTypeLow == null) {
            throw new IllegalStateException(
                    "source low register has not been set.");
        } else {
            if (srcRegTypeLow.typeEnum().category() != kRegTypeAnyCategory2
                    .category())
                throw new VerifyException(
                        VerifyErrorCause.VERIFY_ERROR_GENERIC,
                        String.format(
                                "VFY: copy2 error in the low source register v%u<-v%u type=%d/%d",
                                destRegLow, srcRegLow,
                                srcRegTypeLow.toString(),
                                srcRegTypeHigh.toString()));

        }

        if (srcRegTypeHigh == null) {
            throw new IllegalStateException(
                    "source high register has not been set.");
        } else {
            if (srcRegTypeHigh.typeEnum().category() != kRegTypeAnyCategory2
                    .category())
                throw new VerifyException(
                        VerifyErrorCause.VERIFY_ERROR_GENERIC,
                        String.format(
                                "VFY: copy2 error in the high source register v%u<-v%u type=%d/%d",
                                destRegHigh, srcRegHigh,
                                srcRegTypeLow.toString(),
                                srcRegTypeHigh.toString()));

        }

        checkWidePair(srcRegTypeLow, srcRegTypeHigh);

        /*
         * C code stops checking here. This limited check may be for a reason,
         * such as a requirement that the register read from has a specific
         * category, but the destination register doesn't matter. It's not clear
         * however, if the requirement to check the testination type is a side
         * effect or of it's just not checked for by the VM. See Luo's paper,
         * where he makes it clear that it's the register types that are read
         * that is the most important thing. This approach is call type
         * inferencing.
         */

        // for now we check both the reads and the writes, because register
        // types not set are given a default type

        registerTypes.put(destRegLow, srcRegTypeLow, instruction, srcRegLow);
        registerTypes.put(destRegHigh, srcRegTypeHigh, instruction, srcRegHigh);

    }

    /**
     * For a category 2 register pair, verify that "typeh" is the appropriate
     * high part for "typel".
     * <p>
     * Does not verify that "typel" is in fact the low part of a 64-bit register
     * pair.
     */
    private void checkWidePair(RegisterType typel, RegisterType typeh)
            throws VerifyException {
        if (typeh.typeEnum().ordinal() != typel.typeEnum().ordinal() + 1)
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    "VFY: high and low register pairs are not compatible.");
    }

    /**
     * Implement "move-result". Copy the category-1 value from the result
     * register to another register, and reset the result register.
     * 
     * @throws VerifyException
     */
    protected void copyResultRegister1(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, final int insnRegCount,
            RegisterType defaultType /* cat */) throws VerifyException {

        int destRegister = getA(); // vdst
        int srcRegister = insnRegCount; // vsrc;

        if (destRegister >= srcRegister)
            throw new VerifyException(
                    VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: destination register v%u is too high. Expecting a register less than v&y",
                            destRegister, srcRegister));

        RegisterType sourceRegType = registerTypes
                .get(srcRegister, instruction); // type

        if (sourceRegType == null)
            throw new IllegalStateException(
                    "source register type has not been set.");

        if (sourceRegType.typeEnum().category() != defaultType.typeEnum()
                .category()) {
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("VFY: copyRes1 v%u<-v%u cat=%d type=%d",
                            destRegister, srcRegister, defaultType.typeEnum()
                                    .category().toString(),
                            sourceRegType.toString()));
        } else {
            registerTypes.put(destRegister, sourceRegType, instruction,
                    srcRegister);
            registerTypes.delete(srcRegister);
            // removing sets the type to unknown.
            // setRegisterType(registerLine, vsrc, kRegTypeUnknown);
            /* target monitor stack bits will be cleared */
            // TODO what is the side effect here with the monitor stack?
        }
    }

    /*
     * Implement "move-result-wide". Copy the category-2 value from the result
     * register to another register, and reset the result register.
     */
    protected void copyResultRegister2(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, final int insnRegCount)
            throws VerifyException {

        int destRegister = getA(); // vdst
        int srcRegister = insnRegCount; // vsrc;

        if (destRegister >= srcRegister)
            throw new VerifyException(
                    VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: destination register v%u is too high. Expecting a register less than v&y",
                            destRegister, srcRegister));

        RegisterType typel = registerTypes.get(srcRegister, instruction);
        RegisterType typeh = registerTypes.get(srcRegister + 1, instruction);

        if (typel == null)
            throw new IllegalArgumentException(
                    "low source register has not been set.");

        if (typeh == null)
            throw new IllegalArgumentException(
                    "high source register has not been set.");

        if (typel.typeEnum().category() != kRegTypeAnyCategory2.category())
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("VFY: copyRes2 v%u<-v%u type=%d/%d",
                            destRegister, srcRegister, typel.typeEnum()
                                    .category().toString(), typeh.typeEnum()
                                    .category().toString()));

        checkWidePair(typel, typeh);
        registerTypes.put(destRegister, typel, instruction, srcRegister);

        // like in copyResultRegister1, simply delete the register reference in
        // the array to make it unknown.
        registerTypes.delete(srcRegister);
        registerTypes.delete(srcRegister + 1);

    }

    /**
     * Returns true if this instruction is one that requires a range of values
     * 
     * @return
     */
    public boolean isRange() {
        return (getOpcode() == FILLED_NEW_ARRAY_RANGE || getOpcode() == FILLED_NEW_ARRAY_JUMBO) ? true
                : false;

    }

    /**
     * Indicates whether or not this instruction was created by decoding a
     * register list by InstructionCodec.decodeRegisterList. If this value is
     * true, it is because InstructionCode is causing the following things to
     * happen: the register values are all from 0-15, and when encoded
     * InstructionCodec.encodeRegisterList will be used.
     * <p>
     * When injecting code, use the value returned by this function to check
     * whether or not register references are contained between 0-15. If not,
     * the verification of the program will fail when loaded by the device.
     */
    public boolean isDecodedFromRegisterList() {
        return decodedFromRegisterList;
    }

    /**
     * Verify that the arguments in a filled-new-array instruction are valid. If
     * the verification passes this function does nothing. If the verification
     * doesn't pass then an exception is thrown. "resClass" is the class refered
     * to by pDecInsn->vB.
     */
    private void verifyFilledNewArrayRegs(
            RegisterTypeSparseArray registerTypes, RegisterType expectedType)
            throws VerifyException {

        RegisterTypeEnum expectedTypeEnum = expectedType.typeEnum();

        int argCount = getA();
        boolean isRange = isRange();

        // TODO implement check for array class (elsewhere).
        /*
         * Verify each register. If "argCount" is bad, verifyRegisterType() will
         * run off the end of the list and fail. It's legal, if silly, for
         * argCount to be zero.
         */
        for (int ui = 0; ui < argCount; ui++) {
            int getReg;

            if (isRange)
                getReg = getC() + ui;
            else {
                // getReg = pDecInsn->arg[ui];
                // in the C code the arg array contains the register references
                // (0-4);

                switch (ui) {
                    case 0:
                        getReg = getA();
                        break;
                    case 1:
                        getReg = getB();
                        break;
                    case 2:
                        getReg = getC();
                        break;
                    case 3:
                        getReg = getD();
                        break;
                    case 4:
                        getReg = getE();
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "unable to get register " + ui + ". max = 4");
                }
            }

            // filled-new-array
            try {
                RegisterType.verifyRegisterType(registerTypes, getReg,
                        expectedType, this);
            } catch (VerifyException ex) {
                throw new VerifyException(
                        VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
                                "VFY: filled-new-array arg %u(%u) not valid",
                                ui, getReg));

            }
        }
    }

    protected void aput_1nr_common(RegisterTypeSparseArray registerTypes,
            RegisterType tmpType) {

        // TODO implement this function.
    }

    /**
     * Verify types for a simple two-register instruction (e.g. "neg-int").
     * "dstType" is stored into vA, and "srcType" is verified against vB.
     * 
     * @throws VerifyException
     */
    protected void checkUnop(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, RegisterTypeEnum dstType,
            RegisterTypeEnum srcType) throws VerifyException {

        RegisterType.verifyRegisterType(registerTypes, getB(),
                RegisterType.newInstance(srcType), this);

        registerTypes.put(getA(), RegisterType.newInstance(dstType),
                instruction, null);
    }

    /**
     * Verify types for a simple three-register instruction (e.g. "add-int").
     * "dstType" is stored into vA, and "srcType1"/"srcType2" are verified
     * against vB/vC.
     * 
     * @throws VerifyException
     */
    protected void checkBinop(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, RegisterTypeEnum dstType,
            RegisterTypeEnum srcType1, RegisterTypeEnum srcType2,
            boolean checkBooleanOp) throws VerifyException {
        RegisterType.verifyRegisterType(registerTypes, getB(),
                RegisterType.newInstance(srcType1), this);
        RegisterType.verifyRegisterType(registerTypes, getC(),
                RegisterType.newInstance(srcType2), this);
        if (checkBooleanOp) {
            if (dstType != kRegTypeInteger)
                throw new IllegalArgumentException(
                        "dstType must be type kRegTypeInteger if checkBooleanOp is true.");

            if (upcastBooleanOp(registerTypes, getB(), getC()))
                dstType = kRegTypeBoolean;
        }
        registerTypes.put(getA(), RegisterType.newInstance(dstType),
                instruction, null);
    }

    /**
     * We're performing an operation like "and-int/2addr" that can be performed
     * on booleans as well as integers. We get no indication of boolean-ness,
     * but we can infer it from the types of the arguments.
     * <p>
     * Assumes we've already validated reg1/reg2.
     * <p>
     * TODO: consider generalizing this. The key principle is that the result of
     * a bitwise operation can only be as wide as the widest of the operands.
     * You can safely AND/OR/XOR two chars together and know you still have a
     * char, so it's reasonable for the compiler or "dx" to skip the int-to-char
     * instruction. (We need to do this for boolean because there is no
     * int-to-boolean operation.) Returns true if both args are Boolean, Zero,
     * or One.
     */
    private boolean upcastBooleanOp(RegisterTypeSparseArray registerTypes,
            int reg1, int reg2) {

        RegisterTypeEnum type1 = registerTypes.get(reg1).typeEnum();
        RegisterTypeEnum type2 = registerTypes.get(reg2).typeEnum();

        if ((type1 == kRegTypeBoolean || type1 == kRegTypeZero || type1 == kRegTypeOne)
                && (type2 == kRegTypeBoolean || type2 == kRegTypeZero || type2 == kRegTypeOne)) {
            return true;
        }
        return false;
    }

    /**
     * Verify types for a binary "2addr" operation. "srcType1"/"srcType2" are
     * verified against vA/vB, then "dstType" is stored into vA.
     * 
     * @throws VerifyException
     */
    protected void checkBinop2addr(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, RegisterTypeEnum dstType,
            RegisterTypeEnum srcType1, RegisterTypeEnum srcType2,
            boolean checkBooleanOp) throws VerifyException {
        RegisterType.verifyRegisterType(registerTypes, getA(),
                RegisterType.newInstance(srcType1), this);
        RegisterType.verifyRegisterType(registerTypes, getB(),
                RegisterType.newInstance(srcType2), this);
        if (checkBooleanOp) {

            if (dstType != kRegTypeInteger)
                throw new IllegalArgumentException(
                        "dstType must be type kRegTypeInteger if checkBooleanOp is true.");

            if (upcastBooleanOp(registerTypes, getA(), getB()))
                dstType = kRegTypeBoolean;
        }
        registerTypes.put(getA(), RegisterType.newInstance(dstType),
                instruction, null);
    }

    /**
     * Verify types for A two-register instruction with a literal constant (e.g.
     * "add-int/lit8"). "dstType" is stored into vA, and "srcType" is verified
     * against vB. If "checkBooleanOp" is set, we use the constant value in vC.
     * 
     * @throws VerifyException
     */
    protected void checkLitop(DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, RegisterTypeEnum dstType,
            RegisterTypeEnum srcType, boolean checkBooleanOp)
            throws VerifyException {
        RegisterType.verifyRegisterType(registerTypes, getB(),
                RegisterType.newInstance(srcType), this);
        if (checkBooleanOp) {
            if (dstType != kRegTypeInteger)
                throw new IllegalArgumentException(
                        "dstType must be type kRegTypeInteger if checkBooleanOp is true.");

            /* check vB with the call, then check the constant manually */
            if (upcastBooleanOp(registerTypes, getB(), getB()) // TODO is this
                                                               // checking the
                                                               // same register
                                                               // correct here?
                    && (getC() == 0 || getC() == 1)) {
                dstType = kRegTypeBoolean;
            }
        }
        registerTypes.put(getA(), RegisterType.newInstance(dstType),
                instruction, null);
    }

    /**
     * <p>
     * Treat right-shifting as a narrowing conversion when possible.
     * <p>
     * For example, right-shifting an int 24 times results in a value that can
     * be treated as a byte.
     * <p>
     * Things get interesting when contemplating sign extension. Right- shifting
     * an integer by 16 yields a value that can be represented in a "short" but
     * not a "char", but an unsigned right shift by 16 yields a value that
     * belongs in a char rather than a short. (Consider what would happen if the
     * result of the shift were cast to a char or short and then cast back to an
     * int. If sign extension, or the lack thereof, causes a change in the
     * 32-bit representation, then the conversion was lossy.)
     * <p>
     * A signed right shift by 17 on an integer results in a short. An unsigned
     * right shfit by 17 on an integer results in a posshort, which can be
     * assigned to a short or a char.
     * <p>
     * An unsigned right shift on a short can actually expand the result into a
     * 32-bit integer. For example, 0xfffff123 >>> 8 becomes 0x00fffff1, which
     * can't be represented in anything smaller than an int.
     * <p>
     * javac does not generate code that takes advantage of this, but some of
     * the code optimizers do. It's generally a peephole optimization that
     * replaces a particular sequence, e.g. (bipush 24, ishr, i2b) is replaced
     * by (bipush 24, ishr). Knowing that shifting a short 8 times to the right
     * yields a byte is really more than we need to handle the code that's out
     * there, but support is not much more complex than just handling integer.
     * <p>
     * Right-shifting never yields a boolean value.
     * <p>
     * Returns the new register type if shiftCount != 0, otherwise returns the
     * register in position reg.
     * 
     * @throws VerifyException
     */
    protected RegisterTypeEnum adjustForRightShift(
            DecodedInstruction instruction,
            RegisterTypeSparseArray registerTypes, int reg, int shiftCount,
            boolean isUnsignedShift) throws VerifyException {
        RegisterTypeEnum srcType = registerTypes.get(reg).typeEnum();
        RegisterTypeEnum newType;

        /* convert const derived types to their actual types */
        srcType = constTypeToRegType(srcType);

        /* no-op */
        if (shiftCount == 0)
            // return the same object
            return registerTypes.get(reg).typeEnum();

        /* safe defaults */
        if (isUnsignedShift)
            newType = kRegTypeInteger;
        else
            newType = srcType;

        if (shiftCount >= 32) {
            throw new VerifyException(VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format("Got unexpectedly large shift count %u",
                            shiftCount));
            /* fail? */
        }

        switch (srcType) {
            case kRegTypeInteger: /* 32-bit signed value */
                if (isUnsignedShift) {
                    if (shiftCount > 24)
                        newType = kRegTypePosByte;
                    else if (shiftCount >= 16)
                        newType = kRegTypeChar;
                } else {
                    if (shiftCount >= 24)
                        newType = kRegTypeByte;
                    else if (shiftCount >= 16)
                        newType = kRegTypeShort;
                }
                break;
            case kRegTypeShort: /* 16-bit signed value */
                if (isUnsignedShift) {
                    /* default (kRegTypeInteger) is correct */
                } else {
                    if (shiftCount >= 8)
                        newType = kRegTypeByte;
                }
                break;
            case kRegTypePosShort: /* 15-bit unsigned value */
                if (shiftCount >= 8)
                    newType = kRegTypePosByte;
                break;
            case kRegTypeChar: /* 16-bit unsigned value */
                if (shiftCount > 8)
                    newType = kRegTypePosByte;
                break;
            case kRegTypeByte: /* 8-bit signed value */
                /* defaults (u=kRegTypeInteger / s=srcType) are correct */
                break;
            case kRegTypePosByte: /* 7-bit unsigned value */
                /* always use newType=srcType */
                newType = srcType;
                break;
            case kRegTypeZero: /* 1-bit unsigned value */
            case kRegTypeOne:
            case kRegTypeBoolean:
                /* unnecessary? */
                newType = kRegTypeZero;
                break;
            default:
                /* long, double, references; shouldn't be here! */
                throw new IllegalArgumentException(
                        "long, double, references should not be checked by this function.");
        }

        if (DO_LOG) {

            if (newType != srcType) {
                Log.v(LOG_TAG, String.format("narrowing: %d(%d) --> %d to %d",
                        shiftCount, isUnsignedShift, srcType, newType));
            } else {
                Log.v(LOG_TAG, String.format("not narrowed: %d(%d) --> %d",
                        shiftCount, isUnsignedShift, srcType));
            }
        }
        return newType;
    }

    /**
     * Performs a simple check on the instruction to ensure that it references a
     * sufficient number of registers to access the specified RegisterReference.
     * If this check fails, then an UnsupportedOperationException is thrown. If
     * this check passes, then the function completes without any side effect or
     * operation.
     * 
     * @param ref the register reference to check.
     * @throws NoSuchElementException if the check fails.
     */
    public void checkRegisterUsage(RegisterReference ref) {

        if (ref == null)
            throw new IllegalArgumentException("ref cannot be null.");

        int regCount = getRegisterCount();
        int minRegsRequired = ref.minRegistersRequired();

        if (!decodedFromRegisterList
                && getRegisterCount() < ref.minRegistersRequired())
            throw new NoSuchElementException(
                    String.format(
                            "instruction %s (%d) uses %d registers but the RegisterReference requires %d.",
                            this.getOpcodeEnum().toString(), this.getOpcode(),
                            regCount, minRegsRequired));

    }

    /**
     * Checks to ensure that a register value is within the proper range. If
     * this function does not throw an exception then everything is O.K. and the
     * function executes without side effect (does nothing).
     * 
     * @throws IllegalArgumentException if val is null.
     * @throws IllegalArgumentException if val is not a number from 0 -
     *             MAX_REG_VALUE.
     */
    protected void checkRegisterValue(Integer val) {
        if (val == null)
            throw new IllegalArgumentException("val cannot be null");

        int v = val.intValue();
        if (v < 0 || v > MAX_REG_VALUE)
            throw new IllegalArgumentException(String.format(
                    "register references must be a number 0 - %d",
                    MAX_REG_VALUE));

    }

    /**
     * Return the register type for a reference given this instruction's
     * instance. More specific than OpcodeInfo or InstructionCodec values.
     * <p>
     * Returns register usage information that the instruction can provide
     * without actually knowing about the register usage from other instructions
     * in the method.
     * 
     * @param ref the register reference to retrieve information for.
     * @return the register type.
     */
    public RegisterType getRegisterType(RegisterReference ref) {
        OpcodeInfo.Info opInfo = OpcodeInfo.get(opcode);
        return opInfo.getRegisterType(this, ref);
    }

    /**
     * Return the register type for a reference given this instruction's
     * instance. Similar to getRegisterType(RegisterReference), but this
     * function resolves the register by the register reference number itself.
     * 
     * @param ref the register reference to retrieve information for.
     * @return the register type.
     */
    public RegisterType getRegisterType(int vNum) {
        OpcodeInfo.Info opInfo = OpcodeInfo.get(opcode);
        return opInfo.getRegisterType(this, vNum);
    }

    /**
     * For the "move-exception" instruction at "insnIdx", which must be at an
     * exception handler address, determine the first common superclass of all
     * exceptions that can land here. (For javac output, we're probably looking
     * at multiple spans of bytecode covered by one "try" that lands at an
     * exception-specific "catch", but in general the handler could be shared
     * for multiple exceptions.)
     * <p>
     * In the original C function version a NULL is returned, but the failure
     * variable is set, and after the function returns then an exception is
     * thrown. In this Java version, if a valid String cannot be returned then
     * an exception is thrown.
     * 
     * @return the exception type thrown by the instruction.
     * @throws VerifyException if a value cannot be returned
     */
    public String getCaughtExceptionType() throws VerifyException {
        if (!canResolve())
            throw new IllegalStateException(
                    "Unable to determine caught exception type");

        Code pCode = getCode();

        CatchHandler[] handlers = pCode.getCatchHandlers();
        DexBuffer buffer = getBuffer();
        List<String> types = buffer.typeNames();

        String commonSuper = null;

        for (CatchHandler handler : handlers) {
            int[] addresses = handler.getAddresses();
            for (int i = 0; i < addresses.length; i++) {
                int[] typeIndexes = handler.getTypeIndexes();
                String clazz = types.get(typeIndexes[i]);

                if (addresses[i] == getAddress()) {
                    if (commonSuper == null)
                        commonSuper = clazz;
                    else
                        commonSuper = buffer.findCommonSuperclass(clazz,
                                commonSuper);
                }
            }
        }

        if (commonSuper == null)
            /* no catch blocks, or no catches with classes we can find */
            throw new VerifyException(
                    VerifyErrorCause.VERIFY_ERROR_GENERIC,
                    String.format(
                            "VFY: unable to find exception handler at addr %#x",
                            getAddress()));

        return commonSuper;

    }

    /**
     * Returns the contents of A, B, C, D, or E by an index number 0 - 4.
     * 
     * @param index the value of a register refereice by index number.
     * @return the contents of the specified index
     * @throws IllegalArgumentException if the specified index is bad.
     */
    public int getRegisterContentsByIndex(int index) {
        switch (index) {
            case 0:
                return getA();
            case 1:
                return getB();
            case 2:
                return getC();
            case 3:
                return getD();
            case 4:
                return getE();

        }
        throw new IllegalArgumentException("Index must be >=0 and <=4.");
    }

    public boolean isInvokeRange() {

        return (opcode == Opcodes.INVOKE_VIRTUAL_RANGE
                || opcode == Opcodes.INVOKE_VIRTUAL_JUMBO
                || opcode == Opcodes.INVOKE_SUPER_RANGE
                || opcode == Opcodes.INVOKE_SUPER_JUMBO
                || opcode == Opcodes.INVOKE_DIRECT_RANGE
                || opcode == Opcodes.INVOKE_DIRECT_JUMBO
                || opcode == Opcodes.INVOKE_STATIC_RANGE
                || opcode == Opcodes.INVOKE_STATIC_JUMBO
                || opcode == Opcodes.INVOKE_INTERFACE_RANGE || opcode == Opcodes.INVOKE_INTERFACE_JUMBO);
    }

    public boolean isInvokeSuper() {
        return (opcode == Opcodes.INVOKE_SUPER
                || opcode == Opcodes.INVOKE_SUPER_RANGE || opcode == Opcodes.INVOKE_SUPER_JUMBO);
    }

    public boolean isInvokeDirectRange() {
        return (opcode == Opcodes.INVOKE_DIRECT_RANGE || opcode == Opcodes.INVOKE_DIRECT_JUMBO);
    }

    public boolean isInvokeStaticRange() {
        return (opcode == Opcodes.INVOKE_STATIC_RANGE || opcode == Opcodes.INVOKE_STATIC_JUMBO);
    }

    public boolean isInvokeInterfaceRange() {
        return (opcode == Opcodes.INVOKE_INTERFACE_RANGE || opcode == Opcodes.INVOKE_INTERFACE_JUMBO);

    }

    public boolean isReturnInstruction() {
        return (opcode == Opcodes.RETURN || opcode == Opcodes.RETURN_OBJECT
                || opcode == Opcodes.RETURN_VOID
                || opcode == Opcodes.RETURN_VOID_BARRIER || opcode == Opcodes.RETURN_WIDE);
    }

    public boolean isInvoke() {
        return (

        opcode == Opcodes.INVOKE_VIRTUAL
                || opcode == Opcodes.INVOKE_VIRTUAL_RANGE
                || opcode == Opcodes.INVOKE_VIRTUAL_JUMBO
                || opcode == Opcodes.INVOKE_SUPER
                || opcode == Opcodes.INVOKE_SUPER_RANGE
                || opcode == Opcodes.INVOKE_SUPER_JUMBO
                || opcode == Opcodes.INVOKE_DIRECT
                || opcode == Opcodes.INVOKE_DIRECT_RANGE
                || opcode == Opcodes.INVOKE_DIRECT_JUMBO
                || opcode == Opcodes.INVOKE_STATIC
                || opcode == Opcodes.INVOKE_STATIC_RANGE
                || opcode == Opcodes.INVOKE_STATIC_JUMBO
                || opcode == Opcodes.INVOKE_INTERFACE
                || opcode == Opcodes.INVOKE_INTERFACE_RANGE || opcode == Opcodes.INVOKE_INTERFACE_JUMBO);
    }

    public boolean isInvokeDirect() {
        return (opcode == Opcodes.INVOKE_DIRECT
                || opcode == Opcodes.INVOKE_DIRECT_RANGE || opcode == Opcodes.INVOKE_DIRECT_JUMBO);
    }

    public boolean isInvokeInterface() {
        return (

        opcode == Opcodes.INVOKE_INTERFACE
                || opcode == Opcodes.INVOKE_INTERFACE_RANGE || opcode == Opcodes.INVOKE_INTERFACE_JUMBO);
    }

    public boolean isInvokeVirtual() {
        return (

        opcode == Opcodes.INVOKE_VIRTUAL
                || opcode == Opcodes.INVOKE_VIRTUAL_RANGE || opcode == Opcodes.INVOKE_VIRTUAL_JUMBO);
    }

    public boolean isInvokeStatic() {
        return (opcode == Opcodes.INVOKE_STATIC
                || opcode == Opcodes.INVOKE_STATIC_RANGE || opcode == Opcodes.INVOKE_STATIC_JUMBO);
    }

    public void updateRegisters(SparseIntArray registerMap) {
        getFormat().updateRegisters(this, registerMap);

    }

    /**
     * Adjust targets in the instruction set so that they are offset properly.
     * 
     * @param address
     * @param injectedByteCount
     * @param instructionList the list of instructions the address is found in.
     */
    public void updateTargets(InstructionList instructionList, int oldAddress,
            int newAddress) {
        getFormat().updateTarget(instructionList, this, oldAddress, newAddress);
    }

    public int getRegister(RegisterReference ref) {
        switch (ref) {
            case A:
                return getA();
            case B:
                return getB();
            case C:
                return getC();
            case D:
                return getD();
            case E:
                return getE();

        }
        throw new IllegalArgumentException(String.format(
                "reference %s defined but invalid.", ref.toString()));
    }

    public boolean isGoto() {
        return (opcode == Opcodes.GOTO || opcode == Opcodes.GOTO_16 || opcode == Opcodes.GOTO_32);
    }

    public void setRelativeTarget(int newRelativeTarget) {
        relativeTarget = newRelativeTarget;

    }

    public String toLongString() {
        StringBuilder result = new StringBuilder();

        String opcodeString = String.format("%s(%d)@%d ", getOpcodeEnum()
                .toString(), getOpcode(), getAddress());

        result.append(String.format("%-26s ", opcodeString));

        String targetString = getFormat().hasTarget() ? String.format(
                "target:%d", getTarget()) : "";

        result.append(String.format("%-12s", targetString));

        int regCount = getRegisterCount();

        if (isRange() || isInvokeRange()) {
            int vA = getA();
            int vN = vA + regCount - 1;

            result.append(String.format(" vA%d-v%d (%d registers)", vA, vN,
                    regCount));

        } else {

            if (regCount > 0)
                result.append(String.format(" A%-4d", getA()));

            if (regCount > 1)
                result.append(String.format(" B%-4d", getB()));

            if (regCount > 2)
                result.append(String.format(" C%-4d", getC()));

            if (regCount > 3)
                result.append(String.format(" D%-4d", getD()));

            if (regCount > 4)
                result.append(String.format(" E%-4d", getE()));
        }

        return result.toString();

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("%s(%d)@%d ", getOpcodeEnum().toString(),
                getOpcode(), getAddress()));

        String targetString = getFormat().hasTarget() ? String.format("t%d",
                getTarget()) : "";

        result.append(String.format("%-12s", targetString));

        int regCount = getRegisterCount();

        if (isRange() || isInvokeRange()) {
            int vA = getA();
            int vN = vA + regCount - 1;

            result.append(String.format(" vA%d-v%d (%d registers)", vA, vN,
                    regCount));

        } else {

            if (regCount > 0)
                result.append(String.format(" A%d", getA()));

            if (regCount > 1)
                result.append(String.format(" B%d", getB()));

            if (regCount > 2)
                result.append(String.format(" C%d", getC()));

            if (regCount > 3)
                result.append(String.format(" D%d", getD()));

            if (regCount > 4)
                result.append(String.format(" E%d", getE()));
        }

        return result.toString();

    }

    public String toShortString() {

        int regCount = getRegisterCount();

        if (isRange() || isInvokeRange()) {
            int vA = getA();
            int vN = vA + regCount - 1;

            return String.format(" vA%d-v%d (%d registers)", vA, vN, regCount);
        } else {
            return String.format("%s@%d", getOpcodeEnum().toString(), address);
        }

    }

    /**
     * Get the "this" pointer from a non-static method invocation. This returns
     * the RegisterType so the caller can decide whether it needs the reference
     * to be initialized or not. (Can also return kRegTypeZero if the reference
     * can only be zero at this point.)
     * <p>
     * The argument count is in vA, and the first argument is in vC, for both
     * "simple" and "range" versions. We just need to make sure vA is >= 1 and
     * then return vC.
     */
    public RegisterType getInvocationThis(RegisterTypeSparseArray registerTypes) {

        if (!isInvoke())
            throw new IllegalStateException("not an invoke instruction.");

        if (getA() < 1)
            throw new IllegalStateException("VFY: invoke lacks 'this'");

        /* get the element type of the array held in vsrc */
        RegisterType thisType = registerTypes.get(getC(), this);
        RegisterTypeEnum thisTypeEnum = thisType.typeEnum();

        // TODO: Make sure kRegTypeZero is not stored in the register map for
        // this check. It should not be used this way like it is in the C code
        // to indicate an uninitialized class.
        // See regTypeIsReference in the C code.
        if (thisTypeEnum.category() != RegisterCategory.kTypeCategoryRef
                && thisTypeEnum != RegisterTypeEnum.kRegTypeZero) {

            throw new IllegalStateException(
                    String.format(
                            "VFY: tried to get class from non-ref register v%d (type=%s)",
                            getC(), thisType.toString()));

        }

        return thisType;
    }

    /**
     * Returns the method that is invoked by this instruction.
     * 
     * @param pDecInsn the instruction to check.
     * @return the method id of the method that is invoked.
     * @IllegalStateException if this is not an invoke instruction
     */
    public MethodId getInvokedMethodId() {
        if (!isInvoke())
            throw new IllegalStateException("not an invoke instruction.");

        return getBuffer().methodIds().get(getIndex());
    }

    public String getReferencedData() {
        if (getIndexType() == null)
            throw new IllegalStateException("no referenced type has been set");

        switch (getIndexType()) {
            case TYPE_REF:
                return getBuffer().typeNames().get(getIndex());
            case STRING_REF:
                return getBuffer().strings().get(getIndex());
            case FIELD_REF:
                FieldId fId = getBuffer().fieldIds().get(getIndex());
                return String.format("%s in %s", fId.getType(), fId.getName(),
                        fId.getDeclaringClassType());
            default:
                throw new UnsupportedOperationException(String.format(
                        "unable to convert index types %s to string values",
                        getIndexType().toString()));

        }

    }

    /**
     * Returns the prototype id of the method that this instruction calls.
     * 
     * @return the protoid
     * @IllegalStateException if this is not an invoke instruction (from
     *                        getInvokedMethod()
     */
    public ProtoId getInvokedProtoId() {

        MethodId mId = getInvokedMethodId();

        return getBuffer().protoIds().get(mId.getProtoIndex());
    }

    /**
     * Returns the class definition that defines the method returned by
     * getInvokedMethodId. Calls getInvokedMethodId which means this will throw
     * an exception if isInvoke() is false.
     * 
     * @return class that defines the method that this instruction invokes.
     */
    public ClassDef getInvokedClassDef() {
        return getBuffer().getDef(getMethodId().getDeclaringClassSignature());
    }

    public boolean isMoveResult() {
        return (opcode == Opcodes.MOVE_RESULT
                || opcode == Opcodes.MOVE_RESULT_OBJECT || opcode == Opcodes.MOVE_RESULT_WIDE);
    }

    /**
     * Returns a hex string representation of this opcode.
     * 
     * @return
     */
    public String toHexString() {

        short[] encoded = encode();
        StringBuilder builder = new StringBuilder();

        builder.append(toString());
        builder.append(" ");

        for (short s : encoded) {
            builder.append(String.format("%d/%x/%s ", s, s,
                    Integer.toBinaryString(s)));
        }

        return builder.toString();
    }

    public short[] encode() {

        ShortArrayCodeOutput out = new ShortArrayCodeOutput(
                MAX_CODE_UNITS_PER_INSTRUCTION);
        encode(out);
        return out.getArray();

    }

    /**
     * Verifies that this instruction can invoke a method of a given type. This
     * will be implemented later because we don't need to check for this right
     * now. If this check fails then it's a developer error since we're
     * injecting code that is already compiled.
     * 
     * @throws VerifyException
     */
    public void verifyInvocationArgs() throws VerifyException {
        // m-t

        // TODO implement
    }

    public Dop asDop() {
        return Dops.get(opcode);
    }

    public DalvInsn asDalvInsn() {
        return getFormat().getDalvInsn(this);
    }

    public boolean isNewInstance() {

        return (opcode == Opcodes.NEW_INSTANCE || opcode == Opcodes.NEW_INSTANCE_JUMBO);
    }

    public boolean isMoveObject() {
        return (opcode == Opcodes.MOVE_OBJECT
                || opcode == Opcodes.MOVE_OBJECT_16 || opcode == Opcodes.MOVE_OBJECT_FROM16);
    }

    public void setIndex(int index) {

        this.index = index;

    }

    public boolean isObjectGet() {

        return (opcode == Opcodes.IGET_OBJECT
                || opcode == Opcodes.IGET_OBJECT_JUMBO
                || opcode == Opcodes.IGET_OBJECT_QUICK
                || opcode == Opcodes.IGET_OBJECT_VOLATILE
                || opcode == Opcodes.IGET_OBJECT_VOLATILE_JUMBO
                || opcode == Opcodes.SGET_OBJECT
                || opcode == Opcodes.SGET_OBJECT_JUMBO
                || opcode == Opcodes.SGET_OBJECT_VOLATILE
                || opcode == Opcodes.SGET_OBJECT_VOLATILE_JUMBO || opcode == Opcodes.AGET_OBJECT);

    }

    public boolean isMoveResultObject() {
        return (opcode == Opcodes.MOVE_RESULT_OBJECT);

    }

}
