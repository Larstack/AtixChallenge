package com.atixlabs.exception;

public class ParametersNotFound extends Throwable {

    @Override
    public String getMessage() {
        return "Parameters M and S not found.";
    }
}
