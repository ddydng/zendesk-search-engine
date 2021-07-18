package config;

import utils.EntryLoader;
import utils.JsonEntryLoader;

import java.util.List;

public class Config {
    public static final String ORGS_TABLE_NAME = "organizations";
    public static final String USERS_TABLE_NAME = "users";
    public static final String TICKETS_TABLE_NAME = "tickets";

    public static final String ORGANIZATIONS_FILE_PATH = "json/organizations.json";
    public static final String TICKETS_FILE_PATH = "json/tickets.json";
    public static final String USERS_FILE_PATH = "json/users.json";

    public static final List<String> ORGS_FIELDS = List.of(
        "_id",
        "url",
        "external_id",
        "name",
        "domain_names",
        "created_at",
        "details",
        "shared_tickets",
        "tags"
    );

    public static final List<String> TICKETS_FIELDS = List.of(
        "_id",
        "url",
        "external_id",
        "created_at",
        "type",
        "subject",
        "description",
        "priority",
        "status",
        "submitter_id",
        "assignee_id",
        "organization_id",
        "tags",
        "has_incidents",
        "due_at",
        "via"
    );

    public static final List<String> USERS_FIELDS = List.of(
        "_id",
        "url",
        "external_id",
        "name",
        "alias",
        "created_at",
        "active",
        "verified",
        "shared",
        "locale",
        "timezone",
        "last_login_at",
        "email",
        "phone",
        "signature",
        "organization_id",
        "tags",
        "suspended",
        "role"
    );

    public static final EntryLoader ORGS_TABLE_LOADER = new JsonEntryLoader(ORGS_FIELDS);
    public static final EntryLoader USERS_TABLE_LOADER = new JsonEntryLoader(USERS_FIELDS);
    public static final EntryLoader TICKETS_TABLE_LOADER = new JsonEntryLoader(TICKETS_FIELDS);
}
