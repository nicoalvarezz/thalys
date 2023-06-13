package com.java.koffy;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class QuickTest {

    @NotNull
    private String name;

    public QuickTest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Email
    private String email;
}
