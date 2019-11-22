package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

@Component
public class MsiCredentialsFactory {

    private AzureWrapper azureWrapper;

    @Autowired
    public MsiCredentialsFactory(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

    public MSICredentials getCredentials() {
        return azureWrapper.getMsiCredentials(AzureEnvironment.AZURE);
    }
}
