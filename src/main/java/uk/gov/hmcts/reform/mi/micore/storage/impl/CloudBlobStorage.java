package uk.gov.hmcts.reform.mi.micore.storage.impl;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This uses Azure Storage SDK v8 which is potentially more stable as of the current date: 18-11-2019.
 *
 * @deprecated Use ManagedIdentityBlobStorageAccount for SDK v12.
 */
@Deprecated
@Component
public class CloudBlobStorage implements Storage {

    public CloudBlobContainer retrieveBlobContainer(CloudBlobClient cloudBlobClient, String containerName) {

        try {
            CloudBlobContainer blobContainerClient =
                cloudBlobClient.getContainerReference(containerName);

            if (!blobContainerClient.exists()) {
                blobContainerClient.create();
            }

            return blobContainerClient;
        } catch (URISyntaxException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public List<CloudBlockBlob> getListOfBlobs(CloudBlobClient cloudBlobClient, String containerName) {
        Iterable<ListBlobItem> blobItemIterable =
            retrieveBlobContainer(cloudBlobClient, containerName).listBlobs();

        List<CloudBlockBlob> blobItemList = new ArrayList<>();

        for (ListBlobItem blobItem : blobItemIterable) {
            blobItemList.add((CloudBlockBlob) blobItem);
        }

        return blobItemList;
    }

    public CloudBlockBlob getBlob(CloudBlobClient cloudBlobClient, String containerName, String blobName) {
        try {
            return retrieveBlobContainer(cloudBlobClient, containerName).getBlockBlobReference(blobName);
        } catch (URISyntaxException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public byte[] downloadBlob(CloudBlobClient cloudBlobClient, String containerName, String blobName) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            getBlob(cloudBlobClient, containerName, blobName).download(outputStream);

            outputStream.flush();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (IOException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public void downloadBlobToFile(CloudBlobClient cloudBlobClient,
                                   String containerName,
                                   String blobName,
                                   String outputPath) {

        try {
            getBlob(cloudBlobClient, containerName, blobName).downloadToFile(outputPath);
        } catch (IOException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public void uploadBlobToContainer(CloudBlobClient cloudBlobClient,
                                      String containerName,
                                      String blobName,
                                      String inputPath) {

        try {
            getBlob(cloudBlobClient, containerName, blobName).uploadFromFile(inputPath);
        } catch (IOException | StorageException e) {
            throw new AccessException(e);
        }
    }
}
