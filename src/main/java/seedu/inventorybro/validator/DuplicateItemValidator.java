package seedu.inventorybro.validator;

import seedu.inventorybro.ItemList;

/**
 * Validates that an item name does not already exist in the inventory.
 * Name comparison is case-insensitive so that "Apple" and "apple" are treated as the same item.
 */
public class DuplicateItemValidator implements Validator {
    private final String name;

    /**
     * Creates a validator that checks whether {@code name} already exists in the inventory.
     *
     * @param name The item name to check (should already be trimmed by the caller).
     */
    public DuplicateItemValidator(String name) {
        this.name = name;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the given name already exists in the item list.
     *
     * @param items The current inventory.
     * @throws IllegalArgumentException if the name is already taken (case-insensitive).
     */
    @Override
    public void validate(ItemList items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.getItem(i).getDescription().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException(
                        "An item named '" + name + "' already exists in the inventory."
                );
            }
        }
    }
}
