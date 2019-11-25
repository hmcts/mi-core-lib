package uk.gov.hmcts.reform.mi.micore.storage;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;
import uk.gov.hmcts.reform.mi.micore.storage.impl.CloudBlobStorage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CloudBlobStorageTest {

    private static final String TEST_CONTAINER_NAME = "testContainer";
    private static final String TEST_BLOB_NAME = "testBlob";

    private static final String TEST_STRING_CONTENT = "A Test String";
    private static final String TEST_OUTPUT_FILEPATH = "fileOutput";
    private static final String TEST_INPUT_FILEPATH = "fileInput";

    @InjectMocks
    private CloudBlobStorage blobStorage;

    private CloudBlobClient mockedCloudBlobClient;
    private CloudBlobContainer mockedCloudBlobContainer;

    @BeforeEach
    public void setupMocks() {
        mockedCloudBlobClient = mock(CloudBlobClient.class);
        mockedCloudBlobContainer = mock(CloudBlobContainer.class);
    }

    @Test
    public void givenCloudBlobClientAndExistingContainerName_whenGetBlobContainer_returnBlobContainer()
        throws Exception {

        stubContainerCreation(true);

        assertNotNull(blobStorage.retrieveBlobContainer(mockedCloudBlobClient, TEST_CONTAINER_NAME),
            "Unable to retrieve blob container.");
        verify(mockedCloudBlobContainer, times(0)).create();
    }

    @Test
    public void givenCloudBlobClientAndNonExistingContainerName_whenGetBlobContainer_returnBlobContainer()
        throws Exception {

        stubContainerCreation(false);

        assertNotNull(blobStorage.retrieveBlobContainer(mockedCloudBlobClient, TEST_CONTAINER_NAME),
            "Unable to retrieve blob container.");
        verify(mockedCloudBlobContainer, times(1)).create();
    }

    @Test
    public void givenCloudBlobClientAndContainerName_whenListBlobs_thenReturnListOfBlobItems() throws Exception {
        stubContainerCreation(true);

        int iteratorSize = 3;
        Iterable<ListBlobItem> blobItemIterable = mockIterable(iteratorSize);

        when(mockedCloudBlobContainer.listBlobs()).thenReturn(blobItemIterable);

        int blobListSize = blobStorage.getListOfBlobs(mockedCloudBlobClient, TEST_CONTAINER_NAME).size();

        assertEquals(iteratorSize, blobListSize,
            "Blob list returned does not match the expected blob list size.");
    }

    @Test
    public void givenCloudBlobClientAndContainerNameAndBlobName_whenGetBlob_thenReturnBlobClient() throws Exception {
        stubContainerCreation(true);

        when(mockedCloudBlobContainer.getBlockBlobReference(TEST_BLOB_NAME)).thenReturn(mock(CloudBlockBlob.class));

        assertNotNull(blobStorage.getBlob(mockedCloudBlobClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME),
            "Unable to retrieve blob item.");
    }

    @Test
    public void givenCloudBlobClientAndContainerNameAndBlobName_whenDownloadBlob_thenReturnByteStream()
        throws Exception {

        stubContainerCreation(true);

        CloudBlockBlob mockedBlockBlob = mock(CloudBlockBlob.class);
        doAnswer((Answer<Void>) invocation -> {
            OutputStream outputStream = invocation.getArgument(0);
            try {
                outputStream.write(TEST_STRING_CONTENT.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                throw new AccessException(e);
            }
            return null;
        }).when(mockedBlockBlob).download(any(OutputStream.class));
        when(mockedCloudBlobContainer.getBlockBlobReference(TEST_BLOB_NAME)).thenReturn(mockedBlockBlob);

        byte[] outputInBytes = blobStorage.downloadBlob(mockedCloudBlobClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME);

        assertArrayEquals(TEST_STRING_CONTENT.getBytes(), outputInBytes,
            "Output bytes does not match expected output stream.");
    }

    @Test
    public void givenCloudBlobClientAndContainerNameAndBlobName_whenDownloadBlobToFile_thenDownloadFile()
        throws Exception {

        stubContainerCreation(true);

        CloudBlockBlob mockedBlockBlob = mock(CloudBlockBlob.class);
        when(mockedCloudBlobContainer.getBlockBlobReference(TEST_BLOB_NAME)).thenReturn(mockedBlockBlob);

        blobStorage.downloadBlobToFile(
            mockedCloudBlobClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, TEST_OUTPUT_FILEPATH);

        verify(mockedBlockBlob).downloadToFile(TEST_OUTPUT_FILEPATH);
    }

    @Test
    public void givenCloudBlobClientAndContainerNameAndBlobName_whenUploadBlobToFile_thenDownloadFile()
        throws Exception {

        stubContainerCreation(true);

        CloudBlockBlob mockedBlockBlob = mock(CloudBlockBlob.class);
        when(mockedCloudBlobContainer.getBlockBlobReference(TEST_BLOB_NAME)).thenReturn(mockedBlockBlob);

        blobStorage.uploadBlobToContainer(
            mockedCloudBlobClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME, TEST_INPUT_FILEPATH);

        verify(mockedBlockBlob).uploadFromFile(TEST_INPUT_FILEPATH);
    }

    private void stubContainerCreation(boolean exists) throws Exception {
        when(mockedCloudBlobClient.getContainerReference(TEST_CONTAINER_NAME)).thenReturn(mockedCloudBlobContainer);
        when(mockedCloudBlobContainer.exists()).thenReturn(exists);
    }

    private Iterable<ListBlobItem> mockIterable(int iteratorSize) {
        return () -> new Iterator<>() {
            int counter;

            @Override
            public boolean hasNext() {
                if (counter < iteratorSize) {
                    return true;
                }
                return false;
            }

            @Override
            public ListBlobItem next() {
                counter = counter + 1;
                return mock(CloudBlockBlob.class);
            }
        };
    }
}
