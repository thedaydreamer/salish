package schilling.richard.dexlib.proxy;

import org.gnu.salish.visitors.DexBufferVisitor;

import schilling.richard.dexlib.visitors.ReloadAllReferencedHashesListener;
import schilling.richard.dexlib.visitors.ReloadDexDefinedHasesListener;
import android.util.SparseArray;

import com.android.dx.io.dexbuffer.DexBuffer;

public class DexBufferHashes {
	/**
	 * method hashes from the merged file that contains proxies. Key - method
	 * hash, value = methodId
	 */
	private SparseArray<String> methodHashes = null;

	/**
	 * class hashes from the merged file that contains proxies. Key - class
	 * hash, value = classid
	 */
	private SparseArray<String> classHashes = null;

	public SparseArray<String> getMethodHashes() {

		if (methodHashes == null)
			throw new IllegalStateException("call loadFrom(DexBuffer) first.");

		return methodHashes;
	}

	public SparseArray<String> getClassHashes() {

		if (classHashes == null)
			throw new IllegalStateException("call loadFrom(DexBuffer) first.");

		return classHashes;
	}

	/**
	 * Loads method and class hashes for only those items defined within the DEX
	 * file itself.
	 * 
	 * @param buffer
	 *            the buffer to search
	 * @throws InterruptedException
	 *             if the thread was interrupted during processing.
	 */
	public void loadDefinedWithin(DexBuffer buffer) throws InterruptedException {

		DexBufferVisitor visitor = new DexBufferVisitor(buffer);
		ReloadDexDefinedHasesListener ulmListener = new ReloadDexDefinedHasesListener();
		visitor.registerListener(ulmListener);
		visitor.visitClasses();

		methodHashes = ulmListener.getMethodHash();
		classHashes = ulmListener.getClassHash();
	}

	/**
	 * Loads method and class hashes for all those referenced with in the DEX
	 * file.
	 * 
	 * @param buffer
	 * @throws InterruptedException
	 */
	public void loadAllReferenced(DexBuffer buffer) throws InterruptedException {

		DexBufferVisitor visitor = new DexBufferVisitor(buffer);
		ReloadAllReferencedHashesListener ulmListener = new ReloadAllReferencedHashesListener();
		visitor.registerListener(ulmListener);
		visitor.visitMethodIdentifiers();

		methodHashes = ulmListener.getMethodHash();
		classHashes = ulmListener.getClassHash();

	}

}
