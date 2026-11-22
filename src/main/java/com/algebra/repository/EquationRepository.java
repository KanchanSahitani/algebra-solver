package com.algebra.repository;

import com.algebra.model.Equation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for storing {@link Equation} objects.
 * Thread-safe via {@link ConcurrentHashMap}.
 */
@Repository
public class EquationRepository {

    private final Map<String, Equation> store = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    /**
     * Save a new equation and assign it an auto-incremented id.
     */
    public Equation save(Equation equation) {
        String id = String.valueOf(idCounter.incrementAndGet());
        equation.setId(id);
        store.put(id, equation);
        return equation;
    }

    /**
     * Find an equation by id.
     */
    public Optional<Equation> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Return all stored equations ordered by id.
     */
    public List<Equation> findAll() {
        return store.entrySet().stream()
                .sorted(Comparator.comparingLong(e -> Long.parseLong(e.getKey())))
                .map(Map.Entry::getValue)
                .toList();
    }

    /**
     * Remove all equations (useful for testing).
     */
    public void clear() {
        store.clear();
        idCounter.set(0);
    }
}
