package uk.gov.hmcts.reform.mi.micore.component;

import com.azure.storage.blob.BlobServiceClient;

public interface BlobDownloadComponent<T> {

    T downloadBlob(BlobServiceClient blobServiceClient, String containerName, String blobName);
}
