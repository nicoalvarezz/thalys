package com.java.koffy.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Http Session data structure.
 */
public class Session implements SessionDriver {

    /**
     * Session id.
     */
    private final String sessionId;

    /**
     * Session attributes.
     */
    private final HashMap<String, Object> attributes = new HashMap<>();

    /**
     * Flash attributes.
     */
    private final HashMap<String, ArrayList<String>> flash = new HashMap<>();

    /**
     * Creation time.
     */
    private long creationTime;

    private static final String FLASH_KEY = "_flash";

    private static final String NEW_FLASH_KEY = "new";

    private static final String OLD_FLASH_KEY = "old";


    public Session() {
        this.sessionId = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        sessionInit();
    }

    /**
     * Initialise session with flash attributes.
     */
    private void sessionInit() {
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        flash.put(OLD_FLASH_KEY, new ArrayList<>());
        attributes.put(FLASH_KEY, flash);
    }

    /**
     * Return creation time.
     * @return {@link Long}
     */
    @Override
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * Return session ID.
     * @return {@link Session}
     */
    @Override
    public String getId() {
        return sessionId;
    }

    /**
     * Return a specific key from the attributes.
     * @param key {@link String}
     * @return {@link Object}
     */
    @Override
    public Object get(String key) {
        return attributes.get(key);
    }

    /**
     * Return all the attribute keys in the session.
     * @return {@link Set}
     */
    @Override
    public Set<String> getAttributeNames() {
        return attributes.keySet();
    }

    /**
     * Set new attributes in the session.
     * @param key {@link String}
     * @param value {@link Object}
     */
    @Override
    public void set(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * Remove attributes from the session.
     * Any attribute apart from '_flash' key is allowed to be removed.
     * @param key {@link String}
     */
    @Override
    public void remove(String key) {
        if (!key.equals(FLASH_KEY)) {
            attributes.remove(key);
        }
    }

    /**
     * Flash.
     * @param key
     * @param value
     */
    @Override
    public void flash(String key, Object value) {
        attributes.put(key, value);

        flash.get(NEW_FLASH_KEY).add(key);
        attributes.put(FLASH_KEY, flash);
    }

    /**
     * Age flash.
     */
    public void ageFlashData() {
        flash.put(OLD_FLASH_KEY, flash.get(NEW_FLASH_KEY));
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        attributes.put(FLASH_KEY, flash);
    }

    /**
     * Check if a specific attribute exists in the session.
     * @param key {@link String}
     * @return {@link Boolean}
     */
    @Override
    public boolean has(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Return all attributes in the session.
     * This method is created with the intention of printing keys and values.
     * @return {@link Map}
     */
    public Map<String, String> getAllAttributesStringFormat() {
        return attributes.keySet().stream().collect(Collectors.toMap(
                key -> key,
                key -> String.valueOf(attributes.get(key))
        ));
    }

    /**
     * Get all session attributes.
     * @return {@link Map}
     */
    public Map<String, Object> getAllAttributes() {
        return attributes;
    }

    /**
     * Close http session.
     */
    public void closeSession() {
        flash.put(OLD_FLASH_KEY, new ArrayList<>());
        ageFlashData();
        attributes.clear();
        attributes.put(FLASH_KEY, flash);
    }
}
