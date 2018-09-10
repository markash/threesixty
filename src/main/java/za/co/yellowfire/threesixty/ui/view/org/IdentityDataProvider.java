package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.organization.IdentityService;

import java.util.stream.Stream;

/**
 * Identity data provider
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class IdentityDataProvider extends AbstractBackEndHierarchicalDataProvider<Identity, IdentityDataFilter> {

    private final IdentityService service;

    public IdentityDataProvider(
            final IdentityService service) {

        this.service = service;
    }

    @Override
    protected Stream<Identity> fetchChildrenFromBackEnd(
            final HierarchicalQuery<Identity, IdentityDataFilter> query) {

        IdentityDataFilter filter = query.getFilter().orElse(new IdentityDataFilter());

        if (query.getParent() == null) {
            return this.service
                    .retrieve(filter.isShowActiveOnly())
                    .stream();
        } else {
            return this.service
                    .retrieve(query.getParent(), filter.isShowActiveOnly())
                    .stream();
        }
    }

    @Override
    public int getChildCount(
            final HierarchicalQuery<Identity, IdentityDataFilter> query) {

        IdentityDataFilter filter = query.getFilter().orElse(new IdentityDataFilter());

        return this.service.retrieveChildrenCount(query.getParent(), filter.isShowActiveOnly());
    }

    @Override
    public boolean hasChildren(
            final Identity item) {

        return item.hasChildren();
    }
}
