package uk.gov.hmcts.reform.mi.micore.identity;

import com.microsoft.azure.credentials.MSICredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.factory.identity.CredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ManagedIdentityCredentialsTest {

    @Mock
    CredentialsFactory credentialsFactory;

    @InjectMocks
    ManagedIdentityCredentials managedIdentityCredentials;

    @Test
    public void givenRequestForManagedIdentity_whenGetCredentials_thenReturnCredentials() {
        when(credentialsFactory.getCredentials()).thenReturn(mock(MSICredentials.class));

        assertNotNull(managedIdentityCredentials.getCredentials(), "No Credentials were returned.");
    }

    @Test
    public void givenRequestForManagedIdentity_whenGetCredentialsTwice_thenReturnStoredCredentials() {
        when(credentialsFactory.getCredentials()).thenReturn(mock(MSICredentials.class));

        managedIdentityCredentials.getCredentials();

        assertNotNull(managedIdentityCredentials.getCredentials(), "No Credentials were returned.");
        verify(credentialsFactory, times(1)).getCredentials();
    }
}
