package uk.gov.hmcts.reform.mi.micore.identity;

/**
 * Interface for Azure authenticating credential methods.
 *
 * @param <T> Type of credential to be used.
 */
public interface Credentials<T> {

    /**
     * Retrieve generated credentials.
     *
     * @return credential object to be used for authentication.
     */
    public T getCredentials();
}
