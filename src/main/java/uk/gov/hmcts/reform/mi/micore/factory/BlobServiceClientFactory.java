package uk.gov.hmcts.reform.mi.micore.factory;

import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

/**
 * Factory class providing methods to authenticate and build a Blob Service Client.
 */
@Component
public class BlobServiceClientFactory {

    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTPS_PROTOCOL = "https://";
    private static final String BLOB_CONNECTION_URI = "https://%s.blob.core.windows.net";

    private final AzureWrapper azureWrapper;

    @Autowired
    public BlobServiceClientFactory(AzureWrapper azureWrapper) {
        this.azureWrapper = azureWrapper;
    }

    public BlobServiceClient getBlobClientWithConnectionString(String connectionString) {
        return azureWrapper.getBlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    public BlobServiceClient getBlobClientWithManagedIdentity(String clientId, String storageAccount) {
        return azureWrapper.getBlobServiceClientBuilder()
            .endpoint(parseStorageAccountUrl(storageAccount))
            .credential(new ManagedIdentityCredentialBuilder().clientId(clientId).build())
            .buildClient();
    }

    private String parseStorageAccountUrl(String storageAccount) {
        if (storageAccount.startsWith(HTTP_PROTOCOL) || storageAccount.startsWith(HTTPS_PROTOCOL)) {
            return storageAccount;
        } else {
            return String.format(BLOB_CONNECTION_URI, storageAccount);
        }
    }
}
