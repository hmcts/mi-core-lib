package uk.gov.hmcts.reform.mi.micore.identity;

import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.SpCredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ServicePrincipalCredentials;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ServicePrincipalCredentialsTest {

    private static final String TEST_CLIENT_ID = "testClientId";
    private static final String TEST_TENANT_ID = "testTenantId";
    private static final String TEST_SECRET = "testSecret";

    @Mock
    SpCredentialsFactory spCredentialsFactory;

    @InjectMocks
    ServicePrincipalCredentials servicePrincipalCredentials;

    @Test
    public void givenRequestForManagedIdentityWithSetup_whenGetCredentials_thenReturnCredentials() {
        when(spCredentialsFactory.getCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_SECRET))
            .thenReturn(mock(ApplicationTokenCredentials.class));

        servicePrincipalCredentials.setupCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_SECRET);
        assertNotNull(servicePrincipalCredentials.getCredentials());
    }

    @Test
    public void givenRequestForManagedIdentityWithoutSetup_whenGetCredentials_thenReturnCredentials() {
        assertThrows(AccessException.class, () -> servicePrincipalCredentials.getCredentials());
    }
}
