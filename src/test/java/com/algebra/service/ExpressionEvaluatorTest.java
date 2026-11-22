package com.algebra.service;

import com.algebra.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

    private ExpressionEvaluator evaluator;
    private ExpressionParser parser;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
        parser = new ExpressionParser();
    }

    @Test
    @DisplayName("evaluate '3x + 2y - z' with x=2, y=3, z=1 → 11")
    void evaluateSimple() {
        TreeNode root = parser.parse("3x + 2y - z");
        Map<String, Double> vars = Map.of("x", 2.0, "y", 3.0, "z", 1.0);
        // 3*2 + 2*3 - 1 = 6 + 6 - 1 = 11
        assertEquals(11.0, evaluator.evaluate(root, vars));
    }

    @Test
    @DisplayName("evaluate 'x^2 + y^2 - 4' with x=1, y=2 → 1")
    void evaluateExponent() {
        TreeNode root = parser.parse("x^2 + y^2 - 4");
        Map<String, Double> vars = Map.of("x", 1.0, "y", 2.0);
        // 1 + 4 - 4 = 1
        assertEquals(1.0, evaluator.evaluate(root, vars));
    }

    @Test
    @DisplayName("evaluate constant '42' → 42")
    void evaluateConstant() {
        TreeNode root = parser.parse("42");
        assertEquals(42.0, evaluator.evaluate(root, Map.of()));
    }

    @Test
    @DisplayName("evaluate with parentheses '(x + 1) * (y - 2)' x=3, y=5 → 12")
    void evaluateParentheses() {
        TreeNode root = parser.parse("(x + 1) * (y - 2)");
        Map<String, Double> vars = Map.of("x", 3.0, "y", 5.0);
        // (3+1)*(5-2) = 4*3 = 12
        assertEquals(12.0, evaluator.evaluate(root, vars));
    }

    @Test
    @DisplayName("missing variable throws IllegalArgumentException")
    void missingVariable() {
        TreeNode root = parser.parse("x + y");
        Map<String, Double> vars = Map.of("x", 1.0);
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluate(root, vars));
    }

    @Test
    @DisplayName("division by zero throws ArithmeticException")
    void divisionByZero() {
        TreeNode root = parser.parse("x / y");
        Map<String, Double> vars = Map.of("x", 1.0, "y", 0.0);
        assertThrows(ArithmeticException.class,
                () -> evaluator.evaluate(root, vars));
    }

    @Test
    @DisplayName("null node throws IllegalArgumentException")
    void nullNode() {
        assertThrows(IllegalArgumentException.class,
                () -> evaluator.evaluate(null, Map.of()));
    }

    @Test
    @DisplayName("evaluate with decimal coefficients '3.5x + 2.1' x=2 → 9.1")
    void decimalCoefficients() {
        TreeNode root = parser.parse("3.5x + 2.1");
        Map<String, Double> vars = Map.of("x", 2.0);
        assertEquals(9.1, evaluator.evaluate(root, vars), 0.0001);
    }

    @Test
    @DisplayName("evaluate nested exponent 'x ^ 2 ^ 3' with x=2 → 256 (right-associative)")
    void nestedExponent() {
        TreeNode root = parser.parse("x ^ 2 ^ 3");
        Map<String, Double> vars = Map.of("x", 2.0);
        // right-assoc: 2^(2^3) = 2^8 = 256
        assertEquals(256.0, evaluator.evaluate(root, vars));
    }

    @Test
    @DisplayName("evaluate unary minus '-x + 3' with x=5 → -2")
    void unaryMinus() {
        TreeNode root = parser.parse("-x + 3");
        Map<String, Double> vars = Map.of("x", 5.0);
        // -1*5 + 3 = -2
        assertEquals(-2.0, evaluator.evaluate(root, vars));
    }
}
