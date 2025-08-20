package com.beirova.ui;

import com.beirova.models.Product;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductEditorDialog extends Stage {
    private final Product product;
    public Product getProduct() { return product; }

    public ProductEditorDialog(Product p) {
        this.product = (p == null ? new Product() : p);
        setTitle("Product Editor");
        initModality(Modality.APPLICATION_MODAL);

        TextField name = new TextField(product.getName()); name.setPromptText("Name");
        TextField sku = new TextField(product.getSku()); sku.setPromptText("SKU");
        Spinner<Double> price = new Spinner<>(0.0, 1_000_000.0, product.getPrice(), 0.5);
        Spinner<Integer> stock = new Spinner<>(0, 100_000, product.getStock());
        TextField image = new TextField(product.getImagePath() == null ? "" : product.getImagePath()); image.setPromptText("Image path (e.g., assets/logo.png)");
        Button save = new Button("Save");

        save.setOnAction(e -> {
            product.setName(name.getText().trim());
            product.setSku(sku.getText().trim());
            product.setPrice(price.getValue());
            product.setStock(stock.getValue());
            product.setImagePath(image.getText().isBlank() ? null : image.getText().trim());
            setUserData(Boolean.TRUE);
            close();
        });

        VBox root = new VBox(10,
            new Label("Name"), name,
            new Label("SKU"), sku,
            new Label("Price"), price,
            new Label("Stock"), stock,
            new Label("Image path (classpath)"), image,
            save
        );
        root.setPadding(new Insets(12));
        setScene(new Scene(root, 380, 340));
    }

    public boolean showDialog() {
        showAndWait();
        Object v = getUserData();
        return v instanceof Boolean && (Boolean)v;
    }
}
