package uk.gov.hmcts.reform.mi.micore.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DateTimeUtilsTest {
    @Test
    void parseRightDate() {
        LocalDate localDate = LocalDate.now().withYear(2000).withMonth(1).withDayOfMonth(1);
        assertEquals("20000101", DateTimeUtils.dateToString(localDate), "Valid date parser");
    }

    @Test
    void parseRightCcdDate() {
        LocalDate localDate = LocalDate.now().withYear(2000).withMonth(1).withDayOfMonth(10);
        assertEquals(localDate, DateTimeUtils.stringToCcdDate("2000-01-10"), "Valid ccd date parser");
    }

    @Test
    void parseRightLocalDate() {
        LocalDate localDate = LocalDate.now().withYear(2000).withMonth(1).withDayOfMonth(10);
        assertEquals(localDate, DateTimeUtils.stringToLocalDate("20000110"), "Valid local date parser");
    }

    @Test
    void parseNullDate() {
        assertEquals(null, DateTimeUtils.dateToString(null), "Valid null date");
    }

    @Test
    void parseNullCcdDate() {
        assertEquals(null, DateTimeUtils.stringToCcdDate(null), "Valid null ccd date");
    }

    @Test
    void parseNullLocalDate() {
        assertEquals(null, DateTimeUtils.stringToLocalDate(null), "Valid null local date");
    }
}
