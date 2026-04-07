package seedu.inventorybro.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for TransactionStorage covering saveHistory, load, and corruption handling.
 */
class TransactionStorageTest {

    private static final String TEST_FILE = "./data/test_transactions.txt";
    private TransactionStorage storage;

    @BeforeEach
    void setUp() {
        storage = new TransactionStorage() {
            @Override
            public void saveHistory(String entry) {
                try {
                    File file = new File(TEST_FILE);
                    file.getParentFile().mkdirs();
                    FileWriter fw = new FileWriter(file, true);
                    fw.write(entry + System.lineSeparator());
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public ArrayList<String> load() {
                ArrayList<String> entries = new ArrayList<>();
                File file = new File(TEST_FILE);
                if (!file.exists()) {
                    return entries;
                }
                try (java.util.Scanner s = new java.util.Scanner(file)) {
                    int lineNumber = 0;
                    while (s.hasNextLine()) {
                        lineNumber++;
                        String line = s.nextLine().trim();
                        if (!line.isEmpty()) {
                            String decoded = decode(line, lineNumber);
                            if (decoded != null) {
                                entries.add(decoded);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return entries;
            }
        };
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    /**
     * Verifies that saving a transaction history entry appends it to the file.
     */
    @Test
    void saveHistory_validEntry_appendedToFile() {
        storage.saveHistory("Coke Can", -5);
        ArrayList<String> loaded = storage.load();

        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0).startsWith("Coke Can | -5 |"));
    }

    /**
     * Verifies that multiple saveHistory calls append multiple entries.
     */
    @Test
    void saveHistory_multipleEntries_allAppended() {
        storage.saveHistory("Coke Can", -5);
        storage.saveHistory("Sprite Bottle", 10);
        ArrayList<String> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertTrue(loaded.get(0).startsWith("Coke Can | -5 |"));
        assertTrue(loaded.get(1).startsWith("Sprite Bottle | 10 |"));
    }

    /**
     * Verifies that loading from a non-existent file returns an empty list.
     */
    @Test
    void load_noFileExists_returnsEmptyList() {
        new File(TEST_FILE).delete();
        ArrayList<String> loaded = storage.load();
        assertTrue(loaded.isEmpty());
    }

    /**
     * Verifies that corrupted lines are skipped and valid lines still load.
     */
    @Test
    void load_corruptedLine_skipsAndLoadsValid() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file);
        fw.write("Coke Can | -5 | 2026-03-26 14:30" + System.lineSeparator());
        fw.write("CORRUPTED" + System.lineSeparator());
        fw.write("Sprite Bottle | 10 | 2026-03-26 14:31" + System.lineSeparator());
        fw.close();

        ArrayList<String> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertTrue(loaded.get(0).startsWith("Coke Can"));
        assertTrue(loaded.get(1).startsWith("Sprite Bottle"));
    }

    /**
     * Verifies that a line with non-numeric change value is treated as corrupted.
     */
    @Test
    void load_nonNumericChange_lineSkipped() throws IOException {
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file);
        fw.write("Coke Can | abc | 2026-03-26 14:30" + System.lineSeparator());
        fw.close();

        ArrayList<String> loaded = storage.load();
        assertTrue(loaded.isEmpty());
    }
}

