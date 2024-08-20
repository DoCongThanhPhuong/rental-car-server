package net.codejava.exceptions;

import lombok.Getter;

public class AppException extends RuntimeException {
    @Getter
    private Object[] args;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Object... args) {
        super(message);
        this.args = args;
    }
}
