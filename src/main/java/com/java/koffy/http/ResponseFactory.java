package com.java.koffy.http;


import com.java.koffy.http.Headers.ContentType;

interface ResponseFactory {

    ResponseEntity.Builder response(ContentType contentType, String content, HttpStatus status);
}
