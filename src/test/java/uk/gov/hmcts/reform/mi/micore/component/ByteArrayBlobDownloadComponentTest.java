package uk.gov.hmcts.reform.mi.micore.component;

import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.component.impl.ByteArrayBlobDownloadComponent;
import uk.gov.hmcts.reform.mi.micore.exception.AccessException;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ByteArrayBlobDownloadComponentTest {

    private static final String TEST_CONTAINER_NAME = "testContainer";
    private static final String TEST_BLOB_NAME = "testBlob";
    private static final String TEST_STRING_CONTENT = "A Test String";

    @InjectMocks
    private ByteArrayBlobDownloadComponent underTest;

    @Test
    public void givenBlobServiceClientAndContainerNameAndBlobName_whenDownloadBlob_thenReturnByteStream() {
        BlobServiceClient mockBlobServiceClient = mock(BlobServiceClient.class);

        BlobContainerClient mockBlobContainerClient = mock(BlobContainerClient.class);

        when(mockBlobServiceClient.getBlobContainerClient(TEST_CONTAINER_NAME)).thenReturn(mockBlobContainerClient);

        BlobClient mockBlobClient = mockBlobClientWithContent(TEST_STRING_CONTENT);

        when(mockBlobContainerClient.getBlobClient(TEST_BLOB_NAME)).thenReturn(mockBlobClient);

        byte[] outputInBytes = underTest.downloadBlob(mockBlobServiceClient, TEST_CONTAINER_NAME, TEST_BLOB_NAME);

        assertArrayEquals(TEST_STRING_CONTENT.getBytes(), outputInBytes);
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
