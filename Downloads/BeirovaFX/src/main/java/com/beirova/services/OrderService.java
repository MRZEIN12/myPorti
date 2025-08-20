package com.beirova.services;

import com.beirova.models.*;
import java.util.*;

public class OrderService {
    private final DataStore store;
    public OrderService(DataStore store) { this.store = store; }

    public List<Order> getAll() { return store.loadOrders(); }
    public void saveAll(List<Order> list) { store.saveOrders(list); }

    public Order create(String userId, String customerName, List<OrderItem> items) {
        List<Order> orders = store.loadOrders();
        Order o = new Order();
        o.setUserId(userId);
        o.setCustomerName(customerName);
        o.setItems(items);
        orders.add(o);
        store.saveOrders(orders);
        return o;
    }
}
