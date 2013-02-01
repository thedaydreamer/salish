package schilling.richard.dalvik.vm.analysis;

/**
 * Register map generation mode. Only applicable when generateRegisterMaps is
 * enabled. (The "disabled" state is not folded into this because there are
 * callers like dexopt that want to enable/disable without specifying the
 * configuration details.)
 * 
 * <p>
 * "TypePrecise" is slower and requires additional storage for the register
 * maps, but allows type-precise GC. "LivePrecise" is even slower and requires
 * additional heap during processing, but allows live-precise GC.
 */
public enum RegisterMapMode {
	kRegisterMapModeUnknown,
	kRegisterMapModeTypePrecise,
	kRegisterMapModeLivePrecise
}
