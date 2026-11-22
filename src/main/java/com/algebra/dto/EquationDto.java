package com.algebra.dto;

/**
 * DTO for returning a stored equation's summary.
 */
public class EquationDto {

    private String equationId;
    private String equation;

    public EquationDto() {
    }

    public EquationDto(String equationId, String equation) {
        this.equationId = equationId;
        this.equation = equation;
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
}
