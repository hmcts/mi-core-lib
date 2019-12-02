package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.azure.storage.blob.BlobServiceClient;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;
import uk.gov.hmcts.reform.mi.micore.vault.impl.CredentialsKeyVault;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BlobServiceClientFactoryTest {

    private static final String TEST_STORAGE_ACCOUNT = "testAccount";
    private static final String TEST_CONNECTION_STRING = "testConnectionString=testAccount";
    private static final String TEST_KEY_VAULT_URL = "testKeyVaultUrl";
    private static final String TEST_SECRET_NAME = "testSecretName";
    private static final String TEST_CLIENT_ID = "testClientId";

    @Mock
    private AzureClientHelper azureClientHelper;

    @Mock
    private ManagedIdentityCredentials managedIdentityCredentials;

    @Mock
    private CredentialsKeyVault credentialsKeyVault;

    @InjectMocks
    private BlobServiceClientFactory blobServiceClientFactory;

    @Test
    public void givenRequestForBlobServiceClientWithStorageAccountName_whenSetup_thenReturnBlobServiceClient() {
        when(managedIdentityCredentials.getCredentials()).thenReturn(mock(MSICredentials.class));
        when(azureClientHelper.getBlobClientWithAccountName(TEST_CLIENT_ID, TEST_STORAGE_ACCOUNT))
            .thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory
                .setupBlobStorageClientWithStorageAccount(TEST_CLIENT_ID, TEST_STORAGE_ACCOUNT),
            "No Blob Client object has been returned when using storage account credentials.");
    }

    @Test
    public void givenRequestForBlobServiceClientWithConnectionString_whenSetup_thenReturnBlobServiceClient() {
        when(azureClientHelper.getBlobClientWithConnectionString(TEST_CONNECTION_STRING))
            .thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory.setupBlobStorageClientWithConnectionString(TEST_CONNECTION_STRING),
            "No Blob Client object has been returned when using connection string.");
    }

    @Test
    public void givenRequestForBlobServiceClientWithKeyVaultUrlAndSecretName_whenSetup_thenReturnBlobServiceClient() {
        KeyVaultClient mockedKeyVaultClient = mock(KeyVaultClient.class);

        when(managedIdentityCredentials.getCredentials()).thenReturn(mock(MSICredentials.class));
        when(credentialsKeyVault.getKeyVaultClient(any(MSICredentials.class))).thenReturn(mockedKeyVaultClient);
        when(credentialsKeyVault.readSecretFromVault(mockedKeyVaultClient, TEST_KEY_VAULT_URL, TEST_SECRET_NAME))
            .thenReturn(TEST_CONNECTION_STRING);
        when(azureClientHelper.getBlobClientWithConnectionString(TEST_CONNECTION_STRING))
            .thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory
            .setupBlobStorageClientWithVaultSecret(TEST_KEY_VAULT_URL, TEST_SECRET_NAME),
            "No Blob Client object has been returned when using key vault to get connection string.");
    }

    @Test
    public void givenRequestWithCustomVaultClientAndVaultUrlAndSecretName_whenSetup_thenReturnBlobServiceClient() {
        KeyVaultClient mockedKeyVaultClient = mock(KeyVaultClient.class);

        when(credentialsKeyVault.readSecretFromVault(mockedKeyVaultClient, TEST_KEY_VAULT_URL, TEST_SECRET_NAME))
            .thenReturn(TEST_CONNECTION_STRING);
        when(azureClientHelper.getBlobClientWithConnectionString(TEST_CONNECTION_STRING))
            .thenReturn(mock(BlobServiceClient.class));

        assertNotNull(blobServiceClientFactory
            .setupBlobStorageClientWithVaultSecret(mockedKeyVaultClient, TEST_KEY_VAULT_URL, TEST_SECRET_NAME),
            "No Blob Client object has been returned when using key vault to get connection string.");
    }
}
