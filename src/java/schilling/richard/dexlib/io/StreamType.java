package schilling.richard.dexlib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import schilling.richard.dexlib.io.deserialize.HeaderItem;
import schilling.richard.dexlib.io.deserialize.OdexHeader;
import schilling.richard.util.FileUtils;

public enum StreamType {
	DEX_STREAM, ODEX_STREAM, ZIP_STREAM, BYTE_STREAM;

	public static final byte[] ZIPFILE_MAGIC = { 0x50, 0x4B };

	private byte[] magicValue;

	/**
	 * Returns the magic value that determined what type this object is. Types
	 * of BYTE_STREAM return null.
	 * 
	 * @return the magic value or null if this type is BYTE_STREAM.
	 */
	public byte[] getMagicValue() {
		return magicValue;
	}

	/**
	 * Sets the magic value. Returns this to support chain calls. This supports
	 * calls like 'return DEX_STREAM.setMagicValue().'
	 * 
	 * @param magic
	 *            the magic value to set.
	 * @return this object.
	 */
	private StreamType withMagicValue(byte[] magic) {
		magicValue = magic;
		return this;
	}

	/**
	 * If the first eight bytes of the stream contains the bytes found in
	 * ZIPFILE_MAGIC then ZIP_STREAM is returned otherwise DEX_STREAM is
	 * returned. The stream position is advanced by 8 bytes.
	 * 
	 * @param is
	 *            the input stream to read.
	 * @return ZIP_STREAM if the input stream is for a zip file or DEX_STREAM if
	 *         it's a dex stream of BYTE_STREAM otherwise.
	 * @throws IOException
	 *             if the stream cannot be read.
	 */
	public static StreamType determineType(InputStream is) throws IOException {

		byte[] magic = FileUtils.readStream(is, 8);

		if (magic[0] == ZIPFILE_MAGIC[0] && magic[1] == ZIPFILE_MAGIC[1])
			return ZIP_STREAM.withMagicValue(magic);

		// check for DEX stream
		for (int i = 0; i < HeaderItem.MAGIC_VALUES.length; i++) {

			if (Arrays.equals(magic, HeaderItem.MAGIC_VALUES[i]))
				return DEX_STREAM.withMagicValue(magic);
		}

		// check for ODEX stream
		if (Arrays.equals(magic, OdexHeader.MAGIC_35))
			return ODEX_STREAM.withMagicValue(magic);

		if (Arrays.equals(magic, OdexHeader.MAGIC_36))
			return ODEX_STREAM.withMagicValue(magic);

		// the only type of stream possible is an ordinary byte stream.
		return BYTE_STREAM;
	}

	/**
	 * Determines the type of InputStream that the specified file would
	 * generate.
	 * 
	 * @param file
	 *            the file to check.
	 * @return the type of stream that would be formed by the file.
	 * @throws IOException
	 *             if there is a problem opening the file.
	 */
	public static StreamType determineType(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		try {

			return determineType(is);

		} finally {
			is.close();
		}
	}

}
