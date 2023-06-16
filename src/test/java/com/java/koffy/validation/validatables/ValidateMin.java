package com.java.koffy.validation.validatables;

import javax.validation.constraints.Min;

public class ValidateMin {

    @Min(value = 38, message = "value must be higher")
    private int min;

    public ValidateMin() {
    }

    public int get() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
