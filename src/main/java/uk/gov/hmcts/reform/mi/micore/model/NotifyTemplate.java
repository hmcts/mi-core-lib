package uk.gov.hmcts.reform.mi.micore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class NotifyTemplate {

    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private String version;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("name")
    private String name;
}
