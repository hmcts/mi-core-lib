package uk.gov.hmcts.reform.mi.micore.identity;

import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.CredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ServicePrincipalCredentials;
import uk.gov.hmcts.reform.mi.micore.model.ApplicationCredentialParameters;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ServicePrincipalCredentialsTest {

    private static final String TEST_CLIENT_ID = "testClientId";
    private static final String TEST_TENANT_ID = "testTenantId";
    private static final String TEST_SECRET = "testSecret";

    @Mock
    CredentialsFactory credentialsFactory;

    @InjectMocks
    ServicePrincipalCredentials servicePrincipalCredentials;

    @Test
    public void givenRequestForManagedIdentityWithSetup_whenGetCredentials_thenReturnCredentials() {
        ApplicationCredentialParameters parameters =
            new ApplicationCredentialParameters(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_SECRET);

        when(credentialsFactory.getCredentials(parameters)).thenReturn(mock(ApplicationTokenCredentials.class));

        servicePrincipalCredentials.setupCredentials(parameters);
        assertNotNull(servicePrincipalCredentials.getCredentials(), "No Credentials were returned.");
    }

    @Test
    public void givenRequestForManagedIdentityWithoutSetup_whenGetCredentials_thenReturnCredentials() {
        assertThrows(AccessException.class, () -> servicePrincipalCredentials.getCredentials());
    }
}
