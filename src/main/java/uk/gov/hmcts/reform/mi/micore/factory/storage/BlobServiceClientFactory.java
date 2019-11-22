package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;
import uk.gov.hmcts.reform.mi.micore.vault.impl.ManagedIdentityKeyVault;

public class BlobServiceClientFactory {

    private AzureClientHelper azureClientHelper;
    private ManagedIdentityCredentials managedIdentityCredentials;
    private ManagedIdentityKeyVault managedIdentityKeyVault;

    @Autowired
    public BlobServiceClientFactory(AzureClientHelper azureClientHelper,
                                    ManagedIdentityCredentials managedIdentityCredentials,
                                    ManagedIdentityKeyVault managedIdentityKeyVault) {
        this.azureClientHelper = azureClientHelper;
        this.managedIdentityCredentials = managedIdentityCredentials;
        this.managedIdentityKeyVault = managedIdentityKeyVault;
    }

    public BlobServiceClient setupBlobStorageClient(String authenticationString) {
        // Regex for StorageConnectionString format
        if (authenticationString.matches(".+=.+")) {
            return azureClientHelper.getBlobClientWithConnectionString(authenticationString);
        } else {
            String accessToken = azureClientHelper.getStorageAccessToken(managedIdentityCredentials.getCredentials());
            return azureClientHelper.getBlobClientWithAccessToken(authenticationString, accessToken);
        }
    }

    public BlobServiceClient setupBlobStorageClient(String keyVaultUrl, String connectionStringSecretName) {
        String connectionString = managedIdentityKeyVault.readSecretFromVault(keyVaultUrl, connectionStringSecretName);

        return azureClientHelper.getBlobClientWithConnectionString(connectionString);
    }
}
