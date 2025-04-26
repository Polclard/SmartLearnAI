package org.smartlearnai.model.exeptions;

public class EmailException extends RuntimeException {
    public EmailException() {
        super("Email not valid");
    }
}
