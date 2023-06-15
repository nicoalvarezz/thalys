package com.java.koffy.routing.helpers;

import javax.validation.constraints.NotBlank;

public class ValidateNotBlank {

    @NotBlank(message = "it must not be empty")
    private String notBlank;

    public ValidateNotBlank() {

    }

    public void setNotBlank(String notBlank) {
        this.notBlank = notBlank;
    }

    public String get() {
        return notBlank;
    }
}
