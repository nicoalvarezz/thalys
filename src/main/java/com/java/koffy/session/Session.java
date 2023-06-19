package com.java.koffy.session;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session implements SessionDriver {

    private HttpSession session;

    private static final String FLASH_KEY = "_flash";
    private static final String NEW_FLASH_KEY = "new";
    private static final String OLD_FLASH_KEY = "old";

    public Session(HttpSession session) {
        this.session = session;

        if (!has(FLASH_KEY)) {
            session.setAttribute(FLASH_KEY, new HashMap<>() {{
                put(OLD_FLASH_KEY, new ArrayList<>());
                put(NEW_FLASH_KEY, new ArrayList<>());
            }});
        }
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public ServletContext getServletContext() {
       return session.getServletContext();
    }

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
    public void flash(String key, Object value) {
        session.setAttribute(key, value);
        Map<String, ArrayList<String>> flash = flashed();

        flash.get(NEW_FLASH_KEY).add(key);
        session.setAttribute(FLASH_KEY, flash);
    }

    private void ageFlashData() {
        Map<String, ArrayList<String>> flash = flashed();
        flash.put(OLD_FLASH_KEY, flash.get(NEW_FLASH_KEY));
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        session.setAttribute(FLASH_KEY, flash);

    }

    @Override
    public void invalidate() {
        session.invalidate();
    }

    @Override
    public boolean has(String key) {
        return session.getAttribute(key) != null;
    }

    public Map<String, String> attributes() {
        Map<String, String> theAttributes = new HashMap<>();
        for (String key : Collections.list(session.getAttributeNames())) {
            theAttributes.put(key, String.valueOf(session.getAttribute(key)));
        }
        return theAttributes;
    }

    public void closeSession() {
        Map<String, ArrayList<String>> flash = flashed();
        flash.put(OLD_FLASH_KEY, new ArrayList<>());
        ageFlashData();
        invalidate();
    }

    private Map<String, ArrayList<String>> flashed() {
        @SuppressWarnings("unchecked")
        HashMap<String, ArrayList<String>> flash = (HashMap<String, ArrayList<String>>) session.getAttribute(FLASH_KEY);
        return flash;
    }
}
