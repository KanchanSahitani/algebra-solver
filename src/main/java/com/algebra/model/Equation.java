package com.algebra.model;

/**
 * Domain model holding the original equation string, its expression-tree root,
 * and a unique identifier.
 */
public class Equation {

    private String id;
    private String originalEquation;
    private TreeNode root; // root of the postfix (expression) tree

    public Equation() {
    }

    public Equation(String id, String originalEquation, TreeNode root) {
        this.id = id;
        this.originalEquation = originalEquation;
        this.root = root;
    }

    // ---- Getters & Setters ----

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalEquation() {
        return originalEquation;
    }

    public void setOriginalEquation(String originalEquation) {
        this.originalEquation = originalEquation;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
}
