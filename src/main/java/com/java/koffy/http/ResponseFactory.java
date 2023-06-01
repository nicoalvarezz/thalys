package com.java.koffy.http;


interface ResponseFactory {

    KoffyResponse.Builder response(String contentType, String content);

    KoffyResponse.Builder response();
}
