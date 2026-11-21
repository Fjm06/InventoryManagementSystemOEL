package inventory;

/**
 * Represents a single product in the store inventory.
 *
 * @author Fatima-Alisha-umama
 */
public class Item {
    private final int id;
    private String name;
    private int quantity;
    private Status status;

    /**
     * Creates an item.
     *
     * @param id       unique identifier
     * @param name     item name (must not be empty)
     * @param quantity stock quantity (must be non-negative)
     * @param status   stock status
     */
    public Item(int id, String name, int quantity, Status status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
    }

    /**
     * Gets the unique id.
     *
     * @return the item id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the item name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the item name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the quantity in stock.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity in stock.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the stock status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the stock status.
     *
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
