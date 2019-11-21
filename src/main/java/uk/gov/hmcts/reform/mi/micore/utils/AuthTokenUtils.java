package uk.gov.hmcts.reform.mi.micore.utils;

public class AuthTokenUtils {

    private final static String BEARER_SCHEME = "Bearer ";
    private final static String EMPTY_STRING = "";

    public static String stripBearerScheme(String authToken) {
        return authToken.replaceFirst(BEARER_SCHEME, EMPTY_STRING).trim();
    }

    public static String addBearerScheme(String authToken) {
        if (authToken.startsWith(BEARER_SCHEME)) {
            return authToken.trim();
        }

        return BEARER_SCHEME.concat(authToken).trim();
    }
}
