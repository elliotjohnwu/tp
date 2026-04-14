package seedu.inventorybro.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.CategoryList;
import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;
import seedu.inventorybro.storage.TransactionStorageHistoryStub;

class EditDescriptionCommandTest {

    private final Ui ui = new Ui();
    private ItemList items;
    private CategoryList categories;

    @BeforeEach
    void setUp() {
        items = new ItemList();
        categories = new CategoryList();
        items.addItem(new Item("Apple", 5, 0.0, categories.getCategory("Others")));
        items.addItem(new Item("Banana", 3, 0.0, categories.getCategory("Others")));
        items.addItem(new Item("Cherry", 8, 0.0, categories.getCategory("Others")));
    }

    // -------------------------------------------------------------------------
    // Transaction history sync
    // -------------------------------------------------------------------------

    @Test
    void execute_renameItem_transactionHistoryUpdated() {
        TransactionStorageHistoryStub stub = new TransactionStorageHistoryStub(new ArrayList<>());
        stub.saveHistory("Apple", -3);
        stub.saveHistory("Banana", 5);

        new EditDescriptionCommand("editDescription 1 d/Green Apple", stub).execute(items, categories, ui);

        ArrayList<String> history = stub.load();
        assertEquals(2, history.size());
        // Old "Apple" entries should now be "Green Apple"
        assertTrue(history.get(0).startsWith("Green Apple |"));
        // Unrelated "Banana" entry should be unchanged
        assertTrue(history.get(1).startsWith("Banana |"));
    }

    @Test
    void execute_renameItem_noStorageSet_doesNotThrow() {
        new EditDescriptionCommand("editDescription 1 d/Green Apple", null).execute(items, categories, ui);
        assertEquals("Green Apple", items.getItem(0).getDescription());
    }

    // -------------------------------------------------------------------------
    // Successful execution
    // -------------------------------------------------------------------------

    @Test
    void execute_validInput_updatesDescription() {
        new EditDescriptionCommand("editDescription 1 d/Updated Name", null).execute(items, categories, ui);

        assertEquals("Updated Name", items.getItem(0).getDescription());
    }

    @Test
    void execute_validInputLastIndex_updatesCorrectItem() {
        new EditDescriptionCommand("editDescription 3 d/Last Item", null).execute(items, categories, ui);

        assertEquals("Last Item", items.getItem(2).getDescription());
    }

    @Test
    void execute_descriptionWithSpaces_preservesFullDescription() {
        new EditDescriptionCommand("editDescription 2 d/Some Long Description", null).execute(items, categories, ui);

        assertEquals("Some Long Description", items.getItem(1).getDescription());
    }

    // -------------------------------------------------------------------------
    // Validation failures — delegate to validator (no item mutation)
    // -------------------------------------------------------------------------

    @Test
    void execute_missingArguments_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription", null).execute(items, categories, ui));
    }

    @Test
    void execute_missingDescriptionPrefix_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 1 NewName", null).execute(items, categories, ui));
    }

    @Test
    void execute_outOfBoundsIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 99 d/Name", null).execute(items, categories, ui));
    }

    @Test
    void execute_zeroIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 0 d/Name", null).execute(items, categories, ui));
    }

    @Test
    void execute_emptyDescription_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 1 d/", null).execute(items, categories, ui));
    }

    @Test
    void execute_nonNumericIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription abc d/Name", null).execute(items, categories, ui));
    }
}
