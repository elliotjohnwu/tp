package seedu.inventorybro.validator;

import seedu.inventorybro.ItemList;

/**
 * Validates the raw input string for the editPrice command.
 */
//@@author vionyp
public class EditPriceCommandValidator implements Validator {
    private static final String FORMAT_ERROR = "Invalid editPrice format. "
            + "Use: editPrice INDEX p/NEW_PRICE";
    private final String input;

    public EditPriceCommandValidator(String input) {
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

            String[] parts = words[1].split("p/", 2);
            if (parts.length < 2) {
                throw new IllegalArgumentException(FORMAT_ERROR);
            }

            int index = Integer.parseInt(parts[0].trim()) - 1;
            if (index < 0 || index >= items.size()) {
                throw new IllegalArgumentException("Invalid index.");
            }

            double newPrice = Double.parseDouble(parts[1].trim());
            if (newPrice < 0) {
                throw new IllegalArgumentException("Price cannot be negative.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index must be a number and price must be a valid decimal.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
//@@author
