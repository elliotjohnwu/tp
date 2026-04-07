package seedu.inventorybro;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Manages the list of items, providing operations to add and delete items.
 */
public class ItemList {
    private final ArrayList<Item> items;

    /**
     * Creates an empty Item list.
     */
    public ItemList() {
        this.items = new ArrayList<>();
    }

    /**
     * Creates an item list pre-populated with the given items.
     *
     * @param items An existing list of items to manage.
     */
    public ItemList(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Adds an item to the list.
     *
     * @param item The item to add.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes and returns the item at the given index.
     *
     * @param index Zero-based index of the item to delete.
     * @return The removed item.
     */
    public Item deleteItem(int index) {
        assert index >= 0 && index < items.size() : "Index out of bounds: " + index;
        return items.remove(index);
    }


    /**
     * Returns the item at the given index.
     *
     * @param index Zero-based index of the item.
     * @return The item at that index.
     */
    public Item getItem(int index) {
        assert index >= 0 && index < items.size() : "Index out of bounds: " + index;
        return items.get(index);
    }

    /**
     * Returns the number of items in the list.
     *
     * @return The item count.
     */
    public int size() {
        return items.size();
    }

    /**
     * Returns true if there are no items in the list.
     *
     * @return True if empty, false otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ArrayList<Item> getSortedItems(String field, String order) {
        ArrayList<Item> sortedItems = null;
        switch (field) {
        case "quantity":
            if (order.equals("high")) {
                sortedItems = items.stream()
                        .sorted(Comparator.comparing(Item::getQuantity).reversed())
                        .collect(Collectors.toCollection(ArrayList::new));
            } else if (order.equals("low")) {
                sortedItems = items.stream()
                        .sorted(Comparator.comparing(Item::getQuantity))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            break;
        case "price":
            if (order.equals("high")) {
                sortedItems = items.stream()
                        .sorted(Comparator.comparing(Item::getPrice).reversed())
                        .collect(Collectors.toCollection(ArrayList::new));
            } else if (order.equals("low")) {
                sortedItems = items.stream()
                        .sorted(Comparator.comparing(Item::getPrice))
                        .collect(Collectors.toCollection(ArrayList::new));
            }
            break;
        default:
            break;
        }
        return sortedItems;
    }

    /**
     * *Returns the underlying ArrayList of items.
     *
     * @return The item list.
     */
    public ArrayList<Item> getItems() {
        return items;
    }
}
