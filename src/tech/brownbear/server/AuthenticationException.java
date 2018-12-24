package tech.brownbear.server;

/**
 * This error should be thrown when an action is missing proper identification
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Missing authentication");
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
