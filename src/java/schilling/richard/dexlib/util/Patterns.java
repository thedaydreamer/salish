package schilling.richard.dexlib.util;

import java.util.regex.Pattern;

/**
 * Regular expression patterns the dexlib uses.
 * 
 * @author rschilling
 */
public final class Patterns {

	private Patterns() {
	}

	/**
	 * File names often used by various platforms.
	 */
	public static Pattern RESERVED_FILE_NAMES = Pattern
			.compile("^CON|EXE|PRN|AUX|NUL|COM[1-9]|LPT[1-9]$",
					Pattern.CASE_INSENSITIVE);

}
