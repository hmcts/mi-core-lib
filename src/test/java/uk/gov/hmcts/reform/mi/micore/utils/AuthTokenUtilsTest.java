package uk.gov.hmcts.reform.mi.micore.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthTokenUtilsTest {

    private static final String TOKEN = "jwtToken";
    private static final String BEARER_TOKEN = "Bearer " + TOKEN;

    @Test
    void givenBearerToken_whenStripBearerScheme_thenReturnNonBearerToken() {
        assertEquals(TOKEN, AuthTokenUtils.stripBearerScheme(BEARER_TOKEN));
    }

    @Test
    void givenNonBearerToken_whenStripBearerScheme_thenReturnNonBearerToken() {
        assertEquals(TOKEN, AuthTokenUtils.stripBearerScheme(TOKEN));
    }

    @Test
    void givenNonBearerToken_whenAddBearerScheme_thenReturnBearerToken() {
        assertEquals(BEARER_TOKEN, AuthTokenUtils.addBearerScheme(TOKEN));
    }

    @Test
    void givenBearerToken_whenAddBearerScheme_thenReturnBearerToken() {
        assertEquals(BEARER_TOKEN, AuthTokenUtils.addBearerScheme(BEARER_TOKEN));
    }
}
