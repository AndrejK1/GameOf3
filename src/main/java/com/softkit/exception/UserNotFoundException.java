package com.softkit.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("Invalid username/password supplied", HttpStatus.UNAUTHORIZED);
    }
}
