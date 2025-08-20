package com.beirova.ui;

import com.beirova.models.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class CartDialog extends Stage {
    private final List<OrderItem> cart;
    private final Label total = new Label("Total: $0.00");
    private final TableView<OrderItem> table = new TableView<>();

    public CartDialog(List<OrderItem> cart) {
        this.cart = cart;
        setTitle("Cart");
        initModality(Modality.APPLICATION_MODAL);

        TableColumn<OrderItem, String> c1 = new TableColumn<>("Product"); c1.setCellValueFactory(new PropertyValueFactory<>("productName"));
        TableColumn<OrderItem, Double> c2 = new TableColumn<>("Unit Price"); c2.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        TableColumn<OrderItem, Integer> c3 = new TableColumn<>("Qty"); c3.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<OrderItem, Double> c4 = new TableColumn<>("Total"); c4.setCellValueFactory(new PropertyValueFactory<>("total"));
        table.getColumns().addAll(c1,c2,c3,c4);

        Button remove = new Button("Remove selected");
        remove.setOnAction(e -> {
            OrderItem sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) { cart.remove(sel); reload(); }
        });

        Button checkout = new Button("Checkout");
        checkout.setOnAction(e -> { setUserData(Boolean.TRUE); close(); });

        HBox bottom = new HBox(10, remove, new Label(), total, checkout);
        HBox.setHgrow(bottom.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);
        bottom.setPadding(new Insets(10));

        BorderPane root = new BorderPane(table, null, null, bottom, null);
        setScene(new Scene(root, 600, 360));
        reload();
    }

    private void reload() {
        ObservableList<OrderItem> data = FXCollections.observableArrayList(cart);
        table.setItems(data);
        double sum = cart.stream().mapToDouble(OrderItem::getTotal).sum();
        total.setText(String.format("Total: $%.2f", sum));
    }

    public boolean showDialog() {
        showAndWait();
        Object v = getUserData();
        return v instanceof Boolean && (Boolean)v;
    }
}
