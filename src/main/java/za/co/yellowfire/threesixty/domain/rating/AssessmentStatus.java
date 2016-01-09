package za.co.yellowfire.threesixty.domain.rating;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

public enum AssessmentStatus {
	Creating("Creating", true),
	Created("Created", false),
	EmployeeCompleted("Employee Completed", false),
	ManagerCompleted("Manager Completed", false),
	Reviewed("Reviewed", false);
	
	private final String description;
	private final boolean allowEditing;
	
	private AssessmentStatus(final String description, final boolean allowEditing) {
		this.description = description;
		this.allowEditing = allowEditing;
	}

	public String getDescription() { return description; }
	public boolean isEditingAllowed() { return this.allowEditing; }
	
	public static AssessmentStatus fromString(@NotNull final String description) {
		if (StringUtils.isBlank(description)) {
			throw new IllegalArgumentException("The assessment status description cannot be null");
		}
		
		switch(description) {
		case "Created": return Created;
		case "Employee Completed":
		case "EmployeeCompleted": return EmployeeCompleted;
		case "Manager Completed":
		case "ManagerCompleted": return ManagerCompleted;
		case "Reviewed": return Reviewed;
		default: throw new IllegalArgumentException("The assessment status " + description + " is unknown.");
		}
	}
	
	public String toString() {
		return this.description;
	}
}
