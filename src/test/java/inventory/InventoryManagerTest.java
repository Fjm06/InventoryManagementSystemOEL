package inventory;

import java.util.NoSuchElementException;

/**
 * Console test runner covering the test cases listed in the OEL document.
 * Run its main method and copy the PASS/FAIL output into the report.
 *
 * @author Your Name
 */
public class InventoryManagerTest {
    private static int passed = 0;
    private static int failed = 0;

    /**
     * Runs all test cases.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        InventoryManager m = new InventoryManager();

        check("View an empty inventory", m.isEmpty() && m.getAllItems().isEmpty());

        Item a = m.addItem("Rice", "20");
        check("Add a valid item", m.getAllItems().size() == 1 && a.getQuantity() == 20
                && a.getStatus() == Status.IN_STOCK);

        check("Add item with empty name", throwsIllegalArgument(() -> m.addItem("   ", "5")));
        check("Add item with negative quantity", throwsIllegalArgument(() -> m.addItem("Sugar", "-3")));
        check("Add item with non-numeric quantity", throwsIllegalArgument(() -> m.addItem("Sugar", "abc")));
        check("Add item with empty quantity", throwsIllegalArgument(() -> m.addItem("Sugar", "")));
        check("Add duplicate name", throwsIllegalArgument(() -> m.addItem("rice", "4")));

        m.addItem("Milk", "3");
        check("View all items", m.getAllItems().size() == 2);
        check("Low stock status", m.findByName("Milk").getStatus() == Status.LOW_STOCK);

        m.updateItem(a.getId(), "Basmati Rice", "0", null);
        check("Update an existing item", a.getName().equals("Basmati Rice") && a.getQuantity() == 0
                && a.getStatus() == Status.OUT_OF_STOCK);

        m.updateItem(a.getId(), "Basmati Rice", "50", Status.LOW_STOCK);
        check("Update with manual status", a.getStatus() == Status.LOW_STOCK);

        boolean handled = false;
        try {
            m.updateItem(999, "X", "1", null);
        } catch (NoSuchElementException e) {
            handled = true;
        }
        check("Update a non-existing item", handled);

        m.deleteItem(a.getId());
        check("Delete an existing item", m.findById(a.getId()) == null && m.getAllItems().size() == 1);

        handled = false;
        try {
            m.deleteItem(999);
        } catch (NoSuchElementException e) {
            handled = true;
        }
        check("Delete a non-existing item", handled);

        System.out.println("\nPassed: " + passed + "  Failed: " + failed);
    }

    private static boolean throwsIllegalArgument(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (ok) {
            passed++;
        } else {
            failed++;
        }
    }
}
