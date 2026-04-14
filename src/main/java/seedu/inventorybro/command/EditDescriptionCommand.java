package seedu.inventorybro.command;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.CategoryList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.EditDescriptionCommandValidator;
import seedu.inventorybro.storage.TransactionStorage;

/**
 * Updates an existing item's description.
 */
//@@author vionyp
public class EditDescriptionCommand implements Command {
    private final String input;
    private final TransactionStorage transactionStorage;

    /**
     * Creates an editDescription command from the raw user input.
     *
     * @param input              The full editDescription command string.
     * @param transactionStorage The storage used to keep transaction history in sync; may be null.
     */
    public EditDescriptionCommand(String input, TransactionStorage transactionStorage) {
        assert input != null : "Input should not be null";
        this.input = input;
        this.transactionStorage = transactionStorage;
    }

    /**
     * Validates and updates the targeted item's description.
     * Also updates all matching entries in the transaction history.
     *
     * @param items      The inventory item list to update.
     * @param categories The category list.
     * @param ui         The UI to display messages.
     */
    @Override
    public void execute(ItemList items, CategoryList categories, Ui ui) {
        assert items != null : "ItemList should not be null";
        assert ui != null : "Ui should not be null";

        new EditDescriptionCommandValidator(input).validate(items, categories);

        String[] words = input.split(" ", 2);
        String[] parts = words[1].split("d/", 2);
        int index = Integer.parseInt(parts[0].trim()) - 1;
        String newDescription = parts[1].trim();

        Item item = items.getItem(index);
        String oldDescription = item.getDescription();
        item.setDescription(newDescription);

        if (transactionStorage != null) {
            transactionStorage.updateItemName(oldDescription, newDescription);
        }

        ui.showMessage("Item description updated: " + item);
    }
}
//@@author
