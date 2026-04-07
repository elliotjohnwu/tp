package seedu.inventorybro.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.inventorybro.ItemList;

/**
 * Validates the raw input string for the findItem command.
 */
public class FindCommandValidator implements Validator {
    private static final Pattern FIND_COMMAND_PATTERN = Pattern.compile("^findItem\\s+(.+)$");
    private final String input;

    /**
     * Creates a validator bound to the given raw input string.
     *
     * @param input The full find command string to validate.
     */
    public FindCommandValidator(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Checks that the input matches the {@code findItem KEYWORD} pattern
     * and that the keyword is not empty.
     *
     * @param items The current inventory.
     * @throws IllegalArgumentException if the input does not conform to the expected format.
     */
    @Override
    public void validate(ItemList items) {
        assert input != null : "Input should not be null";
        Matcher matcher = FIND_COMMAND_PATTERN.matcher(input);

        if (!matcher.matches() || matcher.group(1).trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid find format! Use: findItem KEYWORD"
            );
        }
    }
}
