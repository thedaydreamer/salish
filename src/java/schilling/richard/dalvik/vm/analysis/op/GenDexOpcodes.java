package schilling.richard.dalvik.vm.analysis.op;

import schilling.richard.dexlib.Code.Opcode;

/*
 * Copyright (C) 2008 The Android Open Source Project
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

// TODO: consolidate this with the Opcode class and Opcodes class. 
/**
 * Dalvik opcode information.
 * 
 * IMPORTANT NOTE: The contents of this file are mostly generated automatically
 * by the opcode-gen tool. Any edits to the generated sections will get wiped
 * out the next time the tool is run.
 * 
 * See the file opcode-gen/README.txt for information about updating opcodes and
 * instruction formats.
 */
public final class GenDexOpcodes {

	/*
	 * kMaxOpcodeValue: the highest possible raw (unpacked) opcode value
	 * 
	 * kNumPackedOpcodes: the highest possible packed opcode value of a valid
	 * Dalvik opcode, plus one
	 * 
	 * TODO: Change this once the rest of the code is prepared to deal with
	 * extended opcodes.
	 */
	// BEGIN(libdex-maximum-values); GENERATED AUTOMATICALLY BY opcode-gen
	public final static int kMaxOpcodeValue = 0xffff;
	public final static int kNumPackedOpcodes = 0x200;

	// END(libdex-maximum-values); GENERATED AUTOMATICALLY BY opcode-gen

	/*
	 * Switch table and array data signatures are a code unit consisting of
	 * "NOP" (0x00) in the low-order byte and a non-zero identifying code in the
	 * high-order byte. (A true NOP is 0x0000.)
	 */
	public final static int kPackedSwitchSignature = 0x0100;
	public final static int kSparseSwitchSignature = 0x0200;
	public final static int kArrayDataSignature = 0x0300;

	/*
	 * Enumeration of all Dalvik opcodes, where the enumeration value associated
	 * with each is the corresponding packed opcode number. This is different
	 * than the opcode value from the Dalvik bytecode spec for opcode values >=
	 * 0xff; see dexOpcodeFromCodeUnit() below.
	 * 
	 * A note about the "breakpoint" opcode. This instruction is special, in
	 * that it should never be seen by anything but the debug interpreter.
	 * During debugging it takes the place of an arbitrary opcode, which means
	 * operations like "tell me the opcode width so I can find the next
	 * instruction" aren't possible. (This is correctable, but probably not
	 * useful.)
	 */
	// BEGIN(libdex-opcode-enum); GENERATED AUTOMATICALLY BY opcode-gen
	public final static int OP_NOP = 0x00;
	public final static int OP_MOVE = 0x01;
	public final static int OP_MOVE_FROM16 = 0x02;
	public final static int OP_MOVE_16 = 0x03;
	public final static int OP_MOVE_WIDE = 0x04;
	public final static int OP_MOVE_WIDE_FROM16 = 0x05;
	public final static int OP_MOVE_WIDE_16 = 0x06;
	public final static int OP_MOVE_OBJECT = 0x07;
	public final static int OP_MOVE_OBJECT_FROM16 = 0x08;
	public final static int OP_MOVE_OBJECT_16 = 0x09;
	public final static int OP_MOVE_RESULT = 0x0a;
	public final static int OP_MOVE_RESULT_WIDE = 0x0b;
	public final static int OP_MOVE_RESULT_OBJECT = 0x0c;
	public final static int OP_MOVE_EXCEPTION = 0x0d;
	public final static int OP_RETURN_VOID = 0x0e;
	public final static int OP_RETURN = 0x0f;
	public final static int OP_RETURN_WIDE = 0x10;
	public final static int OP_RETURN_OBJECT = 0x11;
	public final static int OP_CONST_4 = 0x12;
	public final static int OP_CONST_16 = 0x13;
	public final static int OP_CONST = 0x14;
	public final static int OP_CONST_HIGH16 = 0x15;
	public final static int OP_CONST_WIDE_16 = 0x16;
	public final static int OP_CONST_WIDE_32 = 0x17;
	public final static int OP_CONST_WIDE = 0x18;
	public final static int OP_CONST_WIDE_HIGH16 = 0x19;
	public final static int OP_CONST_STRING = 0x1a;
	public final static int OP_CONST_STRING_JUMBO = 0x1b;
	public final static int OP_CONST_CLASS = 0x1c;
	public final static int OP_MONITOR_ENTER = 0x1d;
	public final static int OP_MONITOR_EXIT = 0x1e;
	public final static int OP_CHECK_CAST = 0x1f;
	public final static int OP_INSTANCE_OF = 0x20;
	public final static int OP_ARRAY_LENGTH = 0x21;
	public final static int OP_NEW_INSTANCE = 0x22;
	public final static int OP_NEW_ARRAY = 0x23;
	public final static int OP_FILLED_NEW_ARRAY = 0x24;
	public final static int OP_FILLED_NEW_ARRAY_RANGE = 0x25;
	public final static int OP_FILL_ARRAY_DATA = 0x26;
	public final static int OP_THROW = 0x27;
	public final static int OP_GOTO = 0x28;
	public final static int OP_GOTO_16 = 0x29;
	public final static int OP_GOTO_32 = 0x2a;
	public final static int OP_PACKED_SWITCH = 0x2b;
	public final static int OP_SPARSE_SWITCH = 0x2c;
	public final static int OP_CMPL_FLOAT = 0x2d;
	public final static int OP_CMPG_FLOAT = 0x2e;
	public final static int OP_CMPL_DOUBLE = 0x2f;
	public final static int OP_CMPG_DOUBLE = 0x30;
	public final static int OP_CMP_LONG = 0x31;
	public final static int OP_IF_EQ = 0x32;
	public final static int OP_IF_NE = 0x33;
	public final static int OP_IF_LT = 0x34;
	public final static int OP_IF_GE = 0x35;
	public final static int OP_IF_GT = 0x36;
	public final static int OP_IF_LE = 0x37;
	public final static int OP_IF_EQZ = 0x38;
	public final static int OP_IF_NEZ = 0x39;
	public final static int OP_IF_LTZ = 0x3a;
	public final static int OP_IF_GEZ = 0x3b;
	public final static int OP_IF_GTZ = 0x3c;
	public final static int OP_IF_LEZ = 0x3d;
	public final static int OP_UNUSED_3E = 0x3e;
	public final static int OP_UNUSED_3F = 0x3f;
	public final static int OP_UNUSED_40 = 0x40;
	public final static int OP_UNUSED_41 = 0x41;
	public final static int OP_UNUSED_42 = 0x42;
	public final static int OP_UNUSED_43 = 0x43;
	public final static int OP_AGET = 0x44;
	public final static int OP_AGET_WIDE = 0x45;
	public final static int OP_AGET_OBJECT = 0x46;
	public final static int OP_AGET_BOOLEAN = 0x47;
	public final static int OP_AGET_BYTE = 0x48;
	public final static int OP_AGET_CHAR = 0x49;
	public final static int OP_AGET_SHORT = 0x4a;
	public final static int OP_APUT = 0x4b;
	public final static int OP_APUT_WIDE = 0x4c;
	public final static int OP_APUT_OBJECT = 0x4d;
	public final static int OP_APUT_BOOLEAN = 0x4e;
	public final static int OP_APUT_BYTE = 0x4f;
	public final static int OP_APUT_CHAR = 0x50;
	public final static int OP_APUT_SHORT = 0x51;
	public final static int OP_IGET = 0x52;
	public final static int OP_IGET_WIDE = 0x53;
	public final static int OP_IGET_OBJECT = 0x54;
	public final static int OP_IGET_BOOLEAN = 0x55;
	public final static int OP_IGET_BYTE = 0x56;
	public final static int OP_IGET_CHAR = 0x57;
	public final static int OP_IGET_SHORT = 0x58;
	public final static int OP_IPUT = 0x59;
	public final static int OP_IPUT_WIDE = 0x5a;
	public final static int OP_IPUT_OBJECT = 0x5b;
	public final static int OP_IPUT_BOOLEAN = 0x5c;
	public final static int OP_IPUT_BYTE = 0x5d;
	public final static int OP_IPUT_CHAR = 0x5e;
	public final static int OP_IPUT_SHORT = 0x5f;
	public final static int OP_SGET = 0x60;
	public final static int OP_SGET_WIDE = 0x61;
	public final static int OP_SGET_OBJECT = 0x62;
	public final static int OP_SGET_BOOLEAN = 0x63;
	public final static int OP_SGET_BYTE = 0x64;
	public final static int OP_SGET_CHAR = 0x65;
	public final static int OP_SGET_SHORT = 0x66;
	public final static int OP_SPUT = 0x67;
	public final static int OP_SPUT_WIDE = 0x68;
	public final static int OP_SPUT_OBJECT = 0x69;
	public final static int OP_SPUT_BOOLEAN = 0x6a;
	public final static int OP_SPUT_BYTE = 0x6b;
	public final static int OP_SPUT_CHAR = 0x6c;
	public final static int OP_SPUT_SHORT = 0x6d;
	public final static int OP_INVOKE_VIRTUAL = 0x6e;
	public final static int OP_INVOKE_SUPER = 0x6f;
	public final static int OP_INVOKE_DIRECT = 0x70;
	public final static int OP_INVOKE_STATIC = 0x71;
	public final static int OP_INVOKE_INTERFACE = 0x72;
	public final static int OP_UNUSED_73 = 0x73;
	public final static int OP_INVOKE_VIRTUAL_RANGE = 0x74;
	public final static int OP_INVOKE_SUPER_RANGE = 0x75;
	public final static int OP_INVOKE_DIRECT_RANGE = 0x76;
	public final static int OP_INVOKE_STATIC_RANGE = 0x77;
	public final static int OP_INVOKE_INTERFACE_RANGE = 0x78;
	public final static int OP_UNUSED_79 = 0x79;
	public final static int OP_UNUSED_7A = 0x7a;
	public final static int OP_NEG_INT = 0x7b;
	public final static int OP_NOT_INT = 0x7c;
	public final static int OP_NEG_LONG = 0x7d;
	public final static int OP_NOT_LONG = 0x7e;
	public final static int OP_NEG_FLOAT = 0x7f;
	public final static int OP_NEG_DOUBLE = 0x80;
	public final static int OP_INT_TO_LONG = 0x81;
	public final static int OP_INT_TO_FLOAT = 0x82;
	public final static int OP_INT_TO_DOUBLE = 0x83;
	public final static int OP_LONG_TO_INT = 0x84;
	public final static int OP_LONG_TO_FLOAT = 0x85;
	public final static int OP_LONG_TO_DOUBLE = 0x86;
	public final static int OP_FLOAT_TO_INT = 0x87;
	public final static int OP_FLOAT_TO_LONG = 0x88;
	public final static int OP_FLOAT_TO_DOUBLE = 0x89;
	public final static int OP_DOUBLE_TO_INT = 0x8a;
	public final static int OP_DOUBLE_TO_LONG = 0x8b;
	public final static int OP_DOUBLE_TO_FLOAT = 0x8c;
	public final static int OP_INT_TO_BYTE = 0x8d;
	public final static int OP_INT_TO_CHAR = 0x8e;
	public final static int OP_INT_TO_SHORT = 0x8f;
	public final static int OP_ADD_INT = 0x90;
	public final static int OP_SUB_INT = 0x91;
	public final static int OP_MUL_INT = 0x92;
	public final static int OP_DIV_INT = 0x93;
	public final static int OP_REM_INT = 0x94;
	public final static int OP_AND_INT = 0x95;
	public final static int OP_OR_INT = 0x96;
	public final static int OP_XOR_INT = 0x97;
	public final static int OP_SHL_INT = 0x98;
	public final static int OP_SHR_INT = 0x99;
	public final static int OP_USHR_INT = 0x9a;
	public final static int OP_ADD_LONG = 0x9b;
	public final static int OP_SUB_LONG = 0x9c;
	public final static int OP_MUL_LONG = 0x9d;
	public final static int OP_DIV_LONG = 0x9e;
	public final static int OP_REM_LONG = 0x9f;
	public final static int OP_AND_LONG = 0xa0;
	public final static int OP_OR_LONG = 0xa1;
	public final static int OP_XOR_LONG = 0xa2;
	public final static int OP_SHL_LONG = 0xa3;
	public final static int OP_SHR_LONG = 0xa4;
	public final static int OP_USHR_LONG = 0xa5;
	public final static int OP_ADD_FLOAT = 0xa6;
	public final static int OP_SUB_FLOAT = 0xa7;
	public final static int OP_MUL_FLOAT = 0xa8;
	public final static int OP_DIV_FLOAT = 0xa9;
	public final static int OP_REM_FLOAT = 0xaa;
	public final static int OP_ADD_DOUBLE = 0xab;
	public final static int OP_SUB_DOUBLE = 0xac;
	public final static int OP_MUL_DOUBLE = 0xad;
	public final static int OP_DIV_DOUBLE = 0xae;
	public final static int OP_REM_DOUBLE = 0xaf;
	public final static int OP_ADD_INT_2ADDR = 0xb0;
	public final static int OP_SUB_INT_2ADDR = 0xb1;
	public final static int OP_MUL_INT_2ADDR = 0xb2;
	public final static int OP_DIV_INT_2ADDR = 0xb3;
	public final static int OP_REM_INT_2ADDR = 0xb4;
	public final static int OP_AND_INT_2ADDR = 0xb5;
	public final static int OP_OR_INT_2ADDR = 0xb6;
	public final static int OP_XOR_INT_2ADDR = 0xb7;
	public final static int OP_SHL_INT_2ADDR = 0xb8;
	public final static int OP_SHR_INT_2ADDR = 0xb9;
	public final static int OP_USHR_INT_2ADDR = 0xba;
	public final static int OP_ADD_LONG_2ADDR = 0xbb;
	public final static int OP_SUB_LONG_2ADDR = 0xbc;
	public final static int OP_MUL_LONG_2ADDR = 0xbd;
	public final static int OP_DIV_LONG_2ADDR = 0xbe;
	public final static int OP_REM_LONG_2ADDR = 0xbf;
	public final static int OP_AND_LONG_2ADDR = 0xc0;
	public final static int OP_OR_LONG_2ADDR = 0xc1;
	public final static int OP_XOR_LONG_2ADDR = 0xc2;
	public final static int OP_SHL_LONG_2ADDR = 0xc3;
	public final static int OP_SHR_LONG_2ADDR = 0xc4;
	public final static int OP_USHR_LONG_2ADDR = 0xc5;
	public final static int OP_ADD_FLOAT_2ADDR = 0xc6;
	public final static int OP_SUB_FLOAT_2ADDR = 0xc7;
	public final static int OP_MUL_FLOAT_2ADDR = 0xc8;
	public final static int OP_DIV_FLOAT_2ADDR = 0xc9;
	public final static int OP_REM_FLOAT_2ADDR = 0xca;
	public final static int OP_ADD_DOUBLE_2ADDR = 0xcb;
	public final static int OP_SUB_DOUBLE_2ADDR = 0xcc;
	public final static int OP_MUL_DOUBLE_2ADDR = 0xcd;
	public final static int OP_DIV_DOUBLE_2ADDR = 0xce;
	public final static int OP_REM_DOUBLE_2ADDR = 0xcf;
	public final static int OP_ADD_INT_LIT16 = 0xd0;
	public final static int OP_RSUB_INT = 0xd1;
	public final static int OP_MUL_INT_LIT16 = 0xd2;
	public final static int OP_DIV_INT_LIT16 = 0xd3;
	public final static int OP_REM_INT_LIT16 = 0xd4;
	public final static int OP_AND_INT_LIT16 = 0xd5;
	public final static int OP_OR_INT_LIT16 = 0xd6;
	public final static int OP_XOR_INT_LIT16 = 0xd7;
	public final static int OP_ADD_INT_LIT8 = 0xd8;
	public final static int OP_RSUB_INT_LIT8 = 0xd9;
	public final static int OP_MUL_INT_LIT8 = 0xda;
	public final static int OP_DIV_INT_LIT8 = 0xdb;
	public final static int OP_REM_INT_LIT8 = 0xdc;
	public final static int OP_AND_INT_LIT8 = 0xdd;
	public final static int OP_OR_INT_LIT8 = 0xde;
	public final static int OP_XOR_INT_LIT8 = 0xdf;
	public final static int OP_SHL_INT_LIT8 = 0xe0;
	public final static int OP_SHR_INT_LIT8 = 0xe1;
	public final static int OP_USHR_INT_LIT8 = 0xe2;
	public final static int OP_IGET_VOLATILE = 0xe3;
	public final static int OP_IPUT_VOLATILE = 0xe4;
	public final static int OP_SGET_VOLATILE = 0xe5;
	public final static int OP_SPUT_VOLATILE = 0xe6;
	public final static int OP_IGET_OBJECT_VOLATILE = 0xe7;
	public final static int OP_IGET_WIDE_VOLATILE = 0xe8;
	public final static int OP_IPUT_WIDE_VOLATILE = 0xe9;
	public final static int OP_SGET_WIDE_VOLATILE = 0xea;
	public final static int OP_SPUT_WIDE_VOLATILE = 0xeb;
	public final static int OP_BREAKPOINT = 0xec;
	public final static int OP_THROW_VERIFICATION_ERROR = 0xed;
	public final static int OP_EXECUTE_INLINE = 0xee;
	public final static int OP_EXECUTE_INLINE_RANGE = 0xef;
	public final static int OP_INVOKE_OBJECT_INIT_RANGE = 0xf0;
	public final static int OP_RETURN_VOID_BARRIER = 0xf1;
	public final static int OP_IGET_QUICK = 0xf2;
	public final static int OP_IGET_WIDE_QUICK = 0xf3;
	public final static int OP_IGET_OBJECT_QUICK = 0xf4;
	public final static int OP_IPUT_QUICK = 0xf5;
	public final static int OP_IPUT_WIDE_QUICK = 0xf6;
	public final static int OP_IPUT_OBJECT_QUICK = 0xf7;
	public final static int OP_INVOKE_VIRTUAL_QUICK = 0xf8;
	public final static int OP_INVOKE_VIRTUAL_QUICK_RANGE = 0xf9;
	public final static int OP_INVOKE_SUPER_QUICK = 0xfa;
	public final static int OP_INVOKE_SUPER_QUICK_RANGE = 0xfb;
	public final static int OP_IPUT_OBJECT_VOLATILE = 0xfc;
	public final static int OP_SGET_OBJECT_VOLATILE = 0xfd;
	public final static int OP_SPUT_OBJECT_VOLATILE = 0xfe;
	public final static int OP_DISPATCH_FF = 0xff;
	public final static int OP_CONST_CLASS_JUMBO = 0x100;
	public final static int OP_CHECK_CAST_JUMBO = 0x101;
	public final static int OP_INSTANCE_OF_JUMBO = 0x102;
	public final static int OP_NEW_INSTANCE_JUMBO = 0x103;
	public final static int OP_NEW_ARRAY_JUMBO = 0x104;
	public final static int OP_FILLED_NEW_ARRAY_JUMBO = 0x105;
	public final static int OP_IGET_JUMBO = 0x106;
	public final static int OP_IGET_WIDE_JUMBO = 0x107;
	public final static int OP_IGET_OBJECT_JUMBO = 0x108;
	public final static int OP_IGET_BOOLEAN_JUMBO = 0x109;
	public final static int OP_IGET_BYTE_JUMBO = 0x10a;
	public final static int OP_IGET_CHAR_JUMBO = 0x10b;
	public final static int OP_IGET_SHORT_JUMBO = 0x10c;
	public final static int OP_IPUT_JUMBO = 0x10d;
	public final static int OP_IPUT_WIDE_JUMBO = 0x10e;
	public final static int OP_IPUT_OBJECT_JUMBO = 0x10f;
	public final static int OP_IPUT_BOOLEAN_JUMBO = 0x110;
	public final static int OP_IPUT_BYTE_JUMBO = 0x111;
	public final static int OP_IPUT_CHAR_JUMBO = 0x112;
	public final static int OP_IPUT_SHORT_JUMBO = 0x113;
	public final static int OP_SGET_JUMBO = 0x114;
	public final static int OP_SGET_WIDE_JUMBO = 0x115;
	public final static int OP_SGET_OBJECT_JUMBO = 0x116;
	public final static int OP_SGET_BOOLEAN_JUMBO = 0x117;
	public final static int OP_SGET_BYTE_JUMBO = 0x118;
	public final static int OP_SGET_CHAR_JUMBO = 0x119;
	public final static int OP_SGET_SHORT_JUMBO = 0x11a;
	public final static int OP_SPUT_JUMBO = 0x11b;
	public final static int OP_SPUT_WIDE_JUMBO = 0x11c;
	public final static int OP_SPUT_OBJECT_JUMBO = 0x11d;
	public final static int OP_SPUT_BOOLEAN_JUMBO = 0x11e;
	public final static int OP_SPUT_BYTE_JUMBO = 0x11f;
	public final static int OP_SPUT_CHAR_JUMBO = 0x120;
	public final static int OP_SPUT_SHORT_JUMBO = 0x121;
	public final static int OP_INVOKE_VIRTUAL_JUMBO = 0x122;
	public final static int OP_INVOKE_SUPER_JUMBO = 0x123;
	public final static int OP_INVOKE_DIRECT_JUMBO = 0x124;
	public final static int OP_INVOKE_STATIC_JUMBO = 0x125;
	public final static int OP_INVOKE_INTERFACE_JUMBO = 0x126;
	public final static int OP_UNUSED_27FF = 0x127;
	public final static int OP_UNUSED_28FF = 0x128;
	public final static int OP_UNUSED_29FF = 0x129;
	public final static int OP_UNUSED_2AFF = 0x12a;
	public final static int OP_UNUSED_2BFF = 0x12b;
	public final static int OP_UNUSED_2CFF = 0x12c;
	public final static int OP_UNUSED_2DFF = 0x12d;
	public final static int OP_UNUSED_2EFF = 0x12e;
	public final static int OP_UNUSED_2FFF = 0x12f;
	public final static int OP_UNUSED_30FF = 0x130;
	public final static int OP_UNUSED_31FF = 0x131;
	public final static int OP_UNUSED_32FF = 0x132;
	public final static int OP_UNUSED_33FF = 0x133;
	public final static int OP_UNUSED_34FF = 0x134;
	public final static int OP_UNUSED_35FF = 0x135;
	public final static int OP_UNUSED_36FF = 0x136;
	public final static int OP_UNUSED_37FF = 0x137;
	public final static int OP_UNUSED_38FF = 0x138;
	public final static int OP_UNUSED_39FF = 0x139;
	public final static int OP_UNUSED_3AFF = 0x13a;
	public final static int OP_UNUSED_3BFF = 0x13b;
	public final static int OP_UNUSED_3CFF = 0x13c;
	public final static int OP_UNUSED_3DFF = 0x13d;
	public final static int OP_UNUSED_3EFF = 0x13e;
	public final static int OP_UNUSED_3FFF = 0x13f;
	public final static int OP_UNUSED_40FF = 0x140;
	public final static int OP_UNUSED_41FF = 0x141;
	public final static int OP_UNUSED_42FF = 0x142;
	public final static int OP_UNUSED_43FF = 0x143;
	public final static int OP_UNUSED_44FF = 0x144;
	public final static int OP_UNUSED_45FF = 0x145;
	public final static int OP_UNUSED_46FF = 0x146;
	public final static int OP_UNUSED_47FF = 0x147;
	public final static int OP_UNUSED_48FF = 0x148;
	public final static int OP_UNUSED_49FF = 0x149;
	public final static int OP_UNUSED_4AFF = 0x14a;
	public final static int OP_UNUSED_4BFF = 0x14b;
	public final static int OP_UNUSED_4CFF = 0x14c;
	public final static int OP_UNUSED_4DFF = 0x14d;
	public final static int OP_UNUSED_4EFF = 0x14e;
	public final static int OP_UNUSED_4FFF = 0x14f;
	public final static int OP_UNUSED_50FF = 0x150;
	public final static int OP_UNUSED_51FF = 0x151;
	public final static int OP_UNUSED_52FF = 0x152;
	public final static int OP_UNUSED_53FF = 0x153;
	public final static int OP_UNUSED_54FF = 0x154;
	public final static int OP_UNUSED_55FF = 0x155;
	public final static int OP_UNUSED_56FF = 0x156;
	public final static int OP_UNUSED_57FF = 0x157;
	public final static int OP_UNUSED_58FF = 0x158;
	public final static int OP_UNUSED_59FF = 0x159;
	public final static int OP_UNUSED_5AFF = 0x15a;
	public final static int OP_UNUSED_5BFF = 0x15b;
	public final static int OP_UNUSED_5CFF = 0x15c;
	public final static int OP_UNUSED_5DFF = 0x15d;
	public final static int OP_UNUSED_5EFF = 0x15e;
	public final static int OP_UNUSED_5FFF = 0x15f;
	public final static int OP_UNUSED_60FF = 0x160;
	public final static int OP_UNUSED_61FF = 0x161;
	public final static int OP_UNUSED_62FF = 0x162;
	public final static int OP_UNUSED_63FF = 0x163;
	public final static int OP_UNUSED_64FF = 0x164;
	public final static int OP_UNUSED_65FF = 0x165;
	public final static int OP_UNUSED_66FF = 0x166;
	public final static int OP_UNUSED_67FF = 0x167;
	public final static int OP_UNUSED_68FF = 0x168;
	public final static int OP_UNUSED_69FF = 0x169;
	public final static int OP_UNUSED_6AFF = 0x16a;
	public final static int OP_UNUSED_6BFF = 0x16b;
	public final static int OP_UNUSED_6CFF = 0x16c;
	public final static int OP_UNUSED_6DFF = 0x16d;
	public final static int OP_UNUSED_6EFF = 0x16e;
	public final static int OP_UNUSED_6FFF = 0x16f;
	public final static int OP_UNUSED_70FF = 0x170;
	public final static int OP_UNUSED_71FF = 0x171;
	public final static int OP_UNUSED_72FF = 0x172;
	public final static int OP_UNUSED_73FF = 0x173;
	public final static int OP_UNUSED_74FF = 0x174;
	public final static int OP_UNUSED_75FF = 0x175;
	public final static int OP_UNUSED_76FF = 0x176;
	public final static int OP_UNUSED_77FF = 0x177;
	public final static int OP_UNUSED_78FF = 0x178;
	public final static int OP_UNUSED_79FF = 0x179;
	public final static int OP_UNUSED_7AFF = 0x17a;
	public final static int OP_UNUSED_7BFF = 0x17b;
	public final static int OP_UNUSED_7CFF = 0x17c;
	public final static int OP_UNUSED_7DFF = 0x17d;
	public final static int OP_UNUSED_7EFF = 0x17e;
	public final static int OP_UNUSED_7FFF = 0x17f;
	public final static int OP_UNUSED_80FF = 0x180;
	public final static int OP_UNUSED_81FF = 0x181;
	public final static int OP_UNUSED_82FF = 0x182;
	public final static int OP_UNUSED_83FF = 0x183;
	public final static int OP_UNUSED_84FF = 0x184;
	public final static int OP_UNUSED_85FF = 0x185;
	public final static int OP_UNUSED_86FF = 0x186;
	public final static int OP_UNUSED_87FF = 0x187;
	public final static int OP_UNUSED_88FF = 0x188;
	public final static int OP_UNUSED_89FF = 0x189;
	public final static int OP_UNUSED_8AFF = 0x18a;
	public final static int OP_UNUSED_8BFF = 0x18b;
	public final static int OP_UNUSED_8CFF = 0x18c;
	public final static int OP_UNUSED_8DFF = 0x18d;
	public final static int OP_UNUSED_8EFF = 0x18e;
	public final static int OP_UNUSED_8FFF = 0x18f;
	public final static int OP_UNUSED_90FF = 0x190;
	public final static int OP_UNUSED_91FF = 0x191;
	public final static int OP_UNUSED_92FF = 0x192;
	public final static int OP_UNUSED_93FF = 0x193;
	public final static int OP_UNUSED_94FF = 0x194;
	public final static int OP_UNUSED_95FF = 0x195;
	public final static int OP_UNUSED_96FF = 0x196;
	public final static int OP_UNUSED_97FF = 0x197;
	public final static int OP_UNUSED_98FF = 0x198;
	public final static int OP_UNUSED_99FF = 0x199;
	public final static int OP_UNUSED_9AFF = 0x19a;
	public final static int OP_UNUSED_9BFF = 0x19b;
	public final static int OP_UNUSED_9CFF = 0x19c;
	public final static int OP_UNUSED_9DFF = 0x19d;
	public final static int OP_UNUSED_9EFF = 0x19e;
	public final static int OP_UNUSED_9FFF = 0x19f;
	public final static int OP_UNUSED_A0FF = 0x1a0;
	public final static int OP_UNUSED_A1FF = 0x1a1;
	public final static int OP_UNUSED_A2FF = 0x1a2;
	public final static int OP_UNUSED_A3FF = 0x1a3;
	public final static int OP_UNUSED_A4FF = 0x1a4;
	public final static int OP_UNUSED_A5FF = 0x1a5;
	public final static int OP_UNUSED_A6FF = 0x1a6;
	public final static int OP_UNUSED_A7FF = 0x1a7;
	public final static int OP_UNUSED_A8FF = 0x1a8;
	public final static int OP_UNUSED_A9FF = 0x1a9;
	public final static int OP_UNUSED_AAFF = 0x1aa;
	public final static int OP_UNUSED_ABFF = 0x1ab;
	public final static int OP_UNUSED_ACFF = 0x1ac;
	public final static int OP_UNUSED_ADFF = 0x1ad;
	public final static int OP_UNUSED_AEFF = 0x1ae;
	public final static int OP_UNUSED_AFFF = 0x1af;
	public final static int OP_UNUSED_B0FF = 0x1b0;
	public final static int OP_UNUSED_B1FF = 0x1b1;
	public final static int OP_UNUSED_B2FF = 0x1b2;
	public final static int OP_UNUSED_B3FF = 0x1b3;
	public final static int OP_UNUSED_B4FF = 0x1b4;
	public final static int OP_UNUSED_B5FF = 0x1b5;
	public final static int OP_UNUSED_B6FF = 0x1b6;
	public final static int OP_UNUSED_B7FF = 0x1b7;
	public final static int OP_UNUSED_B8FF = 0x1b8;
	public final static int OP_UNUSED_B9FF = 0x1b9;
	public final static int OP_UNUSED_BAFF = 0x1ba;
	public final static int OP_UNUSED_BBFF = 0x1bb;
	public final static int OP_UNUSED_BCFF = 0x1bc;
	public final static int OP_UNUSED_BDFF = 0x1bd;
	public final static int OP_UNUSED_BEFF = 0x1be;
	public final static int OP_UNUSED_BFFF = 0x1bf;
	public final static int OP_UNUSED_C0FF = 0x1c0;
	public final static int OP_UNUSED_C1FF = 0x1c1;
	public final static int OP_UNUSED_C2FF = 0x1c2;
	public final static int OP_UNUSED_C3FF = 0x1c3;
	public final static int OP_UNUSED_C4FF = 0x1c4;
	public final static int OP_UNUSED_C5FF = 0x1c5;
	public final static int OP_UNUSED_C6FF = 0x1c6;
	public final static int OP_UNUSED_C7FF = 0x1c7;
	public final static int OP_UNUSED_C8FF = 0x1c8;
	public final static int OP_UNUSED_C9FF = 0x1c9;
	public final static int OP_UNUSED_CAFF = 0x1ca;
	public final static int OP_UNUSED_CBFF = 0x1cb;
	public final static int OP_UNUSED_CCFF = 0x1cc;
	public final static int OP_UNUSED_CDFF = 0x1cd;
	public final static int OP_UNUSED_CEFF = 0x1ce;
	public final static int OP_UNUSED_CFFF = 0x1cf;
	public final static int OP_UNUSED_D0FF = 0x1d0;
	public final static int OP_UNUSED_D1FF = 0x1d1;
	public final static int OP_UNUSED_D2FF = 0x1d2;
	public final static int OP_UNUSED_D3FF = 0x1d3;
	public final static int OP_UNUSED_D4FF = 0x1d4;
	public final static int OP_UNUSED_D5FF = 0x1d5;
	public final static int OP_UNUSED_D6FF = 0x1d6;
	public final static int OP_UNUSED_D7FF = 0x1d7;
	public final static int OP_UNUSED_D8FF = 0x1d8;
	public final static int OP_UNUSED_D9FF = 0x1d9;
	public final static int OP_UNUSED_DAFF = 0x1da;
	public final static int OP_UNUSED_DBFF = 0x1db;
	public final static int OP_UNUSED_DCFF = 0x1dc;
	public final static int OP_UNUSED_DDFF = 0x1dd;
	public final static int OP_UNUSED_DEFF = 0x1de;
	public final static int OP_UNUSED_DFFF = 0x1df;
	public final static int OP_UNUSED_E0FF = 0x1e0;
	public final static int OP_UNUSED_E1FF = 0x1e1;
	public final static int OP_UNUSED_E2FF = 0x1e2;
	public final static int OP_UNUSED_E3FF = 0x1e3;
	public final static int OP_UNUSED_E4FF = 0x1e4;
	public final static int OP_UNUSED_E5FF = 0x1e5;
	public final static int OP_UNUSED_E6FF = 0x1e6;
	public final static int OP_UNUSED_E7FF = 0x1e7;
	public final static int OP_UNUSED_E8FF = 0x1e8;
	public final static int OP_UNUSED_E9FF = 0x1e9;
	public final static int OP_UNUSED_EAFF = 0x1ea;
	public final static int OP_UNUSED_EBFF = 0x1eb;
	public final static int OP_UNUSED_ECFF = 0x1ec;
	public final static int OP_UNUSED_EDFF = 0x1ed;
	public final static int OP_UNUSED_EEFF = 0x1ee;
	public final static int OP_UNUSED_EFFF = 0x1ef;
	public final static int OP_UNUSED_F0FF = 0x1f0;
	public final static int OP_UNUSED_F1FF = 0x1f1;
	public final static int OP_INVOKE_OBJECT_INIT_JUMBO = 0x1f2;
	public final static int OP_IGET_VOLATILE_JUMBO = 0x1f3;
	public final static int OP_IGET_WIDE_VOLATILE_JUMBO = 0x1f4;
	public final static int OP_IGET_OBJECT_VOLATILE_JUMBO = 0x1f5;
	public final static int OP_IPUT_VOLATILE_JUMBO = 0x1f6;
	public final static int OP_IPUT_WIDE_VOLATILE_JUMBO = 0x1f7;
	public final static int OP_IPUT_OBJECT_VOLATILE_JUMBO = 0x1f8;
	public final static int OP_SGET_VOLATILE_JUMBO = 0x1f9;
	public final static int OP_SGET_WIDE_VOLATILE_JUMBO = 0x1fa;
	public final static int OP_SGET_OBJECT_VOLATILE_JUMBO = 0x1fb;
	public final static int OP_SPUT_VOLATILE_JUMBO = 0x1fc;
	public final static int OP_SPUT_WIDE_VOLATILE_JUMBO = 0x1fd;
	public final static int OP_SPUT_OBJECT_VOLATILE_JUMBO = 0x1fe;
	public final static int OP_THROW_VERIFICATION_ERROR_JUMBO = 0x1ff;

	// END(libdex-opcode-enum)

	/*
	 * Return the Opcode for a given raw opcode code unit (which may include
	 * data payload). The packed index is a zero-based index which can be used
	 * to point into various opcode-related tables. The Dalvik opcode space is
	 * inherently sparse, in that the opcode unit is 16 bits wide, but for most
	 * opcodes, eight of those bits are for data.
	 */
	public static int dexOpcodeFromCodeUnit(short codeUnit) {
		/*
		 * This will want to become table-driven should the opcode layout get
		 * more complicated.
		 * 
		 * Note: This has to match the corresponding code in opcode-gen, so that
		 * data tables get generated in a consistent way.
		 */
		int lowByte = codeUnit & 0xff;
		if (lowByte != 0xff) {
			return lowByte;
		} else {
			return ((codeUnit >> 8) | 0x100);
		}
	}

	/*
	 * Return the name of an opcode.
	 */
	public static String dexGetOpcodeName(short op) {
		return Opcode.getOpcodeByValue(op).name;
	}

}
