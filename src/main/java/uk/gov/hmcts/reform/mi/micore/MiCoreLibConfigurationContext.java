package uk.gov.hmcts.reform.mi.micore;

import com.azure.spring.autoconfigure.storage.StorageAutoConfiguration;
import com.microsoft.applicationinsights.autoconfigure.ApplicationInsightsWebMvcAutoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Spring Boot configuration setup.
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = { StorageAutoConfiguration.class, ApplicationInsightsWebMvcAutoConfiguration.class })
public class MiCoreLibConfigurationContext {
}
