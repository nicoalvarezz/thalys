package com.java.koffy.session;

import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session implements SessionDriver {

    private HttpSession session;

    private Map<String, Object> attributes = new HashMap<>();

    public Session(HttpSession session) {
        this.session = session;
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public String getId() {
        return session.getId();
    }

//    @Override
//    public ServletContext getServletContext() {
//        return;
//    }

//    @Override
//    public HttpSessionContext getSessionContext() {
//        //pending
//        return null;
//    }

    @Override
    public Object getAttribute(String key) {
        return session.getAttribute(key);
    }

    @Override
    public List<String> getAttributeNames() {
        return Collections.list(session.getAttributeNames());
    }

    @Override
    public void setAttribute(String key, Object value) {
        session.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        session.removeAttribute(key);
    }

    @Override
    public void flash() {

    }
}
