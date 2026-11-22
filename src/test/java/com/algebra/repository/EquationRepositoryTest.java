package com.algebra.repository;

import com.algebra.model.Equation;
import com.algebra.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EquationRepositoryTest {

    private EquationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EquationRepository();
    }

    @Test
    @DisplayName("save assigns an auto-incremented id")
    void save() {
        Equation eq = new Equation(null, "x + y", new TreeNode("dummy", false));
        Equation saved = repository.save(eq);
        assertEquals("1", saved.getId());

        Equation eq2 = new Equation(null, "a + b", new TreeNode("dummy", false));
        Equation saved2 = repository.save(eq2);
        assertEquals("2", saved2.getId());
    }

    @Test
    @DisplayName("findById returns present for existing, empty for missing")
    void findById() {
        Equation eq = new Equation(null, "x + y", new TreeNode("dummy", false));
        repository.save(eq);

        assertTrue(repository.findById("1").isPresent());
        assertFalse(repository.findById("99").isPresent());
    }

    @Test
    @DisplayName("findAll returns equations ordered by id")
    void findAll() {
        repository.save(new Equation(null, "a", new TreeNode("a", false)));
        repository.save(new Equation(null, "b", new TreeNode("b", false)));
        repository.save(new Equation(null, "c", new TreeNode("c", false)));

        List<Equation> all = repository.findAll();
        assertEquals(3, all.size());
        assertEquals("1", all.get(0).getId());
        assertEquals("2", all.get(1).getId());
        assertEquals("3", all.get(2).getId());
    }

    @Test
    @DisplayName("clear removes all equations and resets counter")
    void clear() {
        repository.save(new Equation(null, "x", new TreeNode("x", false)));
        repository.clear();
        assertTrue(repository.findAll().isEmpty());

        Equation eq = repository.save(new Equation(null, "y", new TreeNode("y", false)));
        assertEquals("1", eq.getId());
    }
}
