package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

/**
 * Validation tests for {@link DeleteCommandValidator}.
 */
//@@author fmohamedfaras
class DeleteCommandValidatorTest {

    /**
     * Verifies that a valid index within bounds passes validation.
     */
    @Test
    void validate_validIndex_noException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Apple", 10));

        assertDoesNotThrow(() -> new DeleteCommandValidator("deleteItem 1").validate(items));
    }

    /**
     * Verifies that an out-of-range index is rejected.
     */
    @Test
    void validate_indexOutOfRange_throwsException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Coke Can", 50));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DeleteCommandValidator("deleteItem 99").validate(items)
        );
    }

    /**
     * Verifies that index zero is rejected.
     */
    @Test
    void validate_indexZero_throwsException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Coke Can", 50));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DeleteCommandValidator("deleteItem 0").validate(items)
        );
    }

    /**
     * Verifies that a missing index is rejected.
     */
    @Test
    void validate_missingIndex_throwsException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Coke Can", 50));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DeleteCommandValidator("deleteItem").validate(items)
        );
    }

    /**
     * Verifies that a non-numeric index is rejected.
     */
    @Test
    void validate_nonNumericIndex_throwsException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Coke Can", 50));

        assertThrows(
                IllegalArgumentException.class,
                () -> new DeleteCommandValidator("deleteItem abc").validate(items)
        );
    }
}
