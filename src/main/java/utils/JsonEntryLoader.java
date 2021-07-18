package utils;

import model.Entry;
import model.JsonEntry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonEntryLoader implements EntryLoader{

    private List<String> schema;

    public JsonEntryLoader(List<String> schema) {
        this.schema = schema;
    }

    @Override
    public List<Entry> read(String path) {
        try {
            List<Entry> result = new ArrayList<>();
            URL resource = JsonEntryLoader.class.getClassLoader().getResource(path);
            byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new String(bytes));
            for (Object obj : jsonArray) {
                result.add(new JsonEntry((JSONObject) obj, schema));
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Error parsing json file", e);
        }
    }
}
