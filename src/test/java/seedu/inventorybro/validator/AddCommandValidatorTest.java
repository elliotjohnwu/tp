package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

//@@author kenpegrasio
/**
 * Validation tests for {@link AddCommandValidator}.
 */
class AddCommandValidatorTest {

    private final ItemList items = new ItemList();

    /**
     * Verifies that a well-formed addItem input passes validation.
     */
    @Test
    void validate_validInput_noException() {
        assertDoesNotThrow(() -> new AddCommandValidator("addItem d/Apple q/10").validate(items));
    }

    /**
     * Verifies that a well-formed addItem input with a multi-word name passes validation.
     */
    @Test
    void validate_multiWordName_noException() {
        assertDoesNotThrow(() -> new AddCommandValidator("addItem d/Green Apple q/25").validate(items));
    }

    /**
     * Verifies that malformed addItem input is rejected.
     */
    @Test
    void validate_invalidFormat_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AddCommandValidator("addItem Apple 10").validate(items)
        );
    }

    /**
     * Verifies that missing quantity prefix is rejected.
     */
    @Test
    void validate_missingQuantityPrefix_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AddCommandValidator("addItem d/Apple").validate(items)
        );
    }

    /**
     * Verifies that adding an item whose name already exists (case-insensitive) is rejected.
     */
    @Test
    void validate_duplicateName_throwsException() {
        items.addItem(new Item("Apple", 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> new AddCommandValidator("addItem d/apple q/5").validate(items)
        );
    }

    /**
     * Verifies that a name with trailing whitespace is trimmed before the duplicate check,
     * so "Apple " and "Apple" are treated as the same item.
     */
    @Test
    void validate_duplicateNameWithTrailingSpace_throwsException() {
        items.addItem(new Item("Apple", 10));

        assertThrows(
                IllegalArgumentException.class,
                () -> new AddCommandValidator("addItem d/Apple  q/5").validate(items)
        );
    }

    /**
     * Verifies that adding a unique name to a non-empty list passes validation.
     * This covers the equalsIgnoreCase → false branch in DuplicateItemValidator,
     * where the loop iterates over existing items but finds no match.
     */
    @Test
    void validate_uniqueNameWithOtherItemsPresent_noException() {
        items.addItem(new Item("Banana", 5));

        assertDoesNotThrow(() -> new AddCommandValidator("addItem d/Apple q/10").validate(items));
    }

    /**
     * Verifies that passing null as input to the constructor triggers an AssertionError.
     */
    @Test
    void constructor_nullInput_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new AddCommandValidator(null));
    }
}
