package com.beirova.models;

import java.util.UUID;

public class Product {
    private String id = UUID.randomUUID().toString();
    private String name = "";
    private String sku = "";
    private double price;
    private int stock;
    private String imagePath; // classpath resource path (e.g., assets/logo.png)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
