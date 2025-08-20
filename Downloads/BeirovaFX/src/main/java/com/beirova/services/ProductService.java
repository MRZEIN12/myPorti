package com.beirova.services;

import com.beirova.models.Product;
import java.util.*;

public class ProductService {
    private final DataStore store;
    public ProductService(DataStore store) { this.store = store; }

    public List<Product> getAll() { return store.loadProducts(); }
    public void saveAll(List<Product> list) { store.saveProducts(list); }

    public void addOrUpdate(Product p) {
        List<Product> list = store.loadProducts();
        int idx = -1;
        for (int i=0;i<list.size();i++) if (list.get(i).getId().equals(p.getId())) { idx = i; break; }
        if (idx < 0) list.add(p); else list.set(idx, p);
        store.saveProducts(list);
    }

    public void deleteById(String id) {
        List<Product> list = store.loadProducts();
        list.removeIf(x -> Objects.equals(x.getId(), id));
        store.saveProducts(list);
    }
}
