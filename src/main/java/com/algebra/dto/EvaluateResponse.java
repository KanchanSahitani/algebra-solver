package com.algebra.dto;

import java.util.Map;

/**
 * Response DTO after evaluating an equation.
 */
public class EvaluateResponse {

    private String equationId;
    private String equation;
    private Map<String, Double> variables;
    private double result;

    public EvaluateResponse() {
    }

    public EvaluateResponse(String equationId, String equation,
                            Map<String, Double> variables, double result) {
        this.equationId = equationId;
        this.equation = equation;
        this.variables = variables;
        this.result = result;
    }

    public String getEquationId() {
        return equationId;
    }

    public void setEquationId(String equationId) {
        this.equationId = equationId;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables = variables;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
