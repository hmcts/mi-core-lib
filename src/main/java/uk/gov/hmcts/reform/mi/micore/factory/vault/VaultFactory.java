package uk.gov.hmcts.reform.mi.micore.factory.vault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

@Component
public class VaultFactory {

    private final AzureWrapper azureWrapper;

    @Autowired
    public VaultFactory(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

    public KeyVaultClient getKeyVaultClient(ServiceClientCredentials credentials) {
        return azureWrapper.getKeyVaultClient(credentials);
    }
}
