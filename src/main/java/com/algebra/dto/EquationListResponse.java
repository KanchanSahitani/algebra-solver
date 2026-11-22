package com.algebra.dto;

import java.util.List;

/**
 * Response DTO for listing all stored equations.
 */
public class EquationListResponse {

    private List<EquationDto> equations;

    public EquationListResponse() {
    }

    public EquationListResponse(List<EquationDto> equations) {
        this.equations = equations;
    }

    public List<EquationDto> getEquations() {
        return equations;
    }

    public void setEquations(List<EquationDto> equations) {
        this.equations = equations;
    }
}
