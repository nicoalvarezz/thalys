package io.thalys.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpStatusTest {

    @Test
    public void testStatusCode() {
        int expectedStatusCode = 200;
        String expectedMessage = "OK";
        HttpStatus.Category expectedCategory = HttpStatus.Category.SUCCESSFUL;

        HttpStatus status = HttpStatus.OK;

        assertEquals(expectedStatusCode, status.statusCode());
        assertEquals(expectedMessage, status.message());
        assertEquals(expectedCategory, status.category());
    }

    @Test
    public void test1xxStatus() {
        HttpStatus status = HttpStatus.CONTINUE;

        assertTrue(status.is1xxStatus());
        assertFalse(status.is2xxStatus());
        assertFalse(status.is3xxStatus());
        assertFalse(status.is4xxStatus());
        assertFalse(status.is5xxStatus());
    }

    @Test
    public void test2xxStatus() {
        HttpStatus status = HttpStatus.OK;

        assertTrue(status.is2xxStatus());
        assertFalse(status.is1xxStatus());
        assertFalse(status.is3xxStatus());
        assertFalse(status.is4xxStatus());
        assertFalse(status.is5xxStatus());
    }

    @Test
    public void test3xxStatus() {
        HttpStatus status = HttpStatus.FOUND;

        assertTrue(status.is3xxStatus());
        assertFalse(status.is2xxStatus());
        assertFalse(status.is1xxStatus());
        assertFalse(status.is4xxStatus());
        assertFalse(status.is5xxStatus());
    }

    @Test
    public void test4xxStatus() {
        HttpStatus status = HttpStatus.NOT_FOUND;

        assertTrue(status.is4xxStatus());
        assertFalse(status.is2xxStatus());
        assertFalse(status.is3xxStatus());
        assertFalse(status.is1xxStatus());
        assertFalse(status.is5xxStatus());
    }

    @Test
    public void test5xxStatus() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        assertTrue(status.is5xxStatus());
        assertFalse(status.is2xxStatus());
        assertFalse(status.is3xxStatus());
        assertFalse(status.is4xxStatus());
        assertFalse(status.is1xxStatus());
    }

    @Test
    public void testHttpStatusValueOf() {
        HttpStatus status = HttpStatus.OK;

        assertEquals(status, HttpStatus.valueOf(200));
        assertNotEquals(status, HttpStatus.valueOf(100));
        assertNotEquals(status, HttpStatus.valueOf(300));
        assertNotEquals(status, HttpStatus.valueOf(400));
        assertNotEquals(status, HttpStatus.valueOf(500));
    }

    @Test
    public void testCategoryValueOf() {
        HttpStatus.Category category = HttpStatus.Category.SUCCESSFUL;

        assertEquals(category, HttpStatus.Category.valueOf(200));
        assertNotEquals(category, HttpStatus.Category.valueOf(100));
        assertNotEquals(category, HttpStatus.Category.valueOf(300));
        assertNotEquals(category, HttpStatus.Category.valueOf(400));
        assertNotEquals(category, HttpStatus.Category.valueOf(500));
    }

    @Test
    public void testInvalidStatusCodeForValueOf() {
        Assertions.assertThrows(IllegalArgumentException.class,  () -> HttpStatus.valueOf(600));
    }

    @Test
    public void testInvalidCategoryForValueOf() {
        Assertions.assertThrows(IllegalArgumentException.class,  () -> HttpStatus.Category.valueOf(600));
    }

    @Test
    public void testCategoryValue() {
        HttpStatus.Category category = HttpStatus.Category.SUCCESSFUL;

        assertEquals(category.value(),2);
    }
}
