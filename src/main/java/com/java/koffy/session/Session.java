package com.java.koffy.session;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Http Session data structure.
 */
public class Session implements SessionDriver {

    private HttpSession httpSession;

    private static final String FLASH_KEY = "_flash";

    private static final String NEW_FLASH_KEY = "new";

    private static final String OLD_FLASH_KEY = "old";


    public Session(HttpSession session) {
        this.httpSession = session;
        if (!has(FLASH_KEY)) {
            sessionInit();
        }
    }

    /**
     * Initialise session with flash attributes.
     */
    private void sessionInit() {
        set(FLASH_KEY, new HashMap<>() {{
            put(NEW_FLASH_KEY, new ArrayList<>());
            put(OLD_FLASH_KEY, new ArrayList<>());
        }});
    }

    /**
     * Return creation time.
     * @return {@link Long}
     */
    @Override
    public long getCreationTime() {
        return httpSession.getCreationTime();
    }

    /**
     * Return session ID.
     * @return {@link Session}
     */
    @Override
    public String getId() {
        return httpSession.getId();
    }

    /**
     * Return a specific key from the attributes.
     * @param key {@link String}
     * @return {@link Object}
     */
    @Override
    public Object get(String key) {
        return httpSession.getAttribute(key);
    }

    /**
     * Return all the attribute keys in the session.
     * @return {@link Set}
     */
    @Override
    public Set<String> getAttributeNames() {
        return new HashSet<>(Collections.list(httpSession.getAttributeNames()));
    }

    /**
     * Set new attributes in the session.
     * @param key {@link String}
     * @param value {@link Object}
     */
    @Override
    public void set(String key, Object value) {
        httpSession.setAttribute(key, value);
    }

    /**
     * Remove attributes from the session.
     * Any attribute apart from '_flash' key is allowed to be removed.
     * @param key {@link String}
     */
    @Override
    public void remove(String key) {
        if (!key.equals(FLASH_KEY)) {
            httpSession.removeAttribute(key);
        }
    }

    /**
     * Method to store data in the flash {@link Map}.
     * Flash allows to store data will not be allowed in the subsequent request.
     * @param key {@link String}
     * @param value {@link Object}
     */
    @Override
    public void flash(String key, Object value) {
        set(key, value);

        Map<String, ArrayList<String>> flash = getFlashData();
        flash.get(NEW_FLASH_KEY).add(key);
        set(FLASH_KEY, flash);
    }

    /**
     * Method to handle flash data in the subsequent request.
     */
    public void ageFlashData() {
        Map<String, ArrayList<String>> flash = getFlashData();
        flash.put(OLD_FLASH_KEY, flash.get(NEW_FLASH_KEY));
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        set(FLASH_KEY, flash);
    }

    /**
     * Check if a specific attribute exists in the session.
     * @param key {@link String}
     * @return {@link Boolean}
     */
    @Override
    public boolean has(String key) {
        return httpSession.getAttribute(key) != null;
    }

    /**
     * Return all attributes in the session.
     * This method is created with the intention of printing keys and values.
     * @return {@link Map}
     */
    public Map<String, String> getAllAttributesStringFormat() {
        return getAttributeNames().stream().collect(Collectors.toMap(
                key -> key,
                key -> String.valueOf(get(key))
        ));
    }

    /**
     * Get all session attributes.
     * @return {@link Map}
     */
    public Map<String, Object> getAllAttributes() {
        return getAttributeNames().stream().collect(Collectors.toMap(
                key -> key,
                this::get
        ));
    }

    private Map<String, ArrayList<String>> getFlashData() {
        @SuppressWarnings("unchecked")
        Map<String, ArrayList<String>> flash = (Map<String, ArrayList<String>>) get(FLASH_KEY);
        return flash;
    }

    /**
     * Close http session.
     */
    public void closeSession() {
        Map<String, ArrayList<String>> flash = getFlashData();
        flash.put(OLD_FLASH_KEY, new ArrayList<>());
        ageFlashData();
        set(FLASH_KEY, flash);
    }
}
