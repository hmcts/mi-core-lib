package uk.gov.hmcts.reform.mi.micore.identity;

import com.microsoft.azure.credentials.UserTokenCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.CredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.UserCredentials;
import uk.gov.hmcts.reform.mi.micore.model.UserCredentialParameters;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserCredentialsTest {

    private static final String TEST_CLIENT_ID = "testClientId";
    private static final String TEST_TENANT_ID = "testTenantId";
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";

    @Mock
    CredentialsFactory credentialsFactory;

    @InjectMocks
    UserCredentials userCredentials;

    @Test
    public void givenRequestForManagedIdentityWithSetup_whenGetCredentials_thenReturnCredentials() {
        UserCredentialParameters parameters =
            new UserCredentialParameters(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_USERNAME, TEST_PASSWORD);

        when(credentialsFactory.getCredentials(parameters)).thenReturn(mock(UserTokenCredentials.class));

        userCredentials.setupCredentials(parameters);
        assertNotNull(userCredentials.getCredentials(), "No Credentials were returned.");
    }

    @Test
    public void givenRequestForManagedIdentityWithoutSetup_whenGetCredentials_thenReturnCredentials() {
        assertThrows(AccessException.class, () -> userCredentials.getCredentials());
    }
}
