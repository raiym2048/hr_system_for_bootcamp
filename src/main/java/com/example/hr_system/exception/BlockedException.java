package com.example.hr_system.exception;

import io.jsonwebtoken.io.IOException;

public class BlockedException extends RuntimeException {
    public BlockedException() {
        super();
    }

    public BlockedException(String message) {
        super(message);
    }

}
