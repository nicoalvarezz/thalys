package com.java.koffy.validation.validatables;

import javax.validation.constraints.Max;

public class ValidateMax {

    @Max(value = 100, message = "value must be lower")
    private int max;

    public ValidateMax() {
    }

    public int get() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
