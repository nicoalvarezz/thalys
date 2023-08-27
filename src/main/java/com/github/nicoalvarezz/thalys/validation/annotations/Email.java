package com.github.nicoalvarezz.thalys.validation.annotations;

import com.github.nicoalvarezz.thalys.validation.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "email address must have a valid format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
