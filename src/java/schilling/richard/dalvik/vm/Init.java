package schilling.richard.dalvik.vm;

import android.util.Log;

public class Init {
	public static final String LOG_TAG = "Init";

	/*
	 * Abort the VM. We get here on fatal errors. Try very hard not to use this;
	 * whenever possible, return an error to somebody responsible.
	 */
	public static void dvmAbort() {
		/*
		 * Leave gDvm.lastMessage on the stack frame which can be decoded in the
		 * tombstone file. This is for situations where we only have tombstone
		 * files but no logs (ie b/5372634).
		 * 
		 * For example, in the tombstone file you usually see this:
		 * 
		 * #00 pc 00050ef2 /system/lib/libdvm.so (dvmAbort) #01 pc 00077670
		 * /system/lib/libdvm.so (_Z15dvmClassStartupv) :
		 * 
		 * stack: : #00 beed2658 00000000 beed265c 7379732f beed2660 2f6d6574
		 * beed2664 6d617266 beed2668 726f7765 beed266c 6f632f6b beed2670
		 * 6a2e6572 beed2674 00007261 beed2678 00000000
		 * 
		 * The ascii values between beed265c and beed2674 belongs to
		 * messageBuffer and it can be decoded as "/system/framework/core.jar".
		 */

		StringBuilder messageBuffer = new StringBuilder();

		messageBuffer.append(DvmGlobals.gDvm.lastMessage);

		Log.w(LOG_TAG, "VM aborting");
		Log.e(LOG_TAG, messageBuffer.toString());

		/* JNI-supplied abort hook gets right of first refusal */
		if (DvmGlobals.gDvm.abortHook != null)
			DvmGlobals.gDvm.abortHook.run(); // run directly.

	}

}
