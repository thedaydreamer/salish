package schilling.richard.dalvik.vm;

import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeBoolean;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConflict;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstChar;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstHi;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstPosByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstPosShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeConstShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleHi;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeDoubleLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeFloat;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeInteger;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongHi;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeLongLo;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeOne;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosByte;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypePosShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeShort;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUninit;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeUnknown;
import static schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum.kRegTypeZero;
import static schilling.richard.dexlib.verifier.DisplayFlag.DRT_SHOW_LIVENESS;
import schilling.richard.dalvik.vm.analysis.RegType;
import schilling.richard.dalvik.vm.analysis.RegisterType;
import schilling.richard.dalvik.vm.analysis.RegisterType.RegisterTypeEnum;

// DON"T FORMAT THIS FILE OR IT WILL SCREW UP THE PRETTY LOOKING TABLES.
/**
 * Constants used by the CodeVerify class.
 * 
 * @author rschilling
 */
public class CodeVerifyConstants {

	{
		checkMergeTab();
	}

	/**
	 * Set this to enable dead code scanning. This is not required, but it's
	 * very useful when testing changes to the verifier (to make sure we're not
	 * skipping over stuff) and for checking the optimized output from "dx". The
	 * only reason not to do it is that it slightly increases the time required
	 * to perform verification.
	 */
	public static final boolean DEAD_CODE_SCAN = true;
	public static final boolean gDebugVerbose = false;

	public static final int SHOW_REG_DETAILS = 0 | DRT_SHOW_LIVENESS.intValue();
	// | DRT_SHOW_REF_TYPES.intValue() | DRT_SHOW_LOCALS.intValue()

	/**
	 * We need an extra "pseudo register" to hold the return type briefly. It
	 * can be category 1 or 2, so we need two slots.
	 */
	public static final int kExtraRegs = 2;
	// #define RESULT_REGISTER(_insnRegCount) (_insnRegCount)
	// TODO how is RESULT_REGISTER used?

	public static final RegisterTypeEnum kRegType1nrSTART = kRegTypeZero;
	public static final RegisterTypeEnum kRegType1nrEND = kRegTypeFloat;

	public static final int kRegTypeUninitMask = 0xff;
	public static final int kRegTypeUninitShift = 8;
	/*
	 * A bit vector indicating which entries in the monitor stack are associated
	 * with this register. The low bit corresponds to the stack's bottom-most
	 * entry.
	 * 
	 * <P> <pre> typedef u4 MonitorEntries; #define kMaxMonitorStackDepth
	 * (sizeof(MonitorEntries) * 8) </pre>
	 */
	public static final int kMaxMonitorStackDepth = 64; // monitor entries

	public static final int kUninitThisArgAddr = -1;
	public static final int kUninitThisArgSlot = 0;
	public static final int kInsnFlagWidthMask = 0x0000ffff;
	public static final int kInsnFlagInTry = (1 << 16);
	public static final int kInsnFlagBranchTarget = (1 << 17);
	public static final int kInsnFlagGcPoint = (1 << 18);
	public static final int kInsnFlagVisited = (1 << 30);
	public static final int kInsnFlagChanged = (1 << 31);

	/* constants used in the table below for easy import. */

	public static final RegisterTypeEnum __ = kRegTypeUnknown;
	public static final RegisterTypeEnum _U = kRegTypeUninit;
	public static final RegisterTypeEnum _X = kRegTypeConflict;
	public static final RegisterTypeEnum _0 = kRegTypeZero;
	public static final RegisterTypeEnum _1 = kRegTypeOne;
	public static final RegisterTypeEnum _Z = kRegTypeBoolean;
	public static final RegisterTypeEnum _y = kRegTypeConstPosByte;
	public static final RegisterTypeEnum _Y = kRegTypeConstByte;
	public static final RegisterTypeEnum _h = kRegTypeConstPosShort;
	public static final RegisterTypeEnum _H = kRegTypeConstShort;
	public static final RegisterTypeEnum _c = kRegTypeConstChar;
	public static final RegisterTypeEnum _i = kRegTypeConstInteger;
	public static final RegisterTypeEnum _b = kRegTypePosByte;
	public static final RegisterTypeEnum _B = kRegTypeByte;
	public static final RegisterTypeEnum _s = kRegTypePosShort;
	public static final RegisterTypeEnum _S = kRegTypeShort;
	public static final RegisterTypeEnum _C = kRegTypeChar;
	public static final RegisterTypeEnum _I = kRegTypeInteger;
	public static final RegisterTypeEnum _F = kRegTypeFloat;
	public static final RegisterTypeEnum _N = kRegTypeConstLo;
	public static final RegisterTypeEnum _n = kRegTypeConstHi;
	public static final RegisterTypeEnum _J = kRegTypeLongLo;
	public static final RegisterTypeEnum _j = kRegTypeLongHi;
	public static final RegisterTypeEnum _D = kRegTypeDoubleLo;
	public static final RegisterTypeEnum _d = kRegTypeDoubleHi;

	/*
	 * Merge result table for primitive values. The table is symmetric along the
	 * diagonal.
	 * 
	 * <p>Note that 32-bit int/float do not merge into 64-bit long/double. This
	 * is a register merge, not a widening conversion. Only the "implicit"
	 * widening within a category, e.g. byte to short, is allowed.
	 * 
	 * <p>Dalvik does not draw a distinction between int and float, but we
	 * enforce that once a value is used as int, it can't be used as float, and
	 * vice versa. We do not allow free exchange between 32-bit int/float and
	 * 64-bit long/double.
	 * 
	 * <p>Note that Uninit+Uninit=Uninit. This holds true because we only use
	 * this when the RegType value is exactly equal to kRegTypeUninit, which can
	 * only happen for the zeroeth entry in the table.
	 * 
	 * <p>"Unknown" never merges with anything known. The only time a register
	 * transitions from "unknown" to "known" is when we're executing code for
	 * the first time, and we handle that with a simple copy.
	 */
	// @formatter:off
	public static final RegisterTypeEnum gDvmMergeTab[][] = {
			/* chk: _ U X 0 1 Z y Y h H c i b B s S C I F N n J j D d */
			{ /* _ */__, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _X, _X, _X, _X, _X, _X },
			{ /* U */_X, _U, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _X, _X, _X, _X, _X, _X },
			{ /* X */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _X, _X, _X, _X, _X, _X },
			{ /* 0 */_X, _X, _X, _0, _Z, _Z, _y, _Y, _h, _H, _c, _i, _b, _B, _s,
					_S, _C, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* 1 */_X, _X, _X, _Z, _1, _Z, _y, _Y, _h, _H, _c, _i, _b, _B, _s,
					_S, _C, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* Z */_X, _X, _X, _Z, _Z, _Z, _y, _Y, _h, _H, _c, _i, _b, _B, _s,
					_S, _C, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* y */_X, _X, _X, _y, _y, _y, _y, _Y, _h, _H, _c, _i, _b, _B, _s,
					_S, _C, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* Y */_X, _X, _X, _Y, _Y, _Y, _Y, _Y, _h, _H, _c, _i, _B, _B, _S,
					_S, _I, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* h */_X, _X, _X, _h, _h, _h, _h, _h, _h, _H, _c, _i, _s, _S, _s,
					_S, _C, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* H */_X, _X, _X, _H, _H, _H, _H, _H, _H, _H, _c, _i, _S, _S, _S,
					_S, _I, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* c */_X, _X, _X, _c, _c, _c, _c, _c, _c, _c, _c, _i, _C, _I, _C,
					_I, _C, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* i */_X, _X, _X, _i, _i, _i, _i, _i, _i, _i, _i, _i, _I, _I, _I,
					_I, _I, _I, _F, _X, _X, _X, _X, _X, _X },
			{ /* b */_X, _X, _X, _b, _b, _b, _b, _B, _s, _S, _C, _I, _b, _B, _s,
					_S, _C, _I, _X, _X, _X, _X, _X, _X, _X },
			{ /* B */_X, _X, _X, _B, _B, _B, _B, _B, _S, _S, _I, _I, _B, _B, _S,
					_S, _I, _I, _X, _X, _X, _X, _X, _X, _X },
			{ /* s */_X, _X, _X, _s, _s, _s, _s, _S, _s, _S, _C, _I, _s, _S, _s,
					_S, _C, _I, _X, _X, _X, _X, _X, _X, _X },
			{ /* S */_X, _X, _X, _S, _S, _S, _S, _S, _S, _S, _I, _I, _S, _S, _S,
					_S, _I, _I, _X, _X, _X, _X, _X, _X, _X },
			{ /* C */_X, _X, _X, _C, _C, _C, _C, _I, _C, _I, _C, _I, _C, _I, _C,
					_I, _C, _I, _X, _X, _X, _X, _X, _X, _X },
			{ /* I */_X, _X, _X, _I, _I, _I, _I, _I, _I, _I, _I, _I, _I, _I, _I,
					_I, _I, _I, _X, _X, _X, _X, _X, _X, _X },
			{ /* F */_X, _X, _X, _F, _F, _F, _F, _F, _F, _F, _F, _F, _X, _X, _X,
					_X, _X, _X, _F, _X, _X, _X, _X, _X, _X },
			{ /* N */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _N, _X, _J, _X, _D, _X },
			{ /* n */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _X, _n, _X, _j, _X, _d },
			{ /* J */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _J, _X, _J, _X, _X, _X },
			{ /* j */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _X, _j, _X, _j, _X, _X },
			{ /* D */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _D, _X, _X, _X, _D, _X },
			{ /* d */_X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X, _X,
					_X, _X, _X, _X, _X, _d, _X, _X, _X, _d }, };

	public static final int[][] convTab = {
	/* chk: 0 1 Z y Y h H c i b B s S C I F */
	{ /* 0 */1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ /* 1 */0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ /* Z */0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
			{ /* y */0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ /* Y */0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 },
			{ /* h */0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1 },
			{ /* H */0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1 },
			{ /* c */0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1 },
			{ /* i */0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1 },
			{ /* b */0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0 },
			{ /* B */0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
			{ /* s */0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0 },
			{ /* S */0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
			{ /* C */0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 },
			{ /* I */0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
			{ /* F */0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, };

	// @formatter:on

	/**
	 * Determine whether we can convert "srcType" to "checkType", where
	 * "checkType" is one of the category-1 non-reference types.
	 * <p>
	 * Constant derived types may become floats, but other values may not.
	 */

	public static boolean canConvertTo1nr(RegType srcType, RegType checkType) {

		if (srcType.register.typeEnum().ordinal() >= kRegType1nrSTART.ordinal()
				&& srcType.register.typeEnum().ordinal() <= kRegType1nrEND
						.ordinal()) {

			// use x and y for readibilty.
			int x = srcType.register.typeEnum().ordinal()
					- kRegType1nrSTART.ordinal();
			int y = checkType.register.typeEnum().ordinal()
					- kRegType1nrSTART.ordinal();
			return convTab[x][y] == 1 ? true : false;
		}

		return false;
	}

	/**
	 * Determine whether we can convert "srcType" to "checkType", where
	 * "checkType" is one of the category-1 non-reference types.
	 * <p>
	 * Constant derived types may become floats, but other values may not.
	 */

	public static boolean canConvertTo1nr(RegisterTypeEnum srcType,
			RegisterTypeEnum checkType) {

		if (srcType.ordinal() >= kRegType1nrSTART.ordinal()
				&& srcType.ordinal() <= kRegType1nrEND.ordinal()) {

			// use x and y for readibilty.
			int x = srcType.ordinal() - kRegType1nrSTART.ordinal();
			int y = checkType.ordinal() - kRegType1nrSTART.ordinal();
			return convTab[x][y] == 1 ? true : false;
		}

		return false;
	}

	// FIXME: the c source is so screwed up that data structure names are
	// crossed. It's as if multiple people wrote different parts and then didn't
	// coordinate. Fuck!

	/**
	 * Determine whether the category-2 types are compatible.
	 */
	public static boolean canConvertTo2(RegisterTypeEnum srcType,
			RegisterTypeEnum checkType) {
		return ((srcType.ordinal() == kRegTypeConstLo.ordinal() || srcType
				.ordinal() == checkType.ordinal()) && (checkType.ordinal() == kRegTypeLongLo
				.ordinal() || checkType.ordinal() == kRegTypeDoubleLo.ordinal()));
	}

	public static boolean canConvertTo2(RegisterType srcType,
			RegisterType checkType) {
		return ((srcType.typeEnum().ordinal() == kRegTypeConstLo.ordinal() || srcType
				.typeEnum().ordinal() == checkType.typeEnum().ordinal()) && (checkType
				.typeEnum().ordinal() == kRegTypeLongLo.ordinal() || checkType
				.typeEnum().ordinal() == kRegTypeDoubleLo.ordinal()));
	}

	/**
	 * Verify symmetry in the conversion table.
	 */
	public static void checkMergeTab() {
		int i, j;

		for (i = 0; i < gDvmMergeTab.length; i++) {
			for (j = i; j < gDvmMergeTab[i].length; j++) {
				if (gDvmMergeTab[i][j] != gDvmMergeTab[j][i]) {
					throw new IllegalStateException(String.format(
							"Symmetry violation: %d,%d vs %d,%d", i, j, j, i));
				}
			}
		}
	}

	/**
	 * Determine whether or not "instrType" and "targetType" are compatible, for
	 * purposes of getting or setting a value in a field or array. The idea is
	 * that an instruction with a category 1nr type (say, aget-short or
	 * iput-boolean) is accessing a static field, instance field, or array
	 * entry, and we want to make sure sure that the operation is legal. At a
	 * minimum, source and destination must have the same width. We further
	 * refine this to assert that "short" and "char" are not compatible, because
	 * the sign-extension is different on the "get" operations. We're not
	 * considering the actual contents of the register, so we'll never get
	 * "pseudo-types" like kRegTypeZero or kRegTypePosShort. We could get
	 * kRegTypeUnknown in "targetType" if a field or array class lookup failed.
	 * Category 2 types and references are checked elsewhere.
	 */
	static boolean checkFieldArrayStore1nr(RegType instrType, RegType targetType) {
		return (instrType == targetType);
	}

}
