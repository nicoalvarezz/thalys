package com.java.koffy.session;

import jakarta.servlet.ServletContext;

import java.util.List;

public interface SessionDriver {

    long getCreationTime();

    String getId();

    Object get(String key);

    List<String> getAttributeNames();

    void set(String key, Object value);

    void remove(String key);

    void flash(String key, Object value);

    ServletContext getServletContext();

    void invalidate();

    boolean has(String key);
}
