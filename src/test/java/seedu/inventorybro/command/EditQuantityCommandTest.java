package seedu.inventorybro.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;

class EditQuantityCommandTest {

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
    void execute_validInput_updatesQuantity() {
        new EditQuantityCommand("editQuantity 1 q/10").execute(items, ui);

        assertEquals(10, items.getItem(0).getQuantity());
    }

    @Test
    void execute_validInputZeroQuantity_setsZero() {
        new EditQuantityCommand("editQuantity 2 q/0").execute(items, ui);

        assertEquals(0, items.getItem(1).getQuantity());
    }

    @Test
    void execute_validInputLastIndex_updatesCorrectItem() {
        new EditQuantityCommand("editQuantity 3 q/50").execute(items, ui);

        assertEquals(50, items.getItem(2).getQuantity());
    }

    // -------------------------------------------------------------------------
    // Validation failures — delegate to validator (no item mutation)
    // -------------------------------------------------------------------------

    @Test
    void execute_missingArguments_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity").execute(items, ui));
    }

    @Test
    void execute_missingQuantityPrefix_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity 1 10").execute(items, ui));
    }

    @Test
    void execute_outOfBoundsIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity 99 q/5").execute(items, ui));
    }

    @Test
    void execute_zeroIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity 0 q/5").execute(items, ui));
    }

    @Test
    void execute_negativeQuantity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity 1 q/-1").execute(items, ui));
    }

    @Test
    void execute_nonNumericQuantity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity 1 q/abc").execute(items, ui));
    }

    @Test
    void execute_decimalQuantity_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommand("editQuantity 1 q/1.5").execute(items, ui));
    }
}
