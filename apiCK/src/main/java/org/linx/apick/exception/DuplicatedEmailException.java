package org.linx.apick.exception;

public class DuplicatedEmailException extends RuntimeException{

    public DuplicatedEmailException(String message) {
        super(message);
    }
}
