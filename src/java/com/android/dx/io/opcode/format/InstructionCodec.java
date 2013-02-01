/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.dx.io.opcode.format;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeAnyCategory2;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeRef;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.r3.app.DebugConstants.DO_LOG;

import java.io.EOFException;
import java.util.EnumSet;
import java.util.Hashtable;

import schilling.richard.dalvik.vm.InstructionList;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import android.util.Log;
import android.util.SparseIntArray;

import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.Dops;
import com.android.dx.dex.code.SimpleInsn;
import com.android.dx.io.IndexType;
import com.android.dx.io.instructions.CodeInput;
import com.android.dx.io.instructions.CodeOutput;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.FillArrayDataPayloadDecodedInstruction;
import com.android.dx.io.instructions.FiveRegisterDecodedInstruction;
import com.android.dx.io.instructions.FourRegisterDecodedInstruction;
import com.android.dx.io.instructions.OneRegisterDecodedInstruction;
import com.android.dx.io.instructions.PackedSwitchPayloadDecodedInstruction;
import com.android.dx.io.instructions.RegisterRangeDecodedInstruction;
import com.android.dx.io.instructions.SparseSwitchPayloadDecodedInstruction;
import com.android.dx.io.instructions.ThreeRegisterDecodedInstruction;
import com.android.dx.io.instructions.TwoRegisterDecodedInstruction;
import com.android.dx.io.instructions.ZeroRegisterDecodedInstruction;
import com.android.dx.io.opcode.OpcodeInfo;
import com.android.dx.io.opcode.Opcodes;
import com.android.dx.io.opcode.RegisterReference;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.util.DexException;
import com.android.dx.util.Hex;

// TODO create getRegister for each format to return register information that is known for each format.

/**
 * Representation of an instruction format, which knows how to decode into and
 * encode from instances of {@link DecodedInstruction}.
 */
public enum InstructionCodec {
    FORMAT_00X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, opcodeUnit, 0,
                    null, 0, 0, 0L);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray registerMap) {

            return 0;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {

            throw new UnsupportedOperationException(
                    "this format cannot convert (yet)");

        }

    },

    FORMAT_10X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int literal = byte1(opcodeUnit); // should be zero
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            if (instruction.getOpcode() != Opcodes.NOP)
                throw new UnsupportedOperationException(String.format(
                        "%s is unused and cannot be converted.",
                        instruction.toShortString()));

            // FIXME - this is a guess.
            return new SimpleInsn(Dops.NOP, SourcePosition.NO_INFO, RegisterSpecList.EMPTY);

        }
    },

    FORMAT_12X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = nibble2(opcodeUnit);
            int b = nibble3(opcodeUnit);
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcodeUnit(),
                    makeByte(insn.getA(), insn.getB())));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            DecodedInstruction result = instruction;
            int vA = instruction.getA();
            int vB = instruction.getB();
            long literal = instruction.getLiteral();

            switch (instruction.getOpcode()) {
                case Opcodes.MOVE:
                    if (vA > 0xf)
                        result = new TwoRegisterDecodedInstruction(FORMAT_32X,
                                Opcodes.MOVE_16, 0, null, 0, 0, literal, vA, vB);
                    /*
                     * else if (vA > 0xf || vB > 0xf) result = new
                     * TwoRegisterDecodedInstruction(FORMAT_22X,
                     * Opcodes.MOVE_FROM16, 0, null, 0, 0, 0L, vA, vB);
                     */
                    break;

                case Opcodes.MOVE_WIDE:
                    if (vA > 0xff || vB > 0xff)
                        result = new TwoRegisterDecodedInstruction(FORMAT_32X,
                                Opcodes.MOVE_WIDE_16, 0, null, 0, 0, literal, vA,
                                vB);
                    else if (vA > 0xf || vB > 0xf)
                        result = new TwoRegisterDecodedInstruction(FORMAT_22X,
                                Opcodes.MOVE_WIDE_FROM16, 0, null, 0, 0, 0L, vA, vB);
                    break;

                case Opcodes.MOVE_OBJECT:
                    if (vA > 0xff)
                        result = new TwoRegisterDecodedInstruction(FORMAT_32X,
                                Opcodes.MOVE_OBJECT_16, 0, null, 0, 0, literal, vA,
                                vB);
                    else if (vA > 0xf || vB > 0xf)
                        result = new TwoRegisterDecodedInstruction(FORMAT_22X,
                                Opcodes.MOVE_OBJECT_FROM16, 0, null, 0, 0, 0L, vA,
                                vB);
                    break;
            }

            if (result != instruction)
                result.setAddress(instruction.getAddress());

            return result;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            return null;
        }
    },

    FORMAT_11N() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = nibble2(opcodeUnit);
            int literal = (nibble3(opcodeUnit) << 28) >> 28; // sign-extend
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcodeUnit(),
                    makeByte(insn.getA(), insn.getLiteralNibble())));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            DecodedInstruction result = instruction;
            int vA = instruction.getA();
            long literal = instruction.getLiteral();

            switch (instruction.getOpcode()) {
                case Opcodes.CONST_4:
                    if (vA > 0xf) {
                        result = new OneRegisterDecodedInstruction(FORMAT_21S,
                                Opcodes.CONST_16, 0, null, 0, 0, literal, vA);

                    }
                    break;

            }

            if (result != instruction)
                result.setAddress(instruction.getAddress());

            return result;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_11X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getA()));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_10T() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = byte0(opcodeUnit);
            int target = (byte) byte1(opcodeUnit); // sign-extend
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null,
                    baseAddress + target, target, 0L);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTargetByte(out.cursor());
            out.write(codeUnit(insn.getOpcode(), relativeTarget));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            return 0;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);

        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;

        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_20T() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = byte0(opcodeUnit);
            int literal = byte1(opcodeUnit); // should be zero
            int target = (short) in.read(); // sign-extend
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null,
                    baseAddress + target, target, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            short relativeTarget = insn.getTargetUnit(out.cursor());
            out.write(insn.getOpcodeUnit(), relativeTarget);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);

        }

        @Override
        public boolean hasTarget() {

            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_20BC() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            // Note: We use the literal field to hold the decoded AA value.
            int opcode = byte0(opcodeUnit);
            int literal = byte1(opcodeUnit);
            int index = in.read();
            return new ZeroRegisterDecodedInstruction(this, opcode, index,
                    IndexType.VARIES, 0, 0, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getLiteralByte()),
                    insn.getIndexUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_22X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int b = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getA()), insn.getBUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {

            return 0;

        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            DecodedInstruction result = instruction;
            int vA = instruction.getA();
            int vB = instruction.getB();
            long literal = instruction.getLiteral();

            switch (instruction.getOpcode()) {
                case Opcodes.MOVE_FROM16:
                    if (vA > 0xff)
                        result = new TwoRegisterDecodedInstruction(FORMAT_32X,
                                Opcodes.MOVE_16, 0, null, 0, 0, literal, vA, vB);

                    break;

                case Opcodes.MOVE_WIDE_FROM16:
                    if (vA > 0xff || vB > 0xff)
                        result = new TwoRegisterDecodedInstruction(FORMAT_32X,
                                Opcodes.MOVE_WIDE_16, 0, null, 0, 0, literal, vA,
                                vB);
                    break;

                case Opcodes.MOVE_OBJECT_FROM16:
                    if (vA > 0xff)
                        result = new TwoRegisterDecodedInstruction(FORMAT_32X,
                                Opcodes.MOVE_OBJECT_16, 0, null, 0, 0, literal, vA,
                                vB);
                    break;
            }

            if (result != instruction)
                result.setAddress(instruction.getAddress());

            return result;

        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_21T() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int target = (short) in.read(); // sign-extend
            return new OneRegisterDecodedInstruction(this, opcode, 0, null,
                    baseAddress + target, target, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            short relativeTarget = insn.getTargetUnit(out.cursor());
            out.write(codeUnit(insn.getOpcode(), insn.getA()), relativeTarget);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                return 1;
            }

            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeInteger);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);

        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_21S() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int literal = (short) in.read(); // sign-extend
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getA()),
                    insn.getLiteralUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            // same as format 21H
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeConstLo);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_21H() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            long literal = (short) in.read(); // sign-extend

            /*
             * Format 21h decodes differently depending on the opcode, because
             * the "signed hat" might represent either a 32- or 64- bit value.
             */
            literal <<= (opcode == Opcodes.CONST_HIGH16) ? 16 : 48;

            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            // See above.
            int opcode = insn.getOpcode();
            int shift = (opcode == Opcodes.CONST_HIGH16) ? 16 : 48;
            short literal = (short) (insn.getLiteral() >> shift);

            out.write(codeUnit(opcode, insn.getA()), literal);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            // same as format 21S
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeConstLo);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;

        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_21C() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int index = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new OneRegisterDecodedInstruction(this, opcode, index,
                    indexType, 0, 0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getA()),
                    insn.getIndexUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeRef); // DecodedInstruction
                                                                  // may further
                                                                  // specify
                    // a ref type.
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_23X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int bc = in.read();
            int b = byte0(bc);
            int c = byte1(bc);
            return new ThreeRegisterDecodedInstruction(this, opcode, 0, null,
                    0, 0, 0L, a, b, c);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getA()),
                    codeUnit(insn.getB(), insn.getC()));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            cur = instruction.getC();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setC(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                case C:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_22B() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int bc = in.read();
            int b = byte0(bc);
            int literal = (byte) byte1(bc); // sign-extend
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(codeUnit(insn.getOpcode(), insn.getA()),
                    codeUnit(insn.getB(), insn.getLiteralByte()));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeInteger); // I think
                                                                      // it's
                                                                      // always
                                                                      // integer,
                                                                      // but a
                    // check may modify it?
                    // TODO make sure destination and source register are always
                    // the
                    // same.
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_22T() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = byte0(opcodeUnit);
            int a = nibble2(opcodeUnit);
            int b = nibble3(opcodeUnit);
            int target = (short) in.read(); // sign-extend
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null,
                    baseAddress + target, target, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            short relativeTarget = insn.getTargetUnit(out.cursor());
            out.write(
                    codeUnit(insn.getOpcode(),
                            makeByte(insn.getA(), insn.getB())), relativeTarget);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {

            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown); // the
                                                                      // comparison
                                                                      // could
                                                                      // be
                                                                      // between
                                                                      // any
                    // kinds of numbers.
            }

            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);

        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_22S() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = nibble2(opcodeUnit);
            int b = nibble3(opcodeUnit);
            int literal = (short) in.read(); // sign-extend
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(
                    codeUnit(insn.getOpcode(),
                            makeByte(insn.getA(), insn.getB())),
                    insn.getLiteralUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeInteger);
            }

            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_22C() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = nibble2(opcodeUnit);
            int b = nibble3(opcodeUnit);
            int index = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new TwoRegisterDecodedInstruction(this, opcode, index,
                    indexType, 0, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(
                    codeUnit(insn.getOpcode(),
                            makeByte(insn.getA(), insn.getB())),
                    insn.getIndexUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown); // src can
                                                                      // be
                                                                      // an obj
                                                                      // ref for
                                                                      // some
                    // instructions and a number for others.
            }

            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            DecodedInstruction result = instruction;
            if (instruction.getOpcode() == Opcodes.NEW_ARRAY) {
                int vA = instruction.getA();
                int vB = instruction.getB();
                if (vA > 0xf || vB > 0xf) {
                    result = new TwoRegisterDecodedInstruction(FORMAT_52C,
                            Opcodes.NEW_ARRAY_JUMBO, instruction.getIndex(),
                            instruction.getIndexType(), 0, 0, 0L, vA, vB);
                    result.setAddress(instruction.getAddress());
                }
            }
            return result;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    /**
     * This one does not appear to be used anywhere.
     */
    FORMAT_22CS() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = nibble2(opcodeUnit);
            int b = nibble3(opcodeUnit);
            int index = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, index,
                    IndexType.FIELD_OFFSET, 0, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(
                    codeUnit(insn.getOpcode(),
                            makeByte(insn.getA(), insn.getB())),
                    insn.getIndexUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown); // src can
                                                                      // be
                                                                      // an obj
                                                                      // ref for
                                                                      // some
                    // instructions and a number for others.
            }

            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_30T() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = byte0(opcodeUnit);
            int literal = byte1(opcodeUnit); // should be zero
            int target = in.readInt();
            return new ZeroRegisterDecodedInstruction(this, opcode, 0, null,
                    baseAddress + target, target, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTarget(out.cursor());
            out.write(insn.getOpcodeUnit(), unit0(relativeTarget),
                    unit1(relativeTarget));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);

        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_32X() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int literal = byte1(opcodeUnit); // should be zero
            int a = in.read();
            int b = in.read();
            return new TwoRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit(), insn.getAUnit(), insn.getBUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            cur = instruction.getB();
            mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setB(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown); // src can
                                                                      // be
                                                                      // an obj
                                                                      // ref for
                                                                      // some
                    // instructions and a number for others.
            }

            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_31I() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int literal = in.readInt();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int literal = insn.getLiteralInt();
            out.write(codeUnit(insn.getOpcode(), insn.getA()), unit0(literal),
                    unit1(literal));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeConstLo);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    /**
     * See also FORMAT_PACKED_SWITCH_PAYLOAD and FORMAT_SPARSE_SWITCH_PAYLOAD
     */
    FORMAT_31T() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.cursor() - 1;
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int relativeTarget = in.readInt();
            int target = baseAddress + relativeTarget;

            /*
             * Switch instructions need to "forward" their addresses to their
             * payload target instructions.
             */
            switch (opcode) {
                case Opcodes.PACKED_SWITCH:
                case Opcodes.SPARSE_SWITCH: {
                    in.setBaseAddress(target, baseAddress);
                    break;
                }
            }

            return new OneRegisterDecodedInstruction(this, opcode, 0, null,
                    target, relativeTarget, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int relativeTarget = insn.getTarget(out.cursor());
            out.write(codeUnit(insn.getOpcode(), insn.getA()),
                    unit0(relativeTarget), unit1(relativeTarget));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeUnknown); // the
                                                                      // docs
                                                                      // aren't
                                                                      // clear.
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);
        }

        @Override
        public boolean hasTarget() {
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_31C() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            int index = in.readInt();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new OneRegisterDecodedInstruction(this, opcode, index,
                    indexType, 0, 0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(codeUnit(insn.getOpcode(), insn.getA()), unit0(index),
                    unit1(index));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;

        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeRef);

            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_35C() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return decodeRegisterList(this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            encodeRegisterList(insn, out);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;
            int cur;
            int mapped;

            // the docs indicate that all values in the instructions for this
            // format are register references
            if (instruction instanceof OneRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }
            } else if (instruction instanceof TwoRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }
            } else if (instruction instanceof ThreeRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }
            } else if (instruction instanceof FourRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }
            } else if (instruction instanceof FiveRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }

                cur = instruction.getE();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setE(mapped);
                    result++;
                }
            }

            return result;
        }

        /**
         * this format can generate an instruction that can use any of the five
         * registers. DecodeInstruction can make the distinction.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {

            // instruction can have 0-5 registers. See format
            // InstructionCodec.FORMAT_3RC

            DecodedInstruction result = instruction;

            int registers[] = new int[5];
            int registerCount = instruction.getRegisterCount();

            boolean convert = false;

            for (int i = 0; i < registerCount; i++) {
                registers[i] = instruction.getRegisterContentsByIndex(i);
                if (registers[i] > 0xf)
                    convert = true;
            }

            int newOpcode = Integer.MIN_VALUE;

            if (convert) {
                switch (instruction.getOpcode()) {
                    case Opcodes.INVOKE_DIRECT:
                        newOpcode = Opcodes.INVOKE_DIRECT_RANGE;
                        break;
                    case Opcodes.INVOKE_INTERFACE:
                        newOpcode = Opcodes.INVOKE_INTERFACE_RANGE;
                        break;
                    case Opcodes.INVOKE_STATIC:
                        newOpcode = Opcodes.INVOKE_STATIC_RANGE;
                        break;
                    case Opcodes.INVOKE_SUPER:
                        newOpcode = Opcodes.INVOKE_SUPER_RANGE;
                        break;
                    case Opcodes.INVOKE_VIRTUAL:
                        newOpcode = Opcodes.INVOKE_VIRTUAL_RANGE;
                        break;
                    default:
                        throw new RuntimeException(
                                "conversion is necessary but no opcode was found.");

                }

                // all range equivalents are
                // RegiaterRangeDecodedInstruction/FORMAT_3RC

                result = new RegisterRangeDecodedInstruction(FORMAT_3RC,
                        newOpcode, instruction.getIndex(),
                        instruction.getIndexType(), 0, 0, 0L,
                        registers[0] /* vA */, registerCount, true);

                result.setAddress(instruction.getAddress());
            }

            return result;

        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_35MS() {

        // it looks like this foramt is not used.

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return decodeRegisterList(this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            encodeRegisterList(insn, out);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;
            int cur;
            int mapped;

            // the docs indicate that all values in the instructions for this
            // format are register references
            if (instruction instanceof OneRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }
            } else if (instruction instanceof TwoRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }
            } else if (instruction instanceof ThreeRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }
            } else if (instruction instanceof FourRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }
            } else if (instruction instanceof FiveRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }

                cur = instruction.getE();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setE(mapped);
                    result++;
                }
            }

            return result;

        }

        /**
         * this format can generate an instruction that can use any of the five
         * registers. DecodeInstruction can make the distinction.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_35MI() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return decodeRegisterList(this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            encodeRegisterList(insn, out);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;
            int cur;
            int mapped;

            // the docs indicate that all values in the instructions for this
            // format are register references
            if (instruction instanceof OneRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }
            } else if (instruction instanceof TwoRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }
            } else if (instruction instanceof ThreeRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }
            } else if (instruction instanceof FourRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }
            } else if (instruction instanceof FiveRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }

                cur = instruction.getE();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setE(mapped);
                    result++;
                }
            }

            return result;

        }

        /**
         * this format can generate an instruction that can use any of the five
         * registers. DecodeInstruction can make the distinction.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_3RC() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return decodeRegisterRange(this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            encodeRegisterRange(insn, out);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            if (!(instruction instanceof RegisterRangeDecodedInstruction))
                throw new IllegalArgumentException(
                        "format FORMAT_3RC can only update registers for RegisterRangeDecodedInstruction types.");

            int result = 0;
            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);

            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;

        }

        /**
         * This format decodes a register range. Only register A is guaranteed
         * to be set. Usage of the other registers must be determined by
         * DecodedInstruction. But, all registers may be used.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeByte); // argument
                                                                   // word
                                                                   // count
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }

    },

    FORMAT_3RMS() { // not used.

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return decodeRegisterRange(this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            encodeRegisterRange(insn, out);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;
            int cur;
            int mapped;

            // the docs indicate that all values in the instructions for this
            // format are register references
            if (instruction instanceof OneRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }
            } else if (instruction instanceof TwoRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }
            } else if (instruction instanceof ThreeRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }
            } else if (instruction instanceof FourRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }
            } else if (instruction instanceof FiveRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }

                cur = instruction.getE();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setE(mapped);
                    result++;
                }
            }

            return result;

        }

        /**
         * This format decodes a register range. Only register A is guaranteed
         * to be set. Usage of the other registers must be determined by
         * DecodedInstruction. But, all registers may be used.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeByte); // argument
                                                                   // word
                                                                   // count
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_3RMI() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            return decodeRegisterRange(this, opcodeUnit, in);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            encodeRegisterRange(insn, out);
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            int result = 0;
            int cur;
            int mapped;

            // the docs indicate that all values in the instructions for this
            // format are register references
            if (instruction instanceof OneRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }
            } else if (instruction instanceof TwoRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }
            } else if (instruction instanceof ThreeRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }
            } else if (instruction instanceof FourRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }
            } else if (instruction instanceof FiveRegisterDecodedInstruction) {
                cur = instruction.getA();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setA(mapped);
                    result++;
                }

                cur = instruction.getB();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setB(mapped);
                    result++;
                }

                cur = instruction.getC();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setC(mapped);
                    result++;
                }

                cur = instruction.getD();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setD(mapped);
                    result++;
                }

                cur = instruction.getE();
                mapped = targetRegisters.get(cur);
                if (cur != mapped) {
                    instruction.setE(mapped);
                    result++;
                }
            }

            return result;

        }

        /**
         * This format decodes a register range. Only register A is guaranteed
         * to be set. Usage of the other registers must be determined by
         * DecodedInstruction. But, all registers may be used.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeByte); // argument
                                                                   // word
                                                                   // count
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }

    },

    FORMAT_51L() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int opcode = byte0(opcodeUnit);
            int a = byte1(opcodeUnit);
            long literal = in.readLong();
            return new OneRegisterDecodedInstruction(this, opcode, 0, null, 0,
                    0, literal, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            long literal = insn.getLiteral();
            out.write(codeUnit(insn.getOpcode(), insn.getA()), unit0(literal),
                    unit1(literal), unit2(literal), unit3(literal));
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            int result = 0;

            int cur = instruction.getA();
            int mapped = targetRegisters.get(cur);
            if (cur != mapped) {
                instruction.setA(mapped);
                result++;
            }

            return result;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeAnyCategory2);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_33X() { // not used

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int ab = in.read();
            int a = byte0(ab);
            int b = byte1(ab);
            int c = in.read();
            return new ThreeRegisterDecodedInstruction(this, opcodeUnit, 0,
                    null, 0, 0, 0L, a, b, c);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit(), codeUnit(insn.getA(), insn.getB()),
                    insn.getCUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // FIXME there is no documentation on this format.
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                case C:
                    return RegisterType.newInstance(kRegTypeUnknown);

            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_32S() { // not used

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int ab = in.read();
            int a = byte0(ab);
            int b = byte1(ab);
            int literal = (short) in.read(); // sign-extend
            return new TwoRegisterDecodedInstruction(this, opcodeUnit, 0, null,
                    0, 0, literal, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            out.write(insn.getOpcodeUnit(), codeUnit(insn.getA(), insn.getB()),
                    insn.getLiteralUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // FIXME there is no documentation on this format.
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown);

            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_40SC() { // not used

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            // Note: We use the literal field to hold the decoded AA value.
            int index = in.readInt();
            int literal = in.read();
            return new ZeroRegisterDecodedInstruction(this, opcodeUnit, index,
                    IndexType.VARIES, 0, 0, literal);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(insn.getOpcodeUnit(), unit0(index), unit1(index),
                    insn.getLiteralUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // FIXME there is no documentation on this format.
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_41C() { // used for instructions that have format 21c (sget/sput
                   // operations).

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int index = in.readInt();
            int a = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcodeUnit);
            return new OneRegisterDecodedInstruction(this, opcodeUnit, index,
                    indexType, 0, 0, 0L, a);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(insn.getOpcodeUnit(), unit0(index), unit1(index),
                    insn.getAUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // FIXME there is no documentation on this format.
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeUnknown); // DecodedInstruction
                                                                      // may
                                                                      // further
                    // specify a ref type.
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_52C() { // used for instructions that have format 21c (iget/iput
                   // operations).

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int index = in.readInt();
            int a = in.read();
            int b = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcodeUnit);
            return new TwoRegisterDecodedInstruction(this, opcodeUnit, index,
                    indexType, 0, 0, 0L, a, b);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(insn.getOpcodeUnit(), unit0(index), unit1(index),
                    insn.getAUnit(), insn.getBUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // FIXME there is no documentation on this format.
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                case B:
                    return RegisterType.newInstance(kRegTypeUnknown); // DecodedInstruction
                                                                      // may
                                                                      // further
                    // specify a ref type.
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_5RC() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int index = in.readInt();
            int registerCount = in.read();
            int a = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcodeUnit);
            return new RegisterRangeDecodedInstruction(this, opcodeUnit, index,
                    indexType, 0, 0, 0L, a, registerCount, true);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            int index = insn.getIndex();
            out.write(insn.getOpcodeUnit(), unit0(index), unit1(index),
                    insn.getRegisterCountUnit(), insn.getAUnit());
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // FIXME there is no documentation on this format.
            return 0;
        }

        /**
         * This format decodes a register range. Only register A is guaranteed
         * to be set. Usage of the other registers must be determined by
         * DecodedInstruction. But, all registers may be used.
         */
        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            switch (ref) {
                case A:
                    return RegisterType.newInstance(kRegTypeByte); // argument
                                                                   // word
                                                                   // count
                case B:
                case C:
                case D:
                case E:
                    return RegisterType.newInstance(kRegTypeUnknown);
            }
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_PACKED_SWITCH_PAYLOAD() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.baseAddressForCursor() - 1; // already read
                                                             // opcode
            int size = in.read();
            int firstKey = in.readInt();
            int[] targets = new int[size];

            for (int i = 0; i < size; i++) {
                targets[i] = baseAddress + in.readInt();
            }

            return new PackedSwitchPayloadDecodedInstruction(this, opcodeUnit,
                    firstKey, targets);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            PackedSwitchPayloadDecodedInstruction payload = (PackedSwitchPayloadDecodedInstruction) insn;
            int[] targets = payload.getTargets();
            int baseAddress = out.baseAddressForCursor();

            out.write(payload.getOpcodeUnit());
            out.write(asUnsignedUnit(targets.length));
            out.writeInt(payload.getFirstKey());

            for (int target : targets) {
                out.writeInt(target - baseAddress);
            }
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {

            PackedSwitchPayloadDecodedInstruction ins = (PackedSwitchPayloadDecodedInstruction) instruction;

            int[] targets = ins.getTargets();

            if (targets == null)
                return 0;

            int modified = 0;
            for (int i = 0; i < targets.length; i++) {
                if (targets[i] == oldAddress) {
                    targets[i] = newAddress;
                    modified++;
                }
            }

            return modified;

        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_SPARSE_SWITCH_PAYLOAD() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int baseAddress = in.baseAddressForCursor() - 1; // already read
                                                             // opcode
            int size = in.read();
            int[] keys = new int[size];
            int[] targets = new int[size];

            for (int i = 0; i < size; i++) {
                keys[i] = in.readInt();
            }

            for (int i = 0; i < size; i++) {
                targets[i] = baseAddress + in.readInt();
            }

            return new SparseSwitchPayloadDecodedInstruction(this, opcodeUnit,
                    keys, targets);
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            SparseSwitchPayloadDecodedInstruction payload = (SparseSwitchPayloadDecodedInstruction) insn;
            int[] keys = payload.getKeys();
            int[] targets = payload.getTargets();
            int baseAddress = out.baseAddressForCursor();

            out.write(payload.getOpcodeUnit());
            out.write(asUnsignedUnit(targets.length));

            for (int key : keys) {
                out.writeInt(key);
            }

            for (int target : targets) {
                out.writeInt(target - baseAddress);
            }
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {

            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            return updateInstructionTarget(instructions, instruction,
                    oldAddress, newAddress);
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    },

    FORMAT_FILL_ARRAY_DATA_PAYLOAD() {

        @Override
        public DecodedInstruction decode(int opcodeUnit, CodeInput in)
                throws EOFException {
            int elementWidth = in.read();
            int size = in.readInt();

            switch (elementWidth) {
                case 1: {
                    byte[] array = new byte[size];
                    boolean even = true;
                    for (int i = 0, value = 0; i < size; i++, even = !even) {
                        if (even) {
                            value = in.read();
                        }
                        array[i] = (byte) (value & 0xff);
                        value >>= 8;
                    }
                    return new FillArrayDataPayloadDecodedInstruction(this,
                            opcodeUnit, array);
                }
                case 2: {
                    short[] array = new short[size];
                    for (int i = 0; i < size; i++) {
                        array[i] = (short) in.read();
                    }
                    return new FillArrayDataPayloadDecodedInstruction(this,
                            opcodeUnit, array);
                }
                case 4: {
                    int[] array = new int[size];
                    for (int i = 0; i < size; i++) {
                        array[i] = in.readInt();
                    }
                    return new FillArrayDataPayloadDecodedInstruction(this,
                            opcodeUnit, array);
                }
                case 8: {
                    long[] array = new long[size];
                    for (int i = 0; i < size; i++) {
                        array[i] = in.readLong();
                    }
                    return new FillArrayDataPayloadDecodedInstruction(this,
                            opcodeUnit, array);
                }
            }

            throw new DexException("bogus element_width: "
                    + Hex.u2(elementWidth));
        }

        @Override
        public void encode(DecodedInstruction insn, CodeOutput out) {
            FillArrayDataPayloadDecodedInstruction payload = (FillArrayDataPayloadDecodedInstruction) insn;
            short elementWidth = payload.getElementWidthUnit();
            Object data = payload.getData();

            out.write(payload.getOpcodeUnit());
            out.write(elementWidth);
            out.writeInt(payload.getSize());

            switch (elementWidth) {
                case 1:
                    out.write((byte[]) data);
                    break;
                case 2:
                    out.write((short[]) data);
                    break;
                case 4:
                    out.write((int[]) data);
                    break;
                case 8:
                    out.write((long[]) data);
                    break;
                default: {
                    throw new DexException("bogus element_width: "
                            + Hex.u2(elementWidth));
                }
            }
        }

        @Override
        public int updateRegisters(DecodedInstruction instruction,
                SparseIntArray targetRegisters) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public RegisterType getRegisterType(RegisterReference ref) {
            return null;
        }

        @Override
        public int updateTarget(InstructionList instructions,
                DecodedInstruction instruction, int oldAddress, int newAddress) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasTarget() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public DecodedInstruction convert(DecodedInstruction instruction) {
            return instruction;
        }

        @Override
        public DalvInsn getDalvInsn(DecodedInstruction instruction) {
            // TODO Auto-generated method stub
            return null;
        }
    };

    public static final String LOG_TAG = "InstructionCodec";

    /**
     * Decodes an instruction specified by the given opcode unit, reading any
     * required additional code units from the given input source.
     */
    public abstract DecodedInstruction decode(int opcodeUnit, CodeInput in)
            throws EOFException;

    /**
     * Encodes the given instruction.
     */
    public abstract void encode(DecodedInstruction insn, CodeOutput out);

    /**
     * Updates registers and destinations (e.g. jump destinations). This is used
     * to alter instruction contents on registers that are injected into a
     * function. Instructions that are part of the target method do not need to
     * be touched.
     */
    public abstract int updateRegisters(DecodedInstruction instruction,
            SparseIntArray registerMap);

    /**
     * Updates instructions that have targets that need to be adjusted
     * 
     * @param instruction the instruction being updated
     * @param address the address where expansion occurs.
     * @param size the number of bytes to adjust the target by
     * @param instructions the list of instructions that instruction belongs to.
     * @return the number of instructions affected (1 or 0)
     */
    public abstract int updateTarget(InstructionList instructions,
            DecodedInstruction instruction, int oldAddress, int newAddress);

    public abstract boolean hasTarget();

    /**
     * Returns all register references and types used by this format. Only
     * register references used by this format are included in the hashtable.
     * The register type is set to kRegTypeUnknown if the type can only be
     * determined by the DecodedInstruction.
     * 
     * @return register types used by this format.
     */
    public Hashtable<RegisterReference, RegisterType> getRegisterTypes() {
        Hashtable<RegisterReference, RegisterType> result = new Hashtable<RegisterReference, RegisterType>();

        EnumSet<RegisterReference> eSet = EnumSet
                .allOf(RegisterReference.class);
        RegisterType rType = null;
        for (RegisterReference ref : eSet) {
            rType = getRegisterType(RegisterReference.A);
            if (rType != null)
                result.put(ref, rType);
        }

        return result;

    }

    /**
     * Returns the register type for a given register reference or null if that
     * reference is not used by the format. A return type of kRegTypeUnknown
     * means that the register type can only be determined by OpcodeInfo
     * objects. OpcodeInfo object will indicate whether or not the format must
     * be derived from a DecodedInstruction.
     * 
     * @param ref the reference to check
     * @return the RegisterType of the reference
     */

    public abstract RegisterType getRegisterType(RegisterReference ref);

    /**
     * Converts an instruction to an appropriate variant depending upon the
     * values of the registers. For example, if move-wide contains register
     * references that are larger than 0xf, then the result returned will be
     * either move-wide/from16 or move-wide/16.
     * <p>
     * If no conversion is necessary than the instruction passed to the function
     * is returned.
     * 
     * @param instruction the instruction to convert.
     * @return the variant of instruction that can handle the register
     *         references contained within the instruction.
     */
    public abstract DecodedInstruction convert(DecodedInstruction instruction);

    /**
     * Helper method that decodes any of the register-list formats. When
     * decodeRegisterList is used, it means that all the register values are
     * going to be from 0-15 (4 bits each).
     */
    private static DecodedInstruction decodeRegisterList(
            InstructionCodec format, int opcodeUnit, CodeInput in)
            throws EOFException {
        int opcode = byte0(opcodeUnit);
        int e = nibble2(opcodeUnit);
        int registerCount = nibble3(opcodeUnit);
        int index = in.read();
        int abcd = in.read();
        int a = nibble0(abcd);
        int b = nibble1(abcd);
        int c = nibble2(abcd);
        int d = nibble3(abcd);
        IndexType indexType = OpcodeInfo.getIndexType(opcode);

        // TODO: Having to switch like this is less than ideal.
        switch (registerCount) {
            case 0:
                return new ZeroRegisterDecodedInstruction(format, opcode, index,
                        indexType, 0, 0, 0L, true);
            case 1:
                return new OneRegisterDecodedInstruction(format, opcode, index,
                        indexType, 0, 0, 0L, a, true);
            case 2:
                return new TwoRegisterDecodedInstruction(format, opcode, index,
                        indexType, 0, 0, 0L, a, b, true);
            case 3:
                return new ThreeRegisterDecodedInstruction(format, opcode, index,
                        indexType, 0, 0, 0L, a, b, c, true);
            case 4:
                return new FourRegisterDecodedInstruction(format, opcode, index,
                        indexType, 0, 0, 0L, a, b, c, d, true);
            case 5:
                return new FiveRegisterDecodedInstruction(format, opcode, index,
                        indexType, 0, 0, 0L, a, b, c, d, e, true);
        }

        throw new DexException("bogus registerCount: "
                + Hex.uNibble(registerCount));
    }

    /**
     * Helper method that encodes any of the register-list formats.
     */
    private static void encodeRegisterList(DecodedInstruction insn,
            CodeOutput out) {
        out.write(
                codeUnit(insn.getOpcode(),
                        makeByte(insn.getE(), insn.getRegisterCount())),
                insn.getIndexUnit(),
                codeUnit(insn.getA(), insn.getB(), insn.getC(), insn.getD()));
    }

    /**
     * Helper method that decodes any of the three-unit register-range formats.
     */
    private static DecodedInstruction decodeRegisterRange(
            InstructionCodec format, int opcodeUnit, CodeInput in)
            throws EOFException {
        int opcode = byte0(opcodeUnit);
        int registerCount = byte1(opcodeUnit);
        int index = in.read();
        int a = in.read();
        IndexType indexType = OpcodeInfo.getIndexType(opcode);
        return new RegisterRangeDecodedInstruction(format, opcode, index,
                indexType, 0, 0, 0L, a, registerCount, true);
    }

    /**
     * Helper method that encodes any of the three-unit register-range formats.
     */
    private static void encodeRegisterRange(DecodedInstruction insn,
            CodeOutput out) {
        out.write(codeUnit(insn.getOpcode(), insn.getRegisterCount()),
                insn.getIndexUnit(), insn.getAUnit());
    }

    private static short codeUnit(int lowByte, int highByte) {
        if ((lowByte & ~0xff) != 0) {
            throw new IllegalArgumentException("bogus lowByte");
        }

        if ((highByte & ~0xff) != 0) {
            throw new IllegalArgumentException("bogus highByte");
        }

        return (short) (lowByte | (highByte << 8));
    }

    private static short codeUnit(int nibble0, int nibble1, int nibble2,
            int nibble3) {
        if ((nibble0 & ~0xf) != 0) {
            throw new IllegalArgumentException("bogus nibble0");
        }

        if ((nibble1 & ~0xf) != 0) {
            throw new IllegalArgumentException("bogus nibble1");
        }

        if ((nibble2 & ~0xf) != 0) {
            throw new IllegalArgumentException("bogus nibble2");
        }

        if ((nibble3 & ~0xf) != 0) {
            throw new IllegalArgumentException("bogus nibble3");
        }

        return (short) (nibble0 | (nibble1 << 4) | (nibble2 << 8) | (nibble3 << 12));
    }

    /**
     * Creates a byte using a low nibble value and a high nibble value. The
     * result returned is the lower four bits of the highNibble appended to the
     * lower four bits of the lowNibble. Or, highNibblelowNibble. If any bits of
     * each argument other than the lower four bits are set then an exception is
     * thrown.
     * 
     * @param lowNibble the value to use for the lower nibble.
     * @param highNibble the value to use for the higher nibble.
     * @return the value of lowNibble in bits 0-3 and highNibble in bits 4-7.
     * @throws IllegalArgumentException if any bits of lowNibble or highNibble
     *             other that the lower four bits are set.
     */
    private static int makeByte(int lowNibble, int highNibble) {
        if ((lowNibble & ~0xf) != 0) {
            throw new IllegalArgumentException("bogus lowNibble");
        }

        if ((highNibble & ~0xf) != 0) {
            throw new IllegalArgumentException("bogus highNibble");
        }

        return lowNibble | (highNibble << 4);
    }

    private static short asUnsignedUnit(int value) {
        if ((value & ~0xffff) != 0) {
            throw new IllegalArgumentException("bogus unsigned code unit");
        }

        return (short) value;
    }

    private static short unit0(int value) {
        return (short) value;
    }

    private static short unit1(int value) {
        return (short) (value >> 16);
    }

    private static short unit0(long value) {
        return (short) value;
    }

    private static short unit1(long value) {
        return (short) (value >> 16);
    }

    private static short unit2(long value) {
        return (short) (value >> 32);
    }

    private static short unit3(long value) {
        return (short) (value >> 48);
    }

    private static int byte0(int value) {
        return value & 0xff;
    }

    private static int byte1(int value) {
        return (value >> 8) & 0xff;
    }

    private static int byte2(int value) {
        return (value >> 16) & 0xff;
    }

    private static int byte3(int value) {
        return value >>> 24;
    }

    private static int nibble0(int value) {
        return value & 0xf;
    }

    private static int nibble1(int value) {
        return (value >> 4) & 0xf;
    }

    private static int nibble2(int value) {
        return (value >> 8) & 0xf;
    }

    private static int nibble3(int value) {
        return (value >> 12) & 0xf;
    }

    /**
     * Updates the target of the specified instruction with a new address if the
     * current address matches oldAddress
     * 
     * @param instructions the instruction list that instruction belongs to.
     * @param instruction the instruction that is being modified.
     * @param oldAddress the old target value to look for
     * @param size the target value to update to if the old one matches.
     * @return the number of instructions updated.
     */
    protected int updateInstructionTarget(InstructionList instructions,
            DecodedInstruction instruction, int oldAddress, int newAddress) {

        if (instructions.indexOfKey(newAddress) < 0) {
            if (DO_LOG)
                Log.d(LOG_TAG, String.format(
                        "address %d does not exist in the instruction set",
                        newAddress));
        }

        if (!hasTarget())
            return 0;

        int dest = instruction.getTarget();

        if (dest == oldAddress)
            instruction.setTarget(newAddress);

        return 1;

    }

    /**
     * Converts an instruction to a DalvInsn object.
     * 
     * @param decodedInstruction the instruction to convert
     * @return the converted instruction
     */
    public abstract DalvInsn getDalvInsn(DecodedInstruction instruction);

}

/**
 * TODO: implement this idea. implement a getRegisterTypes for each format. Will
 * return register type information just based on the format. getRegisterType
 * from an instruction will get register type information from the instruction
 * usage without. Another function will iterate through an instruction list and
 * get register type based on the actual usage of the instruction within the
 * function.
 */

