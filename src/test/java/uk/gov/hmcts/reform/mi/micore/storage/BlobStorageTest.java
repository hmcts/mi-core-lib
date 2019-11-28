package uk.gov.hmcts.reform.mi.micore.storage;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import com.azure.storage.blob.specialized.AppendBlobClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.storage.impl.BlobStorage;

import java.io.ByteArrayInputStream;
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

@SuppressWarnings("PMD.TooManyMethods")
@SpringBootTest
public class BlobStorageTest {

    private static final String TEST_CONTAINER_NAME = "testContainer";
    private static final String TEST_BLOB_NAME = "testBlob";

    private static final String TEST_STRING_CONTENT = "A Test String";
    private static final String TEST_OUTPUT_FILEPATH = "fileOutput";
    private static final String TEST_INPUT_FILEPATH = "fileInput";

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

        assertNotNull(blobStorage.retrieveBlobContainer(mockedBlobServiceClient, TEST_CONTAINER_NAME),
            "Unable to retrieve blob container.");
        verify(mockedBlobContainerClient, times(0)).create();
    }

    @Test
    public void givenBlobServiceClientAndNonExistingContainerName_whenGetBlobContainer_returnBlobContainer() {
        stubContainerCreation(false);

        assertNotNull(blobStorage.retrieveBlobContainer(mockedBlobServiceClient, TEST_CONTAINER_NAME),
            "Unable to retrieve blob container.");
        verify(mockedBlobContainerClient, times(1)).create();
    }

    @Test
    public void givenBlobServiceClientAndContainerName_whenListBlobs_thenReturnListOfBlobItems() {
        stubContainerCreation(true);

        PagedIterable<BlobItem> mockIterable = mock(PagedIterable.class);
        Iterator<BlobItem> mockIterator = mock(Iterator.class);

        when(mockIterable.iterator()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(true, true, true, false);
        when(mockIterator.next()).thenReturn(mock(BlobItem.class));

        when(mockedBlobContainerClient.listBlobs()).thenReturn(mockIterable);

        int blobListSize = blobStorage.getListOfBlobs(mockedBlobServiceClient, TEST_CONTAINER_NAME).size();

        assertEquals(3, blobListSize,
            "Blob list returned does not match the expected blob list size.");
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenGetBlob_thenReturnBlobClient() {
        stubContainerCreation(true);

        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mock(BlobClient.class));

        assertNotNull(blobStorage.getBlob(mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME),
            "Unable to retrieve blob item.");
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenDownloadBlob_thenReturnByteStream() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mockBlobClientWithContent(TEST_STRING_CONTENT);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);

        byte[] outputInBytes = blobStorage.downloadBlob(mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME);

        assertArrayEquals(TEST_STRING_CONTENT.getBytes(), outputInBytes,
            "Output bytes does not match expected output stream.");
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenDownloadBlobToFile_thenDownloadFile() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mock(BlobClient.class);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);
        when(mockedBlobClient.downloadToFile(TEST_OUTPUT_FILEPATH)).thenReturn(mock(BlobProperties.class));

        assertNotNull(blobStorage.downloadBlobToFile(
            mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, TEST_OUTPUT_FILEPATH),
            "Blob properties from downloadToFile has not been returned.");
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenUploadBlobStream_thenVerifyUpload() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mock(BlobClient.class);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);

        BlockBlobClient mockedBlockBlobClient = mock(BlockBlobClient.class);
        when(mockedBlobClient.getBlockBlobClient()).thenReturn(mockedBlockBlobClient);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_STRING_CONTENT.getBytes());
        long streamSize = TEST_STRING_CONTENT.getBytes().length;

        blobStorage.uploadBlobStreamToContainer(
            mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, inputStream, streamSize);

        verify(mockedBlockBlobClient).upload(inputStream, streamSize);
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenAppendBlobStream_thenVerifyUpload() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mock(BlobClient.class);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);

        AppendBlobClient mockedAppendBlobClient = mock(AppendBlobClient.class);
        when(mockedBlobClient.getAppendBlobClient()).thenReturn(mockedAppendBlobClient);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_STRING_CONTENT.getBytes());
        long streamSize = TEST_STRING_CONTENT.getBytes().length;

        blobStorage.appendBlobStreamToBlob(
            mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, inputStream, streamSize);

        verify(mockedAppendBlobClient).appendBlock(inputStream, streamSize);
    }

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenUploadFileBlobToFile_thenVerifyUpload() {
        stubContainerCreation(true);

        BlobClient mockedBlobClient = mock(BlobClient.class);
        when(mockedBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockedBlobClient);

        blobStorage.uploadBlobFileToContainer(
            mockedBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, TEST_INPUT_FILEPATH);

        verify(mockedBlobClient).uploadFromFile(TEST_INPUT_FILEPATH);
    }

    private void stubContainerCreation(boolean exists) {
        when(mockedBlobServiceClient.getBlobContainerClient(TEST_CONTAINER_NAME)).thenReturn(mockedBlobContainerClient);
        when(mockedBlobContainerClient.exists()).thenReturn(exists);
    }

    private BlobClient mockBlobClientWithContent(String content) {
        return new MockBlobClient(content);
    }

    private class MockBlobClient extends BlobClient {

        private final String content;

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
