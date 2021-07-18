package utils;

import model.Entry;

import java.util.List;

/**
 * This interface enables extensibility to various
 * kinds of source data, as long as they can be modelled into
 * lists of entries.
 */
public interface EntryLoader {
    public List<Entry> read(String path);
}
