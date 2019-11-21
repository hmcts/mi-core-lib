package uk.gov.hmcts.reform.mi.micore.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccessExceptionTest {

    @Test
    public void givenAccessException_thenRuntimeExceptionIsThrown() {
        assertThrows(RuntimeException.class, () -> {
            throwException();
        });
    }

    private void throwException() {
        String[] array = new String[0];
        try {
            array[1].isEmpty();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AccessException(e);
        }
    }
}
