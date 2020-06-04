package uk.gov.hmcts.reform.mi.micore.component;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.mi.micore.component.impl.ContainerIterableToListComponent;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ContainerIterableToListComponentTest {

    private ContainerIterableToListComponent underTest;

    @BeforeEach
    void setUp() {
        underTest = new ContainerIterableToListComponent();
    }

    @Test
    void givenPagedIterableContainerItems_whenGetIterableAsList_thenReturnListOfContainerItems() {
        PagedIterable<BlobContainerItem> blobContainerItems = mock(PagedIterable.class);
        Iterator<BlobContainerItem> blobContainerIterator = mock(Iterator.class);

        doCallRealMethod().when(blobContainerItems).forEach(any(Consumer.class));

        when(blobContainerItems.iterator()).thenReturn(blobContainerIterator);

        when(blobContainerIterator.hasNext()).thenReturn(true, true, false);
        when(blobContainerIterator.next()).thenReturn(mock(BlobContainerItem.class), mock(BlobContainerItem.class));

        List<BlobContainerItem> result = underTest.getIterableAsList(blobContainerItems);

        assertEquals(2, result.size());
    }

    @Test
    void givenBlobServiceClientAndContainerName_whenGetContainersAsList_thenReturnListOfblobContainerItems() {
        BlobServiceClient blobServiceClient = mock(BlobServiceClient.class);

        PagedIterable<BlobContainerItem> blobContainerItems = mock(PagedIterable.class);

        when(blobServiceClient.listBlobContainers(argThat(options -> options.getDetails().getRetrieveMetadata()), eq(null)))
            .thenReturn(blobContainerItems);

        Iterator<BlobContainerItem> blobContainerIterator = mock(Iterator.class);

        doCallRealMethod().when(blobContainerItems).forEach(any(Consumer.class));

        when(blobContainerItems.iterator()).thenReturn(blobContainerIterator);

        when(blobContainerIterator.hasNext()).thenReturn(true, true, false);
        when(blobContainerIterator.next()).thenReturn(mock(BlobContainerItem.class), mock(BlobContainerItem.class));

        List<BlobContainerItem> result = underTest.getContainersAsList(blobServiceClient);

        assertEquals(2, result.size());
    }
}
