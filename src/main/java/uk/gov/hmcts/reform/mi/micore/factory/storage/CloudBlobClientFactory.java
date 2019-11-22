package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;

@Deprecated
public class CloudBlobClientFactory {

    private final AzureClientHelper azureClientHelper;
    private final ManagedIdentityCredentials managedIdentityCredentials;

    @Autowired
    public CloudBlobClientFactory(AzureClientHelper azureClientHelper,
                                  ManagedIdentityCredentials managedIdentityCredentials) {
        this.azureClientHelper = azureClientHelper;
        this.managedIdentityCredentials = managedIdentityCredentials;
    }

    public CloudBlobClient setupBlobStorageClientWithStorageAccount(String storageAccountName) {
        String accessToken = azureClientHelper.getStorageAccessToken(managedIdentityCredentials.getCredentials());

        return azureClientHelper.getCloudBlobClientWithAccessToken(storageAccountName, accessToken);
    }

    public CloudBlobClient setupBlobStorageClientWithConnectionString(String connectionString) {
        return azureClientHelper.getCloudBlobClientWithConnectionString(connectionString);
    }
}
