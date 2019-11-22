package uk.gov.hmcts.reform.mi.micore.vault.impl;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.factory.vault.VaultFactory;
import uk.gov.hmcts.reform.mi.micore.vault.KeyVault;

@Component
public class CredentialsKeyVault implements KeyVault {

    private final VaultFactory vaultFactory;

    @Autowired
    public CredentialsKeyVault(VaultFactory vaultFactory) {
        this.vaultFactory = vaultFactory;
    }

    @Override
    public String readSecretFromVault(KeyVaultClient keyVaultClient, String keyVaultUrl, String secretName) {
        return keyVaultClient.getSecret(keyVaultUrl, secretName).value();
    }

    @Override
    public void writeSecretToVault(KeyVaultClient keyVaultClient,
                                   String keyVaultUrl,
                                   String secretName,
                                   String secretValue) {

        keyVaultClient.setSecret(keyVaultUrl, secretName, secretValue);
    }

    public KeyVaultClient getKeyVaultClient(ServiceClientCredentials serviceClientCredentials) {
        return vaultFactory.getKeyVaultClient(serviceClientCredentials);
    }
}
