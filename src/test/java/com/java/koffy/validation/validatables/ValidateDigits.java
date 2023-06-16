package com.java.koffy.validation.validatables;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class ValidateDigits {

    @Digits(integer = 5, fraction = 2, message = "value out of limits")
    private BigDecimal digits;

    public ValidateDigits() {
    }

    public BigDecimal get() {
        return digits;
    }

    public void setDigits(BigDecimal digits) {
        this.digits = digits;
    }
}
