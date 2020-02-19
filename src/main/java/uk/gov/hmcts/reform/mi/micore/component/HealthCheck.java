package uk.gov.hmcts.reform.mi.micore.component;

import uk.gov.hmcts.reform.mi.micore.exception.ServiceNotAvailableException;

public interface HealthCheck {
    void check() throws ServiceNotAvailableException;
}
