package seedu.inventorybro;

import seedu.inventorybro.storage.ArrayStorage;
import seedu.inventorybro.storage.CategoryStorage;
import seedu.inventorybro.storage.TransactionStorage;

import java.io.IOException;


public class InventoryBro {
    private Ui ui;
    private ItemList items;
    private CategoryList categories;
    private final ArrayStorage arrayStorage;
    private final CategoryStorage categoryStorage;
    private final TransactionStorage transactionStorage;

    public InventoryBro() {
        ui = new Ui();
        categories = new CategoryList();

        categoryStorage = new CategoryStorage();
        loadCategories();

        arrayStorage = new ArrayStorage(categories);
        items = arrayStorage.loadItemList();

        transactionStorage = new TransactionStorage();
    }

    private void loadCategories() {
        for (Category loadedCat : categoryStorage.load()) {
            if (!categories.containsCategory(loadedCat.getName())) {
                categories.addCategory(loadedCat);
            }
        }
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
                Parser.parse(fullCommand, items, categories, ui, transactionStorage);
            } catch (ExitException e) {
                ui.showLine();
                System.exit(0);
            } catch (IllegalArgumentException e) {
                // Catches all the exceptions thrown by your various Commands!
                ui.showError(e.getMessage());
            }

            try {
                categoryStorage.saveCategories(categories);
                arrayStorage.saveArray(items);
            } catch (IOException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }

    public static void main(String[] args) {
        new InventoryBro().run();
    }
}
