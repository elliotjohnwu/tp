package seedu.inventorybro.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.FilterCommandValidator;

/**
 * Filters inventory items using one or more field-operator-value predicates combined
 * with {@code AND} / {@code OR} logic, then displays all matching items.
 *
 * <p>Evaluation follows AND-before-OR grouping (no explicit precedence parsing needed):
 * consecutive predicates joined by {@code AND} form a group; an item passes if it satisfies
 * every predicate in at least one group.</p>
 *
 * <p>Supports {@code description} (lexicographic comparison), {@code quantity}
 * (numeric comparison), and {@code price} (integer comparison).</p>
 */
public class FilterCommand implements Command {
    private static final Pattern PREDICATE_PATTERN =
            Pattern.compile("(description|quantity|price) (=|<|>) ('.*?'|[^\\s']+)");
    private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("^'(.*)'$");

    private final String input;

    /**
     * Creates a filter command bound to the given raw user input.
     *
     * @param input The full command string, expected to follow the format
     *              {@code filterItem PRED (AND|OR PRED)*} where each {@code PRED} is
     *              {@code description =/</> 'VALUE'} or {@code quantity =/</> VALUE}.
     */
    public FilterCommand(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Validates the input, parses all predicates and their joining operators,
     * groups consecutive AND predicates, and displays every item that satisfies at least one group.
     *
     * @param items The inventory item list to filter.
     * @param ui    The UI object used to display results.
     * @throws IllegalArgumentException if the input does not match the expected format.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        assert items != null : "ItemList should not be null";
        assert ui != null : "Ui should not be null";

        new FilterCommandValidator(input).validate(items);

        String logic = input.substring("filterItem ".length()).trim();
        Matcher matcher = PREDICATE_PATTERN.matcher(logic);

        List<String[]> predicates = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        int lastEnd = 0;

        while (matcher.find()) {
            if (!predicates.isEmpty()) {
                operators.add(logic.substring(lastEnd, matcher.start()).trim());
            }
            predicates.add(new String[]{matcher.group(1), matcher.group(2), matcher.group(3)});
            lastEnd = matcher.end();
        }

        List<List<String[]>> andGroups = buildAndGroups(predicates, operators);
        List<Item> results = collectMatchingItems(items, andGroups);
        assert results != null : "Filter result list should not be null";

        if (results.isEmpty()) {
            ui.showMessage("No items match the given filter.");
            return;
        }

        ui.showMessage("Here are the filtered items:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i));
        }
    }

    /**
     * Splits the flat predicate list into AND-groups at every {@code OR} operator.
     * Consecutive predicates joined by {@code AND} belong to the same group.
     *
     * @param predicates List of {@code [field, operator, value]} arrays.
     * @param operators  Joining operators ({@code "AND"} or {@code "OR"}) between consecutive predicates.
     * @return A list of groups; each group is a list of predicates that must all be satisfied together.
     */
    private List<List<String[]>> buildAndGroups(List<String[]> predicates, List<String> operators) {
        List<List<String[]>> groups = new ArrayList<>();
        List<String[]> currentGroup = new ArrayList<>();
        currentGroup.add(predicates.get(0));

        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("AND")) {
                currentGroup.add(predicates.get(i + 1));
            } else {
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
                currentGroup.add(predicates.get(i + 1));
            }
        }
        groups.add(currentGroup);
        return groups;
    }

    /**
     * Iterates the inventory and collects every item that passes the AND-group filter.
     *
     * @param items     The inventory to search.
     * @param andGroups The evaluated groups from {@link #buildAndGroups}.
     * @return A list (possibly empty) of matching items in their original order.
     */
    private List<Item> collectMatchingItems(ItemList items, List<List<String[]>> andGroups) {
        List<Item> results = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.getItem(i);
            if (passesFilter(item, andGroups)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Returns true if the item satisfies every predicate in at least one AND-group (OR between groups).
     *
     * @param item      The item to test.
     * @param andGroups The groups to evaluate.
     * @return true if the item passes any group.
     */
    private boolean passesFilter(Item item, List<List<String[]>> andGroups) {
        for (List<String[]> group : andGroups) {
            if (andGroupPasses(item, group)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the item satisfies every predicate in a single AND-group.
     *
     * @param item  The item to test.
     * @param group A list of predicates that must all be satisfied.
     * @return true if the item satisfies all predicates in the group.
     */
    private boolean andGroupPasses(Item item, List<String[]> group) {
        for (String[] pred : group) {
            if (!evaluatePredicate(item, pred[0], pred[1], pred[2])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Evaluates a single predicate against an item.
     * For {@code description}, uses lexicographic {@link String#compareTo}.
     * For {@code quantity}, uses {@link Integer#compare}.
     *
     * @param item     The item to test.
     * @param field    One of {@code "description"}, {@code "quantity"}, or {@code "price"}.
     * @param operator One of {@code "="}, {@code "<"}, or {@code ">"}.
     * @param rawValue The raw value token from the input string.
     * @return true if the item's field value satisfies the operator against {@code rawValue}.
     */
    private boolean evaluatePredicate(Item item, String field, String operator, String rawValue) {
        if (field.equals("description")) {
            Matcher valueMatcher = STRING_VALUE_PATTERN.matcher(rawValue);
            valueMatcher.matches();
            return satisfiesOperator(item.getDescription().compareTo(valueMatcher.group(1)), operator);
        }
        if (field.equals("price")) {
            return satisfiesOperator(Integer.compare((int) item.getPrice(), Integer.parseInt(rawValue)), operator);
        }
        return satisfiesOperator(Integer.compare(item.getQuantity(), Integer.parseInt(rawValue)), operator);
    }

    /**
     * Returns true if the given comparator result satisfies the operator.
     *
     * @param cmp      The result of a {@link Comparable#compareTo} or {@link Integer#compare} call.
     * @param operator One of {@code "="}, {@code "<"}, or {@code ">"}.
     * @return true if the comparison result matches the operator's semantics.
     */
    private boolean satisfiesOperator(int cmp, String operator) {
        if (operator.equals("=")) {
            return cmp == 0;
        } else if (operator.equals("<")) {
            return cmp < 0;
        } else {
            return cmp > 0;
        }
    }
}
