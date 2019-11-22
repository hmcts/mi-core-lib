package uk.gov.hmcts.reform.mi.micore.exception;

public class AccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccessException(Throwable cause) {
        super(cause);
    }

    public AccessException(String message) {
        super(new Throwable(message));
    }
}
