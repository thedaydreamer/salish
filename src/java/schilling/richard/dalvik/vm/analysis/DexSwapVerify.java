package schilling.richard.dalvik.vm.analysis;

import java.util.List;

import schilling.richard.dalvik.vm.oo.VerifyException;

import com.android.dx.io.ClassData;
import com.android.dx.io.Code;
import com.android.dx.io.Code.CatchHandler;
import com.android.dx.io.Code.Try;
import com.android.dx.io.dexbuffer.DexBuffer;

public class DexSwapVerify {

	public static final String LOG_TAG = "Finnr.DexSwapVerify";

	/**
	 * swaps the lowest two bytes of the value.
	 * 
	 * @param value
	 *            the value to swap.
	 * @return the lowest two bytes swapped.
	 * 
	 */
	// heep these endian fucntions around - they are handy.
	public static int endianSwapU2(int value) {
		int v = value & 0xFFFF; // isolate the lower end short.
		return (v >> 8) | (v << 8);
	}

	public static int endianSwapU4(int value) {
		/* ABCD --> CDAB --> DCBA */
		value = (value >> 16) | (value << 16);
		return ((value & 0xff00ff00) >> 8) | ((value << 8) & 0xff00ff00);
	}

	public static long endianSwapU8(long value) {
		/* ABCDEFGH --> EFGHABCD --> GHEFCDAB --> HGFEDCBA */
		value = (value >> 32) | (value << 32);
		value = ((value & 0xffff0000ffff0000L) >> 16)
				| ((value << 16) & 0xffff0000ffff0000L);
		return ((value & 0xff00ff00ff00ff00L) >> 8)
				| ((value << 8) & 0xff00ff00ff00ff00L);
	}

	public static void swapTriesAndCatches(ClassData.Method method, Code code,
			List<Integer> types) throws VerifyException {
		int codeOffset = method.getCodeOffset();
		if (codeOffset <= 0)
			throw new IllegalArgumentException("method has no code.");

		int insnsSize = code.getInstructions().length;
		Try[] tries = code.getTries();
		CatchHandler[] encodedHandlers = code.getCatchHandlers(); // dexGetCatchHandlerData(code);
																	// // TODO
																	// use a new
																	// name.
		int codeEnd = codeOffset + code.getInsSize() - 1; // last byte of the
															// code block.

		int handlersSize = encodedHandlers.length;
		boolean okay = handlersSize >= 0 ? true : false;

		if (!okay)
			throw new IllegalStateException("Bogus handlers_size");

		if (handlersSize >= 65536)
			throw new IllegalStateException(String.format(
					"Invalid handlers_size: %d", handlersSize));

		/*
		 * make sure that the length, indexes and addresses of the catch
		 * handlers are valid.
		 */
		// setHandlerOffsAndVerify begin
		for (CatchHandler handler : encodedHandlers) {
			int[] typeIndexes = handler.getTypeIndexes();
			int[] addresses = handler.getAddresses(); // 0 based address offsets
														// from codeOffset.

			// FIXME : typeIndexes can have zero length ??? The exception is
			// tripped because typeIndexes.length == 0.

			// reading the indexes has already adjusted for the negative count
			// on tries found in the file.
			if (typeIndexes.length > 65536)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"Invalid size: %d", typeIndexes.length));

			// make sure all type indexes are valid.
			for (int index : typeIndexes) {
				if (index >= types.size() || index < 0)
					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC,
							String.format("Bogus type_idx"));
			}

			// make sure the the catch handler address is somewhere within the
			// code.
			// this block also handles the catch all handler as well.
			for (int addr : addresses) {

				if (addr < 0 || addr >= insnsSize) {

					// the original C code only has a check for > codeEnd. I've
					// added < code Start
					throw new VerifyException(
							VerifyErrorCause.VERIFY_ERROR_GENERIC,
							String.format("Invalid addr: %#x", addr));
				}

			}

		}
		// setHandlerOffsAndVerify end

		/*
		 * In the original C code, the endianess is not handled when the data is
		 * read from a disk like DexBuffer does. So, the bytes must be swapped
		 * in the C program ((endian) swap + verify). But, because we handle
		 * this condition in DexBuffer we can just compare numbers as they are.
		 */
		int lastEnd = 0;
		for (Try tryBlock : tries) {
			int handlerIndex = 0;
			try {
				handlerIndex = DexSwapVerify.getMatchingHandler(tryBlock,
						encodedHandlers);
			} catch (IllegalArgumentException ex) {
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"Bogus handler offset: %#x",
								tryBlock.getHandlerOffset()), ex);
			}

			int startAddr = tryBlock.getStartAddress();

			// CHECK_LIST_SIZE(tries, count, sizeof(DexTry));

			// start address needs to be after the latest.
			if (startAddr < lastEnd)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC,
						String.format(
								"Out-of-order try (startAddr %#x, lastEnd %#x)",
								startAddr, lastEnd));

			// start address needs to appear after the end of the code.
			if (startAddr >= insnsSize)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"Invalid start_addr: %#x", startAddr));

			lastEnd = startAddr + tryBlock.getInstructionCount();

			if (lastEnd > insnsSize)
				throw new VerifyException(
						VerifyErrorCause.VERIFY_ERROR_GENERIC, String.format(
								"Invalid insn_count: %#x (end addr %#x)",
								tryBlock.getInstructionCount(), lastEnd));

		}

	}

	/**
	 * Helper for swapCodeItem(), which does all the try-catch related swapping
	 * and verification.
	 * 
	 * <p>
	 * makes sure the actual try and catch code sections are properly formed.
	 * 
	 * @param buf
	 *            the buffer to check.
	 * @param start
	 *            the offset of the code item to check.
	 * @throws VerifyException
	 */
	public static void swapTriesAndCatches(DexBuffer buf,
			ClassData.Method method) throws VerifyException {
		List<Integer> types = buf.typeIds();
		Code code = buf.readCode(method);
		swapTriesAndCatches(method, code, types);

	}

	/*
	 * Make sure a list of items fits entirely within the file.
	 * 
	 * Assumes "const CheckState* state" and
	 * "typeof(_count) == typeof(_elemSize)" If the type sizes or signs are
	 * mismatched, this will return 0.
	 */
	/*
	 * TODO implement this check when writing DEX files. #define
	 * CHECK_LIST_SIZE(_ptr, _count, _elemSize) { \ const u1* _start = (const
	 * u1*) (_ptr); \ const u1* _end = _start + ((_count) * (_elemSize)); \ if
	 * (!safe_mul(NULL, (_count), (_elemSize)) || \ !checkPtrRange(state,
	 * _start, _end, #_ptr)) { \ return 0; \ } \ }
	 */

	/**
	 * Finds a try block's CatchHandler object from the specified CatchHandler
	 * array;
	 * 
	 * @param tryBlock
	 *            the try block to search for.
	 * @param handlers
	 *            the list of handlers to search in.
	 * @return the index in the array with Try block's catch handler.
	 * @throws IllegalArgumentException
	 *             if no catch handler was found in the handler list.
	 */
	public static int getMatchingHandler(Try tryBlock, CatchHandler[] handlers)
			throws IllegalArgumentException {

		if (tryBlock == null)
			throw new IllegalArgumentException("tryBlock cannot be null.");

		if (handlers == null)
			throw new IllegalArgumentException("handlers array cannot be null.");

		int tryHandlerOffset = tryBlock.getHandlerOffset();
		
		for (int i = 0; i < handlers.length; i++) {

			if (tryHandlerOffset == handlers[i].getHandlerOffset())
				return i;

		}

		throw new IllegalArgumentException("no catch handler found.");
	}

}
