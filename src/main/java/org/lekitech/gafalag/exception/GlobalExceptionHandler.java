package org.lekitech.gafalag.exception;

import org.lekitech.gafalag.exception.security.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * This class uses Spring's {@link ControllerAdvice} to handle exceptions globally across
 * all controllers. It provides centralized exception handling across all {@code @RequestMapping}
 * methods through {@code @ExceptionHandler} methods.
 *
 * <p>This ensures consistent responses for exceptions thrown in different parts of the application.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link UserAlreadyExistsException} by returning a conflict response.
     *
     * <p>This method is invoked when a {@link UserAlreadyExistsException} is thrown
     * anywhere within the application. It constructs a response entity with
     * HTTP status 409 (Conflict) and the exception message.</p>
     *
     * @param e The exception that was thrown.
     * @return A {@link ResponseEntity} with a conflict status and the exception message.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    /**
     * Handles {@link BadCredentialsException} by returning an unauthorized response.
     *
     * <p>This method is invoked when a {@link BadCredentialsException} is thrown
     * during the authentication process. It constructs a response entity with
     * HTTP status 401 (Unauthorized) and the exception message.</p>
     *
     * @param e The exception that was thrown.
     * @return A {@link ResponseEntity} with an unauthorized status and the exception message.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    /**
     * Handles generic {@link Exception} by returning an internal server error response.
     *
     * <p>This method catches all other exceptions that are not explicitly handled
     * by other exception handler methods in this class. It constructs a response entity
     * with HTTP status 500 (Internal Server Error) and a generic error message along
     * with the specific exception message.</p>
     *
     * @param e The exception that was thrown.
     * @return A {@link ResponseEntity} with an internal server error status and a detailed error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(ExpressionNotFound.class)
    public ResponseEntity<String> handleIllegalArgumentException(ExpressionNotFound e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("An error occurred: " + e.getMessage());
    }
}
