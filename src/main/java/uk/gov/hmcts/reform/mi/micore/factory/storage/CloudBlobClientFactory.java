package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;

@Deprecated
public class CloudBlobClientFactory {

    private AzureClientHelper azureClientHelper;
    private ManagedIdentityCredentials managedIdentityCredentials;

    @Autowired
    public CloudBlobClientFactory(AzureClientHelper azureClientHelper,
                                  ManagedIdentityCredentials managedIdentityCredentials) {
        this.azureClientHelper = azureClientHelper;
        this.managedIdentityCredentials = managedIdentityCredentials;
    }

    public CloudBlobClient setupBlobStorageClient(String storageAccountName) {
        String accessToken = azureClientHelper.getStorageAccessToken(managedIdentityCredentials.getCredentials());

        return azureClientHelper.getCloudBlobClient(storageAccountName, accessToken);
    }
}
