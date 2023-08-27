package com.github.nicoalvarezz.thalys.http.Headers;

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
    CONTENT_LOCATION("content-location"),
    FORWARDED("forwarded"),
    X_FORWARDED_FOR("x-forwarded-for"),
    X_FORWARDED_HOST("x-forwarded-host"),
    X_FORWARDED_PROTO("x-forwarded-proto"),
    VIA("via"),
    REFRESH("refresh"),
    FROM("from"),
    REFERER("referer"),
    REFERRER_POLICY("referrer-policy"),
    ALLOW("allow"),
    ACCEPT_RANGES("accept-ranges"),
    RANGE("range"),
    IF_RANGE("if-range"),
    CONTENT_RANGE("content-range"),
    CROSS_ORIGIN_EMBEDDER_POLICY("cross-origin-embedder-policy"),
    CROSS_ORIGIN_OPENER_POLICY("cross-origin-opener-policy"),
    CROSS_ORIGIN_RESOURCE_POLICY("cross-origin-resource-policy"),
    CONTENT_SECURITY_POLICY("content-security-policy"),
    CONTENT_SECURITY_POLICY_REPORT_ONLY("content-security-policy-report-only"),
    EXPECT_CT("expect-ct"),
    ORIGIN_ISOLATION("origin-isolation"),
    PERMISSIONS_POLICY("permissions-policy"),
    STRICT_TRANSPORT_SECURITY("strict-transport-security"),
    UPGRADE_INSECURE_REQUESTS("upgrade-insecure-requests"),
    X_CONTENT_TYPE_OPTIONS("x-content-type-options"),
    X_FRAME_OPTIONS("x-frame-options"),
    X_PERMITTED_CROSS_DOMAIN_POLICIES("x-permitted-cross-domain-policies"),
    X_POWERED_BY("x-powered-by"),
    X_XSS_PROTECTION("x-xss-protection"),
    SEC_FETCH_SITE("sec-fetch-site"),
    SEC_FETCH_MODE("sec-fetch-mode"),
    SEC_FETCH_USER("sec-fetch-user"),
    SEC_FETCH_DEST("sec-fetch-dest"),
    SERVICE_WORKER_NAVIGATION_PRELOAD("service-worker-navigation-preload"),
    LAST_EVENT_ID("last-event-id"),
    NEL("nel"),
    PING_FROM("ping-from"),
    PING_TO("ping-to"),
    REPORT_TO("report-to"),
    TRANSFER_ENCODING("transfer-encoding"),
    TE("te"),
    TRAILER("trailer"),
    SEC_WEBSOCKET_KEY("sec-websocket-key"),
    SEC_WEBSOCKET_EXTENSIONS("sec-websocket-extensions"),
    SEC_WEBSOCKET_ACCEPT("sec-websocket-accept"),
    SEC_WEBSOCKET_PROTOCOL("sec-websocket-protocol"),
    SEC_WEBSOCKET_VERSION("sec-websocket-version"),
    ACCEPT_PUSH_POLICY("accept-push-policy"),
    ACCEPT_SIGNATURE("accept-signature"),
    ALT_SVC("alt-svc"),
    DATE("date"),
    EARLY_DATA("early-data"),
    LARGE_ALLOCATION("large-allocation"),
    LINK("link"),
    PUSH_POLICY("push-policy"),
    RETRY_AFTER("retry-after"),
    SIGNATURE("signature"),
    SIGNED_HEADERS("signed-headers"),
    SERVER_TIMING("server-timing"),
    SERVICE_WORKER_ALLOWED("service-worker-allowed"),
    SOURCE_MAP("source-map"),
    UPGRADE("upgrade"),
    X_DNS_PREFETCH_CONTROL("x-dns-prefetch-control"),
    X_FIREFOX_SPDY("x-firefox-spdy"),
    X_PINGBACK("x-pingback"),
    X_REQUESTED_WITH("x-requested-with"),
    X_ROBOTS_TAG("x-robots-tag");


    private final String headerName;

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }

    /**
     * Returns the {@link String} value of a specific header name..
     * @return {@link String} HTTP header name
     */
    public String get() {
        return headerName;
    }

    /**
     * Returns the {@link HttpHeader} of a specif {@link String} header name.
     * @param header {@link String}
     * @return {@link HttpHeader}
     * @throws IllegalArgumentException
     */
    public static HttpHeader headerOf(String header) {
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
