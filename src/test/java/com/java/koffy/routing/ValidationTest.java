package com.java.koffy.routing;

import com.java.koffy.routing.helpers.ValidateEmail;
import com.java.koffy.routing.helpers.ValidateNotNull;
import com.java.koffy.utils.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTest {

    private static Map<String, String> constructData(String field, String email) {
        return new HashMap<>() {{
            put(field, email);
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
}
