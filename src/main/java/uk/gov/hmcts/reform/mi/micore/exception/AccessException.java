package uk.gov.hmcts.reform.mi.micore.exception;

/**
 * Custom exception for generic errors thrown by MI Core Lib.
 */
public class AccessException extends RuntimeException {

    private static final long serialVersionUID = 1001L;

    public AccessException(Throwable cause) {
        super(cause);
    }

    public AccessException(String message) {
        super(new Throwable(message));
    }
}
