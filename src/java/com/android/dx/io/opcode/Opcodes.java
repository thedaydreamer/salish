/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.dx.io.opcode;

/**
 * All the Dalvik opcode value constants. See the related spec document for the
 * meaning and instruction format of each opcode.
 */
public final class Opcodes {

	/**
	 * pseudo-opcode used for nonstandard format payload "instructions". TODO:
	 * Retire this concept, and start treating the payload instructions more
	 * like the rest.
	 */
	public static final int SPECIAL_FORMAT = -1;

	/**
	 * pseudo-opcode used to indicate there is no next opcode; used in opcode
	 * chaining lists
	 */
	public static final int NO_NEXT = -1;

	/** minimum valid opcode value */
	public static final int MIN_VALUE = -1;

	/** maximum valid opcode value */
	public static final int MAX_VALUE = 0xffff;

	// BEGIN(opcodes); GENERATED AUTOMATICALLY BY opcode-gen
	public static final int NOP = 0x00;
	public static final int MOVE = 0x01;
	public static final int MOVE_FROM16 = 0x02;
	public static final int MOVE_16 = 0x03;
	public static final int MOVE_WIDE = 0x04;
	public static final int MOVE_WIDE_FROM16 = 0x05;
	public static final int MOVE_WIDE_16 = 0x06;
	public static final int MOVE_OBJECT = 0x07;
	public static final int MOVE_OBJECT_FROM16 = 0x08;
	public static final int MOVE_OBJECT_16 = 0x09;
	public static final int MOVE_RESULT = 0x0a;
	public static final int MOVE_RESULT_WIDE = 0x0b;
	public static final int MOVE_RESULT_OBJECT = 0x0c;
	public static final int MOVE_EXCEPTION = 0x0d;
	public static final int RETURN_VOID = 0x0e;
	public static final int RETURN = 0x0f;
	public static final int RETURN_WIDE = 0x10;
	public static final int RETURN_OBJECT = 0x11;
	public static final int CONST_4 = 0x12;
	public static final int CONST_16 = 0x13;
	public static final int CONST = 0x14;
	public static final int CONST_HIGH16 = 0x15;
	public static final int CONST_WIDE_16 = 0x16;
	public static final int CONST_WIDE_32 = 0x17;
	public static final int CONST_WIDE = 0x18;
	public static final int CONST_WIDE_HIGH16 = 0x19;
	public static final int CONST_STRING = 0x1a;
	public static final int CONST_STRING_JUMBO = 0x1b;
	public static final int CONST_CLASS = 0x1c;
	public static final int MONITOR_ENTER = 0x1d;
	public static final int MONITOR_EXIT = 0x1e;
	public static final int CHECK_CAST = 0x1f;
	public static final int INSTANCE_OF = 0x20;
	public static final int ARRAY_LENGTH = 0x21;
	public static final int NEW_INSTANCE = 0x22;
	public static final int NEW_ARRAY = 0x23;
	public static final int FILLED_NEW_ARRAY = 0x24;
	public static final int FILLED_NEW_ARRAY_RANGE = 0x25;
	public static final int FILL_ARRAY_DATA = 0x26;
	public static final int THROW = 0x27;
	public static final int GOTO = 0x28;
	public static final int GOTO_16 = 0x29;
	public static final int GOTO_32 = 0x2a;
	public static final int PACKED_SWITCH = 0x2b;
	public static final int SPARSE_SWITCH = 0x2c;
	public static final int CMPL_FLOAT = 0x2d;
	public static final int CMPG_FLOAT = 0x2e;
	public static final int CMPL_DOUBLE = 0x2f;
	public static final int CMPG_DOUBLE = 0x30;
	public static final int CMP_LONG = 0x31;
	public static final int IF_EQ = 0x32;
	public static final int IF_NE = 0x33;
	public static final int IF_LT = 0x34;
	public static final int IF_GE = 0x35;
	public static final int IF_GT = 0x36;
	public static final int IF_LE = 0x37;
	public static final int IF_EQZ = 0x38;
	public static final int IF_NEZ = 0x39;
	public static final int IF_LTZ = 0x3a;
	public static final int IF_GEZ = 0x3b;
	public static final int IF_GTZ = 0x3c;
	public static final int IF_LEZ = 0x3d;
	public static final int AGET = 0x44;
	public static final int AGET_WIDE = 0x45;
	public static final int AGET_OBJECT = 0x46;
	public static final int AGET_BOOLEAN = 0x47;
	public static final int AGET_BYTE = 0x48;
	public static final int AGET_CHAR = 0x49;
	public static final int AGET_SHORT = 0x4a;
	public static final int APUT = 0x4b;
	public static final int APUT_WIDE = 0x4c;
	public static final int APUT_OBJECT = 0x4d;
	public static final int APUT_BOOLEAN = 0x4e;
	public static final int APUT_BYTE = 0x4f;
	public static final int APUT_CHAR = 0x50;
	public static final int APUT_SHORT = 0x51;
	public static final int IGET = 0x52;
	public static final int IGET_WIDE = 0x53;
	public static final int IGET_OBJECT = 0x54;
	public static final int IGET_BOOLEAN = 0x55;
	public static final int IGET_BYTE = 0x56;
	public static final int IGET_CHAR = 0x57;
	public static final int IGET_SHORT = 0x58;
	public static final int IPUT = 0x59;
	public static final int IPUT_WIDE = 0x5a;
	public static final int IPUT_OBJECT = 0x5b;
	public static final int IPUT_BOOLEAN = 0x5c;
	public static final int IPUT_BYTE = 0x5d;
	public static final int IPUT_CHAR = 0x5e;
	public static final int IPUT_SHORT = 0x5f;
	public static final int SGET = 0x60;
	public static final int SGET_WIDE = 0x61;
	public static final int SGET_OBJECT = 0x62;
	public static final int SGET_BOOLEAN = 0x63;
	public static final int SGET_BYTE = 0x64;
	public static final int SGET_CHAR = 0x65;
	public static final int SGET_SHORT = 0x66;
	public static final int SPUT = 0x67;
	public static final int SPUT_WIDE = 0x68;
	public static final int SPUT_OBJECT = 0x69;
	public static final int SPUT_BOOLEAN = 0x6a;
	public static final int SPUT_BYTE = 0x6b;
	public static final int SPUT_CHAR = 0x6c;
	public static final int SPUT_SHORT = 0x6d;
	public static final int INVOKE_VIRTUAL = 0x6e;
	public static final int INVOKE_SUPER = 0x6f;
	public static final int INVOKE_DIRECT = 0x70;
	public static final int INVOKE_STATIC = 0x71;
	public static final int INVOKE_INTERFACE = 0x72;
	public static final int INVOKE_VIRTUAL_RANGE = 0x74;
	public static final int INVOKE_SUPER_RANGE = 0x75;
	public static final int INVOKE_DIRECT_RANGE = 0x76;
	public static final int INVOKE_STATIC_RANGE = 0x77;
	public static final int INVOKE_INTERFACE_RANGE = 0x78;
	public static final int NEG_INT = 0x7b;
	public static final int NOT_INT = 0x7c;
	public static final int NEG_LONG = 0x7d;
	public static final int NOT_LONG = 0x7e;
	public static final int NEG_FLOAT = 0x7f;
	public static final int NEG_DOUBLE = 0x80;
	public static final int INT_TO_LONG = 0x81;
	public static final int INT_TO_FLOAT = 0x82;
	public static final int INT_TO_DOUBLE = 0x83;
	public static final int LONG_TO_INT = 0x84;
	public static final int LONG_TO_FLOAT = 0x85;
	public static final int LONG_TO_DOUBLE = 0x86;
	public static final int FLOAT_TO_INT = 0x87;
	public static final int FLOAT_TO_LONG = 0x88;
	public static final int FLOAT_TO_DOUBLE = 0x89;
	public static final int DOUBLE_TO_INT = 0x8a;
	public static final int DOUBLE_TO_LONG = 0x8b;
	public static final int DOUBLE_TO_FLOAT = 0x8c;
	public static final int INT_TO_BYTE = 0x8d;
	public static final int INT_TO_CHAR = 0x8e;
	public static final int INT_TO_SHORT = 0x8f;
	public static final int ADD_INT = 0x90;
	public static final int SUB_INT = 0x91;
	public static final int MUL_INT = 0x92;
	public static final int DIV_INT = 0x93;
	public static final int REM_INT = 0x94;
	public static final int AND_INT = 0x95;
	public static final int OR_INT = 0x96;
	public static final int XOR_INT = 0x97;
	public static final int SHL_INT = 0x98;
	public static final int SHR_INT = 0x99;
	public static final int USHR_INT = 0x9a;
	public static final int ADD_LONG = 0x9b;
	public static final int SUB_LONG = 0x9c;
	public static final int MUL_LONG = 0x9d;
	public static final int DIV_LONG = 0x9e;
	public static final int REM_LONG = 0x9f;
	public static final int AND_LONG = 0xa0;
	public static final int OR_LONG = 0xa1;
	public static final int XOR_LONG = 0xa2;
	public static final int SHL_LONG = 0xa3;
	public static final int SHR_LONG = 0xa4;
	public static final int USHR_LONG = 0xa5;
	public static final int ADD_FLOAT = 0xa6;
	public static final int SUB_FLOAT = 0xa7;
	public static final int MUL_FLOAT = 0xa8;
	public static final int DIV_FLOAT = 0xa9;
	public static final int REM_FLOAT = 0xaa;
	public static final int ADD_DOUBLE = 0xab;
	public static final int SUB_DOUBLE = 0xac;
	public static final int MUL_DOUBLE = 0xad;
	public static final int DIV_DOUBLE = 0xae;
	public static final int REM_DOUBLE = 0xaf;
	public static final int ADD_INT_2ADDR = 0xb0;
	public static final int SUB_INT_2ADDR = 0xb1;
	public static final int MUL_INT_2ADDR = 0xb2;
	public static final int DIV_INT_2ADDR = 0xb3;
	public static final int REM_INT_2ADDR = 0xb4;
	public static final int AND_INT_2ADDR = 0xb5;
	public static final int OR_INT_2ADDR = 0xb6;
	public static final int XOR_INT_2ADDR = 0xb7;
	public static final int SHL_INT_2ADDR = 0xb8;
	public static final int SHR_INT_2ADDR = 0xb9;
	public static final int USHR_INT_2ADDR = 0xba;
	public static final int ADD_LONG_2ADDR = 0xbb;
	public static final int SUB_LONG_2ADDR = 0xbc;
	public static final int MUL_LONG_2ADDR = 0xbd;
	public static final int DIV_LONG_2ADDR = 0xbe;
	public static final int REM_LONG_2ADDR = 0xbf;
	public static final int AND_LONG_2ADDR = 0xc0;
	public static final int OR_LONG_2ADDR = 0xc1;
	public static final int XOR_LONG_2ADDR = 0xc2;
	public static final int SHL_LONG_2ADDR = 0xc3;
	public static final int SHR_LONG_2ADDR = 0xc4;
	public static final int USHR_LONG_2ADDR = 0xc5;
	public static final int ADD_FLOAT_2ADDR = 0xc6;
	public static final int SUB_FLOAT_2ADDR = 0xc7;
	public static final int MUL_FLOAT_2ADDR = 0xc8;
	public static final int DIV_FLOAT_2ADDR = 0xc9;
	public static final int REM_FLOAT_2ADDR = 0xca;
	public static final int ADD_DOUBLE_2ADDR = 0xcb;
	public static final int SUB_DOUBLE_2ADDR = 0xcc;
	public static final int MUL_DOUBLE_2ADDR = 0xcd;
	public static final int DIV_DOUBLE_2ADDR = 0xce;
	public static final int REM_DOUBLE_2ADDR = 0xcf;
	public static final int ADD_INT_LIT16 = 0xd0;
	public static final int RSUB_INT = 0xd1;
	public static final int MUL_INT_LIT16 = 0xd2;
	public static final int DIV_INT_LIT16 = 0xd3;
	public static final int REM_INT_LIT16 = 0xd4;
	public static final int AND_INT_LIT16 = 0xd5;
	public static final int OR_INT_LIT16 = 0xd6;
	public static final int XOR_INT_LIT16 = 0xd7;
	public static final int ADD_INT_LIT8 = 0xd8;
	public static final int RSUB_INT_LIT8 = 0xd9;
	public static final int MUL_INT_LIT8 = 0xda;
	public static final int DIV_INT_LIT8 = 0xdb;
	public static final int REM_INT_LIT8 = 0xdc;
	public static final int AND_INT_LIT8 = 0xdd;
	public static final int OR_INT_LIT8 = 0xde;
	public static final int XOR_INT_LIT8 = 0xdf;
	public static final int SHL_INT_LIT8 = 0xe0;
	public static final int SHR_INT_LIT8 = 0xe1;
	public static final int USHR_INT_LIT8 = 0xe2;
	public static final int CONST_CLASS_JUMBO = 0x00ff;
	public static final int CHECK_CAST_JUMBO = 0x01ff;
	public static final int INSTANCE_OF_JUMBO = 0x02ff;
	public static final int NEW_INSTANCE_JUMBO = 0x03ff;
	public static final int NEW_ARRAY_JUMBO = 0x04ff;
	public static final int FILLED_NEW_ARRAY_JUMBO = 0x05ff;
	public static final int IGET_JUMBO = 0x06ff;
	public static final int IGET_WIDE_JUMBO = 0x07ff;
	public static final int IGET_OBJECT_JUMBO = 0x08ff;
	public static final int IGET_BOOLEAN_JUMBO = 0x09ff;
	public static final int IGET_BYTE_JUMBO = 0x0aff;
	public static final int IGET_CHAR_JUMBO = 0x0bff;
	public static final int IGET_SHORT_JUMBO = 0x0cff;
	public static final int IPUT_JUMBO = 0x0dff;
	public static final int IPUT_WIDE_JUMBO = 0x0eff;
	public static final int IPUT_OBJECT_JUMBO = 0x0fff;
	public static final int IPUT_BOOLEAN_JUMBO = 0x10ff;
	public static final int IPUT_BYTE_JUMBO = 0x11ff;
	public static final int IPUT_CHAR_JUMBO = 0x12ff;
	public static final int IPUT_SHORT_JUMBO = 0x13ff;
	public static final int SGET_JUMBO = 0x14ff;
	public static final int SGET_WIDE_JUMBO = 0x15ff;
	public static final int SGET_OBJECT_JUMBO = 0x16ff;
	public static final int SGET_BOOLEAN_JUMBO = 0x17ff;
	public static final int SGET_BYTE_JUMBO = 0x18ff;
	public static final int SGET_CHAR_JUMBO = 0x19ff;
	public static final int SGET_SHORT_JUMBO = 0x1aff;
	public static final int SPUT_JUMBO = 0x1bff;
	public static final int SPUT_WIDE_JUMBO = 0x1cff;
	public static final int SPUT_OBJECT_JUMBO = 0x1dff;
	public static final int SPUT_BOOLEAN_JUMBO = 0x1eff;
	public static final int SPUT_BYTE_JUMBO = 0x1fff;
	public static final int SPUT_CHAR_JUMBO = 0x20ff;
	public static final int SPUT_SHORT_JUMBO = 0x21ff;
	public static final int INVOKE_VIRTUAL_JUMBO = 0x22ff;
	public static final int INVOKE_SUPER_JUMBO = 0x23ff;
	public static final int INVOKE_DIRECT_JUMBO = 0x24ff;
	public static final int INVOKE_STATIC_JUMBO = 0x25ff;
	public static final int INVOKE_INTERFACE_JUMBO = 0x26ff;
	// END(opcodes)

	/**
	 * Instructions that exist but should not be verified. See
	 * DecodedInstruction
	 */
	public static final int UNUSED_3E = 0x3e;
	public static final int UNUSED_3F = 0x3f;
	public static final int UNUSED_40 = 0x40;
	public static final int UNUSED_41 = 0x41;
	public static final int UNUSED_42 = 0x42;
	public static final int UNUSED_43 = 0x43;
	public static final int UNUSED_73 = 0x73;
	public static final int UNUSED_79 = 0x79;
	public static final int UNUSED_7A = 0x7a;
	public static final int IGET_VOLATILE = 0xe3;
	public static final int IPUT_VOLATILE = 0xe4;
	public static final int SGET_VOLATILE = 0xe5;
	public static final int SPUT_VOLATILE = 0xe6;
	public static final int IGET_OBJECT_VOLATILE = 0xe7;
	public static final int IGET_WIDE_VOLATILE = 0xe8;
	public static final int IPUT_WIDE_VOLATILE = 0xe9;
	public static final int SGET_WIDE_VOLATILE = 0xea;
	public static final int SPUT_WIDE_VOLATILE = 0xeb;
	public static final int BREAKPOINT = 0xec;
	public static final int THROW_VERIFICATION_ERROR = 0xed;
	public static final int EXECUTE_INLINE = 0xee;
	public static final int EXECUTE_INLINE_RANGE = 0xef;
	public static final int INVOKE_OBJECT_INIT_RANGE = 0xf0;
	public static final int RETURN_VOID_BARRIER = 0xf1;
	public static final int IGET_QUICK = 0xf2;
	public static final int IGET_WIDE_QUICK = 0xf3;
	public static final int IGET_OBJECT_QUICK = 0xf4;
	public static final int IPUT_QUICK = 0xf5;
	public static final int IPUT_WIDE_QUICK = 0xf6;
	public static final int IPUT_OBJECT_QUICK = 0xf7;
	public static final int INVOKE_VIRTUAL_QUICK = 0xf8;
	public static final int INVOKE_VIRTUAL_QUICK_RANGE = 0xf9;
	public static final int INVOKE_SUPER_QUICK = 0xfa;
	public static final int INVOKE_SUPER_QUICK_RANGE = 0xfb;
	public static final int IPUT_OBJECT_VOLATILE = 0xfc;
	public static final int SGET_OBJECT_VOLATILE = 0xfd;
	public static final int SPUT_OBJECT_VOLATILE = 0xfe;
	public static final int DISPATCH_FF = 0xff;
	public static final int UNUSED_27FF = 0x127;
	public static final int UNUSED_28FF = 0x128;
	public static final int UNUSED_29FF = 0x129;
	public static final int UNUSED_2AFF = 0x12a;
	public static final int UNUSED_2BFF = 0x12b;
	public static final int UNUSED_2CFF = 0x12c;
	public static final int UNUSED_2DFF = 0x12d;
	public static final int UNUSED_2EFF = 0x12e;
	public static final int UNUSED_2FFF = 0x12f;
	public static final int UNUSED_30FF = 0x130;
	public static final int UNUSED_31FF = 0x131;
	public static final int UNUSED_32FF = 0x132;
	public static final int UNUSED_33FF = 0x133;
	public static final int UNUSED_34FF = 0x134;
	public static final int UNUSED_35FF = 0x135;
	public static final int UNUSED_36FF = 0x136;
	public static final int UNUSED_37FF = 0x137;
	public static final int UNUSED_38FF = 0x138;
	public static final int UNUSED_39FF = 0x139;
	public static final int UNUSED_3AFF = 0x13a;
	public static final int UNUSED_3BFF = 0x13b;
	public static final int UNUSED_3CFF = 0x13c;
	public static final int UNUSED_3DFF = 0x13d;
	public static final int UNUSED_3EFF = 0x13e;
	public static final int UNUSED_3FFF = 0x13f;
	public static final int UNUSED_40FF = 0x140;
	public static final int UNUSED_41FF = 0x141;
	public static final int UNUSED_42FF = 0x142;
	public static final int UNUSED_43FF = 0x143;
	public static final int UNUSED_44FF = 0x144;
	public static final int UNUSED_45FF = 0x145;
	public static final int UNUSED_46FF = 0x146;
	public static final int UNUSED_47FF = 0x147;
	public static final int UNUSED_48FF = 0x148;
	public static final int UNUSED_49FF = 0x149;
	public static final int UNUSED_4AFF = 0x14a;
	public static final int UNUSED_4BFF = 0x14b;
	public static final int UNUSED_4CFF = 0x14c;
	public static final int UNUSED_4DFF = 0x14d;
	public static final int UNUSED_4EFF = 0x14e;
	public static final int UNUSED_4FFF = 0x14f;
	public static final int UNUSED_50FF = 0x150;
	public static final int UNUSED_51FF = 0x151;
	public static final int UNUSED_52FF = 0x152;
	public static final int UNUSED_53FF = 0x153;
	public static final int UNUSED_54FF = 0x154;
	public static final int UNUSED_55FF = 0x155;
	public static final int UNUSED_56FF = 0x156;
	public static final int UNUSED_57FF = 0x157;
	public static final int UNUSED_58FF = 0x158;
	public static final int UNUSED_59FF = 0x159;
	public static final int UNUSED_5AFF = 0x15a;
	public static final int UNUSED_5BFF = 0x15b;
	public static final int UNUSED_5CFF = 0x15c;
	public static final int UNUSED_5DFF = 0x15d;
	public static final int UNUSED_5EFF = 0x15e;
	public static final int UNUSED_5FFF = 0x15f;
	public static final int UNUSED_60FF = 0x160;
	public static final int UNUSED_61FF = 0x161;
	public static final int UNUSED_62FF = 0x162;
	public static final int UNUSED_63FF = 0x163;
	public static final int UNUSED_64FF = 0x164;
	public static final int UNUSED_65FF = 0x165;
	public static final int UNUSED_66FF = 0x166;
	public static final int UNUSED_67FF = 0x167;
	public static final int UNUSED_68FF = 0x168;
	public static final int UNUSED_69FF = 0x169;
	public static final int UNUSED_6AFF = 0x16a;
	public static final int UNUSED_6BFF = 0x16b;
	public static final int UNUSED_6CFF = 0x16c;
	public static final int UNUSED_6DFF = 0x16d;
	public static final int UNUSED_6EFF = 0x16e;
	public static final int UNUSED_6FFF = 0x16f;
	public static final int UNUSED_70FF = 0x170;
	public static final int UNUSED_71FF = 0x171;
	public static final int UNUSED_72FF = 0x172;
	public static final int UNUSED_73FF = 0x173;
	public static final int UNUSED_74FF = 0x174;
	public static final int UNUSED_75FF = 0x175;
	public static final int UNUSED_76FF = 0x176;
	public static final int UNUSED_77FF = 0x177;
	public static final int UNUSED_78FF = 0x178;
	public static final int UNUSED_79FF = 0x179;
	public static final int UNUSED_7AFF = 0x17a;
	public static final int UNUSED_7BFF = 0x17b;
	public static final int UNUSED_7CFF = 0x17c;
	public static final int UNUSED_7DFF = 0x17d;
	public static final int UNUSED_7EFF = 0x17e;
	public static final int UNUSED_7FFF = 0x17f;
	public static final int UNUSED_80FF = 0x180;
	public static final int UNUSED_81FF = 0x181;
	public static final int UNUSED_82FF = 0x182;
	public static final int UNUSED_83FF = 0x183;
	public static final int UNUSED_84FF = 0x184;
	public static final int UNUSED_85FF = 0x185;
	public static final int UNUSED_86FF = 0x186;
	public static final int UNUSED_87FF = 0x187;
	public static final int UNUSED_88FF = 0x188;
	public static final int UNUSED_89FF = 0x189;
	public static final int UNUSED_8AFF = 0x18a;
	public static final int UNUSED_8BFF = 0x18b;
	public static final int UNUSED_8CFF = 0x18c;
	public static final int UNUSED_8DFF = 0x18d;
	public static final int UNUSED_8EFF = 0x18e;
	public static final int UNUSED_8FFF = 0x18f;
	public static final int UNUSED_90FF = 0x190;
	public static final int UNUSED_91FF = 0x191;
	public static final int UNUSED_92FF = 0x192;
	public static final int UNUSED_93FF = 0x193;
	public static final int UNUSED_94FF = 0x194;
	public static final int UNUSED_95FF = 0x195;
	public static final int UNUSED_96FF = 0x196;
	public static final int UNUSED_97FF = 0x197;
	public static final int UNUSED_98FF = 0x198;
	public static final int UNUSED_99FF = 0x199;
	public static final int UNUSED_9AFF = 0x19a;
	public static final int UNUSED_9BFF = 0x19b;
	public static final int UNUSED_9CFF = 0x19c;
	public static final int UNUSED_9DFF = 0x19d;
	public static final int UNUSED_9EFF = 0x19e;
	public static final int UNUSED_9FFF = 0x19f;
	public static final int UNUSED_A0FF = 0x1a0;
	public static final int UNUSED_A1FF = 0x1a1;
	public static final int UNUSED_A2FF = 0x1a2;
	public static final int UNUSED_A3FF = 0x1a3;
	public static final int UNUSED_A4FF = 0x1a4;
	public static final int UNUSED_A5FF = 0x1a5;
	public static final int UNUSED_A6FF = 0x1a6;
	public static final int UNUSED_A7FF = 0x1a7;
	public static final int UNUSED_A8FF = 0x1a8;
	public static final int UNUSED_A9FF = 0x1a9;
	public static final int UNUSED_AAFF = 0x1aa;
	public static final int UNUSED_ABFF = 0x1ab;
	public static final int UNUSED_ACFF = 0x1ac;
	public static final int UNUSED_ADFF = 0x1ad;
	public static final int UNUSED_AEFF = 0x1ae;
	public static final int UNUSED_AFFF = 0x1af;
	public static final int UNUSED_B0FF = 0x1b0;
	public static final int UNUSED_B1FF = 0x1b1;
	public static final int UNUSED_B2FF = 0x1b2;
	public static final int UNUSED_B3FF = 0x1b3;
	public static final int UNUSED_B4FF = 0x1b4;
	public static final int UNUSED_B5FF = 0x1b5;
	public static final int UNUSED_B6FF = 0x1b6;
	public static final int UNUSED_B7FF = 0x1b7;
	public static final int UNUSED_B8FF = 0x1b8;
	public static final int UNUSED_B9FF = 0x1b9;
	public static final int UNUSED_BAFF = 0x1ba;
	public static final int UNUSED_BBFF = 0x1bb;
	public static final int UNUSED_BCFF = 0x1bc;
	public static final int UNUSED_BDFF = 0x1bd;
	public static final int UNUSED_BEFF = 0x1be;
	public static final int UNUSED_BFFF = 0x1bf;
	public static final int UNUSED_C0FF = 0x1c0;
	public static final int UNUSED_C1FF = 0x1c1;
	public static final int UNUSED_C2FF = 0x1c2;
	public static final int UNUSED_C3FF = 0x1c3;
	public static final int UNUSED_C4FF = 0x1c4;
	public static final int UNUSED_C5FF = 0x1c5;
	public static final int UNUSED_C6FF = 0x1c6;
	public static final int UNUSED_C7FF = 0x1c7;
	public static final int UNUSED_C8FF = 0x1c8;
	public static final int UNUSED_C9FF = 0x1c9;
	public static final int UNUSED_CAFF = 0x1ca;
	public static final int UNUSED_CBFF = 0x1cb;
	public static final int UNUSED_CCFF = 0x1cc;
	public static final int UNUSED_CDFF = 0x1cd;
	public static final int UNUSED_CEFF = 0x1ce;
	public static final int UNUSED_CFFF = 0x1cf;
	public static final int UNUSED_D0FF = 0x1d0;
	public static final int UNUSED_D1FF = 0x1d1;
	public static final int UNUSED_D2FF = 0x1d2;
	public static final int UNUSED_D3FF = 0x1d3;
	public static final int UNUSED_D4FF = 0x1d4;
	public static final int UNUSED_D5FF = 0x1d5;
	public static final int UNUSED_D6FF = 0x1d6;
	public static final int UNUSED_D7FF = 0x1d7;
	public static final int UNUSED_D8FF = 0x1d8;
	public static final int UNUSED_D9FF = 0x1d9;
	public static final int UNUSED_DAFF = 0x1da;
	public static final int UNUSED_DBFF = 0x1db;
	public static final int UNUSED_DCFF = 0x1dc;
	public static final int UNUSED_DDFF = 0x1dd;
	public static final int UNUSED_DEFF = 0x1de;
	public static final int UNUSED_DFFF = 0x1df;
	public static final int UNUSED_E0FF = 0x1e0;
	public static final int UNUSED_E1FF = 0x1e1;
	public static final int UNUSED_E2FF = 0x1e2;
	public static final int UNUSED_E3FF = 0x1e3;
	public static final int UNUSED_E4FF = 0x1e4;
	public static final int UNUSED_E5FF = 0x1e5;
	public static final int UNUSED_E6FF = 0x1e6;
	public static final int UNUSED_E7FF = 0x1e7;
	public static final int UNUSED_E8FF = 0x1e8;
	public static final int UNUSED_E9FF = 0x1e9;
	public static final int UNUSED_EAFF = 0x1ea;
	public static final int UNUSED_EBFF = 0x1eb;
	public static final int UNUSED_ECFF = 0x1ec;
	public static final int UNUSED_EDFF = 0x1ed;
	public static final int UNUSED_EEFF = 0x1ee;
	public static final int UNUSED_EFFF = 0x1ef;
	public static final int UNUSED_F0FF = 0x1f0;
	public static final int UNUSED_F1FF = 0x1f1;
	public static final int INVOKE_OBJECT_INIT_JUMBO = 0x1f2;
	public static final int IGET_VOLATILE_JUMBO = 0x1f3;
	public static final int IGET_WIDE_VOLATILE_JUMBO = 0x1f4;
	public static final int IGET_OBJECT_VOLATILE_JUMBO = 0x1f5;
	public static final int IPUT_VOLATILE_JUMBO = 0x1f6;
	public static final int IPUT_WIDE_VOLATILE_JUMBO = 0x1f7;
	public static final int IPUT_OBJECT_VOLATILE_JUMBO = 0x1f8;
	public static final int SGET_VOLATILE_JUMBO = 0x1f9;
	public static final int SGET_WIDE_VOLATILE_JUMBO = 0x1fa;
	public static final int SGET_OBJECT_VOLATILE_JUMBO = 0x1fb;
	public static final int SPUT_VOLATILE_JUMBO = 0x1fc;
	public static final int SPUT_WIDE_VOLATILE_JUMBO = 0x1fd;
	public static final int SPUT_OBJECT_VOLATILE_JUMBO = 0x1fe;
	public static final int THROW_VERIFICATION_ERROR_JUMBO = 0x1ff;

	// TODO: Generate these payload opcodes with opcode-gen.

	/**
	 * special pseudo-opcode value for packed-switch data payload instructions
	 */
	public static final int PACKED_SWITCH_PAYLOAD = 0x100;

	/**
	 * special pseudo-opcode value for packed-switch data payload instructions
	 */
	public static final int SPARSE_SWITCH_PAYLOAD = 0x200;

	/**
	 * special pseudo-opcode value for fill-array-data data payload instructions
	 */
	public static final int FILL_ARRAY_DATA_PAYLOAD = 0x300;

	/**
	 * This class is uninstantiable.
	 */
	private Opcodes() {
		// This space intentionally left blank.
	}

	/**
	 * Determines if the given opcode has the right "shape" to be valid. This
	 * includes the range {@code 0x01..0xfe}, the range {@code 0x00ff..0xffff}
	 * where the low-order byte is either {@code 0} or {@code 0xff}, and the
	 * special opcode values {@code SPECIAL_FORMAT} and {@code NO_NEXT}. Note
	 * that not all of the opcode values that pass this test are in fact used.
	 * This method is meant to perform a quick check to reject blatantly wrong
	 * values (e.g. when validating arguments).
	 * 
	 * @param opcode
	 *            the opcode value
	 * @return {@code true} iff the value has the right "shape" to be possibly
	 *         valid
	 */
	public static boolean isValidShape(int opcode) {
		/*
		 * Note: This method bakes in knowledge that all opcodes are one of the
		 * forms:
		 * 
		 * * single byte in range 0x01..0xfe -- normal opcodes * (byteValue <<
		 * 8) -- nop and data payload opcodes * ((byteValue << 8) | 0xff) --
		 * 16-bit extended opcodes * SPECIAL_FORMAT or NO_NEXT -- pseudo-opcodes
		 */

		// Note: SPECIAL_FORMAT == NO_NEXT.
		if (opcode < SPECIAL_FORMAT) {
			return false;
		} else if (opcode == SPECIAL_FORMAT) {
			return true;
		}

		int lowByte = opcode & 0xff;
		if ((lowByte == 0) || (lowByte == 0xff)) {
			return true;
		}

		return (opcode & 0xff00) == 0;
	}

	/**
	 * Gets whether ({@code true}) or not ({@code false}) the given opcode value
	 * is an "extended" opcode (not counting the nop-like payload opcodes).
	 * Extended opcodes require a full 16-bit code unit to represent, without
	 * leaving space for an argument byte.
	 * 
	 * @param opcode
	 *            the opcode value
	 * @return {@code true} iff the opcode is an "extended" opcode
	 */
	public static boolean isExtended(int opcode) {
		/*
		 * Note: Extended opcodes all have the form ((byteValue << 8) | 0xff).
		 */
		return (opcode >= 0x00ff);
	}

	/**
	 * Gets the opcode out of an opcode unit, the latter of which may also
	 * include one or more argument values.
	 * 
	 * @param opcodeUnit
	 *            the opcode-containing code unit
	 * @return the extracted opcode
	 */
	public static int extractOpcodeFromUnit(int opcodeUnit) {
		/*
		 * Note: This method bakes in knowledge that all opcodes are either
		 * single-byte or of the forms (byteValue << 8) or ((byteValue << 8) |
		 * 0xff).
		 */

		int lowByte = opcodeUnit & 0xff;
		return ((lowByte == 0) || (lowByte == 0xff)) ? opcodeUnit : lowByte;
	}
}
