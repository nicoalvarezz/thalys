package com.java.koffy.validation.validators;

import com.java.koffy.validation.annotations.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[\\w+]+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return EMAIL_REGEX.matcher(email).matches();
    }
}
