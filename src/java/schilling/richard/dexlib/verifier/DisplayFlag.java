package schilling.richard.dexlib.verifier;

/* TODO: write a wite paper that explains different code structures and how they affect resource usage or memory usage.  Include examples of blocks of code
 */

/**
 * bit values for dumpRegTypes() "displayFlags"
 * 
 * <pre>
 * enum {
 *     DRT_SIMPLE          = 0,
 *     DRT_SHOW_REF_TYPES  = 0x01,
 *     DRT_SHOW_LOCALS     = 0x02,
 *     DRT_SHOW_LIVENESS   = 0x04,
 * };
 * </pre>
 */
public enum DisplayFlag {

	DRT_SIMPLE(0),
	DRT_SHOW_REF_TYPES(0x01),
	DRT_SHOW_LOCALS(0x02),
	DRT_SHOW_LIVENESS(0x04);

	private int intValue;

	private DisplayFlag(int i) {
		intValue = i;
	}

	public int intValue() {
		return intValue;
	}

}
