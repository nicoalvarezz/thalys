package com.java.koffy.validation.validatables;

import com.java.koffy.validation.annotations.Email;

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
