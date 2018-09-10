package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;

import java.util.stream.Stream;

public class UserDataProvider extends AbstractBackEndDataProvider<User, UserDataFilter> {

    private final UserRepository repository;

    public UserDataProvider(
            final UserRepository repository) {

        this.repository = repository;
    }

    @Override
    protected Stream<User> fetchFromBackEnd(
            final Query<User, UserDataFilter> query) {

        UserDataFilter filter = query.getFilter().orElse(new UserDataFilter());

        return this.repository
                .findByActive(filter.isShowActiveOnly())
                .stream();
    }

    @Override
    protected int sizeInBackEnd(
            final Query<User, UserDataFilter> query) {

        return ((Long) fetchFromBackEnd(query).count()).intValue();
    }
}
