package com.dxc.rf.api.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Employee email already exists (emails are unique regardless of case): " + email);
    }
}
