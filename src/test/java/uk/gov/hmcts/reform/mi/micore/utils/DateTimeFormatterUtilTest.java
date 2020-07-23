package uk.gov.hmcts.reform.mi.micore.utils;

import org.hamcrest.object.HasEqualValues;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateTimeFormatterUtilTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void testGetValidDateFormatter(String input, DateTimeFormatter expected, String dataSample) {
        assertThat( "Expected date time for " + input, expected.parse( dataSample),
            new HasEqualValues(DateTimeFormatterUtil.getDataFormat(input).parse( dataSample)));
    }

    @Test
    void testNull() {
        assertNull(DateTimeFormatterUtil.getDataFormat(null), "Expected null");
    }

    private static Stream<Arguments> argumentsProvider() {
        return Stream.of(
            Arguments.of("ISO_DATE_TIME", DateTimeFormatter.ISO_DATE_TIME, "2011-12-03T10:15:30+01:00[Europe/Paris]"),
            Arguments.of("ISO_ZONED_DATE_TIME", DateTimeFormatter.ISO_ZONED_DATE_TIME, "2011-12-03T10:15:30+01:00[Europe/Paris]"),
            Arguments.of("ISO_OFFSET_DATE_TIME", DateTimeFormatter.ISO_OFFSET_DATE_TIME, "2011-12-03T10:15:30+01:00"),
            Arguments.of("E, dd MMM yyyy HH:mm:ss z", DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss z"), "Tue, 02 Jan 2018 18:07:59 IST")
        );
    }
}
