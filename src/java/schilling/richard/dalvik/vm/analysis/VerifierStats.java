package schilling.richard.dalvik.vm.analysis;

/* some verifier counters, for debugging */
public class VerifierStats {

	int methodsExamined; /* number of methods examined */
	int monEnterMethods; /* number of methods with monitor-enter */
	int instrsExamined; /* incr on first visit of instruction */
	int instrsReexamined; /* incr on each repeat visit of instruction */
	int copyRegCount; /* calls from updateRegisters->copyRegisters */
	int mergeRegCount; /* calls from updateRegisters->merge */
	int mergeRegChanged; /* calls from updateRegisters->merge, changed */
	int uninitSearches; /* times we've had to search the uninit table */
	int biggestAlloc; /* largest RegisterLine table alloc */

}
