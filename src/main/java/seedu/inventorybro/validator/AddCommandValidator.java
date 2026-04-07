package seedu.inventorybro.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.inventorybro.ItemList;

//@@author kenpegrasio
/**
 * Validates the raw input string for the addItem command.
 */
public class AddCommandValidator implements Validator {
    private static final Pattern ADD_COMMAND_PATTERN = Pattern.compile("^addItem d/(.*?) q/(\\d+)$");
    private final String input;

    /**
     * Creates a validator bound to the given raw input string.
     *
     * @param input The full addItem command string to validate.
     */
    public AddCommandValidator(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Checks that the input matches the {@code addItem d/NAME q/INITIAL_QUANTITY} pattern
     * and that no item with the same name already exists in the inventory.
     *
     * @param items The current inventory; used to check for duplicate names.
     * @throws IllegalArgumentException if the input does not conform to the expected format
     *                                  or the item name already exists.
     */
    @Override
    public void validate(ItemList items) {
        Matcher matcher = ADD_COMMAND_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid addItem format! Use: addItem d/NAME q/INITIAL_QUANTITY"
            );
        }
        String name = matcher.group(1).trim();
        new DuplicateItemValidator(name).validate(items);
    }
}
