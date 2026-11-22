package com.algebra.service;

import com.algebra.model.Equation;
import com.algebra.repository.EquationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EquationServiceTest {

    private EquationService service;
    private EquationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new EquationRepository();
        ExpressionParser parser = new ExpressionParser();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        service = new EquationService(repository, parser, evaluator);
    }

    @Test
    @DisplayName("store an equation and retrieve by id")
    void storeAndRetrieve() {
        Equation eq = service.store("3x + 2y - z");
        assertNotNull(eq.getId());
        assertEquals("3x + 2y - z", eq.getOriginalEquation());
        assertNotNull(eq.getRoot());

        Optional<Equation> found = service.getById(eq.getId());
        assertTrue(found.isPresent());
        assertEquals(eq.getId(), found.get().getId());
    }

    @Test
    @DisplayName("getAll returns all stored equations")
    void getAll() {
        service.store("x + y");
        service.store("a * b");
        List<Equation> all = service.getAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("evaluate a stored equation")
    void evaluate() {
        Equation eq = service.store("3x + 2y - z");
        double result = service.evaluate(eq.getId(), Map.of("x", 2.0, "y", 3.0, "z", 1.0));
        assertEquals(11.0, result);
    }

    @Test
    @DisplayName("evaluate with non-existent id throws")
    void evaluateNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> service.evaluate("999", Map.of("x", 1.0)));
    }

    @Test
    @DisplayName("store null equation throws")
    void storeNull() {
        assertThrows(IllegalArgumentException.class, () -> service.store(null));
    }

    @Test
    @DisplayName("store blank equation throws")
    void storeBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.store("   "));
    }

    @Test
    @DisplayName("toInfix reconstructs the expression")
    void toInfix() {
        Equation eq = service.store("x + y");
        String infix = service.toInfix(eq);
        assertEquals("x + y", infix);
    }
}
