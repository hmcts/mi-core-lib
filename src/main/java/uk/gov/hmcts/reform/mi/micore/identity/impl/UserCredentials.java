package uk.gov.hmcts.reform.mi.micore.identity.impl;

import com.microsoft.azure.credentials.UserTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.UserCredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.Credentials;

import java.util.Objects;

/**
 * Azure user credentials.
 * For manual authentication, i.e. on a local environment or through environment properties.
 */
public class UserCredentials implements Credentials<UserTokenCredentials> {

    private UserCredentialsFactory userCredentialsFactory;
    private UserTokenCredentials credentials;

    /**
     * Constructor. Setup dependencies.
     *
     * @param userCredentialsFactory The factory class to determine setup methodology.
     */
    @Autowired
    public UserCredentials(UserCredentialsFactory userCredentialsFactory) {
        this.userCredentialsFactory = userCredentialsFactory;
    }

    /**
     * Retrieve the most recently generated user credentials.
     *
     * @return credentials object for authentication.
     */
    @Override
    public UserTokenCredentials getCredentials() {
        if (Objects.isNull(credentials)) {
            throw new AccessException("User credentials have not been setup.");
        }

        return credentials;
    }

    /**
     * Setup user credentials.
     *
     * @param clientId The id of the service requesting access.
     * @param tenantId The id of the Azure Directory the service belongs to.
     * @param username The username for the user to be authenticated.
     * @param password The password for the user to be authenticated.
     */
    public void setupCredentials(String clientId, String tenantId, String username, String password) {
        credentials = userCredentialsFactory.getCredentials(clientId, tenantId, username, password);
    }
}
