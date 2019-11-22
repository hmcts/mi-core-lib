package uk.gov.hmcts.reform.mi.micore.storage.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlobStorage implements Storage {

    public BlobContainerClient retrieveBlobContainer(BlobServiceClient client, String containerName) {
        BlobContainerClient blobContainerClient = client.getBlobContainerClient(containerName);

        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }

        return blobContainerClient;
    }

    public List<BlobItem> getListOfBlobs(BlobServiceClient client, String containerName) {
        PagedIterable<BlobItem> blobItemIterable =
            retrieveBlobContainer(client, containerName).listBlobs();

        List<BlobItem> blobItemList = new ArrayList<>();

        for (BlobItem blobItem : blobItemIterable) {
            blobItemList.add(blobItem);
        }

        return blobItemList;
    }

    public BlobClient getBlob(BlobServiceClient client, String containerName, String blobName) {
        return retrieveBlobContainer(client, containerName).getBlobClient(blobName);
    }

    public byte[] downloadBlob(BlobServiceClient client, String containerName, String blobName) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            getBlob(client, containerName, blobName).download(outputStream);

            outputStream.flush();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new AccessException(e);
        }
    }

    public BlobProperties downloadBlobToFile(BlobServiceClient client,
                                             String containerName,
                                             String blobName,
                                             String outputPath) {

        return getBlob(client, containerName, blobName).downloadToFile(outputPath);
    }

    public void uploadBlobToContainer(BlobServiceClient client,
                                      String containerName,
                                      String blobName,
                                      String inputPath) {

        getBlob(client, containerName, blobName).uploadFromFile(inputPath);
    }
}
