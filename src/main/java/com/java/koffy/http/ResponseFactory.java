package com.java.koffy.http;

import java.util.Map;

interface ResponseFactory {

    KoffyResponse response(int status, String contentType, String content);

    KoffyResponse response(int status, String name, String value, String content);

    KoffyResponse response(int status, String contentType, Map<String, String> headers, String content);
}
