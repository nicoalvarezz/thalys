package com.java.koffy.http;


interface ResponseFactory {

    ResponseEntity.Builder response(String contentType, String content);

    ResponseEntity.Builder response();
}
