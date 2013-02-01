package schilling.richard.dalvik.vm;

/*
 * Possible instruction formats associated with Dalvik opcodes.
 *
 * See the file opcode-gen/README.txt for information about updating
 * opcodes and instruction formats.
 */
// @formatter:off
public enum InstructionFormat {
	kFmt00x, // = 0, // unknown format (also used for "breakpoint" opcode)
	kFmt10x, // op
	kFmt12x, // op vA, vB
	kFmt11n, // op vA, #+B
	kFmt11x, // op vAA
	kFmt10t, // op +AA
	kFmt20bc, // [opt] op AA, thing@BBBB
	kFmt20t, // op +AAAA
	kFmt22x, // op vAA, vBBBB
	kFmt21t, // op vAA, +BBBB
	kFmt21s, // op vAA, #+BBBB
	kFmt21h, // op vAA, #+BBBB00000[00000000]
	kFmt21c, // op vAA, thing@BBBB
	kFmt23x, // op vAA, vBB, vCC
	kFmt22b, // op vAA, vBB, #+CC
	kFmt22t, // op vA, vB, +CCCC
	kFmt22s, // op vA, vB, #+CCCC
	kFmt22c, // op vA, vB, thing@CCCC
	kFmt22cs, // [opt] op vA, vB, field offset CCCC
	kFmt30t, // op +AAAAAAAA
	kFmt32x, // op vAAAA, vBBBB
	kFmt31i, // op vAA, #+BBBBBBBB
	kFmt31t, // op vAA, +BBBBBBBB
	kFmt31c, // op vAA, string@BBBBBBBB
	kFmt35c, // op {vC,vD,vE,vF,vG}, thing@BBBB
	kFmt35ms, // [opt] invoke-virtual+super
	kFmt3rc, // op {vCCCC .. v(CCCC+AA-1)}, thing@BBBB
	kFmt3rms, // [opt] invoke-virtual+super/range
	kFmt51l, // op vAA, #+BBBBBBBBBBBBBBBB
	kFmt35mi, // [opt] inline invoke
	kFmt3rmi, // [opt] inline invoke/range
	kFmt33x, // exop vAA, vBB, vCCCC
	kFmt32s, // exop vAA, vBB, #+CCCC
	kFmt40sc, // [opt] exop AAAA, thing@BBBBBBBB
	kFmt41c, // exop vAAAA, thing@BBBBBBBB
	kFmt52c, // exop vAAAA, vBBBB, thing@CCCCCCCC
	kFmt5rc, // exop {vCCCC .. v(CCCC+AAAA-1)}, thing@BBBBBBBB
};
// @formatter:on
