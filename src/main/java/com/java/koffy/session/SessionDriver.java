package com.java.koffy.session;

import jakarta.servlet.ServletContext;

import java.util.List;

public interface SessionDriver {

    long getCreationTime();

    String getId();

    Object getAttribute(String key);

    List<String> getAttributeNames();

    void setAttribute(String key, Object value);

    void removeAttribute(String key);

    void flash(String key, Object value);

    ServletContext getServletContext();

    void invalidate();

    boolean has(String key);
}
