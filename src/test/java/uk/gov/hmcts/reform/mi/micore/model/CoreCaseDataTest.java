package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.hmcts.reform.mi.micore.parser.MiDateDeserializer;
import uk.gov.hmcts.reform.mi.micore.test.utils.FileTestUtils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreCaseDataTest {

    public static final String PUBLIC_CLASSIFICATION = "PUBLIC";
    public static final String NAME = "name";

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        SimpleModule module = new SimpleModule("Data serializer");
        module.addDeserializer(OffsetDateTime.class, new MiDateDeserializer());
        objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(module)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
    public void parseData() throws IOException {
        Map<String, Object> caseData = ImmutableMap.of(NAME, "A1",
            "subject", ImmutableMap.of("address", "a1Address"));

        Map<String, Object> classification = ImmutableMap.of(NAME, PUBLIC_CLASSIFICATION,
            "subject", ImmutableMap.of("value", Collections.emptyList(),
                "classification", PUBLIC_CLASSIFICATION));


        final OffsetDateTime dateTime = OffsetDateTime.parse("2020-07-01T11:58:43.737Z");

        CoreCaseData expected = CoreCaseData.builder()
            .extractionDate("20200608-1150")
            .ceId(4_662_541L)
            .ceCaseDataId(1_264_925L)
            .ceCreatedDate(dateTime)
            .ceCaseTypeId("test")
            .ceCaseTypeVersion(33L)
            .ceStateId("created")
            .ceStateName("stateName")
            .ceEventId("eventId")
            .ceData(caseData)
            .ceDataClassification(classification)
            .ceEventName("eventName")
            .ceSummary("summary")
            .ceDescription("description")
            .ceUserId("userId")
            .ceUserFirstName("userFirstName")
            .ceUserLastName("userLastName")
            .ceSecurityClassification(PUBLIC_CLASSIFICATION)
            .cdSecurityClassification(PUBLIC_CLASSIFICATION)
            .cdReference(1_593_043_372_164_377L)
            .cdVersion(2L)
            .cdLatestState("stateName")
            .cdJurisdiction("jurisdiction")
            .cdLastModified(dateTime)
            .cdCreatedDate(dateTime)
            .cdLastStateModifiedDate(dateTime)
            .build();

        String dataSample = FileTestUtils.getDataFromFile("data-sample.json");
        CoreCaseData result = objectMapper.readValue(dataSample, CoreCaseData.class);

        assertEquals(expected, result);
    }

}
