package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@SuppressWarnings("PMD.TooManyFields")
@Builder(toBuilder = true)
@Value
public class NotificationOutput {

    @JsonProperty("extraction_date")
    private String extractionDate;

    @JsonProperty("id")
    private String id;

    @JsonProperty("service")
    private String service;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("line_1")
    private String lineOne;

    @JsonProperty("line_2")
    private String lineTwo;

    @JsonProperty("line_3")
    private String lineThree;

    @JsonProperty("line_4")
    private String lineFour;

    @JsonProperty("line_5")
    private String lineFive;

    @JsonProperty("line_6")
    private String lineSix;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private String status;

    @JsonProperty("template")
    private NotifyTemplate template;

    @JsonProperty("body")
    private String body;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("sent_at")
    private String sentAt;

    @JsonProperty("completed_at")
    private String completedAt;
}
