package seedu.inventorybro.storage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;

//@@author elliotjohnwu
/**
 * Handles saving and loading transaction history as plain strings.
 * No Transaction class is required — formatting is handled internally.
 * Each entry records the item name, quantity change, and timestamp.
 */
public class TransactionStorage extends Storage<String> {

    private static final String FILE_PATH = "./data/transactions.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Creates a TransactionStorage using the default transactions file path.
     */
    public TransactionStorage() {
        super(FILE_PATH);
    }

    /**
     * Builds and saves a history entry from item name and quantity change.
     * Generates the timestamp internally.
     *
     * @param itemName The name of the item transacted.
     * @param change   The quantity change, positive for restock negative for sale.
     */
    public void saveHistory(String itemName, int change) throws IllegalArgumentException {
        assert itemName != null && !itemName.isEmpty() : "Item name should not be null or empty";
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entry = itemName + " | " + change + " | " + timestamp;
        try {
            saveHistory(entry);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        logger.log(Level.INFO, "Saved transaction history entry: {0}", entry);
    }

    /**
     * Encodes a history entry string — no conversion needed.
     *
     * @param entry The string entry to encode.
     * @return The entry unchanged.
     */
    @Override
    protected String encode(String entry) {
        return entry;
    }

    /**
     * Decodes and validates a history entry line.
     * Returns null if the line does not have the expected format.
     *
     * @param line       The line to decode.
     * @param lineNumber The line number for logging.
     * @return The line if valid, or null if corrupted.
     */
    @Override
    protected String decode(String line, int lineNumber) {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                throw new IllegalArgumentException("Expected 3 parts");
            }
            Integer.parseInt(parts[1].trim());
            return line;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Skipping corrupted transaction line {0}: {1} — Reason: {2}",
                    new Object[]{lineNumber, line, e.getMessage()});
            return null;
        }
    }
}
