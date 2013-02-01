package schilling.richard.dalvik.vm.analysis;

public class DumpRegisterType {
	public static final int DRT_SIMPLE = 0;
	public static final int DRT_SHOW_REF_TYPES = 0x01;
	public static final int DRT_SHOW_LOCALS = 0x02;
	public static final int DRT_SHOW_LIVENESS = 0x04;
	public static final int SHOW_REG_DETAILS = DRT_SHOW_LIVENESS; /*
																 * |
																 * DRT_SHOW_REF_TYPES
																 * |
																 * DRT_SHOW_LOCALS
																 */
}
