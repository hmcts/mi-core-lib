package uk.gov.hmcts.reform.mi.micore.identity;

import com.microsoft.azure.credentials.UserTokenCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.identity.UserCredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.impl.UserCredentials;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserCredentialsTest {

    private static final String TEST_CLIENT_ID = "testClientId";
    private static final String TEST_TENANT_ID = "testTenantId";
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";

    @Mock
    UserCredentialsFactory userCredentialsFactory;

    @InjectMocks
    UserCredentials userCredentials;

    @Test
    public void givenRequestForManagedIdentityWithSetup_whenGetCredentials_thenReturnCredentials() {
        when(userCredentialsFactory.getCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_USERNAME, TEST_PASSWORD))
            .thenReturn(mock(UserTokenCredentials.class));

        userCredentials.setupCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(userCredentials.getCredentials());
    }

    @Test
    public void givenRequestForManagedIdentityWithoutSetup_whenGetCredentials_thenReturnCredentials() {
        assertThrows(AccessException.class, () -> userCredentials.getCredentials());
    }
}
