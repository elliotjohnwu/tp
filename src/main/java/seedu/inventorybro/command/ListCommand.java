package seedu.inventorybro.command;

import java.util.ArrayList;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.ListCommandValidator;

//@@author adbsw

/**
 * Displays the current inventory items.
 */
public class ListCommand implements Command {
    private final String input;

    /**
     * Creates a list command from the raw user input.
     *
     * @param input The full list command string.
     */
    public ListCommand(String input) {
        assert input != null : "Input line should not be null";
        this.input = input;
    }

    /**
     * Validates the list command input and prints all items in the inventory.
     * If a preferred order is specified, the items are sorted and printed in that order.
     *
     * @param items The inventory item list to display.
     * @param ui    The ui object.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        new ListCommandValidator(input).validate(items);

        if (items.isEmpty()) {
            ui.showMessage("Your inventory is empty.");
            return;
        }
        assert !items.isEmpty() : "List of items should not be empty";

        String[] words = input.trim().split(" ");

        String field = "";
        String order = "";

        ArrayList<Item> sortedItems = null;
        if (words.length == 3) {
            field = words[1];
            order = words[2];
            sortedItems = items.getSortedItems(field, order);
        }

        int listIndex = 0;
        ui.showMessage("Here are your current inventory items"
                + (field.isEmpty() ? "" : " by ") + field
                + (order.isEmpty() ? "" : (order.equals("high") ? " in decreasing order" : " in increasing order"))
                + ":");
        for (int i = 0; i < items.size(); i++) {
            listIndex = i + 1;
            ui.showMessage(listIndex + ". " + (field.isEmpty() ? items.getItem(i) : sortedItems.get(i)));
        }

        assert listIndex == items.size() : "List index should be equal to total number " +
                "of items in list after iterating through and printing all items";
    }
}
