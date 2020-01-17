package uk.gov.hmcts.reform.mi.micore.factory;

import com.azure.identity.ManagedIdentityCredential;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BlobServiceClientFactoryTest {

    private static final String TEST_CONNECTION_STRING = "testConnectionString=testAccount";

    private static final String TEST_CLIENT_ID = "testClientId";
    private static final String TEST_STORAGE_ACCOUNT_NAME = "testAccount";
    private static final String TEST_STORAGE_ACCOUNT_HTTP_URL = "http://testAccount";
    private static final String TEST_STORAGE_ACCOUNT_HTTPS_URL = "https://testAccount";
    private static final String TEST_STORAGE_ACCOUNT_URL = "https://testAccount.blob.core.windows.net";

    @Mock
    private AzureWrapper azureWrapper;

    @InjectMocks
    private BlobServiceClientFactory underTest;

    @Test
    public void givenConnectionString_whenGetBlobClientWithConnectionString_thenReturnBlobServiceClient() {
        BlobServiceClientBuilder mockBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockBlobServiceClientBuilder);

        when(mockBlobServiceClientBuilder.connectionString(eq(TEST_CONNECTION_STRING))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertThat(underTest.getBlobClientWithConnectionString(TEST_CONNECTION_STRING), instanceOf(BlobServiceClient.class));
    }

    @Test
    public void givenClientIdAndStorageAccountName_whenGetBlobClientWithManagedIdentity_thenReturnBlobServiceClient() {
        BlobServiceClientBuilder mockBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockBlobServiceClientBuilder);

        when(mockBlobServiceClientBuilder.endpoint(eq(TEST_STORAGE_ACCOUNT_URL))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.credential(any(ManagedIdentityCredential.class))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertThat(underTest.getBlobClientWithManagedIdentity(TEST_CLIENT_ID, TEST_STORAGE_ACCOUNT_NAME), instanceOf(BlobServiceClient.class));
    }

    @Test
    public void givenClientIdAndHttpUrl_whenGetBlobClientWithManagedIdentity_thenReturnBlobServiceClient() {
        BlobServiceClientBuilder mockBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockBlobServiceClientBuilder);

        when(mockBlobServiceClientBuilder.endpoint(eq(TEST_STORAGE_ACCOUNT_HTTP_URL))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.credential(any(ManagedIdentityCredential.class))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertThat(underTest.getBlobClientWithManagedIdentity(TEST_CLIENT_ID, TEST_STORAGE_ACCOUNT_HTTP_URL), instanceOf(BlobServiceClient.class));
    }

    @Test
    public void givenClientIdAndHttpsUrl_whenGetBlobClientWithManagedIdentity_thenReturnBlobServiceClient() {
        BlobServiceClientBuilder mockBlobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        when(azureWrapper.getBlobServiceClientBuilder()).thenReturn(mockBlobServiceClientBuilder);

        when(mockBlobServiceClientBuilder.endpoint(eq(TEST_STORAGE_ACCOUNT_HTTPS_URL))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.credential(any(ManagedIdentityCredential.class))).thenReturn(mockBlobServiceClientBuilder);
        when(mockBlobServiceClientBuilder.buildClient()).thenReturn(mock(BlobServiceClient.class));

        assertThat(underTest.getBlobClientWithManagedIdentity(TEST_CLIENT_ID, TEST_STORAGE_ACCOUNT_HTTPS_URL), instanceOf(BlobServiceClient.class));
    }
}
