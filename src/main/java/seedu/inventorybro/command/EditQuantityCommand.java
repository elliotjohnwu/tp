package seedu.inventorybro.command;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.EditQuantityCommandValidator;

/**
 * Updates an existing item's quantity.
 */
//@@author vionyp
public class EditQuantityCommand implements Command {
    private final String input;

    /**
     * Creates an editQuantity command from the raw user input.
     *
     * @param input The full editQuantity command string.
     */
    public EditQuantityCommand(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Validates and updates the targeted item's quantity.
     *
     * @param items The inventory item list to update.
     * @param ui    The UI to display messages.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        assert items != null : "ItemList should not be null";
        assert ui != null : "Ui should not be null";

        new EditQuantityCommandValidator(input).validate(items);

        String[] words = input.split(" ", 2);
        String[] parts = words[1].split("q/", 2);
        int index = Integer.parseInt(parts[0].trim()) - 1;
        int newQuantity = Integer.parseInt(parts[1].trim());

        Item item = items.getItem(index);
        item.setQuantity(newQuantity);

        ui.showMessage("Item quantity updated: " + item);
    }
}
//@@author
