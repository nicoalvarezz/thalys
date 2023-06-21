package com.java.koffy.http;


import com.java.koffy.session.Session;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.UUID;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionTest {

    @Mock
    private HttpSession mockHttpSession;

    private Session session;

    private static final String FLASH_KEY = "_flash";

    private static final String NEW_FLASH_KEY = "new";

    private static final String OLD_FLASH_KEY = "old";

    private static final String TEST_KEY = "some_key";

    private static final String TEST_VALUE = "some_value";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = new Session(mockHttpSession);
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
        String id = String.valueOf(UUID.randomUUID());
        when(mockHttpSession.getId()).thenReturn(id);

        assertEquals(id, session.getId());
    }

    @Test
    public void testGetCreationTime() {
        long creationTime = 123456789L;
        when(mockHttpSession.getCreationTime()).thenReturn(creationTime);

        assertEquals(creationTime, session.getCreationTime());
    }

    @Test
    public void testGetServletContext() {
        ServletContext servletContext = mock(ServletContext.class);
        when(mockHttpSession.getServletContext()).thenReturn(servletContext);

        assertEquals(servletContext, session.getServletContext());
    }

    @Test
    public void testGetAttributes() {
        when(mockHttpSession.getAttribute(TEST_KEY)).thenReturn(TEST_VALUE);

        assertEquals(TEST_VALUE, session.get(TEST_KEY));
    }

    @Test
    public void testSetAttributes() {
        Session sessionMock = mock(Session.class);

        doNothing().when(sessionMock).set(TEST_KEY, TEST_VALUE);

        sessionMock.set(TEST_KEY, TEST_VALUE);

        verify(sessionMock).set(TEST_KEY, TEST_VALUE);
    }

    @Test
    public void testGetAttributeNames() {
        Vector<String> attributeNames = new Vector<>() {{
            add("name");
            add("another_name");
            add("test_name");
            add("test_test_name");
        }};
        when(mockHttpSession.getAttributeNames()).thenReturn(attributeNames.elements());

        assertEquals(attributeNames.stream().toList(), session.getAttributeNames());
    }

    @Test
    public void testRemoveAttributes() {
        Session sessionMock = mock(Session.class);

        doNothing().when(sessionMock).remove(TEST_KEY);

        sessionMock.remove(TEST_KEY);

        verify(sessionMock).remove(TEST_KEY);
    }

    @Test
    public void testFlashAndAgeFlashData() {
        Map<String, ArrayList<String>> flash = new HashMap<>() {{
            put(OLD_FLASH_KEY, new ArrayList<>());
            put(NEW_FLASH_KEY, new ArrayList<>());
        }};
        when(mockHttpSession.getAttribute(FLASH_KEY)).thenReturn(flash);
        session.flash(TEST_KEY, TEST_VALUE);
        flash.get(NEW_FLASH_KEY).add(TEST_KEY);

        assertEquals(flash, session.get(FLASH_KEY));

        session.ageFlashData();
        flash.put(NEW_FLASH_KEY, new ArrayList<>()) ;
        flash.get(OLD_FLASH_KEY).add(TEST_KEY);

        assertEquals(flash, session.get(FLASH_KEY));
    }

    @Test
    public void testInvalidate() {
        session.invalidate();

        verify(mockHttpSession).invalidate();
    }

    @Test
    public void testHasAttribute() {
        String value = TEST_VALUE;
        when(mockHttpSession.getAttribute(TEST_KEY)).thenReturn(value);

        assertTrue(session.has(TEST_KEY));
        assertFalse(session.has("any_key"));
    }
}
