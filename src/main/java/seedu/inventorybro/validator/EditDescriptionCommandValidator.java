package seedu.inventorybro.validator;

import seedu.inventorybro.ItemList;

/**
 * Validates the raw input string for the editDescription command.
 */
//@@author vionyp
public class EditDescriptionCommandValidator implements Validator {
    private static final String FORMAT_ERROR = "Invalid editDescription format. "
            + "Use: editDescription INDEX d/NEW_DESCRIPTION";
    private final String input;

    public EditDescriptionCommandValidator(String input) {
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

            String[] parts = words[1].split("d/", 2);
            if (parts.length < 2) {
                throw new IllegalArgumentException(FORMAT_ERROR);
            }

            int index = Integer.parseInt(parts[0].trim()) - 1;
            if (index < 0 || index >= items.size()) {
                throw new IllegalArgumentException("Invalid index.");
            }

            String newDescription = parts[1].trim();
            if (newDescription.isEmpty()) {
                throw new IllegalArgumentException("Item description cannot be empty.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index must be a number.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
//@@author
