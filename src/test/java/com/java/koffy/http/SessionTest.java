package com.java.koffy.http;


import com.java.koffy.session.Session;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionTest {

    @Mock
    private HttpSession mockHttpSession = mock(HttpSession.class);

    private Session session;

    private static final String FLASH_KEY = "_flash";

    private static final String NEW_FLASH_KEY = "new";

    private static final String OLD_FLASH_KEY = "old";

    private static final String TEST_KEY = "some_key";

    private static final String TEST_VALUE = "some_value";

    @BeforeEach
    public void setUp() {
        session = new Session(mockHttpSession);
    }

    @Test
    public void testGetCreationTime() {
        long expectedCreationTime = System.currentTimeMillis();
        when(mockHttpSession.getCreationTime()).thenReturn(expectedCreationTime);

        assertEquals(expectedCreationTime, session.getCreationTime());
        verify(mockHttpSession).getCreationTime();
    }

    @Test
    public void testFlaskKey() {
        Map<String, ArrayList<String>> flash = new HashMap<>() {{
            put(OLD_FLASH_KEY, new ArrayList<>());
            put(NEW_FLASH_KEY, new ArrayList<>());
        }};
        when(mockHttpSession.getAttribute(FLASH_KEY)).thenReturn(flash);

        assertEquals(flash, session.get(FLASH_KEY));
    }

    @Test
    public void testGetId() {
        String mockSessionId = UUID.randomUUID().toString();
        when(mockHttpSession.getId()).thenReturn(mockSessionId);
        assertEquals(mockSessionId, session.getId());
    }


    @Test
    public void testSetAndGetAttributes() {
        when(mockHttpSession.getAttribute(TEST_KEY)).thenReturn(TEST_VALUE);
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
        when(mockHttpSession.getAttributeNames()).thenReturn(Collections.enumeration(names));
        assertEquals(new HashSet<>(names), session.getAttributeNames());
    }

    @Test
    public void testRemoveAttributes() {
        when(mockHttpSession.getAttribute(TEST_KEY)).thenReturn(TEST_VALUE);

        session.remove(TEST_KEY);

        // Verify that the attribute was removed from the HttpSession
        verify(mockHttpSession).removeAttribute(TEST_KEY);
    }

    @Test
    public void testFlash() {
        Map<String, ArrayList<String>> flashData = new HashMap<>();
        flashData.put(NEW_FLASH_KEY, new ArrayList<>());
        flashData.put(OLD_FLASH_KEY, new ArrayList<>());

        when(mockHttpSession.getAttribute(FLASH_KEY)).thenReturn(flashData);

        session.flash(TEST_KEY, TEST_VALUE);

        // Verify that the attribute was set in the HttpSession
        verify(mockHttpSession).setAttribute(TEST_KEY, TEST_VALUE);

        // Verify that the flash data was updated correctly
        verify(mockHttpSession).setAttribute(FLASH_KEY, flashData);
    }

    @Test
    public void testAgeFlashData() {
        Map<String, ArrayList<String>> flashData = new HashMap<>();
        ArrayList<String> newFlashList = new ArrayList<>();
        newFlashList.add(TEST_KEY);
        flashData.put(NEW_FLASH_KEY, newFlashList);
        flashData.put(OLD_FLASH_KEY, new ArrayList<>());

        when(mockHttpSession.getAttribute(FLASH_KEY)).thenReturn(flashData);

        session.ageFlashData();

        // Verify that the flash data was aged correctly
        ArrayList<String> oldFlashList = flashData.get(OLD_FLASH_KEY);
        ArrayList<String> newFlashListAfterAging = flashData.get(NEW_FLASH_KEY);

        assertEquals(newFlashList, oldFlashList);
        assertNotNull(newFlashListAfterAging);
        assertTrue(newFlashListAfterAging.isEmpty());

        // Verify that the updated flash data was set in the HttpSession
        verify(mockHttpSession).setAttribute(FLASH_KEY, flashData);
    }

    @Test
    public void testFlashKeyCannotBeRemoved() {
        when(mockHttpSession.getAttribute(FLASH_KEY)).thenReturn(new HashMap<>());
        session.remove(FLASH_KEY);
        assertTrue(session.has(FLASH_KEY));
    }

    @Test
    public void testGetAllAttributes() {
        Map<String, Object> expectedAttributes = new HashMap<>() {{
            put(TEST_KEY, TEST_KEY);
        }};
        when(mockHttpSession.getAttributeNames()).thenReturn(Collections.enumeration(expectedAttributes.keySet()));
        when(mockHttpSession.getAttribute(TEST_KEY)).thenReturn(expectedAttributes.get(TEST_KEY));
        when(mockHttpSession.getAttribute(FLASH_KEY)).thenReturn(expectedAttributes.get(FLASH_KEY));

        session.set(TEST_KEY, TEST_KEY);

        Map<String, Object> allAttributes = session.getAllAttributes();

        assertEquals(expectedAttributes, allAttributes);

        verify(mockHttpSession).getAttributeNames();
        verify(mockHttpSession).getAttribute(TEST_KEY);
    }
}
