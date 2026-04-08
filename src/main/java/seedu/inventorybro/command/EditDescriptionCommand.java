package seedu.inventorybro.command;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.EditDescriptionCommandValidator;

/**
 * Updates an existing item's description.
 */
//@@author vionyp
public class EditDescriptionCommand implements Command {
    private final String input;

    /**
     * Creates an editDescription command from the raw user input.
     *
     * @param input The full editDescription command string.
     */
    public EditDescriptionCommand(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Validates and updates the targeted item's description.
     *
     * @param items The inventory item list to update.
     * @param ui    The UI to display messages.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        assert items != null : "ItemList should not be null";
        assert ui != null : "Ui should not be null";

        new EditDescriptionCommandValidator(input).validate(items);

        String[] words = input.split(" ", 2);
        String[] parts = words[1].split("d/", 2);
        int index = Integer.parseInt(parts[0].trim()) - 1;
        String newDescription = parts[1].trim();

        Item item = items.getItem(index);
        item.setDescription(newDescription);

        ui.showMessage("Item description updated: " + item);
    }
}
//@@author
