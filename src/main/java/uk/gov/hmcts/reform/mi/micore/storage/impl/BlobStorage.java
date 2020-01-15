package uk.gov.hmcts.reform.mi.micore.storage.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.models.ListBlobsOptions;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        List<BlobItem> blobItemList = new ArrayList<>();

        ListBlobsOptions options = new ListBlobsOptions().setDetails(new BlobListDetails().setRetrieveMetadata(true));
        for (BlobItem blobItem : retrieveBlobContainer(client, containerName).listBlobs(options, null)) {
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

    public void uploadBlobStreamToContainer(BlobServiceClient client,
                                            String containerName,
                                            String blobName,
                                            InputStream inputStream,
                                            long streamSize) {

        getBlob(client, containerName, blobName).getBlockBlobClient().upload(inputStream, streamSize, true);
    }

    public void appendBlobStreamToBlob(BlobServiceClient client,
                                       String containerName,
                                       String blobName,
                                       InputStream inputStream,
                                       long streamSize) {

        getBlob(client, containerName, blobName).getAppendBlobClient().appendBlock(inputStream, streamSize);
    }

    public void uploadBlobFileToContainer(BlobServiceClient client,
                                          String containerName,
                                          String blobName,
                                          String inputPath) {

        getBlob(client, containerName, blobName).uploadFromFile(inputPath, true);
    }
}
