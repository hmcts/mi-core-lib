package uk.gov.hmcts.reform.mi.micore.test.utils;

import com.azure.storage.blob.models.ListBlobsOptions;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class ListBlobOptionMatcher implements ArgumentMatcher<ListBlobsOptions> {

    private final ListBlobsOptions listBlobsOptions;

    public ListBlobOptionMatcher(ListBlobsOptions listBlobsOptions) {
        this.listBlobsOptions = listBlobsOptions;
    }

    @Override
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public boolean matches(ListBlobsOptions argument) {
        if (Boolean.logicalOr(argument == null, listBlobsOptions == null)) {
            return   listBlobsOptions == argument;
        }
        ReflectionEquals details = new ReflectionEquals(listBlobsOptions.getDetails());
        return details.matches(argument.getDetails());
    }
}
