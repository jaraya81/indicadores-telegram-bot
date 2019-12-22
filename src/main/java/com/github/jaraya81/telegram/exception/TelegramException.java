package com.github.jaraya81.telegram.exception;

public class TelegramException extends Exception {

    public TelegramException() {
    }

    public TelegramException(String message) {
        super(message);
    }

    public TelegramException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramException(Throwable cause) {
        super(cause);
    }

}
