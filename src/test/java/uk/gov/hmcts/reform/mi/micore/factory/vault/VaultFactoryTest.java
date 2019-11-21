package uk.gov.hmcts.reform.mi.micore.factory.vault;

import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.keyvault.KeyVaultClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VaultFactoryTest {

    @Mock
    private AzureWrapper azureWrapper;

    @Mock
    private ManagedIdentityCredentials managedIdentityCredentials;

    @InjectMocks
    private VaultFactory vaultFactory;

    @Test
    public void givenRequestForKeyVaultClient_whenGet_thenReturnKeyVaultClient() {
        when(managedIdentityCredentials.getCredentials()).thenReturn(mock(MSICredentials.class));
        when(azureWrapper.getKeyVaultClient(any(MSICredentials.class))).thenReturn(mock(KeyVaultClient.class));

        assertNotNull(vaultFactory.getKeyVaultClient());
    }
}
