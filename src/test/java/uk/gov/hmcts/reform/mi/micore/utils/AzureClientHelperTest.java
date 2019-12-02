package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.identity.ManagedIdentityCredential;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AzureClientHelperTest {

    private static final String TEST_CONNECTION_STRING = "testConnectionString";
    private static final String TEST_ACCOUNT_NAME = "testAccountName";
    private static final String TEST_ACCESS_TOKEN = "testAccessToken";
    private static final String TEST_CLIENT_ID = "testClientId";
    private static final String TEST_BLOB_CONNECTION_URI = "https://testAccountName.blob.core.windows.net";
    private static final String STORAGE_RESOURCE = "https://storage.azure.com/";

    @Mock
    private AzureWrapper azureWrapper;

    @InjectMocks
    private AzureClientHelper azureClientHelper;

    @Test
    public void givenConnectionString_whenGetBlobClient_thenReturnBlobClient() {
        BlobServiceClientBuilder mockedBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.connectionString(TEST_CONNECTION_STRING))
            .thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertNotNull(azureClientHelper.getBlobClientWithConnectionString(TEST_CONNECTION_STRING),
            "Blob client was not returned with given connection string.");
    }

    @Test
    public void givenStorageAccountAndAccessToken_whenGetBlobClient_thenReturnBlobClient() {
        StorageSharedKeyCredential mockedCredential = mock(StorageSharedKeyCredential.class);
        when(azureWrapper.getStorageSharedKeyCredential(TEST_ACCOUNT_NAME, TEST_ACCESS_TOKEN))
            .thenReturn(mockedCredential);

        BlobServiceClientBuilder mockedBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.credential(mockedCredential)).thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertNotNull(azureClientHelper.getBlobClientWithAccessToken(TEST_ACCOUNT_NAME, TEST_ACCESS_TOKEN),
            "Blob client was not returned with given storage account and access token.");
    }

    @Test
    public void givenStorageAccountName_whenGetBlobClient_thenReturnBlobClient() {
        BlobServiceClientBuilder mockedBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.endpoint(TEST_BLOB_CONNECTION_URI))
            .thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.credential(any(ManagedIdentityCredential.class)))
            .thenReturn(mockedBlobServiceClientBuilder);
        when(mockedBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertNotNull(azureClientHelper.getBlobClientWithAccountName(TEST_CLIENT_ID, TEST_ACCOUNT_NAME),
            "Blob client was not returned with given storage account name.");
    }

    @Test
    public void givenStorageAccountName_whenGetStorageAccessToken_thenReturnToken() throws Exception {
        AzureTokenCredentials tokenCredentials = mock(AzureTokenCredentials.class);

        when(tokenCredentials.getToken(STORAGE_RESOURCE)).thenReturn(TEST_ACCESS_TOKEN);

        assertEquals(TEST_ACCESS_TOKEN, azureClientHelper.getStorageAccessToken(tokenCredentials),
            "Expected token not received.");
    }

    @Test
    public void givenInvalidInput_whenGetStorageAccessToken_thenThrowAccessException() throws Exception {
        AzureTokenCredentials tokenCredentials = mock(AzureTokenCredentials.class);

        when(tokenCredentials.getToken(STORAGE_RESOURCE)).thenThrow(new IOException());

        assertThrows(AccessException.class, () -> azureClientHelper.getStorageAccessToken(tokenCredentials));
    }
}
