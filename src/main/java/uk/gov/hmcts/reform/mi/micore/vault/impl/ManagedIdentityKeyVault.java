package uk.gov.hmcts.reform.mi.micore.vault.impl;

import com.microsoft.azure.keyvault.KeyVaultClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.factory.vault.VaultFactory;
import uk.gov.hmcts.reform.mi.micore.vault.KeyVault;

@Component
public class ManagedIdentityKeyVault implements KeyVault {

    private VaultFactory vaultFactory;

    @Autowired
    public ManagedIdentityKeyVault(VaultFactory vaultFactory) {
        this.vaultFactory = vaultFactory;
    }

    @Override
    public String readSecretFromVault(String keyVaultUrl, String secretName) {
        return getKeyVaultClient().getSecret(keyVaultUrl, secretName).value();
    }

    @Override
    public void writeSecretToVault(String keyVaultUrl, String secretName, String secretValue) {
        getKeyVaultClient().setSecret(keyVaultUrl, secretName, secretValue);
    }

    public KeyVaultClient getKeyVaultClient() {
        return vaultFactory.getKeyVaultClient();
    }
}
