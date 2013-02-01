package schilling.richard.dalvik.vm.analysis;

/**
 * We don't need to store the register data for many instructions, because we
 * either only need it at branch points (for verification) or GC points and
 * branches (for verification + type-precise register analysis).
 */
public enum RegisterTrackingMode {
	kTrackRegsBranches, kTrackRegsGcPoints, kTrackRegsAll
}
