package seedu.inventorybro.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;

class EditDescriptionCommandTest {

    private final Ui ui = new Ui();
    private ItemList items;

    @BeforeEach
    void setUp() {
        items = new ItemList();
        items.addItem(new Item("Apple", 5));
        items.addItem(new Item("Banana", 3));
        items.addItem(new Item("Cherry", 8));
    }

    // -------------------------------------------------------------------------
    // Successful execution
    // -------------------------------------------------------------------------

    @Test
    void execute_validInput_updatesDescription() {
        new EditDescriptionCommand("editDescription 1 d/Updated Name").execute(items, ui);

        assertEquals("Updated Name", items.getItem(0).getDescription());
    }

    @Test
    void execute_validInputLastIndex_updatesCorrectItem() {
        new EditDescriptionCommand("editDescription 3 d/Last Item").execute(items, ui);

        assertEquals("Last Item", items.getItem(2).getDescription());
    }

    @Test
    void execute_descriptionWithSpaces_preservesFullDescription() {
        new EditDescriptionCommand("editDescription 2 d/Some Long Description").execute(items, ui);

        assertEquals("Some Long Description", items.getItem(1).getDescription());
    }

    // -------------------------------------------------------------------------
    // Validation failures — delegate to validator (no item mutation)
    // -------------------------------------------------------------------------

    @Test
    void execute_missingArguments_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription").execute(items, ui));
    }

    @Test
    void execute_missingDescriptionPrefix_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 1 NewName").execute(items, ui));
    }

    @Test
    void execute_outOfBoundsIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 99 d/Name").execute(items, ui));
    }

    @Test
    void execute_zeroIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 0 d/Name").execute(items, ui));
    }

    @Test
    void execute_emptyDescription_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription 1 d/").execute(items, ui));
    }

    @Test
    void execute_nonNumericIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommand("editDescription abc d/Name").execute(items, ui));
    }
}
