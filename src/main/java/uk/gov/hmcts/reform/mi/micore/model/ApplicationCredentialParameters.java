package uk.gov.hmcts.reform.mi.micore.model;

import lombok.Value;

@Value
public class ApplicationCredentialParameters {

    private String clientId;
    private String tenantId;
    private String applicationSecret;
}
