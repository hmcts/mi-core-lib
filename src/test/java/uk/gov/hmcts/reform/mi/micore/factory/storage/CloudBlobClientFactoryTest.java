package uk.gov.hmcts.reform.mi.micore.factory.storage;

import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CloudBlobClientFactoryTest {

    private static final String TEST_STORAGE_ACCOUNT = "testAccount";
    private static final String TEST_TOKEN = "testToken";

    @Mock
    private AzureClientHelper azureClientHelper;

    @Mock
    private ManagedIdentityCredentials managedIdentityCredentials;

    @InjectMocks
    private CloudBlobClientFactory cloudBlobClientFactory;

    @Test
    public void givenRequestForCloudBlobClient_whenSetup_thenReturnCloudBlobClient() {
        when(managedIdentityCredentials.getCredentials()).thenReturn(mock(MSICredentials.class));
        when(azureClientHelper.getStorageAccessToken(any(MSICredentials.class))).thenReturn(TEST_TOKEN);
        when(azureClientHelper.getCloudBlobClient(TEST_STORAGE_ACCOUNT, TEST_TOKEN)).thenReturn(mock(CloudBlobClient.class));

        assertNotNull(cloudBlobClientFactory.setupBlobStorageClient(TEST_STORAGE_ACCOUNT));
    }
}
