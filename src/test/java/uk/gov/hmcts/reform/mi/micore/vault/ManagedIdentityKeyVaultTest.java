package uk.gov.hmcts.reform.mi.micore.vault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.SecretBundle;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.factory.vault.VaultFactory;
import uk.gov.hmcts.reform.mi.micore.vault.impl.ManagedIdentityKeyVault;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ManagedIdentityKeyVaultTest {

    private static final String TEST_VAULT_URL = "testVaultUrl";
    private static final String TEST_NAME = "test-secret";
    private static final String TEST_VALUE = "test-value";

    @Mock
    private VaultFactory vaultFactory;

    @InjectMocks
    private ManagedIdentityKeyVault managedIdentityKeyVault;

    @Test
    public void givenSecretName_whenGetSecretFromVault_returnSecret() {
        SecretBundle mockedSecretBundle = mock(SecretBundle.class);
        KeyVaultClient mockedKeyVaultClient = mock(KeyVaultClient.class);

        when(mockedSecretBundle.value()).thenReturn(TEST_VALUE);
        when(mockedKeyVaultClient.getSecret(TEST_VAULT_URL, TEST_NAME)).thenReturn(mockedSecretBundle);

        when(vaultFactory.getKeyVaultClient()).thenReturn(mockedKeyVaultClient);

        assertEquals(TEST_VALUE, managedIdentityKeyVault.readSecretFromVault(TEST_VAULT_URL, TEST_NAME));
    }

    @Test
    public void givenSecretNameAndValue_whenWriteSecretToVault_verifyCall() {
        KeyVaultClient mockedKeyVaultClient = mock(KeyVaultClient.class);

        when(vaultFactory.getKeyVaultClient()).thenReturn(mockedKeyVaultClient);

        managedIdentityKeyVault.writeSecretToVault(TEST_VAULT_URL, TEST_NAME, TEST_VALUE);

        verify(mockedKeyVaultClient, times(1)).setSecret(TEST_VAULT_URL, TEST_NAME, TEST_VALUE);
    }

    @Test
    public void givenVaultFactoryWorks_whenGetKeyVaultClient_returnKeyVaultClient() {
        when(vaultFactory.getKeyVaultClient()).thenReturn(mock(KeyVaultClient.class));

        assertNotNull(managedIdentityKeyVault.getKeyVaultClient());
    }
}
