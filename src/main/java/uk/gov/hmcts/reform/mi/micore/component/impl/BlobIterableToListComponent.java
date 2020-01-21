package uk.gov.hmcts.reform.mi.micore.component.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.component.IterableToListComponent;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlobIterableToListComponent implements IterableToListComponent<BlobItem> {

    @Override
    public List<BlobItem> getIterableAsList(PagedIterable<BlobItem> iterable) {
        List<BlobItem> blobList = new ArrayList<>();

        iterable.forEach(blobList::add);

        return blobList;
    }

    public List<BlobItem> getBlobsAsList(BlobServiceClient blobServiceClient, String containerName) {
        return getIterableAsList(
            blobServiceClient.getBlobContainerClient(containerName).listBlobs()
        );
    }
}
