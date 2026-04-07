package seedu.inventorybro.command;

import seedu.inventorybro.ExitException;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;

/**
 * Terminates the application.
 */
public class ExitCommand implements Command {
    /**
     * Prints the farewell message and exits the program.
     *
     * @param items The inventory item list, unused for this command.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        ui.showMessage("Bye! See you next time.");
        throw new ExitException();
    }
}
