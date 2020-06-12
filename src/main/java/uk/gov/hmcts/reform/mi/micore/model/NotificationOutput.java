package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class NotificationOutput {

    @CsvBindByPosition(position = 0)
    @JsonProperty("extraction_date")
    private String extractionDate;

    @CsvBindByPosition(position = 1)
    @JsonProperty("id")
    private String id;

    @CsvBindByPosition(position = 2)
    @JsonProperty("service")
    private String service;

    @CsvBindByPosition(position = 3)
    @JsonProperty("reference")
    private String reference;

    @CsvBindByPosition(position = 4)
    @JsonProperty("type")
    private String type;

    @CsvBindByPosition(position = 5)
    @JsonProperty("status")
    private String status;

    @CsvBindByPosition(position = 6)
    @JsonProperty("template")
    private NotifyTemplate template;

    @CsvBindByPosition(position = 7)
    @JsonProperty("created_at")
    private String createdAt;

    @CsvBindByPosition(position = 8)
    @JsonProperty("sent_at")
    private String sentAt;

    @CsvBindByPosition(position = 9)
    @JsonProperty("completed_at")
    private String completedAt;
}
