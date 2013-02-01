package schilling.richard.dexlib.io.util;

import java.io.File;

public final class FileUtil {
	public static boolean rmdir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = rmdir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
}
