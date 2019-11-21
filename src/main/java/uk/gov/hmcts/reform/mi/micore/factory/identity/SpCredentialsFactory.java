package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

@Component
public class SpCredentialsFactory {

    private AzureWrapper azureWrapper;

    @Autowired
    public SpCredentialsFactory(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

    public ApplicationTokenCredentials getCredentials(String clientId, String tenantId, String secret) {
        return azureWrapper.getApplicationTokenCredentials(
            clientId,
            tenantId,
            secret,
            AzureEnvironment.AZURE
        );
    }
}
