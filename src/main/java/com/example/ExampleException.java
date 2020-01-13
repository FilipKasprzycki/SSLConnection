package com.example;

import lombok.extern.java.Log;

import java.util.Arrays;

@Log
final class ExampleException extends Exception {

    private static final String LOG_FULL_MESSAGE = "[!] Exception %s\n[!] Message: %s\n[!] Exception message: %s\n[!] Cause: %s\n[!] Stack trace:\n%s";
    private static final String LOG_EXCEPTION = "[!] Exception %s\n[!] Exception message: %s\n[!] Cause: %s\n[!] Stack trace:\n%s";

    ExampleException(String message, Throwable cause) {
        super(message, cause);
        log.severe(String.format(LOG_FULL_MESSAGE, cause.getClass(), message, cause.getMessage(), cause.toString(),
                Arrays.toString(cause.getStackTrace()).replace(",", "\n")));
    }

    ExampleException(Throwable cause) {
        super(cause);
        log.severe(String.format(LOG_EXCEPTION, cause.getClass(), cause.getMessage(), cause.toString(),
                Arrays.toString(cause.getStackTrace()).replace(",", "\n")));
    }
}