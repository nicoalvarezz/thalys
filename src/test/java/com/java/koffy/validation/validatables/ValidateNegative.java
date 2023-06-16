package com.java.koffy.validation.validatables;

import javax.validation.constraints.Negative;

public class ValidateNegative {

    @Negative(message = "value must be negative")
    private int negative;

    public ValidateNegative() {
    }

    public int get() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }
}
