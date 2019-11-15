package uk.gov.hmcts.reform.mi.micore.Identity.impl;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.MSICredentials;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.Identity.Credentials;

@Component
public class ManagedIdentityCredentials implements Credentials<MSICredentials> {

    private MSICredentials credentials;

    public ManagedIdentityCredentials() {
        credentials = new MSICredentials(AzureEnvironment.AZURE);
    }

    @Override
    public MSICredentials getCredentials() {
        return credentials;
    }
}
