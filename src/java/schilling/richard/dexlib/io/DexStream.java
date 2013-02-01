package schilling.richard.dexlib.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import schilling.richard.dexlib.io.deserialize.HeaderItem;
import schilling.richard.dexlib.io.deserialize.ItemType;
import schilling.richard.dexlib.io.deserialize.OdexHeader;
import schilling.richard.io.ByteArrayInput;
import schilling.richard.io.Input;
import schilling.richard.util.FileUtils;

/**
 * An input stream that can provide data for a DEX or ODEX stream. This makes it
 * easier to retrieve data about the underlying stream and performs checks like
 * ensuring that the header is proper.
 * 
 * 
 * 
 * @author rschilling
 * 
 */
public class DexStream extends FilterInputStream {

	private byte[] magic = null;
	private StreamType sType = null;
	private OdexHeader odexHeader = null;
	private OdexDependencies odexDependencies = null;
	private int odexDependencySkip = 0;
	private Input sectionInput;

	/**
	 * Creates a new stream. Reads the first 8 bytes of the stream to determine
	 * the stream type. The specified stream must support marking. The position
	 * of the cursor is advanced by 8 bytes after this constructor completes.
	 * Also, the header can be retrieved by calling getHeader().
	 * 
	 * @param in
	 *            the input stream to wrap.
	 * @throws NullPointerException
	 *             if the specified input stream is null.
	 * @throws IllegalArgumentException
	 *             if marks are not supported.
	 */
	public DexStream(InputStream in) {
		super(in);
		if (in == null)
			throw new IllegalArgumentException("the stream cannot be null");

		if (!in.markSupported())
			throw new IllegalArgumentException(
					"marks are not supported on this stream. This is requred.  Specify a stream wherne in.markSupported() returns true.");

	}

	/**
	 * Reads in the type of stream.
	 * 
	 * @throws IOException
	 * @throws IllegalArgumentException
	 *             if the stream type is a StreamType.BYTE_STREAM.
	 * @throws IllegalArgumentException
	 *             if an IOException occurs while reading the magic value.
	 */
	public StreamType getType() throws IOException {
		if (sType == null) {
			try {
				in.mark(8);
				sType = StreamType.determineType(this);
				in.reset();

				if (sType == StreamType.BYTE_STREAM)
					throw new IllegalArgumentException(
							"stream type must be StreamType DEX_STREAM, ODEX_STREAM, or ZIP_STREAM");
			} catch (IOException ex) {
				IOException ioe = new IOException(
						"unable to determine stream type");
				ioe.initCause(ex);
				throw ioe;
			}
		}
		return sType;

	}

	/**
	 * Reads the magic value. Calls getType();
	 * 
	 * @throws IOException
	 */
	public byte[] getMagic() throws IOException {
		getType();

		if (sType != StreamType.ODEX_STREAM)
			throw new IOException("stream must be an ODEX stream");

		if (magic == null) {
			try {
				magic = FileUtils.readStream(in, 8);
			} catch (IOException ex) {

				IOException ioe = new IOException(
						"Unable to read the magic value from the stream");
				ioe.initCause(ex);
				throw ioe;

			}

		}
		return magic;
	}

	/**
	 * returns the ODEX header. Calls getMagic();
	 * 
	 * @return
	 * @throws IOException
	 */
	public OdexHeader getOdexHeader() throws IOException {

		if (sType != StreamType.ODEX_STREAM)
			throw new IOException("This stream is a " + sType
					+ " stream, not an ODEX stream.");

		getMagic();
		if (odexHeader == null) {
			byte[] odexHeaderBytes = FileUtils.readStream(this, 40);
			Input odexHeaderIn = new ByteArrayInput(odexHeaderBytes);
			odexHeader = new OdexHeader(odexHeaderIn);

			odexDependencySkip = odexHeader.depsOffset - odexHeader.dexOffset
					- odexHeader.dexLength;

			if (odexDependencySkip < 0)
				throw new IOException(
						"Unexpected placement of the odex dependency data");

			if (odexHeader.dexOffset > 40)
				FileUtils.readStream(this, odexHeader.dexOffset - 40);

			createSectionInput();

		}
		return odexHeader;

	}

	/**
	 * Sets the odex dependencies. Calls getOdexHeader.
	 * 
	 * @return Also
	 * @throws IOException
	 */
	public OdexDependencies getOdexDependencies() throws IOException {

		if (sType != StreamType.ODEX_STREAM)
			throw new IOException("This stream is a " + sType
					+ " stream, not an ODEX stream.");

		getOdexHeader();
		if (odexDependencies == null) {

			if (odexDependencySkip > 0)
				FileUtils.readStream(this, odexDependencySkip);

			odexDependencies = new OdexDependencies(new ByteArrayInput(
					FileUtils.readStream(this, odexHeader.depsLength)));
		}

		return odexDependencies;

	}

	// TODO: Can we use annotations to give a build/debug tool metadata?
	private void createSectionInput() throws IOException {
		getType();
		if (sType == StreamType.ODEX_STREAM) {
			sectionInput = new ByteArrayInput(FileUtils.readStream(this,
					odexHeader.dexLength));
		} else if (sType == StreamType.DEX_STREAM) {
			byte[] completestream = FileUtils.readStreamComplete(this);
			sectionInput = new ByteArrayInput(completestream);
		} else {
			throw new IOException("stream must be either a DEX or ODEX stream.");
		}
	}

	/**
	 * Returns the section input.
	 * 
	 * @return
	 * @throws IOException
	 */
	public Input getSectionInput() throws IOException {
		if (sectionInput == null) {

			if (sType == StreamType.ODEX_STREAM)
				getOdexDependencies(); // causes createSectionInput to be
										// called.
			else
				createSectionInput();

		}
		return sectionInput;

	}

	/**
	 * Reads DEX data from the specified input stream. This function assumes
	 * that the current position of the stream is the start of a valid DEX file.
	 * The stream is not closed by this funcction. The caller must close the
	 * stream.
	 * 
	 * @param inputStream
	 *            the stream to read dex data from.
	 * @throws IOException
	 *             if the stream cannot be read or there is an issue with the
	 *             stream.
	 * @throws IllegalStateException
	 *             if the array of magic numbers to detect in the stream is not
	 *             properly defined.
	 */
	public void loadFromStream(DexFile dFile) throws IOException {

		// make sure there is no error in HeaderItem.MAGIC_VALUES
		// TODO: place this code into a unit test.
		if (HeaderItem.MAGIC_VALUES[0].length != 8)
			throw new IllegalStateException(
					"HeaderItem.MAGIC_VALUES is not properly defined.");

		Input in = getSectionInput();

		ReadContext readContext = new ReadContext();

		dFile.loadHeaderItem(in, readContext);
		int mapOffset = readContext.getSectionOffset(ItemType.TYPE_MAP_LIST);
		in.setCursor(mapOffset);

		dFile.loadMapItem(in, readContext); // load the mapped sections into
											// readContext.
		dFile.loadSections(in, readContext);

	}

}
