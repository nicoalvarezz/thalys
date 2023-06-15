package com.java.koffy.routing.helpers;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class ValidateMinDecimal {

    @DecimalMin(value = "1.4", message = "the value must be higher")
    private BigDecimal minDecimal;

    public ValidateMinDecimal() {

    }

    public BigDecimal get() {
        return minDecimal;
    }

    public void setMinDecimal(BigDecimal minDecimal) {
        this.minDecimal = minDecimal;
    }
}
