package uk.gov.hmcts.reform.mi.micore.storage;

import com.azure.core.http.rest.PagedFlux;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.factory.storage.BlobServiceClientFactory;
import uk.gov.hmcts.reform.mi.micore.storage.impl.BlobStorage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BlobStorageTest {

    private static final String TEST_STORAGE_ACCOUNT = "testAccount";
    private static final String TEST_CONTAINER_NAME = "testContainer";
    private static final String TEST_BLOB_NAME = "testBlob";

    private static final String TEST_STRING_CONTENT = "A Test String";
    private static final String TEST_OUTPUT_FILEPATH = "fileOutput";
    private static final String TEST_INPUT_FILEPATH = "fileInput";

    @Mock
    private BlobServiceClientFactory blobServiceClientFactory;

    @InjectMocks
    private BlobStorage blobStorage;

    private BlobServiceClient mockedBlobServiceClient;
    private BlobContainerClient mockedBlobContainerClient;

    @BeforeEach
    public void setupMocks() {
        mockedBlobServiceClient = mock(BlobServiceClient.class);
        mockedBlobContainerClient = mock(BlobContainerClient.class);
    }

    @Test
    public void givenBlobServiceClientAndExistingContainerName_whenGetBlobContainer_returnBlobContainer() {
        stubContainerCreation(true);

        assertNotNull(blobStorage.retrieveBlobContainer(mockedBlobServiceClient, TEST_CONTAINER_NAME));
        verify(mockedBlobContainerClient, times(0)).create();
    }

    @Test
    public void givenBlobServiceClientAndNonExistingContainerName_whenGetBlobContainer_returnBlobContainer() {
        stubContainerCreation(false);

        assertNotNull(blobStorage.retrieveBlobContainer(mockedBlobServiceClient, TEST_CONTAINER_NAME));
        verify(mockedBlobContainerClient, times(1)).create();
    }

    @Test
    public void givenBlobServiceClientAndContainerName_whenListBlobs_thenReturnListOfBlobItems() {
        stubContainerCreation(true);

        int iteratorSize = 3;
        PagedFlux<BlobItem> mockPagedFlux = mock(PagedFlux.class);
        PagedIterable<BlobItem> blobItemIterable = new PagedIterable<>(mockPagedFlux);

        when(mockPagedFlux.toIterable()).thenReturn(mockIterable(iteratorSize));
        when(mockedBlobContainerClient.listBlobs()).thenReturn(blobItemIterable);

        assertEquals(iteratorSize, blobStorage.getListOfBlobs(mockedBlobServiceClient, TEST_CONTAINER_NAME).size());
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenGetBlob_thenReturnBlobClient() {
        stubContainerCreation(true);

        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mock(BlobClient.class));

        assertNotNull(blobStorage.getBlob(mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME));
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenDownloadBlob_thenReturnByteStream() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mockBlobClientWithContent(TEST_STRING_CONTENT);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);

        byte[] outputInBytes = blobStorage.downloadBlob(mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME);

        assertArrayEquals(TEST_STRING_CONTENT.getBytes(), outputInBytes);
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenDownloadBlobToFile() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mock(BlobClient.class);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);
        when(mockedBlobClient.downloadToFile(TEST_OUTPUT_FILEPATH)).thenReturn(mock(BlobProperties.class));

        assertNotNull(blobStorage.downloadBlobToFile(mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, TEST_OUTPUT_FILEPATH));
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenUploadBlobToFile() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mock(BlobClient.class);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);

        blobStorage.uploadBlobToContainer(mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, TEST_INPUT_FILEPATH);

        verify(mockedBlobClient).uploadFromFile(TEST_INPUT_FILEPATH);
    }

    private void stubContainerCreation(boolean exists) {
        when(blobServiceClientFactory.setupBlobStorageClient(TEST_STORAGE_ACCOUNT))
            .thenReturn(mockedBlobServiceClient);
        when(mockedBlobServiceClient.getBlobContainerClient(TEST_CONTAINER_NAME)).thenReturn(mockedBlobContainerClient);
        when(mockedBlobContainerClient.exists()).thenReturn(exists);
    }

    private Iterable<BlobItem> mockIterable(int iteratorSize) {
        return () -> new Iterator<BlobItem>() {
            private int counter = 0;

            @Override
            public boolean hasNext() {
                if (counter < iteratorSize) {
                    return true;
                }
                return false;
            }

            @Override
            public BlobItem next() {
                counter = counter + 1;
                return mock(BlobItem.class);
            }
        };
    }

    private BlobClient mockBlobClientWithContent(String content) {
        return new MockBlobClient(content);
    }

    private class MockBlobClient extends BlobClient {

        private String content;

        protected MockBlobClient(String content) {
            super(mock(BlobAsyncClient.class));
            this.content = content;
        }

        @Override
        public void download(OutputStream outputStream) {
            try {
                outputStream.write(content.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                throw new AccessException(e);
            }
        }
    }
}
