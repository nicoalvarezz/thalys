package com.java.koffy.routing;

import com.java.koffy.routing.helpers.ValidateAssertFalse;
import com.java.koffy.routing.helpers.ValidateAssertTrue;
import com.java.koffy.routing.helpers.ValidateDigits;
import com.java.koffy.routing.helpers.ValidateEmail;
import com.java.koffy.routing.helpers.ValidateFuture;
import com.java.koffy.routing.helpers.ValidateMaxDecimal;
import com.java.koffy.routing.helpers.ValidateMinDecimal;
import com.java.koffy.routing.helpers.ValidateNotBlank;
import com.java.koffy.routing.helpers.ValidateNotNull;
import com.java.koffy.utils.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationTest {

    private static Map<String, String> constructData(String field, String value) {
        return new HashMap<>() {{
            put(field, value);
        }};
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "user123@example.co.uk", "user.name@example-domain.com", "user123+test@example.com", "user_name@example.org", "user@example123.com", "user123@example-company.com", "user@example.com", "user123@example.co.uk", "user.name@example-domain.com", "user_name@example.org", "user@example123.com", "user123@example-company.com", "user@subdomain.domain.com", "user.name1234@example.co", "user1234@example-domain.com", "abc-d@mail.com", "abc.def@mail.com", "abc@mail.com", "abc_def@mail.com", "abc.def@mail.cc", "abc.def@mail-archive.com", "abc.def@mail.org", "abc.def@mail.com"})
    public void testValidEmailValidation(String email) {
        Map<String, String> data = constructData("email", email);
        ValidateEmail validateEmail = Validator.validate(data, ValidateEmail.class);
        assertEquals(data.get("email"), validateEmail.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@domain", "user.domain.com", "user@domain@com", "user@domain", "user@domain..com", "user@domain_com", "user@domain#com", "user@domain.com.", "user@domain,com", "user..name@domain.com", "user@-domain.com", "user@domain-.com", "user@[123.456.789.0]", "abc-@mail.com", "abc..def@mail.com", ".abc@mail.com", "abc#def@mail.com", "abc.def@mail.c", "abc.def@mail#archive.com", "abc.def@mail", "abc.def@mail..comz"})
    public void testInvalidEmailValidation(String email) {
        String invalidEmailMessage = "Validation failed for field '" + "email" + "': " + "email address must have a valid format";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(constructData("email", email), ValidateEmail.class));
        assertEquals(invalidEmailMessage, exception.getMessage());
    }

    @Test
    public void testNotNullValidation() {
        Map<String, String> data = constructData("notNull", "something_test");
        ValidateNotNull validateNotNull = Validator.validate(data, ValidateNotNull.class);
        assertEquals(data.get("notNull"), validateNotNull.get());
    }

    @Test
    public void testNotNullValidationException() {
        String invalidEmailMessage = "Validation failed for field '" + "notNull" + "': " + "it must not be null";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(new HashMap<>(), ValidateNotNull.class));
        assertEquals(invalidEmailMessage, exception.getMessage());
    }

    @Test
    public void testNotBlankValidation() {
        Map<String, String> data = constructData("notBlank", "something_test");
        ValidateNotBlank validateNotBlank = Validator.validate(data, ValidateNotBlank.class);
        assertEquals(data.get("notBlank"), validateNotBlank.get());
    }

    @Test
    public void testNotBlankValidationException() {
        String invalidEmailMessage = "Validation failed for field '" + "notBlank" + "': " + "it must not be empty";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(constructData("notBlank", ""), ValidateNotBlank.class));
        assertEquals(invalidEmailMessage, exception.getMessage());
    }

    @Test
    public void testAssertFalseValidation() {
        Map<String, String> data = constructData("assertFalse", "false");
        ValidateAssertFalse validateAssertFalse = Validator.validate(data, ValidateAssertFalse.class);
        assertFalse(validateAssertFalse.isAssertFalse());
    }

    @Test
    public void testAssertFalseValidationException() {
        String invalidEmailMessage = "Validation failed for field '" + "assertFalse" + "': " + "it must be false";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(constructData("assertFalse", "true"), ValidateAssertFalse.class));
        assertEquals(invalidEmailMessage, exception.getMessage());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validator.validate(constructData("assertFalse", "asdasdad"), ValidateAssertFalse.class));
    }

    @Test
    public void testAssertTrueValidation() {
        Map<String, String> data = constructData("assertTrue", "true");
        ValidateAssertTrue validateAssertTrue = Validator.validate(data, ValidateAssertTrue.class);
        assertTrue(validateAssertTrue.isAssertTrue());
    }

    @Test
    public void testAssertTrueValidationException() {
        String invalidEmailMessage = "Validation failed for field '" + "assertTrue" + "': " + "it must be true";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(constructData("assertTrue", "false"), ValidateAssertTrue.class));
        assertEquals(invalidEmailMessage, exception.getMessage());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> Validator.validate(constructData("assertFalse", "asdasdad"), ValidateAssertFalse.class));
    }

    @Test
    public void testMaxDecimalValidation() {
        Map<String, String> data = constructData("maxDecimal", "9.4");
        ValidateMaxDecimal maxDecimal = Validator.validate(data, ValidateMaxDecimal.class);
        assertEquals(data.get("maxDecimal"), maxDecimal.get().toString());
    }

    @Test
    public void testMaxDecimalValidationException() {
        String invalidEmailMessage = "Validation failed for field '" + "maxDecimal" + "': " + "the value must be lower";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(constructData("maxDecimal", "10.5"), ValidateMaxDecimal.class));
        assertEquals(invalidEmailMessage, exception.getMessage());
    }

    @Test
    public void testMinDecimalValidation() {
        Map<String, String> data = constructData("minDecimal", "1.0");
        ValidateMinDecimal maxDecimal = Validator.validate(data, ValidateMinDecimal.class);
        assertEquals(data.get("minDecimal"), maxDecimal.get().toString());
    }

    @Test
    public void testMinDecimalValidationException() {
        String invalidEmailMessage = "Validation failed for field '" + "minDecimal" + "': " + "the value must be higher";
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> Validator.validate(constructData("minDecimal", "0.4"), ValidateMinDecimal.class));
        assertEquals(invalidEmailMessage, exception.getMessage());
    }

    @Test
    public void testDigitsValidation() {
        Map<String, String> data = constructData("digits", "12345.67");
        ValidateDigits digits = Validator.validate(data, ValidateDigits.class);
        assertEquals(data.get("digits"), digits.get().toString());
    }

//    @Test
//    public void testDigitsValidationException() {
//        String invalidEmailMessage = "Validation failed for field '" + "digits" + "': " + "value out of limits";
//        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
//                () -> Validator.validate(constructData("digits", "123456.789"), ValidateDigits.class));
//        assertEquals(invalidEmailMessage, exception.getMessage());
//    }
//
//    @Test
//    public void testFutureValidation() {
//        Map<String, String> data = constructData("future", new Date(System.currentTimeMillis() + 1000).toString());
//        ValidateFuture future = Validator.validate(data, ValidateFuture.class);
//        assertEquals(data.get("digits"), future.get().toString());
//    }
}
