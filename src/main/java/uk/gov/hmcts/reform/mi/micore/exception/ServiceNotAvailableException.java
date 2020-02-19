package uk.gov.hmcts.reform.mi.micore.exception;

public class ServiceNotAvailableException extends Exception {
    private static final long serialVersionUID = 100010L;

    public ServiceNotAvailableException(String message) {
        super(message);
    }
}
