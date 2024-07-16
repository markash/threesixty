package za.co.yellowfire.threesixty.domain.rating;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

public enum AssessmentStatus {
	Creating("Creating", true),
	Created("Created", false),
	EmployeeCompleted("Employee Completed", false),
	ManagerCompleted("Manager Completed", false),
	Reviewed("Reviewed", false),
	All("All", false);
	
	private final String description;
	private final boolean allowEditing;
	
	private AssessmentStatus(final String description, final boolean allowEditing) {
		this.description = description;
		this.allowEditing = allowEditing;
	}

	public String getDescription() { return description; }
	public boolean isEditingAllowed() { return this.allowEditing; }
	
	/**
	 * Returns the status that follows the current status in the assessment progression
	 */
	public AssessmentStatus getNextStatus() {
		return switch (this) {
			case Creating -> Created;
			case Created -> EmployeeCompleted;
			case EmployeeCompleted -> ManagerCompleted;
			case ManagerCompleted, Reviewed -> Reviewed;
			default -> this;
		};
	}
	
	public static AssessmentStatus fromString(@NotNull final String description) {
		if (StringUtils.isBlank(description)) {
			throw new IllegalArgumentException("The assessment status description cannot be null");
		}

		return switch (description) {
			case "Created" -> Created;
			case "Employee Completed", "EmployeeCompleted" -> EmployeeCompleted;
			case "Manager Completed", "ManagerCompleted" -> ManagerCompleted;
			case "Reviewed" -> Reviewed;
			default -> throw new IllegalArgumentException("The assessment status " + description + " is unknown.");
		};
	}
	
	public static List<AssessmentStatus> list() {
		return Arrays.asList(All, Creating, Created, EmployeeCompleted, ManagerCompleted, Reviewed);
	}
	
	public static Stream<AssessmentStatus> stream() {
		return list().stream();
	}
	
	public String toString() {
		return this.description;
	}
}
