package org.lekitech.gafalag.exception.security;

/**
 * Custom exception class for handling scenarios where a user registration attempt
 * is made with an email or username that already exists in the system.
 *
 * <p>This exception is typically thrown during the user registration process
 * when the provided email or username is already associated with an existing account.</p>
 *
 * <p>The exception extends {@link RuntimeException}, allowing it to be used
 * within the application without the need for explicit error handling (try-catch blocks).</p>
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new {@code UserAlreadyExistsException} with the specified detail message.
     * The message is saved for later retrieval by the {@link Throwable#getMessage()} method.
     *
     * @param message The detail message indicating the reason for the exception.
     *                The message is intended to provide specific details about the
     *                nature of the problem encountered.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
