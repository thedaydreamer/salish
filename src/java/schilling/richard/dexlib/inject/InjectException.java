
package schilling.richard.dexlib.inject;

public class InjectException extends Exception {

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
