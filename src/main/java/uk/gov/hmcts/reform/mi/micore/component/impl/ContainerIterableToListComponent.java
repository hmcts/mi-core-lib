package uk.gov.hmcts.reform.mi.micore.component.impl;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobContainerListDetails;
import com.azure.storage.blob.models.ListBlobContainersOptions;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.mi.micore.component.IterableToListComponent;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContainerIterableToListComponent implements IterableToListComponent<BlobContainerItem> {

    @Override
    public List<BlobContainerItem> getIterableAsList(PagedIterable<BlobContainerItem> iterable) {
        List<BlobContainerItem> containerList = new ArrayList<>();

        iterable.forEach(containerList::add);

        return containerList;
    }

    public List<BlobContainerItem> getContainersAsList(BlobServiceClient blobServiceClient) {
        ListBlobContainersOptions listBlobContainersOptions
            = new ListBlobContainersOptions().setDetails(new BlobContainerListDetails().setRetrieveMetadata(true));

        return getIterableAsList(
            blobServiceClient.listBlobContainers(listBlobContainersOptions, null)
        );
    }
}
