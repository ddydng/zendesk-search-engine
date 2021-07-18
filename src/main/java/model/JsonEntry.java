package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JsonEntry is a extended subclass of JSONObject and
 * implements Entry interface. JSONObject is the object returned
 * from a JSON file parser and support map-like operations such
 * as get and keySet, making it a perfect implementation for
 * the sample data.
 */
public class JsonEntry extends JSONObject implements Entry {
    private List<String> schema;
    public JsonEntry(JSONObject obj, List<String> schema) {
        super(obj);
        this.schema = schema;
        if (!validate()) {
            throw new RuntimeException("Invalid entry");
        }
    }

    @Override
    public Object get(String field) {
        return super.get(field);
    }

    @Override
    public boolean containsField(String field) {
        return super.containsKey(field);
    }

    @Override
    public List<String> fields() {
        return schema.stream().filter(s -> super.keySet().contains(s)).collect(Collectors.toList());
    }

    @Override
    public void addFieldToIndex(String field, Index index) {
        Object value = "";
        if (containsField(field)) {
            value = get(field);
        }

        if (value instanceof JSONArray) {
            for (Object valObj : (JSONArray) value) {
                index.add(valObj.toString(), this);
            }
        } else {
            index.add(value.toString(), this);
        }
    }

    @Override
    public boolean validate() {
        for (Object fieldOjb : super.keySet()) {
            if (!schema.contains(fieldOjb.toString())) {
                return false;
            }
        }
        return true;
    }

}
