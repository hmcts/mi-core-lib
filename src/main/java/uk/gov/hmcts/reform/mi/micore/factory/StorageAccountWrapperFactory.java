package uk.gov.hmcts.reform.mi.micore.factory;

import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.mi.micore.component.impl.StorageAccountWrapper;
import uk.gov.hmcts.reform.mi.micore.model.StorageAccountConfig;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class StorageAccountWrapperFactory {

    private final String clientId;
    private final BlobServiceClientFactory blobServiceClientFactory;

    @Autowired
    public StorageAccountWrapperFactory(@Value("${azure.managed-identity.client-id:}") String clientId,
                                        BlobServiceClientFactory blobServiceClientFactory) {
        this.clientId = clientId;
        this.blobServiceClientFactory = blobServiceClientFactory;
    }

    public StorageAccountWrapper getStorageAccountWrapper(StorageAccountConfig storageConfig) {
        return  new StorageAccountWrapper(getConnection(storageConfig));
    }

    private BlobServiceClient getConnection(StorageAccountConfig storageConfig) {

        if (isBlank(clientId)) {
            return blobServiceClientFactory.getBlobClientWithConnectionString(storageConfig.getConnectionString());
        } else {
            return blobServiceClientFactory.getBlobClientWithManagedIdentity(clientId, storageConfig.getAccountName());
        }
    }
}
