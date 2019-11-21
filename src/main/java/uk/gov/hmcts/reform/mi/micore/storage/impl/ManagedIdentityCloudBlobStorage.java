package uk.gov.hmcts.reform.mi.micore.storage.impl;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.identity.impl.ManagedIdentityCredentials;
import uk.gov.hmcts.reform.mi.micore.storage.Storage;
import uk.gov.hmcts.reform.mi.micore.utils.AzureClientHelper;

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
public class ManagedIdentityCloudBlobStorage implements Storage {

    private AzureClientHelper azureClientHelper;
    private ManagedIdentityCredentials managedIdentityCredentials;

    @Autowired
    public ManagedIdentityCloudBlobStorage(AzureClientHelper azureClientHelper,
                                           ManagedIdentityCredentials managedIdentityCredentials) {
       this.azureClientHelper = azureClientHelper;
       this.managedIdentityCredentials = managedIdentityCredentials;
    }

    public CloudBlobClient retrieveBlobServiceClient(String storageAccountName) {
        String accessToken = azureClientHelper.getStorageAccessToken(managedIdentityCredentials.getCredentials());

        return azureClientHelper.getCloudBlobClient(storageAccountName, accessToken);
    }

    public CloudBlobContainer retrieveBlobContainer(String storageAccountName, String containerName) {
        try {
            CloudBlobContainer blobContainerClient =
                retrieveBlobServiceClient(storageAccountName).getContainerReference(containerName);

            if (!blobContainerClient.exists()) {
                blobContainerClient.create();
            }

            return blobContainerClient;
        } catch (URISyntaxException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public List<CloudBlockBlob> getListOfBlobs(String storageAccountName, String containerName) {
        Iterable<ListBlobItem> blobItemIterable =
            retrieveBlobContainer(storageAccountName, containerName).listBlobs();

        List<CloudBlockBlob> blobItemList = new ArrayList<>();

        for (ListBlobItem blobItem : blobItemIterable) {
            blobItemList.add((CloudBlockBlob) blobItem);
        }

        return blobItemList;
    }

    public CloudBlockBlob getBlob(String storageAccountName, String containerName, String blobName) {
        try {
            return retrieveBlobContainer(storageAccountName, containerName).getBlockBlobReference(blobName);
        } catch (URISyntaxException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public byte[] downloadBlob(String storageAccountName, String containerName, String blobName) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            getBlob(storageAccountName, containerName, blobName).download(outputStream);

            outputStream.flush();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (IOException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public void downloadBlobToFile(String storageAccountName, String containerName, String blobName, String outputPath) {
        try {
            getBlob(storageAccountName, containerName, blobName).downloadToFile(outputPath);
        } catch (IOException | StorageException e) {
            throw new AccessException(e);
        }
    }

    public void uploadBlobToContainer(String storageAccountName, String containerName, String blobName, String inputPath) {
        try {
            getBlob(storageAccountName, containerName, blobName).uploadFromFile(inputPath);
        } catch (IOException | StorageException e) {
            throw new AccessException(e);
        }
    }
}
