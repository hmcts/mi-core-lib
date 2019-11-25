package uk.gov.hmcts.reform.mi.micore.identity.impl;

import com.microsoft.azure.credentials.UserTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.CredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.Credentials;
import uk.gov.hmcts.reform.mi.micore.model.UserCredentialParameters;

import java.util.Objects;

/**
 * Azure user credentials.
 * For manual authentication, i.e. on a local environment or through environment properties.
 */
@Component
public class UserCredentials implements Credentials<UserTokenCredentials> {

    private final CredentialsFactory credentialsFactory;

    private UserTokenCredentials credentials;

    /**
     * Constructor. Setup dependencies.
     *
     * @param credentialsFactory The factory class to determine setup methodology.
     */
    @Autowired
    public UserCredentials(CredentialsFactory credentialsFactory) {
        this.credentialsFactory = credentialsFactory;
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
     * @param userCredentialParameters An object representing the authentication credentials required.
     */
    public void setupCredentials(UserCredentialParameters userCredentialParameters) {
        credentials = credentialsFactory.getCredentials(userCredentialParameters);
    }
}
