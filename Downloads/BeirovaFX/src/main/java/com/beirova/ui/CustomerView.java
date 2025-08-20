package com.beirova.ui;

import com.beirova.models.*;
import com.beirova.services.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CustomerView {
    private final BorderPane root = new BorderPane();
    private final DataStore store;
    private final ProductService products;
    private final OrderService orders;
    private final User user;

    private final TableView<Product> table = new TableView<>();
    private final List<OrderItem> cart = new ArrayList<>();

    public CustomerView(DataStore store, User user) {
        this.store = store;
        this.products = new ProductService(store);
        this.orders = new OrderService(store);
        this.user = user;

        Label title = Controls.title("BEÏROVA STORE");
        Button btnCart = new Button("View Cart / Checkout");
        HBox top = new HBox(10, title, new Pane(), btnCart);
        HBox.setHgrow(top.getChildren().get(1), Priority.ALWAYS);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10));

        TableColumn<Product, String> c1 = new TableColumn<>("Name"); c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, String> c2 = new TableColumn<>("SKU"); c2.setCellValueFactory(new PropertyValueFactory<>("sku"));
        TableColumn<Product, Double> c3 = new TableColumn<>("Price"); c3.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> c4 = new TableColumn<>("Stock"); c4.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Product, Void> c5 = new TableColumn<>("Add");
        c5.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Add to Cart");
            {
                btn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    addToCart(p);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(c1,c2,c3,c4,c5);
        reloadProducts();

        btnCart.setOnAction(e -> {
            CartDialog dlg = new CartDialog(cart);
            dlg.initOwner((Stage) root.getScene().getWindow());
            boolean checkedOut = dlg.showDialog();
            if (checkedOut) {
                if (cart.isEmpty()) { new Alert(Alert.AlertType.INFORMATION, "Cart is empty.").showAndWait(); return; }
                Order order = orders.create(user.getId(), user.getFullName(), new ArrayList<>(cart));
                // reduce stock
                List<Product> list = products.getAll();
                for (OrderItem i : cart) {
                    for (Product p : list) if (p.getId().equals(i.getProductId())) { p.setStock(p.getStock() - i.getQuantity()); break; }
                }
                products.saveAll(list);
                cart.clear();
                reloadProducts();
                new Alert(Alert.AlertType.INFORMATION, "Order #" + order.getId().substring(0,8) + " created. Thank you!").showAndWait();
            }
        });

        root.setTop(top);
        root.setCenter(table);
    }

    private void reloadProducts() {
        ObservableList<Product> data = FXCollections.observableArrayList(products.getAll());
        table.setItems(data);
    }

    private void addToCart(Product p) {
        if (p.getStock() <= 0) { new Alert(Alert.AlertType.INFORMATION, "Out of stock.").showAndWait(); return; }
        OrderItem existing = null;
        for (OrderItem i : cart) if (i.getProductId().equals(p.getId())) { existing = i; break; }
        if (existing == null) {
            OrderItem it = new OrderItem();
            it.setProductId(p.getId());
            it.setProductName(p.getName());
            it.setUnitPrice(p.getPrice());
            it.setQuantity(1);
            cart.add(it);
        } else {
            existing.setQuantity(existing.getQuantity()+1);
        }
        new Alert(Alert.AlertType.INFORMATION, p.getName() + " added to cart.").showAndWait();
    }

    public BorderPane getRoot() { return root; }
}
