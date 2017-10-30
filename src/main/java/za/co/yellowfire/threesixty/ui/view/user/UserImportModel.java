package za.co.yellowfire.threesixty.ui.view.user;

import za.co.yellowfire.threesixty.domain.organization.Organization;
import za.co.yellowfire.threesixty.domain.user.Country;
import za.co.yellowfire.threesixty.domain.user.Position;
import za.co.yellowfire.threesixty.domain.user.Role;
import za.co.yellowfire.threesixty.domain.user.User;

import java.io.Serializable;

public class UserImportModel implements Serializable {

    public static final String FIELD_IMPORT_STATUS = "status";

    private User user;
    private ImportStatus status;

    public UserImportModel(
            final User user) {

        this.user = user;
        this.user.setId(this.getFirstName().toLowerCase() + this.getLastName().toLowerCase().substring(0, 1));
        this.user.setActive(true);
        this.status = ImportStatus.UNPROCESSED;
    }

    public User getUser() { return user; }
    public String getId() { return getUser().getId(); }
    public String getPassword() { return getUser().getPassword(); }
    public String getEmail() { return getUser().getEmail(); }
    public Country getLocation() { return getUser().getLocation(); }
    public String getPhone() { return getUser().getPhone(); }
    public Integer getNewsletterSubscription() { return getUser().getNewsletterSubscription(); }
    public String getWebsite() { return getUser().getWebsite(); }
    public String getBio() { return getUser().getBio(); }
    public String getGender() { return this.getUser().getGender(); }
    public String getTitle() { return getUser().getTitle(); }
    public Role getRole() { return getUser().getRole(); }
    public Organization getDepartment() { return getUser().getDepartment(); }
    public User getReportsTo() { return getUser().getReportsTo(); }
    public Position getPosition() { return getUser().getPosition(); }
    public String getFirstName() { return getUser().getFirstName(); }
    public String getLastName() { return getUser().getLastName(); }

    public ImportStatus getStatus() { return status; }
    public void setStatus(final ImportStatus status) { this.status = status; }
}
