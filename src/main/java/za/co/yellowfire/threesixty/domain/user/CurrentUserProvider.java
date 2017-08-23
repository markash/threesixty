package za.co.yellowfire.threesixty.domain.user;

import java.util.function.Supplier;

/**
 * Provides the current user of the application
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface CurrentUserProvider extends Supplier<User> {
    /**
     * Get the user
     * @return The user
     */
    User get();
}
