package seedu.inventorybro.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

//@@author elliotjohnwu
/**
 * Abstract base class providing shared file handling logic for all storage classes.
 * Subclasses provide encodeItem and decodeItem for their specific data type.
 *
 * @param <T> The type of object this storage class handles.
 */
public abstract class Storage<T> {

    protected final String filePath;
    protected final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Creates a Storage with the given file path.
     *
     * @param filePath Relative path to the data file.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path should not be null or empty";
        this.filePath = filePath;
    }

    /**
     * Saves a full list to the data file, overwriting existing content.
     * Used for the inventory item list.
     *
     * @param items The list to save.
     */
    public void saveArray(ArrayList<T> items) {
        assert items != null : "Items list should not be null";
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                logger.log(Level.WARNING, "Could not create directory: {0}", parentDir.getPath());
                return;
            }
            FileWriter fw = new FileWriter(file);
            for (T item : items) {
                fw.write(encode(item) + System.lineSeparator());
            }
            fw.close();
            logger.log(Level.INFO, "Saved {0} items to {1}",
                    new Object[]{items.size(), filePath});
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not save to {0}: {1}",
                    new Object[]{filePath, e.getMessage()});
        }
    }

    /**
     * Appends a single item to the data file without overwriting.
     * Used for recording transaction history entries.
     *
     * @param item The item to add to history.
     */
    public void saveHistory(T item) {
        assert item != null : "Item should not be null";
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                logger.log(Level.WARNING, "Could not create directory: {0}", parentDir.getPath());
                return;
            }
            FileWriter fw = new FileWriter(file, true);
            fw.write(encode(item) + System.lineSeparator());
            fw.close();
            logger.log(Level.INFO, "Added 1 entry to history at {0}", filePath);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not add to history at {0}: {1}",
                    new Object[]{filePath, e.getMessage()});
        }
    }

    /**
     * Loads all items from the data file.
     * Returns an empty list if the file does not exist.
     * Skips corrupted lines with a warning.
     *
     * @return The loaded list of items.
     */
    public ArrayList<T> load() {
        ArrayList<T> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            logger.log(Level.INFO, "No file found at {0}, starting fresh.", filePath);
            return items;
        }
        try (Scanner s = new Scanner(file)) {
            int lineNumber = 0;
            while (s.hasNextLine()) {
                lineNumber++;
                String line = s.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                T decoded = decode(line, lineNumber);
                if (decoded != null) {
                    items.add(decoded);
                }
            }
            logger.log(Level.INFO, "Loaded {0} items from {1}",
                    new Object[]{items.size(), filePath});
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not load from {0}: {1}",
                    new Object[]{filePath, e.getMessage()});
        }
        return items;
    }

    /**
     * Converts an item into a string for saving to file.
     * Subclasses must implement this for their specific type.
     *
     * @param item The item to encode.
     * @return The encoded string.
     */
    protected abstract String encode(T item);

    /**
     * Converts a file line back into an item object.
     * Subclasses must implement this for their specific type.
     * Returns null if the line is corrupted.
     *
     * @param line       The line to decode.
     * @param lineNumber The line number for logging.
     * @return The decoded item, or null if corrupted.
     */
    protected abstract T decode(String line, int lineNumber);
}
