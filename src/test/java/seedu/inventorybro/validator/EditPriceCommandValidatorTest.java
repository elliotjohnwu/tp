package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

class EditPriceCommandValidatorTest {

    private ItemList items;

    @BeforeEach
    void setUp() {
        items = new ItemList();
        items.addItem(new Item("Apple", 5));
        items.addItem(new Item("Banana", 3));
        items.addItem(new Item("Cherry", 8));
    }

    // -------------------------------------------------------------------------
    // Valid input
    // -------------------------------------------------------------------------

    @Test
    void validate_validIntegerPrice_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditPriceCommandValidator("editPrice 1 p/10").validate(items));
    }

    @Test
    void validate_validDecimalPrice_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditPriceCommandValidator("editPrice 1 p/9.99").validate(items));
    }

    @Test
    void validate_validZeroPrice_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditPriceCommandValidator("editPrice 2 p/0.0").validate(items));
    }

    @Test
    void validate_validInputLastIndex_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditPriceCommandValidator("editPrice 3 p/100.00").validate(items));
    }

    // -------------------------------------------------------------------------
    // Missing arguments
    // -------------------------------------------------------------------------

    @Test
    void validate_missingArguments_throwsFormatError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice").validate(items));
        assertEquals("Invalid editPrice format. Use: editPrice INDEX p/NEW_PRICE",
                ex.getMessage());
    }

    @Test
    void validate_missingPricePrefix_throwsFormatError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice 1 9.99").validate(items));
        assertEquals("Invalid editPrice format. Use: editPrice INDEX p/NEW_PRICE",
                ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Index validation
    // -------------------------------------------------------------------------

    @Test
    void validate_indexZero_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice 0 p/9.99").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_indexExceedsListSize_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice 4 p/9.99").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_negativeIndex_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice -1 p/9.99").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_nonNumericIndex_throwsNumberFormatMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice abc p/9.99").validate(items));
        assertEquals("Index must be a number and price must be a valid decimal.", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Price value validation
    // -------------------------------------------------------------------------

    @Test
    void validate_negativePrice_throwsNegativePriceError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice 1 p/-1.00").validate(items));
        assertEquals("Price cannot be negative.", ex.getMessage());
    }

    @Test
    void validate_nonNumericPrice_throwsNumberFormatMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditPriceCommandValidator("editPrice 1 p/abc").validate(items));
        assertEquals("Index must be a number and price must be a valid decimal.", ex.getMessage());
    }
}
