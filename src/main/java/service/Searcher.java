package service;

import model.Entry;
import model.Table;

import java.util.*;

import static config.Config.*;

/**
 * Searcher acts as a middle layer between user and data.
 * It processes queries sent from user and translate to search
 * commands to the tables. It also handles join logic
 */
public class Searcher {

    private Table orgsTable;
    private Table usersTable;
    private Table ticketsTable;
    private Map<String, Table> db;

    public Searcher() {
        orgsTable = Table.fromFile(ORGS_TABLE_NAME, ORGS_TABLE_LOADER, ORGANIZATIONS_FILE_PATH, ORGS_FIELDS);
        usersTable = Table.fromFile(USERS_TABLE_NAME, USERS_TABLE_LOADER, USERS_FILE_PATH, USERS_FIELDS);
        ticketsTable = Table.fromFile(TICKETS_TABLE_NAME, TICKETS_TABLE_LOADER, TICKETS_FILE_PATH, TICKETS_FIELDS);
        db = new HashMap<>();
        db.put(ORGS_TABLE_NAME, orgsTable);
        db.put(USERS_TABLE_NAME, usersTable);
        db.put(TICKETS_TABLE_NAME, ticketsTable);
    }

    public List<SearchResult> searchAcrossAll(String table, String field, String value) {
        if (table.equals(USERS_TABLE_NAME)) {
            return searchUsersComp(field, value);
        } else if (table.equals(TICKETS_TABLE_NAME)) {
            return searchTicketsComp(field, value);
        } else if (table.equals(ORGS_TABLE_NAME)) {
            return searchOrganizationsComp(field, value);
        }
        return List.of();
    }

    public List<String> listSearchableTables() {
        return List.of(USERS_TABLE_NAME, ORGS_TABLE_NAME, TICKETS_TABLE_NAME);
    }

    public Map<String, List<String>> listSearchableFields() {
        Map<String, List<String>> result = new HashMap<>();
        for (String table : listSearchableTables()) {
            result.put(table, listSearchableFields(table));
        }
        return result;
    }

    public List<String> listSearchableFields(String table) {
        if (!listSearchableTables().contains(table)) {
            throw new IllegalArgumentException("Invalid table name");
        }
        return db.get(table).getSchema();
    }

    private List<SearchResult> searchUsersComp(String field, String value) {
        List<Entry> users = usersTable.search(field, value);
        if (users == null) {
            return List.of();
        }
        List<SearchResult> results = new ArrayList<>();
        for (Entry user : users) {
            SearchResult r = new SearchResult();
            r.add(user);
            String userId = user.get("_id").toString();


            r.addSecondaryEntries(
                ticketsTable.search("submitter_id", userId),
                "Submitted Ticket Id: ",
                "subject"
            );

            r.addSecondaryEntries(
                ticketsTable.search("assignee_id", userId),
                "Assigned Ticket Id: ",
                "subject"
            );

            if (user.containsField("organization_id")) {
                String orgId = user.get("organization_id").toString();
                r.addSecondaryEntries(
                    orgsTable.search("_id", orgId),
                    "Organisation Id: ",
                    "name"
                );
            }

            results.add(r);
        }

        return results;
    }

    private List<SearchResult> searchOrganizationsComp(String field, String value) {
        List<Entry> orgs = orgsTable.search(field, value);
        if (orgs == null) {
            return List.of();
        }
        List<SearchResult> results = new ArrayList<>();
        for (Entry org : orgs) {
            SearchResult r = new SearchResult();
            r.add(org);
            String orgId = org.get("_id").toString();

            r.addSecondaryEntries(
                ticketsTable.search("organization_id", orgId),
                "Having Ticket Id: ",
                "subject"
            );

            r.addSecondaryEntries(
                usersTable.search("organization_id", orgId),
                "Having User Id: ",
                "name"
            );

            results.add(r);
        }

        return results;
    }

    private List<SearchResult> searchTicketsComp(String field, String value) {
        List<Entry> tickets = ticketsTable.search(field, value);
        if (tickets == null) {
            return List.of();
        }
        List<SearchResult> results = new ArrayList<>();
        for (Entry ticket : tickets) {
            SearchResult r = new SearchResult();
            r.add(ticket);

            if (ticket.containsField("submitter_id")) {
                String submitterId = ticket.get("submitter_id").toString();
                r.addSecondaryEntries(
                    usersTable.search("_id", submitterId),
                    "Submitted by User Id: ",
                    "name"
                );
            }

            if (ticket.containsField("assignee_id")) {
                String assigneeId = ticket.get("assignee_id").toString();
                r.addSecondaryEntries(
                    usersTable.search("_id", assigneeId),
                    "Assigned to User Id: ",
                    "name"
                );
            }

            if (ticket.containsField("organization_id")) {
                String orgId = ticket.get("organization_id").toString();
                r.addSecondaryEntries(
                    orgsTable.search("_id", orgId),
                    "Owned by Organization Id: ",
                    "name"
                );
            }

            results.add(r);
        }

        return results;
    }
}
