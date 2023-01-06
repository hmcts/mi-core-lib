package uk.gov.hmcts.reform.mi.micore.component;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobContainerProperties;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.specialized.BlobInputStream;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.hmcts.reform.mi.micore.component.impl.StorageAccountWrapper;
import uk.gov.hmcts.reform.mi.micore.test.utils.ListBlobOptionMatcher;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.TooManyMethods")
class StorageAccountWrapperTest {

    private static final String ACCOUNT_NAME = "testAccount";
    private static final String CONTAINER_NAME = "testContainer";
    private static final String BLOB_NAME = "testBlob";

    @Mock
    private BlobServiceClient blobServiceClient;
    @Mock
    private BlobContainerClient blobContainerClient;
    @Mock
    private BlobClient blobClient;
    @Mock
    private BlockBlobClient blockBlobClient;
    @Mock
    private BlobContainerProperties blobContainerProperties;
    @Mock
    private BlobInputStream inputStream;

    @InjectMocks
    private StorageAccountWrapper classToTest;

    private void defaultMock() {
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME)).thenReturn(blobContainerClient);
    }

    @Test
    void testGetContainerMetadata() {
        defaultMock();
        Map<String, String> expected = Map.of("key", "value");
        when(blobContainerClient.getProperties()).thenReturn(blobContainerProperties);
        when(blobContainerProperties.getMetadata()).thenReturn(expected);

        assertEquals(expected, classToTest.getContainerMetadata(CONTAINER_NAME), "Valid metadata");
    }

    @Test
    void testSetMetadata() {
        defaultMock();
        classToTest.setMetadata(CONTAINER_NAME, "key", "value");
        verify(blobContainerClient, times(1)).setMetadata(eq(Map.of("key", "value")));
    }

    @Test
    void testCreateContainerIfNotExist() {
        defaultMock();
        when(blobContainerClient.exists()).thenReturn(false);
        classToTest.createContainerIfNotExist(CONTAINER_NAME);
        verify(blobContainerClient, times(1)).create();
    }

    @Test
    void testCreateContainerExist_thenCreateNotCall() {
        defaultMock();
        when(blobContainerClient.exists()).thenReturn(true);
        classToTest.createContainerIfNotExist(CONTAINER_NAME);
        verify(blobContainerClient, never()).create();
    }

    @Test
    void testListAllContainerBlobs() {
        defaultMock();
        PagedIterable<BlobItem> expected = mock(PagedIterable.class);
        when(blobContainerClient.listBlobs()).thenReturn(expected);
        assertEquals(expected, classToTest.listAllContainerBlobs(CONTAINER_NAME), "Valid blob list");
    }

    @Test
    void testListAllContainerBlobsWithOptions() {
        defaultMock();
        ListBlobsOptions listBlobsOptions = mock(ListBlobsOptions.class);
        PagedIterable<BlobItem> expected = mock(PagedIterable.class);
        when(blobContainerClient.listBlobs(listBlobsOptions, null)).thenReturn(expected);
        assertEquals(expected, classToTest.listAllContainerBlobs(CONTAINER_NAME, listBlobsOptions), "Valid blob list with options");
    }

    @Test
    void testListAllContainerBlobsWithMetadata() {
        defaultMock();
        PagedIterable<BlobItem> expected = mock(PagedIterable.class);
        ListBlobsOptions options = new ListBlobsOptions()
            .setDetails(new BlobListDetails()
                            .setRetrieveMetadata(true));

        when(blobContainerClient.listBlobs(argThat(new ListBlobOptionMatcher(options)), isNull())).thenReturn(expected);
        assertEquals(expected, classToTest.listAllContainerBlobsWithMetadata(CONTAINER_NAME), "Valid blob list");
    }

    @Test
    void testListAllContainerBlobsWithVersions() {
        defaultMock();
        PagedIterable<BlobItem> expected = mock(PagedIterable.class);
        ListBlobsOptions options = new ListBlobsOptions()
                .setDetails(new BlobListDetails()
                        .setRetrieveVersions(true));

        when(blobContainerClient.listBlobs(argThat(new ListBlobOptionMatcher(options)), isNull())).thenReturn(expected);
        assertEquals(expected, classToTest.listAllContainerBlobsWithVersions(CONTAINER_NAME), "Valid blob list with versions");
    }

    @Test
    void testListAllContainers() {
        PagedIterable<BlobContainerItem> expected = mock(PagedIterable.class);

        when(blobServiceClient.listBlobContainers()).thenReturn(expected);
        assertEquals(expected, classToTest.listAllContainers(), "Valid container list");
    }

    @Test
    void testDeleteBlob() {
        defaultMock();
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME)
                 .getBlobClient(BLOB_NAME)).thenReturn(blobClient);
        classToTest.deleteBlob(CONTAINER_NAME, BLOB_NAME);
        verify(blobClient, times(1)).delete();
    }

    @Test
    void testDeleteBlobVersion() {
        final String versionId = "versionId";
        defaultMock();
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME)
                .getBlobClient(BLOB_NAME)).thenReturn(blobClient);
        BlobClient versionedBlobClient = mock(BlobClient.class);
        when(blobClient.getVersionClient(versionId)).thenReturn(versionedBlobClient);
        classToTest.deleteBlobVersion(CONTAINER_NAME, BLOB_NAME, versionId);
        verify(versionedBlobClient, times(1)).delete();
    }

    @Test
    void testGetBlobInputStreamNotExist() {
        defaultMock();
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME).getBlobClient(BLOB_NAME)).thenReturn(blobClient);

        assertThrows(
            NoSuchElementException.class,
            () -> classToTest.getBlobInputStream(CONTAINER_NAME, BLOB_NAME)
        );
    }

    @Test
    void testGetBlobInputStream() {
        defaultMock();
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME).getBlobClient(BLOB_NAME)).thenReturn(blobClient);
        when(blobClient.openInputStream()).thenReturn(inputStream);
        when(blobClient.exists()).thenReturn(true);
        try (BlobInputStream getInputStream = classToTest.getBlobInputStream(CONTAINER_NAME, BLOB_NAME)) {
            assertEquals(inputStream, getInputStream, "Valid stream");
        }
    }

    @Test
    void testUploadBlockBlob() {
        defaultMock();
        final long dataLength = 100L;
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME).getBlobClient(BLOB_NAME)).thenReturn(blobClient);
        when(blobClient.getBlockBlobClient()).thenReturn(blockBlobClient);

        classToTest.uploadBlockBlob(CONTAINER_NAME, BLOB_NAME, inputStream, dataLength);

        verify(blockBlobClient, times(1)).upload(inputStream, dataLength);
    }

    @Test
    void testGetStorageAccountName() {
        when(blobServiceClient.getAccountName()).thenReturn(ACCOUNT_NAME);
        assertEquals(ACCOUNT_NAME, classToTest.getStorageAccountName(), "Valid account name");
    }

    @Test
    void testDeleteContainer() {
        defaultMock();
        when(blobServiceClient.getBlobContainerClient(CONTAINER_NAME))
            .thenReturn(blobContainerClient);
        classToTest.deleteContainer(CONTAINER_NAME);
        verify(blobContainerClient, times(1)).delete();
    }
}
