package uk.gov.hmcts.reform.mi.micore.vault.impl;

import com.microsoft.azure.keyvault.KeyVaultClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.factory.vault.VaultFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;

@Component
public class ManagedIdentityKeyVault extends CredentialsKeyVault {

    private final ManagedIdentityCredentials managedIdentityCredentials;

    @Autowired
    public ManagedIdentityKeyVault(VaultFactory vaultFactory,
                                   ManagedIdentityCredentials managedIdentityCredentials) {

        super(vaultFactory);
        this.managedIdentityCredentials = managedIdentityCredentials;
    }

    public String readSecretFromVault(String keyVaultUrl, String secretName) {
        return readSecretFromVault(getKeyVaultClient(), keyVaultUrl, secretName);
    }

    public void writeSecretToVault(String keyVaultUrl,
                                   String secretName,
                                   String secretValue) {

        writeSecretToVault(getKeyVaultClient(), keyVaultUrl, secretName, secretValue);
    }

    public KeyVaultClient getKeyVaultClient() {
        return getKeyVaultClient(managedIdentityCredentials.getCredentials());
    }

    public KeyVaultClient getKeyVaultClient(String clientId) {
        return getKeyVaultClient(managedIdentityCredentials.getCredentials().withClientId(clientId));
    }
}
