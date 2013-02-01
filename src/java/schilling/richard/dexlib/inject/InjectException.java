package schilling.richard.dexlib.inject;

import schilling.richard.r3.app.port.FinnrException;

public class InjectException extends FinnrException {

	public InjectException() {
		super();
	}

	public InjectException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InjectException(String detailMessage) {
		super(detailMessage);

	}

	public InjectException(Throwable throwable) {
		super(throwable);

	}

}
