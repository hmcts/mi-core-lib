package uk.gov.hmcts.reform.mi.micore;

import com.azure.spring.autoconfigure.storage.StorageAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Spring Boot configuration setup.
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = StorageAutoConfiguration.class)
public class MiCoreLibConfigurationContext {
}
