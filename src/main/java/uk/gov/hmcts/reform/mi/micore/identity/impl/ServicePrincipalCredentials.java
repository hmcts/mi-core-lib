package uk.gov.hmcts.reform.mi.micore.identity.impl;

import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.SpCredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.Credentials;

import java.util.Objects;

/**
 * Azure service principal credentials.
 * For manual authentication, i.e. on a local environment or through environment properties.
 */
@Component
public class ServicePrincipalCredentials implements Credentials<ApplicationTokenCredentials> {

    private ApplicationTokenCredentials credentials;
    private SpCredentialsFactory spCredentialsFactory;

    /**
     * Constructor. Setup dependencies.
     *
     * @param spCredentialsFactory The factory class to determine setup methodology.
     */
    @Autowired
    public ServicePrincipalCredentials(SpCredentialsFactory spCredentialsFactory) {
        this.spCredentialsFactory = spCredentialsFactory;
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
     * @param clientId The id of the service requesting access.
     * @param tenantId The id of the Azure Directory the service belongs to.
     * @param secret The secret associated with the clientId.
     */
    public void setupCredentials(String clientId, String tenantId, String secret) {
        credentials = spCredentialsFactory.getCredentials(clientId, tenantId, secret);
    }
}
