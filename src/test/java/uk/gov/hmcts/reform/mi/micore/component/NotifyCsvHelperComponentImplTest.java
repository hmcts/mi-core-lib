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
        String expectedHeader = "\"extraction_date\",\"id\",\"service\",\"reference\",\"type\",\"status\","
                + "\"template\",\"created_at\",\"sent_at\",\"completed_at\"";

        assertEquals(expectedHeader, underTest.generateHeaderString());
    }
}
