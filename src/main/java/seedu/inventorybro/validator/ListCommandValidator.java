package seedu.inventorybro.validator;

import seedu.inventorybro.ItemList;

//@@author adbsw
/**
 * Validates the raw input string for the listItems command.
 */
public class ListCommandValidator implements Validator {
    private final String input;

    /**
     * Creates a list command validator from the raw user input.
     *
     * @param input The user input.
     */
    public ListCommandValidator(String input) {
        assert input != null : "Input line should not be null";
        this.input = input;
    }

    /**
     * Validates the list command input.
     *
     * @param items The current inventory item list, provided for context-sensitive validation.
     */
    @Override
    public void validate(ItemList items) {
        String[] words = input.split(" ", 2);

        if (!words[0].equals("listItems")) {
            throw new IllegalArgumentException("Did you mean 'listItems'?");
        }

        if (words.length == 1) {
            return;
        }

        String[] additions = words[1].split(" ");

        String field = additions[0];

        boolean isValidFieldSpecified = field.equals("price") || field.equals("quantity");

        if (!isValidFieldSpecified) {
            throw new IllegalArgumentException("Invalid field specified for listItems. " +
                    "Use 'price' or 'quantity'.");
        }

        if (additions.length < 2) {
            throw new IllegalArgumentException("Preferred order not specified for listItems. " +
                    "Specify 'high' or 'low'.");
        }

        String order = additions[1];

        boolean isValidOrderSpecified = order.equals("high") || order.equals("low");

        if (!isValidOrderSpecified) {
            throw new IllegalArgumentException("Invalid order specified for listItems. " +
                    "Use 'high' or 'low'.");
        }

        if (additions.length > 2) {
            throw new IllegalArgumentException("Too many descriptors specified for listItems. " +
                    "Use listItems [field_name] [order]");
        }
    }
}
