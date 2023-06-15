package com.java.koffy.routing.helpers;

import javax.validation.constraints.Future;
import java.sql.Date;

public class ValidateFuture {

    @Future
    private Date future;

    public ValidateFuture() {
    }

    public Date get() {
        return future;
    }

    public void setFuture(Date future) {
        this.future = future;
    }
}
