package seedu.inventorybro.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;
import seedu.inventorybro.Ui;

class EditPriceCommandTest {

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
    void execute_validIntegerPrice_updatesPrice() {
        new EditPriceCommand("editPrice 1 p/10").execute(items, ui);

        assertEquals(10.0, items.getItem(0).getPrice());
    }

    @Test
    void execute_validDecimalPrice_updatesPrice() {
        new EditPriceCommand("editPrice 1 p/9.99").execute(items, ui);

        assertEquals(9.99, items.getItem(0).getPrice());
    }

    @Test
    void execute_validZeroPrice_setsZero() {
        new EditPriceCommand("editPrice 2 p/0.0").execute(items, ui);

        assertEquals(0.0, items.getItem(1).getPrice());
    }

    @Test
    void execute_validInputLastIndex_updatesCorrectItem() {
        new EditPriceCommand("editPrice 3 p/100.00").execute(items, ui);

        assertEquals(100.00, items.getItem(2).getPrice());
    }

    // -------------------------------------------------------------------------
    // Validation failures — delegate to validator (no item mutation)
    // -------------------------------------------------------------------------

    @Test
    void execute_missingArguments_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice").execute(items, ui));
    }

    @Test
    void execute_missingPricePrefix_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice 1 9.99").execute(items, ui));
    }

    @Test
    void execute_outOfBoundsIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice 99 p/9.99").execute(items, ui));
    }

    @Test
    void execute_zeroIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice 0 p/9.99").execute(items, ui));
    }

    @Test
    void execute_negativePrice_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice 1 p/-1.00").execute(items, ui));
    }

    @Test
    void execute_nonNumericPrice_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice 1 p/abc").execute(items, ui));
    }

    @Test
    void execute_nonNumericIndex_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommand("editPrice abc p/9.99").execute(items, ui));
    }
}
