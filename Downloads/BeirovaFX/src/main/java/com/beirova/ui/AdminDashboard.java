package com.beirova.ui;

import com.beirova.models.*;
import com.beirova.services.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDashboard {
    protected final BorderPane root = new BorderPane();
    protected final DataStore store;
    protected final ProductService products;
    protected final OrderService orders;
    protected final User user;

    protected final TableView<Product> tableProducts = new TableView<>();
    protected final TableView<Order> tableOrders = new TableView<>();

    public AdminDashboard(DataStore store, User user) {
        this.store = store; this.products = new ProductService(store); this.orders = new OrderService(store); this.user = user;

        TabPane tabs = new TabPane();
        Tab tabProducts = new Tab("Products");
        Tab tabOrders = new Tab("Orders");
        tabs.getTabs().addAll(tabProducts, tabOrders);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // products UI
        TableColumn<Product, String> p1 = new TableColumn<>("Name"); p1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> p2 = new TableColumn<>("SKU"); p2.setCellValueFactory(new PropertyValueFactory<>("sku"));
        TableColumn<Product, Double> p3 = new TableColumn<>("Price"); p3.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> p4 = new TableColumn<>("Stock"); p4.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableProducts.getColumns().addAll(p1,p2,p3,p4);

        Button btnAdd = new Button("Add");
        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");
        btnAdd.setOnAction(e -> {
            ProductEditorDialog dlg = new ProductEditorDialog(null);
            if (dlg.showDialog()) { products.addOrUpdate(dlg.getProduct()); reloadProducts(); }
        });
        btnEdit.setOnAction(e -> {
            Product p = tableProducts.getSelectionModel().getSelectedItem();
            if (p != null) {
                ProductEditorDialog dlg = new ProductEditorDialog(p);
                if (dlg.showDialog()) { products.addOrUpdate(dlg.getProduct()); reloadProducts(); }
            }
        });
        btnDelete.setOnAction(e -> {
            Product p = tableProducts.getSelectionModel().getSelectedItem();
            if (p != null) { products.deleteById(p.getId()); reloadProducts(); }
        });

        HBox prodBar = new HBox(10, btnAdd, btnEdit, btnDelete);
        prodBar.setPadding(new Insets(8));

        BorderPane prodPane = new BorderPane(tableProducts, prodBar, null, null, null);
        tabProducts.setContent(prodPane);

        // orders UI
        TableColumn<Order, String> o1 = new TableColumn<>("Order Id"); o1.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId().substring(0,8)));
        TableColumn<Order, String> o2 = new TableColumn<>("Customer"); o2.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        TableColumn<Order, String> o3 = new TableColumn<>("Total"); o3.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", c.getValue().getTotal())));
        TableColumn<Order, String> o4 = new TableColumn<>("Status"); o4.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableOrders.getColumns().addAll(o1,o2,o3,o4);
        tabOrders.setContent(tableOrders);

        root.setCenter(tabs);
        reloadProducts();
        reloadOrders();
    }

    protected void reloadProducts() {
        ObservableList<Product> data = FXCollections.observableArrayList(products.getAll());
        tableProducts.setItems(data);
    }

    protected void reloadOrders() {
        ObservableList<Order> data = FXCollections.observableArrayList(orders.getAll());
        tableOrders.setItems(data);
    }

    public BorderPane getRoot() { return root; }
}
