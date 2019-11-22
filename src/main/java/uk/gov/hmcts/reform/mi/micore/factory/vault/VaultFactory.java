package uk.gov.hmcts.reform.mi.micore.factory.vault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

@Component
public class VaultFactory {

    private AzureWrapper azureWrapper;
    private ManagedIdentityCredentials managedIdentityCredentials;

    @Autowired
    public VaultFactory(AzureWrapper azureWrapper,
                        ManagedIdentityCredentials managedIdentityCredentials) {

        this.azureWrapper = azureWrapper;
        this.managedIdentityCredentials = managedIdentityCredentials;
    }

    public KeyVaultClient getKeyVaultClient() {
        return azureWrapper.getKeyVaultClient(managedIdentityCredentials.getCredentials());
    }
}
