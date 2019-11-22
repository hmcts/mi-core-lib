package uk.gov.hmcts.reform.mi.micore.identity.impl;

import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.CredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.Credentials;
import uk.gov.hmcts.reform.mi.micore.model.ApplicationCredentialParameters;

import java.util.Objects;

/**
 * Azure service principal credentials.
 * For manual authentication, i.e. on a local environment or through environment properties.
 */
@Component
public class ServicePrincipalCredentials implements Credentials<ApplicationTokenCredentials> {

    private final CredentialsFactory credentialsFactory;

    private ApplicationTokenCredentials credentials;

    /**
     * Constructor. Setup dependencies.
     *
     * @param credentialsFactory The factory class to determine setup methodology.
     */
    @Autowired
    public ServicePrincipalCredentials(CredentialsFactory credentialsFactory) {
        this.credentialsFactory = credentialsFactory;
    }

    /**
     * Retrieve the most recently generated application credentials.
     *
     * @return credentials object for authentication.
     */
    @Override
    public ApplicationTokenCredentials getCredentials() {
        if (Objects.isNull(credentials)) {
            throw new AccessException("Service credentials have not been setup.");
        }

        return credentials;
    }

    /**
     * Setup application credentials.
     *
     * @param applicationCredentialParameters An object representing the authentication credentials required.
     */
    public void setupCredentials(ApplicationCredentialParameters applicationCredentialParameters) {
        credentials = credentialsFactory.getCredentials(applicationCredentialParameters);
    }
}
