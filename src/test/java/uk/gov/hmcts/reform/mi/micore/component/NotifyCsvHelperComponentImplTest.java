package uk.gov.hmcts.reform.mi.micore.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.component.impl.NotifyCsvHelperComponentImpl;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
class NotifyCsvHelperComponentImplTest {

    private NotifyCsvHelperComponentImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new NotifyCsvHelperComponentImpl();
    }

    @Test
    void whenGenerateHeadersForNotify_thenReturnHeadersAsString() {
        String expectedHeader = "\"extraction_date\",\"notification_id\",\"service\",\"reference\",\"type\","
                + "\"template_id\",\"template_version\",\"template_name\","
                + "\"status\",\"created_timestamp\",\"sent_timestamp\",\"completed_timestamp\"";

        assertEquals(expectedHeader, underTest.generateHeaderString());
    }
}
