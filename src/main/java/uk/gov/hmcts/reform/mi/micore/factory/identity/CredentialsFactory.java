package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.credentials.UserTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.model.ApplicationCredentialParameters;
import uk.gov.hmcts.reform.mi.micore.model.UserCredentialParameters;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

@Component
public class CredentialsFactory {

    private final AzureWrapper azureWrapper;

    @Autowired
    public CredentialsFactory(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

    public MSICredentials getCredentials() {
        return azureWrapper.getMsiCredentials(AzureEnvironment.AZURE);
    }

    public ApplicationTokenCredentials getCredentials(ApplicationCredentialParameters applicationCredentialParameters) {
        return azureWrapper.getApplicationTokenCredentials(
            applicationCredentialParameters.getClientId(),
            applicationCredentialParameters.getTenantId(),
            applicationCredentialParameters.getApplicationSecret(),
            AzureEnvironment.AZURE
        );
    }

    public UserTokenCredentials getCredentials(UserCredentialParameters userCredentialParameters) {
        return azureWrapper.getUserTokenCredentials(
            userCredentialParameters.getClientId(),
            userCredentialParameters.getTenantId(),
            userCredentialParameters.getUsername(),
            userCredentialParameters.getPassword(),
            AzureEnvironment.AZURE
        );
    }
}
