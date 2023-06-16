package com.java.koffy.validation.validatables;

import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;

public class ValidateMaxDecimal {

    @DecimalMax(value = "10.4", message = "the value must be lower")
    private BigDecimal maxDecimal;

    public ValidateMaxDecimal() {
    }

    public BigDecimal get() {
        return maxDecimal;
    }

    public void setMaxDecimal(BigDecimal maxDecimal) {
        this.maxDecimal = maxDecimal;
    }
}
