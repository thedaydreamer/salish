package schilling.richard.dalvik.vm.oo;

import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_BOOLEAN;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_BYTE;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_CHAR;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_DOUBLE;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_FLOAT;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_INT;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_LONG;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_SHORT;
import static schilling.richard.dalvik.vm.PrimitiveType.PRIM_VOID;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BOOLEAN;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BYTE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_CHAR;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_DOUBLE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_FLOAT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_INTEGER;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_LONG;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_SHORT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_VOID;
import schilling.richard.dalvik.vm.PrimitiveType;

public final class PrimitiveClass extends PlatformClass {

	private static PrimitiveClass BYTE_DEF = null;

	private static PrimitiveClass SHORT_DEF = null;

	private static PrimitiveClass INT_DEF = null;

	private static PrimitiveClass LONG_DEF = null;

	private static PrimitiveClass FLOAT_DEF = null;

	private static PrimitiveClass DOUBLE_DEF = null;

	private static PrimitiveClass CHAR_DEF = null;

	private static PrimitiveClass BOOLEAN_DEF = null;

	private static PrimitiveClass VOID_DEF = null;

	private final PrimitiveType pType;

	private PrimitiveClass(DexModel model, PrimitiveType type,
			String signature, Class<? extends Object> c) {
		super(model, signature, c);

		// developer sanity checks
		if (signature == null || model == null || c == null || type == null)
			throw new IllegalStateException("no parameter can be null.");

		pType = type;

	}

	public PrimitiveType primitiveTypeEnum() {
		return pType;
	}

	static PrimitiveClass getByteClass(DexModel model) {
		if (BYTE_DEF == null)
			BYTE_DEF = new PrimitiveClass(model, PRIM_BYTE, SIG_BYTE,
					byte.class);

		return BYTE_DEF;
	}

	static PrimitiveClass getShortClass(DexModel model) {
		if (SHORT_DEF == null)
			SHORT_DEF = new PrimitiveClass(model, PRIM_SHORT, SIG_SHORT,
					short.class);

		return SHORT_DEF;
	}

	static PrimitiveClass getIntClass(DexModel model) {
		if (INT_DEF == null)
			INT_DEF = new PrimitiveClass(model, PRIM_INT, SIG_INTEGER,
					int.class);

		return INT_DEF;
	}

	static PrimitiveClass getLongClass(DexModel model) {
		if (LONG_DEF == null)
			LONG_DEF = new PrimitiveClass(model, PRIM_LONG, SIG_LONG,
					long.class);

		return LONG_DEF;
	}

	static PrimitiveClass getFloatClass(DexModel model) {
		if (FLOAT_DEF == null)
			FLOAT_DEF = new PrimitiveClass(model, PRIM_FLOAT, SIG_FLOAT,
					float.class);

		return FLOAT_DEF;
	}

	static PrimitiveClass getDoubleClass(DexModel model) {
		if (DOUBLE_DEF == null)
			DOUBLE_DEF = new PrimitiveClass(model, PRIM_DOUBLE, SIG_DOUBLE,
					double.class);

		return DOUBLE_DEF;
	}

	static PrimitiveClass getCharClass(DexModel model) {
		if (CHAR_DEF == null)
			CHAR_DEF = new PrimitiveClass(model, PRIM_CHAR, SIG_CHAR,
					char.class);

		return CHAR_DEF;
	}

	static PrimitiveClass getBooleanClass(DexModel model) {
		if (BOOLEAN_DEF == null)
			BOOLEAN_DEF = new PrimitiveClass(model, PRIM_BOOLEAN, SIG_BOOLEAN,
					boolean.class);

		return BOOLEAN_DEF;
	}

	static PrimitiveClass getVoidClass(DexModel model) {

		if (VOID_DEF == null)
			VOID_DEF = new PrimitiveClass(model, PRIM_VOID, SIG_VOID,
					void.class);

		return VOID_DEF;
	}

	// TODO define resolveSuperclass - always return java.lang.Object

}
