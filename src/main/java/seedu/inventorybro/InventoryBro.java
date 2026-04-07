package seedu.inventorybro;

import seedu.inventorybro.storage.ArrayStorage;
//import seedu.inventorybro.storage.TransactionStorage;

public class InventoryBro {
    private Ui ui;
    private ItemList items;
    private final ArrayStorage arrayStorage;
    //private final TransactionStorage transactionStorage;

    public InventoryBro() {
        ui = new Ui();
        arrayStorage = new ArrayStorage();
        //transactionStorage = new TransactionStorage();
        items = arrayStorage.loadItemList();
    }

    public void run() {
        ui.showWelcome();

        while (true) {

            String fullCommand = ui.readCommand();

            if (fullCommand.isEmpty()) {
                continue;
            }

            ui.showLine();
            try {
                // Pass the ui object into the parser so the commands can use it to print!
                Parser.parse(fullCommand, items, ui);
            } catch (ExitException e) {
                ui.showLine();
                System.exit(0);
            } catch (IllegalArgumentException e) {
                // Catches all the exceptions thrown by your various Commands!
                ui.showError(e.getMessage());
            }
            arrayStorage.saveArray(items);
            ui.showLine();
        }
    }

    public static void main(String[] args) {
        new InventoryBro().run();
    }
}
