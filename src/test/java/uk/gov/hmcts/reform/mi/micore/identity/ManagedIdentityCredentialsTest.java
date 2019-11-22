package uk.gov.hmcts.reform.mi.micore.identity;

import com.microsoft.azure.credentials.MSICredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.factory.identity.MsiCredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ManagedIdentityCredentialsTest {

    @Mock
    MsiCredentialsFactory msiCredentialsFactory;

    @InjectMocks
    ManagedIdentityCredentials managedIdentityCredentials;

    @Test
    public void givenRequestForManagedIdentity_whenGetCredentials_thenReturnCredentials() {
        when(msiCredentialsFactory.getCredentials()).thenReturn(mock(MSICredentials.class));

        assertNotNull(managedIdentityCredentials.getCredentials());
    }
}
