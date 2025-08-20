package com.beirova.services;

import com.beirova.models.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class AuthService {
    private final DataStore store;
    public AuthService(DataStore store) { this.store = store; }

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02X", x));
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }

    public User login(String email, String password) {
        return store.loadUsers().stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPasswordHash().equals(hash(password)))
            .findFirst().orElse(null);
    }

    public boolean registerCustomer(String fullName, String email, String password, StringBuilder error) {
        List<User> users = store.loadUsers();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                error.append("Email already registered.");
                return false;
            }
        }
        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPasswordHash(hash(password));
        u.setRole(Role.Customer);
        users.add(u);
        store.saveUsers(users);
        return true;
    }

    public List<User> getUsers() { return store.loadUsers(); }
    public void saveUsers(List<User> users) { store.saveUsers(users); }
}
