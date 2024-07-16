package za.co.yellowfire.threesixty.ui.view.security;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import za.co.yellowfire.threesixty.ui.I8n;

public class ConfirmPasswordValidator implements Validator<ChangePasswordModel> {
    /**
     * Validates the given value. Returns a {@code ValidationResult} instance
     * representing the outcome of the validation.
     *
     * @param model   the change password model to validate
     * @param context the value context for validation
     * @return the validation result
     */
    @Override
    public ValidationResult apply(final ChangePasswordModel model, final ValueContext context) {
        if (!model.hasMatchingPasswords()) {
            return ValidationResult.error(I8n.ChangePassword.Errors.PASSWORDS_DO_NOT_MATCH);
        }
        return ValidationResult.ok();
    }
}