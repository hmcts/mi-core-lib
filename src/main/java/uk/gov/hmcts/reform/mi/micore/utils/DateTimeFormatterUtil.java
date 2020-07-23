package uk.gov.hmcts.reform.mi.micore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeFormatterUtil {

    public static DateTimeFormatter getDataFormat(String format) {
        if (StringUtils.isBlank(format)) {
            return null;
        }
        switch (format.toUpperCase(Locale.UK)) {
            case "ISO_ZONED_DATE_TIME": return DateTimeFormatter.ISO_ZONED_DATE_TIME;
            case "ISO_DATE_TIME": return DateTimeFormatter.ISO_DATE_TIME;
            case "ISO_OFFSET_DATE_TIME": return DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            default: return DateTimeFormatter.ofPattern(format);
        }
    }

}
