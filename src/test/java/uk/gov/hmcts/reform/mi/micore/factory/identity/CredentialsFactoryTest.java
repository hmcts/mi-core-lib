package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.credentials.UserTokenCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.model.ApplicationCredentialParameters;
import uk.gov.hmcts.reform.mi.micore.model.UserCredentialParameters;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CredentialsFactoryTest {

    private static final String TEST_CLIENT_ID = "clientId";
    private static final String TEST_TENANT_ID = "tenantId";
    private static final String TEST_SECRET = "secret";
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";

    @Mock
    AzureWrapper azureWrapper;

    @InjectMocks
    CredentialsFactory credentialsFactory;

    @Test
    public void givenRequestForMsiCredentials_whenGet_thenReturnMsiCredentials() {
        when(azureWrapper.getMsiCredentials(AzureEnvironment.AZURE)).thenReturn(mock(MSICredentials.class));
        assertTrue(credentialsFactory.getCredentials() instanceof MSICredentials,
            "Result is not the correct Credential type.");
    }

    @Test
    public void givenRequestForServicePrincipalCredentials_whenGetWithParameters_thenReturnSpCredentials() {
        when(azureWrapper.getApplicationTokenCredentials(
            TEST_CLIENT_ID, TEST_TENANT_ID, TEST_SECRET, AzureEnvironment.AZURE)
        ).thenReturn(mock(ApplicationTokenCredentials.class));

        ApplicationCredentialParameters parameters = new ApplicationCredentialParameters(
            TEST_CLIENT_ID,
            TEST_TENANT_ID,
            TEST_SECRET
        );

        assertTrue(credentialsFactory.getCredentials(parameters) instanceof ApplicationTokenCredentials,
            "Result is not the correct Credential type.");
    }

    @Test
    public void givenRequestForUserCredentials_whenGetWithParameters_thenReturnUserCredentials() {
        when(azureWrapper.getUserTokenCredentials(
            TEST_CLIENT_ID, TEST_TENANT_ID, TEST_USERNAME, TEST_PASSWORD, AzureEnvironment.AZURE)
        ).thenReturn(mock(UserTokenCredentials.class));

        UserCredentialParameters parameters = new UserCredentialParameters(
            TEST_CLIENT_ID,
            TEST_TENANT_ID,
            TEST_USERNAME,
            TEST_PASSWORD
        );

        assertTrue(credentialsFactory.getCredentials(parameters) instanceof UserTokenCredentials,
            "Result is not the correct Credential type.");
    }
}
