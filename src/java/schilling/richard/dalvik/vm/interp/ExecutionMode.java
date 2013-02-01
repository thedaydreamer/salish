package schilling.richard.dalvik.vm.interp;

public enum ExecutionMode {

	/*
	 * Execution mode, e.g. interpreter vs. JIT.
	 */
	kExecutionModeUnknown,
	kExecutionModeInterpPortable,
	kExecutionModeInterpFast,
	kExecutionModeJit,
}
