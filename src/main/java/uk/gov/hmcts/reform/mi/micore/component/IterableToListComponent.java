package uk.gov.hmcts.reform.mi.micore.component;

import com.azure.core.http.rest.PagedIterable;

import java.util.List;

public interface IterableToListComponent<T> {

    List<T> getIterableAsList(PagedIterable<T> iterable);
}
