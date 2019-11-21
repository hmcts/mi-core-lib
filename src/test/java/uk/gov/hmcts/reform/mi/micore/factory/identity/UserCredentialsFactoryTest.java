package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.UserTokenCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserCredentialsFactoryTest {

    private static final String TEST_CLIENT_ID = "clientId";
    private static final String TEST_TENANT_ID = "tenantId";
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";

    @Mock
    private AzureWrapper azureWrapper;

    @InjectMocks
    private UserCredentialsFactory userCredentialsFactory;

    @Test
    public void givenRequestForServicePrincipalCredentials_whenGetWithParameters_thenReturnSpCredentialsWithInput() {
        when(azureWrapper.getUserTokenCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_USERNAME, TEST_PASSWORD, AzureEnvironment.AZURE))
            .thenReturn(mock(UserTokenCredentials.class));

        assertNotNull(userCredentialsFactory.getCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_USERNAME, TEST_PASSWORD));
    }
}
