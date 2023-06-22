package com.java.koffy.http;


import com.java.koffy.session.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SessionTest {

    private Session session;

    private static final String FLASH_KEY = "_flash";

    private static final String NEW_FLASH_KEY = "new";

    private static final String OLD_FLASH_KEY = "old";

    private static final String TEST_KEY = "some_key";

    private static final String TEST_VALUE = "some_value";

    @BeforeEach
    public void setUp() {
        session = new Session();
    }

    @Test
    public void testFlaskKey() {
        Map<String, ArrayList<String>> flash = new HashMap<>() {{
            put(OLD_FLASH_KEY, new ArrayList<>());
            put(NEW_FLASH_KEY, new ArrayList<>());
        }};

        assertEquals(flash, session.get(FLASH_KEY));
    }

    @Test
    public void testGetId() {
        assertNotNull(session.getId());
    }


    @Test
    public void testSetAndGetAttributes() {
        session.set(TEST_KEY, TEST_VALUE);
        assertTrue(session.has(TEST_KEY));
        assertEquals(TEST_VALUE, session.get(TEST_KEY));
    }


    @Test
    public void testGetAttributeNames() {
        List<String> names = new ArrayList<>() {{
            add(TEST_KEY);
            add(FLASH_KEY);
        }};
        session.set(TEST_KEY, TEST_VALUE);
        assertEquals(new HashSet<>(names), session.getAttributeNames());
    }

    @Test
    public void testRemoveAttributes() {
        session.set(TEST_KEY, TEST_VALUE);
        assertTrue(session.has(TEST_KEY));

        session.remove(TEST_KEY);
        assertFalse(session.has(TEST_KEY));
    }

    @Test
    public void testFlashAndAgeFlashData() {
        Map<String, ArrayList<String>> flash = (Map<String, ArrayList<String>>) session.get(FLASH_KEY);
        assertTrue(flash.get(NEW_FLASH_KEY).isEmpty());
        assertTrue(flash.get(OLD_FLASH_KEY).isEmpty());

        session.flash(TEST_KEY, TEST_VALUE);
        flash.put(NEW_FLASH_KEY, new ArrayList<>() {{ add(TEST_VALUE); }});
        assertEquals(TEST_VALUE, session.get(TEST_KEY));
        assertEquals(flash, session.get(FLASH_KEY));

        session.ageFlashData();
        flash.put(NEW_FLASH_KEY, new ArrayList<>());
        flash.put(OLD_FLASH_KEY, new ArrayList<>() {{ add(TEST_VALUE); }});
        assertEquals(flash, session.get(FLASH_KEY));
    }

    @Test
    public void testFlashKeyCannotBeRemoved() {
        session.remove(FLASH_KEY);
        assertTrue(session.has(FLASH_KEY));
    }

    @Test
    public void testGetAllAttributes() {
        Map<String, Object> expectedAttributes = new HashMap<>() {{
            put(TEST_KEY, TEST_VALUE);
            put(FLASH_KEY, new HashMap<>() {{
                put(NEW_FLASH_KEY, new ArrayList<>());
                put(OLD_FLASH_KEY, new ArrayList<>());
            }});
        }};
        session.set(TEST_KEY, TEST_VALUE);

        assertEquals(expectedAttributes, session.getAllAttributes());
    }
}
