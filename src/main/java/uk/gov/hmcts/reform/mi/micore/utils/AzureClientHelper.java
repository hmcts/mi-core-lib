package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;

import java.io.IOException;

@Component
public class AzureClientHelper {

    private static final String STORAGE_RESOURCE = "https://storage.azure.com/";

    private final AzureWrapper azureWrapper;

    @Autowired
    public AzureClientHelper(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

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

    public String getStorageAccessToken(AzureTokenCredentials credentials) {
        try {
            return credentials.getToken(STORAGE_RESOURCE);
        } catch (IOException e) {
            throw new AccessException(e);
        }
    }
}
