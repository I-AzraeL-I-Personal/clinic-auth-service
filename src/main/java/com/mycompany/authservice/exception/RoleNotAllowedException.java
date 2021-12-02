package com.mycompany.authservice.exception;

public class RoleNotAllowedException extends RuntimeException {

    public RoleNotAllowedException(String resource, String message) {
        super(String.format("Failed for [%s]: %s", resource, message));
    }
}
