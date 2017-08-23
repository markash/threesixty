package za.co.yellowfire.threesixty.ui.view.security;

import org.apache.commons.lang3.StringUtils;
import za.co.yellowfire.threesixty.validation.FieldMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@FieldMatch.List({
    @FieldMatch(
    		first = "newPassword", 
    		second = "confirmPassword", 
    		message = "{YourMsgKey}", 
    		groups = ChangePasswordGroup.class
    )
})
public class ChangePasswordModel implements Serializable {
	private static final long serialVersionUID = -5628863041544466221L;

	@NotNull(message = "Old password is required")
	private String oldPassword;
	
	@NotNull(message = "New password is required")
    @Size(min = 8, max = 64, message = "New password must be longer than 8 characters")
	private String newPassword;
	
	@NotNull(message = "Confirm password is required")
    @Size(min = 4, max = 15, message = "Confirm password must be longer than 8 characters")
	private String confirmPassword;

	public String getOldPassword() { return oldPassword; }
	public String getNewPassword() { return newPassword; }
	public String getConfirmPassword() { return confirmPassword; }

	public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
	public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
	public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
	
	/**
	 * Determines if the new and confirmation passwords match
	 * @return true if matching including case else false
	 */
	public boolean hasMatchingPasswords() { return StringUtils.equals(newPassword, confirmPassword); }
}
