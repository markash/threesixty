package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;

import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class AssessmentEntityEditForm extends AbstractEntityEditForm<Assessment> {

	@PropertyId("manager")
	private ComboBox managerField;
	@PropertyId("employee")
	private ComboBox employeeField;
	
	public AssessmentEntityEditForm(AssessmentService service) {
		this.managerField = new ComboBox("Manager", new IndexedContainer(service.findActiveUsers()));
		this.employeeField = new ComboBox("Employee", new IndexedContainer(service.findActiveUsers()));
		this.employeeField.addValueChangeListener(this::onEmployeeSelected);
	}
	
	@Override
	protected void internalLayout() {
		
		this.managerField.setEnabled(false);
		
		addComponent(PanelBuilder.FORM(
        		PanelBuilder.HORIZONTAL(idField, employeeField),
        		PanelBuilder.HORIZONTAL(managerField, new Label(""))
        		));
        addComponent(new Label(""));
	}
	
	@Override
	protected Assessment buildEmpty() {
		return Assessment.EMPTY();
	}
	
	public void onEmployeeSelected(Property.ValueChangeEvent event) {
		User user = (User) event.getProperty().getValue();
		if (user != null && user.getReportsTo() != null) {
			this.managerField.select(user.getReportsTo());
			this.managerField.markAsDirty();
		} else {
			this.managerField.select(null);
			this.managerField.markAsDirty();
		}
	}
}
