package uk.gov.hmcts.reform.mi.micore.component.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.models.StorageAccountInfo;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@EqualsAndHashCode
public class StorageAccountWrapper {

    private final BlobServiceClient blobServiceClient;

    public Map<String, String> getContainerMetadata(String containerName) {
        return blobServiceClient
            .getBlobContainerClient(containerName)
            .getProperties()
            .getMetadata();
    }

    public void setMetadata(String containerName, String key, String value) {
        blobServiceClient
            .getBlobContainerClient(containerName)
            .setMetadata(Map.of(key, value));
    }

    public void createContainerIfNotExist(String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    public PagedIterable<BlobItem> listAllContainerBlobs(String containerName) {
        return blobServiceClient.getBlobContainerClient(containerName).listBlobs();
    }

    public PagedIterable<BlobItem> listAllContainerBlobsWithMetadata(String containerName) {
        ListBlobsOptions options = new ListBlobsOptions();
        BlobListDetails blobListDetails = new BlobListDetails();
        blobListDetails.setRetrieveMetadata(true);
        options.setDetails(blobListDetails);
        return blobServiceClient.getBlobContainerClient(containerName).listBlobs(options, null);
    }

    public PagedIterable<BlobContainerItem> listAllContainers() {
        return blobServiceClient.listBlobContainers();
    }

    public void deleteBlob(String containerName, String blobName) {
        blobServiceClient.getBlobContainerClient(containerName)
            .getBlobClient(blobName)
            .delete();
    }

    public boolean existBlob(String containerName, String blobName) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
            .getBlobClient(blobName);
        return blobClient.exists();
    }

    public InputStream getBlockInputStream(String containerName, String blobName) {
        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName)
            .getBlobClient(blobName);
        if (!blobClient.exists()) {
            throw new NoSuchElementException(String.format("Blob [%s] not exist", blobName));
        }

        return blobClient.openInputStream();
    }

    public void uploadBlockBlob(String targetContainer, String targetName, InputStream data, long length) {
        blobServiceClient.getBlobContainerClient(targetContainer)
            .getBlobClient(targetName)
            .getBlockBlobClient()
            .upload(data, length);
    }

    public String getStorageAccountName() {
        return blobServiceClient.getAccountName();
    }

    public StorageAccountInfo getAccountInfo() {
        return blobServiceClient.getAccountInfo();
    }
}
