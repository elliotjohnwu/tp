package seedu.inventorybro;

//@@author kenpegrasio
/**
 * Enumeration of all valid command keywords recognised by the application.
 * This is the single source of truth for command names — add new commands here
 * and all consumers (autocomplete, typo detection, etc.) pick them up automatically.
 */
public enum CommandWord {
    ADD_ITEM("addItem"),
    DELETE_ITEM("deleteItem"),
    EDIT_ITEM("editItem"),
    TRANSACT("transact"),
    SHOW_HISTORY("showHistory"),
    LIST_ITEMS("listItems"),
    HELP("help"),
    EXIT("exit"),
    FIND_ITEM("findItem"),
    FILTER_ITEM("filterItem");

    private final String word;

    CommandWord(String word) {
        this.word = word;
    }

    /**
     * Returns the command keyword string exactly as the user types it.
     */
    public String getWord() {
        return word;
    }
}
