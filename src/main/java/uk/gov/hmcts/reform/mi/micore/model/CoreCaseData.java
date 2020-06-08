package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

/**
 * Base model for CoreCaseData read from Data Extractor output.
 */
@Builder(toBuilder = true)
@Value
@SuppressWarnings("PMD.TooManyFields")
public class CoreCaseData {

    @JsonProperty("extraction_date")
    String extractionDate;

    @JsonProperty("case_metadata_event_id")
    long caseMetadataEventId;

    @JsonProperty("ce_case_data_id")
    long ceCaseDataId;

    @JsonProperty("ce_created_date")
    long ceCreatedDate;

    @JsonProperty("ce_case_type_id")
    String ceCaseTypeId;

    @JsonProperty("ce_case_type_version")
    long ceCaseTypeVersion;

    @JsonProperty("ce_state_id")
    String ceStateId;

    @JsonProperty("ce_state_name")
    String ceStateName;

    @JsonProperty("ce_summary")
    String summary;

    @JsonProperty("ce_description")
    String description;

    @JsonProperty("ce_event_id")
    String ceEventId;

    @JsonProperty("ce_event_name")
    String ceEventName;

    @JsonProperty("ce_user_id")
    String ceUserId;

    @JsonProperty("ce_user_first_name")
    String ceUserFirstName;

    @JsonProperty("ce_user_last_name")
    String ceUserLastName;

    @JsonProperty("ce_data")
    Map<String, Object> ceData;
}
