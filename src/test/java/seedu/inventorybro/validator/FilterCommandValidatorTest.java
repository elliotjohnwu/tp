package seedu.inventorybro.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.inventorybro.Item;
import seedu.inventorybro.ItemList;

/**
 * Validation tests for {@link FilterCommandValidator}.
 */
class FilterCommandValidatorTest {

    private ItemList buildItems() {
        ItemList items = new ItemList();
        items.addItem(new Item("Apple", 10));
        items.addItem(new Item("Banana", 5));
        return items;
    }

    // ── Single-predicate: valid inputs ────────────────────────────────────────

    /**
     * Verifies that a valid description equality filter passes validation.
     */
    @Test
    void validate_descriptionEquals_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem description = 'Apple'").validate(buildItems()));
    }

    /**
     * Verifies that a valid description less-than filter passes validation.
     */
    @Test
    void validate_descriptionLessThan_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem description < 'Mango'").validate(buildItems()));
    }

    /**
     * Verifies that a valid description greater-than filter passes validation.
     */
    @Test
    void validate_descriptionGreaterThan_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem description > 'Apple'").validate(buildItems()));
    }

    /**
     * Verifies that a description value with multiple words in quotes passes validation.
     */
    @Test
    void validate_descriptionMultiWordValue_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem description = 'Green Apple'").validate(buildItems()));
    }

    /**
     * Verifies that a valid quantity equality filter passes validation.
     */
    @Test
    void validate_quantityEquals_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem quantity = 10").validate(buildItems()));
    }

    /**
     * Verifies that a valid quantity less-than filter passes validation.
     */
    @Test
    void validate_quantityLessThan_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem quantity < 20").validate(buildItems()));
    }

    /**
     * Verifies that a valid quantity greater-than filter passes validation.
     */
    @Test
    void validate_quantityGreaterThan_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem quantity > 3").validate(buildItems()));
    }

    /**
     * Verifies that a valid price equality filter passes validation.
     */
    @Test
    void validate_priceEquals_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem price = 10").validate(buildItems()));
    }

    /**
     * Verifies that a valid price less-than filter passes validation.
     */
    @Test
    void validate_priceLessThan_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem price < 20").validate(buildItems()));
    }

    /**
     * Verifies that a valid price greater-than filter passes validation.
     */
    @Test
    void validate_priceGreaterThan_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem price > 3").validate(buildItems()));
    }

    // ── Single-predicate: invalid inputs ─────────────────────────────────────

    /**
     * Verifies that a completely malformed command is rejected.
     */
    @Test
    void validate_invalidFormat_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem").validate(buildItems()));
    }

    /**
     * Verifies that an unknown field name is rejected.
     */
    @Test
    void validate_unknownField_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem name = 'Apple'").validate(buildItems()));
    }

    /**
     * Verifies that an unknown operator is rejected.
     */
    @Test
    void validate_unknownOperator_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem quantity != 10").validate(buildItems()));
    }

    /**
     * Verifies that a description value without single quotes is rejected.
     */
    @Test
    void validate_descriptionValueWithoutQuotes_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem description = Apple").validate(buildItems()));
    }

    /**
     * Verifies that a non-integer quantity value is rejected.
     */
    @Test
    void validate_quantityValueNonInteger_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem quantity = abc").validate(buildItems()));
    }

    /**
     * Verifies that a negative quantity value is rejected.
     */
    @Test
    void validate_negativeQuantity_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem quantity = -5").validate(buildItems()));
    }

    /**
     * Verifies that a non-integer price value is rejected.
     */
    @Test
    void validate_priceValueNonInteger_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem price = abc").validate(buildItems()));
    }

    /**
     * Verifies that a negative price value is rejected.
     */
    @Test
    void validate_negativePrice_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator("filterItem price = -5").validate(buildItems()));
    }

    // ── Multi-predicate: valid inputs ─────────────────────────────────────────

    /**
     * Verifies that two predicates joined by AND passes validation.
     */
    @Test
    void validate_andCombination_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem description = 'Apple' AND quantity > 5")
                        .validate(buildItems()));
    }

    /**
     * Verifies that two predicates joined by OR passes validation.
     */
    @Test
    void validate_orCombination_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator("filterItem description = 'Apple' OR description = 'Banana'")
                        .validate(buildItems()));
    }

    /**
     * Verifies that three predicates with mixed AND/OR passes validation.
     */
    @Test
    void validate_mixedAndOrCombination_noException() {
        assertDoesNotThrow(() ->
                new FilterCommandValidator(
                        "filterItem quantity > 3 AND quantity < 15 OR description = 'Cherry'")
                        .validate(buildItems()));
    }

    // ── Multi-predicate: invalid inputs ───────────────────────────────────────

    /**
     * Verifies that a misspelled joining operator between predicates is rejected.
     */
    @Test
    void validate_invalidOperatorBetweenPredicates_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator(
                        "filterItem description = 'Apple' NAND quantity > 5").validate(buildItems()));
    }

    /**
     * Verifies that a negative quantity value inside a multi-predicate input is rejected.
     */
    @Test
    void validate_negativeQuantityInMultiPredicate_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilterCommandValidator(
                        "filterItem description = 'Apple' AND quantity > -1").validate(buildItems()));
    }
}
