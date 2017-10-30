package za.co.yellowfire.threesixty.ui.view.user;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.view.AbstractTableSearchView;
import io.threesixty.ui.view.TableDefinition;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.simpleflatmapper.poi.SheetMapper;
import org.simpleflatmapper.poi.SheetMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.upload.FileUploadEvent;
import za.co.yellowfire.threesixty.ui.component.upload.FileUploadForm;

import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings("unused")
@Secured("ROLE_ADMIN")
@VaadinFontIcon(VaadinIcons.USERS)
@SpringView(name = UserImportView.VIEW_NAME)
@SideBarItem(sectionId = Sections.ADMINISTRATION, caption = UserImportView.TITLE)
public class UserImportView extends AbstractTableSearchView<UserImportModel, String> {
	private static final long serialVersionUID = 1L;

	public static final String TITLE = I8n.User.IMPORT;
    public static final String VIEW_NAME = "import_users";
	public static final VaadinIcons ICON = I8n.User.ICON;

	private final List<UserImportModel> users;
    private final FileUploadForm uploadForm = new FileUploadForm();
    private final Window uploadWindow = new Window(I8n.User.IMPORT, uploadForm);
    private final UserService userService;
    private final MButton uploadButton = new MButton(I8n.Button.UPLOAD, this::onShowUpload);
    private final MButton importButton = new MButton(I8n.Button.IMPORT, this::onImport);
    private Registration registration;

    @Autowired
    public UserImportView(
            final List<UserImportModel> userImportData,
            final TableDefinition<UserImportModel> userImportTableDefinition,
            final UserService userService) {

    	super(UserImportModel.class, TITLE, new ListDataProvider<>(userImportData), userImportTableDefinition);

    	this.users = userImportData;
        this.userService = userService;
        this.registration = this.uploadForm.addFileUploadListener(this::onProcessUpload);
        this.uploadWindow.setModal(true);
        this.uploadWindow.setWidth(300f, Unit.PIXELS);

        getToolbar().addAction(uploadButton);
        getToolbar().addAction(importButton);

        importButton.setEnabled(false);
        importButton.setDisableOnClick(true);
    }

    private void onShowUpload(
            final Button.ClickEvent event) {

        UI.getCurrent().addWindow(this.uploadWindow);
    }

    private void onProcessUpload(
            final FileUploadEvent event) {

        final SheetMapper<User> mapper =
                SheetMapperFactory
                        .newInstance()
                        .ignorePropertyNotFound()
                        .newMapper(User.class);
        try (
                InputStream is = new FileInputStream(event.getFile());
                Workbook workbook = new XSSFWorkbook(is)) {

            this.users.clear();
            getGrid().getDataProvider().refreshAll();

            mapper.stream(workbook.getSheetAt(0)).map(UserImportModel::new).forEach(this.users::add);
            getGrid().getDataProvider().refreshAll();

            importButton.setEnabled(true);
        } catch (IOException e) {

            NotificationBuilder.showNotification(
                    "Unable import users",
                    e.getMessage(),
                    3000);
        }
    }

    private void onImport(
            final Button.ClickEvent event) {


        for (UserImportModel model : this.users) {
            try {

                this.userService.save(model.getUser());
                model.setStatus(ImportStatus.PROCESSED);

            } catch (IOException e) {

                model.setStatus(ImportStatus.ERROR);

                NotificationBuilder.showNotification(
                        "Unable import users",
                        e.getMessage(),
                        1000);
            }
        }

        getGrid().getDataProvider().refreshAll();

        NotificationBuilder.showNotification(
                "User import",
                "Completed importing users",
                3000);
    }

    @PreDestroy
    public void close() {

        this.registration.remove();
    }
}