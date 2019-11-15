package uk.gov.hmcts.reform.mi.micore.Identity.impl;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.Identity.Credentials;

/**
 * Azure service principal credentials.
 * For manual authentication, i.e. on a local environment or through environment properties.
 * Will be used when the azure.credentials.service.secret property has been set.
 */
@Component
public class ServicePrincipalCredentials implements Credentials<ApplicationTokenCredentials> {

    private ApplicationTokenCredentials credentials;

    /**
     * Constructor. Sets up the application credentials using passed in parameters.
     *
     * @Param clientId The id of the service requesting access.
     * @Param tenantId The id of the Azure Directory the service belongs to.
     * @Param secret   The secret associated with the passed in clientId.
     */
    public ServicePrincipalCredentials(String clientId, String tenantId, String secret) {
        credentials = new ApplicationTokenCredentials(
            clientId,
            tenantId,
            secret,
            AzureEnvironment.AZURE
        );
    }

    /**
     * Retrieve the most recently generated application credentials.
     *
     * @return credentials object for authentication.
     */
    @Override
    public ApplicationTokenCredentials getCredentials() {
        return credentials;
    }
}
