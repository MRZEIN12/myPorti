package com.beirova.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private String id = UUID.randomUUID().toString();
    private String userId;
    private String customerName;
    private long createdAt = Instant.now().toEpochMilli();
    private List<OrderItem> items = new ArrayList<>();
    private String status = "Pending";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotal() { return items.stream().mapToDouble(OrderItem::getTotal).sum(); }
}
