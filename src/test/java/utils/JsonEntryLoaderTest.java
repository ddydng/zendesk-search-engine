package utils;

import org.junit.Test;
import java.util.List;

public class JsonEntryLoaderTest {
    @Test(expected = RuntimeException.class)
    public void testLoaderThrowsOnInvalidPath() {
        EntryLoader loader = new JsonEntryLoader(List.of());
        loader.read("Invalid/path");
    }
}