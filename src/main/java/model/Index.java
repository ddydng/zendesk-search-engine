package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Index class contains mapping between values
 * and entries sharing the values. This data structure
 * enables O(1) searching performance on full matches
 */
public class Index {
    private Map<String, List<Entry>> map;
    private String field;

    public Index(String field) {
        map = new HashMap<>();
        this.field = field;
    }

    public List<Entry> search(String value) {
        return map.get(value);
    }

    public void add(String value, Entry entry) {
        if (!map.containsKey(value)) {
            map.put(value, new ArrayList<>());
        }
        map.get(value).add(entry);
    }
}
