package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.UserTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

@Component
public class UserCredentialsFactory {

    private AzureWrapper azureWrapper;

    @Autowired
    public UserCredentialsFactory(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

    public UserTokenCredentials getCredentials(String clientId, String tenantId, String username, String password) {
        return azureWrapper.getUserTokenCredentials(
            clientId,
            tenantId,
            username,
            password,
            AzureEnvironment.AZURE
        );
    }
}
