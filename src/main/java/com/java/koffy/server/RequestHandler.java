package com.java.koffy.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.koffy.container.Container;
import com.java.koffy.http.Headers.HttpHeader;
import com.java.koffy.http.Headers.HttpHeaders;
import com.java.koffy.http.HttpMethod;
import com.java.koffy.http.RequestEntity;
import com.java.koffy.routing.Router;
import org.eclipse.jetty.server.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * The `RequestHandler` class is responsible for processing and handling incoming HTTP requests within
 * the Koffy framework. It constructs a `RequestEntity` object that encapsulates various aspects of
 * the incoming request, such as URI, method, headers, and data.
 */
public class RequestHandler {

    private Router router = Container.resolve(Router.class);

    /**
     * Jackson mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Build {@link RequestEntity} from the server request.
     * @param request Server request
     * @return {@link RequestEntity}
     * @throws IOException
     */
    public RequestEntity handleRequest(Request request) throws IOException {
        return RequestEntity.builder()
                .uri(request.getRequestURI())
                .method(HttpMethod.valueOf(request.getMethod()))
                .headers(resolveHeaders(request))
                .postData(parsePostData(request))
                .queryData(parseQueryData(request))
                .route(router.resolveRoute(request.getRequestURI(), HttpMethod.valueOf(request.getMethod())))
                .build();
    }

    /**
     * Gather and save headers from the request.
     * @param request Server request
     * @return {@link Map} Headers
     */
    private HttpHeaders resolveHeaders(Request request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        HttpHeaders headers = new HttpHeaders();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.add(HttpHeader.headerOf(headerName.toLowerCase()), request.getHeader(headerName));
        }
        return headers;
    }

    /**
     * Parse post data received from the Server {@link Request}.
     * @param request Server request
     * @return {@link Map} post data
     * @throws IOException
     */
    private Map<String, String> parsePostData(Request request) throws IOException {
        String data = new BufferedReader(new InputStreamReader(request.getInputStream())).readLine();

        if (data == null) {
            return new HashMap<>();
        }

        // consider application/x-www-form-urlencoded header check. It is a more sophisticated approach
        // curl -X POST -d "key1=value1&key2=value2" -H "Content-Type:
        // application/x-www-form-urlencoded" http://example.com/api/endpoint
        if (data.contains("&")) {
            return parseUrlEncoded(data);
        }
        return MAPPER.readValue(data, Map.class);
    }

    /**
     * Return url-encoded formatted string to {@link Map}.
     * @param data url-encoded format
     * @return {@link Map} data
     */
    private Map<String, String> parseUrlEncoded(String data) {
        return Arrays.stream(data.split("&"))
                .map(param -> param.split("="))
                .collect(HashMap::new, (m, arr) -> m.put(arr[0], arr[1]), HashMap::putAll);
    }

    /**
     * Parse query data received from the Server {@link Request}.
     * @param request Server request
     * @return {@link Map} Query data
     */
    private Map<String, String> parseQueryData(Request request) {
        if (request.getQueryString() != null) {
            return parseUrlEncoded(request.getQueryString());
        }
        return new HashMap<>();
    }
}
