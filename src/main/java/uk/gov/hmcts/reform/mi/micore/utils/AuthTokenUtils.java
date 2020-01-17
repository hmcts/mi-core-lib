package uk.gov.hmcts.reform.mi.micore.utils;

/**
 * Utility class for Authentication Json Web Tokens.
 */
public final class AuthTokenUtils {

    private static final String BEARER_SCHEME = "Bearer ";
    private static final String EMPTY_STRING = "";

    private AuthTokenUtils() {
        // Private Constructor
    }

    public static String addBearerScheme(String authToken) {
        if (authToken.startsWith(BEARER_SCHEME)) {
            return authToken.trim();
        }

        return BEARER_SCHEME.concat(authToken).trim();
    }

    public static String stripBearerScheme(String authToken) {
        return authToken.replaceFirst(BEARER_SCHEME, EMPTY_STRING).trim();
    }
}
