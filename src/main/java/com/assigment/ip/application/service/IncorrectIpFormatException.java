package com.assigment.ip.application.service;

public class IncorrectIpFormatException extends RuntimeException {

    public IncorrectIpFormatException(String text) {
        super(text);
    }

}
