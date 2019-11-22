package uk.gov.hmcts.reform.mi.micore.identity.impl;

import com.microsoft.azure.credentials.MSICredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.factory.identity.CredentialsFactory;
import uk.gov.hmcts.reform.mi.micore.identity.Credentials;

import java.util.Objects;

/**
 * Azure managed identity credentials.
 * For automated authentication on Azure with no need to pass in any additional parameters.
 */
@Component
public class ManagedIdentityCredentials implements Credentials<MSICredentials> {

    private final CredentialsFactory credentialsFactory;

    private MSICredentials credentials;

    /**
     * Constructor. Setup dependencies.
     *
     * @param credentialsFactory The factory class to determine setup methodology.
     */
    @Autowired
    public ManagedIdentityCredentials(CredentialsFactory credentialsFactory) {
        this.credentialsFactory = credentialsFactory;
    }

    /**
     * Retrieve credentials based on the current application environment.
     *
     * @return credentials object for authentication.
     */
    @Override
    public MSICredentials getCredentials() {
        if (Objects.isNull(credentials)) {
            credentials = credentialsFactory.getCredentials();
        }

        return credentials;
    }

    public void refreshCredentials() {
        credentials = credentialsFactory.getCredentials();
    }
}
