package seedu.inventorybro.validator;

import seedu.inventorybro.ItemList;

/**
 * Validates the raw input string for the editQuantity command.
 */
//@@author vionyp
public class EditQuantityCommandValidator implements Validator {
    private static final String FORMAT_ERROR = "Invalid editQuantity format. "
            + "Use: editQuantity INDEX q/NEW_QUANTITY";
    private final String input;

    public EditQuantityCommandValidator(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    @Override
    public void validate(ItemList items) {
        try {
            String[] words = input.split(" ", 2);
            if (words.length < 2) {
                throw new IllegalArgumentException(FORMAT_ERROR);
            }

            String[] parts = words[1].split("q/", 2);
            if (parts.length < 2) {
                throw new IllegalArgumentException(FORMAT_ERROR);
            }

            int index = Integer.parseInt(parts[0].trim()) - 1;
            if (index < 0 || index >= items.size()) {
                throw new IllegalArgumentException("Invalid index.");
            }

            int newQuantity = Integer.parseInt(parts[1].trim());
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index and quantity must be numbers.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
//@@author
