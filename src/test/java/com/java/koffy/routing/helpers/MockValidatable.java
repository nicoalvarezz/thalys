package com.java.koffy.routing.helpers;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;

public class MockValidatable {

    @Email
    private String email;

    @NotNull
    private String notNull;

    @NotBlank
    private String notBlank;

    @AssertFalse
    private boolean falseAssertion;

    @AssertTrue
    private boolean trueAssertion;

    @DecimalMax("10.5")
    private BigDecimal decimalMax;

    @DecimalMin("0.5")
    private BigDecimal decimalMin;

    @Digits(integer = 5, fraction = 2)
    private BigDecimal digits;

    @Future
    private Date future;

    @FutureOrPresent
    private Date futureOrPresent;

    @Max(100)
    private int max;

    @Min(1)
    private int min;

    @Negative
    private int negative;

    @NegativeOrZero
    private int negativeOrZero;

    @Null
    private Object allowNull;

    @Past
    private Date date;

    @PastOrPresent
    private Date pastOrPresent;

    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String pattern;

    @Positive
    private int positive;

    @PositiveOrZero
    private int positiveOrZero;

    @Size(min = 2, max = 10)
    private String size;

    public MockValidatable(String email, String notNull, String notBlank, boolean falseAssertion, boolean trueAssertion, BigDecimal decimalMax, BigDecimal decimalMin, BigDecimal digits, Date future, Date futureOrPresent, int max, int min, int negative, int negativeOrZero, Object allowNull, Date date, Date pastOrPresent, String pattern, int positive, int positiveOrZero, String size) {
        this.email = email;
        this.notNull = notNull;
        this.notBlank = notBlank;
        this.falseAssertion = falseAssertion;
        this.trueAssertion = trueAssertion;
        this.decimalMax = decimalMax;
        this.decimalMin = decimalMin;
        this.digits = digits;
        this.future = future;
        this.futureOrPresent = futureOrPresent;
        this.max = max;
        this.min = min;
        this.negative = negative;
        this.negativeOrZero = negativeOrZero;
        this.allowNull = allowNull;
        this.date = date;
        this.pastOrPresent = pastOrPresent;
        this.pattern = pattern;
        this.positive = positive;
        this.positiveOrZero = positiveOrZero;
        this.size = size;
    }
}
