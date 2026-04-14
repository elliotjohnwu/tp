package seedu.inventorybro.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
        // Reset static storage so tests remain isolated
        EditDescriptionCommand.setTransactionStorage(null);
    }

    // -------------------------------------------------------------------------
    // Transaction history sync
    // -------------------------------------------------------------------------

    @Test
    void execute_renameItem_transactionHistoryUpdated() {
        TransactionStorageHistoryStub stub = new TransactionStorageHistoryStub(new ArrayList<>());
        stub.saveHistory("Apple", -3);
        stub.saveHistory("Banana", 5);
        EditDescriptionCommand.setTransactionStorage(stub);

        new EditDescriptionCommand("editDescription 1 d/Green Apple").execute(items, categories, ui);

        ArrayList<String> history = stub.load();
        assertEquals(2, history.size());
        // Old "Apple" entries should now be "Green Apple"
        assertEquals(true, history.get(0).startsWith("Green Apple |"));
        // Unrelated "Banana" entry should be unchanged
        assertEquals(true, history.get(1).startsWith("Banana |"));
    }

    @Test
    void execute_renameItem_noStorageSet_doesNotThrow() {
        // Ensure no transactionStorage is set (tearDown clears it)
        new EditDescriptionCommand("editDescription 1 d/Green Apple").execute(items, categories, ui);
        assertEquals("Green Apple", items.getItem(0).getDescription());
    }

    // -------------------------------------------------------------------------
    // Successful execution
    // -------------------------------------------------------------------------

    @Test
    void execute_validInput_updatesDescription() {
        new EditDescriptionCommand("editDescription 1 d/Updated Name").execute(items, categories, ui);

        assertEquals("Updated Name", items.getItem(0).getDescription());
    }

    @Test
    void execute_validInputLastIndex_updatesCorrectItem() {
        new EditDescriptionCommand("editDescription 3 d/Last Item").execute(items, categories, ui);

        assertEquals("Last Item", items.getItem(2).getDescription());
    }

    @Test
    void execute_descriptionWithSpaces_preservesFullDescription() {
        new EditDescriptionCommand("editDescription 2 d/Some Long Description").execute(items, categories, ui);

        assertEquals("Some Long Description", items.getItem(1).getDescription());
    }

    // -------------------------------------------------------------------------
    // Validation failures — delegate to validator (no item mutation)
    // -------------------------------------------------------------------------

    @Test
    void execute_missingArguments_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription").execute(items, categories, ui));
    }

    @Test
    void execute_missingDescriptionPrefix_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 1 NewName").execute(items, categories, ui));
    }

    @Test
    void execute_outOfBoundsIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 99 d/Name").execute(items, categories, ui));
    }

    @Test
    void execute_zeroIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 0 d/Name").execute(items, categories, ui));
    }

    @Test
    void execute_emptyDescription_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 1 d/").execute(items, categories, ui));
    }

    @Test
    void execute_nonNumericIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription abc d/Name").execute(items, categories, ui));
    }
}
