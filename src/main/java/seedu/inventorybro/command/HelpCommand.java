package seedu.inventorybro.command;

import static java.util.Map.entry;

import java.util.Map;

import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.validator.HelpCommandValidator;

//@@author adbsw

/**
 * Displays to the user the names and summaries of each command of the application or the detailed
 * instruction of the command specified in the input.
 */
public class HelpCommand implements Command {
    private static final String HELPSUMMARYMESSAGE = """
            Command names and their summaries:
            addItem:    Adds a new item of a given quantity to the current inventory list.
            deleteItem: Deletes an item from the current inventory list.
            editItem:   Edits the name and/or quantity of an existing item in the inventory
                        based on item index. At least name or quantity must be provided,
                        existing values are updated to the input values.
            findItem:   Finds an item in the current inventory list based on the keyword typed \

                        or displays message to the user that the inventory does not have item
                        that matches keyword.
            transact:   Updates stock quantities after a sale or restock.
            listItems:  Displays all items currently in the inventory, or displays message
                        to the user that the inventory is empty if there are no items.
            help:       Displays summaries of each command to the user, or displays a detailed
                        instruction of a specified command.
            exit:       Closes the application.

            For further details on a particular command, specify it using 'help [COMMAND_NAME]'.
            """;
    private static final String HELPADDITEMMESSAGE = """
            addItem:
            Adds a new item of a given name and quantity to the current inventory list.

            Example usage: addItem d/Apples q/10
            This adds an item named 'Apples' of quantity '10' to the inventory list.
            """;
    private static final String HELPDELETEITEMMESSAGE = """
            deleteItem:
            Deletes an item from the current inventory list based on the provided list index.
            Enter 'listItems' to view the list index of the item you wish to delete.

            Example usage: deleteItem 1
            This removes the item indexed at 1 in the inventory list.
            """;
    private static final String HELPEDITITEMMESSAGE = """
            editItem:
            Edits the name and/or quantity of an existing item in the current inventory list
            based on the provided list index. At least a name or quantity should be provided.
            Enter 'listItems' to view the list index of the item you wish to edit.

            Example usages:-
            (Both name and quantity fields provided): editItem 1 d/Oranges q/20
            This updates the name and quantity of the item indexed at 1 in the inventory list
            to 'Oranges' and '20' respectively.

            (Just name field provided): editItem 1 d/Oranges
            This updates only the name of the item indexed at 1 in the inventory list to
            'Oranges'.

            (Just quantity field provided): editItem 1 q/20
            This updates only the quantity of the item indexed at 1 in the inventory list to
            '20'.
            """;
    private static final String HELPFINDITEMMESSAGE = """
            findItem:
            Finds and lists all items in the inventory whose description contains the keyword.
            The search is case-insensitive and matches partial words.

            Example usage: findItem app
            This displays all items containing 'app' in their name, such as 'Apples' or 'Pineapple'.
            """;
    private static final String HELPTRANSACTMESSAGE = """
            transact:
            Increases (restocking) or decreases (selling) quantity of the item based on the
            provided list index.

            To increase the quantity, simply key in the change in quantity.
            To decrease the quantity, precede the change in quantity with '-'.

            The existing quantity value will be updated by adding or subtracting the provided
            change in quantity. The quantity cannot be decreased below 0 or it will result in
            an error.

            Enter 'listItems' to view the list index of the item you wish to transact.

            Example usages:-
            (Restocking): transact 1 q/10
            This adds '10' to the existing quantity assigned to the item indexed at 1
            in the inventory list.

            (Selling): transact 1 q/-10
            This subtract off '10' from the existing quantity assigned to the item indexed at 1
            in the inventory list, provided that it does not result in a quantity lower than 0.
            Otherwise, an error will be shown.
            """;
    private static final String HELPLISTITEMSMESSAGE = """
            listItems:
            Displays the list of items and their quantities in the current inventory, or displays
            a message to the user that inventory is empty if there are no items in the current
            inventory.

            Example usage: listItems
            """;
    private static final String HELPHELPMESSAGE = """
            help:
            Displays the command names of the application and their summaries, or a command can
            be specified to display a more detailed instruction of it. You can use 'help' to view
            the valid name of the command, and specify that command to view further details of
            it.

            Example usages:-
            (Without specifying a command): help
            This displays each command name and their summaries.

            (Specifying a command): help addItem
            This displays a more detailed instruction of how to use the command 'addItem'.
            """;
    private static final String HELPEXITMESSAGE = """
            exit:
            Closes the application.

            Example usage: exit
            """;
    private static final Map<String, String> COMMANDMESSAGES = Map.ofEntries(
            entry("addItem", HELPADDITEMMESSAGE),
            entry("deleteItem", HELPDELETEITEMMESSAGE),
            entry("editItem", HELPEDITITEMMESSAGE),
            entry("findItem", HELPFINDITEMMESSAGE),
            entry("transact", HELPTRANSACTMESSAGE),
            entry("listItems", HELPLISTITEMSMESSAGE),
            entry("help", HELPHELPMESSAGE),
            entry("exit", HELPEXITMESSAGE)
    );
    private final String input;

    /**
     * Creates a help command from the raw user input.
     *
     * @param input The full help command string.
     */
    public HelpCommand(String input) {
        assert input != null : "Input line should not be null";
        this.input = input;
    }

    /**
     * Validates the help command input and prints the summaries of each command to the user, or
     * prints the detailed instruction of the command specified in the input.
     *
     * @param items The inventory item list to operate on.
     * @param ui    The ui object.
     */
    @Override
    public void execute(ItemList items, Ui ui) {
        new HelpCommandValidator(input).validate(items);

        String[] words = input.split(" ");
        String info;
        if (words.length == 1) {
            info = HELPSUMMARYMESSAGE;
        } else {
            info = COMMANDMESSAGES.get(words[1]);
        }

        ui.showMessage(info);
    }
}
