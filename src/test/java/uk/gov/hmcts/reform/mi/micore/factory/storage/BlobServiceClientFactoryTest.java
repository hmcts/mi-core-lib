package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.azure.storage.blob.BlobServiceClient;
import com.microsoft.azure.credentials.MSICredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;
import uk.gov.hmcts.reform.mi.micore.vault.impl.ManagedIdentityKeyVault;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BlobServiceClientFactoryTest {

    private static final String TEST_STORAGE_ACCOUNT = "testAccount";
    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_CONNECTION_STRING = "testConnectionString=testAccount";
    private static final String TEST_KEY_VAULT_URL = "testKeyVaultUrl";
    private static final String TEST_SECRET_NAME = "testSecretName";

    @Mock
    private AzureClientHelper azureClientHelper;

    @Mock
    private ManagedIdentityCredentials managedIdentityCredentials;

    @Mock
    private ManagedIdentityKeyVault managedIdentityKeyVault;

    @InjectMocks
    private BlobServiceClientFactory blobServiceClientFactory;

    @Test
    public void givenRequestForBlobServiceClientWithStorageAccountName_whenSetup_thenReturnBlobServiceClient() {
        when(managedIdentityCredentials.getCredentials()).thenReturn(mock(MSICredentials.class));
        when(azureClientHelper.getStorageAccessToken(any(MSICredentials.class))).thenReturn(TEST_TOKEN);
        when(azureClientHelper.getBlobClientWithAccessToken(TEST_STORAGE_ACCOUNT, TEST_TOKEN))
            .thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory.setupBlobStorageClient(TEST_STORAGE_ACCOUNT));
    }

    @Test
    public void givenRequestForBlobServiceClientWithConnectionString_whenSetup_thenReturnBlobServiceClient() {
        when(azureClientHelper.getBlobClientWithConnectionString(TEST_CONNECTION_STRING)).thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory.setupBlobStorageClient(TEST_CONNECTION_STRING));
    }

    @Test
    public void givenRequestForBlobServiceClientWithKeyVaultUrlAndSecretName_whenSetup_thenReturnBlobServiceClient() {
        when(managedIdentityKeyVault.readSecretFromVault(TEST_KEY_VAULT_URL, TEST_SECRET_NAME)).thenReturn(TEST_CONNECTION_STRING);
        when(azureClientHelper.getBlobClientWithConnectionString(TEST_CONNECTION_STRING)).thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory.setupBlobStorageClient(TEST_KEY_VAULT_URL, TEST_SECRET_NAME));
    }
}
