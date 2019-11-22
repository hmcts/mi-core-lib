package uk.gov.hmcts.reform.mi.micore.model;

import lombok.Value;

@Value
public class UserCredentialParameters {

    private String clientId;
    private String tenantId;
    private String username;
    private String password;
}
