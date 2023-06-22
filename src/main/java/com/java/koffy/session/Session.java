package com.java.koffy.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Session implements SessionDriver {

    private final String sessionId;

    private final HashMap<String, Object> attributes = new HashMap<>();

    private final HashMap<String, ArrayList<String>> flash = new HashMap<>();

    private long creationTime;

    private static final String FLASH_KEY = "_flash";

    private static final String NEW_FLASH_KEY = "new";

    private static final String OLD_FLASH_KEY = "old";


    public Session() {
        this.sessionId = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        sessionIni();
    }

    private void sessionIni() {
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        flash.put(OLD_FLASH_KEY, new ArrayList<>());
        attributes.put(FLASH_KEY, flash);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public Object get(String key) {
        return attributes.get(key);
    }

    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    @Override
    public void set(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void remove(String key) {
        if (!key.equals(FLASH_KEY)) {
            attributes.remove(key);
        }
    }

    @Override
    public void flash(String key, Object value) {
        attributes.put(key, value);

        flash.get(NEW_FLASH_KEY).add(key);
        attributes.put(FLASH_KEY, flash);
    }

    public void ageFlashData() {
        flash.put(OLD_FLASH_KEY, flash.get(NEW_FLASH_KEY));
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        attributes.put(FLASH_KEY, flash);
    }

    @Override
    public boolean has(String key) {
        return attributes.containsKey(key);
    }

    public Map<String, String> attributes() {
        return attributes.keySet().stream().collect(Collectors.toMap(
                key -> key,
                key -> String.valueOf(attributes.get(key))
        ));
    }

    public void closeSession() {
        flash.put(OLD_FLASH_KEY, new ArrayList<>());
        ageFlashData();
        attributes.clear();
        attributes.put(FLASH_KEY, flash);
    }
}
