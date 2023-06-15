package com.java.koffy.routing.helpers;

import javax.validation.constraints.AssertTrue;

public class ValidateAssertTrue {

    @AssertTrue(message = "it must be true")
    private boolean assertTrue;

    public ValidateAssertTrue() {

    }

    public boolean isAssertTrue() {
        return assertTrue;
    }

    public void setAssertTrue(boolean assertTrue) {
        this.assertTrue = assertTrue;
    }
}
