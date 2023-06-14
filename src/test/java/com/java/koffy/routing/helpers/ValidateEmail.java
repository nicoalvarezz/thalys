package com.java.koffy.routing.helpers;

import com.java.koffy.validations.annotations.Email;

public class ValidateEmail {

    @Email
    private String email;

    public ValidateEmail() {

    }

    public String get() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
