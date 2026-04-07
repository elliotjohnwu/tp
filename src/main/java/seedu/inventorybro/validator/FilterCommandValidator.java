package seedu.inventorybro.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.inventorybro.ItemList;

/**
 * Validates the raw input string for the filterItem command.
 * Supports one or more predicates joined by {@code AND} / {@code OR}.
 * Each predicate must follow the form {@code field operator value}, where:
 * <ul>
 *   <li>{@code description} values must be enclosed in single quotes (e.g. {@code 'Apple'})</li>
 *   <li>{@code quantity} values must be a non-negative integer (e.g. {@code 10})</li>
 *   <li>{@code price} values must be a non-negative integer (e.g. {@code 5})</li>
 * </ul>
 */
public class FilterCommandValidator implements Validator {
    private static final Pattern PREDICATE_PATTERN =
            Pattern.compile("(description|quantity|price) (=|<|>) ('.*?'|[^\\s']+)");
    private static final Pattern STRING_VALUE_PATTERN = Pattern.compile("^'.*'$");
    private static final Pattern NONNEG_INT_PATTERN = Pattern.compile("^\\d+$");

    private final String input;

    /**
     * Creates a validator bound to the given raw input string.
     *
     * @param input The full filterItem command string to validate.
     */
    public FilterCommandValidator(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Validates the overall structure, the AND/OR operators between predicates,
     * and the value type of every predicate.
     *
     * @param items Unused; present to satisfy the {@link Validator} contract.
     * @throws IllegalArgumentException if the input does not conform to the expected format,
     *                                  if AND/OR is missing or misspelled between predicates,
     *                                  or if a value type does not match its field.
     */
    @Override
    public void validate(ItemList items) {
        assert input != null : "Input should not be null";

        if (!input.startsWith("filterItem ")) {
            throw new IllegalArgumentException(
                    "Invalid filterItem format! Use: filterItem description =/</> 'VALUE'"
                            + " or filterItem quantity =/</> VALUE or filterItem price =/</> VALUE"
            );
        }

        String logic = input.substring("filterItem ".length()).trim();
        Matcher matcher = PREDICATE_PATTERN.matcher(logic);

        List<int[]> matchRanges = new ArrayList<>();
        List<String[]> predicates = new ArrayList<>();

        while (matcher.find()) {
            matchRanges.add(new int[]{matcher.start(), matcher.end()});
            predicates.add(new String[]{matcher.group(1), matcher.group(2), matcher.group(3)});
        }

        if (predicates.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid filterItem format! Use: filterItem description =/</> 'VALUE'"
                            + " or filterItem quantity =/</> VALUE or filterItem price =/</> VALUE"
            );
        }

        validateGaps(logic, matchRanges);
        validatePredicates(predicates);
    }

    /**
     * Checks that the text between consecutive predicate matches is exactly {@code AND} or {@code OR},
     * and that there is no leading or trailing content outside the predicates.
     *
     * @param logic       The logic substring (input with the {@code filterItem } prefix stripped).
     * @param matchRanges Start/end positions of each predicate match within {@code logic}.
     * @throws IllegalArgumentException if any gap is not {@code AND} or {@code OR}.
     */
    private void validateGaps(String logic, List<int[]> matchRanges) {
        int previousEnd = 0;
        for (int i = 0; i < matchRanges.size(); i++) {
            String gap = logic.substring(previousEnd, matchRanges.get(i)[0]).trim();
            if (i == 0 && !gap.isEmpty()) {
                throw new IllegalArgumentException(
                        "Invalid filterItem format! Use: filterItem description =/</> 'VALUE'"
                                + " or filterItem quantity =/</> VALUE"
                );
            }
            if (i > 0 && !gap.equals("AND") && !gap.equals("OR")) {
                throw new IllegalArgumentException(
                        "Expected AND or OR between predicates, found: '" + gap + "'."
                );
            }
            previousEnd = matchRanges.get(i)[1];
        }
        String trailing = logic.substring(previousEnd).trim();
        if (!trailing.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid filterItem format! Unexpected text after last predicate: '" + trailing + "'."
            );
        }
    }

    /**
     * Checks that each predicate's value is type-compatible with its field:
     * description values must be single-quoted; quantity values must be non-negative integers.
     *
     * @param predicates List of {@code [field, operator, value]} arrays to validate.
     * @throws IllegalArgumentException if any predicate has a type-mismatched or negative value.
     */
    private void validatePredicates(List<String[]> predicates) {
        for (String[] pred : predicates) {
            validateSinglePredicate(pred);
        }
    }

    /**
     * Checks that a single predicate's value type matches its field.
     *
     * @param pred A {@code [field, operator, value]} array.
     * @throws IllegalArgumentException if the value type does not match the field.
     */
    private void validateSinglePredicate(String[] pred) {
        String field = pred[0];
        String value = pred[2];

        if (field.equals("description") && !STRING_VALUE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Description value must be enclosed in single quotes."
                            + " Example: filterItem description = 'Apple'"
            );
        }
        if (field.equals("quantity") && !NONNEG_INT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Quantity value must be a non-negative integer."
                            + " Example: filterItem quantity > 10"
            );
        }
        if (field.equals("price") && !NONNEG_INT_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Price value must be a non-negative integer."
                            + " Example: filterItem price > 5"
            );
        }
    }
}
