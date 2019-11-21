package uk.gov.hmcts.reform.mi.micore.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTokenUtilsTest {

    private static final String TOKEN = "jwtToken";
    private static final String BEARER_TOKEN = "Bearer " + TOKEN;

    @Test
    public void givenBearerToken_whenStripBearerScheme_thenReturnNonBearerToken() {
        assertEquals(TOKEN, AuthTokenUtils.stripBearerScheme(BEARER_TOKEN));
    }

    @Test
    public void givenNonBearerToken_whenStripBearerScheme_thenReturnNonBearerToken() {
        assertEquals(TOKEN, AuthTokenUtils.stripBearerScheme(TOKEN));
    }

    @Test
    public void givenNonBearerToken_whenAddBearerScheme_thenReturnBearerToken() {
        assertEquals(BEARER_TOKEN, AuthTokenUtils.addBearerScheme(TOKEN));
    }

    @Test
    public void givenBearerToken_whenAddBearerScheme_thenReturnBearerToken() {
        assertEquals(BEARER_TOKEN, AuthTokenUtils.addBearerScheme(BEARER_TOKEN));
    }
}
