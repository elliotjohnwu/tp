package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

class EditDescriptionCommandValidatorTest {

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
                new EditDescriptionCommandValidator("editDescription 1 d/New Name").validate(items));
    }

    @Test
    void validate_validInputLastIndex_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditDescriptionCommandValidator("editDescription 3 d/Last Item").validate(items));
    }

    @Test
    void validate_descriptionWithSpaces_doesNotThrow() {
        assertDoesNotThrow(() ->
                new EditDescriptionCommandValidator("editDescription 2 d/Some Long Description Here").validate(items));
    }

    // -------------------------------------------------------------------------
    // Missing arguments
    // -------------------------------------------------------------------------

    @Test
    void validate_missingArguments_throwsFormatError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription").validate(items));
        assertEquals("Invalid editDescription format. Use: editDescription INDEX d/NEW_DESCRIPTION",
                ex.getMessage());
    }

    @Test
    void validate_missingDescriptionPrefix_throwsFormatError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription 1 New Name").validate(items));
        assertEquals("Invalid editDescription format. Use: editDescription INDEX d/NEW_DESCRIPTION",
                ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Index validation
    // -------------------------------------------------------------------------

    @Test
    void validate_indexZero_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription 0 d/Name").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_indexExceedsListSize_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription 4 d/Name").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_negativeIndex_throwsInvalidIndex() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription -1 d/Name").validate(items));
        assertEquals("Invalid index.", ex.getMessage());
    }

    @Test
    void validate_nonNumericIndex_throwsNumberFormatMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription abc d/Name").validate(items));
        assertEquals("Index must be a number.", ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // Description value validation
    // -------------------------------------------------------------------------

    @Test
    void validate_emptyDescription_throwsEmptyDescriptionError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription 1 d/").validate(items));
        assertEquals("Item description cannot be empty.", ex.getMessage());
    }

    @Test
    void validate_whitespaceOnlyDescription_throwsEmptyDescriptionError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new EditDescriptionCommandValidator("editDescription 1 d/   ").validate(items));
        assertEquals("Item description cannot be empty.", ex.getMessage());
    }
}
