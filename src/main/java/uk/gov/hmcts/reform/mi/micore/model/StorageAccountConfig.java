package uk.gov.hmcts.reform.mi.micore.model;

import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConstructorBinding
public class StorageAccountConfig {
    private final String accountName;
    private final String connectionString;
}
