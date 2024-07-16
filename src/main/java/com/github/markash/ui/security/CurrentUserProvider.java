package com.github.markash.ui.security;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Provides the current user of the application
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public interface CurrentUserProvider<T> extends Supplier<Optional<T>> {

    Optional<T> get();
}
