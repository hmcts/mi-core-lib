package uk.gov.hmcts.reform.mi.micore.factory.vault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class VaultFactoryTest {

    @Mock
    private AzureWrapper azureWrapper;

    @InjectMocks
    private VaultFactory vaultFactory;

    @Test
    public void givenRequestForKeyVaultClient_whenGet_thenReturnKeyVaultClient() {
        when(azureWrapper.getKeyVaultClient(any(ServiceClientCredentials.class)))
            .thenReturn(mock(KeyVaultClient.class));

        assertNotNull(vaultFactory.getKeyVaultClient(mock(ServiceClientCredentials.class)),
            "No Key Vault Client was returned.");
    }
}
