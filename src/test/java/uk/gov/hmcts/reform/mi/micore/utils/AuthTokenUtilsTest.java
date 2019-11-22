package uk.gov.hmcts.reform.mi.micore.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTokenUtilsTest {

    private static final String TOKEN = "jwtToken";
    private static final String BEARER_TOKEN = "Bearer " + TOKEN;

    @Test
    public void givenBearerToken_whenStripBearerScheme_thenReturnNonBearerToken() {
        assertEquals(TOKEN, AuthTokenUtils.stripBearerScheme(BEARER_TOKEN),
            "Output has not been stripped of the Bearer prefix.");
    }

    @Test
    public void givenNonBearerToken_whenStripBearerScheme_thenReturnNonBearerToken() {
        assertEquals(TOKEN, AuthTokenUtils.stripBearerScheme(TOKEN),
            "Output has been incorrectly modified to be with Bearer prefix.");
    }

    @Test
    public void givenNonBearerToken_whenAddBearerScheme_thenReturnBearerToken() {
        assertEquals(BEARER_TOKEN, AuthTokenUtils.addBearerScheme(TOKEN),
            "Output has not been prefixed with Bearer.");
    }

    @Test
    public void givenBearerToken_whenAddBearerScheme_thenReturnBearerToken() {
        assertEquals(BEARER_TOKEN, AuthTokenUtils.addBearerScheme(BEARER_TOKEN),
            "Output has been incorrectly modified to be without Bearer prefix.");
    }
}
