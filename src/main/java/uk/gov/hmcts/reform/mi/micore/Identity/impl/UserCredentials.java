package uk.gov.hmcts.reform.mi.micore.Identity.impl;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.UserTokenCredentials;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.Identity.Credentials;

/**
 * Azure user credentials.
 * For manual authentication, i.e. on a local environment or through environment properties.
 * Will be used when the azure.credentials.user.username property has been set.
 */
@Component
public class UserCredentials implements Credentials<UserTokenCredentials> {

    private UserTokenCredentials credentials;

    /**
     * Constructor. Sets up the user credentials using passed in parameters.
     *
     * @Param clientId The id of the service requesting access.
     * @Param tenantId The id of the Azure Directory the service belongs to.
     * @Param username The username of the user requesting access.
     * @Param password The password of the user requesting access.
     */
    public UserCredentials(String clientId, String tenantId, String username, String password) {
        credentials = new UserTokenCredentials(
            clientId,
            tenantId,
            username,
            password,
            AzureEnvironment.AZURE
        );
    }

    /**
     * Retrieve the most recently generated user credentials.
     *
     * @return credentials object for authentication.
     */
    @Override
    public UserTokenCredentials getCredentials() {
        return credentials;
    }
}
