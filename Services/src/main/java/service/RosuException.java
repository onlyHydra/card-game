package service;

public class RosuException extends RuntimeException{
    public RosuException() {
    }

    public RosuException(String message) {
        super(message);
    }

    public RosuException(String message, Throwable cause) {
        super(message, cause);
    }
}