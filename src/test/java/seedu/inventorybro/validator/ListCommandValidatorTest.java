package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

//@@author adbsw
/**
 * Validation tests for {@link ListCommandValidator}.
 */
class ListCommandValidatorTest {

    /**
     * Verifies that the exact listItems input passes validation.
     */
    @Test
    void validate_validInput_noException() {
        ItemList items = new ItemList();

        assertDoesNotThrow(() -> new ListCommandValidator("listItems").validate(items));
        assertDoesNotThrow(() -> new ListCommandValidator("listItems quantity high").validate(items));
        assertDoesNotThrow(() -> new ListCommandValidator("listItems quantity low").validate(items));
        assertDoesNotThrow(() -> new ListCommandValidator("listItems price high").validate(items));
        assertDoesNotThrow(() -> new ListCommandValidator("listItems price low").validate(items));
    }

    /**
     * Verifies that a wrong command word is rejected.
     */
    @Test
    void validate_wrongCommandWord_throwsException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Apple", 50));

        assertThrows(
                IllegalArgumentException.class,
                () -> new ListCommandValidator("list").validate(items)
        );
    }

    /**
     * Verifies that invalid descriptors in user input are rejected.
     */
    @Test
    void validate_invalidDescriptors_throwsException() {
        ItemList items = new ItemList();

        assertThrows(
                IllegalArgumentException.class,
                () -> new ListCommandValidator("listItems invalidField").validate(items)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ListCommandValidator("listItems quantity invalidOrder").validate(items)
        );
    }

    /**
     * Verifies that extra descriptors in user input for are rejected.
     */
    @Test
    void validate_extraDescriptors_throwsException() {
        ItemList items = new ItemList();

        assertThrows(
                IllegalArgumentException.class,
                () -> new ListCommandValidator("ListItems quantity price high").validate(items)
        );
    }

    /**
     * Verifies that a case-variant of the command is rejected.
     */
    @Test
    void validate_wrongCase_throwsException() {
        ItemList items = new ItemList();
        items.addItem(new Item("Apple", 50));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new ListCommandValidator("LiStItEms").validate(items)
        );
        assertEquals("Did you mean 'listItems'?", ex.getMessage());
    }
}
