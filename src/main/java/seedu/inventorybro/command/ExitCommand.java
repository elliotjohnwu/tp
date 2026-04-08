package seedu.inventorybro.command;

import seedu.inventorybro.ExitException;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;

//@@author vionyp
public class ExitCommand implements Command {
    /**
     * Prints the farewell message and exits the program.
     *
     * @param items The inventory item list, unused for this command.
     * @param ui The UI instance used to display the farewell message.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        ui.showMessage("Bye! See you next time.");
        throw new ExitException();
    }
}
//@@author
