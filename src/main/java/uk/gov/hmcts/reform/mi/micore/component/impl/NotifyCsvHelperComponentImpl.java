package uk.gov.hmcts.reform.mi.micore.component.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.component.CsvHelperComponent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Qualifier("notify")
public class NotifyCsvHelperComponentImpl implements CsvHelperComponent {

    @Override
    public String generateHeaderString() {
        String[] headers = {
            "extraction_date", "id", "service", "reference", "type", "status",
            "template", "created_at", "sent_at", "completed_at"
        };

        // Wrap headers in quotation marks to be consistent with data format.
        return Stream.of(headers).map(str -> "\"" + str + "\"").collect(Collectors.joining(","));
    }
}
