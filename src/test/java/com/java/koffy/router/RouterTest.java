package com.java.koffy.router;

import com.java.koffy.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class RouterTest {

    private Router router;
    private Request mockRequest;
    private HttpServletRequest mockHttpServletRequest;
    private HttpServletResponse mockHttpServletResponse;
    private PrintWriter mockPrintWriter;

    @BeforeEach
    void setUp() {
        router = new Router();
        mockRequest = mock(Request.class);
        mockHttpServletRequest = mock(HttpServletRequest.class);
        mockHttpServletResponse = mock(HttpServletResponse.class);
        mockPrintWriter = mock(PrintWriter.class);
    }

    @Test
    public void testResolveBasicRoute() throws ServletException, IOException {

        String uri = "/test";
        Supplier<Object> action = () -> "test";

        when(mockRequest.getMethod()).thenReturn(HttpMethod.GET.toString());
        when(mockRequest.getRequestURI()).thenReturn("/test");
        when(mockHttpServletResponse.getWriter()).thenReturn(mockPrintWriter);

        router.get(uri, action);

        router.handle(null, mockRequest, mockHttpServletRequest, mockHttpServletResponse);

        assertEquals(uri, router.getCurrentRoute().getUri());
        assertEquals(action.get(), router.getCurrentRoute().getAction().get());
        
    }

    @Test
    public void tesResolveMultipleBasicRoutes() throws IOException, ServletException {
        HashMap<String, Supplier<Object>> routes = new HashMap<>() {{
            put("/test", () -> "test");
            put("/foo", () -> "foo");
            put("/bar", () -> "bar");
            put("/long/nested/route", () -> "long nested route");
        }};

        for (String uri : routes.keySet()) {
            router.get(uri, routes.get(uri));

            when(mockRequest.getMethod()).thenReturn(HttpMethod.GET.toString());
            when(mockRequest.getRequestURI()).thenReturn(uri);
            when(mockHttpServletResponse.getWriter()).thenReturn(mockPrintWriter);

            router.handle(null, mockRequest, mockHttpServletRequest, mockHttpServletResponse);

            assertEquals(uri, router.getCurrentRoute().getUri());
            assertEquals(routes.get(uri).get(), router.getCurrentRoute().getAction().get());

        }
    }

//    @Test
//    public void testResolveMultipleBasicRoutesWithDifferentHttpMethods() throws IOException, ServletException {
//        HashMap<HttpMethod, Map<String, Supplier<Object>>> routes = new HashMap<>(){{
//            put(HttpMethod.GET, new HashMap<>() {{ put("/test", () -> "get"); put("/random/get", () -> "get"); }});
//            put(HttpMethod.POST, new HashMap<>() {{ put("/test", () -> "post"); put("/random/nested/post", () -> "post"); }});
//            put(HttpMethod.PATCH, new HashMap<>() {{ put("/test", () -> "patch"); put("/some/patch/route", () -> "patch"); }});
//            put(HttpMethod.PUT, new HashMap<>() {{ put("/test", () -> "put");put("/put/random/route", () -> "put"); }});
//            put(HttpMethod.DELETE, new HashMap<>() {{ put("/test", () -> "delete"); put("/d", () -> "delete"); }});
//        }};
//
//    }
}
