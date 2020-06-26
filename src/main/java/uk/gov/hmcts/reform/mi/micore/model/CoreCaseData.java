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
    String ceSummary;

    @JsonProperty("ce_description")
    String ceDescription;

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

    @JsonProperty("cd_case_data_id")
    long cdCaseDataId;

    @JsonProperty("cd_created_date")
    long cdCreatedDate;

    @JsonProperty("cd_last_modified")
    long cdLastModified;

    @JsonProperty("cd_jurisdiction")
    String cdJurisdiction;

    @JsonProperty("cd_latest_state")
    String cdLatestState;

    @JsonProperty("cd_reference")
    long cdReference;

    @JsonProperty("cd_security_classification")
    String cdSecurityClassification;

    @JsonProperty("cd_version")
    long cdVersion;

    @JsonProperty("cd_data")
    Map<String, Object> cdData;

    @JsonProperty("cd_last_state_modified_date")
    long cdLastStateModifiedDate;

    @JsonProperty("cd_data_classification")
    Map<String, Object> cdDataClassification;

    @JsonProperty("ce_data_classification")
    Map<String, Object> ceDataClassification;

    @JsonProperty("ce_security_classification")
    String ceSecurityClassification;
}
