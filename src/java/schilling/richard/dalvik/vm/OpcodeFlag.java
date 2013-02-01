package schilling.richard.dalvik.vm;

public enum OpcodeFlag {

	CanBranch((byte) 1), // // conditional or unconditional branch
	CanContinue((byte) (1 << 1)), // flow can continue to next statement
	CanSwitch((byte) (1 << 2)), // switch statement
	CanThrow((byte) (1 << 3)), // could cause an exception to be thrown
	CanReturn((byte) (1 << 4)), // returns, no additional statements
	Invoke((byte) (1 << 5)); // a flavor of invoke

	public static final byte kInstrCanBranch = CanBranch.intValue(); // //
																		// conditional
																		// or
																		// unconditional
																		// branch
	public static final byte kInstrCanContinue = CanContinue.intValue(); // flow
																			// can
																			// continue
																			// to
																			// next
																			// statement
	public static final byte kInstrCanSwitch = CanSwitch.intValue(); // switch
																		// statement
	public static final byte kInstrCanThrow = CanThrow.intValue(); // could
																	// cause an
																	// exception
																	// to be
																	// thrown
	public static final byte kInstrCanReturn = CanReturn.intValue(); // returns,
																		// no
																		// additional
																		// statements
	public static final byte kInstrInvoke = Invoke.intValue(); // a flavor of
																// invoke

	private byte value = 0;

	private OpcodeFlag(byte val) {
		value = val;
	}

	public byte intValue() {
		return value;
	}

}
