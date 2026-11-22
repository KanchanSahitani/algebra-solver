package com.algebra.model;

/**
 * Represents a node in an expression (postfix) tree.
 * <p>
 * Operator nodes have left and right children.
 * Operand nodes (leaf nodes) hold a coefficient and an optional variable,
 * or just a constant number.
 * </p>
 */
public class TreeNode {

    private String value;       // operator (+, -, *, /, ^) or operand token ("3x", "2", "y")
    private TreeNode left;
    private TreeNode right;
    private boolean isOperator;

    public TreeNode(String value, boolean isOperator) {
        this.value = value;
        this.isOperator = isOperator;
    }

    // ---- Getters & Setters ----

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public void setOperator(boolean operator) {
        isOperator = operator;
    }
}
