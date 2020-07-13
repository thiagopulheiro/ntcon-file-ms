package com.ntconsult.challenge.thiago.port;

public class EventAggregatorException extends RuntimeException {

    public EventAggregatorException(String message, Exception e) {
        super(message, e);
    }
}
