package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.credentials.UserTokenCredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsToken;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

// Wrapper class on Azure functionality to assist with modularity and testing.
@SuppressWarnings("PMD")
@Component
public class AzureWrapper {

    @Autowired
    public AzureWrapper() {
        // No Dependencies
    }

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

    public CloudBlobClient getCloudBlobClient(URI baseUri, StorageCredentials credentials) {
        return new CloudBlobClient(baseUri, credentials);
    }

    public CloudStorageAccount getCloudStorageAccount(String connectionString)
        throws URISyntaxException, InvalidKeyException {

        return CloudStorageAccount.parse(connectionString);
    }

    public StorageCredentialsToken getStorageCredentialsToken(String accountName, String token) {
        return new StorageCredentialsToken(accountName, token);
    }

    public StorageSharedKeyCredential getStorageSharedKeyCredential(String accountName, String token) {
        return new StorageSharedKeyCredential(accountName, token);
    }

    // Key Vault

    public KeyVaultClient getKeyVaultClient(ServiceClientCredentials credentials) {
        return new KeyVaultClient(credentials);
    }
}
