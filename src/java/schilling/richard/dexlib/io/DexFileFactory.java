package schilling.richard.dexlib.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import schilling.richard.dexlib.io.DexFile.NoClassesDexException;

/**
 * Creates DexFile objects from files and input streams.
 * 
 * @author rschilling
 * 
 */
public class DexFileFactory {

	/**
	 * Create an empty dex file.
	 * 
	 * @param preserveSignedRegisters
	 * @param skipInstructions
	 * @return
	 */
	public static DexFile createEmptyFile() {
		return new DexFile();
	}

	/**
	 * Construct a new DexFile instance by reading in the given file path.
	 * Signed registers are preserved and no instructions are skipped.
	 * <p>
	 * Calls {@link #loadFromFile(String, boolean, boolean) loadFromFile(String,
	 * true, false)}.
	 * 
	 * @param path
	 *            The path of the dex file to read in
	 * @throws IOException
	 *             if an IOException occurs
	 * @see #loadFromFile(String, boolean, boolean)
	 */
	public static DexFile loadFromFile(String path) throws IOException {
		return loadFromFile(new File(path));
	}

	/**
	 * <p>
	 * Calls {@link #loadFromFile(File, boolean, boolean) loadFromFile(File,
	 * true, false)}.
	 * 
	 * @throws IOException
	 */

	public static DexFile loadFromFile(File file) throws IOException {

		DexStream is = getDexInputStream(file);
		return loadFromStream(is);

	}

	public static DexFile loadFromStream(DexStream stream) throws IOException {
		DexFile result = new DexFile();
		stream.loadFromStream(result);

		return result;
	}

	// TODO: move getDexStream to a utility class. This class should only handle
	// DexStreams.
	/**
	 * Returns an input stream that corresponds to a dex file after checking to
	 * be sure that the specified File can produce a valid dex input stream. The
	 * file is checked to see if it's a .zip file (e.g. an .APK file), or if the
	 * file is a straight .dex file. If the file is a .zip file, then the entry
	 * classes.dex is expected to be found.
	 * 
	 * @param file
	 *            the file to get a dex stream from.
	 * @throws IOException
	 *             is the file cannot be opened.
	 * @throws NoClassesDexException
	 *             if the file is a zip file that contains no entry for
	 *             classes.dex.
	 * @throws IllegalArgument
	 *             exception if the file is too large or too small or
	 */
	private static DexStream getDexInputStream(File file) throws IOException {

		StreamType st = StreamType.determineType(file);
		InputStream result = null;

		long streamLength = 0;

		switch (st) {
		case ZIP_STREAM:
			ZipFile zipFile = new ZipFile(file);
			ZipEntry zipEntry = zipFile.getEntry("classes.dex");
			if (zipEntry == null) {
				throw new FileNotFoundException("zip file " + file.getName()
						+ " does not contain a classes.dex " + "file");
			}
			streamLength = zipEntry.getSize();

			result = new BufferedInputStream(zipFile.getInputStream(zipEntry));

			zipFile.close();
			break;
		case DEX_STREAM:
			streamLength = file.length();
			result = new FileInputStream(file);
			break;
		case BYTE_STREAM:
			throw new IOException(file + " is not a ZIP or DEX file.");
		}

		if (streamLength < 40) {
			throw new IllegalArgumentException("The classes.dex file in "
					+ file.getName() + " is too small to be a"
					+ " valid dex file");
		} else if (streamLength > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("The classes.dex file in "
					+ file.getName() + " is too large to read in");
		}

		return new DexStream(result);
	}
}
