package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class IndexTest {
    @Test
    public void testIndexSearchReturnsNullOnInvalidKeys() {
        assertNull(new Index("i").search("nonexistent"));
    }

}