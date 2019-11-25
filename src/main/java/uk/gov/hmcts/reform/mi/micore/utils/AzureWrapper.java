package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.credentials.UserTokenCredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.springframework.stereotype.Component;

// Wrapper class on Azure functionality to assist with modularity and testing.
@SuppressWarnings("PMD")
@Component
public class AzureWrapper {

    // Identity Section

    public MSICredentials getMsiCredentials(AzureEnvironment azureEnvironment) {
        return new MSICredentials(azureEnvironment);
    }

    public ApplicationTokenCredentials getApplicationTokenCredentials(String clientId,
                                                                      String tenantId,
                                                                      String secret,
                                                                      AzureEnvironment azureEnvironment) {

        return new ApplicationTokenCredentials(clientId, tenantId, secret, azureEnvironment);
    }

    public UserTokenCredentials getUserTokenCredentials(String clientId,
                                                        String tenantId,
                                                        String username,
                                                        String password,
                                                        AzureEnvironment azureEnvironment) {

        return new UserTokenCredentials(clientId, tenantId, username, password, azureEnvironment);
    }

    // Storage Section

    public BlobServiceClientBuilder getBlobServiceClientBuilder() {
        return new BlobServiceClientBuilder();
    }

    public StorageSharedKeyCredential getStorageSharedKeyCredential(String accountName, String token) {
        return new StorageSharedKeyCredential(accountName, token);
    }

    // Key Vault

    public KeyVaultClient getKeyVaultClient(ServiceClientCredentials credentials) {
        return new KeyVaultClient(credentials);
    }
}
