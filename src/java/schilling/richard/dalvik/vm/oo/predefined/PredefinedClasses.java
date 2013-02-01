package schilling.richard.dalvik.vm.oo.predefined;

import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BOOLEAN;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_BYTE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_CHAR;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_DOUBLE;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_FLOAT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_INTEGER;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_LONG;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_SHORT;
import static schilling.richard.dalvik.vm.oo.predefined.BoxClasses.SIG_VOID;

/**
 * Define some pre-defined class signatures.
 * 
 * <p>
 * See array.h.
 * 
 * 
 * @author rschilling
 * 
 */
public final class PredefinedClasses {

	public static final String SIG_JAVA_LANG_OBJECT = "Ljava/lang/Object;"
			.intern();
	public static final String SIG_JAVA_LANG_THROWABLE = "Ljava/lang/Throwable;"
			.intern();
	public static final String SIG_JAVA_LANG_CLASS = "Ljava/lang/Class;"
			.intern();
	public static final String SIG_JAVA_LANG_STRING = "Ljava/lang/String;"
			.intern();

	public static final String[] PREDEFINED_SIGNATURES = {
			SIG_JAVA_LANG_OBJECT, SIG_JAVA_LANG_THROWABLE, SIG_JAVA_LANG_CLASS,
			SIG_JAVA_LANG_STRING, SIG_BOOLEAN, SIG_BYTE, SIG_CHAR, SIG_DOUBLE,
			SIG_FLOAT, SIG_INTEGER, SIG_LONG, SIG_SHORT, SIG_VOID };

}
