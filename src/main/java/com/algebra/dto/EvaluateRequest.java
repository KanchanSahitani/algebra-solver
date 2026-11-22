package com.algebra.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Request DTO for evaluating an equation.
 */
public class EvaluateRequest {

    @NotNull(message = "Variables map must not be null")
    private Map<String, Double> variables;

    public EvaluateRequest() {
    }

    public EvaluateRequest(Map<String, Double> variables) {
        this.variables = variables;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables = variables;
    }
}
