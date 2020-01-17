package uk.gov.hmcts.reform.mi.micore.utils;

import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.stereotype.Component;

/**
 * Wrapper class on Azure functionality to assist with modularity and testing.
 */
@Component
public class AzureWrapper {

    public BlobServiceClientBuilder getBlobServiceClientBuilder() {
        return new BlobServiceClientBuilder();
    }
}
