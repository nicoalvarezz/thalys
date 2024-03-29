package com.github.nicoalvarezz.thalys.http.Headers;

/**
 * HTTP Content-Type.
 */
public enum ContentType {

    APPLICATION_EDI_X12("application/EDI-X12"),
    APPLICATION_EDIFACT("application/EDIFACT"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_OGG("application/ogg"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_X_SHOCKWAVE_FLASH("application/x-shockwave-flash"),
    APPLICATION_JSON("application/json"),
    APPLICATION_LD_JSON("application/ld+json"),
    APPLICATION_XML("application/xml"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    AUDIO_MPEG("audio/mpeg"),
    AUDIO_X_MS_WMA("audio/x-ms-wma"),
    AUDIO_VND_RN_REALAUDIO("audio/vnd.rn-realaudio"),
    AUDIO_X_WAV("audio/x-wav"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_TIFF("image/tiff"),
    IMAGE_VND_MICROSOFT_ICON("image/vnd.microsoft.icon"),
    IMAGE_X_ICON("image/x-icon"),
    IMAGE_VND_DJVU("image/vnd.djvu"),
    IMAGE_SVG_XML("image/svg+xml"),
    MULTIPART_MIXED("multipart/mixed"),
    MULTIPART_ALTERNATIVE("multipart/alternative"),
    MULTIPART_RELATED("multipart/related"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_CSS("text/css"),
    TEXT_CSV("text/csv"),
    TEXT_HTML("text/html"),
    TEXT_JAVASCRIPT("text/javascript"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    VIDEO_MPEG("video/mpeg"),
    VIDEO_MP4("video/mp4"),
    VIDEO_QUICKTIME("video/quicktime"),
    VIDEO_X_MS_WMV("video/x-ms-wmv"),
    VIDEO_X_MSVIDEO("video/x-msvideo"),
    VIDEO_X_FLV("video/x-flv"),
    VIDEO_WEBM("video/webm"),
    VND_OASIS_OPENDOCUMENT_TEXT("application/vnd.oasis.opendocument.text"),
    VND_OASIS_OPENDOCUMENT_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet"),
    VND_OASIS_OPENDOCUMENT_PRESENTATION("application/vnd.oasis.opendocument.presentation"),
    VND_OASIS_OPENDOCUMENT_GRAPHICS("application/vnd.oasis.opendocument.graphics"),
    VND_MS_EXCEL("application/vnd.ms-excel"),
    VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    VND_MS_POWERPOINT("application/vnd.ms-powerpoint"),
    VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION(
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    VND_MSWORD("application/msword"),
    VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    VND_MOZILLA_XUL_XML("application/vnd.mozilla.xul+xml");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Returns the String value of a specific content type.
     * @return {@link String} content type value
     */
    public String get() {
        return contentType;
    }

    /**
     *  Returns the {@link ContentType} of a specif {@link String} header name.
     * @param contentType {@link String}
     * @return {@link ContentType}
     */
    public static ContentType contentTypeOf(String contentType) {
        ContentType currentContentType = resolve(contentType);

        if (currentContentType == null) {
            throw new IllegalArgumentException("No matching content for header:" + contentType);
        }
        return currentContentType;
    }

    private static ContentType resolve(String contentType) {
        ContentType[] contentTypes = values();

        for (ContentType currentContentType : contentTypes) {
            if (currentContentType.get().equals(contentType)) {
                return currentContentType;
            }
        }
        return null;
    }
}
