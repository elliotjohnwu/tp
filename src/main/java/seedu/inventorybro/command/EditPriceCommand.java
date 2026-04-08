package seedu.inventorybro.command;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.EditPriceCommandValidator;

/**
 * Updates an existing item's price.
 */
//@@author vionyp
public class EditPriceCommand implements Command {
    private final String input;

    /**
     * Creates an editPrice command from the raw user input.
     *
     * @param input The full editPrice command string.
     */
    public EditPriceCommand(String input) {
        assert input != null : "Input should not be null";
        this.input = input;
    }

    /**
     * Validates and updates the targeted item's price.
     *
     * @param items The inventory item list to update.
     * @param ui    The UI to display messages.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        assert items != null : "ItemList should not be null";
        assert ui != null : "Ui should not be null";

        new EditPriceCommandValidator(input).validate(items);

        String[] words = input.split(" ", 2);
        String[] parts = words[1].split("p/", 2);
        int index = Integer.parseInt(parts[0].trim()) - 1;
        double newPrice = Double.parseDouble(parts[1].trim());

        Item item = items.getItem(index);
        item.setPrice(newPrice);

        ui.showMessage("Item price updated: " + item);
    }
}
//@@author
