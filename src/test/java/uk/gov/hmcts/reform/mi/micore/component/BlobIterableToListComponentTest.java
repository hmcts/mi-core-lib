package uk.gov.hmcts.reform.mi.micore.component;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.component.impl.BlobIterableToListComponent;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BlobIterableToListComponentTest {

    private static final String TEST_CONTAINER_NAME = "test-container";

    @InjectMocks
    private BlobIterableToListComponent underTest;

    @Test
    public void givenPagedIterableBlobItems_whenGetIterableAsList_thenReturnListOfBlobItems() {
        PagedIterable<BlobItem> blobItems = mock(PagedIterable.class);
        Iterator<BlobItem> blobIterator = mock(Iterator.class);

        doCallRealMethod().when(blobItems).forEach(any(Consumer.class));

        when(blobItems.iterator()).thenReturn(blobIterator);

        when(blobIterator.hasNext()).thenReturn(true, true, false);
        when(blobIterator.next()).thenReturn(mock(BlobItem.class), mock(BlobItem.class));

        List<BlobItem> result = underTest.getIterableAsList(blobItems);

        assertEquals(2, result.size());
    }

    @Test
    public void givenBlobServiceClientAndContainerName_whenGetContainersAsList_thenReturnListOfBlobItems() {
        BlobServiceClient blobServiceClient = mock(BlobServiceClient.class);
        BlobContainerClient blobContainerClient = mock(BlobContainerClient.class);

        when(blobServiceClient.getBlobContainerClient(TEST_CONTAINER_NAME)).thenReturn(blobContainerClient);

        PagedIterable<BlobItem> blobItems = mock(PagedIterable.class);

        when(blobContainerClient.listBlobs(argThat(options -> options.getDetails().getRetrieveMetadata()), eq(null)))
            .thenReturn(blobItems);

        Iterator<BlobItem> blobIterator = mock(Iterator.class);

        doCallRealMethod().when(blobItems).forEach(any(Consumer.class));

        when(blobItems.iterator()).thenReturn(blobIterator);

        when(blobIterator.hasNext()).thenReturn(true, true, false);
        when(blobIterator.next()).thenReturn(mock(BlobItem.class), mock(BlobItem.class));

        List<BlobItem> result = underTest.getBlobsAsList(blobServiceClient, TEST_CONTAINER_NAME);

        assertEquals(2, result.size());
    }
}
