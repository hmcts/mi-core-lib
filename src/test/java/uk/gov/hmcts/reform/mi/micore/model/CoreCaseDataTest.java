package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreCaseDataTest {

    private static final String DATA_SAMPLE = "{\n"
        + "\t\"extraction_date\": \"20200608-1150\",\n"
        + "\t\"case_metadata_event_id\": 1,\n"
        + "\t\"ce_case_data_id\": 1,\n"
        + "\t\"ce_created_date\": 1576021546000,\n"
        + "\t\"ce_case_type_id\": \"test\",\n"
        + "\t\"ce_case_type_version\": \"1\",\n"
        + "\t\"ce_state_id\": \"created\",\n"
        + "\t\"ce_state_name\": \"stateName\",\n"
        + "\t\"ce_event_id\": \"eventId\",\n"
        + "\t\"ce_event_name\": \"eventName\",\n"
        + "\t\"ce_summary\": \"summary\",\n"
        + "\t\"ce_description\": \"description\",\n"
        + "\t\"ce_user_id\": \"userId\",\n"
        + "\t\"ce_user_first_name\": \"userFirstName\",\n"
        + "\t\"ce_user_last_name\": \"userLastName\",\n"
        + "\t\"ce_data\": {\n"
        + "\t\t\"name\": \"A1\",\n"
        + "\t\t\"subject\": {\n"
        + "\t\t\t\"address\": \"a1Address\"\n"
        + "\t\t}\n"
        + "\t}\n"
        + "}";

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SuppressWarnings("PMD.UnnecessaryFullyQualifiedName")
    public void parseData() throws JsonProcessingException {
        CoreCaseData expected = CoreCaseData.builder()
            .extractionDate("20200608-1150")
            .caseMetadataEventId(1)
            .ceCaseDataId(1)
            .ceCreatedDate(1_576_021_546_000L)
            .ceCaseTypeId("test")
            .ceCaseTypeVersion(1)
            .ceStateId("created")
            .ceStateName("stateName")
            .ceEventId("eventId")
            .ceEventName("eventName")
            .summary("summary")
            .description("description")
            .ceUserId("userId")
            .ceUserFirstName("userFirstName")
            .ceUserLastName("userLastName")
            .ceData(ImmutableMap.of("name", "A1",
                "subject", ImmutableMap.of("address", "a1Address")))
            .build();
        CoreCaseData result = objectMapper.readValue(DATA_SAMPLE, CoreCaseData.class);
        assertEquals(expected, result);
    }

}
