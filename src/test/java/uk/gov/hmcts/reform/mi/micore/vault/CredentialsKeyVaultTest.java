package uk.gov.hmcts.reform.mi.micore.vault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.factory.vault.VaultFactory;
import uk.gov.hmcts.reform.mi.micore.vault.impl.CredentialsKeyVault;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CredentialsKeyVaultTest {

    private static final String TEST_VAULT_URL = "testVaultUrl";
    private static final String TEST_NAME = "test-secret";
    private static final String TEST_VALUE = "test-value";

    @Mock
    private VaultFactory vaultFactory;

    @InjectMocks
    private CredentialsKeyVault credentialsKeyVault;

    @Test
    public void givenSecretNameAndDefaultCredentials_whenReadSecretFromVault_returnSecret() {
        KeyVaultClient mockedKeyVaultClient = mock(KeyVaultClient.class);
        SecretBundle mockedSecretBundle = mock(SecretBundle.class);

        when(mockedKeyVaultClient.getSecret(TEST_VAULT_URL, TEST_NAME)).thenReturn(mockedSecretBundle);
        when(mockedSecretBundle.value()).thenReturn(TEST_VALUE);

        assertEquals(TEST_VALUE,
            credentialsKeyVault.readSecretFromVault(mockedKeyVaultClient, TEST_VAULT_URL, TEST_NAME),
            "Retrieved secret does not match expected result.");
    }

    @Test
    public void givenSecretNameAndValueAndDefaultCredentials_whenWriteSecretToVault_verifyCall() {
        KeyVaultClient mockedKeyVaultClient = mock(KeyVaultClient.class);

        credentialsKeyVault.writeSecretToVault(mockedKeyVaultClient, TEST_VAULT_URL, TEST_NAME, TEST_VALUE);

        verify(mockedKeyVaultClient, times(1)).setSecret(TEST_VAULT_URL, TEST_NAME, TEST_VALUE);
    }

    @Test
    public void givenVaultFactoryWorks_whenSetKeyVaultClient_verifyAttemptToGetNewClient() {
        credentialsKeyVault.getKeyVaultClient(mock(ServiceClientCredentials.class));
        verify(vaultFactory).getKeyVaultClient(any(ServiceClientCredentials.class));
    }
}
