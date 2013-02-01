package schilling.richard.dexlib.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import schilling.richard.dexlib.io.DexFile.NoClassesDexException;
import schilling.richard.util.FileUtils;

import com.android.dx.io.dexbuffer.DexBuffer;

public final class ApkFile {

	/**
	 * The file that this object operates on. The wrapped file.
	 */
	private File file;

	/**
	 * Wraps a File object as an APK file.
	 * 
	 * @param file
	 *            the file to wrap.
	 */
	public ApkFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public String getName() {
		return file.getName();
	}

	/**
	 * Extracts the .dex file from the APK and saves it to the specified
	 * destination.
	 * 
	 * @param dest
	 *            the location to save the .dex file to.
	 * @throws IOException
	 * @throws ZipException
	 * @returns the size of the clases.dex file in bytes.
	 */
	public int extractDexToFile(File dest) throws ZipException, IOException {

		ZipFile zipFile = new ZipFile(file);
		byte[] inputBuffer = new byte[FileUtils.INPUT_BUFFER_LENGTH];

		BufferedInputStream bis = getDexInputStream(zipFile);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(dest));

		/*
		 * write the dex entry to its own file.
		 */
		int input = 0;
		int writeSz = 0;
		while ((input = bis.read(inputBuffer)) != -1) {

			bos.write(inputBuffer, 0, input);
			writeSz += input;
		}
		bis.close();
		bos.close();
		zipFile.close();

		return writeSz;

	}

	/**
	 * Open an input stream on the classes.dex file with a ZIP/APK file.
	 * 
	 * @return the buffered stream (opened) for classes.dex
	 * @throws ZipException
	 *             if classes.dex was not found.
	 * @throws IOException
	 *             if the input stream could not be created.
	 */
	public BufferedInputStream getDexInputStream(ZipFile zipFile)
			throws ZipException, IOException {

		ZipEntry zipEntry = zipFile.getEntry("classes.dex");
		if (zipEntry == null)
			throw new NoClassesDexException(String.format(
					"apk file %s does not contain a classes.dex file",
					file.getCanonicalPath()));

		return new BufferedInputStream(zipFile.getInputStream(zipEntry));

	}

	private void throwIfInterrupted() throws InterruptedException {

		if (Thread.interrupted())
			throw new InterruptedException("processing cancelled");

	}

	/**
	 * Returns the entire contents of classes.dex in the APK file in the form of
	 * a byte array.
	 * 
	 * @return the bytes of classes.dex
	 * @throws ZipException
	 *             if classes.dex was not found.
	 * @throws IOException
	 *             if the input stream could not be created.
	 * @throws InterruptedException
	 */
	public byte[] extractDexBytes() throws ZipException, IOException,
			InterruptedException {
		ZipFile zipFile = new ZipFile(file);
		byte[] inputBuffer = new byte[FileUtils.INPUT_BUFFER_LENGTH];

		BufferedInputStream bis = getDexInputStream(zipFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		/*
		 * write the dex entry to its own file.
		 */
		int input = 0;
		while ((input = bis.read(inputBuffer)) != -1) {
			baos.write(inputBuffer, 0, input);
			throwIfInterrupted();
		}

		bis.close();
		baos.close();
		zipFile.close();

		return baos.toByteArray();

	}

	/**
	 * Returns the contents of classes.dex as a DexBuffer.
	 * 
	 * @return classes.dex in the form of a DexBuffer.
	 * @throws ZipException
	 *             if classes.dex was not found.
	 * @throws IOException
	 *             if the input stream could not be created.
	 * @throws InterruptedException 
	 */
	public DexBuffer extractDexBuffer() throws ZipException, IOException, InterruptedException {
		return new DexBuffer(extractDexBytes());
	}
}
