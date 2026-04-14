package seedu.inventorybro;

import java.util.Optional;

import seedu.inventorybro.command.AddCommand;
import seedu.inventorybro.command.AddCategoryCommand;
import seedu.inventorybro.command.DeleteCategoryCommand;
import seedu.inventorybro.command.ListCategoriesCommand;
import seedu.inventorybro.command.Command;
import seedu.inventorybro.command.DeleteCommand;
import seedu.inventorybro.command.EditDescriptionCommand;
import seedu.inventorybro.command.EditPriceCommand;
import seedu.inventorybro.command.EditQuantityCommand;
import seedu.inventorybro.command.EditCategoryCommand;
import seedu.inventorybro.command.ExitCommand;
import seedu.inventorybro.command.FilterCommand;
import seedu.inventorybro.command.FindCommand;
import seedu.inventorybro.command.HelpCommand;
import seedu.inventorybro.command.ListCommand;
import seedu.inventorybro.command.ShowTransactionHistoryCommand;
import seedu.inventorybro.command.TransactCommand;
import seedu.inventorybro.storage.TransactionStorage;

public class Parser {
    private static final TypoDetector TYPO_DETECTOR = new TypoDetector();

    public static void parse(String line, ItemList items, CategoryList categories, Ui ui,
                             TransactionStorage transactionStorage) {
        assert line != null : "Input line should not be null";
        assert items != null : "ItemList should not be null";
        assert categories != null : "CategoryList should not be null";
        assert ui != null : "Ui should not be null";

        Command command = parseCommand(line, transactionStorage);
        if (command == null) {
            handleUnknownCommand(line, ui);
            return;
        }

        command.execute(items, categories, ui);
    }

    private static Command parseCommand(String line, TransactionStorage transactionStorage) {
        String trimmedLine = line.trim();
        String firstWord = extractFirstWord(trimmedLine).toLowerCase();

        switch (firstWord) {
        case "additem":
            return new AddCommand(normalize(trimmedLine, CommandWord.ADD_ITEM.getWord()));
        case "deleteitem":
            return new DeleteCommand(normalize(trimmedLine, CommandWord.DELETE_ITEM.getWord()));
        case "editquantity":
            return new EditQuantityCommand(normalize(trimmedLine, CommandWord.EDIT_QUANTITY.getWord()));
        case "editdescription":
            return new EditDescriptionCommand(normalize(trimmedLine, CommandWord.EDIT_DESCRIPTION.getWord()),
                    transactionStorage);
        case "editprice":
            return new EditPriceCommand(normalize(trimmedLine, CommandWord.EDIT_PRICE.getWord()));
        case "editcategory":
            return new EditCategoryCommand(normalize(trimmedLine, CommandWord.EDIT_CATEGORY.getWord()));
        case "transact":
            return new TransactCommand(normalize(trimmedLine, CommandWord.TRANSACT.getWord()), transactionStorage);
        case "filteritem":
            return new FilterCommand(normalize(trimmedLine, CommandWord.FILTER_ITEM.getWord()));
        case "showhistory":
            return new ShowTransactionHistoryCommand(normalize(trimmedLine, CommandWord.SHOW_HISTORY.getWord()),
                    transactionStorage);
        case "listitems":
            return new ListCommand(normalize(trimmedLine, CommandWord.LIST_ITEMS.getWord()));
        case "finditem":
            return new FindCommand(normalize(trimmedLine, CommandWord.FIND_ITEM.getWord()));
        case "addcategory":
            return new AddCategoryCommand(normalize(trimmedLine, CommandWord.ADD_CATEGORY.getWord()));
        case "listcategories":
            return new ListCategoriesCommand(normalize(trimmedLine, CommandWord.LIST_CATEGORY.getWord()));
        case "deletecategory":
            return new DeleteCategoryCommand(normalize(trimmedLine, CommandWord.DELETE_CATEGORY.getWord()));
        case "help":
            return new HelpCommand(normalize(trimmedLine, CommandWord.HELP.getWord()));
        case "exit":
            return new ExitCommand();
        default:
            return null;
        }
    }

    /**
     * Replaces the first word of {@code line} with {@code canonicalWord}, preserving the rest of the input.
     */
    private static String normalize(String line, String canonicalWord) {
        String firstWord = extractFirstWord(line);
        return canonicalWord + line.substring(firstWord.length());
    }

    private static void handleUnknownCommand(String line, Ui ui) {
        String firstWord = extractFirstWord(line);
        Optional<String> suggestion = TYPO_DETECTOR.findClosestMatch(firstWord);
        if (suggestion.isPresent()) {
            ui.showMessage("Do you mean " + suggestion.get() + "?");
        } else {
            ui.showError("Invalid command, please try addCategory, deleteCategory, " +
                    "listCategories, addItem, deleteItem, editDescription, editPrice," +
                    " editQuantity, transact, showHistory, listItems, help, exit");
        }
    }

    private static String extractFirstWord(String line) {
        return line.trim().split("\\s+")[0];
    }
}
