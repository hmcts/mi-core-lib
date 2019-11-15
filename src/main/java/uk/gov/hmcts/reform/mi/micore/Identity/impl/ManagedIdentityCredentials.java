package uk.gov.hmcts.reform.mi.micore.Identity.impl;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.Identity.Credentials;

/**
 * Azure managed identity credentials.
 * For automated authentication on Azure with no need to pass in any additional parameters.
 */
@Component
public class ManagedIdentityCredentials implements Credentials<MSICredentials> {

    private MSICredentials credentials;

    /**
     * Constructor. Sets up managed service identity credential.
     * Identity is based on the current environment the application is currently deployed in.
     */
    public ManagedIdentityCredentials() {
        credentials = new MSICredentials(AzureEnvironment.AZURE);
    }

    /**
     * Retrieve the most recently generated managed service credentials.
     *
     * @return credentials object for authentication.
     */
    @Override
    public MSICredentials getCredentials() {
        return credentials;
    }
}
