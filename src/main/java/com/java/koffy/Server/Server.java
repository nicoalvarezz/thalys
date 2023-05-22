package com.java.koffy.Server;

import com.java.koffy.http.HttpMethod;

import java.util.Map;

public interface Server {

    String getUri();

    HttpMethod getRequestMethod();

    Map<String, String> getPostData();

    Map<String, String> getQueryParams();
}
