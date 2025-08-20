package com.beirova.services;

import com.beirova.models.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

public class DataStore {
    private final Path root;
    private final ObjectMapper mapper;

    private Path usersPath() { return root.resolve("users.json"); }
    private Path productsPath() { return root.resolve("products.json"); }
    private Path ordersPath() { return root.resolve("orders.json"); }

    public DataStore() {
        this(Paths.get(System.getProperty("user.dir"), "data"));
    }

    public DataStore(Path root) {
        this.root = root;
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try { Files.createDirectories(root); } catch (IOException ignored) {}
        ensureSeed();
    }

    private <T> T read(Path p, TypeReference<T> type, T def) {
        try {
            if (!Files.exists(p)) return def;
            return mapper.readValue(Files.readAllBytes(p), type);
        } catch (Exception e) {
            return def;
        }
    }

    private <T> void write(Path p, T value) {
        try {
            mapper.writeValue(p.toFile(), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureSeed() {
        if (!Files.exists(usersPath())) {
            List<User> users = new ArrayList<>();
            users.add(seedUser("Super Admin", "super@beirova.local", "Password123!", Role.SuperAdmin));
            users.add(seedUser("Admin", "admin@beirova.local", "Admin123!", Role.Admin));
            users.add(seedUser("Customer", "customer@beirova.local", "Customer123!", Role.Customer));
            write(usersPath(), users);
        }
        if (!Files.exists(productsPath())) {
            List<Product> products = new ArrayList<>();
            products.add(seedProduct("Beirova Hoodie","BRV-HOOD-001",59.99,25,"assets/logo.png"));
            products.add(seedProduct("Beirova Tee","BRV-TEE-001",24.99,50,"assets/logo.png"));
            products.add(seedProduct("Beirova Cap","BRV-CAP-001",19.99,40,"assets/logo.png"));
            write(productsPath(), products);
        }
        if (!Files.exists(ordersPath())) {
            write(ordersPath(), new ArrayList<Order>());
        }
    }

    private static User seedUser(String name, String email, String pwd, Role role) {
        User u = new User();
        u.setFullName(name); u.setEmail(email);
        u.setPasswordHash(AuthService.hash(pwd));
        u.setRole(role);
        return u;
    }

    private static Product seedProduct(String name, String sku, double price, int stock, String image) {
        Product p = new Product();
        p.setName(name); p.setSku(sku); p.setPrice(price); p.setStock(stock); p.setImagePath(image);
        return p;
    }

    public List<User> loadUsers() {
        return read(usersPath(), new TypeReference<List<User>>() {}, new ArrayList<>());
    }
    public void saveUsers(List<User> users) { write(usersPath(), users); }

    public List<Product> loadProducts() {
        return read(productsPath(), new TypeReference<List<Product>>() {}, new ArrayList<>());
    }
    public void saveProducts(List<Product> products) { write(productsPath(), products); }

    public List<Order> loadOrders() {
        return read(ordersPath(), new TypeReference<List<Order>>() {}, new ArrayList<>());
    }
    public void saveOrders(List<Order> orders) { write(ordersPath(), orders); }
}
