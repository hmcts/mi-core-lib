package uk.gov.hmcts.reform.mi.micore.utils;

import org.apache.commons.lang3.StringUtils;

import uk.gov.hmcts.reform.mi.micore.model.StorageAccountConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtils {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    static final DateTimeFormatter CCD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * date format yyyyMMdd.
     *
     * @param date LocalDate to parse
     * @return
     */
    public static String dateToString(LocalDate date) {
        return date == null ? null : DATE_FORMATTER.format(date);
    }

    /**
     * Local date format yyyyMMdd.
     *
     * @param date Date in string format
     * @return
     */
    public static LocalDate stringToLocalDate(String date) {
        return StringUtils.isNotBlank(date) ? LocalDate.parse(date, DATE_FORMATTER) : null;
    }

    /**
     * String to CCD data format.
     *
     * @param date Date in ccd string format
     * @return
     */
    public static LocalDate stringToCcdDate(String date) {
        return StringUtils.isNotBlank(date) ? LocalDate.parse(date, CCD_DATE_FORMATTER) : null;
    }

    private DateTimeUtils() {
        new StorageAccountConfig("","");
    }
}
