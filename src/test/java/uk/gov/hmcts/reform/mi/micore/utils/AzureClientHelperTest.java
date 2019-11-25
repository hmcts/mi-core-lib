package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentialsToken;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AzureClientHelperTest {

    private static final String TEST_CONNECTION_STRING = "testConnectionString";
    private static final String TEST_ACCOUNT_NAME = "testAccountName";
    private static final String TEST_ACCESS_TOKEN = "testAccessToken";

    private static final String STORAGE_RESOURCE = "https://storage.azure.com/";
    private static final String BLOB_CONNECTION_URI = "https://%s.blob.core.windows.net";

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
    public void givenConnectionString_whenGetCloudBlobClient_thenReturnBlobClient() throws Exception {
        CloudStorageAccount mockedStorageAccount = mock(CloudStorageAccount.class);
        when(azureWrapper.getCloudStorageAccount(TEST_CONNECTION_STRING)).thenReturn(mockedStorageAccount);

        when(mockedStorageAccount.createCloudBlobClient()).thenReturn(mock(CloudBlobClient.class));

        assertNotNull(azureClientHelper.getCloudBlobClientWithConnectionString(TEST_CONNECTION_STRING),
            "Cloud blob client was not returned with given connection string.");
    }

    @Test
    public void givenInvalidUriConnectionString_whenGetCloudBlobClient_thenThrowAccessException() throws Exception {
        when(azureWrapper.getCloudStorageAccount(TEST_CONNECTION_STRING))
            .thenThrow(new URISyntaxException("Invalid", "String"));

        assertThrows(AccessException.class,
            () -> azureClientHelper.getCloudBlobClientWithConnectionString(TEST_CONNECTION_STRING));
    }

    @Test
    public void givenInvalidKey_whenGetCloudBlobClient_thenThrowAccessException() throws Exception {
        when(azureWrapper.getCloudStorageAccount(TEST_CONNECTION_STRING))
            .thenThrow(new InvalidKeyException("Invalid"));

        assertThrows(AccessException.class,
            () -> azureClientHelper.getCloudBlobClientWithConnectionString(TEST_CONNECTION_STRING));
    }

    @Test
    public void givenStorageAccountAndAccessToken_whenGetCloudBlobClient_thenReturnBlobClient() throws Exception {
        URI storageAccountUri = new URI(String.format(BLOB_CONNECTION_URI, TEST_ACCOUNT_NAME));
        StorageCredentialsToken mockedCredential = mock(StorageCredentialsToken.class);
        when(azureWrapper.getStorageCredentialsToken(TEST_ACCOUNT_NAME, TEST_ACCESS_TOKEN))
            .thenReturn(mockedCredential);

        when(azureWrapper.getCloudBlobClient(storageAccountUri, mockedCredential))
            .thenReturn(mock(CloudBlobClient.class));

        assertNotNull(azureClientHelper.getCloudBlobClientWithAccessToken(TEST_ACCOUNT_NAME, TEST_ACCESS_TOKEN),
            "Cloud blob client was not returned with given storage account and access token.");
    }

    @Test
    public void givenUriString_whenGetBlobUriFormat_thenReturnFormattedUriObject() {
        String expectedUriPath = String.format(BLOB_CONNECTION_URI, TEST_ACCOUNT_NAME);

        URI uri = azureClientHelper.getBlobConnectionUri(TEST_ACCOUNT_NAME);

        assertEquals(expectedUriPath, uri.getScheme() + "://" + uri.getHost(),
            "URI scheme and host does not match the expected output.");
    }

    @Test
    public void givenInvalidUriString_whenGetBlobUriFormat_thenThrowAccessException() {
        assertThrows(AccessException.class,
            () -> azureClientHelper.getBlobConnectionUri("$$$$$$###"));
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
