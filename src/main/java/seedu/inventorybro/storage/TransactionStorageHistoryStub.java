package seedu.inventorybro.storage;

import java.util.ArrayList;

//@@author elliotjohnwu
/**
 * A stub for TransactionStorage used in unit testing.
 * Stores and returns transaction history in memory instead of reading/writing from a file.
 */
public class TransactionStorageHistoryStub extends TransactionStorage {

    private final ArrayList<String> entries;

    /**
     * Creates a stub with predefined transaction entries.
     *
     * @param entries The initial entries for the stub.
     */
    public TransactionStorageHistoryStub(ArrayList<String> entries) {
        super(); // Calls parent constructor to satisfy inheritance
        this.entries = entries;
    }

    /**
     * Traps the save call and adds it to the internal memory list instead of a file.
     *
     * @param itemName The name of the item.
     * @param change   The quantity change.
     */
    @Override
    public void saveHistory(String itemName, int change) {
        String dummyEntry = itemName + " | " + change + " | TEST_TIMESTAMP";
        entries.add(dummyEntry);
    }

    /**
     * Returns the in-memory entries instead of reading from a file.
     *
     * @return List of transaction history entries.
     */
    @Override
    public ArrayList<String> load() {
        return entries;
    }

    /**
     * Replaces the in-memory entries with the provided list instead of writing to a file.
     * This allows {@code updateItemName} to work correctly in tests.
     *
     * @param items The updated list of entries.
     */
    @Override
    public void saveArray(ArrayList<String> items) {
        entries.clear();
        entries.addAll(items);
    }
}
