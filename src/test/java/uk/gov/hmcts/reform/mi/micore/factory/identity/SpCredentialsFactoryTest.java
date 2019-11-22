package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.ApplicationTokenCredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SpCredentialsFactoryTest {

    private static final String TEST_CLIENT_ID = "clientId";
    private static final String TEST_TENANT_ID = "tenantId";
    private static final String TEST_SECRET = "secret";

    @Mock
    private AzureWrapper azureWrapper;

    @InjectMocks
    private SpCredentialsFactory spCredentialsFactory;

    @Test
    public void givenRequestForServicePrincipalCredentials_whenGetWithParameters_thenReturnSpCredentialsWithInput() {
        when(azureWrapper.getApplicationTokenCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_SECRET, AzureEnvironment.AZURE))
            .thenReturn(mock(ApplicationTokenCredentials.class));

        assertNotNull(spCredentialsFactory.getCredentials(TEST_CLIENT_ID, TEST_TENANT_ID, TEST_SECRET));
    }
}
