package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import uk.gov.hmcts.reform.mi.micore.test.utils.FileTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreCaseDataTest {

    public static final String PUBLIC_CLASSIFICATION = "PUBLIC";
    public static final String NAME = "name";
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
    public void parseData() throws IOException {
        Map<String, Object> caseData = ImmutableMap.of(NAME, "A1",
            "subject", ImmutableMap.of("address", "a1Address"));

        Map<String, Object> classification = ImmutableMap.of(NAME, PUBLIC_CLASSIFICATION,
            "subject", ImmutableMap.of("value", Collections.emptyList(),
                "classification", PUBLIC_CLASSIFICATION));
        CoreCaseData expected = CoreCaseData.builder()
            .extractionDate("20200608-1150")
            .caseMetadataEventId(4_662_541)
            .ceCaseDataId(1_264_925)
            .ceCreatedDate(1_576_021_546_000L)
            .ceCaseTypeId("test")
            .ceCaseTypeVersion(33)
            .ceStateId("created")
            .ceStateName("stateName")
            .ceEventId("eventId")
            .ceEventName("eventName")
            .ceSummary("summary")
            .ceDescription("description")
            .ceUserId("userId")
            .ceUserFirstName("userFirstName")
            .ceUserLastName("userLastName")
            .ceData(caseData)
            .ceDataClassification(classification)
            .ceSecurityClassification(PUBLIC_CLASSIFICATION)
            .cdSecurityClassification(PUBLIC_CLASSIFICATION)
            .cdReference(1_593_043_372_164_377L)
            .cdCaseDataId(1_264_925)
            .cdData(caseData)
            .cdLatestState("stateName")
            .cdJurisdiction("jurisdiction")
            .cdLastModified(1_593_043_372_162L)
            .cdCreatedDate(1_593_043_372_161L)
            .cdLastStateModifiedDate(1_593_043_372_161L)
            .cdDataClassification(classification)
            .build();

        String dataSample = FileTestUtils.getDataFromFile("data-sample.json");
        CoreCaseData result = objectMapper.readValue(dataSample, CoreCaseData.class);

        assertEquals(expected, result);
    }

}
