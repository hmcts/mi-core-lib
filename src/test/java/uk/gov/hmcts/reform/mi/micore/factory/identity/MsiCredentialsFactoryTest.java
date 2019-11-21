package uk.gov.hmcts.reform.mi.micore.factory.identity;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.utils.AzureWrapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MsiCredentialsFactoryTest {

    @Mock
    AzureWrapper azureWrapper;

    @InjectMocks
    MsiCredentialsFactory msiCredentialsFactory;

    @Test
    public void givenRequestForMsiCredentials_whenGet_thenReturnMsiCredentials() {
        when(azureWrapper.getMSICredentials(AzureEnvironment.AZURE)).thenReturn(mock(MSICredentials.class));
        assertNotNull(msiCredentialsFactory.getCredentials());
    }
}
