package cli;

import service.SearchResult;
import service.Searcher;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Console {

    private static final String CHOICE_START_SEARCH = "Search Zendesk";
    private static final String CHOICE_SHOW_ALL_FIELDS = "View a list of searchable fields.";
    private static final String CHOICE_ANOTHER_SEARCH = "Yes, perform another search.";
    private static final String INVALID_INPUT = "Input not valid, try again.";

    private static void showChoices(List<String> allowedValues, Boolean isNumbered) {
        System.out.println("Choices are: ");
        if (isNumbered) {
            for (int i = 0; i < allowedValues.size(); i++) {
                System.out.print(i + ") " + allowedValues.get(i) + " ");
            }
            System.out.println();
        } else {
            System.out.println(String.join(", ", allowedValues));
        }
    }

    private static String handleNextInput(List<String> allowedValues, Boolean showChoices, Boolean isNumbered) {
        while (true) {
            if (showChoices) {
                showChoices(allowedValues, isNumbered);
            }
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            if (input.equals("/quit")) {
                System.exit(0);
            }

            if (input.equals("/list") && allowedValues != null) {
                showChoices(allowedValues, isNumbered);
                showChoices = false;
                continue;
            }

            if (allowedValues == null) {
                return input;
            }

            if (isNumbered) {
                try {
                    int choice = Integer.valueOf(input);
                    return allowedValues.get(choice);
                } catch (Throwable e) {
                    System.out.println(INVALID_INPUT);
                    continue;
                }
            } else {
                if (allowedValues.contains(input)) {
                    return input;
                } else  {
                    System.out.println(INVALID_INPUT);
                    continue;
                }
            }
        }
    }

    private static void showFieldsOfAllTables(Map<String, List<String>> allFields) {
        for (String table : allFields.keySet()) {
            System.out.println("Fields of " + table);
            System.out.println("------------------");
            showTableFields(allFields.get(table));
            System.out.println();
        }
    }

    private static void showTableFields(List<String> fields) {
        fields.forEach(System.out::println);
    }

    private static void showResults(List<SearchResult> results) {
        if (results.size() == 0) {
            System.out.println("No results returned.");
        } else {
            System.out.println(
                String.join("#".repeat(120) + "\n",
                    results.stream().map(SearchResult::toString).collect(Collectors.toList()))
            );
            System.out.println(String.format("Returned %d result(s)", results.size()));
        }
    }

    public static void main(String[] args) {
        Searcher searcher = new Searcher();
        System.out.println("Welcome to Zendesk Search");
        System.out.println("Type '/quit' or ctrl-c to exit at any time.\n");
        String choice = "";
        do {
            choice = handleNextInput(
                List.of(CHOICE_START_SEARCH, CHOICE_SHOW_ALL_FIELDS),
                true,
                true
            );

            if (choice.equals(CHOICE_START_SEARCH)) {
                System.out.println("Start searching.\n");
            } else {
                System.out.println("All fields are:");
                Map<String, List<String>> allFields = searcher.listSearchableFields();
                showFieldsOfAllTables(allFields);
                System.out.println();
            }
        } while (
            !choice.equals(CHOICE_START_SEARCH)
        );

        // Start of searching phase
        do {
            System.out.println("Select a table to search from");
            System.out.println("To see searchable tables, type '/list'");
            String table = handleNextInput(
                searcher.listSearchableTables(),
                true,
                true
            );
            System.out.println();

            System.out.println("Enter a field to search in table " + table);
            System.out.println("To see searchable fields, type '/list'");
            String field = handleNextInput(
                searcher.listSearchableFields(table),
                false,
                false
            );
            System.out.println();

            System.out.println(String.format("Enter the value to search for field %s in table %s", field, table));
            String value = handleNextInput(
                null,
                false,
                false
            );
            System.out.println();

            if (value.equals("")) {
                System.out.println(String.format("Searching table %s for %s with empty value",
                    table,
                    field));
            } else {
                System.out.println(String.format("Searching table %s for %s with a value of %s",
                    table,
                    field,
                    value));
            }

            showResults(
                searcher.searchAcrossAll(
                    table,
                    field,
                    value
                )
            );
            System.out.println();

            System.out.println("Another search?");

            choice = handleNextInput(
                List.of(CHOICE_ANOTHER_SEARCH, "No"),
                true,
                true
            );

        } while (
            choice.equals(CHOICE_ANOTHER_SEARCH)
        );
    }
}
