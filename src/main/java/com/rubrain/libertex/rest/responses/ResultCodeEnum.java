package com.rubrain.libertex.rest.responses;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResultCodeEnum {
    OK("Ok"),
    UNAUTHORIZED("Unauthorized"),
    INCORRECT_PARAMETER("IncorrectParameter"),
    UNEXPECTEDERROR("UnexpectedError");

    private String code;

    ResultCodeEnum(String code) {
        this.code = code;
    }

    @JsonValue
    public String jsonValue() {
        return this.code;
    }
}
