package uk.gov.hmcts.reform.mi.micore.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Value;

/**
 * Base model for Notifications to be output for data storage.
 */
@Builder(toBuilder = true)
@Value
public class NotificationOutput {

    @CsvBindByName(column = "extraction_date")
    @CsvBindByPosition(position = 0)
    private String extractionDate;

    @CsvBindByName(column = "notification_id")
    @CsvBindByPosition(position = 1)
    private String notificationId;

    @CsvBindByName(column = "service")
    @CsvBindByPosition(position = 2)
    private String service;

    @CsvBindByName(column = "reference")
    @CsvBindByPosition(position = 3)
    private String reference;

    @CsvBindByName(column = "type")
    @CsvBindByPosition(position = 4)
    private String type;

    @CsvBindByName(column = "template_id")
    @CsvBindByPosition(position = 5)
    private String templateId;

    @CsvBindByName(column = "template_version")
    @CsvBindByPosition(position = 6)
    private String templateVersion;

    @CsvBindByName(column = "template_name")
    @CsvBindByPosition(position = 7)
    private String templateName;

    @CsvBindByName(column = "status")
    @CsvBindByPosition(position = 8)
    private String status;

    @CsvBindByName(column = "created_timestamp")
    @CsvBindByPosition(position = 9)
    private String createdTimestamp;

    @CsvBindByName(column = "sent_timestamp")
    @CsvBindByPosition(position = 10)
    private String sentTimestamp;

    @CsvBindByName(column = "completed_timestamp")
    @CsvBindByPosition(position = 11)
    private String completedTimestamp;
}
