package inventory;

import javafx.application.Application;

/**
 * Entry point. A separate launcher class avoids the "JavaFX runtime components
 * are missing" error when running from an IDE without a module path.
 *
 * @author Fatima-Alisha-umama
 */
public class Main {
    /**
     * Starts the JavaFX application.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Application.launch(InventoryApp.class, args);
    }
}
