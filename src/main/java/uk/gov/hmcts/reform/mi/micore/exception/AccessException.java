package uk.gov.hmcts.reform.mi.micore.exception;

public class AccessException extends RuntimeException {

    public AccessException(Throwable cause) {
        super(cause);
    }

    public AccessException(String message) {
        super(new Throwable(message));
    }
}
