package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Component
public class AzureClientHelper {

    private static final String STORAGE_RESOURCE = "https://storage.azure.com/";
    private static final String BLOB_CONNECTION_URI = "https://%s.blob.core.windows.net";

    @Autowired
    private AzureWrapper azureWrapper;

    // Storage Section

    public BlobServiceClient getBlobClientWithConnectionString(String connectionString) {
        return azureWrapper.getBlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    public BlobServiceClient getBlobClientWithAccessToken(String storageAccountName, String accessToken) {
        StorageSharedKeyCredential credential =
            azureWrapper.getStorageSharedKeyCredential(storageAccountName,
                AuthTokenUtils.stripBearerScheme(accessToken));

        return azureWrapper.getBlobServiceClientBuilder().credential(credential).buildClient();
    }

    public CloudBlobClient getCloudBlobClient(String connectionString) {
        try {
            return azureWrapper.getCloudStorageAccount(connectionString).createCloudBlobClient();
        } catch (URISyntaxException | InvalidKeyException e) {
            throw new AccessException(e);
        }
    }

    public CloudBlobClient getCloudBlobClient(String storageAccountName, String accessToken) {
        return azureWrapper.getCloudBlobClient(
            getBlobConnectionUri(storageAccountName),
            azureWrapper.getStorageCredentialsToken(storageAccountName, accessToken)
        );
    }

    public URI getBlobConnectionUri(String storageAccountName) {
        try {
            return new URI(String.format(BLOB_CONNECTION_URI, storageAccountName));
        } catch (URISyntaxException e) {
            throw new AccessException(e);
        }
    }

    public String getStorageAccessToken(AzureTokenCredentials credentials) {
        try {
            return credentials.getToken(STORAGE_RESOURCE);
        } catch (IOException e) {
            throw new AccessException(e);
        }
    }
}
