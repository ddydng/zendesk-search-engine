package service;

import model.Entry;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private Entry primary;
    private List<Secondary> secondaries;

    class Secondary {
        public final Entry entry;
        public final String prefix;
        public final String descField;

        public Secondary(Entry entry, String prefix, String descField) {
            this.entry = entry;
            this.prefix = prefix;
            this.descField = descField;
        }

        public String getId() {
            return entry.get("_id").toString();
        }

        public String getValString() {
            return entry.get(descField).toString();
        }

        public String getKeyString() {
            return prefix + getId();
        }
    }

    SearchResult() {
        secondaries = new ArrayList<>();
    }

    public void add(Entry entry) {
        primary = entry;
    }

    public void addSecondaryEntries(List<Entry> entries, String prefix, String descField) {
        if (entries != null) {
            for (Entry entry : entries) {
                addSecondaryEntry(entry, prefix, descField);
            }
        }
    }

    public void addSecondaryEntry(Entry entry, String prefix, String descField) {
        secondaries.add(new Secondary(entry, prefix, descField));
    }

    public String stringfyKeyValue(String key, String value) {
        return String.format("%-80s%-80s%n", key, value);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("-----------------\n");
        s.append("Primary result\n");
        s.append("----------------\n");
        for (String field : primary.fields()) {
            s.append(stringfyKeyValue(
                field, primary.get(field).toString()
            ));
        }

        s.append("-----------------\n");
        s.append("Secondary results\n");
        s.append("-----------------\n");
        for (Secondary secondary : secondaries) {
            s.append(stringfyKeyValue(secondary.getKeyString(), secondary.getValString()));
        }

        return s.toString();
    }

    public Entry getPrimary() {
        return primary;
    }

    public List<Secondary> getSecondaries() {
        return secondaries;
    }
}
