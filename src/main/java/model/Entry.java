package model;

import java.util.List;

/**
 * A row in a table. The interface is designed to fit for
 * different types of tabular data (xml, json, csv, etc...)
 */
public interface Entry {

    Object get(String field);

    boolean containsField(String field);

    List<String> fields();

    void addFieldToIndex(String field, Index index);

    boolean validate();
}
