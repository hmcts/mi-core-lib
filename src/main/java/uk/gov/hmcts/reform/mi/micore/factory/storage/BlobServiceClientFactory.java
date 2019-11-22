package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.azure.storage.blob.BlobServiceClient;
import com.microsoft.azure.keyvault.KeyVaultClient;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;
import uk.gov.hmcts.reform.mi.micore.vault.impl.CredentialsKeyVault;

public class BlobServiceClientFactory {

    private final AzureClientHelper azureClientHelper;
    private final ManagedIdentityCredentials managedIdentityCredentials;
    private final CredentialsKeyVault credentialsKeyVault;

    @Autowired
    public BlobServiceClientFactory(AzureClientHelper azureClientHelper,
                                    ManagedIdentityCredentials managedIdentityCredentials,
                                    CredentialsKeyVault credentialsKeyVault) {
        this.azureClientHelper = azureClientHelper;
        this.managedIdentityCredentials = managedIdentityCredentials;
        this.credentialsKeyVault = credentialsKeyVault;
    }

    public BlobServiceClient setupBlobStorageClientWithStorageAccount(String storageAccountName) {
        String accessToken = azureClientHelper.getStorageAccessToken(managedIdentityCredentials.getCredentials());
        return azureClientHelper.getBlobClientWithAccessToken(storageAccountName, accessToken);
    }

    public BlobServiceClient setupBlobStorageClientWithConnectionString(String connectionString) {
        return azureClientHelper.getBlobClientWithConnectionString(connectionString);
    }

    public BlobServiceClient setupBlobStorageClientWithVaultSecret(String keyVaultUrl,
                                                                   String connectionStringSecretName) {

        KeyVaultClient keyVaultClient = credentialsKeyVault
            .getKeyVaultClient(managedIdentityCredentials.getCredentials());

        return setupBlobStorageClientWithVaultSecret(keyVaultClient, keyVaultUrl, connectionStringSecretName);
    }

    public BlobServiceClient setupBlobStorageClientWithVaultSecret(KeyVaultClient keyVaultClient,
                                                                   String keyVaultUrl,
                                                                   String connectionStringSecretName) {

        String connectionString = credentialsKeyVault
            .readSecretFromVault(keyVaultClient, keyVaultUrl, connectionStringSecretName);

        return azureClientHelper.getBlobClientWithConnectionString(connectionString);
    }
}
