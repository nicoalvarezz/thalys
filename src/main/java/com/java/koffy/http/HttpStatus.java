package com.java.koffy.http;

public enum HttpStatus {

    CONTINUE(100, "Continue", Category.INFORMATIONAL),
    SWITCHING_PROTOCOLS(101, "Switching Protocols", Category.INFORMATIONAL),
    PROCESSING(102, "Processing", Category.INFORMATIONAL),
    EARLY_HINTS(103, "Early Hints", Category.INFORMATIONAL),
    OK(200, "OK", Category.SUCCESSFUL),
    CREATED(201, "Created", Category.SUCCESSFUL),
    ACCEPTED(202, "Accepted", Category.SUCCESSFUL),
    NON_AUTHORITATIVE_INFORMATION(203, " Non-Authoritative Information", Category.SUCCESSFUL),
    NO_CONTENT(204, "No content", Category.SUCCESSFUL),
    RESET_CONTENT(205, "Reset Content", Category.SUCCESSFUL),
    PARTIAL_CONTENT(206, "Partial Content", Category.SUCCESSFUL),
    MULTI_STATUS (207, "Multi-Status", Category.SUCCESSFUL),
    ALREADY_REPORTED(208, "Already Reported", Category.SUCCESSFUL),
    IM_USED(226, "IM Used", Category.SUCCESSFUL),
    FOUND(302, "Found", Category.REDIRECTION),
    NOT_FOUND(404, "Not Found", Category.CLIENT_ERROR);

    private final int statusCode;
    private final String message;
    private final Category category;

    HttpStatus(int statusCode, String message, Category category) {
        this.statusCode = statusCode;
        this.message = message;
        this.category = category;
    }

    public int statusCode() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    public Category category() {
        return category;
    }

    public boolean is1xxStatus() {
        return category() == Category.INFORMATIONAL;
    }

    public boolean is2xxStatus() {
        return category() == Category.SUCCESSFUL;
    }

    public boolean is3xxStatus() {
        return category() == Category.REDIRECTION;
    }

    public boolean is4xxStatus() {
        return category() == Category.CLIENT_ERROR;
    }

    public boolean is5xxStatus() {
        return category() == Category.SERVER_ERROR;
    }

    public HttpStatus valueOf(int statusCode) {
        HttpStatus httpStatus  = resolve(statusCode);
        if (httpStatus == null) {
            throw new IllegalArgumentException("No matching content for status code:" + statusCode);
        }
        return httpStatus;
    }

    private HttpStatus resolve(int statusCode) {
        HttpStatus[] httpStatuses = values();

        for (HttpStatus httpStatus : httpStatuses) {
            if (httpStatus.statusCode == statusCode) {
                return httpStatus;
            }
        }

        return null;
    }

    public enum Category {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Category(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Category valueOf(int statusCode) {
            Category category = resolve(statusCode);
            if (category == null) {
                throw new IllegalArgumentException("No matching content for status code: " + statusCode);
            }
            return category;
        }

        private static Category resolve(int statusCode) {
            int categoryCode = statusCode / 100;
            Category[] categories = values();

            for (Category category : categories) {
                if (category.value == categoryCode) {
                    return category;
                }
            }
            return null;
        }
    }
}
