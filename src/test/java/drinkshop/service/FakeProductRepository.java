package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeProductRepository implements Repository<Integer, Product> {

    // Simulates a database in memory
    public final Map<Integer, Product> database = new HashMap<>();

    @Override
    public Product findOne(Integer id) {
        return database.get(id);
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Product save(Product entity) {
        database.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Product delete(Integer id) {
        return database.remove(id);
    }

    @Override
    public Product update(Product entity) {
        if (database.containsKey(entity.getId())) {
            database.put(entity.getId(), entity);
            return entity;
        }
        return null; // Or throw an exception, depending on your actual repo logic
    }
}