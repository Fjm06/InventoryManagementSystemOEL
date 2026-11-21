package inventory;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * JavaFX user interface for the Inventory Management System.
 *
 * @author Fatima-Alisha-umama
 */
public class InventoryApp extends Application {
    private static final String AUTO = "Auto (from quantity)";

    private final InventoryManager manager = new InventoryManager();
    private final TableView<Item> table = new TableView<>();
    private final TextField nameField = new TextField();
    private final TextField quantityField = new TextField();
    private final ComboBox<Object> statusBox = new ComboBox<>();
    private final Label messageLabel = new Label("Inventory is empty.");

    /**
     * Builds and shows the main window.
     *
     * @param stage the primary stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        nameField.setPromptText("Item name");
        quantityField.setPromptText("Quantity");
        statusBox.getItems().add(AUTO);
        statusBox.getItems().addAll((Object[]) Status.values());
        statusBox.setValue(AUTO);

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button viewBtn = new Button("View All");
        Button clearBtn = new Button("Clear");
        addBtn.setOnAction(e -> addItem());
        updateBtn.setOnAction(e -> updateItem());
        deleteBtn.setOnAction(e -> deleteItem());
        viewBtn.setOnAction(e -> viewAll());
        clearBtn.setOnAction(e -> clearForm());

        HBox form = new HBox(8, new Label("Name:"), nameField,
                new Label("Quantity:"), quantityField,
                new Label("Status:"), statusBox);
        HBox buttons = new HBox(8, addBtn, updateBtn, deleteBtn, viewBtn, clearBtn);

        TableColumn<Item, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);
        TableColumn<Item, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Item, Status> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(150);
        table.getColumns().addAll(idCol, nameCol, qtyCol, statusCol);
        table.setPlaceholder(new Label("Inventory is empty."));
        table.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> loadSelected(selected));

        VBox root = new VBox(10, form, buttons, table, messageLabel);
        root.setPadding(new Insets(12));
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root, 800, 480));
        stage.show();
    }

    /** Handles the Add button. */
    private void addItem() {
        try {
            manager.addItem(nameField.getText(), quantityField.getText());
            refreshTable();
            clearForm();
            messageLabel.setText("Item added successfully.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /** Handles the Update button. */
    private void updateItem() {
        Item selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select an item from the table to update.");
            return;
        }
        try {
            Object sel = statusBox.getValue();
            Status status = (sel instanceof Status) ? (Status) sel : null;
            manager.updateItem(selected.getId(), nameField.getText(), quantityField.getText(), status);
            refreshTable();
            clearForm();
            messageLabel.setText("Item updated successfully.");
        } catch (NoSuchElementException ex) {
            showError(ex.getMessage());
            refreshTable();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    /** Handles the Delete button. */
    private void deleteItem() {
        Item selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select an item from the table to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete '" + selected.getName() + "'?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                manager.deleteItem(selected.getId());
                messageLabel.setText("Item deleted.");
            } catch (NoSuchElementException ex) {
                showError(ex.getMessage());
            }
            refreshTable();
            clearForm();
        }
    }

    /** Handles the View All button. */
    private void viewAll() {
        refreshTable();
        if (manager.isEmpty()) {
            Alert info = new Alert(Alert.AlertType.INFORMATION, "The inventory is empty.");
            info.setHeaderText(null);
            info.showAndWait();
        }
    }

    /** Reloads the table from the manager. */
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(manager.getAllItems()));
        table.refresh();
        messageLabel.setText(manager.isEmpty()
                ? "Inventory is empty." : table.getItems().size() + " item(s) in inventory.");
    }

    /**
     * Copies the selected row into the input fields.
     *
     * @param item the selected item, or null if nothing is selected
     */
    private void loadSelected(Item item) {
        if (item == null) {
            return;
        }
        nameField.setText(item.getName());
        quantityField.setText(String.valueOf(item.getQuantity()));
        statusBox.setValue(AUTO);
    }

    /** Clears the input fields and table selection. */
    private void clearForm() {
        nameField.clear();
        quantityField.clear();
        statusBox.setValue(AUTO);
        table.getSelectionModel().clearSelection();
    }

    /**
     * Shows an error dialog.
     *
     * @param msg the message to display
     */
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}