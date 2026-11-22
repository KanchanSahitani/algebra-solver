package com.algebra.service;

import com.algebra.model.TreeNode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Evaluates an expression tree by substituting variable values.
 */
@Component
public class ExpressionEvaluator {

    /**
     * Evaluate the expression tree with the given variable bindings.
     *
     * @param node      root of the expression tree
     * @param variables map of variable-name → numeric value
     * @return the computed result
     * @throws IllegalArgumentException if a required variable is missing
     */
    public double evaluate(TreeNode node, Map<String, Double> variables) {
        if (node == null) {
            throw new IllegalArgumentException("Cannot evaluate a null node");
        }

        if (!node.isOperator()) {
            return evaluateOperand(node.getValue(), variables);
        }

        double left = evaluate(node.getLeft(), variables);
        double right = evaluate(node.getRight(), variables);

        return applyOperator(node.getValue(), left, right);
    }

    // ---------- helpers ----------

    private double evaluateOperand(String token, Map<String, Double> variables) {
        // Try parsing as a plain number first
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException ignored) {
            // Not a plain number — must be a variable
        }

        // Look up in variables map
        if (variables != null && variables.containsKey(token)) {
            return variables.get(token);
        }

        throw new IllegalArgumentException("Missing value for variable: '" + token + "'");
    }

    private double applyOperator(String op, double left, double right) {
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> {
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield left / right;
            }
            case "^" -> Math.pow(left, right);
            default -> throw new IllegalArgumentException("Unknown operator: '" + op + "'");
        };
    }
}
