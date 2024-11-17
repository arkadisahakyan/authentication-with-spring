package arkadisahakyan.blog;

import arkadisahakyan.blog.util.Utilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Test
    void testNullableString() {
        assertEquals(null, Utilities.nullableString(""));
        assertEquals("hello world", Utilities.nullableString("hello world"));
    }

    @Test
    void testNotNullString() {
        assertEquals("", Utilities.notNullString(null));
        assertEquals("hello world", Utilities.notNullString("hello world"));
    }

    @Test
    void testParseLong() {
        Long expectedNumber = Utilities.parseLong("10");
        assertEquals(10L, expectedNumber);
        Long expectedNull = Utilities.parseLong("aaa");
        assertEquals(null, expectedNull);
    }
}