package com.java.koffy.http;

/**
 * Http status.
 */
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
    MULTIPLE_CHOICES(300, "Multiple Choices", Category.REDIRECTION),
    MOVED_PERMANENTLY(301, "Moved Permanently", Category.REDIRECTION),
    FOUND(302, "Found", Category.REDIRECTION),
    SEE_OTHER(303, "See other", Category.REDIRECTION),
    NOT_MODIFIED(304, "Not modified", Category.REDIRECTION),
    USE_PROXY(305, "Use proxy", Category.REDIRECTION),
    UNUSED(306, "Unused", Category.REDIRECTION),
    TEMPORARY_REDIRECT(308, "Temporary Redirect", Category.REDIRECTION),
    BAD_REQUEST(400, "Bad Request", Category.CLIENT_ERROR),
    UNAUTHORIZED(401, "Unauthorized", Category.CLIENT_ERROR),
    PAYMENT_REQUIRED(402, "Payment required", Category.CLIENT_ERROR),
    FORBIDDEN(403, "Forbidden", Category.CLIENT_ERROR),
    NOT_FOUND(404, "Not Found", Category.CLIENT_ERROR),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", Category.CLIENT_ERROR),
    NOT_ACCEPTABLE(406, "Not acceptable", Category.CLIENT_ERROR),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required", Category.CLIENT_ERROR),
    REQUEST_TIMEOUT(408, "Request Timeout", Category.CLIENT_ERROR),
    CONFLICT(409, "Conflict", Category.CLIENT_ERROR),
    GONE(410, "Permanently Deleted From Server", Category.CLIENT_ERROR),
    LENGTH_REQUIRED(411, "Length Required", Category.CLIENT_ERROR),
    PRECONDITION_FAILED(412, "Precondition failed", Category.CLIENT_ERROR),
    PAYLOAD_TOO_LARGE(413, "Payload too large", Category.CLIENT_ERROR),
    URI_TOO_LARGE(414, "URI too large", Category.CLIENT_ERROR),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported media type", Category.CLIENT_ERROR),
    RANGE_NOT_SATISFIABLE(416, "Range not satisfiable", Category.CLIENT_ERROR),
    EXPECTATION_FAILED(417, "Expectation failed", Category.CLIENT_ERROR),
    MISDIRECTED_REQUEST(421, "Misdirected Request", Category.CLIENT_ERROR),
    UNPROCESSABLE_CONTENT(422, "Unprocessable content", Category.CLIENT_ERROR),
    LOCKED(423, "Locked", Category.CLIENT_ERROR),
    FAILED_DEPENDENCY(424, "Failed Dependency", Category.CLIENT_ERROR),
    TOO_EARLY(425, "Too early", Category.CLIENT_ERROR),
    UPGRADE_REQUIRED(426, "Upgrade required", Category.CLIENT_ERROR),
    PRECONDITION_REQUIRED(428, "Precondition required", Category.CLIENT_ERROR),
    TOO_MANY_REQUESTS(429, "Too many requests", Category.CLIENT_ERROR),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request header fields too large", Category.CLIENT_ERROR),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable for legal reasons", Category.CLIENT_ERROR),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", Category.SERVER_ERROR),
    NOT_IMPLEMENTED(501, "Not implemented", Category.SERVER_ERROR),
    BAD_GATEWAY(502, "Bad gateway", Category.SERVER_ERROR),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", Category.SERVER_ERROR),
    GATEWAY_TIMEOUT(504, "Gateway timeout", Category.SERVER_ERROR),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP version not supported", Category.SERVER_ERROR),
    VARIANT_ALSO_NEGOTIATES(506, "Variant also negotiates", Category.SERVER_ERROR),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required", Category.SERVER_ERROR);

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

    public static HttpStatus valueOf(int statusCode) {
        HttpStatus httpStatus  = resolve(statusCode);
        if (httpStatus == null) {
            throw new IllegalArgumentException("No matching content for status code:" + statusCode);
        }
        return httpStatus;
    }

    private static HttpStatus resolve(int statusCode) {
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
