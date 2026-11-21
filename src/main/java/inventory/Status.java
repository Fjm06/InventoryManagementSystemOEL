package inventory;

/**
 * Stock status of an inventory item.
 *
 * @author Fatima-Alisha-umama
 */
public enum Status {
    IN_STOCK("In Stock"),
    LOW_STOCK("Low Stock"),
    OUT_OF_STOCK("Out of Stock");

    /** Quantity at or below this value (but above zero) counts as low stock. */
    public static final int LOW_STOCK_THRESHOLD = 5;

    private final String label;

    /**
     * Creates a status with a display label.
     *
     * @param label the text shown to the user
     */
    Status(String label) {
        this.label = label;
    }

    /**
     * Works out the status automatically from a quantity.
     *
     * @param quantity the item quantity (non-negative)
     * @return OUT_OF_STOCK if 0, LOW_STOCK if 1..5, otherwise IN_STOCK
     */
    public static Status fromQuantity(int quantity) {
        if (quantity == 0) {
            return OUT_OF_STOCK;
        }
        return quantity <= LOW_STOCK_THRESHOLD ? LOW_STOCK : IN_STOCK;
    }

    /**
     * Returns the display label.
     *
     * @return human readable status text
     */
    @Override
    public String toString() {
        return label;
    }
}
