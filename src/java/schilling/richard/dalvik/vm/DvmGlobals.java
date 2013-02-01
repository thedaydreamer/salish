package schilling.richard.dalvik.vm;

import java.util.Hashtable;
import java.util.Timer;
import java.util.Vector;

import schilling.richard.dalvik.vm.analysis.DexClassVerifyMode;
import schilling.richard.dalvik.vm.analysis.DexOptimizerMode;
import schilling.richard.dalvik.vm.analysis.RegisterMapMode;
import schilling.richard.dalvik.vm.analysis.VerifierStats;
import schilling.richard.dalvik.vm.interp.ExecutionMode;
import schilling.richard.dalvik.vm.oo.PlatformClass;

import com.android.dx.io.ClassData;

/**
 * Virtual machine global state which is used during runtine of a program and
 * dueing code analysis.
 * 
 * @author rschilling
 * 
 */
public class DvmGlobals {
	public static final boolean WITH_JIT = true;

	// TODO consolidate this definition.
	public static final String JAVA_LANG_OBJECT_SIGNATURE = "Ljava/lang/Object;";
	public static final String JAVA_LANG_OBJECT = "java.lang.Object";

	public boolean generateRegisterMaps;

	/**
	 * Global state. Defined this way to retain name compatability with the
	 * original code.
	 */
	public static final DvmGlobals gDvm = new DvmGlobals();
	/*
	 * Some options from the command line or environment.
	 */
	String bootClassPathStr;
	String classPathStr;

	int heapStartingSize;
	int heapMaximumSize;
	int heapGrowthLimit;
	int stackSize;

	boolean verboseGc;
	boolean verboseJni;
	boolean verboseClass;
	boolean verboseShutdown;

	boolean jdwpAllowed; // debugging allowed for this process?
	boolean jdwpConfigured; // has debugging info been provided?
	// FIXME implement JdwpTransportType jdwpTransport;
	boolean jdwpServer;
	String jdwpHost;
	int jdwpPort;
	boolean jdwpSuspend;

	ProfilerClockSource profilerClockSource;

	/*
	 * Lock profiling threshold value in milliseconds. Acquires that exceed
	 * threshold are logged. Acquires within the threshold are logged with a
	 * probability of $\frac{time}{threshold}$ . If the threshold is unset no
	 * additional logging occurs.
	 */
	long lockProfThreshold;

	// int (*vfprintfHook)(FILE*, const char*, va_list);
	// void (*exitHook)(int);
	// void (*abortHook)(void);
	// sbool (*isSensitiveThreadHook)(void);

	public Runnable abortHook;
	public Runnable vfprintfHook;
	public Runnable isSensitiveThreadHook;
	public Runnable exitHook;

	public Timer hookTimer = new Timer();

	int jniGrefLimit; // 0 means no limit
	String jniTrace;
	boolean reduceSignals;
	boolean noQuitHandler;
	boolean verifyDexChecksum;
	String stackTraceFile; // for SIGQUIT-inspired output

	boolean logStdio;

	DexOptimizerMode dexOptMode;
	DexClassVerifyMode classVerifyMode;

	public RegisterMapMode registerMapMode;

	public boolean monitorVerification;

	boolean dexOptForSmp;

	/*
	 * GC option flags.
	 */
	boolean preciseGc;
	boolean preVerify;
	boolean postVerify;
	boolean concurrentMarkSweep;
	boolean verifyCardTable;
	boolean disableExplicitGc;

	int assertionCtrlCount;
	AssertionControl assertionCtrl;

	ExecutionMode executionMode;

	/*
	 * VM init management.
	 */
	public boolean initializing;
	public boolean optimizing;

	/* used by the DEX optimizer to load classes from an unfinished DEX */
	DvmDex bootClassPathOptExtra;
	boolean optimizingBootstrapClass;

	/*
	 * Value for the next class serial number to be assigned. This is
	 * incremented as we load classes. Failed loads and races may result in some
	 * numbers being skipped, and the serial number is not guaranteed to start
	 * at 1, so the current value should not be used as a count of loaded
	 * classes.
	 */
	volatile int classSerialNumber;

	/*
	 * Interned strings.
	 */

	/* A mutex that guards access to the interned string tables. */
	static Object internLock = new Object();

	/* Hash table of strings interned by the user. */
	Hashtable internedStrings;

	/* Hash table of strings interned by the class loader. */
	Hashtable literalStrings;

	/*
	 * Classes constructed directly by the vm.
	 */

	/* the class Class */
	PlatformClass classJavaLangClass;

	/* synthetic classes representing primitive types */
	PlatformClass typeVoid;
	PlatformClass typeBoolean;
	PlatformClass typeByte;
	PlatformClass typeShort;
	PlatformClass typeChar;
	PlatformClass typeInt;
	PlatformClass typeLong;
	PlatformClass typeFloat;
	PlatformClass typeDouble;

	/* synthetic classes for arrays of primitives */
	PlatformClass classArrayBoolean;
	PlatformClass classArrayByte;
	PlatformClass classArrayShort;
	PlatformClass classArrayChar;
	PlatformClass classArrayInt;
	PlatformClass classArrayLong;
	PlatformClass classArrayFloat;
	PlatformClass classArrayDouble;

	/*
	 * Quick lookups for popular classes used internally.
	 */
	PlatformClass classJavaLangClassArray;
	PlatformClass classJavaLangClassLoader;
	PlatformClass classJavaLangObject;
	PlatformClass classJavaLangObjectArray;
	PlatformClass classJavaLangString;
	PlatformClass classJavaLangThread;
	PlatformClass classJavaLangVMThread;
	PlatformClass classJavaLangThreadGroup;
	PlatformClass classJavaLangStackTraceElement;
	PlatformClass classJavaLangStackTraceElementArray;
	PlatformClass classJavaLangAnnotationAnnotationArray;
	PlatformClass classJavaLangAnnotationAnnotationArrayArray;
	PlatformClass classJavaLangReflectAccessibleObject;
	PlatformClass classJavaLangReflectConstructor;
	PlatformClass classJavaLangReflectConstructorArray;
	PlatformClass classJavaLangReflectField;
	PlatformClass classJavaLangReflectFieldArray;
	PlatformClass classJavaLangReflectMethod;
	PlatformClass classJavaLangReflectMethodArray;
	PlatformClass classJavaLangReflectProxy;
	PlatformClass classJavaNioReadWriteDirectByteBuffer;
	PlatformClass classOrgApacheHarmonyLangAnnotationAnnotationFactory;
	PlatformClass classOrgApacheHarmonyLangAnnotationAnnotationMember;
	PlatformClass classOrgApacheHarmonyLangAnnotationAnnotationMemberArray;
	PlatformClass classOrgApacheHarmonyDalvikDdmcChunk;
	PlatformClass classOrgApacheHarmonyDalvikDdmcDdmServer;
	PlatformClass classJavaLangRefFinalizerReference;

	/*
	 * classes representing exception types. The names here don't include
	 * packages, just to keep the use sites a bit less verbose. All are in
	 * java.lang, except where noted.
	 */
	PlatformClass exAbstractMethodError;
	PlatformClass exArithmeticException;
	PlatformClass exArrayIndexOutOfBoundsException;
	PlatformClass exArrayStoreException;
	PlatformClass exClassCastException;
	PlatformClass exClassCircularityError;
	PlatformClass exClassFormatError;
	PlatformClass exClassNotFoundException;
	PlatformClass exError;
	PlatformClass exExceptionInInitializerError;
	PlatformClass exFileNotFoundException; /* in java.io */
	PlatformClass exIOException; /* in java.io */
	PlatformClass exIllegalAccessError;
	PlatformClass exIllegalAccessException;
	PlatformClass exIllegalArgumentException;
	PlatformClass exIllegalMonitorStateException;
	PlatformClass exIllegalStateException;
	PlatformClass exIllegalThreadStateException;
	PlatformClass exIncompatibleClassChangeError;
	PlatformClass exInstantiationError;
	PlatformClass exInstantiationException;
	PlatformClass exInternalError;
	PlatformClass exInterruptedException;
	PlatformClass exLinkageError;
	PlatformClass exNegativeArraySizeException;
	PlatformClass exNoClassDefFoundError;
	PlatformClass exNoSuchFieldError;
	PlatformClass exNoSuchFieldException;
	PlatformClass exNoSuchMethodError;
	PlatformClass exNullPointerException;
	PlatformClass exOutOfMemoryError;
	PlatformClass exRuntimeException;
	PlatformClass exStackOverflowError;
	PlatformClass exStaleDexCacheError; /* in dalvik.system */
	PlatformClass exStringIndexOutOfBoundsException;
	PlatformClass exThrowable;
	PlatformClass exTypeNotPresentException;
	PlatformClass exUnsatisfiedLinkError;
	PlatformClass exUnsupportedOperationException;
	PlatformClass exVerifyError;
	PlatformClass exVirtualMachineError;

	/* method offsets - Object */
	int voffJavaLangObject_equals;
	int voffJavaLangObject_hashCode;
	int voffJavaLangObject_toString;

	/* field offsets - String */
	int offJavaLangString_value;
	int offJavaLangString_count;
	int offJavaLangString_offset;
	int offJavaLangString_hashCode;

	/* field offsets - Thread */
	int offJavaLangThread_vmThread;
	int offJavaLangThread_group;
	int offJavaLangThread_daemon;
	int offJavaLangThread_name;
	int offJavaLangThread_priority;
	int offJavaLangThread_uncaughtHandler;
	int offJavaLangThread_contextClassLoader;

	/* method offsets - Thread */
	int voffJavaLangThread_run;

	/* field offsets - ThreadGroup */
	int offJavaLangThreadGroup_name;
	int offJavaLangThreadGroup_parent;

	/* field offsets - VMThread */
	int offJavaLangVMThread_thread;
	int offJavaLangVMThread_vmData;

	/* method offsets - ThreadGroup */
	int voffJavaLangThreadGroup_removeThread;

	/* field offsets - Throwable */
	int offJavaLangThrowable_stackState;
	int offJavaLangThrowable_cause;

	/* method offsets - ClassLoader */
	int voffJavaLangClassLoader_loadClass;

	/* direct method pointers - ClassLoader */
	ClassData.Method methJavaLangClassLoader_getSystemClassLoader;

	/* field offsets - java.lang.reflect.* */
	int offJavaLangReflectConstructor_slot;
	int offJavaLangReflectConstructor_declClass;
	int offJavaLangReflectField_slot;
	int offJavaLangReflectField_declClass;
	int offJavaLangReflectMethod_slot;
	int offJavaLangReflectMethod_declClass;

	/* field offsets - java.lang.ref.Reference */
	int offJavaLangRefReference_referent;
	int offJavaLangRefReference_queue;
	int offJavaLangRefReference_queueNext;
	int offJavaLangRefReference_pendingNext;

	/* field offsets - java.lang.ref.FinalizerReference */
	int offJavaLangRefFinalizerReference_zombie;

	/* method pointers - java.lang.ref.ReferenceQueue */
	ClassData.Method methJavaLangRefReferenceQueueAdd;

	/* method pointers - java.lang.ref.FinalizerReference */
	ClassData.Method methJavaLangRefFinalizerReferenceAdd;

	/* constructor method pointers; no vtable involved, so use Method */
	ClassData.Method methJavaLangStackTraceElement_init;
	ClassData.Method methJavaLangReflectConstructor_init;
	ClassData.Method methJavaLangReflectField_init;
	ClassData.Method methJavaLangReflectMethod_init;
	ClassData.Method methOrgApacheHarmonyLangAnnotationAnnotationMember_init;

	/* static method pointers - android.lang.annotation.* */
	ClassData.Method methOrgApacheHarmonyLangAnnotationAnnotationFactory_createAnnotation;

	/* direct method pointers - java.lang.reflect.Proxy */
	ClassData.Method methJavaLangReflectProxy_constructorPrototype;

	/* field offsets - java.lang.reflect.Proxy */
	int offJavaLangReflectProxy_h;

	/* field offsets - java.io.FileDescriptor */
	int offJavaIoFileDescriptor_descriptor;

	/* direct method pointers - dalvik.system.NativeStart */
	ClassData.Method methDalvikSystemNativeStart_main;
	ClassData.Method methDalvikSystemNativeStart_run;

	/* assorted direct buffer helpers */
	ClassData.Method methJavaNioReadWriteDirectByteBuffer_init;
	int offJavaNioBuffer_capacity;
	int offJavaNioBuffer_effectiveDirectAddress;

	/* direct method pointers - org.apache.harmony.dalvik.ddmc.DdmServer */
	ClassData.Method methDalvikDdmcServer_dispatch;
	ClassData.Method methDalvikDdmcServer_broadcast;

	/* field offsets - org.apache.harmony.dalvik.ddmc.Chunk */
	int offDalvikDdmcChunk_type;
	int offDalvikDdmcChunk_data;
	int offDalvikDdmcChunk_offset;
	int offDalvikDdmcChunk_length;

	/*
	 * Thread list. This always has at least one element in it (main), and main
	 * is always the first entry.
	 * 
	 * The threadListLock is used for several things, including the thread start
	 * condition variable. Generally speaking, you must hold the threadListLock
	 * when: - adding/removing items from the list - waiting on or signaling
	 * threadStartCond - examining the Thread struct for another thread (this is
	 * to avoid one thread freeing the Thread struct while another thread is
	 * perusing it)
	 */
	Vector<Thread> threadList;
	static final Object threadListLock = new Object();

	/**
	 * In the original code it looks like pthreads are used and threadStartCond
	 * is used to store thread level data.
	 */
	ThreadLocal threadStartCond; // pthread_cond_t threadStartCond;

	/*
	 * The thread code grabs this before suspending all threads. There are a few
	 * things that can cause a "suspend all": (1) the GC is starting; (2) the
	 * debugger has sent a "suspend all" request; (3) a thread has hit a
	 * breakpoint or exception that the debugger has marked as a "suspend all"
	 * event; (4) the SignalCatcher caught a signal that requires suspension.
	 * (5) (if implemented) the JIT needs to perform a heavyweight rearrangement
	 * of the translation cache or JitTable.
	 * 
	 * Because we use "safe point" self-suspension, it is never safe to do a
	 * blocking "lock" call on this mutex -- if it has been acquired, somebody
	 * is probably trying to put you to sleep. The leading '_' is intended as a
	 * reminder that this lock is special.
	 */
	static final Object _threadSuspendLock = new Object();

	/*
	 * Guards Thread->suspendCount for all threads, and provides the lock for
	 * the condition variable that all suspended threads sleep on
	 * (threadSuspendCountCond).
	 * 
	 * This has to be separate from threadListLock because of the way threads
	 * put themselves to sleep.
	 */
	static final Object threadSuspendCountLock = new Object();

	/*
	 * Suspended threads sleep on this. They should sleep on the condition
	 * variable until their "suspend count" is zero.
	 * 
	 * Paired with "threadSuspendCountLock".
	 */
	static final Object threadSuspendCountCond = new Object();

	/*
	 * Sum of all threads' suspendCount fields. Guarded by
	 * threadSuspendCountLock.
	 */
	int sumThreadSuspendCount;

	/*
	 * MUTEX ORDERING: when locking multiple mutexes, always grab them in this
	 * order to avoid deadlock:
	 * 
	 * (1) _threadSuspendLock (use lockThreadSuspend()) (2) threadListLock (use
	 * dvmLockThreadList()) (3) threadSuspendCountLock (use
	 * lockThreadSuspendCount())
	 */

	/*
	 * Thread ID bitmap. We want threads to have small integer IDs so we can use
	 * them in "thin locks".
	 */
	Vector<BitVector> threadIdMap = new Vector<BitVector>();

	/*
	 * Manage exit conditions. The VM exits when all non-daemon threads have
	 * exited. If the main thread returns early, we need to sleep on a condition
	 * variable.
	 */
	int nonDaemonThreadCount; /* must hold threadListLock to access */
	ThreadLocal vmExitCond;

	/*
	 * The set of DEX files loaded by custom class loaders.
	 */
	Hashtable userDexFiles;

	/*
	 * JNI global reference table.
	 */
	IndirectRefTable jniGlobalRefTable;
	IndirectRefTable jniWeakGlobalRefTable;
	static final Object jniGlobalRefLock = new Object();
	static final Object jniWeakGlobalRefLock = new Object();
	int jniGlobalRefHiMark;
	int jniGlobalRefLoMark;

	/*
	 * JNI pinned object table (used for primitive arrays).
	 */
	// ReferenceTable jniPinRefTable;
	static final Object jniPinRefLock = new Object();

	/*
	 * Native shared library table.
	 */
	Hashtable nativeLibs;

	/*
	 * GC heap lock. Functions like gcMalloc() acquire this before making any
	 * changes to the heap. It is held throughout garbage collection.
	 */
	static final Object gcHeapLock = new Object();

	/*
	 * Condition variable to queue threads waiting to retry an allocation.
	 * Signaled after a concurrent GC is completed.
	 */
	ThreadLocal gcHeapCond;

	/* Opaque pointer representing the heap. */
	// FIXME implement GcHeap* gcHeap;

	/* The card table base, modified as needed for marking cards. */
	// FIXME implement u1* biasedCardTableBase;

	/*
	 * Pre-allocated throwables.
	 */
	Object outOfMemoryObj;
	Object internalErrorObj;
	Object noClassDefFoundErrorObj;

	/* Monitor list, so we can free them */
	// FIXME implement /*volatile*/ Monitor monitorList;

	/* Monitor for Thread.sleep() implementation */
	// FIXME implement Monitor threadSleepMon;

	/* set when we create a second heap inside the zygote */
	boolean newZygoteHeapAllocated;

	/*
	 * TLS keys.
	 */
	// FIXME implement pthread_key_t pthreadKeySelf; /* Thread*, for
	// dvmThreadSelf */

	/*
	 * Cache results of "A instanceof B".
	 */
	// FIXME implement AtomicCache* instanceofCache;

	/* inline substitution table, used during optimization */
	// FIXME implement InlineSub* inlineSubs;

	/*
	 * Bootstrap class loader linear allocator.
	 */
	// FIXME implement LinearAllocHdr* pBootLoaderAlloc;

	/*
	 * Compute some stats on loaded classes.
	 */
	int numLoadedClasses;
	int numDeclaredMethods;
	int numDeclaredInstFields;
	int numDeclaredStaticFields;

	/* when using a native debugger, set this to suppress watchdog timers */
	boolean nativeDebuggerActive;

	/*
	 * JDWP debugger support.
	 * 
	 * Note: Each thread will normally determine whether the debugger is active
	 * for it by referring to its subMode flags. "debuggerActive" here should be
	 * seen as "debugger is making requests of 1 or more threads".
	 */
	boolean debuggerConnected; /* debugger or DDMS is connected */
	boolean debuggerActive; /* debugger is making requests */
	// FIXME implement JdwpState* jdwpState;

	/*
	 * Registry of objects known to the debugger.
	 */
	// FIXME implement HashTable* dbgRegistry;

	/*
	 * Debugger breakpoint table.
	 */
	// FIXME implement BreakpointSet* breakpointSet;

	/*
	 * Single-step control struct. We currently only allow one thread to be
	 * single-stepping at a time, which is all that really makes sense, but it's
	 * possible we may need to expand this to be per-thread.
	 */
	// FIXME implement StepControl stepControl;

	/*
	 * DDM features embedded in the VM.
	 */
	boolean ddmThreadNotification;

	/*
	 * Zygote (partially-started process) support
	 */
	boolean zygote;

	/*
	 * Used for tracking allocations that we report to DDMS. When the feature is
	 * enabled (through a DDMS request) the "allocRecords" pointer becomes
	 * non-NULL.
	 */
	static final Object allocTrackerLock = new Object();
	// FIXME implement AllocRecord* allocRecords;
	int allocRecordHead; /* most-recently-added entry */
	int allocRecordCount; /* #of valid entries */

	/*
	 * When a profiler is enabled, this is incremented. Distinct profilers
	 * include "dmtrace" method tracing, emulator method tracing, and possibly
	 * instruction counting.
	 * 
	 * The purpose of this is to have a single value that shows whether any
	 * profiling is going on. Individual thread will normally check their
	 * thread-private subMode flags to take any profiling action.
	 */
	volatile int activeProfilers;

	/*
	 * State for method-trace profiling.
	 */
	// FIXME implement MethodTraceState methodTrace;
	ClassData.Method methodTraceGcMethod;
	ClassData.Method methodTraceClassPrepMethod;

	/*
	 * State for emulator tracing.
	 */
	// FIXME implement void* emulatorTracePage;
	int emulatorTraceEnableCount;

	/*
	 * Global state for memory allocation profiling.
	 */
	// FIXME implement AllocProfState allocProf;

	/*
	 * Pointers to the original methods for things that have been inlined. This
	 * makes it easy for us to output method entry/exit records for the method
	 * calls we're not actually making. (Used by method profiling.)
	 */
	ClassData.Method inlinedMethods;

	/*
	 * Dalvik instruction counts (kNumPackedOpcodes entries).
	 */
	// FIXME implement int* executedInstrCounts;
	int instructionCountEnableCount;

	/*
	 * Signal catcher thread (for SIGQUIT).
	 */
	Thread signalCatcherHandle;
	boolean haltSignalCatcher;

	/*
	 * Stdout/stderr conversion thread.
	 */
	boolean haltStdioConverter;
	boolean stdioConverterReady;
	Thread stdioConverterHandle;
	static final Object stdioConverterLock = new Object();
	ThreadLocal stdioConverterCond;
	int[] stdoutPipe = new int[2];
	int[] stderrPipe = new int[2];

	/*
	 * pid of the system_server process. We track it so that when system server
	 * crashes the Zygote process will be killed and restarted.
	 */
	int systemServerPid;

	int kernelGroupScheduling;

	public static final boolean COUNT_PRECISE_METHODS = false;
	// #define COUNT_PRECISE_METHODS
	// #ifdef COUNT_PRECISE_METHODS
	// FIXME implement PointerSet* preciseMethods;
	// #endif

	/* some RegisterMap statistics, useful during development */
	// FIXME implement void* registerMapStats;

	public static final boolean VERIFIER_STATS = true;
	// FIXME implement #ifdef VERIFIER_STATS
	public VerifierStats verifierStats;
	// FIXME implement #endif

	/* String pointed here will be deposited on the stack frame of dvmAbort */
	String lastMessage;

}
