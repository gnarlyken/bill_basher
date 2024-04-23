package com.billbasher.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String message) {
        super(message);
    }
}