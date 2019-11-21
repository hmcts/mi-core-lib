package uk.gov.hmcts.reform.mi.micore.identity.impl;

import com.microsoft.azure.credentials.MSICredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.factory.identity.MsiCredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.Credentials;

/**
 * Azure managed identity credentials.
 * For automated authentication on Azure with no need to pass in any additional parameters.
 */
@Component
public class ManagedIdentityCredentials implements Credentials<MSICredentials> {

    private MsiCredentialsFactory msiCredentialsFactory;

    /**
     * Constructor. Setup dependencies.
     *
     * @param msiCredentialsFactory The factory class to determine setup methodology.
     */
    @Autowired
    public ManagedIdentityCredentials(MsiCredentialsFactory msiCredentialsFactory) {
        this.msiCredentialsFactory = msiCredentialsFactory;
    }

    /**
     * Retrieve credentials based on the current application environment.
     *
     * @return credentials object for authentication.
     */
    @Override
    public MSICredentials getCredentials() {
        return msiCredentialsFactory.getCredentials();
    }
}
