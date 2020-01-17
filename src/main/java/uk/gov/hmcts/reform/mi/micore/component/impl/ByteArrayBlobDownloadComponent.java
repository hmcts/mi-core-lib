package uk.gov.hmcts.reform.mi.micore.component.impl;

import com.azure.storage.blob.BlobServiceClient;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.component.BlobDownloadComponent;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ByteArrayBlobDownloadComponent implements BlobDownloadComponent<byte[]> {

    @Override
    public byte[] downloadBlob(BlobServiceClient blobServiceClient, String containerName, String blobName) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(blobName)
                .download(outputStream);

            outputStream.flush();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new AccessException(e);
        }
    }
}
