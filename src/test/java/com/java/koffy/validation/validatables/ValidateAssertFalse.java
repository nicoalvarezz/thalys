package com.java.koffy.validation.validatables;

import javax.validation.constraints.AssertFalse;

public class ValidateAssertFalse {

    @AssertFalse (message = "it must be false")
    private boolean assertFalse;

    public ValidateAssertFalse() {

    }

    public boolean isAssertFalse() {
        return assertFalse;
    }

    public void setAssertFalse(boolean assertFalse) {
        this.assertFalse = assertFalse;
    }
}
