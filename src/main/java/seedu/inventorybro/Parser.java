package seedu.inventorybro;

import java.util.Optional;

import seedu.inventorybro.command.AddCommand;
import seedu.inventorybro.command.Command;
import seedu.inventorybro.command.DeleteCommand;
import seedu.inventorybro.command.EditCommand;
import seedu.inventorybro.command.ExitCommand;
import seedu.inventorybro.command.FilterCommand;
import seedu.inventorybro.command.HelpCommand;
import seedu.inventorybro.command.ListCommand;
import seedu.inventorybro.command.TransactCommand;
import seedu.inventorybro.command.ShowTransactionHistoryCommand;
import seedu.inventorybro.command.FindCommand;

public class Parser {
    private static final TypoDetector TYPO_DETECTOR = new TypoDetector();

    public static void parse(String line, ItemList items, Ui ui) {
        assert line != null : "Input line should not be null";
        assert items != null : "ItemList should not be null";
        assert ui != null : "Ui should not be null";

        Command command = parseCommand(line);
        if (command == null) {
            handleUnknownCommand(line, ui);
            return;
        }

        command.execute(items, ui);
    }

    private static Command parseCommand(String line) {
        String trimmedLine = line.trim();
        String firstWord = extractFirstWord(trimmedLine).toLowerCase();

        switch (firstWord) {
        case "additem":
            return new AddCommand(trimmedLine);
        case "deleteitem":
            return new DeleteCommand(trimmedLine);
        case "edititem":
            return new EditCommand(trimmedLine);
        case "transact":
            return new TransactCommand(trimmedLine);
        case "filteritem":
            return new FilterCommand(trimmedLine);
        case "showhistory":
            return new ShowTransactionHistoryCommand(trimmedLine);
        case "listitems":
            return new ListCommand(trimmedLine);
        case "finditem":
            return new FindCommand(trimmedLine);
        case "help":
            return new HelpCommand(trimmedLine);
        case "exit":
            return new ExitCommand();
        default:
            return null;
        }
    }

    private static void handleUnknownCommand(String line, Ui ui) {
        String firstWord = extractFirstWord(line);
        Optional<String> suggestion = TYPO_DETECTOR.findClosestMatch(firstWord);
        if (suggestion.isPresent()) {
            ui.showMessage("Do you mean " + suggestion.get() + "?");
        } else {
            ui.showError("Invalid command, please try addItem, deleteItem, editItem, transact, " +
                    "showHistory, listItems, help, exit");
        }
    }

    private static String extractFirstWord(String line) {
        return line.trim().split("\\s+")[0];
    }
}
