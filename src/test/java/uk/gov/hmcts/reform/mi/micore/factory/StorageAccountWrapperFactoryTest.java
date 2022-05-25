package uk.gov.hmcts.reform.mi.micore.factory;

import com.azure.storage.blob.BlobServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.hmcts.reform.mi.micore.component.impl.StorageAccountWrapper;
import uk.gov.hmcts.reform.mi.micore.model.StorageAccountConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageAccountWrapperFactoryTest {

    private static final String STORAGE_ACCOUNT_NAME = "testStorageAccount";
    private static final String CLIENT_ID = "testClientId";
    private static final String CONNECTION_STRING = "testConnectionString";


    @Mock
    private BlobServiceClientFactory blobServiceClientFactory;
    @Mock
    private BlobServiceClient blobServiceClient;

    private StorageAccountWrapperFactory classToTest;

    @Test
    void givenClientId_thenUseManageIdentity() {
        classToTest = new StorageAccountWrapperFactory(CLIENT_ID, blobServiceClientFactory);

        when(blobServiceClientFactory.getBlobClientWithManagedIdentity(CLIENT_ID, STORAGE_ACCOUNT_NAME))
            .thenReturn(blobServiceClient);

        assertEquals(
            new StorageAccountWrapper(blobServiceClient),
            classToTest.getStorageAccountWrapper(new StorageAccountConfig(STORAGE_ACCOUNT_NAME, CONNECTION_STRING)),
            "Expected storage account wrapper"
        );

        verify(blobServiceClientFactory, times(1)).getBlobClientWithManagedIdentity(CLIENT_ID, STORAGE_ACCOUNT_NAME);
    }

    @Test
    void givenEmptyClientId_thenUseConnectionString() {
        classToTest = new StorageAccountWrapperFactory(StringUtils.EMPTY, blobServiceClientFactory);

        when(blobServiceClientFactory.getBlobClientWithConnectionString(CONNECTION_STRING))
            .thenReturn(blobServiceClient);

        assertEquals(
            new StorageAccountWrapper(blobServiceClient),
            classToTest.getStorageAccountWrapper(new StorageAccountConfig(STORAGE_ACCOUNT_NAME, CONNECTION_STRING)),
            "Expected storage account wrapper"
        );

        verify(blobServiceClientFactory, times(1)).getBlobClientWithConnectionString(CONNECTION_STRING);
    }
}
