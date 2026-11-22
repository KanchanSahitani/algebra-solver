package com.algebra.service;

import com.algebra.model.TreeNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Parses an algebraic equation string into an expression (postfix) tree.
 * <p>
 * Supported features:
 * <ul>
 *   <li>Implicit multiplication: {@code 3x} → {@code 3 * x}</li>
 *   <li>Operators: {@code +  -  *  /  ^}</li>
 *   <li>Parenthesised sub-expressions</li>
 *   <li>Unary minus at the start or after an opening parenthesis</li>
 *   <li>Multi-character variable names when separated by operators</li>
 * </ul>
 * </p>
 */
@Component
public class ExpressionParser {

    // ---------- public API ----------

    /**
     * Parse an infix algebraic expression and return the root of its expression tree.
     *
     * @param equation the infix equation string (e.g. "3x + 2y - z")
     * @return root {@link TreeNode} of the constructed expression tree
     * @throws IllegalArgumentException if the equation is null, blank, or syntactically invalid
     */
    public TreeNode parse(String equation) {
        if (equation == null || equation.isBlank()) {
            throw new IllegalArgumentException("Equation must not be null or blank");
        }

        List<String> tokens = tokenize(equation);
        validateTokens(tokens);
        List<String> postfix = infixToPostfix(tokens);
        return buildTree(postfix);
    }

    /**
     * Reconstruct a human-readable infix string from an expression tree.
     */
    public String toInfix(TreeNode node) {
        if (node == null) {
            return "";
        }
        if (!node.isOperator()) {
            return node.getValue();
        }

        String left = toInfix(node.getLeft());
        String right = toInfix(node.getRight());

        // Decide whether children need parentheses
        boolean leftParen = needsParentheses(node, node.getLeft(), true);
        boolean rightParen = needsParentheses(node, node.getRight(), false);

        StringBuilder sb = new StringBuilder();
        if (leftParen) sb.append("(");
        sb.append(left);
        if (leftParen) sb.append(")");

        sb.append(" ").append(node.getValue()).append(" ");

        if (rightParen) sb.append("(");
        sb.append(right);
        if (rightParen) sb.append(")");

        return sb.toString();
    }

    // ---------- tokenizer ----------

    List<String> tokenize(String equation) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        char[] chars = equation.toCharArray();

        while (i < chars.length) {
            char c = chars[i];

            // skip whitespace
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // operator or paren
            if (isOperatorChar(c) || c == '(' || c == ')') {
                // Handle unary minus: beginning, after '(' , or after another operator
                if (c == '-' && (tokens.isEmpty()
                        || tokens.get(tokens.size() - 1).equals("(")
                        || isOperator(tokens.get(tokens.size() - 1)))) {
                    // consume the sign and the following number/variable
                    StringBuilder sb = new StringBuilder();
                    sb.append('-');
                    i++;
                    while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
                        sb.append(chars[i++]);
                    }
                    // If the next char is a letter, it is part of the same term (e.g. -3x)
                    if (i < chars.length && Character.isLetter(chars[i])) {
                        // implicit multiplication between number part and variable part
                        String numPart = sb.toString(); // e.g. "-3"
                        StringBuilder varPart = new StringBuilder();
                        while (i < chars.length && Character.isLetter(chars[i])) {
                            varPart.append(chars[i++]);
                        }
                        // If numPart is just "-", treat it as "-1"
                        if (numPart.equals("-")) {
                            numPart = "-1";
                        }
                        addImplicitMultiplication(tokens);
                        tokens.add(numPart);
                        tokens.add("*");
                        tokens.add(varPart.toString());
                    } else {
                        // just a negative number
                        if (sb.toString().equals("-")) {
                            // bare unary minus before '(' — treat as "-1 *"
                            addImplicitMultiplication(tokens);
                            tokens.add("-1");
                            tokens.add("*");
                        } else {
                            addImplicitMultiplication(tokens);
                            tokens.add(sb.toString());
                        }
                    }
                    continue;
                }

                // closing paren followed by opening paren or operand → implicit mult handled later
                if (c == ')') {
                    tokens.add(String.valueOf(c));
                    i++;
                    continue;
                }
                if (c == '(') {
                    addImplicitMultiplication(tokens);
                    tokens.add(String.valueOf(c));
                    i++;
                    continue;
                }

                tokens.add(String.valueOf(c));
                i++;
                continue;
            }

            // number (possibly with decimal)
            if (Character.isDigit(c) || c == '.') {
                addImplicitMultiplication(tokens);
                StringBuilder sb = new StringBuilder();
                while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.')) {
                    sb.append(chars[i++]);
                }
                // implicit mult: number immediately followed by letter → "3x" → "3", "*", "x"
                if (i < chars.length && Character.isLetter(chars[i])) {
                    tokens.add(sb.toString());
                    tokens.add("*");
                    // the variable will be picked up in the next iteration
                } else {
                    tokens.add(sb.toString());
                }
                continue;
            }

            // variable / identifier
            if (Character.isLetter(c)) {
                addImplicitMultiplication(tokens);
                StringBuilder sb = new StringBuilder();
                while (i < chars.length && Character.isLetter(chars[i])) {
                    sb.append(chars[i++]);
                }
                tokens.add(sb.toString());
                // if next char is a digit (e.g. x2 meaning x*2) → not standard, ignore
                continue;
            }

            throw new IllegalArgumentException("Unexpected character: '" + c + "' at position " + i);
        }
        return tokens;
    }

    /**
     * If the last token is a closing paren, digit-ending operand, or variable,
     * and the next token is an operand/opening-paren/variable, inject '*'.
     */
    private void addImplicitMultiplication(List<String> tokens) {
        if (tokens.isEmpty()) return;
        String last = tokens.get(tokens.size() - 1);
        if (last.equals(")") || isOperand(last)) {
            tokens.add("*");
        }
    }

    // ---------- Shunting-yard → postfix ----------

    List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> opStack = new Stack<>();

        for (String token : tokens) {
            if (isOperand(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    output.add(opStack.pop());
                }
                if (opStack.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses: extra closing ')'");
                }
                opStack.pop(); // discard '('
            } else if (isOperator(token)) {
                while (!opStack.isEmpty()
                        && !opStack.peek().equals("(")
                        && comparePrecedence(opStack.peek(), token)) {
                    output.add(opStack.pop());
                }
                opStack.push(token);
            }
        }

        while (!opStack.isEmpty()) {
            String top = opStack.pop();
            if (top.equals("(")) {
                throw new IllegalArgumentException("Mismatched parentheses: extra opening '('");
            }
            output.add(top);
        }

        return output;
    }

    // ---------- tree construction from postfix ----------

    TreeNode buildTree(List<String> postfix) {
        Stack<TreeNode> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException(
                            "Invalid expression: not enough operands for operator '" + token + "'");
                }
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                TreeNode opNode = new TreeNode(token, true);
                opNode.setLeft(left);
                opNode.setRight(right);
                stack.push(opNode);
            } else {
                stack.push(new TreeNode(token, false));
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException(
                    "Invalid expression: could not reduce to a single tree (stack size = " + stack.size() + ")");
        }
        return stack.pop();
    }

    // ---------- helpers ----------

    private boolean isOperatorChar(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    boolean isOperator(String s) {
        return s.length() == 1 && isOperatorChar(s.charAt(0));
    }

    private boolean isOperand(String s) {
        return !isOperator(s) && !s.equals("(") && !s.equals(")");
    }

    private int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }

    /**
     * Returns true if stackTop should be popped before pushing current.
     * All operators are left-associative except '^' which is right-associative.
     */
    private boolean comparePrecedence(String stackTop, String current) {
        int stPrec = precedence(stackTop);
        int curPrec = precedence(current);
        if (current.equals("^")) {
            return stPrec > curPrec; // right-associative
        }
        return stPrec >= curPrec; // left-associative
    }

    private boolean needsParentheses(TreeNode parent, TreeNode child, boolean isLeft) {
        if (child == null || !child.isOperator()) {
            return false;
        }
        int parentPrec = precedence(parent.getValue());
        int childPrec = precedence(child.getValue());

        if (childPrec < parentPrec) {
            return true;
        }
        // For right child of '-' or '/', same-precedence needs parens
        if (!isLeft && childPrec == parentPrec
                && (parent.getValue().equals("-") || parent.getValue().equals("/"))) {
            return true;
        }
        return false;
    }

    // ---------- validation ----------

    private void validateTokens(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Equation produced no tokens");
        }

        int parenDepth = 0;
        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i);
            if (t.equals("(")) {
                parenDepth++;
            } else if (t.equals(")")) {
                parenDepth--;
                if (parenDepth < 0) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            }
        }
        if (parenDepth != 0) {
            throw new IllegalArgumentException("Mismatched parentheses");
        }

        // Must not start or end with a binary operator (unary already handled in tokenizer)
        String first = tokens.get(0);
        String last = tokens.get(tokens.size() - 1);
        if (isOperator(first)) {
            throw new IllegalArgumentException("Expression starts with operator '" + first + "'");
        }
        if (isOperator(last)) {
            throw new IllegalArgumentException("Expression ends with operator '" + last + "'");
        }
    }
}
