package com.example.phoenixtest.feign.exception;

import lombok.Getter;

@Getter
public class UserDetailsFeignException extends RuntimeException {
    private static final long serialVersionUID = 3694912101388027958L;

    private final int code;
    private final String name;

    public UserDetailsFeignException(int code, String name, String message) {
        super(message);
        this.code = code;
        this.name = name;
    }
}
