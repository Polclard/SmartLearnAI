package org.smartlearnai.model.exeptions;

public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException() {
        super("email or password are empty or null");
    }
}
