package com.algebra.service;

import com.algebra.model.Equation;
import com.algebra.model.TreeNode;
import com.algebra.repository.EquationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Business-logic service for storing, retrieving, and evaluating algebraic equations.
 */
@Service
public class EquationService {

    private final EquationRepository repository;
    private final ExpressionParser parser;
    private final ExpressionEvaluator evaluator;

    public EquationService(EquationRepository repository,
                           ExpressionParser parser,
                           ExpressionEvaluator evaluator) {
        this.repository = repository;
        this.parser = parser;
        this.evaluator = evaluator;
    }

    /**
     * Parse and store an equation.
     *
     * @param equationStr the infix equation string
     * @return the persisted {@link Equation}
     */
    public Equation store(String equationStr) {
        if (equationStr == null || equationStr.isBlank()) {
            throw new IllegalArgumentException("Equation must not be null or empty");
        }
        TreeNode root = parser.parse(equationStr);
        Equation equation = new Equation();
        equation.setOriginalEquation(equationStr.trim());
        equation.setRoot(root);
        return repository.save(equation);
    }

    /**
     * Retrieve all stored equations.
     */
    public List<Equation> getAll() {
        return repository.findAll();
    }

    /**
     * Find a stored equation by id.
     */
    public Optional<Equation> getById(String id) {
        return repository.findById(id);
    }

    /**
     * Evaluate a stored equation with the given variable values.
     *
     * @param id        the equation id
     * @param variables map of variable-name → value
     * @return the numeric result
     * @throws IllegalArgumentException if the equation is not found
     */
    public double evaluate(String id, Map<String, Double> variables) {
        Equation equation = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equation not found with id: " + id));
        return evaluator.evaluate(equation.getRoot(), variables);
    }

    /**
     * Reconstruct the infix representation of a stored equation from its tree.
     */
    public String toInfix(Equation equation) {
        return parser.toInfix(equation.getRoot());
    }
}
