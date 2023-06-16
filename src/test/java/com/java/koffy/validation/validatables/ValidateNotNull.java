package com.java.koffy.validation.validatables;

import javax.validation.constraints.NotNull;

public class ValidateNotNull {

    @NotNull(message = "it must not be null")
    private String notNull;

    public ValidateNotNull() {
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    public String get() {
        return notNull;
    }
}
