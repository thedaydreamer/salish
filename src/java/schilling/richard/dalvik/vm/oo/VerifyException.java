package schilling.richard.dalvik.vm.oo;

import schilling.richard.dalvik.vm.analysis.VerifyErrorCause;

public class VerifyException extends Exception {

	private VerifyErrorCause error = null;

	public VerifyException(VerifyErrorCause error) {
		super();
		this.error = error;
	}

	public VerifyException(VerifyErrorCause error, String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
		this.error = error;
	}

	public VerifyException(VerifyErrorCause error, String detailMessage) {
		super(detailMessage);
		this.error = error;
	}

	public VerifyException(VerifyErrorCause error, Throwable throwable) {
		super(throwable);
		this.error = error;
	}

	public VerifyErrorCause getError() {
		return error;
	}

}
