package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonEntryTest {

    @Mock
    Index index;

    @Test(expected = RuntimeException.class)
    public void testThrowsWhenCreatingInvalidEntry() {
        createEntryFromMap(Map.of("field", "invalid"), List.of());
    }

    @Test
    public void testEntryAddEmptyStringToIndexIfFieldNotPresent() {
        doNothing().when(index).add(any(), any());

        Entry entry = createEntryFromMap(Map.of(), List.of("field"));
        entry.addFieldToIndex("field", index);

        List<Invocation> invos = Mockito.mockingDetails(index).getInvocations().stream().collect(Collectors.toList());
        assertEquals(1, invos.size());
        assertEquals("", invos.get(0).getArgument(0));
    }

    @Test
    public void testEntryAddMultiFields() throws ParseException {
        doNothing().when(index).add(any(), any());

        Object array = (new JSONParser().parse("[\"a\", \"b\"]"));
        Entry entry = createEntryFromMap(Map.of("field", array), List.of("field"));
        entry.addFieldToIndex("field", index);

        List<Invocation> invos = Mockito.mockingDetails(index).getInvocations().stream().collect(Collectors.toList());
        assertEquals(2, invos.size());
        assertEquals("a", invos.get(0).getArgument(0));
        assertEquals("b", invos.get(1).getArgument(0));
    }

    private JsonEntry createEntryFromMap(Map m, List<String> schema) {
        JSONObject obj = new JSONObject(m);
        return new JsonEntry(obj, schema);
    }

}