package za.co.yellowfire.threesixty.ui.view.user;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.view.AbstractEntityEditView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Secured("ROLE_ADMIN")
@SpringView(name = UserEditView.VIEW_NAME)
public class UserEditView extends AbstractEntityEditView<User> {
	private static final long serialVersionUID = 1L;

	private EventBus.SessionEventBus eventBus;

	public static final String TITLE = I8n.User.SINGULAR;
	public static final String VIEW_NAME = "user";

    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }

    @Autowired
    public UserEditView(
			final EntitySupplier<User, Serializable> userSupplier,
    		final BlankSupplier<User> blankUserSupplier,
			final EntityPersistFunction<User> userPersistFunction,
    		final UserEntityEditForm userEntityEditForm,
			final EventBus.SessionEventBus eventBus) {
    	
    	super(TITLE,
                userEntityEditForm,
                userSupplier,
                blankUserSupplier,
                userPersistFunction);
    	this.eventBus = eventBus;
    }

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		super.enter(event);
	}

	@Override
    protected void publishOnEventBus(final ApplicationEvent event) {
        Optional.ofNullable(eventBus).ifPresent(eb -> eb.publish(this, event));
    }
}

