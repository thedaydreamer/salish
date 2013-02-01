package schilling.richard.dalvik.vm.analysis;

/**
 * An enumeration of problems that can turn up during verification. The original
 * code calls defines this as a VerifyError enum.
 */
public enum VerifyErrorCause {

	VERIFY_ERROR_NONE, /* no error; must be zero */
	VERIFY_ERROR_GENERIC, /* VerifyError */
	VERIFY_ERROR_NO_CLASS, /* NoClassDefFoundError */
	VERIFY_ERROR_NO_FIELD, /* NoSuchFieldError */
	VERIFY_ERROR_NO_METHOD, /* NoSuchMethodError */
	VERIFY_ERROR_ACCESS_CLASS, /* IllegalAccessError */
	VERIFY_ERROR_ACCESS_FIELD, /* IllegalAccessError */
	VERIFY_ERROR_ACCESS_METHOD, /* IllegalAccessError */
	VERIFY_ERROR_CLASS_CHANGE, /* IncompatibleClassChangeError */
	VERIFY_ERROR_INSTANTIATION /* InstantiationError */

};
