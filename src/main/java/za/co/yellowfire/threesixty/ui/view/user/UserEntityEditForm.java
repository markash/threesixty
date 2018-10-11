package za.co.yellowfire.threesixty.ui.view.user;

import com.github.markash.ui.component.button.ButtonBuilder;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.view.AbstractEntityEditForm;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.user.*;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
public class UserEntityEditForm extends AbstractEntityEditForm<User> {

	private TextField firstNameField = new TextField("Name");
	private TextField lastNameField = new TextField("Last Name");
    private TextField emailField = new TextField("Email");
    private TextField phoneField = new TextField("Phone");
    private TextField websiteField = new TextField("Website");
    private RichTextArea bioField = new RichTextArea("Bio");
    private ComboBox<String> salutationField;
    private ComboBox<String> genderField;
    private ComboBox<Country> countryField;
	private ComboBox<Role> roleField;
	private ComboBox<Position> positionField;
	private ComboBox<User> reportsToField;
	private ComboBox<Identity> departmentField;

	private List<User> reportsTo = new ArrayList<>();
	private ListDataProvider<User> reportsToProvider = new ListDataProvider<>(reportsTo);
	private Image pictureField = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
	private Window pictureWindow = new Window(I8n.Profile.PICTURE, new PictureSelectionForm(this::onSelectedPicture));
    private boolean pictureChanged = false;

	private Button pictureButton = ButtonBuilder.CHANGE(this::onChangePicture);


    private final CurrentUserProvider<User> currentUserProvider;
    private final UserService userService;

	UserEntityEditForm(
            final UserService userService,
            final CurrentUserProvider<User> currentUserProvider) {

		super(User.class, true);

		this.userService = userService;
		this.currentUserProvider = currentUserProvider;

		this.getIdField().setCaption("User Name");
        this.getIdField().setWidth(100, Unit.PERCENTAGE);
//        this.getIdField().setReadOnly(false);
//        this.getIdField().setRequiredIndicatorVisible(true);

		this.salutationField = new ComboBox<>("Salutation", this.userService.findSalutations());
        this.salutationField.setWidth(100, Unit.PERCENTAGE);

		this.genderField = new ComboBox<>("Gender", this.userService.findGenders());
        this.genderField.setWidth(100, Unit.PERCENTAGE);

		this.countryField = new ComboBox<>("Country", this.userService.findCountries());
        this.countryField.setWidth(100, Unit.PERCENTAGE);

		this.roleField = new ComboBox<>("Role", this.userService.findRoles());
        this.roleField.setWidth(100, Unit.PERCENTAGE);

		this.reportsToField = new ComboBox<>("Reports To");
		this.reportsToField.setDataProvider(this.reportsToProvider);
        this.reportsToField.setWidth(100, Unit.PERCENTAGE);

		this.positionField = new ComboBox<>("Position", this.userService.findPositions());
        this.positionField.setWidth(100, Unit.PERCENTAGE);

		this.departmentField = new ComboBox<>("Department", this.userService.findDepartments());
        this.departmentField.setWidth(100, Unit.PERCENTAGE);

        this.firstNameField.setWidth(100, Unit.PERCENTAGE);
        this.lastNameField.setWidth(100, Unit.PERCENTAGE);
        this.emailField.setWidth(100, Unit.PERCENTAGE);
        this.phoneField.setWidth(100, Unit.PERCENTAGE);
        this.websiteField.setWidth(100, Unit.PERCENTAGE);
        this.bioField.setWidth(100, Unit.PERCENTAGE);
        this.pictureField.setStyleName("profile-image");

        getBinder().forField(firstNameField).asRequired(I8n.User.Validation.FIRST_NAME_REQUIRED).bind(User.FIELD_FIRST_NAME);
        getBinder().forField(lastNameField).asRequired(I8n.User.Validation.LAST_NAME_REQUIRED).bind(User.FIELD_LAST_NAME);
        getBinder().forField(salutationField).bind(User.FIELD_TITLE);
        getBinder().forField(genderField).bind(User.FIELD_GENDER);
        getBinder().forField(emailField).bind(User.FIELD_EMAIL);
        getBinder().forField(phoneField).bind(User.FIELD_PHONE);
        getBinder().forField(websiteField).bind(User.FIELD_WEBSITE);
        getBinder().forField(bioField).bind(User.FIELD_BIO);
        getBinder().forField(departmentField).bind(User.FIELD_DEPARTMENT);
        getBinder().forField(positionField).bind(User.FIELD_POSITION);
        getBinder().forField(reportsToField).bind(User.FIELD_REPORTS_TO);
        getBinder().forField(countryField).bind(User.FIELD_LOCATION);
        getBinder().forField(roleField).bind(User.FIELD_ROLE);

        firstNameField.setRequiredIndicatorVisible(true);
        lastNameField.setRequiredIndicatorVisible(true);

	}

    @Override
    public boolean isModified() {
        return super.isModified() || this.pictureChanged;
    }

	@Override
	protected void internalLayout() {

	    MHorizontalLayout picture =
                new MHorizontalLayout()
                        .withMargin(false)
                        .withFullWidth()
                        .withComponents(
                                new MVerticalLayout(pictureField, pictureButton).withMargin(false),
                                new MVerticalLayout()
                                        .withMargin(false)
                                        .with(
                                                getIdField(),
                                                firstNameField,
                                                lastNameField,
                                                new MHorizontalLayout()
                                                        .withMargin(false)
                                                        .with(salutationField, genderField)
                                        )
                        );

	    MVerticalLayout left =
                new MVerticalLayout()
                        .withMargin(false)
                        .with(
                                picture,
                                departmentField,
                                positionField,
                                new MHorizontalLayout(roleField, reportsToField).withMargin(false)
                        );

	    MVerticalLayout right =
                new MVerticalLayout()
                        .withMargin(false)
                        .with(
                                emailField,
                                phoneField,
                                websiteField,
                                countryField,
                                bioField
                        );

        HorizontalLayout details =
                new MHorizontalLayout()
                        .withMargin(false)
                        .withFullSize()
                        .with(
                                left,
                                right
                        );

        addComponent(details);
	}

    /**
     * Provide a hook for subclasses to update dependant fields
     */
    @Override
    protected void updateDependentFields() {

        updateReportsTo();
        updatePicture();
    }

    @SuppressWarnings("unused")
    private void onChangePicture(
            Button.ClickEvent event) {

	    UI.getCurrent().addWindow(pictureWindow);
    }


    void resetPassword() {

	    Optional<User> principal = this.currentUserProvider.get();
        if (principal.isPresent()) {

            String currentUserId = principal.get().getId();
            this.userService.resetPassword(getValue(), currentUserId);
            if (getValue().getId().equalsIgnoreCase(currentUserId)) {
                NotificationBuilder.showNotification(
                        "Password reset",
                        "Your password has been reset and can be changed on next logon.",
                        5000);
            } else {
                NotificationBuilder.showNotification(
                        "Password reset",
                        "User's password has been reset and can be changed on next logon.",
                        5000);
            }
        } else {
            NotificationBuilder.showNotification(
                    "Unable to reset password",
                    "Unable to determine the current user session.",
                    5000);
        }
    }

    private void onSelectedPicture(
            final PictureSelectionForm.FileEvent event) {

	    try {
            if (getValue() != null) {
                getValue().setPicture(event.getFile());
                updatePicture();

                this.pictureChanged = true;
                getEventRouter().fireEvent(new StatusChangeEvent(getBinder(), false));

            }
        } catch (IOException e) {
            Notification.show("Error changing profile picture", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void updateReportsTo() {

        this.reportsTo.clear();
        this.currentUserProvider
                .get()
                .map(userService::findUsersExcept)
                .ifPresent(reportsTo::addAll);
        this.reportsToProvider.refreshAll();
    }

    private void updatePicture() {

        if (getValue().hasPicture()) {
            this.pictureField.setSource(new ByteArrayStreamResource(getValue().getPictureContent(), getValue().getPictureName()));
        }
    }
}
