package schilling.richard.dalvik.vm.analysis;

/*
 * Global verification mode.  These must be in order from least verification
 * to most.  If we're using "exact GC", we may need to perform some of
 * the verification steps anyway.
 */
public enum DexClassVerifyMode {
	VERIFY_MODE_UNKNOWN, // = 0,
	VERIFY_MODE_NONE,
	VERIFY_MODE_REMOTE,
	VERIFY_MODE_ALL
}
