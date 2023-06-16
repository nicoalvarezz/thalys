package com.java.koffy.validation.validatables;

import javax.validation.constraints.NegativeOrZero;

public class ValidateNegativeOrZero {

    @NegativeOrZero(message = "value must be negative or zero")
    private int negativeOrZero;

    public ValidateNegativeOrZero() {
    }

    public int get() {
        return negativeOrZero;
    }

    public void setNegativeOrZero(int negativeOrZero) {
        this.negativeOrZero = negativeOrZero;
    }
}
