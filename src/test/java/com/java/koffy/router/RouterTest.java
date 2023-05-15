package com.java.koffy.router;

import org.eclipse.jetty.server.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

public class RouterTest {

    private Router router;
    private Request mockRequest;
    private HttpServletRequest mockHttpServletRequest;
    private HttpServletResponse mockHttpServletResponse;

    PrintWriter mockPrintWriter;

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

        when(mockRequest.getMethod()).thenReturn("GET");
        when(mockRequest.getRequestURI()).thenReturn("/test");
        when(mockHttpServletResponse.getWriter()).thenReturn(mockPrintWriter);

        router.get(uri, action);

        router.handle(null, mockRequest, mockHttpServletRequest, mockHttpServletResponse);

        verify(mockHttpServletResponse.getWriter()).println(action.get());
        
    }

    public void tesResolveMultipleBasicrRoutes() {

    }

    public void testResolveMultpleBasicRoutesWithDifferentHttpMethods() {

    }
}
