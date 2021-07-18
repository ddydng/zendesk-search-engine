package service;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class SearcherTest extends TestCase {
    private Searcher searcher = new Searcher();

    @Test
    public void testUserSearchCompMultipleResult() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "users",
            "active",
            "true"
        );
        assertTrue(results.size() > 0);
        results.forEach(System.out::println);
    }

    @Test
    public void testUserSearchCompJoins() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "users",
            "_id",
            "1"
        );

        assertContainSecondaryId(results, "119");
        assertContainSecondaryId(results, "cb304286-7064-4509-813e-edc36d57623d");
        assertContainSecondaryId(results, "fc5a8a70-3814-4b17-a6e9-583936fca909");
        assertContainSecondaryId(results, "1fafaa2a-a1e9-4158-aeb4-f17e64615300");
        assertContainSecondaryId(results, "13aafde0-81db-47fd-b1a2-94b0015803df");
    }


    @Test
    public void testOrgSearchCompMultipleResult() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "organizations",
            "details",
            "Non profit"
        );
        assertTrue(results.size() > 0);
        results.forEach(System.out::println);
    }

    @Test
    public void testOrgSearchCompJoins() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "organizations",
            "_id",
            "101"
        );

        assertContainSecondaryId(results, "5");
        assertContainSecondaryId(results, "23");
        assertContainSecondaryId(results, "27");
        assertContainSecondaryId(results, "29");
        assertContainSecondaryId(results, "b07a8c20-2ee5-493b-9ebf-f6321b95966e");
        assertContainSecondaryId(results, "c22aaced-7faa-4b5c-99e5-1a209500ff16");
        assertContainSecondaryId(results, "89255552-e9a2-433b-970a-af194b3a39dd");
        assertContainSecondaryId(results, "27c447d9-cfda-4415-9a72-d5aa12942cf1");
    }

    @Test
    public void testTicketSearchCompMultipleResult() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "tickets",
            "type",
            "task"
        );
        assertTrue(results.size() > 0);
        results.forEach(System.out::println);
    }

    @Test
    public void testTicketSearchCompJoins() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "tickets",
            "_id",
            "436bf9b0-1147-4c0a-8439-6f79833bff5b"
        );

        assertContainSecondaryId(results, "38");
        assertContainSecondaryId(results, "24");
        assertContainSecondaryId(results, "116");
    }

    @Test
    public void testCanSearchForEmptyValues() {
        List<SearchResult> results = searcher.searchAcrossAll(
            "users",
            "organization_id",
            ""
        );
        assertTrue(results.size() > 0);
        results.forEach(System.out::println);
    }


    private void assertContainSecondaryId(List<SearchResult> results, String id) {
        assertTrue(results.get(0)
            .getSecondaries()
            .stream()
            .map(s -> s.entry.get("_id").toString())
            .anyMatch(s -> s.equals(id))
        );
    }
}