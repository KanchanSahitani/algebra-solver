package com.algebra.service;

import com.algebra.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionParserTest {

    private ExpressionParser parser;

    @BeforeEach
    void setUp() {
        parser = new ExpressionParser();
    }

    // ==================== Tokenizer Tests ====================

    @Nested
    @DisplayName("Tokenizer")
    class TokenizerTests {

        @Test
        @DisplayName("simple expression '3x + 2y - z' tokenizes with implicit multiplication")
        void simpleExpression() {
            List<String> tokens = parser.tokenize("3x + 2y - z");
            assertEquals(List.of("3", "*", "x", "+", "2", "*", "y", "-", "z"), tokens);
        }

        @Test
        @DisplayName("expression with exponent 'x^2 + y^2 - 4'")
        void exponentExpression() {
            List<String> tokens = parser.tokenize("x^2 + y^2 - 4");
            assertEquals(List.of("x", "^", "2", "+", "y", "^", "2", "-", "4"), tokens);
        }

        @Test
        @DisplayName("parenthesised expression '(x + 1) * (y - 2)'")
        void parenthesisedExpression() {
            List<String> tokens = parser.tokenize("(x + 1) * (y - 2)");
            assertEquals(List.of("(", "x", "+", "1", ")", "*", "(", "y", "-", "2", ")"), tokens);
        }

        @Test
        @DisplayName("unary minus at start '-x + 3'")
        void unaryMinusAtStart() {
            List<String> tokens = parser.tokenize("-x + 3");
            assertEquals(List.of("-1", "*", "x", "+", "3"), tokens);
        }

        @Test
        @DisplayName("decimal numbers '3.5x + 2.1'")
        void decimalNumbers() {
            List<String> tokens = parser.tokenize("3.5x + 2.1");
            assertEquals(List.of("3.5", "*", "x", "+", "2.1"), tokens);
        }
    }

    // ==================== Infix-to-Postfix Tests ====================

    @Nested
    @DisplayName("Infix to Postfix")
    class InfixToPostfixTests {

        @Test
        @DisplayName("simple addition 'a + b'")
        void simpleAddition() {
            List<String> tokens = List.of("a", "+", "b");
            List<String> postfix = parser.infixToPostfix(tokens);
            assertEquals(List.of("a", "b", "+"), postfix);
        }

        @Test
        @DisplayName("respects precedence 'a + b * c'")
        void precedence() {
            List<String> tokens = List.of("a", "+", "b", "*", "c");
            List<String> postfix = parser.infixToPostfix(tokens);
            assertEquals(List.of("a", "b", "c", "*", "+"), postfix);
        }

        @Test
        @DisplayName("parentheses override precedence '(a + b) * c'")
        void parenthesesOverride() {
            List<String> tokens = List.of("(", "a", "+", "b", ")", "*", "c");
            List<String> postfix = parser.infixToPostfix(tokens);
            assertEquals(List.of("a", "b", "+", "c", "*"), postfix);
        }

        @Test
        @DisplayName("exponent is right-associative 'a ^ b ^ c'")
        void rightAssociativeExponent() {
            List<String> tokens = List.of("a", "^", "b", "^", "c");
            List<String> postfix = parser.infixToPostfix(tokens);
            // right-assoc: a ^ (b ^ c) → a b c ^ ^
            assertEquals(List.of("a", "b", "c", "^", "^"), postfix);
        }
    }

    // ==================== Full Parse → Tree Tests ====================

    @Nested
    @DisplayName("Full Parse")
    class FullParseTests {

        @Test
        @DisplayName("parse '3x + 2y - z' builds a valid tree")
        void parseSimple() {
            TreeNode root = parser.parse("3x + 2y - z");
            assertNotNull(root);
            assertTrue(root.isOperator());
        }

        @Test
        @DisplayName("parse 'x^2 + y^2 - 4' builds a valid tree")
        void parseExponent() {
            TreeNode root = parser.parse("x^2 + y^2 - 4");
            assertNotNull(root);
        }

        @Test
        @DisplayName("null equation throws IllegalArgumentException")
        void nullEquation() {
            assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
        }

        @Test
        @DisplayName("blank equation throws IllegalArgumentException")
        void blankEquation() {
            assertThrows(IllegalArgumentException.class, () -> parser.parse("   "));
        }

        @Test
        @DisplayName("mismatched parentheses throws IllegalArgumentException")
        void mismatchedParens() {
            assertThrows(IllegalArgumentException.class, () -> parser.parse("(x + 1"));
            assertThrows(IllegalArgumentException.class, () -> parser.parse("x + 1)"));
        }
    }

    // ==================== Infix Reconstruction Tests ====================

    @Nested
    @DisplayName("Infix Reconstruction")
    class InfixReconstructionTests {

        @Test
        @DisplayName("reconstruct 'x + y' from tree")
        void reconstructSimple() {
            TreeNode root = parser.parse("x + y");
            String infix = parser.toInfix(root);
            assertEquals("x + y", infix);
        }

        @Test
        @DisplayName("reconstruct preserves operator precedence with parens")
        void reconstructPrecedence() {
            TreeNode root = parser.parse("(x + y) * z");
            String infix = parser.toInfix(root);
            assertEquals("(x + y) * z", infix);
        }

        @Test
        @DisplayName("null node returns empty string")
        void nullNode() {
            assertEquals("", parser.toInfix(null));
        }
    }
}
