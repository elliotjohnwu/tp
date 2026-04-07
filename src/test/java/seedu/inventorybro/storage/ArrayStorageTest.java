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

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

/**
 * Tests for ArrayStorage covering save, load, and corruption handling.
 */
class ArrayStorageTest {

    // use a separate test file so real data is never affected
    private static final String TEST_FILE = "./data/test_inventory.txt";
    private ArrayStorage storage;

    @BeforeEach
    void setUp() {
        // point storage to test file by overriding file path via subclass
        storage = new ArrayStorage() {
            @Override
            public void saveArray(ArrayList<Item> items) {
                // redirect to test file
                try {
                    File file = new File(TEST_FILE);
                    file.getParentFile().mkdirs();
                    FileWriter fw = new FileWriter(file);
                    for (Item item : items) {
                        fw.write(item.toSaveFormat() + System.lineSeparator());
                    }
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public ArrayList<Item> load() {
                ArrayList<Item> items = new ArrayList<>();
                File file = new File(TEST_FILE);
                if (!file.exists()) {
                    return items;
                }
                try (java.util.Scanner s = new java.util.Scanner(file)) {
                    int lineNumber = 0;
                    while (s.hasNextLine()) {
                        lineNumber++;
                        String line = s.nextLine().trim();
                        if (!line.isEmpty()) {
                            Item decoded = decode(line, lineNumber);
                            if (decoded != null) {
                                items.add(decoded);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return items;
            }
        };
    }

    @AfterEach
    void tearDown() {
        // delete test file after each test
        new File(TEST_FILE).delete();
    }

    /**
     * Verifies that saving and loading an ItemList preserves all items.
     */
    @Test
    void saveArray_andLoad_itemsPreserved() {
        ItemList items = new ItemList();
        items.addItem(new Item("Coke Can", 50));
        items.addItem(new Item("Sprite Bottle", 30));

        storage.saveArray(items.getItems());
        ArrayList<Item> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("Coke Can", loaded.get(0).getDescription());
        assertEquals(50, loaded.get(0).getQuantity());
        assertEquals("Sprite Bottle", loaded.get(1).getDescription());
        assertEquals(30, loaded.get(1).getQuantity());
    }

    /**
     * Verifies that loading from a non-existent file returns an empty list.
     */
    @Test
    void load_noFileExists_returnsEmptyList() {
        new File(TEST_FILE).delete();
        ArrayList<Item> loaded = storage.load();
        assertTrue(loaded.isEmpty());
    }

    /**
     * Verifies that saving an empty list results in an empty file and empty load.
     */
    @Test
    void saveArray_emptyList_loadsEmpty() {
        storage.saveArray(new ArrayList<>());
        ArrayList<Item> loaded = storage.load();
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
        fw.write("50 | Coke Can" + System.lineSeparator());
        fw.write("CORRUPTED LINE" + System.lineSeparator());
        fw.write("30 | Sprite Bottle" + System.lineSeparator());
        fw.close();

        ArrayList<Item> loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("Coke Can", loaded.get(0).getDescription());
        assertEquals("Sprite Bottle", loaded.get(1).getDescription());
    }

    /**
     * Verifies that saving overwrites previous content.
     */
    @Test
    void saveArray_overwritesPreviousContent() {
        ItemList first = new ItemList();
        first.addItem(new Item("Coke Can", 50));
        storage.saveArray(first.getItems());

        ItemList second = new ItemList();
        second.addItem(new Item("Fanta", 20));
        storage.saveArray(second.getItems());

        ArrayList<Item> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals("Fanta", loaded.get(0).getDescription());
    }

    /**
     * Verifies that loadItemList returns an ItemList with the correct contents.
     */
    @Test
    void loadItemList_returnsCorrectItemList() {
        ItemList items = new ItemList();
        items.addItem(new Item("Coke Can", 50));
        storage.saveArray(items.getItems());

        ItemList loaded = storage.loadItemList();
        assertEquals(1, loaded.size());
        assertEquals("Coke Can", loaded.getItem(0).getDescription());
    }
}
