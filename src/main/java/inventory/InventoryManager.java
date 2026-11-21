package inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Holds all items and implements add, view, update and delete with validation.
 *
 * @author Fatima-Alisha-Umama
 */
public class InventoryManager {
    private final List<Item> items = new ArrayList<>();
    private int nextId = 1;

    /**
     * Validates an item name.
     *
     * @param name the raw name text
     * @return the trimmed name
     * @throws IllegalArgumentException if the name is null or empty
     */
    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        return name.trim();
    }

    /**
     * Parses and validates a quantity.
     *
     * @param text the raw quantity text
     * @return the quantity as an int
     * @throws IllegalArgumentException if not a whole number or if negative
     */
    public static int parseQuantity(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Quantity cannot be empty.");
        }
        int q;
        try {
            q = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity must be a whole number.");
        }
        if (q < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        return q;
    }

    /**
     * Adds a new item. Status is set automatically from the quantity.
     *
     * @param name     item name
     * @param quantity quantity text
     * @return the newly created item
     * @throws IllegalArgumentException if input is invalid or name already exists
     */
    public Item addItem(String name, String quantity) {
        String n = validateName(name);
        int q = parseQuantity(quantity);
        if (findByName(n) != null) {
            throw new IllegalArgumentException("An item named '" + n + "' already exists.");
        }
        Item item = new Item(nextId++, n, q, Status.fromQuantity(q));
        items.add(item);
        return item;
    }

    /**
     * Returns all items.
     *
     * @return a copy of the item list
     */
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Checks whether the inventory has no items.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Finds an item by name (case-insensitive).
     *
     * @param name the name to look for
     * @return the item, or null if not found
     */
    public Item findByName(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Finds an item by id.
     *
     * @param id the item id
     * @return the item, or null if not found
     */
    public Item findById(int id) {
        for (Item i : items) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    /**
     * Updates an existing item.
     *
     * @param id       id of the item to update
     * @param name     new name
     * @param quantity new quantity text
     * @param status   new status, or null to set it automatically from quantity
     * @throws NoSuchElementException   if no item has this id
     * @throws IllegalArgumentException if the new input is invalid or the name is taken
     */
    public void updateItem(int id, String name, String quantity, Status status) {
        Item item = findById(id);
        if (item == null) {
            throw new NoSuchElementException("Item does not exist.");
        }
        String n = validateName(name);
        int q = parseQuantity(quantity);
        Item same = findByName(n);
        if (same != null && same.getId() != id) {
            throw new IllegalArgumentException("An item named '" + n + "' already exists.");
        }
        item.setName(n);
        item.setQuantity(q);
        item.setStatus(status == null ? Status.fromQuantity(q) : status);
    }

    /**
     * Deletes an item.
     *
     * @param id id of the item to delete
     * @throws NoSuchElementException if no item has this id
     */
    public void deleteItem(int id) {
        Item item = findById(id);
        if (item == null) {
            throw new NoSuchElementException("Item does not exist.");
        }
        items.remove(item);
    }
}
