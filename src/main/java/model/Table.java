package model;

import utils.EntryLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to contain references to entries
 * Support adding & searching for entries.
 */
public class Table {
    private int size;
    private String name;
    private Map<String, Index> indexes;
    private List<String> schema;

    Table(String name, List<String> schema) {
        this.name = name;
        indexes = new HashMap<>();
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public List<String> getSchema() {
        return schema;
    }

    static public Table fromFile(String tableName, EntryLoader loader, String path, List<String> schema) {
        Table table = new Table(tableName, schema);
        table.add(loader.read(path));
        return table;
    }

    public void add(List<Entry> entries) {
        for (Entry entry : entries) {
            add(entry);
        }
    }

    public void add(Entry entry) {
        if (validateEntry(entry)) {
            addToIndexes(entry);
            size += 1;
        }

    }

    public int size() {
        return size;
    }

    public List<Entry> search(String field, String value) {
        Index index = indexes.get(field);
        if (index == null) {
            return List.of();
        }

        return index.search(value);
    }

    private void addToIndexes(Entry entry) {
        for (String field : schema) {
            if (!indexes.containsKey(field)) {
                indexes.put(field, new Index(field));
            }

            Index index = indexes.get(field);

            entry.addFieldToIndex(field, index);
        }
    }

    private boolean validateEntry(Entry entry) {
        for (String field : entry.fields()) {
            if (!schema.contains(field)) {
                return false;
            }
        }
        return true;
    }
}
