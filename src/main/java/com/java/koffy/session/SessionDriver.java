package com.java.koffy.session;

import java.util.Set;

public interface SessionDriver {

    long getCreationTime();

    String getId();

    Object get(String key);

    Set<String> getAttributeNames();

    void set(String key, Object value);

    void remove(String key);

    void flash(String key, Object value);

    boolean has(String key);
}
