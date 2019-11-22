package uk.gov.hmcts.reform.mi.micore.vault;

import com.microsoft.azure.keyvault.KeyVaultClient;

public interface KeyVault {

    String readSecretFromVault(KeyVaultClient keyVaultClient, String keyVaultUrl, String secretName);

    void writeSecretToVault(KeyVaultClient keyVaultClient, String keyVaultUrl, String secretName, String secretValue);
}
