package com.java.koffy.http.Headers;

/**
 * HTTP Header enum.
 * It allows to access already existing headers as constants
 * This approach offers the advantage of type safety and can help prevent errors when referencing header names.
 */
public enum HttpHeader {

    LOCATION("location"),
    CONTENT_TYPE("content-type"),
    AUTHORIZATION("authorization"),
    ACCEPT("accept"),
    USER_AGENT("user-agent"),
    HOST("host"),
    CACHE_CONTROL("cache-control"),
    CONTENT_LENGTH("content-length"),
    IF_MODIFIED_SINCE("if-modified-since"),
    SERVER("server"),
    ETAG("etag"),
    WWW_AUTHENTICATE("www-authenticate"),
    PROXY_AUTHENTICATE("proxy-authenticate"),
    PROXY_AUTHORIZATION("proxy-authorization"),
    AGE("age"),
    CLEAR_SITE_DATA("clear-site-data"),
    EXPIRES("expires"),
    PRAGMA("pragma"),
    WARNING("warning"),
    ACCEPT_CH("accept-ch"),
    ACCEPT_CH_LIFETIME("accept-ch-lifetime"),
    CRITICAL_CH("critical-ch"),
    SEC_CH_PREFERS_REDUCED_MOTION("sec-ch-prefers-reduced-motion"),
    SEC_CH_UA("sec-ch-ua"),
    SEC_CH_UA_ARCH("Sec-CH-UA-Arch"),
    SEC_CH_UA_BITNESS("sec-ch-ua-bitness"),
    SEC_CH_UA_FULL_VERSION("sec-ch-ua-full-version"),
    SEC_CH_UA_FULL_VERSION_LIST("sec-ch-ua-full-version-list"),
    SEC_CH_UA_MOBILE("sec-ch-ua-mobile"),
    SEC_CH_UA_MODEL("sec-ch-ua-model"),
    SEC_CH_UA_PLATFORM("sec-ch-ua-platform"),
    SEC_CH_UA_PLATFORM_VERSION("sec-ch-ua-platform-version"),
    CONTENT_DPR("content-dpr"),
    DEVICE_MEMORY("device-memory"),
    DPR("dpr"),
    VIEWPORT_WIDTH("viewport-width"),
    WIDTH("width"),
    DOWNLINK("downlink"),
    ECT("ect"),
    RTT("rtt"),
    SAVE_DATA("save-data"),
    LAST_MODIFIED("last-modified"),
    IF_MATCH("if-match"),
    IF_NONE_MATCH("if-none-match"),
    IF_UNMODIFIED_SINCE("if-unmodified-since"),
    VARY("vary"),
    CONNECTION("connection"),
    KEEP_ALIVE("keep-alive"),
    ACCEPT_ENCODING("accept-encoding"),
    ACCEPT_LANGUAGE("accept-language"),
    EXPECT("expect"),
    MAX_FORWARDS("max-forwards"),
    COOKIE("cookie"),
    SET_COOKIE("set-cookie"),
    ACCESS_CONTROL_ALLOW_ORIGIN("access-control-allow-origin"),
    ACCESS_CONTROL_ALLOW_CREDENTIALS("access-control-allow-credentials"),
    ACCESS_CONTROL_ALLOW_HEADERS("access-control-allow-headers"),
    ACCESS_CONTROL_ALLOW_METHODS("access-control-allow-methods"),
    ACCESS_CONTROL_EXPOSE_HEADERS("access-control-expose-headers"),
    ACCESS_CONTROL_MAX_AGE("access-control-max-age"),
    ACCESS_CONTROL_REQUEST_HEADERS("access-control-request-headers"),
    ACCESS_CONTROL_REQUEST_METHOD("access-control-request-method"),
    ORIGIN("origin"),
    TIMING_ALLOW_ORIGIN("timing-allow-origin"),
    CONTENT_DISPOSITION("content-disposition"),
    CONTENT_ENCODING("content-encoding"),
    CONTENT_LANGUAGE("content-language"),
    CONTENT_LOCATION("content-location");


    private final String headerName;

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }

    /**
     * Get header name.
     * @return {@link String}
     */
    public String get() {
        return headerName;
    }

    /**
     * Get HttpHeader from a given string.
     * @param header {@link String}
     * @return {@link HttpHeader}
     * @throws IllegalArgumentException
     */
    public HttpHeader headerOf(String header) {
        HttpHeader currentHeader = resolve(header);
        if (currentHeader == null) {
            throw new IllegalArgumentException("No matching content for header:" + header);
        }
        return currentHeader;
    }

    private static HttpHeader resolve(String header) {
        HttpHeader[] headers = values();

        for (HttpHeader currentHeader : headers) {
            if (currentHeader.headerName.equals(header)) {
                return currentHeader;
            }
        }
        return null;
    }
}
