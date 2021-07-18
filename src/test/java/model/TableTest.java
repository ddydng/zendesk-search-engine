package model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static config.Config.*;

public class TableTest extends TestCase {

    @Test
    public void testCreateFromSampleHasCorrectNumberOfRows() {
        Table t1 = Table.fromFile("organizations", ORGS_TABLE_LOADER, "json/organizations.json", ORGS_FIELDS);

        assertEquals(25, t1.size());

    }

    @Test
    public void testSearchBooleanField() {
        Table orgsTable = Table.fromFile("organizations", ORGS_TABLE_LOADER, "json/organizations.json", ORGS_FIELDS);
        List<Entry> result = orgsTable.search("shared_tickets", "false");

        assertEquals(15, result.size());

        result = orgsTable.search("shared_tickets", "true");
        assertEquals(10, result.size());
    }

    @Test
    public void testSearchLongField() {
        Table ticketsTable =
            Table.fromFile(TICKETS_TABLE_NAME, TICKETS_TABLE_LOADER, TICKETS_FILE_PATH, TICKETS_FIELDS);

        List<Entry> result = ticketsTable.search("organization_id", "107");
        assertEquals(11, result.size());
    }

    @Test
    public void testSearchStringField() {
        Table usersTable =
            Table.fromFile(USERS_TABLE_NAME, USERS_TABLE_LOADER, USERS_FILE_PATH, USERS_FIELDS);

        List<Entry> result = usersTable.search("signature", "Don't Worry Be Happy!");
        assertEquals(75, result.size());
    }

    @Test
    public void testSearchMultiStringField() {
        Table ticketsTable =
            Table.fromFile(TICKETS_TABLE_NAME, TICKETS_TABLE_LOADER, TICKETS_FILE_PATH, TICKETS_FIELDS);

        List<Entry> result = ticketsTable.search("tags", "Texas");
        assertEquals(14, result.size());
    }

    @Test
    public void testNotThrowInvalidSearchField() {
        Table usersTable =
            Table.fromFile(USERS_TABLE_NAME, USERS_TABLE_LOADER, USERS_FILE_PATH, USERS_FIELDS);

        List<Entry> result = usersTable.search("invalid", "field");
    }

    @Test
    public void testReturnEmptyInvalidSearchField() {
        Table usersTable =
            Table.fromFile(USERS_TABLE_NAME, USERS_TABLE_LOADER, USERS_FILE_PATH, USERS_FIELDS);

        List<Entry> result = usersTable.search("invalid", "field");
        assertEquals(0, result.size());
    }

}