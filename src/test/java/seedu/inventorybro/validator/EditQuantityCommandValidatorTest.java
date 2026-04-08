package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

class EditQuantityCommandValidatorTest {

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
    void validate_validInput_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditQuantityCommandValidator("editQuantity 1 q/10").validate(items));
    }

    @Test
    void validate_validInputZeroQuantity_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditQuantityCommandValidator("editQuantity 2 q/0").validate(items));
    }

    @Test
    void validate_validInputLastIndex_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditQuantityCommandValidator("editQuantity 3 q/99").validate(items));
    }

    // -------------------------------------------------------------------------
    // Missing arguments
    // -------------------------------------------------------------------------

    @Test
    void validate_missingArguments_throwsFormatError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity").validate(items));
        assertEquals("Invalid editQuantity format. Use: editQuantity INDEX q/NEW_QUANTITY",
                ex.getMessage());
    }

    @Test
    void validate_missingQuantityPrefix_throwsFormatError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity 1 10").validate(items));
        assertEquals("Invalid editQuantity format. Use: editQuantity INDEX q/NEW_QUANTITY",
                ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Index validation
    // -------------------------------------------------------------------------

    @Test
    void validate_indexZero_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity 0 q/5").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_indexExceedsListSize_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity 4 q/5").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_negativeIndex_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity -1 q/5").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_nonNumericIndex_throwsNumberFormatMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity abc q/5").validate(items));
        assertEquals("Index and quantity must be numbers.", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Quantity value validation
    // -------------------------------------------------------------------------

    @Test
    void validate_negativeQuantity_throwsNegativeQuantityError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity 1 q/-1").validate(items));
        assertEquals("Quantity cannot be negative.", ex.getMessage());
    }

    @Test
    void validate_nonNumericQuantity_throwsNumberFormatMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity 1 q/abc").validate(items));
        assertEquals("Index and quantity must be numbers.", ex.getMessage());
    }

    @Test
    void validate_decimalQuantity_throwsNumberFormatMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditQuantityCommandValidator("editQuantity 1 q/1.5").validate(items));
        assertEquals("Index and quantity must be numbers.", ex.getMessage());
    }
}
